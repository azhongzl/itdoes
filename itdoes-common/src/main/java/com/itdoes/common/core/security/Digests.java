package com.itdoes.common.core.security;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;

import com.google.common.hash.Hashing;
import com.itdoes.common.core.Constants;
import com.itdoes.common.core.util.Exceptions;
import com.itdoes.common.core.util.Randoms;

/**
 * @author Jalen Zhong
 */
public class Digests {
	public static final String SHA1 = "SHA-1";
	public static final String SHA256 = "SHA-256";
	public static final String MD5 = "MD5";

	private static final int DEFAULT_SALT_SIZE = 8;

	private static final int BUFFER_LENGTH = 8 * 1024;

	public static byte[] digest(String algorithm, String data) {
		return digest(algorithm, data, Constants.UTF8_CHARSET);
	}

	public static byte[] digest(String algorithm, String data, Charset charset) {
		return digest(algorithm, data.getBytes(charset));
	}

	public static byte[] digest(String algorithm, String data, byte[] salt) {
		return digest(algorithm, data, Constants.UTF8_CHARSET, salt);
	}

	public static byte[] digest(String algorithm, String data, Charset charset, byte[] salt) {
		return digest(algorithm, data.getBytes(charset), salt);
	}

	public static byte[] digest(String algorithm, String data, byte[] salt, int iterations) {
		return digest(algorithm, data, Constants.UTF8_CHARSET, salt, iterations);
	}

	public static byte[] digest(String algorithm, String data, Charset charset, byte[] salt, int iterations) {
		return digest(algorithm, data.getBytes(charset), salt, iterations);
	}

	public static byte[] digest(String algorithm, byte[] data) {
		return digest(algorithm, data, null);
	}

	public static byte[] digest(String algorithm, byte[] data, byte[] salt) {
		return digest(algorithm, data, salt, 1);
	}

	public static byte[] digest(String algorithm, byte[] data, int iterations) {
		return digest(algorithm, data, null, iterations);
	}

	public static byte[] digest(String algorithm, byte[] data, byte[] salt, int iterations) {
		try {
			final MessageDigest digest = MessageDigest.getInstance(algorithm);

			if (salt != null) {
				digest.update(salt);
			}

			byte[] result = digest.digest(data);

			for (int i = 1; i < iterations; i++) {
				digest.reset();
				result = digest.digest(result);
			}

			return result;
		} catch (NoSuchAlgorithmException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static byte[] digest(String algorithm, InputStream is) {
		try {
			final MessageDigest digest = MessageDigest.getInstance(algorithm);
			final byte[] buffer = new byte[BUFFER_LENGTH];
			int read = is.read(buffer, 0, BUFFER_LENGTH);
			while (read > -1) {
				digest.update(buffer, 0, read);
				read = is.read(buffer, 0, BUFFER_LENGTH);
			}

			return digest.digest();
		} catch (NoSuchAlgorithmException | IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static byte[] generateSalt() {
		return generateSalt(DEFAULT_SALT_SIZE);
	}

	public static byte[] generateSalt(int size) {
		return Randoms.nextBytes(Randoms.SECURE_RANDOM, size);
	}

	public static long crc32(byte[] data) {
		final CRC32 crc32 = new CRC32();
		crc32.update(data);
		return crc32.getValue();
	}

	public static long crc32(String data) {
		return crc32(data, Constants.UTF8_CHARSET);
	}

	public static long crc32(String data, Charset charset) {
		return crc32(data.getBytes(charset));
	}

	public static int murmur32(byte[] data) {
		return Hashing.murmur3_32().hashBytes(data).asInt();
	}

	public static int murmur32(String data) {
		return murmur32(data, Constants.UTF8_CHARSET);
	}

	public static int murmur32(String data, Charset charset) {
		return Hashing.murmur3_32().hashString(data, charset).asInt();
	}

	public static int murmur32(byte[] data, int seed) {
		return Hashing.murmur3_32(seed).hashBytes(data).asInt();
	}

	public static int murmur32(String data, int seed) {
		return murmur32(data, seed, Constants.UTF8_CHARSET);
	}

	public static int murmur32(String data, int seed, Charset charset) {
		return Hashing.murmur3_32(seed).hashString(data, charset).asInt();
	}

	private Digests() {
	}
}
