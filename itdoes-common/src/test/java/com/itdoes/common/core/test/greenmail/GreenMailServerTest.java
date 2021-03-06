package com.itdoes.common.core.test.greenmail;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.icegreen.greenmail.util.GreenMail;
import com.itdoes.common.core.test.spring.SpringTestCase;

/**
 * @author Jalen Zhong
 */
@ContextConfiguration(locations = { "/applicationContext-mail.xml" })
public class GreenMailServerTest extends SpringTestCase {
	@Autowired
	private GreenMail greenMail;

	@Test
	public void greenMail() {
		assertThat(greenMail.getSmtp().getPort()).isEqualTo(GreenMailServer.DEFAULT_PORT);
	}
}
