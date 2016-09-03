package com.itdoes.common.extension.codegenerator.java;

import java.io.File;
import java.io.IOException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itdoes.common.core.Constants;
import com.itdoes.common.core.freemarker.FreeMarkers;
import com.itdoes.common.core.jdbc.meta.Column;
import com.itdoes.common.core.jdbc.meta.MetaParser;
import com.itdoes.common.core.jdbc.meta.Table;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.Exceptions;
import com.itdoes.common.core.util.Files;
import com.itdoes.common.core.util.Urls;

import freemarker.template.Template;

/**
 * @author Jalen Zhong
 */
public class JavaGenerators {
	private static final String DEFAULT_GENERATED_VALUE = "@GeneratedValue(strategy = GenerationType.AUTO)";

	private static final Map<Integer, String> TYPE_MAPPING = Maps.newHashMap();
	static {
		TYPE_MAPPING.put(Types.INTEGER, "int");
		TYPE_MAPPING.put(Types.BIGINT, "long");
		TYPE_MAPPING.put(Types.DATE, "Date");
		TYPE_MAPPING.put(Types.TIME, "Date");
		TYPE_MAPPING.put(Types.TIMESTAMP, "Date");
	}

	public static void generateEntitiesAndDaos(String jdbcDriver, String jdbcUrl, String jdbcUsername,
			String jdbcPassword, String outputDir, String basePackageName, Map<String, String> tableMapping,
			Map<String, String> columnMapping, String generatedValue) {
		final MetaParser parser = new MetaParser(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
		final List<Table> tableList = parser.parseTables();

		final String templateDir = "classpath:/" + JavaGenerators.class.getPackage().getName().replace(".", "/");
		final Template entityTemplate = FreeMarkers.getTemplate(templateDir, "JavaEntity.ftl");
		final Template daoTemplate = FreeMarkers.getTemplate(templateDir, "JavaDao.ftl");

		final String entityPackageName = basePackageName + ".entity";
		final String daoPackageName = basePackageName + ".repository";

		final String entityDir = Urls.concat(Files.toUnixPath(outputDir), entityPackageName.replace(".", "/"));
		final String daoDir = Urls.concat(Files.toUnixPath(outputDir), daoPackageName.replace(".", "/"));

		for (Table table : tableList) {
			final String tableName = table.getName();
			final String className = mapClassName(tableName, tableMapping);
			final List<JavaField> fieldList = mapFieldList(tableName, table.getColumnList(), columnMapping);

			final Map<String, Object> model = Maps.newHashMap();
			model.put("packageName", entityPackageName);
			model.put("tableName", tableName);
			model.put("className", className);
			model.put("fieldList", fieldList);
			model.put("generatedValue", StringUtils.isBlank(generatedValue) ? DEFAULT_GENERATED_VALUE : generatedValue);
			final String result = FreeMarkers.render(entityTemplate, model);

			try {
				FileUtils.writeStringToFile(new File(entityDir, className + ".java"), result, Constants.UTF8_CHARSET);
			} catch (IOException e) {
				throw Exceptions.unchecked(e);
			}
		}
	}

	private static String mapClassName(String tableName, Map<String, String> tableMapping) {
		if (!Collections3.isEmpty(tableMapping)) {
			if (tableMapping.containsKey(tableName)) {
				return tableMapping.get(tableName);
			}
		}

		return StringUtils.capitalize(tableName);
	}

	private static List<JavaField> mapFieldList(String tableName, List<Column> columnList,
			Map<String, String> columnMapping) {
		final List<JavaField> fieldList = Lists.newArrayList();
		for (Column column : columnList) {
			String fieldName = column.getName();
			if (!Collections3.isEmpty(columnMapping)) {
				final String key = tableName + "." + column.getName();
				if (columnMapping.containsKey(key)) {
					fieldName = columnMapping.get(key);
				}
			}

			final JavaField field = new JavaField(fieldName, mapType(column.getType().getId()), column.isPk());
			fieldList.add(field);
		}
		return fieldList;
	}

	private static String mapType(int typeId) {
		if (TYPE_MAPPING.containsKey(typeId)) {
			return TYPE_MAPPING.get(typeId);
		}

		return "String";
	}

	private JavaGenerators() {
	}
}
