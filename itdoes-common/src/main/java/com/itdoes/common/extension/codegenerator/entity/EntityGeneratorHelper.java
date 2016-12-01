package com.itdoes.common.extension.codegenerator.entity;

import java.util.List;

import com.itdoes.common.core.util.PropertiesLoader;
import com.itdoes.common.core.util.TxtLoader;
import com.itdoes.common.extension.codegenerator.entity.config.ConstraintConfig;
import com.itdoes.common.extension.codegenerator.entity.config.EhcacheConfig;
import com.itdoes.common.extension.codegenerator.entity.config.MappingConfig;
import com.itdoes.common.extension.codegenerator.entity.config.PermConfig;
import com.itdoes.common.extension.codegenerator.entity.config.QueryCacheConfig;
import com.itdoes.common.extension.codegenerator.entity.config.SearchConfig;
import com.itdoes.common.extension.codegenerator.entity.config.SkipConfig;
import com.itdoes.common.extension.codegenerator.entity.config.UploadConfig;
import com.itdoes.common.extension.codegenerator.entity.model.EhcacheModel;

/**
 * @author Jalen Zhong
 */
public class EntityGeneratorHelper {
	private static final String DEFAULT_OUTPUT_DIR = "/tmp/codegenerator/entity";
	private static final String CONFIG_DIR = "classpath:/codegenerator/entity/";

	private static final String SKIP_FILE = CONFIG_DIR + "skip.ini";
	private static final String MAPPING_FILE = CONFIG_DIR + "mapping.properties";
	private static final String CONSTRAINT_FILE = CONFIG_DIR + "constraint.properties";
	private static final String PERM_FILE = CONFIG_DIR + "perm.properties";
	private static final String SEARCH_FILE = CONFIG_DIR + "search.properties";
	private static final String UPLOAD_FILE = CONFIG_DIR + "upload.properties";
	private static final String QUERY_CACHE_FILE = CONFIG_DIR + "queryCache.properties";
	private static final String EHCACHE_FILE = CONFIG_DIR + "ehcache.properties";

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

	private static class FileSkipConfig implements SkipConfig {
		private final List<String> lineList;

		public FileSkipConfig(TxtLoader tl) {
			this.lineList = tl.getLineList();
		}

		@Override
		public boolean isTableSkip(String tableName) {
			return lineList.contains(tableName);
		}
	}

	private static class FileMappingConfig implements MappingConfig {
		private final PropertiesLoader pl;

		public FileMappingConfig(PropertiesLoader pl) {
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

	private static class FileConstraintConfig implements ConstraintConfig {
		private final PropertiesLoader pl;

		public FileConstraintConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public String getFieldConstraint(String tableName, String columnName) {
			return pl.getStringMay(getColumnKey(tableName, columnName), null);
		}
	}

	private static class FilePermConfig implements PermConfig {
		private static final String DEFAULT_ENTITY = "_default_entity_";
		private static final String DEFAULT_FIELD = "_default_field_";

		private final PropertiesLoader pl;

		public FilePermConfig(PropertiesLoader pl) {
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

	private static class FileSearchConfig implements SearchConfig {
		private static final String DEFAULT_ENTITY = "_default_entity_";
		private static final String DEFAULT_FIELD = "_default_field_";

		private final PropertiesLoader pl;

		public FileSearchConfig(PropertiesLoader pl) {
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

	private static class FileUploadConfig implements UploadConfig {
		private static final String DEFAULT_FIELD = "_default_field_";

		private final PropertiesLoader pl;

		public FileUploadConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public String getFieldUpload(String tableName, String columnName) {
			return getValue(pl, getColumnKey(tableName, columnName), DEFAULT_FIELD, false);
		}
	}

	private static class FileQueryCacheConfig implements QueryCacheConfig {
		private static final String DEFAULT_QUERY_CACHE = "_default_";

		private final PropertiesLoader pl;

		public FileQueryCacheConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public boolean isEntityEnabled(String tableName) {
			return pl.getBooleanMust(new String[] { tableName, DEFAULT_QUERY_CACHE });
		}
	}

	private static class FileEhcacheConfig implements EhcacheConfig {
		private static final String DEFAULT_CACHE_TEMPLATE = "_default_cache_template_";
		private static final String CACHE_NAME_PLACEHOLDER = "_cache_name_placeholder_";

		private final PropertiesLoader pl;

		public FileEhcacheConfig(PropertiesLoader pl) {
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
		public String newEntityCache(String tableName, String entityPackageName, String entityClassName) {
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
		final SkipConfig skipConfig = new FileSkipConfig(new TxtLoader(SKIP_FILE));
		final MappingConfig mappingConfig = new FileMappingConfig(new PropertiesLoader(MAPPING_FILE));
		final ConstraintConfig constraintConfig = new FileConstraintConfig(new PropertiesLoader(CONSTRAINT_FILE));
		final PermConfig permConfig = new FilePermConfig(new PropertiesLoader(PERM_FILE));
		final SearchConfig searchConfig = new FileSearchConfig(new PropertiesLoader(SEARCH_FILE));
		final UploadConfig uploadConfig = new FileUploadConfig(new PropertiesLoader(UPLOAD_FILE));
		final QueryCacheConfig queryCacheConfig = new FileQueryCacheConfig(new PropertiesLoader(QUERY_CACHE_FILE));
		final EhcacheConfig ehcacheConfig = new FileEhcacheConfig(new PropertiesLoader(EHCACHE_FILE));

		EntityGenerator.generateEntities(pl.getStringMust("jdbc.driver"), pl.getStringMust("jdbc.url"),
				pl.getStringMust("jdbc.username"), pl.getStringMust("jdbc.password"), loaderClass, outputDir,
				basePackageName, idGeneratedValue, skipConfig, mappingConfig, constraintConfig, permConfig,
				searchConfig, uploadConfig, queryCacheConfig, ehcacheConfig);
	}
}
