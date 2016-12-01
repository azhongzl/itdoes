package com.itdoes.common.extension.codegenerator.entity.config;

/**
 * @author Jalen Zhong
 */
public interface MappingConfig {
	String getEntity(String tableName);

	String getField(String tableName, String columnName);
}
