package com.itdoes.common.test.business.functional;

import java.io.IOException;
import java.net.URL;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Request;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itdoes.common.test.Tests;
import com.itdoes.common.test.jetty.JettyServer;
import com.itdoes.common.test.spring.Profiles;
import com.itdoes.common.util.Exceptions;
import com.itdoes.common.util.PropertiesLoader;
import com.itdoes.common.util.Urls;

/**
 * @author Jalen Zhong
 */
public abstract class BaseFunctionalTestCase {
	// Required
	protected static final PropertiesLoader PL = new PropertiesLoader("classpath:/application.properties",
			"classpath:/application.functionaltest.properties", "classpath:/application.local.properties");
	protected static final String URL_BASE = PL.getProperty("url.base");
	protected static final URL URL_BASE_URL = Urls.createUrl(URL_BASE);

	// Optional
	protected static JettyServer jettyServer;
	protected static DataSource dataSource;

	@BeforeClass
	public static void initEnv() {
		Profiles.activeProfile(Profiles.FUNCTIONAL_TEST);

		initJettyServer();
		initDataSource();
		initDb();
	}

	protected static void initJettyServer() {
		if (jettyServer != null || !URL_BASE_URL.getHost().equals("localhost") || !PL.getBoolean("jetty.on")) {
			return;
		}

		jettyServer = Tests.createAndStartJettyServer(URL_BASE_URL.getPort(), PL.getProperty("jetty.context"),
				StringUtils.split(PL.getProperty("jetty.taglib"), ","));
	}

	protected static void initDataSource() {
		if (dataSource != null || (!PL.getBoolean("jdbc.on") && !PL.getBoolean("sql.on"))) {
			return;
		}

		dataSource = Tests.createDataSource(PL.getProperty("jdbc.driver"), PL.getProperty("jdbc.url"),
				PL.getProperty("jdbc.username"), PL.getProperty("jdbc.password"));
	}

	protected static void initDb() {
		if (!PL.getBoolean("sql.on")) {
			return;
		}

		Tests.executeSql(dataSource, StringUtils.split(PL.getProperty("sql.file"), ","));
	}

	protected static String get(String urlString) {
		try {
			return Request.Get(Urls.toUri(Urls.concat(URL_BASE, urlString))).execute().returnContent().asString();
		} catch (IOException e) {
			throw Exceptions.unchecked(e, IllegalStateException.class);
		}
	}

	protected static String post(String urlString) {
		try {
			return Request.Post(Urls.toUri(Urls.concat(URL_BASE, urlString))).execute().returnContent().asString();
		} catch (IOException e) {
			throw Exceptions.unchecked(e, IllegalStateException.class);
		}
	}

	protected final Logger logger = LoggerFactory.getLogger(getClass());
}
