package com.itdoes.common.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.persistence.Id;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.collect.Maps;
import com.itdoes.common.business.dao.BaseDao;
import com.itdoes.common.business.dao.loader.LazyDaoLoader;
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
	public static String getDaoClassName(String entityClassName) {
		return entityClassName + "Dao";
	}

	private ApplicationContext applicationContext;

	private String entityPackage;

	private final Map<String, EntityPair<?, ? extends Serializable>> entityPairMap = Maps.newHashMap();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}

	@PostConstruct
	public void init() {
		initEntityPairMap();
	}

	public Map<String, EntityPair<?, ? extends Serializable>> getEntityPairMap() {
		return entityPairMap;
	}

	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> EntityPair<T, ID> getEntityPair(String entityClassSimpleName) {
		final EntityPair<T, ID> pair = (EntityPair<T, ID>) entityPairMap.get(entityClassSimpleName);
		Validate.notNull(pair, "Cannot find EntityPair for class [%s]", entityClassSimpleName);
		return pair;
	}

	int sumIdField = 0;
	int sumDao = 0;
	int sumSecureFields = 0;

	private void initEntityPairMap() {
		final List<Class<?>> entityClasses = Reflections.getClasses(entityPackage,
				new Reflections.ClassFilter.SuperClassFilter(BaseEntity.class), Env.class.getClassLoader());

		for (Class<?> entityClass : entityClasses) {
			initEntityPair(entityClass);
		}

		System.out.println("Jalen--------------------------sumIdField: " + sumIdField);
		System.out.println("Jalen--------------------------sumDao: " + sumDao);
		System.out.println("Jalen--------------------------sumSecureFields: " + sumSecureFields);
	}

	private <T, ID extends Serializable> void initEntityPair(Class<T> entityClass) {
		final String key = entityClass.getSimpleName();

		// Id Field
		long start = System.currentTimeMillis();
		final Field idField = Reflections.getFieldWithAnnotation(entityClass, Id.class);
		Validate.notNull(idField, "Cannot find @Id annotation for class [%s]", key);
		long end = System.currentTimeMillis();
		long elapse = end - start;
		sumIdField += elapse;

		// Dao
		start = System.currentTimeMillis();
		final String daoBeanId = Springs.getBeanId(getDaoClassName(key));
		final BaseDao<T, ID> dao = LazyDaoLoader.getInstance().load(applicationContext, daoBeanId);// (BaseDao<T, ID>)
																									// applicationContext.getBean(daoBeanId);
		Validate.notNull(dao, "Cannot find bean for id [%s]", daoBeanId);
		end = System.currentTimeMillis();
		elapse = end - start;
		sumDao += elapse;

		// Secure Fields
		start = System.currentTimeMillis();
		final List<Field> secureFields = Reflections.getFieldsWithAnnotation(entityClass, SecureColumn.class);
		// (Optional) Initialize for performance concern, can be removed if it is not readable
		if (!Collections3.isEmpty(secureFields)) {
			CglibMapper.getBeanCopier(entityClass);
		}
		end = System.currentTimeMillis();
		elapse = end - start;
		sumSecureFields += elapse;

		entityPairMap.put(key, new EntityPair<T, ID>(entityClass, idField, dao, secureFields));
	}
}
