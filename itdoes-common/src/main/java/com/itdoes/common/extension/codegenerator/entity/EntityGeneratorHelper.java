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
	private static final String DB_CONSTRAINT_FILE = CONFIG_DIR + "db.constraint.properties";
	private static final String DB_PERM_FILE = CONFIG_DIR + "db.perm.properties";
	private static final String DB_SEARCH_FILE = CONFIG_DIR + "db.search.properties";
	private static final String DB_UPLOAD_FILE = CONFIG_DIR + "db.upload.properties";
	private static final String DB_QUERY_CACHE_FILE = CONFIG_DIR + "db.queryCache.properties";
	private static final String DB_EHCACHE_FILE = CONFIG_DIR + "db.ehcache.properties";

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

	private static class FileDbConstraintConfig implements DbConstraintConfig {
		private final PropertiesLoader pl;

		public FileDbConstraintConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public String getFieldConstraint(String tableName, String columnName) {
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
		private static final String DEFAULT_FIELD = "_default_field_";

		private final PropertiesLoader pl;

		public FileDbUploadConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public String getFieldUpload(String tableName, String columnName) {
			return getValue(pl, getColumnKey(tableName, columnName), DEFAULT_FIELD, false);
		}
	}

	private static class FileDbQueryCacheConfig implements DbQueryCacheConfig {
		private static final String DEFAULT_QUERY_CACHE = "_default_";

		private final PropertiesLoader pl;

		public FileDbQueryCacheConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public boolean isEnabled(String tableName) {
			return pl.getBooleanMust(new String[] { tableName, DEFAULT_QUERY_CACHE });
		}
	}

	private static class FileDbEhcacheConfig implements DbEhcacheConfig {
		private static final String DEFAULT_CACHE_TEMPLATE = "_default_cache_template_";
		private static final String CACHE_NAME_PLACEHOLDER = "_cache_name_placeholder_";

		private final PropertiesLoader pl;

		public FileDbEhcacheConfig(PropertiesLoader pl) {
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
		public String newCache(String tableName, String entityPackageName, String entityClassName) {
			final String cache = getValue(pl, tableName, DEFAULT_CACHE_TEMPLATE, true);
			if (cache == null) {
				return null;
			}

			return cache.replace(CACHE_NAME_PLACEHOLDER, entityPackageName + "." + entityClassName);
		}
	}

	public static void generateEntities(Class<?> loaderClass, String basePackageName, String idGeneratedValue) {
		generateEntities(loaderClass, DEFAULT_OUTPUT_DIR, basePackageName, idGeneratedValue);
	}

	public static void generateEntities(Class<?> loaderClass, String outputDir, String basePackageName,
			String idGeneratedValue) {
		final PropertiesLoader pl = new PropertiesLoader("classpath:/application.properties",
				"classpath:/application.local.properties");
		final DbSkipConfig dbSkipConfig = new FileDbSkipConfig(new TxtLoader(DB_SKIP_FILE));
		final DbMappingConfig dbMappingConfig = new FileDbMappingConfig(new PropertiesLoader(DB_MAPPING_FILE));
		final DbConstraintConfig dbConstraintConfig = new FileDbConstraintConfig(
				new PropertiesLoader(DB_CONSTRAINT_FILE));
		final DbPermConfig dbPermConfig = new FileDbPermConfig(new PropertiesLoader(DB_PERM_FILE));
		final DbSearchConfig dbSearchConfig = new FileDbSearchConfig(new PropertiesLoader(DB_SEARCH_FILE));
		final DbUploadConfig dbUploadConfig = new FileDbUploadConfig(new PropertiesLoader(DB_UPLOAD_FILE));
		final DbQueryCacheConfig entityQueryCacheConfig = new FileDbQueryCacheConfig(
				new PropertiesLoader(DB_QUERY_CACHE_FILE));
		final DbEhcacheConfig entityEhcacheConfig = new FileDbEhcacheConfig(new PropertiesLoader(DB_EHCACHE_FILE));

		EntityGenerator.generateEntities(pl.getStringMust("jdbc.driver"), pl.getStringMust("jdbc.url"),
				pl.getStringMust("jdbc.username"), pl.getStringMust("jdbc.password"), loaderClass, outputDir,
				basePackageName, idGeneratedValue, dbSkipConfig, dbMappingConfig, dbConstraintConfig, dbPermConfig,
				dbSearchConfig, dbUploadConfig, entityQueryCacheConfig, entityEhcacheConfig);
	}
}
