package com.itdoes.common.business.web;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Permissions;
import com.itdoes.common.business.service.FacadeService;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
public class FacadeServiceFieldSecurer {
	@Autowired
	private FacadeService facadeService;

	public <T, ID extends Serializable> Page<T> secureSearch(EntityPair<T, ID> pair, Specification<T> specification,
			PageRequest pageRequest) {
		final Page<T> page = facadeService.search(pair, specification, pageRequest);
		Permissions.handleGetSecureFields(pair, page.getContent());
		return page;
	}

	public <T, ID extends Serializable> T secureGet(EntityPair<T, ID> pair, ID id) {
		final T entity = facadeService.get(pair, id);
		Permissions.handleGetSecureFields(pair, entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> ID securePost(EntityPair<T, ID> pair, T entity) {
		Permissions.handlePostSecureFields(pair, entity);
		entity = facadeService.save(pair, entity);
		return (ID) Reflections.getFieldValue(entity, pair.getIdField().getName());
	}

	public <T, ID extends Serializable> void securePut(EntityPair<T, ID> pair, T entity, T oldEntity) {
		Permissions.handlePutSecureFields(pair, entity, oldEntity);
		facadeService.save(pair, entity);
	}
}
