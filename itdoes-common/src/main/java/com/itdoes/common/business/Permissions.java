package com.itdoes.common.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.google.common.collect.Sets;
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
 * <td>facade[:*][:*]
 * <td>All facade commands for all entities and fields
 * <td>
 * <ul>
 * <li>facade
 * <li>facade:*
 * <li>facade:*:*
 * </ul>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Facade</code>
 * <td>facade:&lt;entity&gt;[:*]
 * <td>All facade commands for a specific entity <br />
 * (not including field authorization)
 * <td>
 * <ul>
 * <li>facade:User
 * <li>facade:User:*
 * </ul>
 * <tr>
 * <td><code>Facade</code>
 * <td>facade:&lt;entity&gt;:&lt;command&gt;
 * <td>Specific facade operation for a specific entity <br/>
 * (not including field authorization)
 * <td>
 * <ul>
 * <li>facade:User:find
 * <li>facade:User:findOne
 * <li>facade:User:count
 * <li>facade:User:get
 * <li>facade:User:delete
 * <li>facade:User:post
 * <li>facade:User:put
 * </ul>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Facade</code>
 * <td>facade:&lt;entity&gt;.&lt;field&gt;[:*]
 * <td>All facade commands for a specific entity field <br />
 * (not including entity authorization)
 * <td>
 * <ul>
 * <li>facade:User.username
 * <li>facade:User.username:*
 * </ul>
 * <tr>
 * <td><code>Facade</code>
 * <td>facade:&lt;entity&gt;.&lt;field&gt;:&lt;command&gt;
 * <td>Specific facade operation for a specific entity field <br/>
 * (Commands now including: get, write)
 * <td>
 * <ul>
 * <li>facade:User.username:read
 * <li>facade:User.username:write
 * </ul>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Upload</code>
 * <td>upload[:*]
 * <td>All upload resources
 * <td>
 * <ul>
 * <li>upload
 * <li>upload.*
 * </ul>
 * <tr>
 * <td><code>Upload</code>
 * <td>upload:&lt;resource&gt;
 * <td>Specific upload resource
 * <td>
 * <ul>
 * <li>upload:resource1
 * <li>upload:resource2
 * </ul>
 * <tr style="background-color: rgb(238, 238, 255);">
 * <td><code>Search</code>
 * <td>search[:*]
 * <td>All search commands
 * <td>
 * <ul>
 * <li>search
 * <li>search.*
 * </ul>
 * <tr>
 * <td><code>Search</code>
 * <td>search:&lt;command&gt;
 * <td>Specific search command
 * <td>
 * <ul>
 * <li>search:command1
 * <li>search:command2
 * </ul>
 * </table>
 * </blockquote>
 * 
 * @author Jalen Zhong
 */
public class Permissions {
	public static final String SEARCH_PERMISSION_PREFIX = "search";
	public static final String UPLOAD_PERMISSION_PREFIX = "upload";
	public static final String FACADE_PERMISSION_PREFIX = "facade";

	private static final char ENTITY_FIELD_SEPARATOR = '.';
	private static final char PERM_SEPARATOR = ':';
	private static final String PERM_READ = "read";
	private static final String PERM_WRITE = "write";
	private static final String PERM_ANY = "*";

	private static final String SEARCH_ALL_PERMISSION = getSearchPermission(PERM_ANY);
	private static final String UPLOAD_ALL_PERMISSION = getUploadPermission(PERM_ANY);
	private static final String FACADE_ALL_PERMISSION = getFacadeEntityAllPermission(PERM_ANY);
	private static final Set<String> ALL_PERMISSIONS = Sets.newHashSet(SEARCH_ALL_PERMISSION, UPLOAD_ALL_PERMISSION,
			FACADE_ALL_PERMISSION);

	public static Set<String> getAllPermissions() {
		return ALL_PERMISSIONS;
	}

	public static String getSearchAllPermission() {
		return SEARCH_ALL_PERMISSION;
	}

	public static String getSearchPermission(String command) {
		return SEARCH_PERMISSION_PREFIX + PERM_SEPARATOR + command;
	}

	public static String getUploadAllPermission() {
		return UPLOAD_ALL_PERMISSION;
	}

	public static String getUploadPermission(String resource) {
		return UPLOAD_PERMISSION_PREFIX + PERM_SEPARATOR + resource;
	}

	public static String getFacadeAllPermission() {
		return FACADE_ALL_PERMISSION;
	}

	public static String getFacadeEntityAllPermission(String entityName) {
		return getFacadeEntityPermission(entityName, PERM_ANY);
	}

	public static String getFacadeEntityPermission(String entityName, String command) {
		return FACADE_PERMISSION_PREFIX + PERM_SEPARATOR + entityName + PERM_SEPARATOR + command;
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
		return FACADE_PERMISSION_PREFIX + PERM_SEPARATOR + entityName + ENTITY_FIELD_SEPARATOR + fieldName
				+ PERM_SEPARATOR + mode;
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
