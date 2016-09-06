package com.itdoes.common.business.web;

import javax.servlet.ServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.entity.BaseEntity;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = FacadeBaseController.FACADE_URL_PREFIX, produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class FacadeMainController extends FacadeBaseController {
	@RequestMapping(value = "/{ec}/" + FACADE_URL_SEARCH, method = RequestMethod.GET)
	public Result search(@PathVariable("ec") String ec, @RequestParam(value = "page_no", defaultValue = "1") int pageNo,
			@RequestParam(value = "page_size", defaultValue = "-1") int pageSize,
			@RequestParam(value = "page_sort", required = false) String pageSort, ServletRequest request) {
		final EntityPair pair = facadeService.getEntityPair(ec);
		final Page<? extends BaseEntity> page = facadeService.search(pair,
				buildSpecification(pair.getEntityClass(), request), buildPageRequest(pageNo, pageSize, pageSort));
		return HttpResults.success(page);
	}

	@RequestMapping(value = "/{ec}/" + FACADE_URL_COUNT, method = RequestMethod.GET)
	public Result count(@PathVariable("ec") String ec, ServletRequest request) {
		final EntityPair pair = facadeService.getEntityPair(ec);
		final long count = facadeService.count(pair, buildSpecification(pair.getEntityClass(), request));
		return HttpResults.success(count);
	}

	@RequestMapping(value = "/{ec}/" + FACADE_URL_GET + "/{id}", method = RequestMethod.GET)
	public Result get(@PathVariable("ec") String ec, @PathVariable("id") String id) {
		final EntityPair pair = facadeService.getEntityPair(ec);
		final BaseEntity entity = facadeService.get(pair, convertId(pair, id));
		return HttpResults.success(entity);
	}

	@RequestMapping(value = "/{ec}/" + FACADE_URL_DELETE + "/{id}")
	public Result delete(@PathVariable("ec") String ec, @PathVariable("id") String id) {
		final EntityPair pair = facadeService.getEntityPair(ec);
		facadeService.delete(pair, convertId(pair, id));
		return HttpResults.success();
	}
}
