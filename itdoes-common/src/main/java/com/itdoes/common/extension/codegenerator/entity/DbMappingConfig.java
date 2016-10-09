package com.itdoes.common.extension.codegenerator.entity;

/**
 * @author Jalen Zhong
 */
public interface DbMappingConfig {
	String toEntity(String tableName);

	String toField(String tableName, String columnName);
}
