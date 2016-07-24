package com.itdoes.common.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;

/**
 * @author Jalen Zhong
 */
public class Businesses {
	public static Map<String, EntityDaoPair> getPairMap(Class baseEntityClass, Class baseDaoClass,
			ApplicationContext context) {
		// TODO
		final List<Class> entityClasses = null;
		// final List<Class> entityClasses =
		// Reflections.getClasses(baseEntityClass.getPackage(),
		// baseEntityClass);
		final Map pairMap = new HashMap(entityClasses.size());
		for (Class entityClass : entityClasses) {
			final String key = entityClass.getSimpleName();
			final String daoBeanName = key + "Dao";
			final BaseDao dao = (BaseDao) context.getBean(daoBeanName);
			if (dao == null) {
				throw new IllegalStateException("Cannot find bean for name: " + daoBeanName);
			}
			pairMap.put(key, new EntityDaoPair(entityClass, dao));
		}
		return pairMap;
	}

	public static class EntityDaoPair {
		public Class entityClass;
		public BaseDao dao;

		public EntityDaoPair(Class entityClass, BaseDao dao) {
			this.entityClass = entityClass;
			this.dao = dao;
		}
	}

	private Businesses() {
	}
}
