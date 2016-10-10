package com.itdoes.common.extension.codegenerator.entity;

/**
 * @author Jalen Zhong
 */
public interface DbSearchConfig {
	String getEntitySearch(String tableName);

	String getFieldSearch(String tableName, String columnName);
}
