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
 * <td><code>Entity</code>
 * <td>itdoes:entity[:*][:*][:*][:*]
 * <td>All entity commands for all entities and fields
 * <td>
 * <ul>
 * <li>itdoes:entity
 * <li>itdoes:entity:*
 * <li>itdoes:entity:*:*
 * <li>itdoes:entity:*:*:*
 * <li>itdoes:entity:*:*:*:*
 * </ul>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Entity</code>
 * <td>itdoes:entity:&lt;entity&gt;[:*][:*][:*]
 * <td>All entity commands for a specific entity and all its fields
 * <td>
 * <ul>
 * <li>itdoes:entity:User
 * <li>itdoes:entity:User:*
 * <li>itdoes:entity:User:*:*
 * <li>itdoes:entity:User:*:*:*
 * </ul>
 * <tr>
 * <td><code>Entity</code>
 * <td>itdoes:entity:&lt;entity&gt;:field:[:*][:*]
 * <td>All entity commands for all fields of a specific entity <br />
 * (not including entity authorization)
 * <td>
 * <ul>
 * <li>itdoes:entity:User:field
 * <li>itdoes:entity:User:field:*
 * <li>itdoes:entity:User:field:*:*
 * </ul>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Entity</code>
 * <td>itdoes:entity:&lt;entity&gt;:field:&lt;field&gt;[:*]
 * <td>All entity commands for a specific entity field <br />
 * (not including entity authorization)
 * <td>
 * <ul>
 * <li>itdoes:entity:User:field:username
 * <li>itdoes:entity:User:field:username:*
 * </ul>
 * <tr>
 * <td><code>Entity</code>
 * <td>itdoes:entity:&lt;entity&gt;:field:&lt;field&gt;:&lt;command&gt;
 * <td>Specific entity operation for a specific entity field <br/>
 * (Commands now including: get, write)
 * <td>
 * <ul>
 * <li>itdoes:entity:User:field:username:read
 * <li>itdoes:entity:User:field:username:write
 * </ul>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>&lt;Resource&gt;</code>
 * <td>itdoes:&lt;resource&gt;[:*]
 * <td>All resource commands
 * <td>
 * <ul>
 * <li>itdoes:resource1
 * <li>itdoes:resource1.*
 * </ul>
 * <tr>
 * <td><code>&lt;Resource&gt;</code>
 * <td>itdoes:&lt;resource&gt;:&lt;command&gt;
 * <td>Specific resource command
 * <td>
 * <ul>
 * <li>itdoes:resource1:command1
 * <li>itdoes:resource2:command2
 * </ul>
 * </table>
 * </blockquote>
 * 
 * @author Jalen Zhong
 */
public class Perms {
	private static final char PERM_SEPARATOR = ':';
	private static final String PERM_ROOT = "itdoes";
	private static final String PERM_ENTITY = "entity";
	private static final String PERM_ENTITY_FIELD = "field";
	private static final String PERM_READ = "read";
	private static final String PERM_WRITE = "write";

	public static String getFullPerm(String partPerm) {
		return PERM_ROOT + PERM_SEPARATOR + partPerm;
	}

	public static String getAllPerm() {
		return PERM_ROOT;
	}

	public static String getResourceAllPerm(String resource) {
		return PERM_ROOT + PERM_SEPARATOR + resource;
	}

	public static String getResourcePerm(String resource, String command) {
		return getResourceAllPerm(resource) + PERM_SEPARATOR + command;
	}

	public static String getEntityAllFieldsAllPerm(String entityName) {
		return PERM_ROOT + PERM_SEPARATOR + PERM_ENTITY + PERM_SEPARATOR + entityName + PERM_SEPARATOR
				+ PERM_ENTITY_FIELD;
	}

	public static String getEntityOneFieldAllPerm(String entityName, String fieldName) {
		return getEntityAllFieldsAllPerm(entityName) + PERM_SEPARATOR + fieldName;
	}

	public static String getEntityOneFieldReadPerm(String entityName, String fieldName) {
		return getEntityOneFieldPerm(entityName, fieldName, PERM_READ);
	}

	public static String getEntityOneFieldWritePerm(String entityName, String fieldName) {
		return getEntityOneFieldPerm(entityName, fieldName, PERM_WRITE);
	}

	private static String getEntityOneFieldPerm(String entityName, String fieldName, String mode) {
		return getEntityOneFieldAllPerm(entityName, fieldName) + PERM_SEPARATOR + mode;
	}

	private Perms() {
	}
}
