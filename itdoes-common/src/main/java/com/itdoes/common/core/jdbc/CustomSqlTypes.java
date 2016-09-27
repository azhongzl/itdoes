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
	private static final Map<Integer, Class<?>> FIELD_SQL_JAVA_TYPE_MAP = Maps.newHashMap(SqlTypes.getSqlJavaTypeMap());
	static {
		FIELD_SQL_JAVA_TYPE_MAP.put(Types.DATE, LocalDate.class);
		FIELD_SQL_JAVA_TYPE_MAP.put(Types.TIME, LocalTime.class);
		FIELD_SQL_JAVA_TYPE_MAP.put(Types.TIMESTAMP, LocalDateTime.class);
	}

	private static final Map<Integer, Class<?>> ID_SQL_JAVA_TYPE_MAP = Maps.newHashMap(FIELD_SQL_JAVA_TYPE_MAP);
	static {
		ID_SQL_JAVA_TYPE_MAP.put(Types.BINARY, UUID.class);
	}

	public static Map<Integer, Class<?>> getFieldSqlJavaTypeMap() {
		return FIELD_SQL_JAVA_TYPE_MAP;
	}

	public static Map<Integer, Class<?>> getIdSqlJavaTypeMap() {
		return ID_SQL_JAVA_TYPE_MAP;
	}

	private CustomSqlTypes() {
	}
}
