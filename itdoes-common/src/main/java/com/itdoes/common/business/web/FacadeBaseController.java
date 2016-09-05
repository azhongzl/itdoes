package com.itdoes.common.business.web;

import org.springframework.beans.factory.annotation.Autowired;

import com.itdoes.common.business.service.FacadeService;

/**
 * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Usage of FacadeController">
 * <tr style="background-color: rgb(204, 204, 255);">
 * <th align=left>Operation
 * <th align=left>URL
 * <th align=left>Method
 * <th align=left>Parameter
 * <tr>
 * <td><code>Search</code>
 * <td>/facade/(Entity_Class)/search
 * <td>GET</a>
 * <td>
 * <ul>
 * <li>1) Filter:<br/>
 * ff_(Property)[_Operator]=(Value)<br/>
 * Operator: EQ, LIKE, LT, GT, LTE, GTE, if Operator is not set, default is EQ<br/>
 * Examples:<br/>
 * ff_username_EQ=Jalen (the same as: ff_username=Jalen<br/>
 * ff_email_EQ=azhongzl@gmail.com (the same as: ff_email=azhongzl@gmail.com)<br/>
 * ff_age_GTE=40 (GTE should NOT be omitted)<br/>
 * ff_department.id=3 (the same as: ff_department.id_EQ=3<br/>
 * <li>2) Page:<br/>
 * page_no=1<br/>
 * page_size=50<br/>
 * page_sort=username_a, "a" = ASC, "d" = DESC
 * </ul>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Count</code>
 * <td>/facade/(Entity_Class)/count
 * <td>GET</a>
 * <td>The same as "Filter" parameter of "Search" operation, but without "Page" parameter
 * <tr>
 * <td><code>Get</code>
 * <td>/facade/(Entity_Class)/get/(id)
 * <td>GET</a>
 * <td>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Delete</code>
 * <td>/facade/(Entity_Class)/delete/(id)
 * <td>GET or POST
 * <td>
 * <tr>
 * <td><code>Post</code>
 * <td>/facade/(Entity_Class)/post
 * <td>POST
 * <td>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Put</code>
 * <td>/facade/(Entity_Class)/put/(id)
 * <td>POST
 * <td>
 * </table>
 * </blockquote>
 * 
 * @author Jalen Zhong
 */
public abstract class FacadeBaseController extends BaseController {
	public static final String FACADE_URL_PREFIX = "/facade";
	public static final String FACADE_URL_SEARCH = "search";
	public static final String FACADE_URL_COUNT = "count";
	public static final String FACADE_URL_GET = "get";
	public static final String FACADE_URL_DELETE = "delete";
	public static final String FACADE_URL_POST = "post";
	public static final String FACADE_URL_PUT = "put";

	@Autowired
	protected FacadeService facadeService;
}
