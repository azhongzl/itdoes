package com.itdoes.common.extension.codegenerator.entity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.itdoes.common.business.Env;
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
import com.itdoes.common.extension.codegenerator.entity.EhcacheModel.Cache;
import com.itdoes.common.extension.codegenerator.entity.EntityModel.EntityField;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author Jalen Zhong
 */
public class EntityGenerator {
	private static final String DEFAULT_ID_GENERATED_VALUE = "@GeneratedValue(strategy = GenerationType.AUTO)";
	private static final String TEMPLATE_DIR = "classpath:/" + Reflections.packageToPath(EntityGenerator.class);

	public static void generateEntities(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword,
			String outputDir, String basePackageName, Map<String, String> tableMapping, List<String> tableSkipList,
			Map<String, String> columnMapping, List<String> secureColumnList, String idGeneratedValue,
			QueryCacheConfig queryCacheConfig, EhcacheConfig ehcacheConfig) {
		final Configuration freeMarkerConfig = FreeMarkers.buildConfiguration(TEMPLATE_DIR);

		final String entityPackageName = basePackageName + ".entity";
		final String entityDir = getPackageDir(outputDir, entityPackageName);
		final Template entityTemplate = getTemplate(freeMarkerConfig, "Entity.ftl");

		final String daoPackageName = basePackageName + ".dao";
		final String daoDir = getPackageDir(outputDir, daoPackageName);
		final Template daoTemplate = getTemplate(freeMarkerConfig, "Dao.ftl");

		final String realIdGeneratedValue = StringUtils.isBlank(idGeneratedValue) ? DEFAULT_ID_GENERATED_VALUE
				: idGeneratedValue;

		final EhcacheModel ehcacheModel = ehcacheConfig.newModel();

		final MetaParser parser = new MetaParser(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
		final List<Table> tableList = parser.parseTables();
		for (Table table : tableList) {
			final String tableName = table.getName();

			if (tableSkipList.contains(tableName)) {
				continue;
			}

			// Generate Entity
			final String entityClassName = mapEntityClassName(tableName, tableMapping);
			final List<EntityField> entityFieldList = mapEntityFieldList(tableName, table.getColumnList(),
					columnMapping, secureColumnList);
			final EntityModel entityModel = new EntityModel(entityPackageName, tableName, entityClassName,
					getSerialVersionUIDStr(entityClassName), entityFieldList, realIdGeneratedValue);
			final String entityString = FreeMarkers.render(entityTemplate, entityModel);
			writeJavaFile(entityDir, entityClassName, entityString);

			// Generate Dao
			final String daoClassName = Env.getDaoClassName(entityClassName);
			final boolean queryCacheEnabled = queryCacheConfig.isEnabled(entityClassName);
			final DaoModel daoModel = new DaoModel(daoPackageName, entityPackageName, entityClassName, daoClassName,
					queryCacheEnabled, mapIdType(tableName, table.getColumnList()));
			final String daoString = FreeMarkers.render(daoTemplate, daoModel);
			writeJavaFile(daoDir, daoClassName, daoString);

			// Generate ehcache cache
			final Cache cache = ehcacheConfig.newCache(entityPackageName, entityClassName);
			ehcacheModel.addCache(cache);
		}

		final String ehcacheDir = Files.toUnixPath(outputDir);
		final Template ehcacheTemplate = getTemplate(freeMarkerConfig, "ehcache.ftl");
		final String ehcacheString = FreeMarkers.render(ehcacheTemplate, ehcacheModel);
		writeEhcacheFile(ehcacheDir, ehcacheModel.getName(), ehcacheString);
	}

	private static String getPackageDir(String outputDir, String packageName) {
		return Urls.concat(Files.toUnixPath(outputDir), Reflections.packageToPath(packageName));
	}

	private static Template getTemplate(Configuration freeMarkerConfig, String templateName) {
		return FreeMarkers.getTemplate(freeMarkerConfig, templateName);
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

	private static void writeEhcacheFile(String dir, String ehcacheName, String content) {
		writeFile(dir, getEhcacheFilename(ehcacheName), content);
	}

	private static String getEhcacheFilename(String ehcacheName) {
		return (StringUtils.isNotBlank(ehcacheName) ? ehcacheName : "ehcache") + ".xml";
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

	private static List<EntityField> mapEntityFieldList(String tableName, List<Column> columnList,
			Map<String, String> columnMapping, List<String> secureColumnList) {
		final List<EntityField> entityFieldList = Lists.newArrayList();
		for (Column column : columnList) {
			boolean secure = false;
			if (!Collections3.isEmpty(secureColumnList)) {
				if (secureColumnList.contains(getColumnKey(tableName, column.getName()))) {
					secure = true;
				}
			}

			final EntityField entityField = new EntityField(mapFieldName(tableName, column.getName(), columnMapping),
					mapFieldType(column.getType().getId()), column, secure);
			entityFieldList.add(entityField);
		}

		return entityFieldList;
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
