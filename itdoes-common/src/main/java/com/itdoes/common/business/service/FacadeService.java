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

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Envs;
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

	public EntityPair getEntityPair(String ec) {
		final EntityPair pair = entityPairs.get(ec);
		Validate.notNull(pair, "Cannot find EntityPair [%s] in FacadeService", ec);
		return pair;
	}

	public Set<String> getEntityClassSimpleNames() {
		return entityPairs.keySet();
	}
}
