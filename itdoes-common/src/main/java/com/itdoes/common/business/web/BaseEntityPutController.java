package com.itdoes.common.business.web;

import javax.servlet.ServletRequest;

import com.itdoes.common.core.cglib.CglibMapper;

/**
 * @author Jalen Zhong
 */
public abstract class BaseEntityPutController extends BaseEntityController {
	protected void cacheEntity(ServletRequest request, Object entity) {
		request.setAttribute("entity", entity);

		final Object oldEntity = CglibMapper.copy(entity);
		request.setAttribute("oldEntity", oldEntity);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getOldEntity(ServletRequest request) {
		return (T) request.getAttribute("oldEntity");
	}
}
