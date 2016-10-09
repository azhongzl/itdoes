package com.itdoes.common.extension.codegenerator.entity;

import java.util.List;

import com.itdoes.common.core.jdbc.meta.Column;
import com.itdoes.common.extension.codegenerator.Field;

/**
 * @author Jalen Zhong
 */
public class EntityModel {
	public static class EntityField extends Field {
		private final Column column;
		private final boolean perm;
		private final boolean upload;
		private final String searchConfig;

		public EntityField(String name, String type, Column column, boolean perm, boolean upload, String searchConfig) {
			super(name, type);
			this.column = column;
			this.perm = perm;
			this.upload = upload;
			this.searchConfig = searchConfig;
		}

		public Column getColumn() {
			return column;
		}

		public boolean isPerm() {
			return perm;
		}

		public boolean isUpload() {
			return upload;
		}

		public String getSearchConfig() {
			return searchConfig;
		}
	}

	private final String packageName;
	private final String tableName;
	private final String searchConfig;
	private final String className;
	private final String serialVersionUID;
	private final List<EntityField> fieldList;
	private final String idGeneratedValue;

	public EntityModel(String packageName, String tableName, String searchConfig, String className,
			String serialVersionUID, List<EntityField> fieldList, String idGeneratedValue) {
		this.packageName = packageName;
		this.tableName = tableName;
		this.searchConfig = searchConfig;
		this.className = className;
		this.serialVersionUID = serialVersionUID;
		this.fieldList = fieldList;
		this.idGeneratedValue = idGeneratedValue;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getSearchConfig() {
		return searchConfig;
	}

	public String getClassName() {
		return className;
	}

	public String getSerialVersionUID() {
		return serialVersionUID;
	}

	public List<EntityField> getFieldList() {
		return fieldList;
	}

	public String getIdGeneratedValue() {
		return idGeneratedValue;
	}
}
