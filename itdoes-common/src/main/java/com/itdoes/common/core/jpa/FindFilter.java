package com.itdoes.common.core.jpa;

/**
 * @author Jalen Zhong
 */
public class FindFilter {
	public enum Operator {
		EQ, NEQ, LIKE, GT, LT, GTE, LTE, BTWN
	}

	public final String field;
	public final Operator operator;
	public final Object value;

	public FindFilter(String field, Operator operator, Object value) {
		this.field = field;
		this.operator = operator;
		this.value = value;
	}
}
