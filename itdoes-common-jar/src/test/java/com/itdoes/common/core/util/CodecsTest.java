package com.itdoes.common.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * @author Jalen Zhong
 */
public class CodecsTest {
	@Test
	public void hex() {
		final String input = "Hello world";
		final String result = Codecs.hexEncode(input.getBytes());
		assertThat(new String(Codecs.hexDecode(result))).isEqualTo(input);
	}

	@Test
	public void base64() {
		final String input = "Hello world";
		final String result = Codecs.base64Encode(input.getBytes());
		assertThat(new String(Codecs.base64Decode(result))).isEqualTo(input);
	}

	@Test
	public void base64Urlsafe() {
		final String input = "Hello world";
		final String result = Codecs.base64EncodeUrlSafe(input.getBytes());
		assertThat(new String(Codecs.base64Decode(result))).isEqualTo(input);
	}

	@Test
	public void url() {
		final String input = "http://locahost/?q=中文&t=1";
		final String result = Codecs.urlEncode(input);
		assertThat(Codecs.urlDecode(result)).isEqualTo(input);
	}

	@Test
	public void xml() {
		final String input = "1<2";
		final String result = Codecs.xmlEscape(input);
		assertThat(result).isEqualTo(input.replaceAll("<", "&lt;"));
		assertThat(Codecs.xmlUnescape(result)).isEqualTo(input);
	}

	@Test
	public void html() {
		final String input = "1<2";
		final String result = Codecs.htmlEscape(input);
		assertThat(result).isEqualTo(input.replaceAll("<", "&lt;"));
		assertThat(Codecs.htmlUnescape(result)).isEqualTo(input);
	}
}
