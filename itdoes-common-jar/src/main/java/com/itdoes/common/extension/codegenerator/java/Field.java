package com.itdoes.common.extension.codegenerator.java;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Jalen Zhong
 */
public class Field {
	private final String name;
	private final String upperName;
	private final String type;

	public Field(String name, String type) {
		this.name = name;
		this.upperName = StringUtils.capitalize(name);
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public String getUpperName() {
		return upperName;
	}

	public String getType() {
		return type;
	}
}
