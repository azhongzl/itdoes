package com.itdoes.common.business;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.itdoes.common.business.entity.BaseEntity;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
public class Permissions {
	private static final char ENTITY_FIELD_SEPARATOR = '.';
	private static final char PERM_SEPARATOR = ':';
	private static final String PERM_READ = "read";
	private static final String PERM_WRITE = "write";
	private static final String PERM_ANY = "*";

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
			public void handle(Subject subject, String entityName, String secureFieldName, BaseEntity entity,
					BaseEntity oldEntity) {
				if (!subject.isPermitted(Permissions.getFieldReadPermission(entityName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, null);
				}
			}
		};
		SecureFieldHandler POST = new SecureFieldHandler() {
			@Override
			public void handle(Subject subject, String entityName, String secureFieldName, BaseEntity entity,
					BaseEntity oldEntity) {
				if (!subject.isPermitted(Permissions.getFieldWritePermission(entityName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, null);
				}
			}
		};
		SecureFieldHandler PUT = new SecureFieldHandler() {
			@Override
			public void handle(Subject subject, String entityName, String secureFieldName, BaseEntity entity,
					BaseEntity oldEntity) {
				if (!subject.isPermitted(Permissions.getFieldWritePermission(entityName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, Reflections.invokeGet(oldEntity, secureFieldName));
				}
			}
		};

		void handle(Subject subject, String entityName, String secureFieldName, BaseEntity entity,
				BaseEntity oldEntity);
	}

	public static void handleGetSecureFields(EntityPair pair, List<? extends BaseEntity> entityList) {
		if (!pair.hasSecureFields()) {
			return;
		}

		for (BaseEntity entity : entityList) {
			handleGetSecureFields(pair, entity);
		}
	}

	public static void handleGetSecureFields(EntityPair pair, BaseEntity entity) {
		handleSecureFields(pair, SecureFieldHandler.GET, entity, null);
	}

	public static void handlePostSecureFields(EntityPair pair, BaseEntity entity) {
		handleSecureFields(pair, SecureFieldHandler.POST, entity, null);
	}

	public static void handlePutSecureFields(EntityPair pair, BaseEntity entity, BaseEntity oldEntity) {
		handleSecureFields(pair, SecureFieldHandler.PUT, entity, oldEntity);
	}

	private static void handleSecureFields(EntityPair pair, SecureFieldHandler handler, BaseEntity entity,
			BaseEntity oldEntity) {
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
