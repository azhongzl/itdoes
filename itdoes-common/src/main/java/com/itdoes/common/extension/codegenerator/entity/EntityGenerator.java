package com.itdoes.common.extension.codegenerator.entity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
import com.itdoes.common.extension.codegenerator.entity.EhcacheConfig.Cache;
import com.itdoes.common.extension.codegenerator.entity.EhcacheConfig.DefaultCache;
import com.itdoes.common.extension.codegenerator.entity.EhcacheConfig.DiskStore;
import com.itdoes.common.extension.codegenerator.entity.EhcacheConfig.Persistence;
import com.itdoes.common.extension.codegenerator.entity.EntityConfig.EntityField;

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
			Map<String, Boolean> queryCacheMap, Map<String, String> ehcacheMap) {
		final Configuration freeMarkerConfig = FreeMarkers.buildConfiguration(TEMPLATE_DIR);

		final String entityPackageName = basePackageName + ".entity";
		final String entityDir = getPackageDir(outputDir, entityPackageName);
		final Template entityTemplate = getTemplate(freeMarkerConfig, "Entity.ftl");

		final String daoPackageName = basePackageName + ".dao";
		final String daoDir = getPackageDir(outputDir, daoPackageName);
		final Template daoTemplate = getTemplate(freeMarkerConfig, "Dao.ftl");

		final String realIdGeneratedValue = StringUtils.isBlank(idGeneratedValue) ? DEFAULT_ID_GENERATED_VALUE
				: idGeneratedValue;

		final EhcacheConfig ehcacheConfig = mapEhcacheConfig(ehcacheMap);

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
			final EntityConfig entityConfig = new EntityConfig(entityPackageName, tableName, entityClassName,
					getSerialVersionUIDStr(entityClassName), entityFieldList, realIdGeneratedValue);
			final Map<String, Object> entityModel = Maps.newHashMap();
			entityModel.put("config", entityConfig);
			final String entityString = FreeMarkers.render(entityTemplate, entityModel);
			writeJavaFile(entityDir, entityClassName, entityString);

			// Generate Dao
			final String daoClassName = Env.getDaoClassName(entityClassName);
			final boolean queryCacheEnabled = mapQueryCacheEnabled(entityClassName, queryCacheMap);
			final DaoConfig daoConfig = new DaoConfig(daoPackageName, queryCacheEnabled, entityPackageName,
					entityClassName, daoClassName, mapIdType(tableName, table.getColumnList()));
			final Map<String, Object> daoModel = Maps.newHashMap();
			daoModel.put("config", daoConfig);
			final String daoString = FreeMarkers.render(daoTemplate, daoModel);
			writeJavaFile(daoDir, daoClassName, daoString);

			final Cache cache = mapEhcacheCache(entityPackageName, entityClassName, ehcacheMap);
			ehcacheConfig.addCache(cache);
		}

		final String ehcacheDir = Files.toUnixPath(outputDir);
		final Template ehcacheTemplate = getTemplate(freeMarkerConfig, "ehcache.ftl");
		final Map<String, Object> ehcacheModel = Maps.newHashMap();
		ehcacheModel.put("config", ehcacheConfig);
		final String ehcacheString = FreeMarkers.render(ehcacheTemplate, ehcacheModel);
		writeFile(ehcacheDir, ehcacheConfig.getName() + ".xml", ehcacheString);
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

	private static boolean mapQueryCacheEnabled(String entityClassName, Map<String, Boolean> queryCacheMap) {
		final Boolean value = queryCacheMap.get("queryCache." + entityClassName + ".enabled");
		return value != null ? value : queryCacheMap.get("default.queryCache.enabled");
	}

	private static EhcacheConfig mapEhcacheConfig(Map<String, String> ehcacheMap) {
		final String name = ehcacheMap.get("name");
		final DiskStore diskStore = new DiskStore(ehcacheMap.get("diskStore.path"));
		final Persistence persisence = new Persistence(ehcacheMap.get("defaultCache.persistence.strategy"),
				ehcacheMap.get("defaultCache.persistence.synchronousWrites"));
		final DefaultCache defaultCache = new DefaultCache(ehcacheMap.get("defaultCache.maxEntriesLocalHeap"),
				ehcacheMap.get("defaultCache.maxEntriesLocalDisk"), ehcacheMap.get("defaultCache.eternal"),
				ehcacheMap.get("defaultCache.timeToIdleSeconds"), ehcacheMap.get("defaultCache.timeToLiveSeconds"),
				persisence);
		final Persistence standardQueryCachePersisence = new Persistence(
				ehcacheMap.get("StandardQueryCache.persistence.strategy"),
				ehcacheMap.get("StandardQueryCache.persistence.synchronousWrites"));
		final Cache standardQueryCache = new Cache("org.hibernate.cache.internal.StandardQueryCache",
				ehcacheMap.get("StandardQueryCache.maxEntriesLocalHeap"),
				ehcacheMap.get("StandardQueryCache.maxEntriesLocalDisk"), ehcacheMap.get("StandardQueryCache.eternal"),
				ehcacheMap.get("StandardQueryCache.timeToIdleSeconds"),
				ehcacheMap.get("StandardQueryCache.timeToLiveSeconds"), standardQueryCachePersisence);
		final Persistence updateTimestampsCachePersisence = new Persistence(
				ehcacheMap.get("UpdateTimestampsCache.persistence.strategy"),
				ehcacheMap.get("UpdateTimestampsCache.persistence.synchronousWrites"));
		final Cache updateTimestampsCache = new Cache("org.hibernate.cache.spi.UpdateTimestampsCache",
				ehcacheMap.get("UpdateTimestampsCache.maxEntriesLocalHeap"),
				ehcacheMap.get("UpdateTimestampsCache.maxEntriesLocalDisk"),
				ehcacheMap.get("UpdateTimestampsCache.eternal"),
				ehcacheMap.get("UpdateTimestampsCache.timeToIdleSeconds"),
				ehcacheMap.get("UpdateTimestampsCache.timeToLiveSeconds"), updateTimestampsCachePersisence);
		return new EhcacheConfig(name, diskStore, defaultCache, standardQueryCache, updateTimestampsCache);
	}

	private static Cache mapEhcacheCache(String entityPackageName, String entityClassName,
			Map<String, String> ehcacheMap) {
		final Persistence persisence = new Persistence(
				mapEhcacheCacheValue(entityPackageName, entityClassName, "persistence.strategy", ehcacheMap),
				mapEhcacheCacheValue(entityPackageName, entityClassName, "persistence.synchronousWrites", ehcacheMap));
		final Cache cache = new Cache(entityPackageName + "." + entityClassName,
				mapEhcacheCacheValue(entityPackageName, entityClassName, "maxEntriesLocalHeap", ehcacheMap),
				mapEhcacheCacheValue(entityPackageName, entityClassName, "maxEntriesLocalDisk", ehcacheMap),
				mapEhcacheCacheValue(entityPackageName, entityClassName, "eternal", ehcacheMap),
				mapEhcacheCacheValue(entityPackageName, entityClassName, "timeToIdleSeconds", ehcacheMap),
				mapEhcacheCacheValue(entityPackageName, entityClassName, "timeToLiveSeconds", ehcacheMap), persisence);
		return cache;
	}

	private static String mapEhcacheCacheValue(String entityPackageName, String entityClassName, String key,
			Map<String, String> ehcacheMap) {
		final String value = ehcacheMap.get("cache." + entityClassName + "." + key);
		return value != null ? value : ehcacheMap.get("templateCache." + key);
	}

	private EntityGenerator() {
	}
}
