package com.itdoes.common.core.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Jalen Zhong
 */
public class CryptosTest {
	@Test
	public void hmac() {
		String data = "user";

		byte[] key = Cryptos.generateHmacKey(Cryptos.HMACSHA256, Cryptos.DEFAULT_HMACSHA256_KEYSIZE);
		assertThat(key).hasSize(32);

		byte[] result = Cryptos.hmac(Cryptos.HMACSHA256, data.getBytes(), key);
		assertThat(Cryptos.isHmacValid(Cryptos.HMACSHA256, result, data.getBytes(), key)).isTrue();
	}

	@Test
	public void aes() {
		String data = "user";

		byte[] key = Cryptos.generateAesKey();
		assertThat(key).hasSize(16);

		byte[] encryptResult = Cryptos.aesEncrypt(data.getBytes(), key);
		String decryptResult = Cryptos.aesDecrypt(encryptResult, key);
		assertThat(decryptResult).isEqualTo(data);
	}

	@Test
	public void aesIv() {
		String data = "user";

		byte[] key = Cryptos.generateAesKey();
		assertThat(key).hasSize(16);

		byte[] iv = Cryptos.generateIv();
		assertThat(iv).hasSize(16);

		byte[] encryptResult = Cryptos.aesEncrypt(data.getBytes(), key, iv);
		String decryptResult = Cryptos.aesDecrypt(encryptResult, key, iv);
		assertThat(decryptResult).isEqualTo(data);
	}
}
