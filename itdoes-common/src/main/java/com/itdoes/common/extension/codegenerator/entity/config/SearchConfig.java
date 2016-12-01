package com.itdoes.common.extension.codegenerator.entity.config;

/**
 * @author Jalen Zhong
 */
public interface SearchConfig {
	String getEntitySearch(String tableName);

	String getFieldSearch(String tableName, String columnName);
}
