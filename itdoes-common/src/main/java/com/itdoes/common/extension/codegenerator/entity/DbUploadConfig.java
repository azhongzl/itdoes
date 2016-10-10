package com.itdoes.common.extension.codegenerator.entity;

/**
 * @author Jalen Zhong
 */
public interface DbUploadConfig {
	boolean isFieldUpload(String tableName, String columnName);
}
