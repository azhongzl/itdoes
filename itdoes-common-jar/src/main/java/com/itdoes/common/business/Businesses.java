package com.itdoes.common.business;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.context.ApplicationContext;

import com.itdoes.common.util.PropertiesLoader;
import com.itdoes.common.util.Reflections;

/**
 * @author Jalen Zhong
 */
public class Businesses {
	public static final char PROP_S = ':';
	public static final String PROP_READ = "read";
	public static final String PROP_WRITE = "write";

	private static final PropertiesLoader PL = new PropertiesLoader("classpath*:/dataPermission.properties");
	private static final char PROP_SEPARATOR = ',';

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

			// Secure Props
			final String propsString = PL.getProperty(key);
			final List<String> secureProps = Lists.newArrayList();
			if (!StringUtils.isBlank(propsString)) {
				final String[] props = StringUtils.split(propsString, PROP_SEPARATOR);
				if (props != null && props.length > 0) {
					for (String prop : props) {
						if (!StringUtils.isBlank(prop)) {
							secureProps.add(prop.trim());
						}
					}
				}
			}

			pairs.put(key, new EntityPair(entityClass, idField, dao, secureProps));
		}
		return pairs;
	}

	public static class EntityPair {
		public final Class<?> entityClass;
		public final Field idField;
		public final BaseDao<?, ?> dao;
		public final List<String> secureProps;

		public EntityPair(Class<?> entityClass, Field idField, BaseDao<?, ?> dao, List<String> secureProps) {
			this.entityClass = entityClass;
			this.idField = idField;
			this.dao = dao;
			this.secureProps = secureProps;
		}
	}

	private Businesses() {
	}
}
