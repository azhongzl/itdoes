package com.itdoes.business;

import com.itdoes.common.test.jetty.JettyServer;
import com.itdoes.common.test.spring.Profiles;

/**
 * @author Jalen Zhong
 */
public class JettyServerMain {
	private static final int PORT = 8080;
	private static final String CONTEXT_PATH = "/biz";
	private static final String[] TLD_JAR_NAMES = new String[] { "shiro-web" };

	public static void main(String[] args) {
		try {
			Profiles.activeProfile(Profiles.DEVELOPMENT);

			final JettyServer server = new JettyServer(PORT, CONTEXT_PATH, TLD_JAR_NAMES);
			server.start();

			System.out.println("[HINT] Hit Enter to reload server");
			while (true) {
				final char c = (char) System.in.read();
				if (c == '\n') {
					server.reload();
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(-1);
		}
	}

	private JettyServerMain() {
	}
}
