package com.itdoes.common.business;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;

import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

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

			final Class<?> idClass = Reflections.getFieldClassWithAnnotation(entityClass, Id.class);
			if (idClass == null) {
				throw new IllegalArgumentException("Cannot find @Id annotation for class: " + key);
			}

			final String daoBeanName = StringUtils.uncapitalize(key) + "Dao";
			final BaseDao<?, ?> dao = (BaseDao<?, ?>) context.getBean(daoBeanName);
			if (dao == null) {
				throw new IllegalArgumentException("Cannot find bean for name: " + daoBeanName);
			}
			pairMap.put(key, new EntityDaoPair(entityClass, idClass, dao));
		}
		return pairMap;
	}

	public static class EntityDaoPair {
		public Class<?> entityClass;
		public Class<?> idClass;
		public BaseDao<?, ?> dao;

		public EntityDaoPair(Class<?> entityClass, Class<?> idClass, BaseDao<?, ?> dao) {
			this.entityClass = entityClass;
			this.idClass = idClass;
			this.dao = dao;
		}
	}

	private Businesses() {
	}
}
