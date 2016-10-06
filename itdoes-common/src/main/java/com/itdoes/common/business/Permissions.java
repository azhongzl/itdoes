package com.itdoes.common.business;

/**
 * <blockquote>
 * <table border=0 cellspacing=3 cellpadding=0 summary="Permission Readme">
 * <tr style="background-color: rgb(204, 204, 255);">
 * <th align=left>Operation
 * <th align=left>Format
 * <th align=left>Meaning
 * <th align=left>Example
 * <tr>
 * <td><code>Facade</code>
 * <td>itdoes:facade[:*][:*][:*][:*]
 * <td>All facade commands for all entities and fields
 * <td>
 * <ul>
 * <li>itdoes:facade
 * <li>itdoes:facade:*
 * <li>itdoes:facade:*:*
 * <li>itdoes:facade:*:*:*
 * <li>itdoes:facade:*:*:*:*
 * </ul>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Facade</code>
 * <td>itdoes:facade:&lt;entity&gt;[:*][:*][:*]
 * <td>All facade commands for a specific entity and all its fields
 * <td>
 * <ul>
 * <li>itdoes:facade:User
 * <li>itdoes:facade:User:*
 * <li>itdoes:facade:User:*:*
 * <li>itdoes:facade:User:*:*:*
 * </ul>
 * <tr>
 * <td><code>Facade</code>
 * <td>itdoes:facade:&lt;entity&gt;:class[:*]
 * <td>All facade commands for a specific entity <br />
 * (not including field authorization)
 * <td>
 * <ul>
 * <li>itdoes:facade:User:class
 * <li>itdoes:facade:User:class:*
 * </ul>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Facade</code>
 * <td>itdoes:facade:&lt;entity&gt;:class:&lt;command&gt;
 * <td>Specific facade operation for a specific entity <br/>
 * (not including field authorization)
 * <td>
 * <ul>
 * <li>itdoes:facade:User:class:find
 * <li>itdoes:facade:User:class:findOne
 * <li>itdoes:facade:User:class:count
 * <li>itdoes:facade:User:class:get
 * <li>itdoes:facade:User:class:delete
 * <li>itdoes:facade:User:class:post
 * <li>itdoes:facade:User:class:put
 * </ul>
 * <tr>
 * <td><code>Facade</code>
 * <td>itdoes:facade:&lt;entity&gt;:field:[:*][:*]
 * <td>All facade commands for all fields of a specific entity <br />
 * (not including entity authorization)
 * <td>
 * <ul>
 * <li>itdoes:facade:User:field
 * <li>itdoes:facade:User:field:*
 * <li>itdoes:facade:User:field:*:*
 * </ul>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Facade</code>
 * <td>itdoes:facade:&lt;entity&gt;:field:&lt;field&gt;[:*]
 * <td>All facade commands for a specific entity field <br />
 * (not including entity authorization)
 * <td>
 * <ul>
 * <li>itdoes:facade:User:field:username
 * <li>itdoes:facade:User:field:username:*
 * </ul>
 * <tr>
 * <td><code>Facade</code>
 * <td>itdoes:facade:&lt;entity&gt;:field:&lt;field&gt;:&lt;command&gt;
 * <td>Specific facade operation for a specific entity field <br/>
 * (Commands now including: get, write)
 * <td>
 * <ul>
 * <li>itdoes:facade:User:field:username:read
 * <li>itdoes:facade:User:field:username:write
 * </ul>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Upload</code>
 * <td>itdoes:upload[:*]
 * <td>All upload resources
 * <td>
 * <ul>
 * <li>itdoes:upload
 * <li>itdoes:upload.*
 * </ul>
 * <tr>
 * <td><code>Upload</code>
 * <td>itdoes:upload:&lt;resource&gt;
 * <td>Specific upload resource
 * <td>
 * <ul>
 * <li>itdoes:upload:resource1
 * <li>itdoes:upload:resource2
 * </ul>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Search</code>
 * <td>itdoes:search[:*]
 * <td>All search commands
 * <td>
 * <ul>
 * <li>itdoes:search
 * <li>itdoes:search.*
 * </ul>
 * <tr>
 * <td><code>Search</code>
 * <td>itdoes:search:&lt;command&gt;
 * <td>Specific search command
 * <td>
 * <ul>
 * <li>itdoes:search:command1
 * <li>itdoes:search:command2
 * </ul>
 * </table>
 * </blockquote>
 * 
 * @author Jalen Zhong
 */
public class Permissions {
	public static final String PERM_SEARCH = "search";
	public static final String PERM_UPLOAD = "upload";
	public static final String PERM_FACADE = "facade";

	private static final char PERM_SEPARATOR = ':';
	private static final String PERM_ROOT = "itdoes";
	private static final String PERM_ENTITY_CLASS = "class";
	private static final String PERM_ENTITY_FIELD = "field";
	private static final String PERM_READ = "read";
	private static final String PERM_WRITE = "write";

	public static String getAllPermission() {
		return PERM_ROOT;
	}

	public static String getSearchAllPermission() {
		return getAllPermission() + PERM_SEPARATOR + PERM_SEARCH;
	}

	public static String getSearchPermission(String command) {
		return getSearchAllPermission() + PERM_SEPARATOR + command;
	}

	public static String getUploadAllPermission() {
		return getAllPermission() + PERM_SEPARATOR + PERM_UPLOAD;
	}

	public static String getUploadPermission(String resource) {
		return getUploadAllPermission() + PERM_SEPARATOR + resource;
	}

	public static String getFacadeAllPermission() {
		return getAllPermission() + PERM_SEPARATOR + PERM_FACADE;
	}

	public static String getFacadeOneEntityAllPermission(String entityName) {
		return getFacadeAllPermission() + PERM_SEPARATOR + entityName;
	}

	public static String getFacadeOneEntityClassAllPermission(String entityName) {
		return getFacadeOneEntityAllPermission(entityName) + PERM_SEPARATOR + PERM_ENTITY_CLASS;
	}

	public static String getFacadeOneEntityClassPermission(String entityName, String command) {
		return getFacadeOneEntityClassAllPermission(entityName) + PERM_SEPARATOR + command;
	}

	public static String getFacadeOneEntityAllFieldsAllPermission(String entityName) {
		return getFacadeOneEntityAllPermission(entityName) + PERM_SEPARATOR + PERM_ENTITY_FIELD;
	}

	public static String getFacadeOneEntityOneFieldAllPermission(String entityName, String fieldName) {
		return getFacadeOneEntityAllFieldsAllPermission(entityName) + PERM_SEPARATOR + fieldName;
	}

	public static String getFacadeOneEntityOneFieldReadPermission(String entityName, String fieldName) {
		return getFacadeOneEntityOneFieldPermission(entityName, fieldName, PERM_READ);
	}

	public static String getFacadeOneEntityOneFieldWritePermission(String entityName, String fieldName) {
		return getFacadeOneEntityOneFieldPermission(entityName, fieldName, PERM_WRITE);
	}

	private static String getFacadeOneEntityOneFieldPermission(String entityName, String fieldName, String mode) {
		return getFacadeOneEntityOneFieldAllPermission(entityName, fieldName) + PERM_SEPARATOR + mode;
	}

	private Permissions() {
	}
}
