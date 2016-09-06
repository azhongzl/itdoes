package com.itdoes.common.business.service;

import java.io.Serializable;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Permissions;
import com.itdoes.common.business.dao.BaseDao;
import com.itdoes.common.business.entity.BaseEntity;

/**
 * @author Jalen Zhong
 */
@Service
public class FacadeService extends BaseService {
	public <T extends BaseEntity> Page<T> search(EntityPair pair, Specification<T> specification,
			PageRequest pageRequest) {
		final BaseDao<T, ? extends Serializable> dao = pair.getDao();
		final Page<T> page = dao.findAll(specification, pageRequest);

		Permissions.handleGetSecureFields(pair, page.getContent());

		return page;
	}

	public BaseEntity get(EntityPair pair, Serializable id) {
		final BaseEntity entity = pair.getDao().findOne(id);

		Permissions.handleGetSecureFields(pair, entity);

		return entity;
	}

	public <T extends BaseEntity> long count(EntityPair pair, Specification<T> specification) {
		final BaseDao<T, ? extends Serializable> dao = pair.getDao();
		return dao.count(specification);
	}

	@Transactional(readOnly = false)
	public <T extends BaseEntity> void post(EntityPair pair, T entity) {
		Permissions.handlePostSecureFields(pair, entity);

		pair.getDao().save(entity);
	}

	@Transactional(readOnly = false)
	public <T extends BaseEntity> void put(EntityPair pair, T entity, T oldEntity) {
		Permissions.handlePutSecureFields(pair, entity, oldEntity);

		pair.getDao().save(entity);
	}

	@Transactional(readOnly = false)
	public void delete(EntityPair pair, Serializable id) {
		pair.getDao().delete(id);
	}
}
