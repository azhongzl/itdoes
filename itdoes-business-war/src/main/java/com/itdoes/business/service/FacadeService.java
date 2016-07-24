package com.itdoes.business.service;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.itdoes.business.entity.InvCompany;
import com.itdoes.business.repository.BaseDao;
import com.itdoes.business.repository.InvCompanyDao;
import com.itdoes.common.jpa.SearchFilter;
import com.itdoes.common.jpa.Specifications;

/**
 * @author Jalen Zhong
 */
@Service
public class FacadeService extends BaseService {
	private final Map<Class, BaseDao> daoMap = Maps.newHashMap();

	@Autowired
	private InvCompanyDao invCompanyDao;

	@PostConstruct
	public void init() {
		daoMap.put(InvCompany.class, invCompanyDao);
	}

	public List getAll(Class entityClass, List<SearchFilter> filters) {
		final BaseDao dao = getDao(entityClass);
		return dao.findAll(Specifications.build(entityClass, filters));
	}

	private BaseDao getDao(Class entityClass) {
		BaseDao genericDao = daoMap.get(entityClass);
		if (genericDao == null) {
			throw new IllegalArgumentException("Dao cannot be found by entity class: " + entityClass);
		}
		return genericDao;
	}
}
