package com.itdoes.common.extension.codegenerator.entity.model;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author Jalen Zhong
 */
public class EhcacheModel {
	private final String name;
	private final String diskStore;
	private final String defaultCache;
	private final String standardQueryCache;
	private final String updateTimestampsCache;
	private final List<String> cacheList = Lists.newArrayList();

	public EhcacheModel(String name, String diskStore, String defaultCache, String standardQueryCache,
			String updateTimestampsCache) {
		this.name = name;
		this.diskStore = diskStore;
		this.defaultCache = defaultCache;
		this.standardQueryCache = standardQueryCache;
		this.updateTimestampsCache = updateTimestampsCache;
	}

	public String getName() {
		return name;
	}

	public String getDiskStore() {
		return diskStore;
	}

	public String getDefaultCache() {
		return defaultCache;
	}

	public String getStandardQueryCache() {
		return standardQueryCache;
	}

	public String getUpdateTimestampsCache() {
		return updateTimestampsCache;
	}

	public List<String> getCacheList() {
		return cacheList;
	}

	public void addCache(String cache) {
		cacheList.add(cache);
	}
}
