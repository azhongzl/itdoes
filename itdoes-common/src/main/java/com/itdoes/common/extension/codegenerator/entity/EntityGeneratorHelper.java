package com.itdoes.common.extension.codegenerator.entity;

import java.util.List;

import com.itdoes.common.core.util.PropertiesLoader;
import com.itdoes.common.core.util.TxtLoader;

/**
 * @author Jalen Zhong
 */
public class EntityGeneratorHelper {
	private static final String OUTPUT_DIR = "/tmp/codegenerator/entity";
	private static final String CONFIG_DIR = "classpath:/codegenerator/entity/";
	private static final String DB_MAPPING_FILE = CONFIG_DIR + "db.mapping.properties";
	private static final String DB_TABLE_SKIP_FILE = CONFIG_DIR + "db.skip.ini";
	private static final String DB_COLUMN_PERM_FILE = CONFIG_DIR + "db.perm.ini";
	private static final String DB_COLUMN_UPLOAD_FILE = CONFIG_DIR + "db.upload.ini";
	private static final String ENTITY_QUERY_CACHE_FILE = CONFIG_DIR + "entity.queryCache.properties";
	private static final String ENTITY_EHCACHE_FILE = CONFIG_DIR + "entity.ehcache.properties";
	private static final String DB_SEARCH_FILE = CONFIG_DIR + "db.search.properties";

	private static String getColumnKey(String tableName, String columnName) {
		return tableName + "." + columnName;
	}

	private static class PropertiesDbMappingConfig implements DbMappingConfig {
		private final PropertiesLoader pl;

		public PropertiesDbMappingConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public String toEntity(String tableName) {
			return pl.getStringMay(tableName, null);
		}

		@Override
		public String toField(String tableName, String columnName) {
			return pl.getStringMay(getColumnKey(tableName, columnName), null);
		}
	}

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
		private static final String CACHE_NAME_PLACEHOLDER = "_cache_name_placeholder_";

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

	private static class PropertiesSearchConfig implements SearchConfig {
		private static final String DEFAULT_TABLE = "_default_table_";
		private static final String DEFAULT_COLUMN = "_default_column_";

		private final PropertiesLoader pl;

		public PropertiesSearchConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public String getTableSearchConfig(String tableName) {
			if (!pl.getProperties().containsKey(tableName)) {
				return null;
			}

			return pl.getStringMust(new String[] { tableName, DEFAULT_TABLE });
		}

		@Override
		public String getColumnSearchConfig(String tableName, String columnName) {
			final String key = tableName + "." + columnName;
			if (!pl.getProperties().containsKey(key)) {
				return null;
			}

			return pl.getStringMust(new String[] { key, DEFAULT_COLUMN });
		}
	}

	public static void generateEntities(String basePackageName, String idGeneratedValue) {
		final PropertiesLoader pl = new PropertiesLoader("classpath:/application.properties",
				"classpath:/application.local.properties");
		final DbMappingConfig dbMappingConfig = new PropertiesDbMappingConfig(new PropertiesLoader(DB_MAPPING_FILE));
		final List<String> tableSkipList = toList(DB_TABLE_SKIP_FILE);
		final List<String> permColumnList = toList(DB_COLUMN_PERM_FILE);
		final List<String> uploadColumnList = toList(DB_COLUMN_UPLOAD_FILE);
		final QueryCacheConfig queryCacheConfig = new PropertiesQueryCacheConfig(
				new PropertiesLoader(ENTITY_QUERY_CACHE_FILE));
		final EhcacheConfig ehcacheConfig = new PropertiesEhcacheConfig(new PropertiesLoader(ENTITY_EHCACHE_FILE));
		final SearchConfig searchConfig = new PropertiesSearchConfig(new PropertiesLoader(DB_SEARCH_FILE));

		EntityGenerator.generateEntities(pl.getStringMust("jdbc.driver"), pl.getStringMust("jdbc.url"),
				pl.getStringMust("jdbc.username"), pl.getStringMust("jdbc.password"), OUTPUT_DIR, basePackageName,
				dbMappingConfig, tableSkipList, permColumnList, uploadColumnList, idGeneratedValue, queryCacheConfig,
				ehcacheConfig, searchConfig);
	}

	private static List<String> toList(String propertyFilename) {
		return new TxtLoader(propertyFilename).getLineList();
	}
}
