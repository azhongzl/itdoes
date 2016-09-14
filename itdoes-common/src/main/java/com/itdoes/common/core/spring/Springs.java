package com.itdoes.common.core.spring;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * @author Jalen Zhong
 */
public class Springs {
	public static String getBeanName(String classSimpleName) {
		Validate.notBlank(classSimpleName, "Class simple name is blank");

		return StringUtils.uncapitalize(classSimpleName);
	}

	public static String getBeanName(Class<?> clazz) {
		Validate.notNull(clazz, "Class is null");

		return getBeanName(clazz.getSimpleName());
	}

	private Springs() {
	}
}
