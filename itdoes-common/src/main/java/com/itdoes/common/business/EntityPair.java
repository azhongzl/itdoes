package com.itdoes.common.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import com.itdoes.common.business.dao.BaseDao;
import com.itdoes.common.business.entity.BaseEntity;
import com.itdoes.common.core.util.Collections3;

/**
 * @author Jalen Zhong
 */
public class EntityPair {
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
