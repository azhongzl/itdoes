package com.itdoes.common.extension.codegenerator.entity;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.PropertiesLoader;
import com.itdoes.common.core.util.TxtLoader;

/**
 * @author Jalen Zhong
 */
public class EntityGeneratorHelper {
	private static final String OUTPUT_DIR = "/tmp/codegenerator/entity";
	private static final String CONFIG_DIR = "classpath:/codegenerator/entity/";
	private static final String TABLE_MAPPING_FILE = CONFIG_DIR + "table.mapping.properties";
	private static final String TABLE_SKIP_FILE = CONFIG_DIR + "table.skip.ini";
	private static final String COLUMN_MAPPING_FILE = CONFIG_DIR + "column.mapping.properties";
	private static final String COLUMN_SECURE_FILE = CONFIG_DIR + "column.secure.ini";
	private static final String QUERY_CACHE_FILE = CONFIG_DIR + "queryCache.properties";
	private static final String EHCACHE_FILE = CONFIG_DIR + "ehcache.properties";

	public static void generateEntities(String basePackageName, String idGeneratedValue) {
		final PropertiesLoader pl = new PropertiesLoader("classpath:/application.properties",
				"classpath:/application.local.properties");
		final Map<String, String> tableMapping = toMap(TABLE_MAPPING_FILE);
		final List<String> tableSkipList = toList(TABLE_SKIP_FILE);
		final Map<String, String> columnMapping = toMap(COLUMN_MAPPING_FILE);
		final List<String> secureColumnList = toList(COLUMN_SECURE_FILE);
		final Map<String, Boolean> queryCacheMap = toQueryCacheMap(QUERY_CACHE_FILE);
		final Map<String, String> ehcacheMap = toMap(EHCACHE_FILE);

		EntityGenerator.generateEntities(pl.getString("jdbc.driver"), pl.getString("jdbc.url"),
				pl.getString("jdbc.username"), pl.getString("jdbc.password"), OUTPUT_DIR, basePackageName, tableMapping,
				tableSkipList, columnMapping, secureColumnList, idGeneratedValue, queryCacheMap, ehcacheMap);
	}

	private static Map<String, String> toMap(String propertyFilename) {
		return Collections3.toMap(new PropertiesLoader(propertyFilename).getProperties());
	}

	private static List<String> toList(String propertyFilename) {
		return new TxtLoader(propertyFilename).getLineList();
	}

	private static Map<String, Boolean> toQueryCacheMap(String propertyFilename) {
		final Map<String, Boolean> queryCacheMap = Maps.newHashMap();
		final PropertiesLoader pl = new PropertiesLoader(propertyFilename);
		for (String key : pl.getProperties().stringPropertyNames()) {
			queryCacheMap.put(key, pl.getBoolean(key));
		}
		return queryCacheMap;
	}
}
