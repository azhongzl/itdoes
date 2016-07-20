package com.itdoes.common.test.jetty;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.itdoes.common.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class JettyServer {
	private static final String DEFAULT_WEBAPP_PATH = "src/main/webapp";
	private static final String WEBDEFAULT_PATH = "jetty/webdefault-me.xml";

	private static final Logger LOGGER = LoggerFactory.getLogger(JettyServer.class);

	private final int port;
	private final String contextPath;
	private final String url;

	private final Server server;

	public JettyServer(int port, String contextPath) {
		this.port = port;
		this.contextPath = contextPath;
		this.url = new StringBuilder().append("http://localhost:").append(port).append(contextPath).toString();

		this.server = new Server();

		init();
	}

	public void start() {
		try {
			LOGGER.info("Jetty Server starting...");
			server.start();
			LOGGER.info("Jetty Server started at: {}", url);
		} catch (Throwable t) {
			throw Exceptions.unchecked(t);
		}
	}

	public void setTaglibJarNames(String... jarNames) {
		final List<String> jarNamePatterns = Lists.newArrayList(".*/jstl-[^/]*\\.jar$", ".*/.*taglibs[^/]*\\.jar$");
		for (String jarName : jarNames) {
			jarNamePatterns.add(".*/" + jarName + "-[^/]*\\.jar$");
		}

		final WebAppContext context = (WebAppContext) server.getHandler();
		context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
				StringUtils.join(jarNamePatterns, '|'));
	}

	public void reload() {
		try {
			final WebAppContext context = (WebAppContext) server.getHandler();

			LOGGER.info("Jetty Server Context stopping...");
			context.stop();

			final WebAppClassLoader classLoader = new WebAppClassLoader(context);
			classLoader.addClassPath("target/classes");
			classLoader.addClassPath("target/test-classes");
			context.setClassLoader(classLoader);

			context.start();
			LOGGER.info("Jetty Server Context started at: {}", url);
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

	public String getUrl() {
		return url;
	}

	private void init() {
		server.setStopAtShutdown(true);

		final ServerConnector connector = new ServerConnector(server);
		connector.setPort(port);
		connector.setReuseAddress(false);
		server.addConnector(connector);

		final WebAppContext context = new WebAppContext(DEFAULT_WEBAPP_PATH, contextPath);
		context.setDefaultsDescriptor(WEBDEFAULT_PATH);
		server.setHandler(context);
	}
}
