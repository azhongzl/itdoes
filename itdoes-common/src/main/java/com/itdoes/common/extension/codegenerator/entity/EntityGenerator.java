package com.itdoes.common.extension.codegenerator.entity;

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
import com.itdoes.common.core.util.Reflections;
import com.itdoes.common.core.util.Strings;
import com.itdoes.common.core.util.Urls;
import com.itdoes.common.extension.codegenerator.entity.EhcacheConfig.EhcacheItem;
import com.itdoes.common.extension.codegenerator.entity.EntityConfig.EntityField;

import freemarker.template.Template;

/**
 * @author Jalen Zhong
 */
public class EntityGenerator {
	private static final String DEFAULT_ID_GENERATED_VALUE = "@GeneratedValue(strategy = GenerationType.AUTO)";
	private static final String TEMPLATE_DIR = "classpath:/" + Reflections.packageToPath(EntityGenerator.class);

	public static void generateEntities(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword,
			String outputDir, String basePackageName, Map<String, String> tableMapping,
			Map<String, String> columnMapping, List<String> secureColumnList, String idGeneratedValue) {
		final String entityPackageName = basePackageName + ".entity";
		final String entityDir = getPackageDir(outputDir, entityPackageName);
		final Template entityTemplate = getTemplate("Entity.ftl");

		final String daoPackageName = basePackageName + ".dao";
		final String daoDir = getPackageDir(outputDir, daoPackageName);
		final Template daoTemplate = getTemplate("Dao.ftl");

		final String realIdGeneratedValue = StringUtils.isBlank(idGeneratedValue) ? DEFAULT_ID_GENERATED_VALUE
				: idGeneratedValue;

		final EhcacheConfig ehcacheConfig = new EhcacheConfig();

		final MetaParser parser = new MetaParser(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
		final List<Table> tableList = parser.parseTables();
		for (Table table : tableList) {
			final String tableName = table.getName();

			// Generate Entity
			final String entityClassName = mapEntityClassName(tableName, tableMapping);
			final EntityFieldListResult entityFieldListResult = mapEntityFieldList(tableName, table.getColumnList(),
					columnMapping, secureColumnList);
			final EntityConfig entityConfig = new EntityConfig(entityPackageName, entityFieldListResult.hasSecureColumn,
					tableName, entityClassName, getSerialVersionUIDStr(entityClassName),
					entityFieldListResult.entityFieldList, realIdGeneratedValue);
			final Map<String, Object> entityModel = Maps.newHashMap();
			entityModel.put("config", entityConfig);
			final String entityString = FreeMarkers.render(entityTemplate, entityModel);
			writeJavaFile(entityDir, entityClassName, entityString);

			// Generate Dao
			final String daoClassName = Businesses.getDaoClassName(entityClassName);
			final DaoConfig daoConfig = new DaoConfig(daoPackageName, entityPackageName, entityClassName, daoClassName,
					mapIdType(tableName, table.getColumnList()));
			final Map<String, Object> daoModel = Maps.newHashMap();
			daoModel.put("config", daoConfig);
			final String daoString = FreeMarkers.render(daoTemplate, daoModel);
			writeJavaFile(daoDir, daoClassName, daoString);

			final EhcacheItem ehcacheItem = new EhcacheItem(entityPackageName + "." + entityClassName, "10000", "false",
					"true", "100000");
			ehcacheConfig.addItem(ehcacheItem);
		}

		final String ehcacheDir = Files.toUnixPath(outputDir);
		final Template ehcacheTemplate = getTemplate("ehcache.ftl");
		final Map<String, Object> ehcacheModel = Maps.newHashMap();
		ehcacheModel.put("config", ehcacheConfig);
		final String ehcacheString = FreeMarkers.render(ehcacheTemplate, ehcacheModel);
		writeFile(ehcacheDir, "ehcache.xml", ehcacheString);
	}

	private static String getPackageDir(String outputDir, String packageName) {
		return Urls.concat(Files.toUnixPath(outputDir), Reflections.packageToPath(packageName));
	}

	private static Template getTemplate(String templateName) {
		return FreeMarkers.getTemplate(TEMPLATE_DIR, templateName);
	}

	private static String getSerialVersionUIDStr(String className) {
		return String.valueOf(className.hashCode());
	}

	private static void writeJavaFile(String dir, String className, String content) {
		writeFile(dir, getJavaFilename(className), content);
	}

	private static String getJavaFilename(String className) {
		return className + ".java";
	}

	private static void writeFile(String dir, String filename, String content) {
		try {
			FileUtils.writeStringToFile(new File(dir, filename), content, Constants.UTF8_CHARSET);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	private static String mapEntityClassName(String tableName, Map<String, String> tableMapping) {
		if (!Collections3.isEmpty(tableMapping)) {
			if (tableMapping.containsKey(tableName)) {
				return tableMapping.get(tableName);
			}
		}

		return Strings.underscoreToPascal(tableName);
	}

	private static class EntityFieldListResult {
		private List<EntityField> entityFieldList;
		private boolean hasSecureColumn;
	}

	private static EntityFieldListResult mapEntityFieldList(String tableName, List<Column> columnList,
			Map<String, String> columnMapping, List<String> secureColumnList) {
		final List<EntityField> entityFieldList = Lists.newArrayList();
		boolean hasSecureColumn = false;
		for (Column column : columnList) {
			boolean secureColumn = false;
			if (!Collections3.isEmpty(secureColumnList)) {
				if (secureColumnList.contains(getColumnKey(tableName, column.getName()))) {
					secureColumn = true;
					hasSecureColumn = true;
				}
			}

			final EntityField entityField = new EntityField(mapFieldName(tableName, column.getName(), columnMapping),
					mapFieldType(column.getType().getId()), column, secureColumn);
			entityFieldList.add(entityField);
		}

		final EntityFieldListResult entityFieldListResult = new EntityFieldListResult();
		entityFieldListResult.entityFieldList = entityFieldList;
		entityFieldListResult.hasSecureColumn = hasSecureColumn;
		return entityFieldListResult;
	}

	private static String mapFieldName(String tableName, String columnName, Map<String, String> columnMapping) {
		if (!Collections3.isEmpty(columnMapping)) {
			final String key = getColumnKey(tableName, columnName);
			if (columnMapping.containsKey(key)) {
				return columnMapping.get(key);
			}
		}

		return Strings.underscoreToCamel(columnName);
	}

	private static String getColumnKey(String tableName, String columnName) {
		return tableName + "." + columnName;
	}

	private static String mapFieldType(int sqlType) {
		final Class<?> typeClass = SqlTypes.toJavaType(sqlType);
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
		int pkSum = 0;
		String pkType = null;
		for (Column column : columnList) {
			if (column.isPk()) {
				pkSum++;
				pkType = mapFieldType(column.getType().getId());
			}
		}

		if (pkSum == 0) {
			throw new IllegalArgumentException("There is no primary key in table [" + tableName + "]");
		}
		if (pkSum > 1) {
			throw new IllegalArgumentException("There are [" + pkSum + "] primary keys in table [" + tableName
					+ "]. Every table should contain only 1 primary key");
		}

		return pkType;
	}

	private EntityGenerator() {
	}
}
