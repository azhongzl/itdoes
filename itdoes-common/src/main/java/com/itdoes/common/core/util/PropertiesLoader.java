package com.itdoes.common.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * @author Jalen Zhong
 */
public class PropertiesLoader {
	private static final String DEFAULT_VALUE_SEPARATOR = ",";

	private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesLoader.class);

	private static final ResourceLoader RESOURCE_LOADER = new DefaultResourceLoader();

	private static Properties loadProperties(String... paths) {
		final Properties props = new Properties();
		for (String path : paths) {
			LOGGER.debug("Loading properties from file: {}", path);
			InputStream is = null;
			try {
				final Resource resource = RESOURCE_LOADER.getResource(path);
				is = resource.getInputStream();
				props.load(is);
			} catch (IOException e) {
				throw new IllegalArgumentException("Error in loadng properties from file: " + path, e);
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
		return props;
	}

	private final Properties properties;
	private final boolean systemPrefered;

	public PropertiesLoader(boolean systemPrefered, String... paths) {
		this.systemPrefered = systemPrefered;
		this.properties = loadProperties(paths);
	}

	public PropertiesLoader(String... paths) {
		this(true, paths);
	}

	public Properties getProperties() {
		return properties;
	}

	public String getString(String key) {
		final String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException("Key " + key + " does not exist");
		}
		return value;
	}

	public String getString(String key, String defaultValue) {
		final String value = getValue(key);
		return value == null ? defaultValue : value;
	}

	public String getString(String[] keys) {
		String value = getValue(keys);
		if (value == null) {
			throw new NoSuchElementException("Keys " + Arrays.toString(keys) + " does not exist");
		}
		return value;
	}

	public String getString(String[] keys, String defaultValue) {
		final String value = getValue(keys);
		return value == null ? defaultValue : value;
	}

	public String[] getStrings(String key, String separator) {
		final String value = getString(key);
		return StringUtils.split(value, separator);
	}

	public String[] getStrings(String key) {
		return getStrings(key, DEFAULT_VALUE_SEPARATOR);
	}

	public String[] getStringsWithDefault(String key, String defaultValue, String separator) {
		final String value = getString(key, defaultValue);
		return StringUtils.split(value, separator);
	}

	public String[] getStringsWithDefault(String key, String defaultValue) {
		return getStringsWithDefault(key, defaultValue, DEFAULT_VALUE_SEPARATOR);
	}

	public String[] getStrings(String[] keys, String separator) {
		final String value = getString(keys);
		return StringUtils.split(value, separator);
	}

	public String[] getStrings(String[] keys) {
		return getStrings(keys, DEFAULT_VALUE_SEPARATOR);
	}

	public String[] getStringsWithDefault(String[] keys, String defaultValue, String separator) {
		final String value = getString(keys, defaultValue);
		return StringUtils.split(value, separator);
	}

	public String[] getStringsWithDefault(String[] keys, String defaultValue) {
		return getStringsWithDefault(keys, defaultValue, DEFAULT_VALUE_SEPARATOR);
	}

	public Integer getInteger(String key) {
		final String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException("Key " + key + " does not exist");
		}
		return Integer.valueOf(value);
	}

	public Integer getInteger(String key, Integer defaultValue) {
		final String value = getValue(key);
		return value == null ? defaultValue : Integer.valueOf(value);
	}

	public Integer getInteger(String[] keys) {
		final String value = getValue(keys);
		if (value == null) {
			throw new NoSuchElementException("Keys " + Arrays.toString(keys) + " does not exist");
		}
		return Integer.valueOf(value);
	}

	public Integer getInteger(String[] keys, Integer defaultValue) {
		final String value = getValue(keys);
		return value == null ? defaultValue : Integer.valueOf(value);
	}

	public Long getLong(String key) {
		final String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException("Key " + key + " does not exist");
		}
		return Long.valueOf(value);
	}

	public Long getLong(String key, Long defaultValue) {
		final String value = getValue(key);
		return value == null ? defaultValue : Long.valueOf(value);
	}

	public Long getLong(String[] keys) {
		final String value = getValue(keys);
		if (value == null) {
			throw new NoSuchElementException("Keys " + Arrays.toString(keys) + " does not exist");
		}
		return Long.valueOf(value);
	}

	public Long getLong(String[] keys, Long defaultValue) {
		final String value = getValue(keys);
		return value == null ? defaultValue : Long.valueOf(value);
	}

	public Double getDouble(String key) {
		final String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException("Key " + key + " does not exist");
		}
		return Double.valueOf(value);
	}

	public Double getDouble(String key, Double defaultValue) {
		final String value = getValue(key);
		return value == null ? defaultValue : Double.valueOf(value);
	}

	public Double getDouble(String[] keys) {
		final String value = getValue(keys);
		if (value == null) {
			throw new NoSuchElementException("Keys " + Arrays.toString(keys) + " does not exist");
		}
		return Double.valueOf(value);
	}

	public Double getDouble(String[] keys, Double defaultValue) {
		final String value = getValue(keys);
		return value == null ? defaultValue : Double.valueOf(value);
	}

	public Boolean getBoolean(String key) {
		final String value = getValue(key);
		if (value == null) {
			throw new NoSuchElementException("Key " + key + " does not exist");
		}
		return Boolean.valueOf(value);
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		final String value = getValue(key);
		return value == null ? defaultValue : Boolean.valueOf(value);
	}

	public Boolean getBoolean(String[] keys) {
		final String value = getValue(keys);
		if (value == null) {
			throw new NoSuchElementException("Keys " + Arrays.toString(keys) + " does not exist");
		}
		return Boolean.valueOf(value);
	}

	public Boolean getBoolean(String[] keys, Boolean defaultValue) {
		final String value = getValue(keys);
		return value == null ? defaultValue : Boolean.valueOf(value);
	}

	private String getValue(String key) {
		if (systemPrefered) {
			final String value = System.getProperty(key);
			return StringUtils.isBlank(value) ? properties.getProperty(key) : value;
		} else {
			final String value = properties.getProperty(key);
			return StringUtils.isBlank(value) ? System.getProperty(key) : value;
		}
	}

	private String getValue(String[] keys) {
		for (String key : keys) {
			final String value = getValue(key);
			if (value != null) {
				return value;
			}
		}

		return null;
	}
}
