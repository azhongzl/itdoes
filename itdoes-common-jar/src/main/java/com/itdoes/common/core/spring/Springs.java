package com.itdoes.common.core.spring;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * @author Jalen Zhong
 */
public class Springs {
	public static String getBeanId(String classSimpleName) {
		Validate.notBlank(classSimpleName, "Class simple name is blank");

		return StringUtils.uncapitalize(classSimpleName);
	}

	public static String getBeanId(Class<?> clazz) {
		Validate.notNull(clazz, "Class is null");

		return getBeanId(clazz.getSimpleName());
	}

	private Springs() {
	}
}
