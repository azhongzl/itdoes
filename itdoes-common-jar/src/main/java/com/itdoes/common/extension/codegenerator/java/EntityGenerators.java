package com.itdoes.common.extension.codegenerator.java;

import java.io.File;
import java.io.IOException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itdoes.common.core.Constants;
import com.itdoes.common.core.freemarker.FreeMarkers;
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
public class EntityGenerators {
	private static final String DEFAULT_ID_GENERATED_VALUE = "@GeneratedValue(strategy = GenerationType.AUTO)";
	private static final String TEMPLATE_DIR = "classpath:/"
			+ EntityGenerators.class.getPackage().getName().replace(".", "/");
	private static final Map<Integer, String> TYPE_MAPPING = Maps.newHashMap();
	static {
		TYPE_MAPPING.put(Types.INTEGER, "Integer");
		TYPE_MAPPING.put(Types.BIGINT, "Long");
		TYPE_MAPPING.put(Types.DATE, "Date");
		TYPE_MAPPING.put(Types.TIME, "Date");
		TYPE_MAPPING.put(Types.TIMESTAMP, "Date");
	}

	public static void generateEntities(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword,
			String outputDir, String basePackageName, Map<String, String> tableMapping,
			Map<String, String> columnMapping, String idGeneratedValue) {
		final String formattedOutputDir = Files.toUnixPath(outputDir);

		final String entityPackageName = basePackageName + ".entity";
		final String entityDir = Urls.concat(formattedOutputDir, entityPackageName.replace(".", "/"));
		final Template entityTemplate = FreeMarkers.getTemplate(TEMPLATE_DIR, "Entity.ftl");

		final String repositoryPackageName = basePackageName + ".repository";
		final String repositoryDir = Urls.concat(formattedOutputDir, repositoryPackageName.replace(".", "/"));
		final Template repositoryTemplate = FreeMarkers.getTemplate(TEMPLATE_DIR, "Repository.ftl");

		final MetaParser parser = new MetaParser(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
		final List<Table> tableList = parser.parseTables();
		for (Table table : tableList) {
			final String tableName = table.getName();

			final String entityClassName = mapEntityClassName(tableName, tableMapping);
			final List<Field> entityFieldList = mapEntityFieldList(tableName, table.getColumnList(), columnMapping);
			final Map<String, Object> entityModel = Maps.newHashMap();
			entityModel.put("packageName", entityPackageName);
			entityModel.put("tableName", tableName);
			entityModel.put("className", entityClassName);
			entityModel.put("fieldList", entityFieldList);
			entityModel.put("idGeneratedValue",
					StringUtils.isBlank(idGeneratedValue) ? DEFAULT_ID_GENERATED_VALUE : idGeneratedValue);
			final String entityString = FreeMarkers.render(entityTemplate, entityModel);

			final String repositoryClassName = entityClassName + "Dao";
			final Map<String, Object> repositoryModel = Maps.newHashMap();
			repositoryModel.put("packageName", repositoryPackageName);
			repositoryModel.put("entityPackageName", entityPackageName);
			repositoryModel.put("entityClassName", entityClassName);
			repositoryModel.put("className", repositoryClassName);
			repositoryModel.put("entityIdType", mapIdType(tableName, table.getColumnList()));
			final String repositoryString = FreeMarkers.render(repositoryTemplate, repositoryModel);

			try {
				FileUtils.writeStringToFile(new File(entityDir, entityClassName + ".java"), entityString,
						Constants.UTF8_CHARSET);
				FileUtils.writeStringToFile(new File(repositoryDir, repositoryClassName + ".java"), repositoryString,
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

	private static List<Field> mapEntityFieldList(String tableName, List<Column> columnList,
			Map<String, String> columnMapping) {
		final List<Field> fieldList = Lists.newArrayList();
		for (Column column : columnList) {
			String fieldName = column.getName();
			if (!Collections3.isEmpty(columnMapping)) {
				final String key = tableName + "." + column.getName();
				if (columnMapping.containsKey(key)) {
					fieldName = columnMapping.get(key);
				}
			}

			final Field field = new Field(fieldName, mapFieldType(column.getType().getId()), column.isPk());
			fieldList.add(field);
		}
		return fieldList;
	}

	private static String mapFieldType(int sqlType) {
		if (TYPE_MAPPING.containsKey(sqlType)) {
			return TYPE_MAPPING.get(sqlType);
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

	private EntityGenerators() {
	}
}
