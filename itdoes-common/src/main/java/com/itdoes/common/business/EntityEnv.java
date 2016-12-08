package com.itdoes.common.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.Id;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.itdoes.common.business.dao.BaseDao;
import com.itdoes.common.business.entity.BaseEntity;
import com.itdoes.common.business.entity.FieldConstraint;
import com.itdoes.common.business.entity.FieldConstraintPair;
import com.itdoes.common.business.entity.FieldPerm;
import com.itdoes.common.business.entity.FieldPermType;
import com.itdoes.common.business.entity.FieldUpload;
import com.itdoes.common.business.service.EntityDbService;
import com.itdoes.common.business.service.EntityPermFieldService;
import com.itdoes.common.business.service.EntityUploadService;
import com.itdoes.common.core.spring.LazyInitBeanLoader;
import com.itdoes.common.core.spring.Springs;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
public class EntityEnv implements ApplicationContextAware {
	public static String getDaoClassSimpleName(String entityClassSimpleName) {
		return entityClassSimpleName + "Dao";
	}

	private Map<String, EntityPair<?, ? extends Serializable>> pairMap;

	@Autowired
	private EntityDbService dbService;
	@Autowired
	private EntityUploadService uploadService;
	@Autowired
	private EntityPermFieldService permFieldService;

	private ConfigurableApplicationContext context;
	private String basePackage;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = (ConfigurableApplicationContext) applicationContext;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
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
		final List<Class<?>> entityClassList = Reflections.getClasses(basePackage + ".entity",
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
		}
	}

	@SuppressWarnings("unchecked")
	private <T, ID extends Serializable> void initPair(Class<T> entityClass) {
		final String key = entityClass.getSimpleName();

		// Dao
		final String daoBeanName = Springs.getBeanName(getDaoClassSimpleName(key));
		// Generating dao by Cglib is time-consuming. Lazy initialize in non production environment
		final BaseDao<T, ID> dao;
		if (!isLazyInit(daoBeanName)) {
			dao = (BaseDao<T, ID>) context.getBean(daoBeanName);
		} else {
			dao = (BaseDao<T, ID>) LazyInitBeanLoader.getInstance().loadBean(context, daoBeanName,
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

		pairMap.put(key, new EntityPair<T, ID>(entityClass, dao, idField, fkFieldConstraintPairSet, readPermFieldList,
				writePermFieldList, uploadField, dbService, uploadService, permFieldService));
	}

	private boolean isLazyInit(String beanName) {
		return context.getBeanFactory().getBeanDefinition(beanName).isLazyInit();
	}
}
