package com.itdoes.common.test;

import java.sql.Driver;

import javax.sql.DataSource;

import org.openqa.selenium.WebDriver;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.itdoes.common.test.jetty.JettyServer;
import com.itdoes.common.test.selenium.Selenium2;
import com.itdoes.common.test.selenium.WebDrivers;
import com.itdoes.common.test.sql.SqlExecutors;
import com.itdoes.common.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class Tests {
	public static JettyServer createAndStartJettyServer(int port, String contextPath, String... taglibJarNames) {
		final JettyServer jettyServer = new JettyServer(port, contextPath, taglibJarNames);
		jettyServer.start();
		return jettyServer;
	}

	@SuppressWarnings("unchecked")
	public static SimpleDriverDataSource createDataSource(String driver, String url, String username, String password) {
		try {
			final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
			dataSource.setDriverClass((Class<? extends Driver>) Class.forName(driver));
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			return dataSource;
		} catch (ClassNotFoundException e) {
			throw Exceptions.unchecked(e, IllegalArgumentException.class);
		}
	}

	public static void executeSql(DataSource dataSource, String... sqlPaths) {
		SqlExecutors.executeSql(dataSource, sqlPaths);
	}

	public static Selenium2 createSelenium(String driverName, String baseUrl, int timeout) {
		final WebDriver driver = WebDrivers.createDriver(driverName);
		return new Selenium2(driver, baseUrl, timeout);
	}

	private Tests() {
	}
}
