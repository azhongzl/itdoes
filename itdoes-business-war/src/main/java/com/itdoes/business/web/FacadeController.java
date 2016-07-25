package com.itdoes.business.web;

import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.business.service.FacadeService;
import com.itdoes.common.business.BaseController;
import com.itdoes.common.business.BaseEntity;
import com.itdoes.common.business.Result;
import com.itdoes.common.jpa.SearchFilter;
import com.itdoes.common.web.MediaTypes;

/**
 * @author Jalen Zhong
 */
@RestController
@RequestMapping(value = "/facade", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class FacadeController extends BaseController {
	@Autowired
	private FacadeService facadeService;

	/**
	 * Search parameter examples (case sensitive):
	 * 
	 * <pre>
	 * Entity Class:
	 *   ec=InvCompany
	 * 
	 * Filters (if no operator provided, use default EQ):
	 *   ff_username_EQ=Jalen (the same as: ff_username=Jalen
	 *   ff_email_EQ=azhongzl@gmail.com (the same as: ff_email=azhongzl@gmail.com)
	 *   ff_age_GTE=40 (GTE should NOT be omitted)
	 *   ff_department.id=3 (the same as: ff_department.id_EQ=3
	 * 
	 * Pages:
	 *   page_no=1
	 *   page_size=50
	 *   page_sort=username_a
	 * </pre>
	 */
	@RequestMapping(value = "search", method = RequestMethod.GET)
	public String search(@RequestParam(value = "ec") String ec,
			@RequestParam(value = "page_no", defaultValue = "1") int pageNo,
			@RequestParam(value = "page_size", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(value = "page_sort", required = false) String pageSort, ServletRequest request) {
		final List<SearchFilter> filters = buildFilters(request);
		final PageRequest pageRequest = buildPageRequest(pageNo, pageSize, pageSort);
		final Page<? extends BaseEntity> page = facadeService.search(ec, filters, pageRequest);
		final List<? extends BaseEntity> list = page.getContent();
		return toJson(Result.success(list.toArray(new BaseEntity[list.size()])));
	}

	@RequestMapping(value = "get", method = RequestMethod.GET)
	public String get(@RequestParam(value = "ec") String ec, @RequestParam("id") Serializable id) {
		final BaseEntity entity = facadeService.get(ec, id);
		return toJson(Result.success(new BaseEntity[] { entity }));
	}

	@RequestMapping(value = "delete")
	public String delete(@RequestParam(value = "ec") String ec, @RequestParam("id") Serializable id) {
		facadeService.delete(ec, id);
		return toJson(Result.success(null));
	}
}
