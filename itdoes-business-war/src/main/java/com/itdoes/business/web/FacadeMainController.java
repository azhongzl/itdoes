package com.itdoes.business.web;

import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.BaseEntity;
import com.itdoes.common.business.Result;
import com.itdoes.common.jpa.SearchFilter;
import com.itdoes.common.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = FacadeBaseController.FACADE_URL_PREFIX, produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class FacadeMainController extends FacadeBaseController {
	@RequestMapping(value = "/{ec}/" + FACADE_URL_SEARCH, method = RequestMethod.GET)
	public Result search(@PathVariable("ec") String ec, @RequestParam(value = "page_no", defaultValue = "1") int pageNo,
			@RequestParam(value = "page_size", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(value = "page_sort", required = false) String pageSort, ServletRequest request) {
		final List<SearchFilter> filters = buildFilters(request);
		final PageRequest pageRequest = buildPageRequest(pageNo, pageSize, pageSort);
		final Page<? extends BaseEntity> page = facadeService.search(ec, filters, pageRequest);
		final List<? extends BaseEntity> list = page.getContent();
		return Result.success(list.toArray(new BaseEntity[list.size()]));
	}

	@RequestMapping(value = "/{ec}/" + FACADE_URL_COUNT, method = RequestMethod.GET)
	public Result count(@PathVariable("ec") String ec, ServletRequest request) {
		final List<SearchFilter> filters = buildFilters(request);
		final long count = facadeService.count(ec, filters);
		return Result.success(new Long[] { count });
	}

	@RequestMapping(value = "/{ec}/" + FACADE_URL_GET + "/{id}", method = RequestMethod.GET)
	public Result get(@PathVariable("ec") String ec, @PathVariable("id") String id) {
		final BaseEntity entity = facadeService.get(ec, id);
		return Result.success(new BaseEntity[] { entity });
	}

	@RequestMapping(value = "/{ec}/" + FACADE_URL_DELETE + "/{id}")
	public Result delete(@PathVariable("ec") String ec, @PathVariable("id") String id) {
		facadeService.delete(ec, id);
		return Result.success();
	}
}
