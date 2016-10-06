package com.itdoes.common.business.web;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;

import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.service.EntityFieldSecurerService;
import com.itdoes.common.business.service.EntityTransactionalService;

/**
 * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Entity Controller Usage">
 * <tr style="background-color: rgb(204, 204, 255);">
 * <th align=left>Operation
 * <th align=left>URL
 * <th align=left>Method
 * <th align=left>Parameter
 * <tr>
 * <td><code>Find</code>
 * <td>/e/&lt;Entity_Class&gt;/find
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
 * <td>/e/&lt;Entity_Class&gt;/findOne
 * <td>GET
 * <td>The same as "Filter" parameter of "Find" operation, but without "Page" parameter
 * <tr>
 * <td><code>Count</code>
 * <td>/e/&lt;Entity_Class&gt;/count
 * <td>GET
 * <td>The same as "Filter" parameter of "Find" operation, but without "Page" parameter
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Get</code>
 * <td>/e/&lt;Entity_Class&gt;/get/&lt;id&gt;
 * <td>GET
 * <td>
 * <tr>
 * <td><code>Delete</code>
 * <td>/e/&lt;Entity_Class&gt;/delete/&lt;id&gt;
 * <td>GET or POST
 * <td>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Post</code>
 * <td>/e/&lt;Entity_Class&gt;/post
 * <td>POST
 * <td>
 * <tr>
 * <td><code>Put</code>
 * <td>/e/&lt;Entity_Class&gt;/put/&lt;id&gt;
 * <td>POST
 * <td>
 * </table>
 * </blockquote>
 * 
 * @author Jalen Zhong
 */
public abstract class EntityBaseController extends BaseController {
	public static final String ENTITY_URL_PREFIX = "/e";
	public static final String ENTITY_URL_FIND = "find";
	public static final String ENTITY_URL_FIND_ONE = "findOne";
	public static final String ENTITY_URL_COUNT = "count";
	public static final String ENTITY_URL_GET = "get";
	public static final String ENTITY_URL_DELETE = "delete";
	public static final String ENTITY_URL_POST = "post";
	public static final String ENTITY_URL_PUT = "put";

	@Autowired
	protected EntityEnv env;

	@Autowired
	protected EntityTransactionalService entityService;

	@Autowired
	protected EntityFieldSecurerService entityFieldSecurerService;

	protected <T, ID extends Serializable> EntityPair<T, ID> getPair(String ec) {
		return env.getPair(ec);
	}

	@SuppressWarnings("unchecked")
	protected <T, ID extends Serializable> ID convertId(EntityPair<T, ID> pair, String id) {
		return (ID) convertId(id, pair.getIdField().getType());
	}
}
