package com.itdoes.common.extension.codegenerator.entity;

/**
 * @author Jalen Zhong
 */
public interface DbPermConfig {
	boolean isPermField(String tableName, String columnName);
}