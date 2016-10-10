package com.itdoes.common.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import com.itdoes.common.business.dao.BaseDao;
import com.itdoes.common.business.entity.EntityPerm;
import com.itdoes.common.core.cglib.CglibMapper;
import com.itdoes.common.core.util.Collections3;

/**
 * @author Jalen Zhong
 */
public class EntityPair<T, ID extends Serializable> {
	private final Class<T> entityClass;
	private final Field idField;
	private final BaseDao<T, ID> dao;
	private final EntityPerm entityPerm;
	private final List<Field> permFieldList;
	private final Field uploadField;

	public EntityPair(Class<T> entityClass, Field idField, BaseDao<T, ID> dao, EntityPerm entityPerm,
			List<Field> permFieldList, Field uploadField) {
		this.entityClass = entityClass;
		this.idField = idField;
		this.dao = dao;
		this.entityPerm = entityPerm;
		this.permFieldList = permFieldList;
		this.uploadField = uploadField;

		// (Optional) Initialize for performance concern, can be removed if it is not readable
		if (needCopyOldEntity()) {
			CglibMapper.getBeanCopier(entityClass);
		}
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

	public EntityPerm getEntityPerm() {
		return entityPerm;
	}

	public List<Field> getPermFieldList() {
		return permFieldList;
	}

	public Field getUploadField() {
		return uploadField;
	}

	public boolean hasPermField() {
		return !Collections3.isEmpty(permFieldList);
	}

	public boolean needCopyOldEntity() {
		return hasPermField();
	}
}
