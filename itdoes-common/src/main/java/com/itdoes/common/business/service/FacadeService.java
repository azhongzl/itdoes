package com.itdoes.common.business.service;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Permissions;

/**
 * @author Jalen Zhong
 */
@Service
public class FacadeService extends BaseService {
	public <T, ID extends Serializable> Page<T> search(EntityPair<T, ID> pair, Specification<T> specification,
			PageRequest pageRequest) {
		final Page<T> page = pair.getDao().findAll(specification, pageRequest);

		Permissions.handleGetSecureFields(pair, page.getContent());

		return page;
	}

	public <T, ID extends Serializable> T get(EntityPair<T, ID> pair, ID id) {
		final T entity = pair.getDao().findOne(id);

		Permissions.handleGetSecureFields(pair, entity);

		return entity;
	}

	public <T, ID extends Serializable> long count(EntityPair<T, ID> pair, Specification<T> specification) {
		return pair.getDao().count(specification);
	}

	@Transactional(readOnly = false)
	public <T, ID extends Serializable> void post(EntityPair<T, ID> pair, T entity) {
		Permissions.handlePostSecureFields(pair, entity);

		pair.getDao().save(entity);
	}

	@Transactional(readOnly = false)
	public <T, ID extends Serializable> void put(EntityPair<T, ID> pair, T entity, T oldEntity) {
		Permissions.handlePutSecureFields(pair, entity, oldEntity);

		pair.getDao().save(entity);
	}

	@Transactional(readOnly = false)
	public <T, ID extends Serializable> void delete(EntityPair<T, ID> pair, ID id) {
		pair.getDao().delete(id);
	}
}
