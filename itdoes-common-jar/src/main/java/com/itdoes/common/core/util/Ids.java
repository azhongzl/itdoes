package com.itdoes.common.core.util;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author Jalen Zhong
 */
public class Ids {
	private static final SecureRandom RANDOM = new SecureRandom();

	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	public static String uuidWithoutHyphen() {
		return uuid().replaceAll("-", "");
	}

	public static long randomLongAll() {
		return RANDOM.nextLong();
	}

	public static long randomLongAbs() {
		return Math.abs(randomLongAll());
	}

	public static String randomBase62(int length) {
		final byte[] randomBytes = new byte[length];
		RANDOM.nextBytes(randomBytes);
		return Codecs.base62Encode(randomBytes);
	}

	private Ids() {
	}
}
