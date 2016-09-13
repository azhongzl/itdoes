package com.itdoes.common.business.dao;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;

/**
 * @author Jalen Zhong
 */
public interface DaoLoader {
	<T, ID extends Serializable> BaseDao<T, ID> load(ApplicationContext context, String beanId);
}
