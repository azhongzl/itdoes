package com.itdoes.common.extension.codegenerator.entity;

import com.itdoes.common.extension.codegenerator.entity.EhcacheModel.Cache;

/**
 * @author Jalen Zhong
 */
public interface EhcacheConfig {
	EhcacheModel newModel();

	Cache newCache(String entityPackageName, String entityClassName);
}
