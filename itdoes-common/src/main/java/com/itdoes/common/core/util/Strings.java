package com.itdoes.common.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
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

	public static String substring(String str, String toFind) {
		if (StringUtils.isBlank(str) || StringUtils.isBlank(toFind)) {
			return str;
		}

		final int index = str.indexOf(toFind);
		if (index == -1) {
			return str;
		}

		return str.substring(index + toFind.length());
	}

	public static String[] splitNoLimit(String value, String separator) {
		if (StringUtils.isBlank(value)) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}

		if (separator.equals("|")) {
			separator = "\\|";
		}
		return value.split(separator, -1);
	}

	public static String keepOneSpace(String value) {
		return value.trim().replaceAll("[\\s|\t]+", " ");
	}

	private Strings() {
	}
}
