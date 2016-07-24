package com.itdoes.business.service;

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
		pairMap = Businesses.getPairMap(ENTITY_PACKAGE, BaseEntity.class, applicationContext);
	}

	public Object get(String ec, Integer id) {
		return getDao(ec).findOne(id);
	}

	public List<? extends BaseEntity> getAll(String ec, List<SearchFilter> filters) {
		return getDao(ec).findAll(Specifications.build(getEntityClass(ec), filters));
	}

	@Transactional(readOnly = false)
	public void save(String ec, Object entity) {
		getDao(ec).save(entity);
	}

	@Transactional(readOnly = false)
	public void delete(String ec, Integer id) {
		getDao(ec).delete(id);
	}

	private BaseDao getDao(String ec) {
		return getPair(ec).dao;
	}

	private Class getEntityClass(String ec) {
		return getPair(ec).entityClass;
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
