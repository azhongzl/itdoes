package com.itdoes.common.extension.codegenerator.entity;

import java.util.List;

import com.itdoes.common.core.util.PropertiesLoader;
import com.itdoes.common.core.util.TxtLoader;

/**
 * @author Jalen Zhong
 */
public class EntityGeneratorHelper {
	private static final String DEFAULT_OUTPUT_DIR = "/tmp/codegenerator/entity";
	private static final String CONFIG_DIR = "classpath:/codegenerator/entity/";
	private static final String DB_SKIP_FILE = CONFIG_DIR + "db.skip.ini";
	private static final String DB_MAPPING_FILE = CONFIG_DIR + "db.mapping.properties";
	private static final String DB_PERM_FILE = CONFIG_DIR + "db.perm.properties";
	private static final String DB_SEARCH_FILE = CONFIG_DIR + "db.search.properties";
	private static final String DB_UPLOAD_FILE = CONFIG_DIR + "db.upload.ini";
	private static final String DB_ENTITY_EXTENSION_FILE = CONFIG_DIR + "db.entity.extension.properties";
	private static final String ENTITY_QUERY_CACHE_FILE = CONFIG_DIR + "entity.queryCache.properties";
	private static final String ENTITY_EHCACHE_FILE = CONFIG_DIR + "entity.ehcache.properties";

	private static String getColumnKey(String tableName, String columnName) {
		return tableName + "." + columnName;
	}

	private static String getValue(PropertiesLoader pl, String key, String defaultKey, boolean useDefaultIfNotDefined) {
		if (!pl.containsKey(key)) {
			return useDefaultIfNotDefined ? pl.getStringMay(defaultKey, null) : null;
		}

		final String value = pl.getStringMay(key, null);
		if (value == null) {
			return null;
		}

		final String valueFromValue = pl.getStringMay(value, null);
		return valueFromValue != null ? valueFromValue : value;
	}

	private static class FileDbSkipConfig implements DbSkipConfig {
		private final List<String> lineList;

		public FileDbSkipConfig(TxtLoader tl) {
			this.lineList = tl.getLineList();
		}

		@Override
		public boolean isTableSkip(String tableName) {
			return lineList.contains(tableName);
		}
	}

	private static class FileDbMappingConfig implements DbMappingConfig {
		private final PropertiesLoader pl;

		public FileDbMappingConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public String getEntity(String tableName) {
			return pl.getStringMay(tableName, null);
		}

		@Override
		public String getField(String tableName, String columnName) {
			return pl.getStringMay(getColumnKey(tableName, columnName), null);
		}
	}

	private static class FileDbPermConfig implements DbPermConfig {
		private static final String DEFAULT_ENTITY = "_default_entity_";
		private static final String DEFAULT_FIELD = "_default_field_";

		private final PropertiesLoader pl;

		public FileDbPermConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public String getEntityPerm(String tableName) {
			return getValue(pl, tableName, DEFAULT_ENTITY, true);
		}

		@Override
		public String getFieldPerm(String tableName, String columnName) {
			return getValue(pl, getColumnKey(tableName, columnName), DEFAULT_FIELD, false);
		}
	}

	private static class FileDbSearchConfig implements DbSearchConfig {
		private static final String DEFAULT_ENTITY = "_default_entity_";
		private static final String DEFAULT_FIELD = "_default_field_";

		private final PropertiesLoader pl;

		public FileDbSearchConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public String getEntitySearch(String tableName) {
			return getValue(pl, tableName, DEFAULT_ENTITY, false);
		}

		@Override
		public String getFieldSearch(String tableName, String columnName) {
			return getValue(pl, getColumnKey(tableName, columnName), DEFAULT_FIELD, false);
		}
	}

	private static class FileDbUploadConfig implements DbUploadConfig {
		private final List<String> lineList;

		public FileDbUploadConfig(TxtLoader tl) {
			this.lineList = tl.getLineList();
		}

		@Override
		public boolean isFieldUpload(String tableName, String columnName) {
			return lineList.contains(getColumnKey(tableName, columnName));
		}
	}

	private static class FileDbEntityExtensionConfig implements DbEntityExtensionConfig {
		private final PropertiesLoader pl;

		public FileDbEntityExtensionConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public String getEntityExtension(String tableName) {
			return pl.getStringMay(tableName, null);
		}
	}

	private static class FileEntityQueryCacheConfig implements EntityQueryCacheConfig {
		private static final String DEFAULT_QUERY_CACHE = "_default_";

		private final PropertiesLoader pl;

		public FileEntityQueryCacheConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public boolean isEnabled(String entityClassName) {
			return pl.getBooleanMust(new String[] { entityClassName, DEFAULT_QUERY_CACHE });
		}
	}

	private static class FileEntityEhcacheConfig implements EntityEhcacheConfig {
		private static final String DEFAULT_CACHE_TEMPLATE = "_default_cache_template_";
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
			final String cache = getValue(pl, entityClassName, DEFAULT_CACHE_TEMPLATE, true);
			if (cache == null) {
				return null;
			}

			return cache.replace(CACHE_NAME_PLACEHOLDER, entityPackageName + "." + entityClassName);
		}
	}

	public static void generateEntities(String basePackageName, String idGeneratedValue) {
		generateEntities(DEFAULT_OUTPUT_DIR, basePackageName, idGeneratedValue);
	}

	public static void generateEntities(String outputDir, String basePackageName, String idGeneratedValue) {
		final PropertiesLoader pl = new PropertiesLoader("classpath:/application.properties",
				"classpath:/application.local.properties");
		final DbSkipConfig dbSkipConfig = new FileDbSkipConfig(new TxtLoader(DB_SKIP_FILE));
		final DbMappingConfig dbMappingConfig = new FileDbMappingConfig(new PropertiesLoader(DB_MAPPING_FILE));
		final DbPermConfig dbPermConfig = new FileDbPermConfig(new PropertiesLoader(DB_PERM_FILE));
		final DbSearchConfig dbSearchConfig = new FileDbSearchConfig(new PropertiesLoader(DB_SEARCH_FILE));
		final DbUploadConfig dbUploadConfig = new FileDbUploadConfig(new TxtLoader(DB_UPLOAD_FILE));
		final FileDbEntityExtensionConfig dbEntityExtensionConfig = new FileDbEntityExtensionConfig(
				new PropertiesLoader(DB_ENTITY_EXTENSION_FILE));
		final EntityQueryCacheConfig entityQueryCacheConfig = new FileEntityQueryCacheConfig(
				new PropertiesLoader(ENTITY_QUERY_CACHE_FILE));
		final EntityEhcacheConfig entityEhcacheConfig = new FileEntityEhcacheConfig(
				new PropertiesLoader(ENTITY_EHCACHE_FILE));

		EntityGenerator.generateEntities(pl.getStringMust("jdbc.driver"), pl.getStringMust("jdbc.url"),
				pl.getStringMust("jdbc.username"), pl.getStringMust("jdbc.password"), outputDir, basePackageName,
				idGeneratedValue, dbSkipConfig, dbMappingConfig, dbPermConfig, dbSearchConfig, dbUploadConfig,
				dbEntityExtensionConfig, entityQueryCacheConfig, entityEhcacheConfig);
	}
}
