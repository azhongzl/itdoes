package com.itdoes.common.business;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.google.common.collect.Sets;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
public class Permissions {
	public static final String UPLOAD_PERMISSON_PREFIX = "upload";

	private static final char ENTITY_FIELD_SEPARATOR = '.';
	private static final char PERM_SEPARATOR = ':';
	private static final String PERM_READ = "read";
	private static final String PERM_WRITE = "write";
	private static final String PERM_ANY = "*";

	public static Set<String> getAllPermissions(Env env) {
		final Set<String> all = Sets.newHashSet();

		// Upload permission
		all.add(getUploadAllPermission());

		for (Entry<String, EntityPair<?, ? extends Serializable>> entry : env.getEntityPairMap().entrySet()) {
			final String entityName = entry.getKey();

			// Add entity permission
			all.add(getEntityAllPermission(entityName));

			// Add field permission
			final EntityPair<?, ? extends Serializable> pair = entry.getValue();
			if (pair.hasSecureFields()) {
				for (Field field : pair.getSecureFields()) {
					all.add(getFieldAllPermission(entityName, field.getName()));
				}
			}
		}

		return all;
	}

	public static String getUploadAllPermission() {
		return UPLOAD_PERMISSON_PREFIX;
	}

	public static String getEntityAllPermission(String entityName) {
		return entityName;
	}

	public static String getEntityPermission(String entityName, String command) {
		return entityName + PERM_SEPARATOR + command;
	}

	public static String getFieldAllPermission(String entityName, String fieldName) {
		return getFieldPermission(entityName, fieldName, PERM_ANY);
	}

	public static String getFieldReadPermission(String entityName, String fieldName) {
		return getFieldPermission(entityName, fieldName, PERM_READ);
	}

	public static String getFieldWritePermission(String entityName, String fieldName) {
		return getFieldPermission(entityName, fieldName, PERM_WRITE);
	}

	private static String getFieldPermission(String entityName, String fieldName, String mode) {
		return entityName + ENTITY_FIELD_SEPARATOR + fieldName + PERM_SEPARATOR + mode;
	}

	private static interface SecureFieldHandler {
		SecureFieldHandler GET = new SecureFieldHandler() {
			@Override
			public <T> void handle(Subject subject, String entityName, String secureFieldName, T entity, T oldEntity) {
				if (!subject.isPermitted(Permissions.getFieldReadPermission(entityName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, null);
				}
			}
		};
		SecureFieldHandler POST = new SecureFieldHandler() {
			@Override
			public <T> void handle(Subject subject, String entityName, String secureFieldName, T entity, T oldEntity) {
				if (!subject.isPermitted(Permissions.getFieldWritePermission(entityName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, null);
				}
			}
		};
		SecureFieldHandler PUT = new SecureFieldHandler() {
			@Override
			public <T> void handle(Subject subject, String entityName, String secureFieldName, T entity, T oldEntity) {
				if (!subject.isPermitted(Permissions.getFieldWritePermission(entityName, secureFieldName))) {
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
