package com.itdoes.common.extension.codegenerator.entity;

import java.util.List;
import java.util.Map;

import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.PropertiesLoader;
import com.itdoes.common.core.util.TxtLoader;

/**
 * @author Jalen Zhong
 */
public class EntityGeneratorHelper {
	public static final String CACHE_NAME_PLACEHOLDER = "_cache_name_placeholder_";

	private static final String OUTPUT_DIR = "/tmp/codegenerator/entity";
	private static final String CONFIG_DIR = "classpath:/codegenerator/entity/";
	private static final String TABLE_MAPPING_FILE = CONFIG_DIR + "table.mapping.properties";
	private static final String TABLE_SKIP_FILE = CONFIG_DIR + "table.skip.ini";
	private static final String COLUMN_MAPPING_FILE = CONFIG_DIR + "column.mapping.properties";
	private static final String COLUMN_SECURE_FILE = CONFIG_DIR + "column.secure.ini";
	private static final String COLUMN_UPLOAD_FILE = CONFIG_DIR + "column.upload.ini";
	private static final String QUERY_CACHE_FILE = CONFIG_DIR + "queryCache.properties";
	private static final String EHCACHE_FILE = CONFIG_DIR + "ehcache.properties";

	private static class PropertiesQueryCacheConfig implements QueryCacheConfig {
		private final PropertiesLoader pl;

		public PropertiesQueryCacheConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public boolean isEnabled(String entityClassName) {
			return pl.getBooleanMust(
					new String[] { "queryCache." + entityClassName + ".enabled", "default.queryCache.enabled" });
		}
	}

	private static class PropertiesEhcacheConfig implements EhcacheConfig {
		private final PropertiesLoader pl;

		public PropertiesEhcacheConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public EhcacheModel newModel() {
			final String name = pl.getStringMust("name");
			final String diskStore = pl.getStringMay("diskStore", null);
			final String defaultCache = pl.getStringMay("defaultCache", null);
			final String standardQueryCache = pl.getStringMay("standardQueryCache", null);
			final String updateTimestampsCache = pl.getStringMay("updateTimestampsCache", null);
			return new EhcacheModel(name, diskStore, defaultCache, standardQueryCache, updateTimestampsCache);
		}

		@Override
		public String newCache(String entityPackageName, String entityClassName) {
			final String cache = pl.getStringMust(new String[] { "cache." + entityClassName, "cacheTemplate" })
					.replace(CACHE_NAME_PLACEHOLDER, entityPackageName + "." + entityClassName);
			return cache;
		}
	}

	public static void generateEntities(String basePackageName, String idGeneratedValue) {
		final PropertiesLoader pl = new PropertiesLoader("classpath:/application.properties",
				"classpath:/application.local.properties");
		final Map<String, String> tableMapping = toMap(TABLE_MAPPING_FILE);
		final List<String> tableSkipList = toList(TABLE_SKIP_FILE);
		final Map<String, String> columnMapping = toMap(COLUMN_MAPPING_FILE);
		final List<String> secureColumnList = toList(COLUMN_SECURE_FILE);
		final List<String> uploadColumnList = toList(COLUMN_UPLOAD_FILE);
		final QueryCacheConfig queryCacheConfig = new PropertiesQueryCacheConfig(
				new PropertiesLoader(QUERY_CACHE_FILE));
		final EhcacheConfig ehcacheConfig = new PropertiesEhcacheConfig(new PropertiesLoader(EHCACHE_FILE));

		EntityGenerator.generateEntities(pl.getStringMust("jdbc.driver"), pl.getStringMust("jdbc.url"),
				pl.getStringMust("jdbc.username"), pl.getStringMust("jdbc.password"), OUTPUT_DIR, basePackageName,
				tableMapping, tableSkipList, columnMapping, secureColumnList, uploadColumnList, idGeneratedValue,
				queryCacheConfig, ehcacheConfig);
	}

	private static Map<String, String> toMap(String propertyFilename) {
		return Collections3.toMap(new PropertiesLoader(propertyFilename).getProperties());
	}

	private static List<String> toList(String propertyFilename) {
		return new TxtLoader(propertyFilename).getLineList();
	}
}
