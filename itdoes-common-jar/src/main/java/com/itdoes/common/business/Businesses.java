package com.itdoes.common.business;

import java.lang.reflect.Field;
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
	public static Map<String, EntityPair> getEntityPairs(String entityPackage, ClassLoader classLoader,
			ApplicationContext context) {
		final List<Class<?>> entityClasses = Reflections.getClasses(entityPackage, new Reflections.ClassFilter() {
			@Override
			public boolean isOk(Class<?> clazz) {
				return BaseEntity.class.isAssignableFrom(clazz);
			}
		}, classLoader);
		final Map<String, EntityPair> pairs = new HashMap<String, EntityPair>(entityClasses.size());
		for (Class<?> entityClass : entityClasses) {
			final String key = entityClass.getSimpleName();

			final Field idField = Reflections.getFieldWithAnnotation(entityClass, Id.class);
			if (idField == null) {
				throw new IllegalArgumentException("Cannot find @Id annotation for class: " + key);
			}

			final String daoBeanName = StringUtils.uncapitalize(key) + "Dao";
			final BaseDao<?, ?> dao = (BaseDao<?, ?>) context.getBean(daoBeanName);
			if (dao == null) {
				throw new IllegalArgumentException("Cannot find bean for name: " + daoBeanName);
			}
			pairs.put(key, new EntityPair(entityClass, idField, dao));
		}
		return pairs;
	}

	public static class EntityPair {
		public Class<?> entityClass;
		public Field idField;
		public BaseDao<?, ?> dao;

		public EntityPair(Class<?> entityClass, Field idField, BaseDao<?, ?> dao) {
			this.entityClass = entityClass;
			this.idField = idField;
			this.dao = dao;
		}
	}

	private Businesses() {
	}
}
