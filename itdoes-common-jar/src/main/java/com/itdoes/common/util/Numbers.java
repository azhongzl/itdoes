package com.itdoes.common.util;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

/**
 * @author Jalen Zhong
 */
public class Numbers {
	public static byte[] longToBytes(long value) {
		return Longs.toByteArray(value);
	}

	public static long bytesToLong(byte[] value) {
		return Longs.fromByteArray(value);
	}

	public static byte[] intToBytes(int value) {
		return Ints.toByteArray(value);
	}

	public static int bytesToInt(byte[] value) {
		return Ints.fromByteArray(value);
	}

	private Numbers() {
	}
}
