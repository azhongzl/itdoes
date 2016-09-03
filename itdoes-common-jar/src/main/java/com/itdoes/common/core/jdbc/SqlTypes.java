package com.itdoes.common.core.jdbc;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.Struct;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author Jalen Zhong
 */
public class SqlTypes {
	private static final Map<Integer, Class<?>> SQL_TYPE_MAP = Maps.newHashMap();
	static {
		SQL_TYPE_MAP.put(Types.CHAR, String.class);
		SQL_TYPE_MAP.put(Types.VARCHAR, String.class);
		SQL_TYPE_MAP.put(Types.LONGVARCHAR, String.class);
		SQL_TYPE_MAP.put(Types.NUMERIC, BigDecimal.class);
		SQL_TYPE_MAP.put(Types.DECIMAL, BigDecimal.class);
		SQL_TYPE_MAP.put(Types.BIT, Boolean.class);
		SQL_TYPE_MAP.put(Types.TINYINT, Integer.class);
		SQL_TYPE_MAP.put(Types.SMALLINT, Integer.class);
		SQL_TYPE_MAP.put(Types.INTEGER, Integer.class);
		SQL_TYPE_MAP.put(Types.BIGINT, Long.class);
		SQL_TYPE_MAP.put(Types.REAL, Float.class);
		SQL_TYPE_MAP.put(Types.FLOAT, Double.class);
		SQL_TYPE_MAP.put(Types.DOUBLE, Double.class);
		SQL_TYPE_MAP.put(Types.BINARY, byte[].class);
		SQL_TYPE_MAP.put(Types.VARBINARY, byte[].class);
		SQL_TYPE_MAP.put(Types.LONGVARBINARY, byte[].class);
		SQL_TYPE_MAP.put(Types.DATE, Date.class);
		SQL_TYPE_MAP.put(Types.TIME, Time.class);
		SQL_TYPE_MAP.put(Types.TIMESTAMP, Timestamp.class);
		SQL_TYPE_MAP.put(Types.CLOB, Clob.class);
		SQL_TYPE_MAP.put(Types.BLOB, Blob.class);
		SQL_TYPE_MAP.put(Types.ARRAY, Array.class);
		SQL_TYPE_MAP.put(Types.STRUCT, Struct.class);
		SQL_TYPE_MAP.put(Types.REF, Ref.class);
	}

	public static Class<?> fromSqlType(Integer sqlType) {
		return SQL_TYPE_MAP.get(sqlType);
	}

	private SqlTypes() {
	}
}
