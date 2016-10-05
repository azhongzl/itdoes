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
		private final boolean secure;
		private final boolean upload;

		public EntityField(String name, String type, Column column, boolean secure, boolean upload) {
			super(name, type);
			this.column = column;
			this.secure = secure;
			this.upload = upload;
		}

		public Column getColumn() {
			return column;
		}

		public boolean isSecure() {
			return secure;
		}

		public boolean isUpload() {
			return upload;
		}
	}

	private final String packageName;
	private final String tableName;
	private final String className;
	private final String serialVersionUID;
	private final List<EntityField> fieldList;
	private final String idGeneratedValue;

	public EntityModel(String packageName, String tableName, String className, String serialVersionUID,
			List<EntityField> fieldList, String idGeneratedValue) {
		this.packageName = packageName;
		this.tableName = tableName;
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
