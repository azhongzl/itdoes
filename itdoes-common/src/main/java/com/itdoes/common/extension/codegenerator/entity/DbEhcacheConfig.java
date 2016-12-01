package com.itdoes.common.extension.codegenerator.entity;

/**
 * @author Jalen Zhong
 */
public interface DbEhcacheConfig {
	EhcacheModel newModel();

	String newCache(String tableName, String entityPackageName, String entityClassName);
}
