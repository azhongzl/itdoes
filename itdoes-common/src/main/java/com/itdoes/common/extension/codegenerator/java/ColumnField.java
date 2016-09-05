package com.itdoes.common.extension.codegenerator.java;

import com.itdoes.common.core.jdbc.meta.Column;

/**
 * @author Jalen Zhong
 */
public class ColumnField extends Field {
	private final Column column;
	private final boolean secureColumn;

	public ColumnField(String name, String type, Column column, boolean secureColumn) {
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
