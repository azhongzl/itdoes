package com.itdoes.common.core.jpa;

import org.apache.commons.lang3.Validate;

/**
 * @author Jalen Zhong
 */
public class FindFilter {
	public enum Operator {
		EQ, NOT_EQ, LIKE, NOT_LIKE, GT, LT, GTE, LTE, NULL, NOT_NULL, BTWN
	}

	public final String field;
	public final Operator operator;
	public final Object value;

	public FindFilter(String field, Operator operator, Object value) {
		this.field = field;
		this.operator = operator;

		if (Operator.BTWN.equals(operator)) {
			final Object[] values = (Object[]) value;
			Validate.isTrue(values != null && values.length == 2);
			Validate.notNull(values[0]);
			Validate.notNull(values[1]);
		} else if (Operator.EQ.equals(operator) || Operator.NOT_EQ.equals(operator) || Operator.LIKE.equals(operator)
				|| Operator.NOT_LIKE.equals(operator) || Operator.GT.equals(operator) || Operator.LT.equals(operator)
				|| Operator.GTE.equals(operator) || Operator.LTE.equals(operator)) {
			Validate.notNull(value);
		}
		this.value = value;
	}
}
