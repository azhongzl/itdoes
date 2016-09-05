package com.itdoes.common.extension.codegenerator.java;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.PropertiesLoader;

/**
 * @author Jalen Zhong
 */
public class EntityGeneratorHelper {
	private static final String OUTPUT_DIR = "/codegenerator/java";
	private static final String CONFIG_DIR = "classpath:/codegenerator/java/";
	private static final String TABLE_MAPPING_FILE = CONFIG_DIR + "table.mapping.properties";
	private static final String COLUMN_MAPPING_FILE = CONFIG_DIR + "column.mapping.properties";
	private static final String COLUMN_SECURE_MAPPING_FILE = CONFIG_DIR + "column.secure.mapping.properties";

	public static void generateEntities(String basePackageName, String idGeneratedValue) {
		final PropertiesLoader pl = new PropertiesLoader("classpath:/application.properties",
				"classpath:/application.local.properties");
		final Map<String, String> tableMapping = toMap(TABLE_MAPPING_FILE);
		final Map<String, String> columnMapping = toMap(COLUMN_MAPPING_FILE);
		final List<String> secureColumnList = getSecureColumnList();

		EntityGenerator.generateEntities(pl.getProperty("jdbc.driver"), pl.getProperty("jdbc.url"),
				pl.getProperty("jdbc.username"), pl.getProperty("jdbc.password"), OUTPUT_DIR, basePackageName,
				tableMapping, columnMapping, secureColumnList, idGeneratedValue);
	}

	private static List<String> getSecureColumnList() {
		final List<String> secureColumnList = Lists.newArrayList();
		final Map<String, String> secureColumnMapping = toMap(COLUMN_SECURE_MAPPING_FILE);
		for (Entry<String, String> entry : secureColumnMapping.entrySet()) {
			final String tableName = entry.getKey();
			final String columnNamesStr = entry.getValue();
			if (StringUtils.isNotBlank(columnNamesStr)) {
				final String[] columnNames = StringUtils.split(columnNamesStr, ",");
				if (!Collections3.isEmpty(columnNames)) {
					for (String columnName : columnNames) {
						secureColumnList.add(tableName + "." + columnName);
					}
				}
			}
		}
		return secureColumnList;
	}

	private static Map<String, String> toMap(String propertyFilename) {
		return Collections3.toMap(new PropertiesLoader(propertyFilename).getProperties());
	}
}
