package com.itdoes.common.extension.codegenerator.java;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.itdoes.common.core.Constants;

/**
 * @author Jalen Zhong
 */
public class Field {
	private final String name;
	private final String upperName;
	private final String type;

	public Field(String name, String type) {
		Validate.notBlank(name, "Field name is blank");
		Validate.notBlank(type, "Field type is blank");

		if (Constants.JAVA_KEYWORDS.contains(name)) {
			throw new IllegalArgumentException("Field [" + name + "] is a Java keyword");
		}

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
