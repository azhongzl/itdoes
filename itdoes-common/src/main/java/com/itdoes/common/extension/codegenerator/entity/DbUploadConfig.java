package com.itdoes.common.extension.codegenerator.entity;

/**
 * @author Jalen Zhong
 */
public interface DbUploadConfig {
	boolean isUploadField(String tableName, String columnName);
}
