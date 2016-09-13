package com.itdoes.common.business.dao.loader;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;

import com.itdoes.common.business.dao.BaseDao;
import com.itdoes.common.business.dao.DaoLoader;

/**
 * @author Jalen Zhong
 */
public class EagerDaoLoader implements DaoLoader {
	private static EagerDaoLoader INSTANCE = new EagerDaoLoader();

	public static EagerDaoLoader getInstance() {
		return INSTANCE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, ID extends Serializable> BaseDao<T, ID> load(ApplicationContext context, String beanId) {
		return (BaseDao<T, ID>) context.getBean(beanId);
	}
}
