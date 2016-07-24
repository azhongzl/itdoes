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
import com.itdoes.business.entity.InvCompany;
import com.itdoes.business.service.FacadeService;
import com.itdoes.common.jpa.SearchFilter;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/")
public class FacadeController extends BaseController {
	@Autowired
	private FacadeService facadeService;

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public Result search(@RequestParam("filter") String filter) {
		final FilterWrapper filterWrapper = parseFilter(filter);
		final List list = facadeService.getAll(filterWrapper.entityClass, filterWrapper.filters);
		return Result.success(list.toArray(new Object[list.size()]));
	}

	@RequestMapping(value = "get", method = RequestMethod.GET)
	public Result get(@RequestParam(value = "entityClass") Class entityClass, @RequestParam("id") Integer id) {
		final Object entity = facadeService.get(entityClass, id);
		return null;
	}

	@RequestMapping(value = "delete")
	public Result delete(@RequestParam(value = "entityClass") Class entityClass, @RequestParam("id") Integer id) {
		facadeService.delete(entityClass, id);
		return null;
	}

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public Result create(@Valid Object entity) {
		facadeService.save(entity);
		return null;
	}

	@RequestMapping(value = "update", method = RequestMethod.POST)
	public Result update(@Valid @ModelAttribute("entity") Object entity) {
		facadeService.save(entity);
		return null;
	}

	@ModelAttribute
	public void getEntity(@RequestParam(value = "entityClass") Class entityClass,
			@RequestParam(value = "id", defaultValue = "-1") Integer id, Model model) {
		if (id != -1) {
			model.addAttribute("entity", facadeService.get(entityClass, id));
		}
	}

	private FilterWrapper parseFilter(String filter) {
		FilterWrapper filterWrapper = new FilterWrapper();
		filterWrapper.entityClass = InvCompany.class;
		filterWrapper.filters = Lists.newArrayList();
		return filterWrapper;
	}

	private static class FilterWrapper {
		public Class entityClass;
		public List<SearchFilter> filters;
	}
}
