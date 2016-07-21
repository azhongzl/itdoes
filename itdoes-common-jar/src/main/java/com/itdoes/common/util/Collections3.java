package com.itdoes.common.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author Jalen Zhong
 */
public class Collections3 {
	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	private Collections3() {
	}
}
