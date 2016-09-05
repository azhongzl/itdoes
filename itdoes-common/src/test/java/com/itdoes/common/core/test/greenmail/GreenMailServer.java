package com.itdoes.common.core.test.greenmail;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

/**
 * @author Jalen Zhong
 */
public class GreenMailServer implements InitializingBean, DisposableBean, FactoryBean<GreenMail> {
	public static final String DEFAULT_USERNAME = "greenmail@localhost.com";
	public static final String DEFAULT_PASSWORD = "greenmail";
	public static final int DEFAULT_PORT = 3025;

	private GreenMail greenMail;

	private String username = DEFAULT_USERNAME;
	private String password = DEFAULT_PASSWORD;
	private int port = DEFAULT_PORT;

	@Override
	public void afterPropertiesSet() throws Exception {
		greenMail = new GreenMail(new ServerSetup(port, null, ServerSetup.PROTOCOL_SMTP));
		greenMail.setUser(username, password);
		greenMail.start();
	}

	@Override
	public void destroy() throws Exception {
		if (greenMail != null) {
			greenMail.stop();
		}
	}

	@Override
	public GreenMail getObject() throws Exception {
		return greenMail;
	}

	@Override
	public Class<?> getObjectType() {
		return GreenMail.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
