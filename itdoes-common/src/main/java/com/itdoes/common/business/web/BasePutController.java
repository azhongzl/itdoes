package com.itdoes.common.business.web;

import javax.servlet.ServletRequest;

import org.springframework.ui.Model;

import com.itdoes.common.core.cglib.CglibMapper;

/**
 * @author Jalen Zhong
 */
public abstract class BasePutController extends BaseController {
	protected static final String ENTITY_KEY = "entity";
	private static final String OLD_ENTITY_KEY = "oldEntity";

	protected void cacheEntity(Model model, ServletRequest request, Object entity) {
		model.addAttribute(ENTITY_KEY, entity);

		final Object oldEntity = CglibMapper.copy(entity);
		request.setAttribute(OLD_ENTITY_KEY, oldEntity);
	}

	@SuppressWarnings("unchecked")
	protected <T> T getOldEntity(ServletRequest request) {
		return (T) request.getAttribute(OLD_ENTITY_KEY);
	}
}
