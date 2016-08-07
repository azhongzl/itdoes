package com.itdoes.common.test.business.functional;

import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.junit.BeforeClass;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.itdoes.common.test.Tests;
import com.itdoes.common.test.jetty.JettyServer;
import com.itdoes.common.test.spring.Profiles;
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
	protected static SimpleDriverDataSource dataSource;

	@BeforeClass
	protected static void initEnv() {
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
		if (dataSource != null || !PL.getBoolean("jdbc.on")) {
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
}
