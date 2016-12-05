package com.itdoes.common.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.itdoes.common.business.dao.BaseDao;
import com.itdoes.common.business.entity.EntityPerms;
import com.itdoes.common.business.entity.FieldConstraintPair;
import com.itdoes.common.business.service.entity.external.EntityExternalService;
import com.itdoes.common.business.service.entity.internal.EntityInternalService;
import com.itdoes.common.core.cglib.CglibMapper;
import com.itdoes.common.core.util.Collections3;

/**
 * @author Jalen Zhong
 */
public class EntityPair<T, ID extends Serializable> {
	private final Class<T> entityClass;
	private final BaseDao<T, ID> dao;
	private final Field idField;
	private final Set<FieldConstraintPair> fkFieldConstraintPairSet;
	private final EntityPerms entityPerms;
	private final List<Field> readPermFieldList;
	private final List<Field> writePermFieldList;
	private final Field uploadField;

	private final Set<FieldConstraintPair> pkFieldConstraintPairSet = Sets.newHashSet();
	private EntityInternalService internalService;
	private EntityExternalService externalService;

	public EntityPair(Class<T> entityClass, BaseDao<T, ID> dao, Field idField,
			Set<FieldConstraintPair> fkFieldConstraintPairSet, EntityPerms entityPerms, List<Field> readPermFieldList,
			List<Field> writePermFieldList, Field uploadField) {
		this.entityClass = entityClass;
		this.dao = dao;
		this.idField = idField;
		this.fkFieldConstraintPairSet = fkFieldConstraintPairSet;
		this.entityPerms = entityPerms;
		this.readPermFieldList = readPermFieldList;
		this.writePermFieldList = writePermFieldList;
		this.uploadField = uploadField;

		// (Optional) Initialize for performance concern, can be removed if it is not readable
		CglibMapper.getBeanCopier(entityClass);
	}

	public Class<T> getEntityClass() {
		return entityClass;
	}

	public BaseDao<T, ID> getDao() {
		return dao;
	}

	public Field getIdField() {
		return idField;
	}

	public Set<FieldConstraintPair> getFkFieldConstraintPairSet() {
		return fkFieldConstraintPairSet;
	}

	public EntityPerms getEntityPerms() {
		return entityPerms;
	}

	public List<Field> getReadPermFieldList() {
		return readPermFieldList;
	}

	public List<Field> getWritePermFieldList() {
		return writePermFieldList;
	}

	public Field getUploadField() {
		return uploadField;
	}

	public boolean hasReadPermField() {
		return !Collections3.isEmpty(readPermFieldList);
	}

	public boolean hasWritePermField() {
		return !Collections3.isEmpty(writePermFieldList);
	}

	public Set<FieldConstraintPair> getPkFieldConstraintPairSet() {
		return pkFieldConstraintPairSet;
	}

	public EntityInternalService getInternalService() {
		return internalService;
	}

	public void setInternalService(EntityInternalService internalService) {
		this.internalService = internalService;
	}

	public EntityExternalService getExternalService() {
		return externalService;
	}

	public void setExternalService(EntityExternalService externalService) {
		this.externalService = externalService;
	}
}
