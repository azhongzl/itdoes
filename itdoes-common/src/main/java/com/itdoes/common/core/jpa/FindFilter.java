package com.itdoes.common.core.jpa;

import org.apache.commons.lang3.Validate;

/**
 * @author Jalen Zhong
 */
public class FindFilter {
	public enum Operator {
		EQ, NOT_EQ, LIKE, NOT_LIKE, GT, LT, GTE, LTE, NULL, NOT_NULL, BTWN
	}

	public static FindFilter eq(String field, Object value) {
		Validate.notNull(value, "Filter EQ value is null");
		return new FindFilter(field, Operator.EQ, value);
	}

	public static FindFilter notEq(String field, Object value) {
		Validate.notNull(value, "Filter NOT_EQ value is null");
		return new FindFilter(field, Operator.NOT_EQ, value);
	}

	public static FindFilter like(String field, Object value) {
		Validate.notNull(value, "Filter LIKE value is null");
		return new FindFilter(field, Operator.LIKE, value);
	}

	public static FindFilter notLike(String field, Object value) {
		Validate.notNull(value, "Filter NOT_LIKE value is null");
		return new FindFilter(field, Operator.NOT_LIKE, value);
	}

	public static FindFilter gt(String field, Object value) {
		Validate.notNull(value, "Filter GT value is null");
		return new FindFilter(field, Operator.GT, value);
	}

	public static FindFilter lt(String field, Object value) {
		Validate.notNull(value, "Filter LT value is null");
		return new FindFilter(field, Operator.LT, value);
	}

	public static FindFilter gte(String field, Object value) {
		Validate.notNull(value, "Filter GTE value is null");
		return new FindFilter(field, Operator.GTE, value);
	}

	public static FindFilter lte(String field, Object value) {
		Validate.notNull(value, "Filter LTE value is null");
		return new FindFilter(field, Operator.LTE, value);
	}

	public static FindFilter isNull(String field) {
		return new FindFilter(field, Operator.NULL);
	}

	public static FindFilter isNotNull(String field) {
		return new FindFilter(field, Operator.NOT_NULL);
	}

	public static FindFilter between(String field, Object beginValue, Object endValue) {
		Validate.notNull(beginValue, "Filter begin value is null");
		Validate.notNull(endValue, "Filter end value is null");
		return new FindFilter(field, Operator.BTWN, beginValue, endValue);
	}

	public final String field;
	public final Operator operator;
	public final Object[] values;

	private FindFilter(String field, Operator operator, Object... values) {
		Validate.notBlank(field, "Filter field is blank");
		this.field = field;
		this.operator = operator;
		this.values = values;
	}
}
