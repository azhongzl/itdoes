package com.itdoes.business.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.itdoes.business.service.FacadeService;
import com.itdoes.common.business.BaseController;
import com.itdoes.common.business.Result;
import com.itdoes.common.jpa.SearchFilter;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/facade")
public class FacadeController extends BaseController {
	@Autowired
	private FacadeService facadeService;

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public Result search(@RequestParam(value = "ec") String ec, @RequestParam("filter") String filter) {
		final FilterWrapper filterWrapper = parseFilter(filter);
		final List list = facadeService.getAll(filterWrapper.ec, filterWrapper.filters);
		return Result.success(list.toArray(new Object[list.size()]));
	}

	@RequestMapping(value = "get", method = RequestMethod.GET)
	public Result get(@RequestParam(value = "ec") String ec, @RequestParam("id") Integer id) {
		final Object entity = facadeService.get(ec, id);
		return null;
	}

	@RequestMapping(value = "delete")
	public Result delete(@RequestParam(value = "ec") String ec, @RequestParam("id") Integer id) {
		facadeService.delete(ec, id);
		return null;
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public Result create(@RequestParam(value = "ec") String ec, @Valid Object entity) {
		facadeService.save(ec, entity);
		return null;
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public Result update(@RequestParam(value = "ec") String ec, @Valid @ModelAttribute("entity") Object entity) {
		facadeService.save(ec, entity);
		return null;
	}

	@ModelAttribute
	public void getEntity(@RequestParam(value = "ec") String ec,
			@RequestParam(value = "id", defaultValue = "-1") Integer id, Model model) {
		if (id != -1) {
			model.addAttribute("entity", facadeService.get(ec, id));
		}
	}

	private FilterWrapper parseFilter(String filter) {
		FilterWrapper filterWrapper = new FilterWrapper();
		filterWrapper.ec = "InvCompany";
		filterWrapper.filters = Lists.newArrayList();
		return filterWrapper;
	}

	private static class FilterWrapper {
		public String ec;
		public List<SearchFilter> filters;
	}
}
