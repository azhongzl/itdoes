package com.itdoes.common.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.itdoes.common.business.dao.BaseDao;
import com.itdoes.common.business.entity.EntityPerm;
import com.itdoes.common.business.entity.FieldConstraint;
import com.itdoes.common.business.service.EntityService;
import com.itdoes.common.core.cglib.CglibMapper;
import com.itdoes.common.core.util.Collections3;

/**
 * @author Jalen Zhong
 */
public class EntityPair<T, ID extends Serializable> {
	private final Class<T> entityClass;
	private final BaseDao<T, ID> dao;
	private final Field idField;
	private final Map<Field, FieldConstraint> referringFieldConstraintMap;
	private final EntityPerm entityPerm;
	private final List<Field> readPermFieldList;
	private final List<Field> writePermFieldList;
	private final Field uploadField;

	private final Map<Field, Set<FieldConstraint>> referredFieldConstraintMap = Maps.newHashMap();
	private EntityService service;

	public EntityPair(Class<T> entityClass, BaseDao<T, ID> dao, Field idField,
			Map<Field, FieldConstraint> referringFieldConstraintMap, EntityPerm entityPerm,
			List<Field> readPermFieldList, List<Field> writePermFieldList, Field uploadField) {
		this.entityClass = entityClass;
		this.dao = dao;
		this.idField = idField;
		this.referringFieldConstraintMap = referringFieldConstraintMap;
		this.entityPerm = entityPerm;
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

	public Map<Field, FieldConstraint> getReferringFieldConstraintMap() {
		return referringFieldConstraintMap;
	}

	public EntityPerm getEntityPerm() {
		return entityPerm;
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

	public Map<Field, Set<FieldConstraint>> getReferredFieldConstraintMap() {
		return referredFieldConstraintMap;
	}

	public void addReferredFieldConstraint(Field field, FieldConstraint fieldConstraint) {
		Set<FieldConstraint> fieldConstraintSet = referredFieldConstraintMap.get(field);
		if (fieldConstraintSet == null) {
			fieldConstraintSet = Sets.newHashSet();
			referredFieldConstraintMap.put(field, fieldConstraintSet);
		}
		fieldConstraintSet.add(fieldConstraint);
	}

	public EntityService getService() {
		return service;
	}

	public void setService(EntityService service) {
		this.service = service;
	}
}
