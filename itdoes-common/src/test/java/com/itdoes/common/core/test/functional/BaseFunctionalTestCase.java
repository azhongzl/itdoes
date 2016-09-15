package com.itdoes.common.core.test.functional;

import java.io.IOException;
import java.net.URL;

import javax.sql.DataSource;

import org.apache.http.client.fluent.Request;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itdoes.common.core.jdbc.SimpleDriverDataSources;
import com.itdoes.common.core.jdbc.SqlExecutors;
import com.itdoes.common.core.test.jetty.JettyServer;
import com.itdoes.common.core.test.spring.Profiles;
import com.itdoes.common.core.util.Exceptions;
import com.itdoes.common.core.util.PropertiesLoader;
import com.itdoes.common.core.util.Urls;

/**
 * @author Jalen Zhong
 */
public abstract class BaseFunctionalTestCase {
	// Required
	protected static final PropertiesLoader PL = new PropertiesLoader("classpath:/application.properties",
			"classpath:/application.functionaltest.properties", "classpath:/application.local.properties");
	protected static final String URL_BASE = PL.getStringMust("url.base");
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
		if (jettyServer != null || !URL_BASE_URL.getHost().equals("localhost") || !PL.getBooleanMust("jetty.on")) {
			return;
		}

		jettyServer = JettyServer.createAndStart(URL_BASE_URL.getPort(), PL.getStringMust("jetty.context"),
				PL.getStringsMay("jetty.taglib", null));
	}

	protected static void initDataSource() {
		if (dataSource != null || (!PL.getBooleanMust("jdbc.on") && !PL.getBooleanMust("sql.on"))) {
			return;
		}

		dataSource = SimpleDriverDataSources.createDataSource(PL.getStringMust("jdbc.driver"),
				PL.getStringMust("jdbc.url"), PL.getStringMust("jdbc.username"), PL.getStringMust("jdbc.password"));
	}

	protected static void initDb() {
		if (!PL.getBooleanMust("sql.on")) {
			return;
		}

		SqlExecutors.executeSql(dataSource, PL.getStringsMust("sql.file"));
	}

	protected static String getContent(String urlString) {
		try {
			return Request.Get(Urls.toUri(Urls.concat(URL_BASE, urlString))).execute().returnContent().asString();
		} catch (IOException e) {
			throw Exceptions.unchecked(e, IllegalStateException.class);
		}
	}

	protected static String postContent(String urlString) {
		try {
			return Request.Post(Urls.toUri(Urls.concat(URL_BASE, urlString))).execute().returnContent().asString();
		} catch (IOException e) {
			throw Exceptions.unchecked(e, IllegalStateException.class);
		}
	}

	protected final Logger logger = LoggerFactory.getLogger(getClass());
}
