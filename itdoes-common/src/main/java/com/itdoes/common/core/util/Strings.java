package com.itdoes.common.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Jalen Zhong
 */
public class Strings {
	private static final Pattern CAMEL_PATTERN = Pattern.compile("[A-Z]");
	private static final Pattern UNDERSCORE_PATTERN = Pattern.compile("_[a-z]");

	public static String camelToUnderscore(String str) {
		if (StringUtils.isBlank(str)) {
			return str;
		}

		final StringBuilder builder = new StringBuilder(str);
		final Matcher matcher = CAMEL_PATTERN.matcher(str);
		for (int i = 0; matcher.find(); i++) {
			builder.replace(matcher.start() + i, matcher.end() + i, "_" + matcher.group().toLowerCase());
		}
		if (builder.charAt(0) == '_') {
			builder.deleteCharAt(0);
		}
		return builder.toString();
	}

	public static String underscoreToCamel(String str) {
		if (StringUtils.isBlank(str)) {
			return str;
		}

		final StringBuilder builder = new StringBuilder(str);
		final Matcher matcher = UNDERSCORE_PATTERN.matcher(str);
		for (int i = 0; matcher.find(); i++) {
			builder.replace(matcher.start() - i, matcher.end() - i, matcher.group().substring(1).toUpperCase());
		}
		if (Character.isUpperCase(builder.charAt(0))) {
			builder.replace(0, 1, String.valueOf(Character.toLowerCase(builder.charAt(0))));
		}
		return builder.toString();
	}

	public static String underscoreToPascal(String str) {
		return StringUtils.capitalize(underscoreToCamel(str));
	}

	private Strings() {
	}
}
