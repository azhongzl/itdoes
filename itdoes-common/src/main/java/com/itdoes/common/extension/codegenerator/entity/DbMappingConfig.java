package com.itdoes.common.extension.codegenerator.entity;

/**
 * @author Jalen Zhong
 */
public interface DbMappingConfig {
	String getEntity(String tableName);

	String getField(String tableName, String columnName);
}
