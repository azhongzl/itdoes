package com.itdoes.common.business.service;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itdoes.common.business.Envs;
import com.itdoes.common.business.Envs.EntityPair;
import com.itdoes.common.business.Permissions;
import com.itdoes.common.business.dao.BaseDao;
import com.itdoes.common.business.entity.BaseEntity;

/**
 * @author Jalen Zhong
 */
@Service
public class FacadeService extends BaseService implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	private String entityPackage;

	private Map<String, EntityPair> entityPairs;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}

	@PostConstruct
	public void init() {
		entityPairs = Envs.getEntityPairs(entityPackage, applicationContext);
	}

	public <T extends BaseEntity> Page<T> search(String ec, Specification<T> specification, PageRequest pageRequest) {
		final EntityPair pair = getEntityPair(ec);
		final BaseDao<T, ? extends Serializable> dao = pair.getDao();
		final Page<T> page = dao.findAll(specification, pageRequest);

		Permissions.handleGetSecureFields(pair, page.getContent());

		return page;
	}

	public BaseEntity get(String ec, Serializable id) {
		final EntityPair pair = getEntityPair(ec);
		final BaseEntity entity = pair.getDao().findOne(id);

		Permissions.handleGetSecureFields(pair, entity);

		return entity;
	}

	public <T extends BaseEntity> long count(String ec, Specification<T> specification) {
		final EntityPair pair = getEntityPair(ec);
		final BaseDao<T, ? extends Serializable> dao = pair.getDao();
		return dao.count(specification);
	}

	@Transactional(readOnly = false)
	public <T extends BaseEntity> void post(String ec, T entity) {
		final EntityPair pair = getEntityPair(ec);

		Permissions.handlePostSecureFields(pair, entity);

		pair.getDao().save(entity);
	}

	@Transactional(readOnly = false)
	public <T extends BaseEntity> void put(String ec, T entity, T oldEntity) {
		final EntityPair pair = getEntityPair(ec);

		Permissions.handlePutSecureFields(pair, entity, oldEntity);

		pair.getDao().save(entity);
	}

	@Transactional(readOnly = false)
	public void delete(String ec, Serializable id) {
		final EntityPair pair = getEntityPair(ec);
		pair.getDao().delete(id);
	}

	public EntityPair getEntityPair(String ec) {
		final EntityPair pair = entityPairs.get(ec);
		Validate.notNull(pair, "Cannot find EntityPair [%s] in FacadeService", ec);
		return pair;
	}

	public Set<String> getEntityClassSimpleNames() {
		return entityPairs.keySet();
	}
}
