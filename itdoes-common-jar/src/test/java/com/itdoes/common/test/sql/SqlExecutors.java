package com.itdoes.common.test.sql;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import com.itdoes.common.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class SqlExecutors {
	private static final String DEFAULT_ENCODING = "UTF-8";

	private static final ResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();

	public static void executeSql(DataSource dataSource, String... sqlPaths) {
		if (sqlPaths == null || sqlPaths.length == 0) {
			return;
		}

		try {
			for (String sqlPath : sqlPaths) {
				final Resource resource = RESOURCE_LOADER.getResource(sqlPath);
				ScriptUtils.executeSqlScript(dataSource.getConnection(),
						new EncodedResource(resource, DEFAULT_ENCODING));
			}
		} catch (ScriptException | SQLException e) {
			throw Exceptions.unchecked(e);
		}
	}

	private SqlExecutors() {
	}
}
