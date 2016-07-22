package com.itdoes.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

/**
 * @author Jalen Zhong
 */
public class Validators {
	public static <T> void validateWithException(Validator validator, T object, Class<?>... groups) {
		final Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(violations);
		}
	}

	public static List<String> messages(ConstraintViolationException e) {
		return messages(e.getConstraintViolations());
	}

	public static List<String> messages(Set<? extends ConstraintViolation<?>> violations) {
		final List<String> list = new ArrayList<String>(violations.size());
		for (ConstraintViolation<?> violation : violations) {
			list.add(violation.getMessage());
		}
		return list;
	}

	public static Map<String, String> propertyMessages(ConstraintViolationException e) {
		return propertyMessages(e.getConstraintViolations());
	}

	public static Map<String, String> propertyMessages(Set<? extends ConstraintViolation<?>> violations) {
		final Map<String, String> map = new HashMap<String, String>();
		for (ConstraintViolation<?> violation : violations) {
			map.put(violation.getPropertyPath().toString(), violation.getMessage());
		}
		return map;
	}

	public static List<String> propertyMessagesAsList(ConstraintViolationException e) {
		return propertyMessagesAsList(e.getConstraintViolations());
	}

	public static List<String> propertyMessagesAsList(Set<? extends ConstraintViolation<?>> violations) {
		return propertyMessagesAsList(violations, " ");
	}

	public static List<String> propertyMessagesAsList(ConstraintViolationException e, String separator) {
		return propertyMessagesAsList(e.getConstraintViolations(), separator);
	}

	public static List<String> propertyMessagesAsList(Set<? extends ConstraintViolation<?>> violations,
			String separator) {
		final List<String> list = new ArrayList<String>(violations.size());
		for (ConstraintViolation<?> violation : violations) {
			list.add(violation.getPropertyPath() + separator + violation.getMessage());
		}
		return list;
	}

	private Validators() {
	}
}
