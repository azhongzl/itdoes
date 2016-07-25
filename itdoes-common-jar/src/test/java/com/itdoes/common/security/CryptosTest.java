package com.itdoes.common.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Jalen Zhong
 */
public class CryptosTest {
	@Test
	public void hmac() {
		String data = "user";

		byte[] key = Cryptos.generateHmacSha256Key();
		assertThat(key).hasSize(20);

		byte[] result = Cryptos.hmacSha256(data.getBytes(), key);
		assertThat(Cryptos.isHmacSha256Valid(result, data.getBytes(), key)).isTrue();
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
