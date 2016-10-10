package com.itdoes.common.extension.codegenerator.entity;

/**
 * @author Jalen Zhong
 */
public interface EntityEhcacheConfig {
	EhcacheModel newModel();

	String newCache(String entityPackageName, String entityClassName);
}
