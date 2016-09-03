package com.itdoes.common.extension.codegenerator.java;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Jalen Zhong
 */
public class Field {
	private final String name;
	private final String upperName;
	private final String type;
	private final boolean pk;
	private final boolean secureColumn;

	public Field(String name, String type, boolean pk, boolean secureColumn) {
		this.name = name;
		this.upperName = StringUtils.capitalize(name);
		this.type = type;
		this.pk = pk;
		this.secureColumn = secureColumn;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public boolean isPk() {
		return pk;
	}

	public String getUpperName() {
		return upperName;
	}

	public boolean isSecureColumn() {
		return secureColumn;
	}
}
