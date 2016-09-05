package com.itdoes.common.core.hibernate;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Jalen Zhong
 */
public class HibernatesTest {
	@Test
	public void getDialect() throws SQLException {
		DataSource dataSource = Mockito.mock(DataSource.class);
		Connection connection = Mockito.mock(Connection.class);
		DatabaseMetaData metaData = Mockito.mock(DatabaseMetaData.class);

		Mockito.when(dataSource.getConnection()).thenReturn(connection);
		Mockito.when(connection.getMetaData()).thenReturn(metaData);

		Mockito.when(metaData.getURL()).thenReturn("jdbc:h2:file:~/test;AUTO_SERVER=TRUE");
		String dialect = Hibernates.getDialect(dataSource);
		assertThat(dialect).isEqualTo(H2Dialect.class.getName());

		Mockito.when(metaData.getURL()).thenReturn("jdbc:mysql://localhost:3306/test");
		dialect = Hibernates.getDialect(dataSource);
		assertThat(dialect).isEqualTo(MySQL5InnoDBDialect.class.getName());

		Mockito.when(metaData.getURL()).thenReturn("jdbc:oracle:thin:@127.0.0.1:1521:XE");
		dialect = Hibernates.getDialect(dataSource);
		assertThat(dialect).isEqualTo(Oracle10gDialect.class.getName());
	}
}
