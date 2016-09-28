package com.itdoes.common.extension.security;

import java.nio.charset.Charset;

import com.itdoes.common.core.Constants;
import com.itdoes.common.core.security.Digests;
import com.itdoes.common.core.util.Codecs;

/**
 * @author Jalen Zhong
 */
public class DigestsHelper {
	public static class HexDigestSalt {
		private final String hexDigest;
		private final String hexSalt;

		public HexDigestSalt(String hexDigest, String hexSalt) {
			this.hexDigest = hexDigest;
			this.hexSalt = hexSalt;
		}

		public String getHexDigest() {
			return hexDigest;
		}

		public String getHexSalt() {
			return hexSalt;
		}
	}

	public static HexDigestSalt digestByRandomSalt(String algorithm, String data) {
		return digestByRandomSalt(algorithm, data, Constants.UTF8_CHARSET);
	}

	public static HexDigestSalt digestByRandomSalt(String algorithm, String data, Charset charset) {
		return digestByRandomSalt(algorithm, data.getBytes(charset), 1);
	}

	public static HexDigestSalt digestByRandomSalt(String algorithm, String data, int iterations) {
		return digestByRandomSalt(algorithm, data, Constants.UTF8_CHARSET, iterations);
	}

	public static HexDigestSalt digestByRandomSalt(String algorithm, String data, Charset charset, int iterations) {
		return digestByRandomSalt(algorithm, data.getBytes(charset), iterations);
	}

	public static HexDigestSalt digestByRandomSalt(String algorithm, byte[] data, int iterations) {
		final byte[] salt = Digests.generateSalt();
		final byte[] hashedPassword = Digests.digest(algorithm, data, salt, iterations);
		return new HexDigestSalt(Codecs.hexEncode(hashedPassword), Codecs.hexEncode(salt));
	}

	private DigestsHelper() {
	}
}
