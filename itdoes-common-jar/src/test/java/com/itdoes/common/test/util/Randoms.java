package com.itdoes.common.test.util;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Jalen Zhong
 */
public class Randoms {
	private static final Random RANDOM = new Random();

	public static long randomLong() {
		return RANDOM.nextLong();
	}

	public static String randomName(String prefix) {
		return new StringBuilder().append(prefix).append(randomLong()).toString();
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

	public static <T> List<T> randomAny(List<T> list) {
		return randomAny(list, RANDOM.nextInt(list.size()));
	}

	private Randoms() {
	}
}