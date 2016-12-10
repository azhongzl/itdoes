package com.itdoes.common.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.itdoes.common.business.dao.BaseDao;
import com.itdoes.common.business.entity.FieldConstraintPair;
import com.itdoes.common.business.service.EntityDbService;
import com.itdoes.common.business.service.EntityDbServiceBuilder;
import com.itdoes.common.business.service.EntityPermFieldService;
import com.itdoes.common.business.service.EntityPermFieldServiceBuilder;
import com.itdoes.common.business.service.EntityUploadService;
import com.itdoes.common.business.service.EntityUploadServiceBuilder;
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
	private final Set<FieldConstraintPair> pkFieldConstraintPairSet = Sets.newHashSet();
	private final List<Field> readPermFieldList;
	private final List<Field> writePermFieldList;
	private final Field uploadField;

	private final EntityDbService dbService;
	private final EntityUploadService uploadService;
	private final EntityPermFieldService permFieldService;

	public EntityPair(Class<T> entityClass, BaseDao<T, ID> dao, Field idField,
			Set<FieldConstraintPair> fkFieldConstraintPairSet, List<Field> readPermFieldList,
			List<Field> writePermFieldList, Field uploadField, EntityDbService dbService,
			EntityUploadService uploadService, EntityPermFieldService permFieldService) {
		this.entityClass = entityClass;
		this.dao = dao;
		this.idField = idField;
		this.fkFieldConstraintPairSet = fkFieldConstraintPairSet;
		this.readPermFieldList = readPermFieldList;
		this.writePermFieldList = writePermFieldList;
		this.uploadField = uploadField;
		this.dbService = dbService;
		this.uploadService = uploadService;
		this.permFieldService = permFieldService;

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

	public Set<FieldConstraintPair> getPkFieldConstraintPairSet() {
		return pkFieldConstraintPairSet;
	}

	public List<Field> getReadPermFieldList() {
		return readPermFieldList;
	}

	public List<Field> getWritePermFieldList() {
		return writePermFieldList;
	}

	public boolean hasReadPermField() {
		return !Collections3.isEmpty(readPermFieldList);
	}

	public boolean hasWritePermField() {
		return !Collections3.isEmpty(writePermFieldList);
	}

	public Field getUploadField() {
		return uploadField;
	}

	public EntityDbService getDbService() {
		return dbService;
	}

	public EntityUploadService getUploadService() {
		return uploadService;
	}

	public EntityPermFieldService getPermFieldService() {
		return permFieldService;
	}

	public EntityDbServiceBuilder<T, ID> db() {
		return new EntityDbServiceBuilder<>(this);
	}

	public EntityUploadServiceBuilder<T, ID> upload() {
		return new EntityUploadServiceBuilder<>(this);
	}

	public EntityPermFieldServiceBuilder<T, ID> permField() {
		return new EntityPermFieldServiceBuilder<>(this);
	}
}
