package com.itdoes.common.extension.codegenerator.entity;

/**
 * @author Jalen Zhong
 */
public interface DbConstraintConfig {
	String getFieldConstraint(String tableName, String columnName);
}
