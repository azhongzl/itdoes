package com.itdoes.common.business.web;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.Businesses.EntityPair;
import com.itdoes.common.business.entity.BaseEntity;
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
	@RequestMapping(value = "/{ec}/" + FacadeMainController.FACADE_URL_PUT + "/{id}", method = RequestMethod.POST)
	public Result put(@PathVariable("ec") String ec, @Valid @ModelAttribute("entity") BaseEntity entity,
			ServletRequest request) {
		final BaseEntity oldEntity = (BaseEntity) request.getAttribute("oldEntity");
		facadeService.put(ec, entity, oldEntity);
		return HttpResults.success(entity);
	}

	@ModelAttribute
	public void getEntity(@PathVariable("ec") String ec, @PathVariable("id") String id, Model model,
			ServletRequest request) {
		final BaseEntity entity = facadeService.get(ec, id);
		model.addAttribute("entity", entity);

		final EntityPair pair = facadeService.getEntityPair(ec);
		if (hasSecureColumns(pair)) {
			final BaseEntity oldEntity = (BaseEntity) CglibMapper.copy(entity);
			request.setAttribute("oldEntity", oldEntity);
		}
	}
}
