package com.itdoes.business.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itdoes.common.business.BaseDao;
import com.itdoes.common.business.BaseEntity;
import com.itdoes.common.business.BaseService;
import com.itdoes.common.business.Businesses;
import com.itdoes.common.business.Businesses.EntityPair;
import com.itdoes.common.jpa.SearchFilter;
import com.itdoes.common.jpa.Specifications;
import com.itdoes.common.util.Reflections;

/**
 * @author Jalen Zhong
 */
@Service
public class FacadeService extends BaseService implements ApplicationContextAware {
	private static final String ENTITY_PACKAGE = "com.itdoes.business.entity";

	private Map<String, EntityPair> entityPairs;

	private ApplicationContext applicationContext;

	@PostConstruct
	public void init() {
		entityPairs = Businesses.getEntityPairs(ENTITY_PACKAGE, FacadeService.class.getClassLoader(),
				applicationContext);
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> Page<T> search(String ec, List<SearchFilter> filters, PageRequest pageRequest) {
		final EntityPair pair = getEntityPair(ec);
		return (Page<T>) getDao(pair).findAll(Specifications.build(getEntityClass(pair), filters), pageRequest);
	}

	public BaseEntity get(String ec, String idString) {
		final EntityPair pair = getEntityPair(ec);
		final Serializable id = convertId(idString, pair.idField.getType());
		return getDao(pair).findOne(id);
	}

	public long count(String ec, List<SearchFilter> filters) {
		final EntityPair pair = getEntityPair(ec);
		return getDao(pair).count(Specifications.build(getEntityClass(pair), filters));
	}

	@Transactional(readOnly = false)
	public <T extends BaseEntity> void post(String ec, T entity) {
		final EntityPair pair = getEntityPair(ec);
		getDao(pair).save(entity);
	}

	@Transactional(readOnly = false)
	public <T extends BaseEntity> void put(String ec, T entity) {
		final EntityPair pair = getEntityPair(ec);
		getDao(pair).save(entity);
	}

	@Transactional(readOnly = false)
	public void delete(String ec, String idString) {
		final EntityPair pair = getEntityPair(ec);
		final Serializable id = convertId(idString, pair.idField.getType());
		getDao(pair).delete(id);
	}

	public EntityPair getEntityPair(String ec) {
		final EntityPair pair = entityPairs.get(ec);
		if (pair == null) {
			throw new IllegalArgumentException("Cannot find EntityPair for \"" + ec + "\" in FacadeService");
		}
		return pair;
	}

	public Set<String> getEntityClassSimpleNames() {
		return entityPairs.keySet();
	}

	@SuppressWarnings("unchecked")
	private <T extends BaseEntity> Class<T> getEntityClass(EntityPair pair) {
		return (Class<T>) pair.entityClass;
	}

	@SuppressWarnings("unchecked")
	private <T extends BaseEntity, ID extends Serializable> BaseDao<T, ID> getDao(EntityPair pair) {
		return (BaseDao<T, ID>) pair.dao;
	}

	private Serializable convertId(String id, Class<?> idClass) {
		return (Serializable) Reflections.convert(id, idClass);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
