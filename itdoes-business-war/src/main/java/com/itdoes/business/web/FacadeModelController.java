package com.itdoes.business.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.business.service.FacadeService;
import com.itdoes.common.business.BaseController;
import com.itdoes.common.business.BaseEntity;
import com.itdoes.common.business.Result;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/facade")
public class FacadeModelController extends BaseController {
	@Autowired
	private FacadeService facadeService;

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public Result create(@RequestParam(value = "ec") String ec, @Valid @ModelAttribute("entity") BaseEntity entity) {
		facadeService.save(ec, entity);
		return null;
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public Result update(@RequestParam(value = "ec") String ec, @Valid @ModelAttribute("entity") BaseEntity entity) {
		facadeService.save(ec, entity);
		return null;
	}

	@ModelAttribute
	public void getEntity(@RequestParam(value = "ec") String ec,
			@RequestParam(value = "id", defaultValue = "-1") Integer id, Model model) {
		if (id != -1) {
			model.addAttribute("entity", facadeService.get(ec, id));
		} else {
			model.addAttribute("entity", facadeService.newInstance(ec));
		}
	}
}
