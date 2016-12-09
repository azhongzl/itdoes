package com.itdoes.common.business.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.QueryHints;

/**
 * @author Jalen Zhong
 */
public interface BaseQueryCacheDao<T, ID extends Serializable> extends BaseDao<T, ID> {
	@QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
	Page<T> findAll(Specification<T> spec, Pageable pageable);

	@QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
	List<T> findAll(Specification<T> spec, Sort sort);

	@QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
	List<T> findAll(Specification<T> spec);
}
