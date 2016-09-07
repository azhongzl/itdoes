package com.itdoes.common.extension.codegenerator.entity;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author Jalen Zhong
 */
public class EhcacheConfig {
	public static class DiskStore {
		private final String path;

		public DiskStore(String path) {
			this.path = path;
		}

		public String getPath() {
			return path;
		}
	}

	public static class Persistence {
		private final String strategy;
		private final String synchronousWrites;

		public Persistence(String strategy, String synchronousWrites) {
			this.strategy = strategy;
			this.synchronousWrites = synchronousWrites;
		}

		public String getStrategy() {
			return strategy;
		}

		public String getSynchronousWrites() {
			return synchronousWrites;
		}
	}

	public static class DefaultCache {
		private final String maxEntriesLocalHeap;
		private final String maxEntriesLocalDisk;
		private final String eternal;
		private final String timeToIdleSeconds;
		private final String timeToLiveSeconds;
		private final Persistence persistence;

		public DefaultCache(String maxEntriesLocalHeap, String maxEntriesLocalDisk, String eternal,
				String timeToIdleSeconds, String timeToLiveSeconds, Persistence persistence) {
			this.maxEntriesLocalHeap = maxEntriesLocalHeap;
			this.maxEntriesLocalDisk = maxEntriesLocalDisk;
			this.eternal = eternal;
			this.timeToIdleSeconds = timeToIdleSeconds;
			this.timeToLiveSeconds = timeToLiveSeconds;
			this.persistence = persistence;
		}

		public String getMaxEntriesLocalHeap() {
			return maxEntriesLocalHeap;
		}

		public String getMaxEntriesLocalDisk() {
			return maxEntriesLocalDisk;
		}

		public String getEternal() {
			return eternal;
		}

		public String getTimeToIdleSeconds() {
			return timeToIdleSeconds;
		}

		public String getTimeToLiveSeconds() {
			return timeToLiveSeconds;
		}

		public Persistence getPersistence() {
			return persistence;
		}
	}

	public static class Cache extends DefaultCache {
		private final String name;

		public Cache(String name, String maxEntriesLocalHeap, String maxEntriesLocalDisk, String eternal,
				String timeToIdleSeconds, String timeToLiveSeconds, Persistence persistence) {
			super(maxEntriesLocalHeap, maxEntriesLocalDisk, eternal, timeToIdleSeconds, timeToLiveSeconds, persistence);
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	private final String name;
	private final DiskStore diskStore;
	private final DefaultCache defaultCache;
	private final List<Cache> cacheList = Lists.newArrayList();

	public EhcacheConfig(String name, DiskStore diskStore, DefaultCache defaultCache) {
		this.name = name;
		this.diskStore = diskStore;
		this.defaultCache = defaultCache;
	}

	public String getName() {
		return name;
	}

	public DiskStore getDiskStore() {
		return diskStore;
	}

	public DefaultCache getDefaultCache() {
		return defaultCache;
	}

	public List<Cache> getCacheList() {
		return cacheList;
	}

	public void addCache(Cache cache) {
		cacheList.add(cache);
	}
}
