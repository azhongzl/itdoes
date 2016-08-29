package com.itdoes.common.core.util;

import java.security.SecureRandom;
import java.util.Random;

import org.apache.commons.lang3.Validate;

/**
 * @author Jalen Zhong
 */
public class Randoms {
	public static SecureRandoms SECURE = new SecureRandoms();
	public static SimpleRandoms SIMPLE = new SimpleRandoms();

	private static interface IRandoms {
		public long nextLong();

		public long nextLongAbs();

		public int nextInt();

		public int nextInt(int bound);

		public byte[] nextBytes(int length);

		public String nextBase62(int length);
	}

	public static class SecureRandoms implements IRandoms {
		// Holder class to defer initialization until needed.
		private static class Holder {
			private static final SecureRandom RANDOM = new SecureRandom();
		}

		private SecureRandoms() {
		}

		@Override
		public long nextLong() {
			return Randoms.nextLong(Holder.RANDOM);
		}

		@Override
		public long nextLongAbs() {
			return Randoms.nextLongAbs(Holder.RANDOM);
		}

		@Override
		public int nextInt() {
			return Randoms.nextInt(Holder.RANDOM);
		}

		@Override
		public int nextInt(int bound) {
			return Randoms.nextInt(Holder.RANDOM, bound);
		}

		@Override
		public byte[] nextBytes(int length) {
			return Randoms.nextBytes(Holder.RANDOM, length);
		}

		@Override
		public String nextBase62(int length) {
			return Randoms.nextBase62(Holder.RANDOM, length);
		}
	}

	public static class SimpleRandoms implements IRandoms {
		// Holder class to defer initialization until needed.
		private static class Holder {
			private static final Random RANDOM = new Random();
		}

		private SimpleRandoms() {
		}

		@Override
		public long nextLong() {
			return Randoms.nextLong(Holder.RANDOM);
		}

		@Override
		public long nextLongAbs() {
			return Randoms.nextLongAbs(Holder.RANDOM);
		}

		@Override
		public int nextInt() {
			return Randoms.nextInt(Holder.RANDOM);
		}

		@Override
		public int nextInt(int bound) {
			return Randoms.nextInt(Holder.RANDOM, bound);
		}

		@Override
		public byte[] nextBytes(int length) {
			return Randoms.nextBytes(Holder.RANDOM, length);
		}

		@Override
		public String nextBase62(int length) {
			return Randoms.nextBase62(Holder.RANDOM, length);
		}
	}

	private static long nextLong(Random random) {
		return random.nextLong();
	}

	private static long nextLongAbs(Random random) {
		return Math.abs(nextLong(random));
	}

	private static int nextInt(Random random) {
		return random.nextInt();
	}

	private static int nextInt(Random random, int bound) {
		return random.nextInt(bound);
	}

	private static byte[] nextBytes(Random random, int length) {
		Validate.isTrue(length > 0, "Random length must be a positive integer", length);

		final byte[] bytes = new byte[length];
		random.nextBytes(bytes);
		return bytes;
	}

	private static String nextBase62(Random random, int length) {
		final byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return Codecs.base62Encode(randomBytes);
	}

	private Randoms() {
	}
}
