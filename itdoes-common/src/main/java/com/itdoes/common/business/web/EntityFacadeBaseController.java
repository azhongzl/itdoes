package com.itdoes.common.business.web;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.itdoes.common.business.EntityFacadeEnv;
import com.itdoes.common.business.EntityFacadePair;
import com.itdoes.common.business.Permissions;
import com.itdoes.common.business.service.EntityFacadeFieldSecurerService;
import com.itdoes.common.business.service.EntityFacadeTransactionalService;

/**
 * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Facade Controller Usage">
 * <tr style="background-color: rgb(204, 204, 255);">
 * <th align=left>Operation
 * <th align=left>URL
 * <th align=left>Method
 * <th align=left>Parameter
 * <tr>
 * <td><code>Find</code>
 * <td>/facade/&lt;Entity_Class&gt;/find
 * <td>GET
 * <td>
 * <ul>
 * <li>1) Filter:<br/>
 * ff_&lt;property&gt;[_Operator]=&lt;Value&gt;<br/>
 * Operator: EQ, LIKE, LT, GT, LTE, GTE, BTWN, if Operator is not set, default is EQ<br/>
 * Examples:<br/>
 * ff_username_EQ=Jalen (the same as: ff_username=Jalen<br/>
 * ff_email_EQ=azhongzl@gmail.com (the same as: ff_email=azhongzl@gmail.com)<br/>
 * ff_age_GTE=40 (GTE should NOT be omitted)<br/>
 * ff_age_BTWN=30_40 (BTWN should NOT be omitted. Two values are separated by '_')<br/>
 * ff_department.id=3 (the same as: ff_department.id_EQ=3<br/>
 * <li>2) Page:<br/>
 * page_no=1<br/>
 * page_size=50<br/>
 * page_sort=username_a, "a" = ASC, "d" = DESC
 * </ul>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>FindOne</code>
 * <td>/facade/&lt;Entity_Class&gt;/findOne
 * <td>GET
 * <td>The same as "Filter" parameter of "Find" operation, but without "Page" parameter
 * <tr>
 * <td><code>Count</code>
 * <td>/facade/&lt;Entity_Class&gt;/count
 * <td>GET
 * <td>The same as "Filter" parameter of "Find" operation, but without "Page" parameter
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Get</code>
 * <td>/facade/&lt;Entity_Class&gt;/get/&lt;id&gt;
 * <td>GET
 * <td>
 * <tr>
 * <td><code>Delete</code>
 * <td>/facade/&lt;Entity_Class&gt;/delete/&lt;id&gt;
 * <td>GET or POST
 * <td>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Post</code>
 * <td>/facade/&lt;Entity_Class&gt;/post
 * <td>POST
 * <td>
 * <tr>
 * <td><code>Put</code>
 * <td>/facade/&lt;Entity_Class&gt;/put/&lt;id&gt;
 * <td>POST
 * <td>
 * </table>
 * </blockquote>
 * 
 * @author Jalen Zhong
 */
public abstract class EntityFacadeBaseController extends BaseController {
	public static final String FACADE_URL_PREFIX = "/" + Permissions.PERM_FACADE;
	public static final String FACADE_URL_FIND = "find";
	public static final String FACADE_URL_FIND_ONE = "findOne";
	public static final String FACADE_URL_COUNT = "count";
	public static final String FACADE_URL_GET = "get";
	public static final String FACADE_URL_DELETE = "delete";
	public static final String FACADE_URL_POST = "post";
	public static final String FACADE_URL_PUT = "put";

	@Autowired
	protected EntityFacadeEnv env;

	@Autowired
	protected EntityFacadeTransactionalService facadeService;

	@Autowired
	protected EntityFacadeFieldSecurerService facadeFieldSecurerService;

	protected <T, ID extends Serializable> EntityFacadePair<T, ID> getPair(String ec) {
		return env.getPair(ec);
	}

	@SuppressWarnings("unchecked")
	protected <T, ID extends Serializable> ID convertId(EntityFacadePair<T, ID> pair, String id) {
		return (ID) convertId(id, pair.getIdField().getType());
	}
}
