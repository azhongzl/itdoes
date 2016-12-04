package com.itdoes.common.extension.codegenerator.entity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

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
import com.itdoes.common.extension.codegenerator.entity.config.ConstraintConfig;
import com.itdoes.common.extension.codegenerator.entity.config.EhcacheConfig;
import com.itdoes.common.extension.codegenerator.entity.config.MappingConfig;
import com.itdoes.common.extension.codegenerator.entity.config.PermConfig;
import com.itdoes.common.extension.codegenerator.entity.config.QueryCacheConfig;
import com.itdoes.common.extension.codegenerator.entity.config.SearchConfig;
import com.itdoes.common.extension.codegenerator.entity.config.SkipConfig;
import com.itdoes.common.extension.codegenerator.entity.config.UploadConfig;
import com.itdoes.common.extension.codegenerator.entity.model.DaoModel;
import com.itdoes.common.extension.codegenerator.entity.model.EhcacheModel;
import com.itdoes.common.extension.codegenerator.entity.model.EntityModel;
import com.itdoes.common.extension.codegenerator.entity.model.EntityModel.EntityField;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author Jalen Zhong
 */
public class EntityGenerator {
	public static final String AUTO_ID_GENERATED_VALUE = "@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)";
	public static final String UUID_ID_GENERATED_VALUE = "@javax.persistence.GeneratedValue";
	public static final String EMPTY_ID_GENERATED_VALUE = "";

	public static final String RELATIVE_ENTITY_PACKAGE_NAME = "entity";
	public static final String RELATIVE_DAO_PACKAGE_NAME = "dao";

	private static final String FTL_ENTITY = "Entity.ftl";
	private static final String FTL_DAO = "Dao.ftl";
	private static final String FTL_EHCACHE = "ehcache.ftl";

	private static final String TEMPLATE_DIR = "classpath:/" + Reflections.packageToPath(EntityGenerator.class);

	public static void generateEntities(String jdbcDriver, String jdbcUrl, String jdbcUsername, String jdbcPassword,
			Class<?> loaderClass, String outputDir, String basePackageName, String idGeneratedValue,
			SkipConfig skipConfig, MappingConfig mappingConfig, ConstraintConfig constraintConfig,
			PermConfig permConfig, SearchConfig searchConfig, UploadConfig uploadConfig,
			QueryCacheConfig queryCacheConfig, EhcacheConfig ehcacheConfig) {
		final Configuration freeMarkerConfig = FreeMarkers.buildConfiguration(TEMPLATE_DIR);

		final String baseExtensionDir = getBaseExtensionDir(loaderClass, basePackageName);

		final String entityPackageName = basePackageName + "." + RELATIVE_ENTITY_PACKAGE_NAME;
		final String entityDir = getPackageDir(outputDir, entityPackageName);
		final Template entityTemplate = getTemplate(freeMarkerConfig, FTL_ENTITY);

		final String daoPackageName = basePackageName + "." + RELATIVE_DAO_PACKAGE_NAME;
		final String daoDir = getPackageDir(outputDir, daoPackageName);
		final Template daoTemplate = getTemplate(freeMarkerConfig, FTL_DAO);

		final EhcacheModel ehcacheModel = ehcacheConfig.newModel();

		final MetaParser parser = new MetaParser(jdbcDriver, jdbcUrl, jdbcUsername, jdbcPassword);
		final List<Table> tableList = parser.parseTables();
		for (Table table : tableList) {
			final String tableName = table.getName();

			if (skipConfig.isTableSkip(tableName)) {
				continue;
			}

			// Generate Entity
			final String entityClassName = mapEntityClassName(tableName, mappingConfig);
			final EntityModel entityModel = new EntityModel(entityPackageName, tableName,
					permConfig.getEntityPerm(tableName), searchConfig.getEntitySearch(tableName), entityClassName,
					getSerialVersionUIDStr(entityClassName), idGeneratedValue,
					getEntityExtension(baseExtensionDir, entityClassName));
			populateEntityFieldList(entityModel, tableName, table.getColumnList(), mappingConfig, constraintConfig,
					permConfig, searchConfig, uploadConfig);
			final String entityString = FreeMarkers.render(entityTemplate, entityModel);
			writeJavaFile(entityDir, entityClassName, entityString);

			// Generate Dao
			final String daoClassName = EntityEnv.getDaoClassSimpleName(entityClassName);
			final boolean queryCacheEnabled = queryCacheConfig.isEntityEnabled(tableName);
			final DaoModel daoModel = new DaoModel(daoPackageName, entityPackageName, entityClassName, daoClassName,
					queryCacheEnabled, mapIdType(tableName, entityModel.getFieldList()),
					getDaoExtension(baseExtensionDir, daoClassName));
			final String daoString = FreeMarkers.render(daoTemplate, daoModel);
			writeJavaFile(daoDir, daoClassName, daoString);

			// Generate ehcache cache
			final String cache = ehcacheConfig.newEntityCache(tableName, entityPackageName, entityClassName);
			if (cache != null) {
				ehcacheModel.addCache(cache);
			}
		}

		final String ehcacheDir = Files.toUnixPath(outputDir);
		final Template ehcacheTemplate = getTemplate(freeMarkerConfig, FTL_EHCACHE);
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

	private static String mapEntityClassName(String tableName, MappingConfig mappingConfig) {
		final String mappingEntityClassName = mappingConfig.getEntity(tableName);
		return mappingEntityClassName != null ? mappingEntityClassName : Strings.underscoreToPascal(tableName);
	}

	private static void populateEntityFieldList(EntityModel entityModel, String tableName, List<Column> columnList,
			MappingConfig mappingConfig, ConstraintConfig constraintConfig, PermConfig permConfig,
			SearchConfig searchConfig, UploadConfig uploadConfig) {
		final List<EntityField> entityFieldList = Lists.newArrayList();
		for (Column column : columnList) {
			final EntityField entityField = new EntityField(mapFieldName(tableName, column.getName(), mappingConfig),
					mapFieldType(column), column, constraintConfig.getFieldConstraint(tableName, column.getName()),
					permConfig.getFieldPerm(tableName, column.getName()),
					searchConfig.getFieldSearch(tableName, column.getName()),
					uploadConfig.getFieldUpload(tableName, column.getName()));
			entityFieldList.add(entityField);
		}

		entityModel.setFieldList(entityFieldList);
	}

	private static String mapFieldName(String tableName, String columnName, MappingConfig mappingConfig) {
		final String mappingFieldName = mappingConfig.getField(tableName, columnName);
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

	private static String getEntityExtension(String baseExtensionDir, String entityClassName) {
		return readExtension(baseExtensionDir, RELATIVE_ENTITY_PACKAGE_NAME, entityClassName);
	}

	private static String getDaoExtension(String baseExtensionDir, String daoClassName) {
		return readExtension(baseExtensionDir, RELATIVE_DAO_PACKAGE_NAME, daoClassName);
	}

	private static String readExtension(String baseExtensionDir, String relativePackageName, String className) {
		final File file = new File(baseExtensionDir + relativePackageName + "/" + className + ".java");
		if (!file.exists()) {
			return null;
		}

		try {
			final List<String> lineList = FileUtils.readLines(file, Constants.UTF8_CHARSET);
			int fromIndex = -1;
			int toIndex = -1;
			for (int i = 0; i < lineList.size(); i++) {
				if (lineList.get(i).contains(className)) {
					fromIndex = i + 1;
					break;
				}
			}
			Validate.isTrue(fromIndex != -1, "Cannot find class name: [%s]", className);
			for (int i = lineList.size() - 1; i >= 0; i--) {
				if (lineList.get(i).contains("}")) {
					toIndex = i;
					break;
				}
			}
			Validate.isTrue(toIndex != -1, "Cannot find end [}]");
			return StringUtils.join(lineList.subList(fromIndex, toIndex), "\n");
		} catch (IOException e) {
			throw Exceptions.unchecked(e);
		}
	}

	private static String getBaseExtensionDir(Class<?> loaderClass, String basePackageName) {
		final String loaderLocation = loaderClass.getProtectionDomain().getCodeSource().getLocation().getPath();
		final int index = loaderLocation.indexOf("/target/");
		final String baseExtenstionDir = loaderLocation.substring(0, index) + "/src/test/java/"
				+ basePackageName.replace('.', '/') + "/codegenerator/entity/";
		return baseExtenstionDir;
	}

	private EntityGenerator() {
	}
}
