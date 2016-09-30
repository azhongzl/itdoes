package com.itdoes.common.business.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.Permissions;
import com.itdoes.common.business.service.SearchService;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = SearchController.URL_PREFIX, produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class SearchController extends BaseController {
	public static final String URL_PREFIX = "/" + Permissions.SEARCH_PERMISSION_PREFIX;

	@Autowired
	protected SearchService searchService;

	@RequestMapping(value = "/createIndex", method = RequestMethod.GET)
	public Result createIndex() {
		searchService.createIndex();
		return HttpResults.success();
	}
}
