package com.itdoes.common.jpa;

/**
 * @author Jalen Zhong
 */
public class SearchFilter {
	public enum Operator {
		EQ, LIKE, GT, LT, GTE, LTE
	}

	public final String field;
	public final Operator operator;
	public final Object value;

	public SearchFilter(String field, Operator operator, Object value) {
		this.field = field;
		this.operator = operator;
		this.value = value;
	}
}
