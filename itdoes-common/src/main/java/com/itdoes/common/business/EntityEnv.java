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
import org.springframework.context.ConfigurableApplicationContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.itdoes.common.business.dao.BaseDao;
import com.itdoes.common.business.entity.BaseEntity;
import com.itdoes.common.business.entity.EntityPerm;
import com.itdoes.common.business.entity.FieldPerm;
import com.itdoes.common.business.entity.FieldPermType;
import com.itdoes.common.business.entity.UploadField;
import com.itdoes.common.core.spring.LazyInitBeanLoader;
import com.itdoes.common.core.spring.Springs;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
public class EntityEnv implements ApplicationContextAware {
	public static String getDaoClassName(String entityClassName) {
		return entityClassName + "Dao";
	}

	private ApplicationContext applicationContext;

	private String entityPackage;

	private Map<String, EntityPair<?, ? extends Serializable>> pairMap;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}

	@PostConstruct
	public void init() {
		initPairMap();
	}

	public Map<String, EntityPair<?, ? extends Serializable>> getPairMap() {
		return pairMap;
	}

	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> EntityPair<T, ID> getPair(String entityClassSimpleName) {
		final EntityPair<T, ID> pair = (EntityPair<T, ID>) pairMap.get(entityClassSimpleName);
		Validate.notNull(pair, "Cannot find pair for class [%s]", entityClassSimpleName);
		return pair;
	}

	private void initPairMap() {
		final List<Class<?>> entityClassList = Reflections.getClasses(entityPackage,
				new Reflections.ClassFilter.SuperClassFilter(BaseEntity.class), EntityEnv.class.getClassLoader());

		pairMap = Maps.newHashMapWithExpectedSize(entityClassList.size());

		for (Class<?> entityClass : entityClassList) {
			initPair(entityClass);
		}
	}

	@SuppressWarnings("unchecked")
	private <T, ID extends Serializable> void initPair(Class<T> entityClass) {
		final String key = entityClass.getSimpleName();

		// Dao
		final String daoBeanName = Springs.getBeanName(getDaoClassName(key));
		// Generating dao by Cglib is time-consuming. Lazy initialize in non production environment
		final BaseDao<T, ID> dao;
		if (!isLazyInit(daoBeanName)) {
			dao = (BaseDao<T, ID>) applicationContext.getBean(daoBeanName);
		} else {
			dao = (BaseDao<T, ID>) LazyInitBeanLoader.getInstance().loadBean(applicationContext, daoBeanName,
					new Class[] { BaseDao.class });
		}
		Validate.notNull(dao, "Cannot find bean for name [%s]", daoBeanName);

		// Id Field
		final Field idField = Reflections.getFieldWithAnnotation(entityClass, Id.class);
		Validate.notNull(idField, "Cannot find @Id annotation for class [%s]", key);

		// Entity Perm
		final EntityPerm entityPerm = entityClass.getAnnotation(EntityPerm.class);

		// Field Perm
		final List<Field> permFieldList = Reflections.getFieldsWithAnnotation(entityClass, FieldPerm.class);
		final List<Field> readPermFieldList = Lists.newArrayList();
		final List<Field> writePermFieldList = Lists.newArrayList();
		if (!Collections3.isEmpty(permFieldList)) {
			for (Field permField : permFieldList) {
				final FieldPerm fieldPerm = permField.getAnnotation(FieldPerm.class);
				if (fieldPerm != null) {
					final FieldPermType fieldPermType = fieldPerm.type();
					if (fieldPermType != null) {
						if (fieldPermType.equals(FieldPermType.ALL)) {
							readPermFieldList.add(permField);
							writePermFieldList.add(permField);
						} else if (fieldPermType.equals(FieldPermType.READ)) {
							readPermFieldList.add(permField);
						} else if (fieldPermType.equals(FieldPermType.WRITE)) {
							writePermFieldList.add(permField);
						}
					}
				}
			}
		}

		// Field Upload
		final Field uploadField = Reflections.getFieldWithAnnotation(entityClass, UploadField.class);

		pairMap.put(key, new EntityPair<T, ID>(entityClass, dao, null, idField, entityPerm, readPermFieldList,
				writePermFieldList, uploadField));
	}

	private boolean isLazyInit(String beanName) {
		if (applicationContext instanceof ConfigurableApplicationContext) {
			return ((ConfigurableApplicationContext) applicationContext).getBeanFactory().getBeanDefinition(beanName)
					.isLazyInit();
		}
		return false;
	}
}
