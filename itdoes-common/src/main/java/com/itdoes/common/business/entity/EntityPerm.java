package com.itdoes.common.business.entity;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Jalen Zhong
 */
@Target({ TYPE })
@Retention(RUNTIME)
public @interface EntityPerm {
	EntityPermCommand command();

	EntityPermFilter filter();
}
