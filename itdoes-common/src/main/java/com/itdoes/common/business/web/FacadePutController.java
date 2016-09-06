package com.itdoes.common.business.web;

import java.io.Serializable;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Permissions;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.cglib.CglibMapper;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = FacadeBaseController.FACADE_URL_PREFIX, produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class FacadePutController extends FacadeBaseController {
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/{ec}/" + FacadeMainController.FACADE_URL_PUT + "/{id}", method = RequestMethod.POST)
	public <T, ID extends Serializable> Result put(@PathVariable("ec") String ec,
			@Valid @ModelAttribute("entity") T entity, ServletRequest request) {
		final EntityPair<T, ID> pair = env.getEntityPair(ec);
		final T oldEntity = (T) request.getAttribute("oldEntity");

		Permissions.handlePutSecureFields(pair, entity, oldEntity);

		facadeService.save(pair, entity);
		return HttpResults.success(entity);
	}

	@SuppressWarnings("unchecked")
	@ModelAttribute
	public <T, ID extends Serializable> void getEntity(@PathVariable("ec") String ec, @PathVariable("id") String id,
			Model model, ServletRequest request) {
		final EntityPair<T, ID> pair = env.getEntityPair(ec);

		final T entity = facadeService.get(pair, convertId(pair, id));
		model.addAttribute("entity", entity);

		if (pair.hasSecureFields()) {
			final T oldEntity = (T) CglibMapper.copy(entity);
			request.setAttribute("oldEntity", oldEntity);
		}
	}
}
