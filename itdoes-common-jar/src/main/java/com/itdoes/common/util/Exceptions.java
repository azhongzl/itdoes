package com.itdoes.common.util;

/**
 * @author Jalen Zhong
 */
public class Exceptions {
	public static RuntimeException unchecked(Throwable t) {
		if (t instanceof RuntimeException) {
			return (RuntimeException) t;
		} else {
			return new RuntimeException(t);
		}
	}

	private Exceptions() {
	}
}
