package com.itdoes.business.web;

import java.util.List;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
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
 * 
 *         <blockquote>
 *         <table border=0 cellspacing=3 cellpadding=0 summary="Usage of FacadeController">
 *         <tr style="background-color: rgb(204, 204, 255);">
 *         <th align=left>Operation
 *         <th align=left>URL
 *         <th align=left>Method
 *         <th align=left>Parameter
 *         <tr>
 *         <td><code>Search</code>
 *         <td>/facade/(Entity_Class)/search
 *         <td>GET</a>
 *         <td>
 *         <ul>
 *         <li>1) Filter:<br/>
 *         ff_(Property)[_Operator]=(Value)<br/>
 *         Operator: EQ, LIKE, LT, GT, LTE, GTE, if Operator is not set, default is EQ<br/>
 *         Examples:<br/>
 *         ff_username_EQ=Jalen (the same as: ff_username=Jalen<br/>
 *         ff_email_EQ=azhongzl@gmail.com (the same as: ff_email=azhongzl@gmail.com)<br/>
 *         ff_age_GTE=40 (GTE should NOT be omitted)<br/>
 *         ff_department.id=3 (the same as: ff_department.id_EQ=3<br/>
 *         <li>2) Page:<br/>
 *         page_no=1<br/>
 *         page_size=50<br/>
 *         page_sort=username_a, "a" = ASC, "d" = DESC
 *         </ul>
 *         <tr style="background-color: rgb(238, 238, 255);">
 *         <td><code>Get</code>
 *         <td>/facade/(Entity_Class)/get/(id)
 *         <td>GET</a>
 *         <td>
 *         <tr>
 *         <td><code>Delete</code>
 *         <td>/facade/(Entity_Class)/delete/(id)
 *         <td>GET or POST
 *         <td>
 *         <tr style="background-color: rgb(238, 238, 255);">
 *         <td><code>Post</code>
 *         <td>/facade/(Entity_Class)/post
 *         <td>POST
 *         <td>
 *         <tr>
 *         <td><code>Put</code>
 *         <td>/facade/(Entity_Class)/put/(id)
 *         <td>POST
 *         <td>
 *         </table>
 *         </blockquote>
 */
@RestController
@RequestMapping(value = "/facade", produces = MediaTypes.APPLICATION_JSON_UTF_8)
public class FacadeController extends BaseController {
	@Autowired
	private FacadeService facadeService;

	@RequestMapping(value = "/{ec}/search", method = RequestMethod.GET)
	public String search(@PathVariable("ec") String ec, @RequestParam(value = "page_no", defaultValue = "1") int pageNo,
			@RequestParam(value = "page_size", defaultValue = DEFAULT_PAGE_SIZE) int pageSize,
			@RequestParam(value = "page_sort", required = false) String pageSort, ServletRequest request) {
		final List<SearchFilter> filters = buildFilters(request);
		final PageRequest pageRequest = buildPageRequest(pageNo, pageSize, pageSort);
		final Page<? extends BaseEntity> page = facadeService.search(ec, filters, pageRequest);
		final List<? extends BaseEntity> list = page.getContent();
		return toJson(Result.success(list.toArray(new BaseEntity[list.size()])));
	}

	@RequestMapping(value = "/{ec}/get/{id}", method = RequestMethod.GET)
	public String get(@PathVariable("ec") String ec, @PathVariable("id") String id) {
		final BaseEntity entity = facadeService.get(ec, id);
		return toJson(Result.success(new BaseEntity[] { entity }));
	}

	@RequestMapping(value = "/{ec}/delete/{id}")
	public String delete(@PathVariable("ec") String ec, @PathVariable("id") String id) {
		facadeService.delete(ec, id);
		return toJson(Result.success(null));
	}
}
