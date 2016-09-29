package com.itdoes.common.extension.codegenerator.entity;

/**
 * @author Jalen Zhong
 */
public interface EhcacheConfig {
	EhcacheModel newModel();

	String newCache(String entityPackageName, String entityClassName);
}
