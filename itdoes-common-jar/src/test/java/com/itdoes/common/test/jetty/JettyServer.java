package com.itdoes.common.test.jetty;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

import com.google.common.collect.Lists;
import com.itdoes.common.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class JettyServer {
	private static final String DEFAULT_WEBAPP_PATH = "src/main/webapp";
	private static final String WEBDEFAULT_WINDOWS_PATH = "jetty/webdefault-windows.xml";

	private final int port;
	private final String contextPath;

	private final Server server;

	public JettyServer(int port, String contextPath) {
		this.port = port;
		this.contextPath = contextPath;

		this.server = new Server();

		init();
	}

	public void start() {
		try {
			System.out.println("[INFO] Jetty Server starting...");
			server.start();
			System.out.println("[INFO] Jetty Server started at " + getAddress());
		} catch (Throwable t) {
			throw Exceptions.unchecked(t);
		}
	}

	public void setTaglibJarNames(String... jarNames) {
		List<String> jarNamePatterns = Lists.newArrayList(".*/jstl-[^/]*\\.jar$", ".*/.*taglibs[^/]*\\.jar$");
		for (String jarName : jarNames) {
			jarNamePatterns.add(".*/" + jarName + "-[^/]*\\.jar$");
		}

		WebAppContext context = (WebAppContext) server.getHandler();
		context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
				StringUtils.join(jarNamePatterns, '|'));
	}

	public void reload() {
		try {
			WebAppContext context = (WebAppContext) server.getHandler();

			System.out.println("[INFO] Jetty Server Context stopping...");
			context.stop();

			WebAppClassLoader classLoader = new WebAppClassLoader(context);
			classLoader.addClassPath("target/classes");
			classLoader.addClassPath("target/test-classes");
			context.setClassLoader(classLoader);

			context.start();
			System.out.println("[INFO] Jetty Server Context started at" + getAddress());
		} catch (Throwable t) {
			throw Exceptions.unchecked(t);
		}
	}

	public int getPort() {
		return port;
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getAddress() {
		return "http://localhost:" + port + contextPath;
	}

	private void init() {
		server.setStopAtShutdown(true);

		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setPort(port);
		connector.setReuseAddress(false);
		server.setConnectors(new Connector[] { connector });

		WebAppContext context = new WebAppContext(DEFAULT_WEBAPP_PATH, contextPath);
		context.setDefaultsDescriptor(WEBDEFAULT_WINDOWS_PATH);
		server.setHandler(context);
	}
}