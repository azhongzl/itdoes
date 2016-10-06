package com.itdoes.common.business.service;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itdoes.common.business.EntityPair;

/**
 * @author Jalen Zhong
 */
@Service
public class EntityTransactionalService extends BaseTransactionalService {
	public <T, ID extends Serializable> Page<T> find(EntityPair<T, ID> pair, Specification<T> specification,
			PageRequest pageRequest) {
		return pair.getDao().findAll(specification, pageRequest);
	}

	public <T, ID extends Serializable> T findOne(EntityPair<T, ID> pair, Specification<T> specification) {
		return pair.getDao().findOne(specification);
	}

	public <T, ID extends Serializable> long count(EntityPair<T, ID> pair, Specification<T> specification) {
		return pair.getDao().count(specification);
	}

	public <T, ID extends Serializable> T get(EntityPair<T, ID> pair, ID id) {
		return pair.getDao().findOne(id);
	}

	@Transactional(readOnly = false)
	public <T, ID extends Serializable> T save(EntityPair<T, ID> pair, T entity) {
		return pair.getDao().save(entity);
	}

	@Transactional(readOnly = false)
	public <T, ID extends Serializable> void delete(EntityPair<T, ID> pair, ID id) {
		pair.getDao().delete(id);
	}
}
