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
	private final boolean blankAllowed;

	public PropertiesLoader(boolean systemPrefered, boolean blankAllowed, String... paths) {
		this.systemPrefered = systemPrefered;
		this.blankAllowed = blankAllowed;
		this.properties = loadProperties(paths);
	}

	public PropertiesLoader(String... paths) {
		this(true, false, paths);
	}

	public Properties getProperties() {
		return properties;
	}

	public String getStringMust(String key) {
		final String value = getValue(key);
		if (!hasValue(value)) {
			throw new NoSuchElementException("Key " + key + " does not exist");
		}
		return value;
	}

	public String getStringMay(String key, String defaultValue) {
		final String value = getValue(key);
		return hasValue(value) ? value : defaultValue;
	}

	public String getStringMust(String[] keys) {
		final String value = getValue(keys);
		if (!hasValue(value)) {
			throw new NoSuchElementException("Keys " + Arrays.toString(keys) + " does not exist");
		}
		return value;
	}

	public String getStringMay(String[] keys, String defaultValue) {
		final String value = getValue(keys);
		return hasValue(value) ? value : defaultValue;
	}

	public String[] getStringsMustBySeparator(String key, String separator) {
		final String value = getStringMust(key);
		return StringUtils.split(value, separator);
	}

	public String[] getStringsMust(String key) {
		return getStringsMustBySeparator(key, DEFAULT_VALUE_SEPARATOR);
	}

	public String[] getStringsMayBySeparator(String key, String defaultValue, String separator) {
		final String value = getStringMay(key, defaultValue);
		return StringUtils.split(value, separator);
	}

	public String[] getStringsMay(String key, String defaultValue) {
		return getStringsMayBySeparator(key, defaultValue, DEFAULT_VALUE_SEPARATOR);
	}

	public String[] getStringsMustBySeparator(String[] keys, String separator) {
		final String value = getStringMust(keys);
		return StringUtils.split(value, separator);
	}

	public String[] getStringsMust(String[] keys) {
		return getStringsMustBySeparator(keys, DEFAULT_VALUE_SEPARATOR);
	}

	public String[] getStringsMayBySeparator(String[] keys, String defaultValue, String separator) {
		final String value = getStringMay(keys, defaultValue);
		return StringUtils.split(value, separator);
	}

	public String[] getStringsMay(String[] keys, String defaultValue) {
		return getStringsMayBySeparator(keys, defaultValue, DEFAULT_VALUE_SEPARATOR);
	}

	public Integer getIntegerMust(String key) {
		final String value = getValue(key);
		if (!hasValue(value)) {
			throw new NoSuchElementException("Key " + key + " does not exist");
		}
		return Integer.valueOf(value);
	}

	public Integer getIntegerMay(String key, Integer defaultValue) {
		final String value = getValue(key);
		return hasValue(value) ? Integer.valueOf(value) : defaultValue;
	}

	public Integer getIntegerMust(String[] keys) {
		final String value = getValue(keys);
		if (!hasValue(value)) {
			throw new NoSuchElementException("Keys " + Arrays.toString(keys) + " does not exist");
		}
		return Integer.valueOf(value);
	}

	public Integer getIntegerMay(String[] keys, Integer defaultValue) {
		final String value = getValue(keys);
		return hasValue(value) ? Integer.valueOf(value) : defaultValue;
	}

	public Long getLongMust(String key) {
		final String value = getValue(key);
		if (!hasValue(value)) {
			throw new NoSuchElementException("Key " + key + " does not exist");
		}
		return Long.valueOf(value);
	}

	public Long getLongMay(String key, Long defaultValue) {
		final String value = getValue(key);
		return hasValue(value) ? Long.valueOf(value) : defaultValue;
	}

	public Long getLongMust(String[] keys) {
		final String value = getValue(keys);
		if (!hasValue(value)) {
			throw new NoSuchElementException("Keys " + Arrays.toString(keys) + " does not exist");
		}
		return Long.valueOf(value);
	}

	public Long getLongMay(String[] keys, Long defaultValue) {
		final String value = getValue(keys);
		return hasValue(value) ? Long.valueOf(value) : defaultValue;
	}

	public Double getDoubleMust(String key) {
		final String value = getValue(key);
		if (!hasValue(value)) {
			throw new NoSuchElementException("Key " + key + " does not exist");
		}
		return Double.valueOf(value);
	}

	public Double getDoubleMay(String key, Double defaultValue) {
		final String value = getValue(key);
		return hasValue(value) ? Double.valueOf(value) : defaultValue;
	}

	public Double getDoubleMust(String[] keys) {
		final String value = getValue(keys);
		if (!hasValue(value)) {
			throw new NoSuchElementException("Keys " + Arrays.toString(keys) + " does not exist");
		}
		return Double.valueOf(value);
	}

	public Double getDoubleMay(String[] keys, Double defaultValue) {
		final String value = getValue(keys);
		return hasValue(value) ? Double.valueOf(value) : defaultValue;
	}

	public Boolean getBooleanMust(String key) {
		final String value = getValue(key);
		if (!hasValue(value)) {
			throw new NoSuchElementException("Key " + key + " does not exist");
		}
		return Boolean.valueOf(value);
	}

	public Boolean getBooleanMay(String key, Boolean defaultValue) {
		final String value = getValue(key);
		return hasValue(value) ? Boolean.valueOf(value) : defaultValue;
	}

	public Boolean getBooleanMust(String[] keys) {
		final String value = getValue(keys);
		if (!hasValue(value)) {
			throw new NoSuchElementException("Keys " + Arrays.toString(keys) + " does not exist");
		}
		return Boolean.valueOf(value);
	}

	public Boolean getBooleanMay(String[] keys, Boolean defaultValue) {
		final String value = getValue(keys);
		return hasValue(value) ? Boolean.valueOf(value) : defaultValue;
	}

	private String getValue(String key) {
		if (systemPrefered) {
			final String value = System.getProperty(key);
			return hasValue(value) ? value : properties.getProperty(key);
		} else {
			final String value = properties.getProperty(key);
			return hasValue(value) ? value : System.getProperty(key);
		}
	}

	private String getValue(String[] keys) {
		for (String key : keys) {
			final String value = getValue(key);
			if (hasValue(value)) {
				return value;
			}
		}

		return null;
	}

	private boolean hasValue(String value) {
		return blankAllowed ? value != null : StringUtils.isNotBlank(value);
	}
}
