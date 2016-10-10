package com.itdoes.common.extension.codegenerator.entity;

/**
 * @author Jalen Zhong
 */
public interface DbPermConfig {
	String getEntityPerm(String tableName);

	String getFieldPerm(String tableName, String columnName);
}
