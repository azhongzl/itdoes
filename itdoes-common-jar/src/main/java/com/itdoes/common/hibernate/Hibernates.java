package com.itdoes.common.hibernate;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.dialect.SQLServer2008Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jalen Zhong
 */
public class Hibernates {
	private static final Logger LOGGER = LoggerFactory.getLogger(Hibernates.class);

	public static void initialize(Object proxy) {
		Hibernate.initialize(proxy);
	}

	public static String getDialect(DataSource dataSource) {
		final String jdbcUrl = getJdbcUrl(dataSource);
		if (StringUtils.contains(jdbcUrl, ":h2:")) {
			return H2Dialect.class.getName();
		} else if (StringUtils.contains(jdbcUrl, ":mysql:")) {
			return MySQL5InnoDBDialect.class.getName();
		} else if (StringUtils.contains(jdbcUrl, ":oracle:")) {
			return Oracle10gDialect.class.getName();
		} else if (StringUtils.contains(jdbcUrl, ":postgresql:")) {
			return PostgreSQL82Dialect.class.getName();
		} else if (StringUtils.contains(jdbcUrl, ":sqlserver:")) {
			return SQLServer2008Dialect.class.getName();
		} else {
			throw new IllegalArgumentException("Unknown database for jdbc url: " + jdbcUrl);
		}
	}

	private static String getJdbcUrl(DataSource dataSource) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			if (conn == null) {
				throw new IllegalStateException("Connection returned by DataSource is null");
			}

			return conn.getMetaData().getURL();
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot get jdbc url", e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					LOGGER.warn("Close Connection failed", e);
				}
			}
		}
	}

	private Hibernates() {
	}
}
