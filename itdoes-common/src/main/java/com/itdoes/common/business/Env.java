package com.itdoes.common.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.Id;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

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
public class Env implements ApplicationContextAware {
	private ApplicationContext context;

	private String entityPackage;

	private Map<String, EntityPair> entityPairMap;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}

	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}

	@PostConstruct
	public void init() {
		entityPairMap = Env.buildEntityPairMap(entityPackage, context);
	}

	public EntityPair getEntityPair(String entityClassSimpleName) {
		final EntityPair pair = entityPairMap.get(entityClassSimpleName);
		Validate.notNull(pair, "Cannot find EntityPair [%s] in FacadeService", entityClassSimpleName);
		return pair;
	}

	public Set<String> getEntityClassSimpleNames() {
		return entityPairMap.keySet();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Map<String, EntityPair> buildEntityPairMap(String entityPackage, ApplicationContext context) {
		final List<Class<? extends BaseEntity>> entityClasses = (List) Reflections.getClasses(entityPackage,
				new Reflections.ClassFilter.SuperClassFilter(BaseEntity.class), Env.class.getClassLoader());

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
}
