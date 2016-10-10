package com.itdoes.common.extension.codegenerator.entity;

/**
 * @author Jalen Zhong
 */
public interface DbSearchConfig {
	String getTableSearchConfig(String tableName);

	String getColumnSearchConfig(String tableName, String columnName);
}
