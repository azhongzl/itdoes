package com.itdoes.business.web;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.business.entity.InvCompany;
import com.itdoes.business.service.FacadeService;
import com.itdoes.common.jpa.SearchFilter;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/")
public class FacadeController {
	@Autowired
	private FacadeService facadeService;

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public Result search(@RequestParam("filter") String filter) {
		final FilterWrapper filterWrapper = parseFilter(filter);
		List list = facadeService.getAll(filterWrapper.entityClass, filterWrapper.filters);
		return Result.success(list.toArray(new Object[list.size()]));
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
