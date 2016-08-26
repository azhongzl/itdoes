package com.itdoes.common.core.test.jetty;

import static org.assertj.core.api.Assertions.assertThat;

import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Test;

/**
 * @author Jalen Zhong
 */
public class JettyServerTest {
	@Test
	public void normal() {
		final JettyServer server = new JettyServer(1976, "/test");

		final ServerConnector connector = (ServerConnector) server.getServer().getConnectors()[0];
		assertThat(connector.getPort()).isEqualTo(1976);

		final WebAppContext context = (WebAppContext) server.getServer().getHandler();
		assertThat(context.getContextPath()).isEqualTo("/test");
		assertThat(context.getWar()).isEqualTo("src/main/webapp");
	}
}
