package com.itdoes.business.service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.itdoes.business.entity.BaseEntity;
import com.itdoes.business.repository.BaseDao;
import com.itdoes.common.business.Businesses;
import com.itdoes.common.jpa.SearchFilter;
import com.itdoes.common.jpa.Specifications;

/**
 * @author Jalen Zhong
 */
@Service
public class FacadeService extends BaseService implements ApplicationContextAware {
	private Map<Class<? extends BaseEntity>, BaseDao<? extends BaseEntity>> daoMap;

	// @Autowired
	// private InvCompanyDao invCompanyDao;

	private ApplicationContext applicationContext;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		// daoMap.put(InvCompany.class, invCompanyDao);
		daoMap = Businesses.getDaoMap(BaseEntity.class, BaseDao.class, applicationContext);
	}

	@SuppressWarnings("unchecked")
	public List<? extends BaseEntity> getAll(Class<? extends BaseEntity> entityClass, List<SearchFilter> filters) {
		return getDao(entityClass).findAll(Specifications.build(entityClass, filters));
	}

	@SuppressWarnings("rawtypes")
	private BaseDao getDao(Class<? extends BaseEntity> entityClass) {
		final BaseDao<? extends BaseEntity> baseDao = daoMap.get(entityClass);
		if (baseDao == null) {
			throw new IllegalStateException("Dao cannot be found by entity class: " + entityClass);
		}
		return baseDao;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
