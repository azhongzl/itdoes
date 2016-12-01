package com.itdoes.common.extension.codegenerator.entity.config;

/**
 * @author Jalen Zhong
 */
public interface PermConfig {
	String getEntityPerm(String tableName);

	String getFieldPerm(String tableName, String columnName);
}
