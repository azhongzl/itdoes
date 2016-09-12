package com.itdoes.common.extension.codegenerator.entity;

import java.util.List;
import java.util.Map;

import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.PropertiesLoader;
import com.itdoes.common.core.util.TxtLoader;
import com.itdoes.common.extension.codegenerator.entity.EhcacheModel.Cache;
import com.itdoes.common.extension.codegenerator.entity.EhcacheModel.DefaultCache;
import com.itdoes.common.extension.codegenerator.entity.EhcacheModel.DiskStore;
import com.itdoes.common.extension.codegenerator.entity.EhcacheModel.Persistence;

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

	private static class PropertiesQueryCacheConfig implements QueryCacheConfig {
		private final PropertiesLoader pl;

		public PropertiesQueryCacheConfig(PropertiesLoader pl) {
			this.pl = pl;
		}

		@Override
		public boolean isEnabled(String entityClassName) {
			return pl.getBoolean(
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
			final String name = pl.getString("name");
			final DiskStore diskStore = new DiskStore(pl.getString("diskStore.path"));
			final DefaultCache defaultCache = new DefaultCache(pl.getString("defaultCache.maxEntriesLocalHeap"),
					pl.getString("defaultCache.maxEntriesLocalDisk"), pl.getString("defaultCache.eternal"),
					pl.getString("defaultCache.timeToIdleSeconds"), pl.getString("defaultCache.timeToLiveSeconds"),
					new Persistence(pl.getString("defaultCache.persistence.strategy"),
							pl.getString("defaultCache.persistence.synchronousWrites", null)));
			final Cache standardQueryCache = new Cache("org.hibernate.cache.internal.StandardQueryCache",
					pl.getString("StandardQueryCache.maxEntriesLocalHeap"),
					pl.getString("StandardQueryCache.maxEntriesLocalDisk"), pl.getString("StandardQueryCache.eternal"),
					pl.getString("StandardQueryCache.timeToIdleSeconds"),
					pl.getString("StandardQueryCache.timeToLiveSeconds"),
					new Persistence(pl.getString("StandardQueryCache.persistence.strategy"),
							pl.getString("StandardQueryCache.persistence.synchronousWrites", null)));
			final Cache updateTimestampsCache = new Cache("org.hibernate.cache.spi.UpdateTimestampsCache",
					pl.getString("UpdateTimestampsCache.maxEntriesLocalHeap"),
					pl.getString("UpdateTimestampsCache.maxEntriesLocalDisk"),
					pl.getString("UpdateTimestampsCache.eternal"),
					pl.getString("UpdateTimestampsCache.timeToIdleSeconds"),
					pl.getString("UpdateTimestampsCache.timeToLiveSeconds"),
					new Persistence(pl.getString("UpdateTimestampsCache.persistence.strategy"),
							pl.getString("UpdateTimestampsCache.persistence.synchronousWrites", null)));
			return new EhcacheModel(name, diskStore, defaultCache, standardQueryCache, updateTimestampsCache);
		}

		@Override
		public Cache newCache(String entityPackageName, String entityClassName) {
			final Persistence persisence = new Persistence(
					mapEhcacheCacheValue(entityClassName, "persistence.strategy"),
					mapEhcacheCacheValue(entityClassName, "persistence.synchronousWrites", null));
			final Cache cache = new Cache(entityPackageName + "." + entityClassName,
					mapEhcacheCacheValue(entityClassName, "maxEntriesLocalHeap"),
					mapEhcacheCacheValue(entityClassName, "maxEntriesLocalDisk"),
					mapEhcacheCacheValue(entityClassName, "eternal"),
					mapEhcacheCacheValue(entityClassName, "timeToIdleSeconds"),
					mapEhcacheCacheValue(entityClassName, "timeToLiveSeconds"), persisence);
			return cache;
		}

		private String mapEhcacheCacheValue(String entityClassName, String key) {
			return pl.getString(new String[] { "cache." + entityClassName + "." + key, "templateCache." + key });
		}

		private String mapEhcacheCacheValue(String entityClassName, String key, String defaultValue) {
			return pl.getString(new String[] { "cache." + entityClassName + "." + key, "templateCache." + key },
					defaultValue);
		}
	}

	public static void generateEntities(String basePackageName, String idGeneratedValue) {
		final PropertiesLoader pl = new PropertiesLoader("classpath:/application.properties",
				"classpath:/application.local.properties");
		final Map<String, String> tableMapping = toMap(TABLE_MAPPING_FILE);
		final List<String> tableSkipList = toList(TABLE_SKIP_FILE);
		final Map<String, String> columnMapping = toMap(COLUMN_MAPPING_FILE);
		final List<String> secureColumnList = toList(COLUMN_SECURE_FILE);
		final QueryCacheConfig queryCacheConfig = new PropertiesQueryCacheConfig(
				new PropertiesLoader(QUERY_CACHE_FILE));
		final EhcacheConfig ehcacheConfig = new PropertiesEhcacheConfig(new PropertiesLoader(EHCACHE_FILE));

		EntityGenerator.generateEntities(pl.getString("jdbc.driver"), pl.getString("jdbc.url"),
				pl.getString("jdbc.username"), pl.getString("jdbc.password"), OUTPUT_DIR, basePackageName, tableMapping,
				tableSkipList, columnMapping, secureColumnList, idGeneratedValue, queryCacheConfig, ehcacheConfig);
	}

	private static Map<String, String> toMap(String propertyFilename) {
		return Collections3.toMap(new PropertiesLoader(propertyFilename).getProperties());
	}

	private static List<String> toList(String propertyFilename) {
		return new TxtLoader(propertyFilename).getLineList();
	}
}
