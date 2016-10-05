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
import com.itdoes.common.business.entity.BaseEntity;
import com.itdoes.common.business.entity.SecureField;
import com.itdoes.common.core.cglib.CglibMapper;
import com.itdoes.common.core.spring.LazyInitBeanLoader;
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

	private boolean daoLazyInit;

	private final Map<String, EntityPair<?, ? extends Serializable>> entityPairMap = Maps.newHashMap();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}

	public void setDaoLazyInit(boolean daoLazyInit) {
		this.daoLazyInit = daoLazyInit;
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

	private void initEntityPairMap() {
		final List<Class<?>> entityClasses = Reflections.getClasses(entityPackage,
				new Reflections.ClassFilter.SuperClassFilter(BaseEntity.class), Env.class.getClassLoader());

		for (Class<?> entityClass : entityClasses) {
			initEntityPair(entityClass);
		}
	}

	@SuppressWarnings("unchecked")
	private <T, ID extends Serializable> void initEntityPair(Class<T> entityClass) {
		final String key = entityClass.getSimpleName();

		// Id Field
		final Field idField = Reflections.getFieldWithAnnotation(entityClass, Id.class);
		Validate.notNull(idField, "Cannot find @Id annotation for class [%s]", key);

		// Dao
		final String daoBeanName = Springs.getBeanName(getDaoClassName(key));
		// Generating dao by Cglib is time-consuming. Lazy initialize in non production environment
		final BaseDao<T, ID> dao;
		if (!daoLazyInit) {
			dao = (BaseDao<T, ID>) applicationContext.getBean(daoBeanName);
		} else {
			dao = (BaseDao<T, ID>) LazyInitBeanLoader.getInstance().loadBean(applicationContext, daoBeanName,
					new Class[] { BaseDao.class });
		}

		Validate.notNull(dao, "Cannot find bean for name [%s]", daoBeanName);

		// Secure Fields
		final List<Field> secureFields = Reflections.getFieldsWithAnnotation(entityClass, SecureField.class);
		// (Optional) Initialize for performance concern, can be removed if it is not readable
		if (!Collections3.isEmpty(secureFields)) {
			CglibMapper.getBeanCopier(entityClass);
		}

		entityPairMap.put(key, new EntityPair<T, ID>(entityClass, idField, dao, secureFields));
	}
}
