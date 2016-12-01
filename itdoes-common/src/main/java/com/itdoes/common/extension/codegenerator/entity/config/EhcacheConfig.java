package com.itdoes.common.extension.codegenerator.entity.config;

import com.itdoes.common.extension.codegenerator.entity.model.EhcacheModel;

/**
 * @author Jalen Zhong
 */
public interface EhcacheConfig {
	EhcacheModel newModel();

	String newEntityCache(String tableName, String entityPackageName, String entityClassName);
}
