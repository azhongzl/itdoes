package com.itdoes.common.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.itdoes.common.core.util.Reflections;

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
 * <td>itdoes:facade:&lt;entity&gt;:entity[:*]
 * <td>All facade commands for a specific entity <br />
 * (not including field authorization)
 * <td>
 * <ul>
 * <li>itdoes:facade:User:entity
 * <li>itdoes:facade:User:entity:*
 * </ul>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Facade</code>
 * <td>itdoes:facade:&lt;entity&gt;:entity:&lt;command&gt;
 * <td>Specific facade operation for a specific entity <br/>
 * (not including field authorization)
 * <td>
 * <ul>
 * <li>itdoes:facade:User:entity:find
 * <li>itdoes:facade:User:entity:findOne
 * <li>itdoes:facade:User:entity:count
 * <li>itdoes:facade:User:entity:get
 * <li>itdoes:facade:User:entity:delete
 * <li>itdoes:facade:User:entity:post
 * <li>itdoes:facade:User:entity:put
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
	private static final String PERM_ENTITY = "entity";
	private static final String PERM_FIELD = "field";
	private static final String PERM_READ = "read";
	private static final String PERM_WRITE = "write";
	private static final String PERM_ANY = "*";

	public static String getAllPermission() {
		return PERM_ROOT;
	}

	public static String getSearchAllPermission() {
		return getSearchPermission(PERM_ANY);
	}

	public static String getSearchPermission(String command) {
		return PERM_ROOT + PERM_SEPARATOR + PERM_SEARCH + PERM_SEPARATOR + command;
	}

	public static String getUploadAllPermission() {
		return getUploadPermission(PERM_ANY);
	}

	public static String getUploadPermission(String resource) {
		return PERM_ROOT + PERM_SEPARATOR + PERM_UPLOAD + PERM_SEPARATOR + resource;
	}

	public static String getFacadeAllPermission() {
		return PERM_ROOT + PERM_SEPARATOR + PERM_FACADE;
	}

	public static String getFacadeEntityAllPermission(String entityName) {
		return getFacadeEntityPermission(entityName, PERM_ANY);
	}

	public static String getFacadeEntityPermission(String entityName, String command) {
		return PERM_ROOT + PERM_SEPARATOR + PERM_FACADE + PERM_SEPARATOR + entityName + PERM_SEPARATOR + PERM_ENTITY
				+ PERM_SEPARATOR + command;
	}

	public static String getFacadeFieldAllPermission(String entityName, String fieldName) {
		return getFacadeFieldPermission(entityName, fieldName, PERM_ANY);
	}

	public static String getFacadeFieldReadPermission(String entityName, String fieldName) {
		return getFacadeFieldPermission(entityName, fieldName, PERM_READ);
	}

	public static String getFacadeFieldWritePermission(String entityName, String fieldName) {
		return getFacadeFieldPermission(entityName, fieldName, PERM_WRITE);
	}

	private static String getFacadeFieldPermission(String entityName, String fieldName, String mode) {
		return PERM_ROOT + PERM_SEPARATOR + PERM_FACADE + PERM_SEPARATOR + entityName + PERM_SEPARATOR + PERM_FIELD
				+ PERM_SEPARATOR + fieldName + PERM_SEPARATOR + mode;
	}

	private static interface SecureFieldHandler {
		SecureFieldHandler GET = new SecureFieldHandler() {
			@Override
			public <T> void handle(Subject subject, String entityName, String secureFieldName, T entity, T oldEntity) {
				if (!subject.isPermitted(Permissions.getFacadeFieldReadPermission(entityName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, null);
				}
			}
		};
		SecureFieldHandler POST = new SecureFieldHandler() {
			@Override
			public <T> void handle(Subject subject, String entityName, String secureFieldName, T entity, T oldEntity) {
				if (!subject.isPermitted(Permissions.getFacadeFieldWritePermission(entityName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, null);
				}
			}
		};
		SecureFieldHandler PUT = new SecureFieldHandler() {
			@Override
			public <T> void handle(Subject subject, String entityName, String secureFieldName, T entity, T oldEntity) {
				if (!subject.isPermitted(Permissions.getFacadeFieldWritePermission(entityName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, Reflections.invokeGet(oldEntity, secureFieldName));
				}
			}
		};

		<T> void handle(Subject subject, String entityName, String secureFieldName, T entity, T oldEntity);
	}

	public static <T, ID extends Serializable> void handleGetSecureFields(EntityPair<T, ID> pair, List<T> entityList) {
		if (!pair.hasSecureFields()) {
			return;
		}

		for (T entity : entityList) {
			handleGetSecureFields(pair, entity);
		}
	}

	public static <T, ID extends Serializable> void handleGetSecureFields(EntityPair<T, ID> pair, T entity) {
		handleSecureFields(pair, SecureFieldHandler.GET, entity, null);
	}

	public static <T, ID extends Serializable> void handlePostSecureFields(EntityPair<T, ID> pair, T entity) {
		handleSecureFields(pair, SecureFieldHandler.POST, entity, null);
	}

	public static <T, ID extends Serializable> void handlePutSecureFields(EntityPair<T, ID> pair, T entity,
			T oldEntity) {
		handleSecureFields(pair, SecureFieldHandler.PUT, entity, oldEntity);
	}

	private static <T, ID extends Serializable> void handleSecureFields(EntityPair<T, ID> pair,
			SecureFieldHandler handler, T entity, T oldEntity) {
		if (!pair.hasSecureFields()) {
			return;
		}

		final Subject subject = SecurityUtils.getSubject();

		final String entityName = pair.getEntityClass().getSimpleName();
		for (Field secureField : pair.getSecureFields()) {
			final String secureFieldName = secureField.getName();
			handler.handle(subject, entityName, secureFieldName, entity, oldEntity);
		}
	}

	private Permissions() {
	}
}
