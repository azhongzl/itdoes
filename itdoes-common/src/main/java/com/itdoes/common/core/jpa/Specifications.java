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

import com.itdoes.common.core.jpa.FindFilter.Operator;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
public class Specifications {
	public static <T> Specification<T> build(final Class<T> entityClass, final Collection<FindFilter> filters) {
		return new Specification<T>() {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (!Collections3.isEmpty(filters)) {
					final List<Predicate> predicates = new ArrayList<Predicate>(filters.size());

					for (FindFilter filter : filters) {
						final String[] names = StringUtils.split(filter.field, ".");
						Path expression = root.get(names[0]);
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}

						Object value = filter.value;
						if (value == null) {
							continue;
						}

						Object value2 = null;
						if (filter.operator == Operator.BTWN) {
							final Object[] values = (Object[]) value;
							if (Collections3.isEmpty(values) || values[0] == null || values[1] == null) {
								continue;
							}

							value = convertString(values[0], expression.getJavaType());
							value2 = convertString(values[1], expression.getJavaType());
						} else {
							value = convertString(value, expression.getJavaType());
						}

						switch (filter.operator) {
						case EQ:
							predicates.add(builder.equal(expression, value));
							break;
						case LIKE:
							predicates.add(builder.like(expression, "%" + value + "%"));
							break;
						case GT:
							predicates.add(builder.greaterThan(expression, (Comparable) value));
							break;
						case LT:
							predicates.add(builder.lessThan(expression, (Comparable) value));
							break;
						case GTE:
							predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) value));
							break;
						case LTE:
							predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) value));
							break;
						case BTWN:
							predicates.add(builder.between(expression, (Comparable) value, (Comparable) value2));
							break;
						default:
							throw new IllegalArgumentException("Cannot find Operator: " + filter.operator);
						}
					}

					if (!predicates.isEmpty()) {
						return builder.and(predicates.toArray(new Predicate[predicates.size()]));
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
