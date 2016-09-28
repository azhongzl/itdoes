package com.itdoes.common.core.security;

import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @author Jalen Zhong
 */
public class DigestsTest {
	@SuppressWarnings("unused")
	@Test
	public void digestString() {
		String data = "user";
		byte[] result = Digests.digest(Digests.SHA256, data.getBytes());

		byte[] salt = Digests.generateSalt(8);
		result = Digests.digest(Digests.SHA256, data.getBytes(), salt);

		result = Digests.digest(Digests.SHA256, data.getBytes(), salt, 1024);
	}

	@SuppressWarnings("unused")
	@Test
	public void digestFile() throws IOException {
		Resource resource = new ClassPathResource("/logback.xml");
		byte[] result = Digests.digest(Digests.SHA256, resource.getInputStream());
	}
}
