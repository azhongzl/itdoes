package com.itdoes.common.business.entity;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Jalen Zhong
 */
@Target({ FIELD })
@Retention(RUNTIME)
public @interface FieldConstraint {
	Class<?> entity();

	String field();

	FieldConstraintStrategy updateStrategy();

	FieldConstraintStrategy deleteStrategy();

	String defaultValue() default "";
}
