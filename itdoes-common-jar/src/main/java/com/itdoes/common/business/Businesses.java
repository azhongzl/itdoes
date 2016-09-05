package com.itdoes.common.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;

import org.springframework.context.ApplicationContext;

import com.itdoes.common.business.dao.BaseDao;
import com.itdoes.common.business.entity.BaseEntity;
import com.itdoes.common.business.entity.SecureColumn;
import com.itdoes.common.core.cglib.CglibMapper;
import com.itdoes.common.core.spring.Springs;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
public class Businesses {
	private static final char ENTITY_FIELD_SEPARATOR = '.';
	private static final char PERM_SEPARATOR = ':';
	private static final String PERM_READ = "read";
	private static final String PERM_WRITE = "write";
	private static final String PERM_ANY = "*";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, EntityPair> getEntityPairs(String entityPackage, ApplicationContext context) {
		final List<Class<? extends BaseEntity>> entityClasses = (List) Reflections.getClasses(entityPackage,
				new Reflections.ClassFilter.SuperClassFilter(BaseEntity.class), Businesses.class.getClassLoader());

		final Map<String, EntityPair> pairs = new HashMap<String, EntityPair>(entityClasses.size());
		for (Class<? extends BaseEntity> entityClass : entityClasses) {
			final String key = entityClass.getSimpleName();

			// Id Field
			final Field idField = Reflections.getFieldWithAnnotation(entityClass, Id.class);
			if (idField == null) {
				throw new IllegalArgumentException("Cannot find @Id annotation for class: " + key);
			}

			// Dao
			final String daoBeanId = getDaoBeanId(key);
			final BaseDao<? extends BaseEntity, ? extends Serializable> dao = (BaseDao<? extends BaseEntity, ? extends Serializable>) context
					.getBean(daoBeanId);
			if (dao == null) {
				throw new IllegalArgumentException("Cannot find bean for id: " + daoBeanId);
			}

			// Secure Fields
			final List<Field> secureFields = Reflections.getFieldsWithAnnotation(entityClass, SecureColumn.class);
			// (Optional) Initialize for performance concern, can be removed if it is not readable
			if (!Collections3.isEmpty(secureFields)) {
				CglibMapper.getBeanCopier(entityClass);
			}

			pairs.put(key, new EntityPair(entityClass, idField, dao, secureFields));
		}
		return pairs;
	}

	private static String getDaoBeanId(String entityClassName) {
		return Springs.getBeanId(getDaoClassName(entityClassName));
	}

	public static String getDaoClassName(String entityClassName) {
		return entityClassName + "Dao";
	}

	public static class EntityPair {
		private final Class<? extends BaseEntity> entityClass;
		private final Field idField;
		private final BaseDao<? extends BaseEntity, ? extends Serializable> dao;
		private final List<Field> secureFields;

		public EntityPair(Class<? extends BaseEntity> entityClass, Field idField,
				BaseDao<? extends BaseEntity, ? extends Serializable> dao, List<Field> secureFields) {
			this.entityClass = entityClass;
			this.idField = idField;
			this.dao = dao;
			this.secureFields = secureFields;
		}

		@SuppressWarnings("unchecked")
		public <T extends BaseEntity> Class<T> getEntityClass() {
			return (Class<T>) entityClass;
		}

		public Field getIdField() {
			return idField;
		}

		@SuppressWarnings("unchecked")
		public <T extends BaseEntity, ID extends Serializable> BaseDao<T, ID> getDao() {
			return (BaseDao<T, ID>) dao;
		}

		public List<Field> getSecureFields() {
			return secureFields;
		}

		public boolean hasSecureFields() {
			return !Collections3.isEmpty(secureFields);
		}
	}

	public static String getAllPermission(String entityName, String fieldName) {
		return getPermission(entityName, fieldName, PERM_ANY);
	}

	public static String getReadPermission(String entityName, String fieldName) {
		return getPermission(entityName, fieldName, PERM_READ);
	}

	public static String getWritePermission(String entityName, String fieldName) {
		return getPermission(entityName, fieldName, PERM_WRITE);
	}

	private static String getPermission(String entityName, String fieldName, String mode) {
		return entityName + ENTITY_FIELD_SEPARATOR + fieldName + PERM_SEPARATOR + mode;
	}

	private Businesses() {
	}
}
