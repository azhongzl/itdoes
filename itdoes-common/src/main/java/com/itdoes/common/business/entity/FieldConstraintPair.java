package com.itdoes.common.business.entity;

import java.lang.reflect.Field;

import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
public class FieldConstraintPair {
	private final Class<?> referringEntity;
	private final Field referringField;
	private final Class<?> referredEntity;
	private final Field referredField;
	private final FieldConstraintStrategy updateStrategy;
	private final FieldConstraintStrategy deleteStragety;

	public FieldConstraintPair(Class<?> referringEntity, Field referringField, FieldConstraint fieldConstraint) {
		this.referringEntity = referringEntity;
		this.referringField = referringField;
		this.referredEntity = fieldConstraint.entity();
		this.referredField = Reflections.getField(referredEntity, fieldConstraint.field());
		this.updateStrategy = fieldConstraint.updateStrategy();
		this.deleteStragety = fieldConstraint.deleteStrategy();
	}

	public Class<?> getReferringEntity() {
		return referringEntity;
	}

	public Field getReferringField() {
		return referringField;
	}

	public Class<?> getReferredEntity() {
		return referredEntity;
	}

	public Field getReferredField() {
		return referredField;
	}

	public FieldConstraintStrategy getUpdateStrategy() {
		return updateStrategy;
	}

	public FieldConstraintStrategy getDeleteStragety() {
		return deleteStragety;
	}
}
