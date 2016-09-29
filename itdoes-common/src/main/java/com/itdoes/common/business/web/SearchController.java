package com.itdoes.common.business.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.service.SearchService;
import com.itdoes.common.core.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "search", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class SearchController extends BaseController {
	@Autowired
	protected SearchService searchService;

}
