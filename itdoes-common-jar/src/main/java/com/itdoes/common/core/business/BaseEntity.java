package com.itdoes.common.core.business;

import javax.persistence.MappedSuperclass;

import com.itdoes.common.core.BaseBean;

/**
 * @author Jalen Zhong
 */
@MappedSuperclass
public abstract class BaseEntity extends BaseBean {
	private static final long serialVersionUID = 5916437281913234215L;
}
