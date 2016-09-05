package com.itdoes.common.core.util;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.Validate;

/**
 * @author Jalen Zhong
 */
public class Randoms {
	public static final SecureRandom SECURE_RANDOM = new SecureRandom();
	public static final Random SIMPLE_RANDOM = new Random();

	public static long nextLong(Random random) {
		return random.nextLong();
	}

	public static long nextLongAbs(Random random) {
		return Math.abs(nextLong(random));
	}

	public static int nextInt(Random random) {
		return random.nextInt();
	}

	public static int nextInt(Random random, int bound) {
		return random.nextInt(bound);
	}

	public static byte[] nextBytes(Random random, int length) {
		Validate.isTrue(length > 0, "Length must be a positive integer", length);

		final byte[] bytes = new byte[length];
		random.nextBytes(bytes);
		return bytes;
	}

	public static String nextBase62(Random random, int length) {
		return Codecs.base62Encode(nextBytes(random, length));
	}

	public static String nextPrefix(Random random, String prefix, int length) {
		Validate.isTrue(length > 0, "Length must be a positive integer", length);

		return new StringBuilder().append(prefix).append(nextInt(random, (int) Math.pow(10, length))).toString();
	}

	public static <T> T randomOne(List<T> list) {
		Collections.shuffle(list);
		return list.get(0);
	}

	public static <T> List<T> randomAny(List<T> list, int size) {
		if (size < 1) {
			size = 1;
		}
		if (size > list.size()) {
			size = list.size();
		}

		Collections.shuffle(list);
		return list.subList(0, size);
	}

	public static <T> List<T> randomAny(Random random, List<T> list) {
		return randomAny(list, nextInt(random, list.size()));
	}

	private Randoms() {
	}
}
