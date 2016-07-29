package com.itdoes.common.business;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;

import com.itdoes.common.util.Reflections;

/**
 * @author Jalen Zhong
 */
public class Businesses {
	private static final char PERM_SEPARATOR = ':';
	private static final String PERM_READ = "read";
	private static final String PERM_WRITE = "write";

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

			// Id Field
			final Field idField = Reflections.getFieldWithAnnotation(entityClass, Id.class);
			if (idField == null) {
				throw new IllegalArgumentException("Cannot find @Id annotation for class: " + key);
			}

			// Dao
			final String daoBeanName = StringUtils.uncapitalize(key) + "Dao";
			final BaseDao<?, ?> dao = (BaseDao<?, ?>) context.getBean(daoBeanName);
			if (dao == null) {
				throw new IllegalArgumentException("Cannot find bean for name: " + daoBeanName);
			}

			// Secure Fields
			final List<Field> secureFields = Reflections.getFieldsWithAnnotation(entityClass, SecureColumn.class);

			pairs.put(key, new EntityPair(entityClass, idField, dao, secureFields));
		}
		return pairs;
	}

	public static class EntityPair {
		public final Class<?> entityClass;
		public final Field idField;
		public final BaseDao<?, ?> dao;
		public final List<Field> secureFields;

		public EntityPair(Class<?> entityClass, Field idField, BaseDao<?, ?> dao, List<Field> secureFields) {
			this.entityClass = entityClass;
			this.idField = idField;
			this.dao = dao;
			this.secureFields = secureFields;
		}
	}

	public static String getReadPermission(String fieldName) {
		return getPermission(fieldName, PERM_READ);
	}

	public static String getWritePermission(String fieldName) {
		return getPermission(fieldName, PERM_WRITE);
	}

	private static String getPermission(String fieldName, String mode) {
		return fieldName + Businesses.PERM_SEPARATOR + mode;
	}

	private Businesses() {
	}
}
