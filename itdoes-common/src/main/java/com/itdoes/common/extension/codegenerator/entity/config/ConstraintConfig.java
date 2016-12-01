package com.itdoes.common.extension.codegenerator.entity.config;

/**
 * @author Jalen Zhong
 */
public interface ConstraintConfig {
	String getFieldConstraint(String tableName, String columnName);
}
