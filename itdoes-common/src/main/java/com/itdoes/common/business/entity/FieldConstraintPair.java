package com.itdoes.common.business.entity;

import java.lang.reflect.Field;

import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
public class FieldConstraintPair {
	private final Class<?> fkEntity;
	private final Field fkField;
	private final Class<?> pkEntity;
	private final Field pkField;
	private final FieldConstraintStrategy updateStrategy;
	private final FieldConstraintStrategy deleteStragety;

	public FieldConstraintPair(Class<?> fkEntity, Field fkField, FieldConstraint fieldConstraint) {
		this.fkEntity = fkEntity;
		this.fkField = fkField;
		this.pkEntity = fieldConstraint.entity();
		this.pkField = Reflections.getField(pkEntity, fieldConstraint.field());
		this.updateStrategy = fieldConstraint.updateStrategy();
		this.deleteStragety = fieldConstraint.deleteStrategy();
	}

	public Class<?> getFkEntity() {
		return fkEntity;
	}

	public Field getFkField() {
		return fkField;
	}

	public Class<?> getPkEntity() {
		return pkEntity;
	}

	public Field getPkField() {
		return pkField;
	}

	public FieldConstraintStrategy getUpdateStrategy() {
		return updateStrategy;
	}

	public FieldConstraintStrategy getDeleteStragety() {
		return deleteStragety;
	}
}
