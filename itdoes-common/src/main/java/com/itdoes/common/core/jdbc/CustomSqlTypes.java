package com.itdoes.common.core.jdbc;

import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

/**
 * @author Jalen Zhong
 */
public class CustomSqlTypes {
	private static final Map<Integer, Class<?>> SQL_JAVA_TYPE_MAP = Maps.newHashMap(SqlTypes.getSqlJavaTypeMap());
	static {
		SQL_JAVA_TYPE_MAP.put(Types.DATE, LocalDate.class);
		SQL_JAVA_TYPE_MAP.put(Types.TIME, LocalTime.class);
		SQL_JAVA_TYPE_MAP.put(Types.TIMESTAMP, LocalDateTime.class);

		SQL_JAVA_TYPE_MAP.put(Types.BINARY, UUID.class);
	}

	public static Map<Integer, Class<?>> getSqlJavaTypeMap() {
		return SQL_JAVA_TYPE_MAP;
	}

	private CustomSqlTypes() {
	}
}
