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
	private static final String DB_SKIP_FILE = CONFIG_DIR + "db.skip.ini";
	private static final String DB_MAPPING_FILE = CONFIG_DIR + "db.mapping.properties";
	private static final String DB_PERM_FILE = CONFIG_DIR + "db.perm.ini";
	private static final String DB_UPLOAD_FILE = CONFIG_DIR + "db.upload.ini";
	private static final String DB_SEARCH_FILE = CONFIG_DIR + "db.search.properties";
	private static final String ENTITY_QUERY_CACHE_FILE = CONFIG_DIR + "entity.queryCache.properties";
	private static final String ENTITY_EHCACHE_FILE = CONFIG_DIR + "entity.ehcache.properties";

	private static String getColumnKey(String tableName, String columnName) {
		return tableName + "." + columnName;
	}

	private static class FileDbSkipConfig implements DbSkipConfig {
		private final List<String> lineList;

		public FileDbSkipConfig(TxtLoader tl) {
			this.lineList = tl.getLineList();
		}

		@Override
		public boolean isSkip(String tableName) {
			return lineList.contains(tableName);
		}
	}

	private static class FileDbMappingConfig implements DbMappingConfig {
		private final PropertiesLoader pl;

		public FileDbMappingConfig(PropertiesLoader pl) {
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

	private static class FileDbPermConfig implements DbPermConfig {
		private final List<String> lineList;

		public FileDbPermConfig(TxtLoader tl) {
			this.lineList = tl.getLineList();
		}

		@Override
		public boolean isPermField(String tableName, String columnName) {
			return lineList.contains(getColumnKey(tableName, columnName));
		}
	}

	private static class FileDbUploadConfig implements DbUploadConfig {
		private final List<String> lineList;

		public FileDbUploadConfig(TxtLoader tl) {
			this.lineList = tl.getLineList();
		}

		@Override
		public boolean isUploadField(String tableName, String columnName) {
			return lineList.contains(getColumnKey(tableName, columnName));
		}
	}

	private static class FileDbSearchConfig implements DbSearchConfig {
		private static final String DEFAULT_TABLE = "_default_table_";
		private static final String DEFAULT_COLUMN = "_default_column_";

		private final PropertiesLoader pl;

		public FileDbSearchConfig(PropertiesLoader pl) {
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

	private static class FileEntityQueryCacheConfig implements EntityQueryCacheConfig {
		private final PropertiesLoader pl;

		public FileEntityQueryCacheConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public boolean isEnabled(String entityClassName) {
			return pl.getBooleanMust(
					new String[] { "queryCache." + entityClassName + ".enabled", "default.queryCache.enabled" });
		}
	}

	private static class FileEntityEhcacheConfig implements EntityEhcacheConfig {
		private static final String CACHE_NAME_PLACEHOLDER = "_cache_name_placeholder_";

		private final PropertiesLoader pl;

		public FileEntityEhcacheConfig(PropertiesLoader pl) {
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
		final DbSkipConfig dbSkipConfig = new FileDbSkipConfig(new TxtLoader(DB_SKIP_FILE));
		final DbMappingConfig dbMappingConfig = new FileDbMappingConfig(new PropertiesLoader(DB_MAPPING_FILE));
		final DbPermConfig dbPermConfig = new FileDbPermConfig(new TxtLoader(DB_PERM_FILE));
		final DbUploadConfig dbUploadConfig = new FileDbUploadConfig(new TxtLoader(DB_UPLOAD_FILE));
		final DbSearchConfig dbSearchConfig = new FileDbSearchConfig(new PropertiesLoader(DB_SEARCH_FILE));
		final EntityQueryCacheConfig entityQueryCacheConfig = new FileEntityQueryCacheConfig(
				new PropertiesLoader(ENTITY_QUERY_CACHE_FILE));
		final EntityEhcacheConfig entityEhcacheConfig = new FileEntityEhcacheConfig(
				new PropertiesLoader(ENTITY_EHCACHE_FILE));

		EntityGenerator.generateEntities(pl.getStringMust("jdbc.driver"), pl.getStringMust("jdbc.url"),
				pl.getStringMust("jdbc.username"), pl.getStringMust("jdbc.password"), OUTPUT_DIR, basePackageName,
				dbSkipConfig, dbMappingConfig, dbPermConfig, dbUploadConfig, dbSearchConfig, idGeneratedValue,
				entityQueryCacheConfig, entityEhcacheConfig);
	}
}
