package com.itdoes.business.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itdoes.common.business.BaseDao;
import com.itdoes.common.business.BaseEntity;
import com.itdoes.common.business.BaseService;
import com.itdoes.common.business.Businesses;
import com.itdoes.common.business.Businesses.EntityDaoPair;
import com.itdoes.common.jpa.SearchFilter;
import com.itdoes.common.jpa.Specifications;
import com.itdoes.common.util.Exceptions;

/**
 * @author Jalen Zhong
 */
@Service
public class FacadeService extends BaseService implements ApplicationContextAware {
	private static final String ENTITY_PACKAGE = "com.itdoes.business.entity";

	private Map<String, EntityDaoPair> pairMap;

	// @Autowired
	// private InvCompanyDao invCompanyDao;

	private ApplicationContext applicationContext;

	@PostConstruct
	public void init() {
		// daoMap.put(InvCompany.class, invCompanyDao);
		pairMap = Businesses.getPairMap(ENTITY_PACKAGE, FacadeService.class.getClassLoader(), applicationContext);
	}

	public <T extends BaseEntity, ID extends Serializable> T get(String ec, ID id) {
		return (T) getDao(ec).findOne(id);
	}

	public <T extends BaseEntity> List<T> getAll(String ec, List<SearchFilter> filters) {
		final EntityDaoPair pair = getPair(ec);
		return (List<T>) getDao(pair).findAll(Specifications.build(getEntityClass(pair), filters));
	}

	@Transactional(readOnly = false)
	public <T extends BaseEntity> void save(String ec, T entity) {
		getDao(ec).save(entity);
	}

	@Transactional(readOnly = false)
	public <ID extends Serializable> void delete(String ec, ID id) {
		getDao(ec).delete(id);
	}

	@Transactional()
	public <T extends BaseEntity> T newInstance(String ec) {
		final Class<T> entityClass = getEntityClass(ec);
		try {
			return entityClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw Exceptions.unchecked(e);
		}
	}

	private <T extends BaseEntity> Class<T> getEntityClass(String ec) {
		return getEntityClass(getPair(ec));
	}

	private <T extends BaseEntity, ID extends Serializable> BaseDao<T, ID> getDao(String ec) {
		return getDao(getPair(ec));
	}

	private <T extends BaseEntity> Class<T> getEntityClass(EntityDaoPair pair) {
		return (Class<T>) pair.entityClass;
	}

	private <T extends BaseEntity, ID extends Serializable> BaseDao<T, ID> getDao(EntityDaoPair pair) {
		return (BaseDao<T, ID>) pair.dao;
	}

	private EntityDaoPair getPair(String ec) {
		final EntityDaoPair pair = pairMap.get(ec);
		if (pair == null) {
			throw new IllegalArgumentException("Entity Class and Dao pair cannot be found by: " + ec);
		}
		return pair;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
