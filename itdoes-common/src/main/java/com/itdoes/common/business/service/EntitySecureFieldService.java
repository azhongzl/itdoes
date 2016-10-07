package com.itdoes.common.business.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Permissions;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
@Service
public class EntitySecureFieldService extends BaseService {
	private static interface SecureFieldHandler {
		SecureFieldHandler GET = new SecureFieldHandler() {
			@Override
			public <T> void handle(Subject subject, String entityName, String secureFieldName, T entity, T oldEntity) {
				if (!subject.isPermitted(
						Permissions.getEntityOneEntityOneFieldReadPermission(entityName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, null);
				}
			}
		};
		SecureFieldHandler POST = new SecureFieldHandler() {
			@Override
			public <T> void handle(Subject subject, String entityName, String secureFieldName, T entity, T oldEntity) {
				if (!subject.isPermitted(
						Permissions.getEntityOneEntityOneFieldWritePermission(entityName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, null);
				}
			}
		};
		SecureFieldHandler PUT = new SecureFieldHandler() {
			@Override
			public <T> void handle(Subject subject, String entityName, String secureFieldName, T entity, T oldEntity) {
				if (!subject.isPermitted(
						Permissions.getEntityOneEntityOneFieldWritePermission(entityName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, Reflections.invokeGet(oldEntity, secureFieldName));
				}
			}
		};

		<T> void handle(Subject subject, String entityName, String secureFieldName, T entity, T oldEntity);
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

	public <T, ID extends Serializable> void handleGetSecureFields(EntityPair<T, ID> pair, List<T> entityList) {
		if (!pair.hasSecureFields()) {
			return;
		}

		for (T entity : entityList) {
			handleGetSecureFields(pair, entity);
		}
	}

	public <T, ID extends Serializable> void handleGetSecureFields(EntityPair<T, ID> pair, T entity) {
		handleSecureFields(pair, SecureFieldHandler.GET, entity, null);
	}

	public <T, ID extends Serializable> void handlePostSecureFields(EntityPair<T, ID> pair, T entity) {
		handleSecureFields(pair, SecureFieldHandler.POST, entity, null);
	}

	public <T, ID extends Serializable> void handlePutSecureFields(EntityPair<T, ID> pair, T entity, T oldEntity) {
		handleSecureFields(pair, SecureFieldHandler.PUT, entity, oldEntity);
	}
}
