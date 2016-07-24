package com.itdoes.common.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.itdoes.common.util.Reflections;

/**
 * @author Jalen Zhong
 */
public class Businesses {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map getDaoMap(Class baseEntityClass, Class baseDaoClass, ApplicationContext context) {
		// TODO
		final List<Class> entityClasses = null;// Reflections.getClasses(baseEntityClass.getPackage(), baseEntityClass);
		final Map daoMap = new HashMap(entityClasses.size());
		for (Class entityClass : entityClasses) {
			final Object dao = context.getBean(entityClass.getName() + "Dao");
			daoMap.put(entityClass, dao);
		}
		return daoMap;
	}

	private Businesses() {
	}
}
