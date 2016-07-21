package com.itdoes.business.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Jalen Zhong
 */
public interface GenericDao<T> extends PagingAndSortingRepository<T, Integer>, JpaSpecificationExecutor<T> {

}
