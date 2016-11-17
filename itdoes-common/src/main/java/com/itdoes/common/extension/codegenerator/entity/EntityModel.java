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
		private final String perm;
		private final String search;
		private final boolean upload;

		public EntityField(String name, String type, Column column, String perm, String search, boolean upload) {
			super(name, type);
			this.column = column;
			this.perm = perm;
			this.search = search;
			this.upload = upload;
		}

		public Column getColumn() {
			return column;
		}

		public String getPerm() {
			return perm;
		}

		public String getSearch() {
			return search;
		}

		public boolean isUpload() {
			return upload;
		}
	}

	private final String packageName;
	private final String tableName;
	private final String perm;
	private final String search;
	private final String className;
	private final String serialVersionUID;
	private final List<EntityField> fieldList;
	private final String idGeneratedValue;
	private final String extension;

	public EntityModel(String packageName, String tableName, String perm, String search, String className,
			String serialVersionUID, List<EntityField> fieldList, String idGeneratedValue, String extension) {
		this.packageName = packageName;
		this.tableName = tableName;
		this.perm = perm;
		this.search = search;
		this.className = className;
		this.serialVersionUID = serialVersionUID;
		this.fieldList = fieldList;
		this.idGeneratedValue = idGeneratedValue;
		this.extension = extension;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getPerm() {
		return perm;
	}

	public String getSearch() {
		return search;
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

	public String getExtension() {
		return extension;
	}
}
