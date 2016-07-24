package com.itdoes.business.entity;

import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author Jalen Zhong
 */
@MappedSuperclass
public abstract class BaseEntity {
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
