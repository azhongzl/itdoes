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
	public static Map<String, EntityDaoPair> getPairMap(String entityPackage, ClassLoader classLoader,
			ApplicationContext context) {
		final List<Class<?>> entityClasses = Reflections.getClasses(entityPackage, new Reflections.ClassFilter() {
			@Override
			public boolean isOk(Class<?> clazz) {
				return BaseEntity.class.isAssignableFrom(clazz);
			}
		}, classLoader);
		final Map<String, EntityDaoPair> pairMap = new HashMap<String, EntityDaoPair>(entityClasses.size());
		for (Class<?> entityClass : entityClasses) {
			final String key = entityClass.getSimpleName();
			final String daoBeanName = key + "Dao";
			final BaseDao<?, ?> dao = (BaseDao<?, ?>) context.getBean(daoBeanName);
			if (dao == null) {
				throw new IllegalArgumentException("Cannot find bean for name: " + daoBeanName);
			}
			pairMap.put(key, new EntityDaoPair(entityClass, dao));
		}
		return pairMap;
	}

	public static class EntityDaoPair {
		public Class<?> entityClass;
		public BaseDao<?, ?> dao;

		public EntityDaoPair(Class<?> entityClass, BaseDao<?, ?> dao) {
			this.entityClass = entityClass;
			this.dao = dao;
		}
	}

	private Businesses() {
	}
}
