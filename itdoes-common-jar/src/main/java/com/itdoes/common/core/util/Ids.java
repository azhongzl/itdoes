package com.itdoes.common.core.util;

import java.util.UUID;

/**
 * @author Jalen Zhong
 */
public class Ids {
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	public static String uuidNoHyphen() {
		return uuid().replaceAll("-", "");
	}

	private Ids() {
	}
}
