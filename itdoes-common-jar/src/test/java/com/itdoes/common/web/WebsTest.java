package com.itdoes.common.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.google.common.net.HttpHeaders;

/**
 * @author Jalen Zhong
 */
public class WebsTest {
	@Test
	public void checkIfModifiedSince() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		assertThat(Webs.checkIfModifiedSince(request, response, System.currentTimeMillis() - 2000)).isTrue();

		request.addHeader(HttpHeaders.IF_MODIFIED_SINCE, System.currentTimeMillis());
		assertThat(Webs.checkIfModifiedSince(request, response, System.currentTimeMillis() - 2000)).isFalse();
		assertThat(Webs.checkIfModifiedSince(request, response, System.currentTimeMillis() + 2000)).isTrue();
	}

	@Test
	public void checkIfNoneMatch() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		assertThat(Webs.checkIfNoneMatch(request, response, "V1.0")).isTrue();

		request.addHeader(HttpHeaders.IF_NONE_MATCH, "V1.0,V1.1");
		assertThat(Webs.checkIfNoneMatch(request, response, "V1.0")).isFalse();
		assertThat(Webs.checkIfNoneMatch(request, response, "V2.0")).isTrue();
	}
}
