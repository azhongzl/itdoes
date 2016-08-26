package com.itdoes.common.core.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jalen Zhong
 */
public class ThreadLocalMap {
	private static final ThreadLocal<Map<Object, Object>> THREAD_LOCAL_MAP = new ThreadLocal<Map<Object, Object>>() {
		@Override
		protected Map<Object, Object> initialValue() {
			return new HashMap<Object, Object>();
		}
	};

	public static void put(Object key, Object value) {
		THREAD_LOCAL_MAP.get().put(key, value);
	}

	public static Object get(Object key) {
		return THREAD_LOCAL_MAP.get().get(key);
	}

	public static void clear() {
		THREAD_LOCAL_MAP.get().clear();
	}

	private ThreadLocalMap() {
	}
}
