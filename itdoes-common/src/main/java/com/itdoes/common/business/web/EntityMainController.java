package com.itdoes.common.business.web;

import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.entity.EntityPermCommand;
import com.itdoes.common.core.Result;
import com.itdoes.common.core.web.HttpResults;
import com.itdoes.common.core.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = EntityBaseController.ENTITY_URL_PREFIX, produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class EntityMainController extends EntityBaseController {
	@RequestMapping(value = "/{ec}/" + EntityPermCommand.Command.FIND, method = RequestMethod.GET)
	public <T, ID extends Serializable> Result find(@PathVariable("ec") String ec,
			@RequestParam(value = BaseController.PAGE_NO, defaultValue = "1") int pageNo,
			@RequestParam(value = BaseController.PAGE_SIZE, defaultValue = "-1") int pageSize,
			@RequestParam(value = BaseController.PAGE_SORT, required = false) String pageSort, ServletRequest request) {
		final EntityPair<T, ID> pair = getEntityPair(ec);
		final Page<T> page = pair.getService().find(pair, buildSpecification(pair.getEntityClass(), request),
				buildPageRequest(pageNo, pageSize, pageSort));
		return HttpResults.success(page);
	}

	@RequestMapping(value = "/{ec}/" + EntityPermCommand.Command.FIND_ALL, method = RequestMethod.GET)
	public <T, ID extends Serializable> Result findAll(@PathVariable("ec") String ec,
			@RequestParam(value = BaseController.PAGE_SORT, required = false) String pageSort, ServletRequest request) {
		final EntityPair<T, ID> pair = getEntityPair(ec);
		final List<T> list = pair.getService().findAll(pair, buildSpecification(pair.getEntityClass(), request),
				buildSort(pageSort));
		return HttpResults.success(list);
	}

	@RequestMapping(value = "/{ec}/" + EntityPermCommand.Command.FIND_ONE, method = RequestMethod.GET)
	public <T, ID extends Serializable> Result findOne(@PathVariable("ec") String ec, ServletRequest request) {
		final EntityPair<T, ID> pair = getEntityPair(ec);
		final T entity = pair.getService().findOne(pair, buildSpecification(pair.getEntityClass(), request));
		return HttpResults.success(entity);
	}

	@RequestMapping(value = "/{ec}/" + EntityPermCommand.Command.COUNT, method = RequestMethod.GET)
	public <T, ID extends Serializable> Result count(@PathVariable("ec") String ec, ServletRequest request) {
		final EntityPair<T, ID> pair = getEntityPair(ec);
		final long count = pair.getService().count(pair, buildSpecification(pair.getEntityClass(), request));
		return HttpResults.success(count);
	}

	@RequestMapping(value = "/{ec}/" + EntityPermCommand.Command.GET + "/{id}", method = RequestMethod.GET)
	public <T, ID extends Serializable> Result get(@PathVariable("ec") String ec, @PathVariable("id") String id) {
		final EntityPair<T, ID> pair = getEntityPair(ec);
		final T entity = pair.getService().get(pair, convertId(pair, id));
		return HttpResults.success(entity);
	}

	@RequestMapping(value = "/{ec}/" + EntityPermCommand.Command.DELETE + "/{id}")
	public <T, ID extends Serializable> Result delete(@PathVariable("ec") String ec, @PathVariable("id") String id) {
		final EntityPair<T, ID> pair = getEntityPair(ec);
		pair.getService().delete(pair, convertId(pair, id), getUploadRealRootPath(), isUploadDeleteOrphanFiles());
		return HttpResults.success();
	}
}
