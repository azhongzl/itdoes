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
	public static Map<String, EntityDaoPair> getPairMap(String entityPackage, Class baseEntityClass,
			ClassLoader classLoader, ApplicationContext context) {
		final List<Class> entityClasses = Reflections.getClasses(entityPackage, new Reflections.ClassFilter() {
			@Override
			public boolean isOk(Class clazz) {
				return baseEntityClass.isAssignableFrom(clazz);
			}
		}, classLoader);
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
