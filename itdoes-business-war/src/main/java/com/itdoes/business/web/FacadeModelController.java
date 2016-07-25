package com.itdoes.business.web;

import javax.servlet.http.HttpServletRequest;
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
import com.itdoes.common.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/facade", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class FacadeModelController extends BaseController {
	@Autowired
	private FacadeService facadeService;

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(@RequestParam(value = "ec") String ec, HttpServletRequest request) {
		BaseEntity entity = (BaseEntity)request.getAttribute("entity");
		System.out.println(entity.getClass() + ":" + entity);
		facadeService.save(ec, entity);
		return toJson(Result.success(new BaseEntity[] { entity }));
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@RequestParam(value = "ec") String ec, @Valid @ModelAttribute("entity") BaseEntity entity) {
		facadeService.save(ec, entity);
		return toJson(Result.success(new BaseEntity[] { entity }));
	}

	@ModelAttribute
	public void getEntity(@RequestParam(value = "ec") String ec,
			@RequestParam(value = "id", required = false) String id, Model model) {
		if (id != null) {
			model.addAttribute("entity", facadeService.get(ec, id));
		} else {
			BaseEntity be = facadeService.newInstance(ec);
			model.addAttribute("entity", facadeService.newInstance(ec));
		}
	}
}
