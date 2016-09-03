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
	private static final String PROPERTY_FILE_PREFIX = "classpath:/extension/codegenerator/java/";

	public static void generateEntities(String basePackageName, String idGeneratedValue) {
		final PropertiesLoader pl = new PropertiesLoader("classpath:/application.properties",
				"classpath:/application.local.properties");
		final Map<String, String> tableMapping = Collections3
				.toMap(new PropertiesLoader(PROPERTY_FILE_PREFIX + "table.mapping.properties").getProperties());
		final Map<String, String> columnMapping = Collections3
				.toMap(new PropertiesLoader(PROPERTY_FILE_PREFIX + "column.mapping.properties").getProperties());
		final List<String> secureColumnList = getSecureColumnList();

		EntityGenerator.generateEntities(pl.getProperty("jdbc.driver"), pl.getProperty("jdbc.url"),
				pl.getProperty("jdbc.username"), pl.getProperty("jdbc.password"), OUTPUT_DIR, basePackageName,
				tableMapping, columnMapping, secureColumnList, idGeneratedValue);
	}

	private static List<String> getSecureColumnList() {
		final List<String> secureColumnList = Lists.newArrayList();
		final Map<String, String> secureColumnMapping = Collections3
				.toMap(new PropertiesLoader(PROPERTY_FILE_PREFIX + "column.secure.mapping.properties").getProperties());
		for (Entry<String, String> entry : secureColumnMapping.entrySet()) {
			final String tableName = entry.getKey();
			final String columnsStr = entry.getValue();
			if (StringUtils.isNotBlank(columnsStr)) {
				final String[] columns = StringUtils.split(columnsStr, ",");
				if (!Collections3.isEmpty(columns)) {
					for (String column : columns) {
						secureColumnList.add(tableName + "." + column);
					}
				}
			}
		}
		return secureColumnList;
	}
}
