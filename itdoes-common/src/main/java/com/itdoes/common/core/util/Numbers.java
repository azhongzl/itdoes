package com.itdoes.common.core.util;

import java.math.BigDecimal;

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

	public static int compareTo(double d1, double d2, int scale) {
		final BigDecimal bd1 = new BigDecimal(d1).setScale(scale, BigDecimal.ROUND_HALF_UP);
		final BigDecimal bd2 = new BigDecimal(d2).setScale(scale, BigDecimal.ROUND_HALF_UP);
		return bd1.compareTo(bd2);
	}

	private Numbers() {
	}
}
