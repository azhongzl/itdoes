package com.itdoes.business.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.business.service.FacadeService;
import com.itdoes.common.business.BaseController;
import com.itdoes.common.business.BaseEntity;
import com.itdoes.common.business.Businesses.EntityPair;
import com.itdoes.common.business.Result;
import com.itdoes.common.util.Exceptions;
import com.itdoes.common.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = FacadeController.FACADE_URL_PREFIX, produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class FacadePostController extends BaseController {
	@Autowired
	private FacadeService facadeService;

	@RequestMapping(value = "/{ec}/" + FacadeController.FACADE_URL_POST, method = RequestMethod.POST)
	public String post(@PathVariable(value = "ec") String ec, @Valid @ModelAttribute("entity") BaseEntity entity) {
		facadeService.save(ec, entity);
		return toJson(Result.success(new BaseEntity[] { entity }));
	}

	@ModelAttribute
	public void getEntity(@PathVariable(value = "ec") String ec, Model model) {
		final EntityPair pair = facadeService.getEntityPair(ec);
		try {
			final Object entity = pair.entityClass.newInstance();
			model.addAttribute("entity", entity);
		} catch (InstantiationException | IllegalAccessException e) {
			throw Exceptions.unchecked(e);
		}
	}
}
