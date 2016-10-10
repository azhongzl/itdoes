package com.itdoes.common.extension.codegenerator.entity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.core.Constants;
import com.itdoes.common.core.freemarker.FreeMarkers;
import com.itdoes.common.core.jdbc.CustomSqlTypes;
import com.itdoes.common.core.jdbc.meta.Column;
import com.itdoes.common.core.jdbc.meta.MetaParser;
import com.itdoes.common.core.jdbc.meta.Table;
import com.itdoes.common.core.util.Exceptions;
import com.itdoes.common.core.util.Files;
import com.itdoes.common.core.util.Reflections;
import com.itdoes.common.core.util.Strings;
import com.itdoes.common.core.util.Urls;
import com.itdoes.common.extension.codegenerator.entity.EntityModel.EntityField;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author Jalen Zhong
 */
public class EntityGenerator {
	public static final String AUTO_ID_GENERATED_VALUE = "@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)";
	public static final String UUID_ID_GENERATED_VALUE = "@javax.persistence.GeneratedValue";
	public static final String EMPTY_ID_GENERATED_VALUE = "";

	private static final String TEMPLATE_DIR = "classpath:/" + Reflections.packageToPath(EntityGenerator.class);

	public static void generateEntities(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword,
			String outputDir, String basePackageName, String idGeneratedValue, DbSkipConfig dbSkipConfig,
			DbMappingConfig dbMappingConfig, DbPermConfig dbPermConfig, DbUploadConfig dbUploadConfig,
			DbSearchConfig dbSearchConfig, EntityQueryCacheConfig entityQueryCacheConfig,
			EntityEhcacheConfig entityEhcacheConfig) {
		final Configuration freeMarkerConfig = FreeMarkers.buildConfiguration(TEMPLATE_DIR);

		final String entityPackageName = basePackageName + ".entity";
		final String entityDir = getPackageDir(outputDir, entityPackageName);
		final Template entityTemplate = getTemplate(freeMarkerConfig, "Entity.ftl");

		final String daoPackageName = basePackageName + ".dao";
		final String daoDir = getPackageDir(outputDir, daoPackageName);
		final Template daoTemplate = getTemplate(freeMarkerConfig, "Dao.ftl");

		final EhcacheModel ehcacheModel = entityEhcacheConfig.newModel();

		final MetaParser parser = new MetaParser(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
		final List<Table> tableList = parser.parseTables();
		for (Table table : tableList) {
			final String tableName = table.getName();

			if (dbSkipConfig.isSkip(tableName)) {
				continue;
			}

			// Generate Entity
			final String entityClassName = mapEntityClassName(tableName, dbMappingConfig);
			final List<EntityField> entityFieldList = mapEntityFieldList(tableName, table.getColumnList(),
					dbMappingConfig, dbPermConfig, dbUploadConfig, dbSearchConfig);
			final EntityModel entityModel = new EntityModel(entityPackageName, tableName,
					dbSearchConfig.getTableSearchConfig(tableName), entityClassName,
					getSerialVersionUIDStr(entityClassName), entityFieldList, idGeneratedValue);
			final String entityString = FreeMarkers.render(entityTemplate, entityModel);
			writeJavaFile(entityDir, entityClassName, entityString);

			// Generate Dao
			final String daoClassName = EntityEnv.getDaoClassName(entityClassName);
			final boolean queryCacheEnabled = entityQueryCacheConfig.isEnabled(entityClassName);
			final DaoModel daoModel = new DaoModel(daoPackageName, entityPackageName, entityClassName, daoClassName,
					queryCacheEnabled, mapIdType(tableName, entityFieldList));
			final String daoString = FreeMarkers.render(daoTemplate, daoModel);
			writeJavaFile(daoDir, daoClassName, daoString);

			// Generate ehcache cache
			final String cache = entityEhcacheConfig.newCache(entityPackageName, entityClassName);
			ehcacheModel.addCache(cache);
		}

		final String ehcacheDir = Files.toUnixPath(outputDir);
		final Template ehcacheTemplate = getTemplate(freeMarkerConfig, "ehcache.ftl");
		final String ehcacheString = FreeMarkers.render(ehcacheTemplate, ehcacheModel);
		writeEhcacheFile(ehcacheDir, ehcacheModel.getName(), ehcacheString);
	}

	private static String getPackageDir(String outputDir, String packageName) {
		return Urls.concat(Files.toUnixPath(outputDir), Reflections.packageToPath(packageName));
	}

	private static Template getTemplate(Configuration freeMarkerConfig, String templateName) {
		return FreeMarkers.getTemplate(freeMarkerConfig, templateName);
	}

	private static String getSerialVersionUIDStr(String className) {
		return String.valueOf(className.hashCode());
	}

	private static void writeJavaFile(String dir, String className, String content) {
		writeFile(dir, getJavaFilename(className), content);
	}

	private static String getJavaFilename(String className) {
		return className + ".java";
	}

	private static void writeEhcacheFile(String dir, String ehcacheName, String content) {
		writeFile(dir, getEhcacheFilename(ehcacheName), content);
	}

	private static String getEhcacheFilename(String ehcacheName) {
		return (StringUtils.isNotBlank(ehcacheName) ? ehcacheName : "ehcache") + ".xml";
	}

	private static void writeFile(String dir, String filename, String content) {
		try {
			FileUtils.writeStringToFile(new File(dir, filename), content, Constants.UTF8_CHARSET);
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	private static String mapEntityClassName(String tableName, DbMappingConfig dbMappingConfig) {
		final String mappingEntityClassName = dbMappingConfig.toEntity(tableName);
		return mappingEntityClassName != null ? mappingEntityClassName : Strings.underscoreToPascal(tableName);
	}

	private static List<EntityField> mapEntityFieldList(String tableName, List<Column> columnList,
			DbMappingConfig dbMappingConfig, DbPermConfig dbPermConfig, DbUploadConfig dbUploadConfig,
			DbSearchConfig dbSearchConfig) {
		final List<EntityField> entityFieldList = Lists.newArrayList();
		for (Column column : columnList) {
			final EntityField entityField = new EntityField(mapFieldName(tableName, column.getName(), dbMappingConfig),
					mapFieldType(column), column, dbPermConfig.isPermField(tableName, column.getName()),
					dbUploadConfig.isUploadField(tableName, column.getName()),
					dbSearchConfig.getColumnSearchConfig(tableName, column.getName()));
			entityFieldList.add(entityField);
		}

		return entityFieldList;
	}

	private static String mapFieldName(String tableName, String columnName, DbMappingConfig dbMappingConfig) {
		final String mappingFieldName = dbMappingConfig.toField(tableName, columnName);
		return mappingFieldName != null ? mappingFieldName : Strings.underscoreToCamel(columnName);
	}

	private static String mapFieldType(Column column) {
		final Class<?> typeClass = CustomSqlTypes.getSqlJavaTypeMap().get(column.getType().getId());

		if (typeClass != null) {
			final String typeClassName = typeClass.getName();
			if (typeClassName.startsWith("java.lang")) {
				return typeClass.getSimpleName();
			} else {
				return typeClassName;
			}
		}

		return "String";
	}

	private static String mapIdType(String tableName, List<EntityField> entityFieldList) {
		int pkSum = 0;
		String pkType = null;
		for (EntityField entityField : entityFieldList) {
			if (entityField.getColumn().isPk()) {
				pkSum++;
				pkType = entityField.getType();
			}
		}

		if (pkSum == 0) {
			throw new IllegalArgumentException("There is no primary key in table [" + tableName + "]");
		}
		if (pkSum > 1) {
			throw new IllegalArgumentException("There are [" + pkSum + "] primary keys in table [" + tableName
					+ "]. Every table should contain only 1 primary key");
		}

		return pkType;
	}

	private EntityGenerator() {
	}
}
