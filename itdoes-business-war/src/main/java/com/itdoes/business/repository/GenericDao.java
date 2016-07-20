package com.itdoes.business.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface GenericDao<T> extends PagingAndSortingRepository<T, Integer>, JpaSpecificationExecutor<T> {

}
