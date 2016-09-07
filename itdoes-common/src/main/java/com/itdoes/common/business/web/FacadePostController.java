package com.itdoes.common.business.web;

import java.io.Serializable;

import javax.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.util.Reflections;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = FacadeBaseController.FACADE_URL_PREFIX, produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class FacadePostController extends FacadeBaseController {
	@RequestMapping(value = "/{ec}/" + FacadeMainController.FACADE_URL_POST, method = RequestMethod.POST)
	public <T, ID extends Serializable> Result post(@PathVariable(value = "ec") String ec,
			@Valid @ModelAttribute("entity") T entity) {
		final EntityPair<T, ID> pair = getEntityPair(ec);
		final ID id = facadeServiceFieldSecurer.securePost(pair, entity);
		return HttpResults.success(id);
	}

	@ModelAttribute
	public <T, ID extends Serializable> void getEntity(@PathVariable(value = "ec") String ec, Model model) {
		final EntityPair<T, ID> pair = getEntityPair(ec);
		final T entity = Reflections.newInstance(pair.getEntityClass());
		model.addAttribute("entity", entity);
	}
}
