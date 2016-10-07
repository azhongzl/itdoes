package com.itdoes.common.business.web;

import java.io.Serializable;

import javax.servlet.ServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = EntityBaseController.ENTITY_URL_PREFIX, produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class EntityMainController extends EntityBaseController {
	@RequestMapping(value = "/{ec}/" + ENTITY_COMMAND_FIND, method = RequestMethod.GET)
	public <T, ID extends Serializable> Result find(@PathVariable("ec") String ec,
			@RequestParam(value = BaseController.PAGE_NO, defaultValue = "1") int pageNo,
			@RequestParam(value = BaseController.PAGE_SIZE, defaultValue = "-1") int pageSize,
			@RequestParam(value = BaseController.PAGE_SORT, required = false) String pageSort, ServletRequest request) {
		final EntityPair<T, ID> pair = getPair(ec);
		final Page<T> page = subjectService.find(pair, buildSpecification(pair.getEntityClass(), request),
				buildPageRequest(pageNo, pageSize, pageSort));
		return HttpResults.success(page);
	}

	@RequestMapping(value = "/{ec}/" + ENTITY_COMMAND_FIND_ONE, method = RequestMethod.GET)
	public <T, ID extends Serializable> Result findOne(@PathVariable("ec") String ec, ServletRequest request) {
		final EntityPair<T, ID> pair = getPair(ec);
		final T entity = subjectService.findOne(pair, buildSpecification(pair.getEntityClass(), request));
		return HttpResults.success(entity);
	}

	@RequestMapping(value = "/{ec}/" + ENTITY_COMMAND_COUNT, method = RequestMethod.GET)
	public <T, ID extends Serializable> Result count(@PathVariable("ec") String ec, ServletRequest request) {
		final EntityPair<T, ID> pair = getPair(ec);
		final long count = subjectService.count(pair, buildSpecification(pair.getEntityClass(), request));
		return HttpResults.success(count);
	}

	@RequestMapping(value = "/{ec}/" + ENTITY_COMMAND_GET + "/{id}", method = RequestMethod.GET)
	public <T, ID extends Serializable> Result get(@PathVariable("ec") String ec, @PathVariable("id") String id) {
		final EntityPair<T, ID> pair = getPair(ec);
		final T entity = subjectService.get(pair, convertId(pair, id));
		return HttpResults.success(entity);
	}

	@RequestMapping(value = "/{ec}/" + ENTITY_COMMAND_DELETE + "/{id}")
	public <T, ID extends Serializable> Result delete(@PathVariable("ec") String ec, @PathVariable("id") String id) {
		final EntityPair<T, ID> pair = getPair(ec);
		subjectService.delete(pair, convertId(pair, id), realRootPath, isUploadDeleteOrphanFiles());
		return HttpResults.success();
	}
}