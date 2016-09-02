package com.itdoes.common.extension.codegenerator.java;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Jalen Zhong
 */
public class JavaField {
	private final String name;
	private final String upperName;
	private final String type;
	private final boolean pk;

	public JavaField(String name, String type, boolean pk) {
		this.name = name;
		this.upperName = StringUtils.capitalize(name);
		this.type = type;
		this.pk = pk;
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
}
