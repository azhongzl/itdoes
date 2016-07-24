package com.itdoes.business.service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	private Map daoMap;

	// @Autowired
	// private InvCompanyDao invCompanyDao;

	private ApplicationContext applicationContext;

	@PostConstruct
	public void init() {
		// daoMap.put(InvCompany.class, invCompanyDao);
		daoMap = Businesses.getDaoMap(BaseEntity.class, BaseDao.class, applicationContext);
	}

	public Object get(Class entityClass, Integer id) {
		return getDao(entityClass).findOne(id);
	}

	public List<? extends BaseEntity> getAll(Class entityClass, List<SearchFilter> filters) {
		return getDao(entityClass).findAll(Specifications.build(entityClass, filters));
	}

	@Transactional(readOnly = false)
	public void save(Object entity) {
		getDao(entity.getClass()).save(entity);
	}

	@Transactional(readOnly = false)
	public void delete(Class entityClass, Integer id) {
		getDao(entityClass).delete(id);
	}

	private BaseDao getDao(Class entityClass) {
		final BaseDao baseDao = (BaseDao) daoMap.get(entityClass);
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
