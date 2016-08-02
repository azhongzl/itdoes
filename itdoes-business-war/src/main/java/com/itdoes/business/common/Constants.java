package com.itdoes.business.common;

import com.itdoes.common.util.PropertiesLoader;

/**
 * @author Jalen Zhong
 */
public interface Constants {
	String APPLICATION_CONFIG_FILE = "classpath:/application.properties";
	PropertiesLoader PL = new PropertiesLoader(APPLICATION_CONFIG_FILE);
	String ENTITY_BASE_PACKAGE_KEY = "entity.base.package";
	String ENTITY_BASE_PACKAGE = PL.getProperty(ENTITY_BASE_PACKAGE_KEY);
}
