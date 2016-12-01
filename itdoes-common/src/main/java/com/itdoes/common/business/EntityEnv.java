package com.itdoes.common.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.Id;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.itdoes.common.business.dao.BaseDao;
import com.itdoes.common.business.entity.BaseEntity;
import com.itdoes.common.business.entity.EntityPerms;
import com.itdoes.common.business.entity.FieldConstraint;
import com.itdoes.common.business.entity.FieldConstraintPair;
import com.itdoes.common.business.entity.FieldPerm;
import com.itdoes.common.business.entity.FieldPermType;
import com.itdoes.common.business.entity.FieldUpload;
import com.itdoes.common.business.service.EntityService;
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

	public static String getServiceClassName(String entityClassName) {
		return entityClassName + "Service";
	}

	private ConfigurableApplicationContext applicationContext;

	private String entityPackage;

	private Map<String, EntityPair<?, ? extends Serializable>> pairMap;

	@Resource(name = "entityService")
	private EntityService defaultEntityService;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = (ConfigurableApplicationContext) applicationContext;
	}

	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}

	@PostConstruct
	public void myInit() {
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

		for (EntityPair<?, ? extends Serializable> pair : pairMap.values()) {
			// Initialize PK FieldConstraint
			final Set<FieldConstraintPair> fkFieldConstraintPairSet = pair.getFkFieldConstraintPairSet();
			if (!Collections3.isEmpty(fkFieldConstraintPairSet)) {
				for (FieldConstraintPair fieldConstraintPair : fkFieldConstraintPairSet) {
					final EntityPair<?, ?> pkPair = pairMap.get(fieldConstraintPair.getPkEntity().getSimpleName());
					pkPair.getPkFieldConstraintPairSet().add(fieldConstraintPair);
				}
			}

			// Initialize service after initPair to avoid "circular reference" between EntityService and EntityPair
			final String key = pair.getEntityClass().getSimpleName();
			final String serviceBeanName = Springs.getBeanName(getServiceClassName(key));
			final EntityService service = applicationContext.containsBean(serviceBeanName)
					? (EntityService) applicationContext.getBean(serviceBeanName) : defaultEntityService;
			pair.setService(service);
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

		// FK FieldConstraint
		final List<Field> fieldConstraintFieldList = Reflections.getFieldsWithAnnotation(entityClass,
				FieldConstraint.class);
		final Set<FieldConstraintPair> fkFieldConstraintPairSet = Sets
				.newHashSetWithExpectedSize(fieldConstraintFieldList.size());
		if (!Collections3.isEmpty(fieldConstraintFieldList)) {
			for (Field fieldConstraintField : fieldConstraintFieldList) {
				final FieldConstraint fieldConstraint = fieldConstraintField.getAnnotation(FieldConstraint.class);
				if (fieldConstraint != null) {
					fkFieldConstraintPairSet
							.add(new FieldConstraintPair(entityClass, fieldConstraintField, fieldConstraint));
				}
			}
		}

		// Entity Perm
		final EntityPerms entityPerms = entityClass.getAnnotation(EntityPerms.class);

		// Field Perm
		final List<Field> permFieldList = Reflections.getFieldsWithAnnotation(entityClass, FieldPerm.class);
		final List<Field> readPermFieldList = Lists.newArrayList();
		final List<Field> writePermFieldList = Lists.newArrayList();
		if (!Collections3.isEmpty(permFieldList)) {
			for (Field permField : permFieldList) {
				final FieldPerm fieldPerm = permField.getAnnotation(FieldPerm.class);
				if (fieldPerm != null) {
					final FieldPermType fieldPermType = fieldPerm.value();
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
		final Field uploadField = Reflections.getFieldWithAnnotation(entityClass, FieldUpload.class);

		pairMap.put(key, new EntityPair<T, ID>(entityClass, dao, idField, fkFieldConstraintPairSet, entityPerms,
				readPermFieldList, writePermFieldList, uploadField));
	}

	private boolean isLazyInit(String beanName) {
		return applicationContext.getBeanFactory().getBeanDefinition(beanName).isLazyInit();
	}
}
