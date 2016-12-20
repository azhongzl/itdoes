package com.itdoes.common.core.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.data.jpa.domain.Specification;

import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
public class Specifications {
	public static <T> Specification<T> build(final Class<T> entityClass, final Collection<FindFilter> filters) {
		return build(entityClass, filters, true);
	}

	public static <T> Specification<T> build(final Class<T> entityClass, final Collection<FindFilter> filters,
			boolean and) {
		return new Specification<T>() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (!Collections3.isEmpty(filters)) {
					final List<Predicate> predicateList = new ArrayList<Predicate>(filters.size());

					for (FindFilter filter : filters) {
						final String[] names = StringUtils.split(filter.field, ".");
						Path expression = root.get(names[0]);
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}

						final Object[] values = filter.values;

						switch (filter.operator) {
						case EQ:
							predicateList
									.add(builder.equal(expression, convertString(values[0], expression.getJavaType())));
							break;
						case NOT_EQ:
							predicateList.add(
									builder.notEqual(expression, convertString(values[0], expression.getJavaType())));
							break;
						case LIKE:
							predicateList.add(builder.like(expression,
									"%" + convertString(values[0], expression.getJavaType()) + "%"));
							break;
						case NOT_LIKE:
							predicateList.add(builder.notLike(expression,
									"%" + convertString(values[0], expression.getJavaType()) + "%"));
							break;
						case GT:
							predicateList.add(builder.greaterThan(expression,
									(Comparable) convertString(values[0], expression.getJavaType())));
							break;
						case LT:
							predicateList.add(builder.lessThan(expression,
									(Comparable) convertString(values[0], expression.getJavaType())));
							break;
						case GTE:
							predicateList.add(builder.greaterThanOrEqualTo(expression,
									(Comparable) convertString(values[0], expression.getJavaType())));
							break;
						case LTE:
							predicateList.add(builder.lessThanOrEqualTo(expression,
									(Comparable) convertString(values[0], expression.getJavaType())));
							break;
						case NULL:
							predicateList.add(builder.isNull(expression));
							break;
						case NOT_NULL:
							predicateList.add(builder.isNotNull(expression));
							break;
						case BTWN:
							predicateList.add(builder.between(expression,
									(Comparable) convertString(values[0], expression.getJavaType()),
									(Comparable) convertString(values[1], expression.getJavaType())));
							break;
						default:
							throw new IllegalArgumentException("Cannot find Operator: " + filter.operator);
						}
					}

					if (!predicateList.isEmpty()) {
						final Predicate[] predicateArray = predicateList.toArray(new Predicate[predicateList.size()]);
						if (and) {
							return builder.and(predicateArray);
						} else {
							return builder.or(predicateArray);
						}
					}
				}

				return builder.conjunction();
			}
		};
	}

	private static Object convertString(Object value, Class<?> toClass) {
		if (!(value instanceof String)) {
			return value;
		}

		final String stringValue = (String) value;
		Validate.notBlank(stringValue, "Filter value is blank");
		return Reflections.convert(stringValue, toClass);
	}

	private Specifications() {
	}
}
