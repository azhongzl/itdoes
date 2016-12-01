package com.itdoes.common.core.util;

/**
 * @author Jalen Zhong
 */
public class Objects {
	public static boolean isEqual(Object a, Object b) {
		return a == null ? b == null : a.equals(b);
	}

	private Objects() {
	}
}
