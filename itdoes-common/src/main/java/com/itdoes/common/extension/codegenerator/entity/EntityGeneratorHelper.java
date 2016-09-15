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
			final DiskStore diskStore = new DiskStore(pl.getStringMust("diskStore.path"));
			final DefaultCache defaultCache = new DefaultCache(pl.getStringMust("defaultCache.maxEntriesLocalHeap"),
					pl.getStringMust("defaultCache.maxEntriesLocalDisk"), pl.getStringMust("defaultCache.eternal"),
					pl.getStringMay("defaultCache.timeToIdleSeconds", null),
					pl.getStringMay("defaultCache.timeToLiveSeconds", null),
					new Persistence(pl.getStringMust("defaultCache.persistence.strategy"),
							pl.getStringMay("defaultCache.persistence.synchronousWrites", null)));
			final Cache standardQueryCache = new Cache("org.hibernate.cache.internal.StandardQueryCache",
					pl.getStringMust("StandardQueryCache.maxEntriesLocalHeap"),
					pl.getStringMust("StandardQueryCache.maxEntriesLocalDisk"),
					pl.getStringMust("StandardQueryCache.eternal"),
					pl.getStringMay("StandardQueryCache.timeToIdleSeconds", null),
					pl.getStringMay("StandardQueryCache.timeToLiveSeconds", null),
					new Persistence(pl.getStringMust("StandardQueryCache.persistence.strategy"),
							pl.getStringMay("StandardQueryCache.persistence.synchronousWrites", null)));
			final Cache updateTimestampsCache = new Cache("org.hibernate.cache.spi.UpdateTimestampsCache",
					pl.getStringMust("UpdateTimestampsCache.maxEntriesLocalHeap"),
					pl.getStringMust("UpdateTimestampsCache.maxEntriesLocalDisk"),
					pl.getStringMust("UpdateTimestampsCache.eternal"),
					pl.getStringMay("UpdateTimestampsCache.timeToIdleSeconds", null),
					pl.getStringMay("UpdateTimestampsCache.timeToLiveSeconds", null),
					new Persistence(pl.getStringMust("UpdateTimestampsCache.persistence.strategy"),
							pl.getStringMay("UpdateTimestampsCache.persistence.synchronousWrites", null)));
			return new EhcacheModel(name, diskStore, defaultCache, standardQueryCache, updateTimestampsCache);
		}

		@Override
		public Cache newCache(String entityPackageName, String entityClassName) {
			final Persistence persisence = new Persistence(getMustCacheValue(entityClassName, "persistence.strategy"),
					getMayCacheValue(entityClassName, "persistence.synchronousWrites", null));
			final Cache cache = new Cache(entityPackageName + "." + entityClassName,
					getMustCacheValue(entityClassName, "maxEntriesLocalHeap"),
					getMustCacheValue(entityClassName, "maxEntriesLocalDisk"),
					getMustCacheValue(entityClassName, "eternal"),
					getMayCacheValue(entityClassName, "timeToIdleSeconds", null),
					getMayCacheValue(entityClassName, "timeToLiveSeconds", null), persisence);
			return cache;
		}

		private String getMustCacheValue(String entityClassName, String key) {
			return pl.getStringMust(getCacheKeys(entityClassName, key));
		}

		private String getMayCacheValue(String entityClassName, String key, String defaultValue) {
			return pl.getStringMay(getCacheKeys(entityClassName, key), defaultValue);
		}

		private static String[] getCacheKeys(String entityClassName, String key) {
			return new String[] { "cache." + entityClassName + "." + key, "templateCache." + key };
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

		EntityGenerator.generateEntities(pl.getStringMust("jdbc.driver"), pl.getStringMust("jdbc.url"),
				pl.getStringMust("jdbc.username"), pl.getStringMust("jdbc.password"), OUTPUT_DIR, basePackageName,
				tableMapping, tableSkipList, columnMapping, secureColumnList, idGeneratedValue, queryCacheConfig,
				ehcacheConfig);
	}

	private static Map<String, String> toMap(String propertyFilename) {
		return Collections3.toMap(new PropertiesLoader(propertyFilename).getProperties());
	}

	private static List<String> toList(String propertyFilename) {
		return new TxtLoader(propertyFilename).getLineList();
	}
}
