package com.itdoes.common.core.test.jetty;

import java.util.Scanner;

/**
 * @author Jalen Zhong
 */
public class JettyServerHelper {
	public static void createAndStart(int port, String contextPath, String... taglibJarNames) {
		try {
			final JettyServer jettyServer = JettyServer.createAndStart(port, contextPath, taglibJarNames);

			System.out.println("[HINT] Press 'Enter' to restart server or 'x' to close it");
			try (final Scanner scanner = new Scanner(System.in)) {
				while (true) {
					final String line = scanner.nextLine();
					if (line.length() == 0) {
						jettyServer.reload();
					} else if ("x".equalsIgnoreCase(line)) {
						System.exit(0);
					}
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
