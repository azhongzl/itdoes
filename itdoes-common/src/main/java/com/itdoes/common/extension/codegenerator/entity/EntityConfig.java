package com.itdoes.common.extension.codegenerator.entity;

import java.util.List;

import com.itdoes.common.core.jdbc.meta.Column;
import com.itdoes.common.extension.codegenerator.Field;

/**
 * @author Jalen Zhong
 */
public class EntityConfig {
	public static class EntityField extends Field {
		private final Column column;
		private final boolean secureColumn;

		public EntityField(String name, String type, Column column, boolean secureColumn) {
			super(name, type);
			this.column = column;
			this.secureColumn = secureColumn;
		}

		public Column getColumn() {
			return column;
		}

		public boolean isSecureColumn() {
			return secureColumn;
		}
	}

	private final String packageName;
	private final boolean hasSecureColumn;
	private final String tableName;
	private final String className;
	private final String serialVersionUID;
	private final List<EntityField> fieldList;
	private final String idGeneratedValue;

	public EntityConfig(String packageName, boolean hasSecureColumn, String tableName, String className,
			String serialVersionUID, List<EntityField> fieldList, String idGeneratedValue) {
		this.packageName = packageName;
		this.hasSecureColumn = hasSecureColumn;
		this.tableName = tableName;
		this.className = className;
		this.serialVersionUID = serialVersionUID;
		this.fieldList = fieldList;
		this.idGeneratedValue = idGeneratedValue;
	}

	public String getPackageName() {
		return packageName;
	}

	public boolean isHasSecureColumn() {
		return hasSecureColumn;
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
