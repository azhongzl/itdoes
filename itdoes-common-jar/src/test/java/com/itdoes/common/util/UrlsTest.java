package com.itdoes.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Jalen Zhong
 */
public class UrlsTest {
	@Test
	public void concat() {
		assertThat(Urls.concat("abc", "def")).isEqualTo("abc/def");
		assertThat(Urls.concat("abc/", "def")).isEqualTo("abc/def");
		assertThat(Urls.concat("abc", "/def")).isEqualTo("abc/def");
		assertThat(Urls.concat("abc/", "/def")).isEqualTo("abc/def");
		assertThat(Urls.concat("http://localhost/abc", "http://localhost/def")).isEqualTo("http://localhost/def");
	}
}
