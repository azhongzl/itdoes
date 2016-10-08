package com.itdoes.common.extension.codegenerator.entity;

/**
 * @author Jalen Zhong
 */
public interface SearchConfig {
	String getTableSearchConfig(String tableName);

	String getColumnSearchConfig(String tableName, String columnName);
}
