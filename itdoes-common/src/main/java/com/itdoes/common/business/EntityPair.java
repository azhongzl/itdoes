package com.itdoes.common.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import com.itdoes.common.business.dao.BaseDao;
import com.itdoes.common.core.util.Collections3;

/**
 * @author Jalen Zhong
 */
public class EntityPair<T, ID extends Serializable> {
	private final Class<T> entityClass;
	private final Field idField;
	private final BaseDao<T, ID> dao;
	private final List<Field> secureFields;
	private final Field uploadField;

	public EntityPair(Class<T> entityClass, Field idField, BaseDao<T, ID> dao, List<Field> secureFields,
			Field uploadField) {
		this.entityClass = entityClass;
		this.idField = idField;
		this.dao = dao;
		this.secureFields = secureFields;
		this.uploadField = uploadField;
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public Field getIdField() {
		return idField;
	}

	public BaseDao<T, ID> getDao() {
		return dao;
	}

	public List<Field> getSecureFields() {
		return secureFields;
	}

	public Field getUploadField() {
		return uploadField;
	}

	public boolean hasSecureFields() {
		return !Collections3.isEmpty(secureFields);
	}

	public boolean needCopyOldEntity() {
		return hasSecureFields() || getUploadField() != null;
	}
}
