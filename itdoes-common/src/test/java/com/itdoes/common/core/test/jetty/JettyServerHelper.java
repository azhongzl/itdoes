package com.itdoes.common.core.test.jetty;

/**
 * @author Jalen Zhong
 */
public class JettyServerHelper {
	public static void createAndStart(int port, String contextPath, String... taglibJarNames) {
		try {
			final JettyServer jettyServer = JettyServer.createAndStart(port, contextPath, taglibJarNames);

			System.out.println("[HINT] Hit Enter to reload server");
			while (true) {
				final char c = (char) System.in.read();
				if (c == '\n') {
					jettyServer.reload();
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(-1);
		}
	}

	private JettyServerHelper() {
	}
}
