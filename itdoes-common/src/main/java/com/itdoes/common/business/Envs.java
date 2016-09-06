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
public class Envs {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, EntityPair> getEntityPairs(String entityPackage, ApplicationContext context) {
		final List<Class<? extends BaseEntity>> entityClasses = (List) Reflections.getClasses(entityPackage,
				new Reflections.ClassFilter.SuperClassFilter(BaseEntity.class), Envs.class.getClassLoader());

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

	private Envs() {
	}
}
