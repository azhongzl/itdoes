package com.itdoes.business.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Jalen Zhong
 */
@NoRepositoryBean
public interface BaseDao<T> extends PagingAndSortingRepository<T, Integer>, JpaSpecificationExecutor<T> {
}
