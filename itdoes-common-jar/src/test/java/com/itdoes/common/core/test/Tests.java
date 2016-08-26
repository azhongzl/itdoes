package com.itdoes.common.core.test;

import java.sql.Driver;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.itdoes.common.core.sql.SqlExecutors;
import com.itdoes.common.core.test.jetty.JettyServer;
import com.itdoes.common.core.util.Exceptions;

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

	private Tests() {
	}
}
