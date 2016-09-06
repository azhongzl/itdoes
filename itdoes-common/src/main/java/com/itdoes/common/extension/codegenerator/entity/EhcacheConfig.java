package com.itdoes.common.extension.codegenerator.entity;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * @author Jalen Zhong
 */
public class EhcacheConfig {
	public static class EhcacheItem {
		private final String name;
		private final String maxEntriesLocalHeap;
		private final String eternal;
		private final String overflowToDisk;
		private final String maxEntriesLocalDisk;

		public EhcacheItem(String name, String maxEntriesLocalHeap, String eternal, String overflowToDisk,
				String maxEntriesLocalDisk) {
			this.name = name;
			this.maxEntriesLocalHeap = maxEntriesLocalHeap;
			this.eternal = eternal;
			this.overflowToDisk = overflowToDisk;
			this.maxEntriesLocalDisk = maxEntriesLocalDisk;
		}

		public String getName() {
			return name;
		}

		public String getMaxEntriesLocalHeap() {
			return maxEntriesLocalHeap;
		}

		public String getEternal() {
			return eternal;
		}

		public String getOverflowToDisk() {
			return overflowToDisk;
		}

		public String getMaxEntriesLocalDisk() {
			return maxEntriesLocalDisk;
		}
	}

	private final List<EhcacheItem> itemList = Lists.newArrayList();

	public EhcacheConfig() {

	}

	public List<EhcacheItem> getItemList() {
		return itemList;
	}

	public void addItem(EhcacheItem item) {
		itemList.add(item);
	}
}
