package com.itdoes.common.extension.codegenerator.java;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itdoes.common.business.Businesses;
import com.itdoes.common.core.Constants;
import com.itdoes.common.core.freemarker.FreeMarkers;
import com.itdoes.common.core.jdbc.SqlTypes;
import com.itdoes.common.core.jdbc.meta.Column;
import com.itdoes.common.core.jdbc.meta.MetaParser;
import com.itdoes.common.core.jdbc.meta.Table;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.Exceptions;
import com.itdoes.common.core.util.Files;
import com.itdoes.common.core.util.Urls;

import freemarker.template.Template;

/**
 * @author Jalen Zhong
 */
public class EntityGenerator {
	private static final String DEFAULT_ID_GENERATED_VALUE = "@GeneratedValue(strategy = GenerationType.AUTO)";
	private static final String TEMPLATE_DIR = "classpath:/"
			+ EntityGenerator.class.getPackage().getName().replace(".", "/");

	public static void generateEntities(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword,
			String outputDir, String basePackageName, Map<String, String> tableMapping,
			Map<String, String> columnMapping, List<String> secureColumnList, String idGeneratedValue) {
		final String formattedOutputDir = Files.toUnixPath(outputDir);

		final String entityPackageName = basePackageName + ".entity";
		final String entityDir = Urls.concat(formattedOutputDir, entityPackageName.replace(".", "/"));
		final Template entityTemplate = FreeMarkers.getTemplate(TEMPLATE_DIR, "Entity.ftl");

		final String daoPackageName = basePackageName + ".dao";
		final String daoDir = Urls.concat(formattedOutputDir, daoPackageName.replace(".", "/"));
		final Template daoTemplate = FreeMarkers.getTemplate(TEMPLATE_DIR, "Dao.ftl");

		final MetaParser parser = new MetaParser(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
		final List<Table> tableList = parser.parseTables();
		for (Table table : tableList) {
			final String tableName = table.getName();

			final String entityClassName = mapEntityClassName(tableName, tableMapping);
			final EntityFieldListResult entityFieldListResult = mapEntityFieldList(tableName, table.getColumnList(),
					columnMapping, secureColumnList);
			final Map<String, Object> entityModel = Maps.newHashMap();
			entityModel.put("packageName", entityPackageName);
			entityModel.put("containSecureColumn", entityFieldListResult.containSecureColumn);
			entityModel.put("tableName", tableName);
			entityModel.put("className", entityClassName);
			entityModel.put("fieldList", entityFieldListResult.entityFieldList);
			entityModel.put("idGeneratedValue",
					StringUtils.isBlank(idGeneratedValue) ? DEFAULT_ID_GENERATED_VALUE : idGeneratedValue);
			final String entityString = FreeMarkers.render(entityTemplate, entityModel);

			final String daoClassName = Businesses.getDaoClassName(entityClassName);
			final Map<String, Object> daoModel = Maps.newHashMap();
			daoModel.put("packageName", daoPackageName);
			daoModel.put("entityPackageName", entityPackageName);
			daoModel.put("entityClassName", entityClassName);
			daoModel.put("className", daoClassName);
			daoModel.put("entityIdType", mapIdType(tableName, table.getColumnList()));
			final String daoString = FreeMarkers.render(daoTemplate, daoModel);

			try {
				FileUtils.writeStringToFile(new File(entityDir, entityClassName + ".java"), entityString,
						Constants.UTF8_CHARSET);
				FileUtils.writeStringToFile(new File(daoDir, daoClassName + ".java"), daoString,
						Constants.UTF8_CHARSET);
			} catch (IOException e) {
				throw Exceptions.unchecked(e);
			}
		}
	}

	private static String mapEntityClassName(String tableName, Map<String, String> tableMapping) {
		if (!Collections3.isEmpty(tableMapping)) {
			if (tableMapping.containsKey(tableName)) {
				return tableMapping.get(tableName);
			}
		}

		return StringUtils.capitalize(tableName);
	}

	private static class EntityFieldListResult {
		private List<ColumnField> entityFieldList;
		private boolean containSecureColumn;
	}

	private static EntityFieldListResult mapEntityFieldList(String tableName, List<Column> columnList,
			Map<String, String> columnMapping, List<String> secureColumnList) {
		final List<ColumnField> entityFieldList = Lists.newArrayList();
		boolean containSecureColumn = false;
		for (Column column : columnList) {
			String fieldName = column.getName();
			final String key = tableName + "." + column.getName();

			if (!Collections3.isEmpty(columnMapping)) {
				if (columnMapping.containsKey(key)) {
					fieldName = columnMapping.get(key);
				}
			}

			boolean secureColumn = false;
			if (!Collections3.isEmpty(secureColumnList)) {
				if (secureColumnList.contains(key)) {
					secureColumn = true;
					containSecureColumn = true;
				}
			}

			final ColumnField field = new ColumnField(fieldName, mapFieldType(column.getType().getId()), column,
					secureColumn);
			entityFieldList.add(field);
		}

		final EntityFieldListResult entityFieldListResult = new EntityFieldListResult();
		entityFieldListResult.entityFieldList = entityFieldList;
		entityFieldListResult.containSecureColumn = containSecureColumn;
		return entityFieldListResult;
	}

	private static String mapFieldType(int sqlType) {
		final Class<?> typeClass = SqlTypes.fromSqlType(sqlType);
		if (typeClass != null) {
			final String typeClassName = typeClass.getName();
			if (typeClassName.startsWith("java.lang")) {
				return typeClass.getSimpleName();
			} else {
				return typeClassName;
			}
		}

		return "String";
	}

	private static String mapIdType(String tableName, List<Column> columnList) {
		for (Column column : columnList) {
			if (column.isPk()) {
				return mapFieldType(column.getType().getId());
			}
		}

		throw new IllegalArgumentException("There is no primary key in table: " + tableName);
	}

	private EntityGenerator() {
	}
}
