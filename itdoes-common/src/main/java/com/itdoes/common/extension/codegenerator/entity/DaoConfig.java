package com.itdoes.common.extension.codegenerator.entity;

/**
 * @author Jalen Zhong
 */
public class DaoConfig {
	private final String packageName;
	private final String entityPackageName;
	private final String entityClassName;
	private final String className;
	private final String entityIdType;

	public DaoConfig(String packageName, String entityPackageName, String entityClassName, String className,
			String entityIdType) {
		this.packageName = packageName;
		this.entityPackageName = entityPackageName;
		this.entityClassName = entityClassName;
		this.className = className;
		this.entityIdType = entityIdType;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getEntityPackageName() {
		return entityPackageName;
	}

	public String getEntityClassName() {
		return entityClassName;
	}

	public String getClassName() {
		return className;
	}

	public String getEntityIdType() {
		return entityIdType;
	}
}