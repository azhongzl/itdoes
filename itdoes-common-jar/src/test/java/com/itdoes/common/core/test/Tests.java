package com.itdoes.common.core.test;

import java.sql.Driver;

import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.itdoes.common.core.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class Tests {
	@SuppressWarnings("unchecked")
	public static SimpleDriverDataSource createDataSource(String driver, String url, String username, String password) {
		try {
			return createDataSource((Class<? extends Driver>) Class.forName(driver), url, username, password);
		} catch (ClassNotFoundException e) {
			throw Exceptions.unchecked(e, IllegalArgumentException.class);
		}
	}

	public static SimpleDriverDataSource createDataSource(Class<? extends Driver> driverClass, String url,
			String username, String password) {
		final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		dataSource.setDriverClass(driverClass);
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	private Tests() {
	}
}
