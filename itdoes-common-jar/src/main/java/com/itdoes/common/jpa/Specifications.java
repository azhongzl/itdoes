package com.itdoes.common.jpa;

import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

/**
 * @author Jalen Zhong
 */
public class Specifications {
	public static <T> Specification<T> build(final Class<T> entityClass, final Collection<SearchFilter> filters) {
		return new Specification<T>() {
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				// TODO Auto-generated method stub
				return builder.conjunction();
			}
		};
	}

	private Specifications() {
	}
}
