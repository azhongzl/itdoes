package com.itdoes.common.util;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Jalen Zhong
 */
public class PropertiesLoader {
	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLoader.class);

	private static final ResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();

	private static Properties loadProperties(String... paths) {
		final Properties props = new Properties();
		for (String path : paths) {
			LOGGER.debug("Loading properties file from path: {}", path);
		}
		return props;
	}

	private final Properties properties;

	public PropertiesLoader(String... paths) {
		this.properties = loadProperties(paths);
	}

	public Properties getProperties() {
		return properties;
	}
}
