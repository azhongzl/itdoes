package com.itdoes.common.core.jpa;

import org.apache.commons.lang3.Validate;

/**
 * @author Jalen Zhong
 */
public class FindFilter {
	public enum Operator {
		EQ, NEQ, LIKE, NLIKE, GT, LT, GTE, LTE, NULL, NNULL, BTWN
	}

	public static FindFilter equal(String field, Object value) {
		Validate.notNull(value, "Filter EQ value is null");
		return new FindFilter(field, Operator.EQ, value);
	}

	public static FindFilter notEqual(String field, Object value) {
		Validate.notNull(value, "Filter NEQ value is null");
		return new FindFilter(field, Operator.NEQ, value);
	}

	public static FindFilter like(String field, Object value) {
		Validate.notNull(value, "Filter LIKE value is null");
		return new FindFilter(field, Operator.LIKE, value);
	}

	public static FindFilter notLike(String field, Object value) {
		Validate.notNull(value, "Filter NLIKE value is null");
		return new FindFilter(field, Operator.NLIKE, value);
	}

	public static FindFilter greater(String field, Object value) {
		Validate.notNull(value, "Filter GT value is null");
		return new FindFilter(field, Operator.GT, value);
	}

	public static FindFilter less(String field, Object value) {
		Validate.notNull(value, "Filter LT value is null");
		return new FindFilter(field, Operator.LT, value);
	}

	public static FindFilter greaterEqual(String field, Object value) {
		Validate.notNull(value, "Filter GTE value is null");
		return new FindFilter(field, Operator.GTE, value);
	}

	public static FindFilter lessEqual(String field, Object value) {
		Validate.notNull(value, "Filter LTE value is null");
		return new FindFilter(field, Operator.LTE, value);
	}

	public static FindFilter isNull(String field) {
		return new FindFilter(field, Operator.NULL);
	}

	public static FindFilter isNotNull(String field) {
		return new FindFilter(field, Operator.NNULL);
	}

	public static FindFilter between(String field, Object beginValue, Object endValue) {
		if (beginValue != null && endValue != null) {
			return new FindFilter(field, Operator.BTWN, beginValue, endValue);
		} else if (beginValue != null) {
			return greaterEqual(field, beginValue);
		} else if (endValue != null) {
			return lessEqual(field, endValue);
		} else {
			return null;
		}
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
