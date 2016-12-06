package com.itdoes.common.business.web;

import javax.servlet.ServletRequest;

import com.itdoes.common.core.cglib.CglibMapper;

/**
 * @author Jalen Zhong
 */
public abstract class BaseEntityPutController extends BaseEntityController {
	private static final String ENTITY_KEY = "entity";
	private static final String OLD_ENTITY_KEY = "oldEntity";

	protected void cacheEntity(ServletRequest request, Object entity) {
		request.setAttribute(ENTITY_KEY, entity);

		final Object oldEntity = CglibMapper.copy(entity);
		request.setAttribute(OLD_ENTITY_KEY, oldEntity);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getOldEntity(ServletRequest request) {
		return (T) request.getAttribute(OLD_ENTITY_KEY);
	}
}
