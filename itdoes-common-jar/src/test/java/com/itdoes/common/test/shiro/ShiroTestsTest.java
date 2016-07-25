package com.itdoes.common.test.shiro;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.shiro.SecurityUtils;
import org.junit.Test;

/**
 * @author Jalen Zhong
 */
public class ShiroTestsTest {
	@Test
	public void subject() {
		ShiroTests.pushSubject("foo");
		assertThat(SecurityUtils.getSubject().isAuthenticated()).isTrue();
		assertThat(SecurityUtils.getSubject().getPrincipal()).isEqualTo("foo");

		ShiroTests.popSubject();
		ShiroTests.pushSubject("bar");
		assertThat(SecurityUtils.getSubject().isAuthenticated()).isTrue();
		assertThat(SecurityUtils.getSubject().getPrincipal()).isEqualTo("bar");
	}
}
