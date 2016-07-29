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
import com.itdoes.common.business.Result;
import com.itdoes.common.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = FacadeController.FACADE_URL_PREFIX, produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class FacadePutController extends BaseController {
	@Autowired
	private FacadeService facadeService;

	@RequestMapping(value = "/{ec}/" + FacadeController.FACADE_URL_PUT + "/{id}", method = RequestMethod.POST)
	public String put(@PathVariable("ec") String ec, @Valid @ModelAttribute("entity") BaseEntity entity) {
		facadeService.put(ec, entity);
		return toJson(Result.success(new BaseEntity[] { entity }));
	}

	@ModelAttribute
	public void getEntity(@PathVariable("ec") String ec, @PathVariable("id") String id, Model model) {
		model.addAttribute("entity", facadeService.get(ec, id));
	}
}
