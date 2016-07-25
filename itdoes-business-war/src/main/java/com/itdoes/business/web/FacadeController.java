package com.itdoes.business.web;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.itdoes.business.service.FacadeService;
import com.itdoes.common.business.BaseController;
import com.itdoes.common.business.BaseEntity;
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
		final List<? extends BaseEntity> list = facadeService.getAll(filterWrapper.ec, filterWrapper.filters);
		return Result.success(list.toArray(new BaseEntity[list.size()]));
	}

	@RequestMapping(value = "get", method = RequestMethod.GET)
	public Result get(@RequestParam(value = "ec") String ec, @RequestParam("id") Serializable id) {
		final BaseEntity entity = facadeService.get(ec, id);
		return Result.success(new BaseEntity[] { entity });
	}

	@RequestMapping(value = "delete")
	public Result delete(@RequestParam(value = "ec") String ec, @RequestParam("id") Serializable id) {
		facadeService.delete(ec, id);
		return Result.success(null);
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
