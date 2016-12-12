package com.itdoes.common.core.test.jetty;

import java.util.Scanner;

/**
 * @author Jalen Zhong
 */
public class JettyServerHelper {
	public static void createAndStart(int port, String contextPath, String... taglibJarNames) {
		try {
			final JettyServer jettyServer = JettyServer.createAndStart(port, contextPath, taglibJarNames);

			showUsage();

			try (final Scanner scanner = new Scanner(System.in)) {
				while (true) {
					final String line = scanner.nextLine();
					if (line.length() == 0) {
						jettyServer.restart();
						showUsage();
					} else if ("l".equalsIgnoreCase(line)) {
						jettyServer.reload();
						showUsage();
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

	private static void showUsage() {
		printUsageLine();
		System.out.println("[HINT] Press 'Enter' to restart, 'l' to reload, or 'x' to close");
		printUsageLine();
	}

	private static void printUsageLine() {
		printCharTimes('=', 70);
	}

	private static void printCharTimes(char c, int n) {
		for (int i = 1; i <= n; i++) {
			System.out.print(c);
		}
		System.out.println();
	}

	private JettyServerHelper() {
	}
}
