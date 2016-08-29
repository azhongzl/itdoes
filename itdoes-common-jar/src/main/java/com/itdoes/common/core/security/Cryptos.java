package com.itdoes.common.core.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.itdoes.common.core.util.Codecs;
import com.itdoes.common.core.util.Exceptions;
import com.itdoes.common.core.util.Randoms;

/**
 * @author Jalen Zhong
 */
public class Cryptos {
	private static final String HMACSHA1 = "HmacSHA1";
	private static final int DEFAULT_HMACSHA1_KEYSIZE = 20 * 8;
	private static final String HMACSHA256 = "HmacSHA256";
	private static final int DEFAULT_HMACSHA256_KEYSIZE = 32 * 8;
	private static final String HMACMD5 = "HmacMD5";
	private static final int DEFAULT_HMACMD5_KEYSIZE = 16 * 8;

	private static final String AES = "AES";
	private static final String AES_CBC = "AES/CBC/PKCS5Padding";
	private static final int DEFAULT_AES_KEYSIZE = 16 * 8;
	private static final int DEFAULT_IVSIZE = 16;

	private static final byte[] DEFAULT_AES_KEY = Codecs.hexDecode("8632e36e2755802e32989baa896882f1");

	public static byte[] hmacSha1(byte[] data, byte[] key) {
		return hmac(HMACSHA1, data, key);
	}

	public static byte[] hmacSha256(byte[] data, byte[] key) {
		return hmac(HMACSHA256, data, key);
	}

	public static byte[] hmacMd5(byte[] data, byte[] key) {
		return hmac(HMACMD5, data, key);
	}

	private static byte[] hmac(String algorithm, byte[] data, byte[] key) {
		try {
			final SecretKey secretKey = new SecretKeySpec(key, algorithm);
			final Mac mac = Mac.getInstance(algorithm);
			mac.init(secretKey);
			return mac.doFinal(data);
		} catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static boolean isHmacSha1Valid(byte[] expected, byte[] data, byte[] key) {
		return isHmacValid(HMACSHA1, expected, data, key);
	}

	public static boolean isHmacSha256Valid(byte[] expected, byte[] data, byte[] key) {
		return isHmacValid(HMACSHA256, expected, data, key);
	}

	public static boolean isHmacMd5Valid(byte[] expected, byte[] data, byte[] key) {
		return isHmacValid(HMACMD5, expected, data, key);
	}

	private static boolean isHmacValid(String algorithm, byte[] expected, byte[] data, byte[] key) {
		final byte[] actual = hmac(algorithm, data, key);
		return Arrays.equals(expected, actual);
	}

	public static byte[] generateHmacSha1Key() {
		return generateHmaKey(HMACSHA1, DEFAULT_HMACSHA1_KEYSIZE);
	}

	public static byte[] generateHmacSha256Key() {
		return generateHmaKey(HMACSHA256, DEFAULT_HMACSHA256_KEYSIZE);
	}

	public static byte[] generateHmacMd5Key() {
		return generateHmaKey(HMACMD5, DEFAULT_HMACMD5_KEYSIZE);
	}

	private static byte[] generateHmaKey(String algorithm, int keySize) {
		try {
			final KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
			keyGenerator.init(keySize);
			final SecretKey secretKey = keyGenerator.generateKey();
			return secretKey.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static String aesEncryptDefault(String plainPassword) {
		return Codecs.hexEncode(Cryptos.aesEncrypt(plainPassword.getBytes(), DEFAULT_AES_KEY));
	}

	public static String aesDecryptDefault(String encryptedPassword) {
		return Cryptos.aesDecrypt(Codecs.hexDecode(encryptedPassword), DEFAULT_AES_KEY);
	}

	public static byte[] aesEncrypt(byte[] data, byte[] key) {
		return aes(Cipher.ENCRYPT_MODE, data, key);
	}

	public static byte[] aesEncrypt(byte[] data, byte[] key, byte[] iv) {
		return aes(Cipher.ENCRYPT_MODE, data, key, iv);
	}

	public static String aesDecrypt(byte[] data, byte[] key) {
		return new String(aes(Cipher.DECRYPT_MODE, data, key));
	}

	public static String aesDecrypt(byte[] data, byte[] key, byte[] iv) {
		return new String(aes(Cipher.DECRYPT_MODE, data, key, iv));
	}

	private static byte[] aes(int mode, byte[] data, byte[] key) {
		try {
			final SecretKey secretKey = new SecretKeySpec(key, AES);
			final Cipher cipher = Cipher.getInstance(AES);
			cipher.init(mode, secretKey);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	private static byte[] aes(int mode, byte[] data, byte[] key, byte[] iv) {
		try {
			final SecretKey secretKey = new SecretKeySpec(key, AES);
			final IvParameterSpec ivSpec = new IvParameterSpec(iv);
			final Cipher cipher = Cipher.getInstance(AES_CBC);
			cipher.init(mode, secretKey, ivSpec);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static byte[] generateAesKey() {
		return generateAesKey(DEFAULT_AES_KEYSIZE);
	}

	public static byte[] generateAesKey(int keySize) {
		try {
			final KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
			keyGenerator.init(keySize);
			final SecretKey secretKey = keyGenerator.generateKey();
			return secretKey.getEncoded();
		} catch (NoSuchAlgorithmException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static byte[] generateIv() {
		return generateIv(DEFAULT_IVSIZE);
	}

	public static byte[] generateIv(int ivSize) {
		return Randoms.nextBytes(Randoms.SECURE_RANDOM, ivSize);
	}

	private Cryptos() {
	}
}
