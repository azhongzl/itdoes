package com.itdoes.common.core.jdbc.meta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itdoes.common.core.jdbc.SimpleDriverDataSources;
import com.itdoes.common.core.jdbc.meta.Column.ColumnType;

/**
 * @author Jalen Zhong
 */
public class MetaParser {
	private final JdbcTemplate template;

	public MetaParser(String driver, String url, String username, String password) {
		this(SimpleDriverDataSources.createDataSource(driver, url, username, password));
	}

	public MetaParser(DataSource dataSource) {
		this(new JdbcTemplate(dataSource));
	}

	public MetaParser(JdbcTemplate template) {
		this.template = template;
	}

	public List<Table> parseTables() {
		return template.execute(new ConnectionCallback<List<Table>>() {
			@Override
			public List<Table> doInConnection(Connection connection) throws SQLException, DataAccessException {
				final List<Table> tableList = Lists.newArrayList();

				final DatabaseMetaData meta = connection.getMetaData();

				final ResultSet tableRs = meta.getTables(null, null, null, null);
				try {
					while (tableRs.next()) {
						final String tableName = tableRs.getString("TABLE_NAME");
						final Table table = new Table(tableName);
						tableList.add(table);
					}
				} finally {
					tableRs.close();
				}

				for (Table table : tableList) {
					final Map<String, Column> columnMap = Maps.newHashMap();

					final ResultSet columnRs = meta.getColumns(null, null, table.getName(), null);
					try {
						while (columnRs.next()) {
							final String columnName = columnRs.getString("COLUMN_NAME");
							final int columnTypeId = columnRs.getInt("DATA_TYPE");
							final String columnTypeName = columnRs.getString("TYPE_NAME");
							final boolean nullable = columnRs.getBoolean("NULLABLE");
							final Column column = new Column(columnName, new ColumnType(columnTypeId, columnTypeName),
									nullable);
							table.getColumnList().add(column);
							columnMap.put(column.getName(), column);
						}
					} finally {
						columnRs.close();
					}

					final ResultSet pkRs = meta.getPrimaryKeys(null, null, table.getName());
					try {
						while (pkRs.next()) {
							final String keyName = pkRs.getString("COLUMN_NAME");
							if (columnMap.containsKey(keyName)) {
								columnMap.get(keyName).setPk(true);
							}
						}
					} finally {
						pkRs.close();
					}
				}

				return tableList;
			}
		});
	}
}
