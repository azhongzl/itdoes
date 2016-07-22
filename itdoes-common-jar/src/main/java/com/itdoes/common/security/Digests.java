package com.itdoes.common.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.lang3.Validate;

import com.itdoes.common.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class Digests {
	private static final String SHA1 = "SHA-1";
	private static final String SHA256 = "SHA-256";
	private static final String MD5 = "MD5";

	private static final int DEFAULT_SALT_SIZE = 8;

	private static final SecureRandom RANDOM = new SecureRandom();

	private static final int BUFFER_LENGTH = 8 * 1024;

	public static byte[] sha1(byte[] data) {
		return sha1(data, null);
	}

	public static byte[] sha1(byte[] data, byte[] salt) {
		return sha1(data, salt, 1);
	}

	public static byte[] sha1(byte[] data, byte[] salt, int iterations) {
		return digest(SHA1, data, salt, iterations);
	}

	public static byte[] sha256(byte[] data) {
		return sha256(data, null);
	}

	public static byte[] sha256(byte[] data, byte[] salt) {
		return sha256(data, salt, 1);
	}

	public static byte[] sha256(byte[] data, byte[] salt, int iterations) {
		return digest(SHA256, data, salt, iterations);
	}

	public static byte[] md5(byte[] data) {
		return md5(data, null);
	}

	public static byte[] md5(byte[] data, byte[] salt) {
		return md5(data, salt, 1);
	}

	public static byte[] md5(byte[] data, byte[] salt, int iterations) {
		return digest(MD5, data, salt, iterations);
	}

	private static byte[] digest(String algorithm, byte[] data, byte[] salt, int iterations) {
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

	public static byte[] sha1(InputStream is) {
		return digest(SHA1, is);
	}

	public static byte[] sha256(InputStream is) {
		return digest(SHA256, is);
	}

	public static byte[] md5(InputStream is) {
		return digest(MD5, is);
	}

	private static byte[] digest(String algorithm, InputStream is) {
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
		Validate.isTrue(size > 0, "Salt size must be a positive integer", size);

		final byte[] bytes = new byte[size];
		RANDOM.nextBytes(bytes);
		return bytes;
	}

	private Digests() {
	}
}
