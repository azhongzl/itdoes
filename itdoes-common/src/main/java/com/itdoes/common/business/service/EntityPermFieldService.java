package com.itdoes.common.business.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Perms;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
@Service
public class EntityPermFieldService extends BaseService {
	private static interface PermFieldHandler {
		PermFieldHandler GET = new PermFieldHandler() {
			@Override
			public <T> void handle(Subject subject, String entityName, String permFieldName, T entity, T oldEntity) {
				if (!subject.isPermitted(Perms.getEntityOneEntityOneFieldReadPerm(entityName, permFieldName))) {
					Reflections.invokeSet(entity, permFieldName, null);
				}
			}
		};
		PermFieldHandler POST = new PermFieldHandler() {
			@Override
			public <T> void handle(Subject subject, String entityName, String permFieldName, T entity, T oldEntity) {
				if (!subject.isPermitted(Perms.getEntityOneEntityOneFieldWritePerm(entityName, permFieldName))) {
					Reflections.invokeSet(entity, permFieldName, null);
				}
			}
		};
		PermFieldHandler PUT = new PermFieldHandler() {
			@Override
			public <T> void handle(Subject subject, String entityName, String permFieldName, T entity, T oldEntity) {
				if (!subject.isPermitted(Perms.getEntityOneEntityOneFieldWritePerm(entityName, permFieldName))) {
					Reflections.invokeSet(entity, permFieldName, Reflections.invokeGet(oldEntity, permFieldName));
				}
			}
		};

		<T> void handle(Subject subject, String entityName, String permFieldName, T entity, T oldEntity);
	}

	private static <T, ID extends Serializable> void handlePermFields(EntityPair<T, ID> pair, PermFieldHandler handler,
			T entity, T oldEntity) {
		if (!pair.hasPermField()) {
			return;
		}

		final Subject subject = SecurityUtils.getSubject();

		final String entityName = pair.getEntityClass().getSimpleName();
		for (Field permField : pair.getPermFieldList()) {
			final String permFieldName = permField.getName();
			handler.handle(subject, entityName, permFieldName, entity, oldEntity);
		}
	}

	public <T, ID extends Serializable> void handleGetPermFields(EntityPair<T, ID> pair, List<T> entityList) {
		if (!pair.hasPermField()) {
			return;
		}

		for (T entity : entityList) {
			handleGetPermFields(pair, entity);
		}
	}

	public <T, ID extends Serializable> void handleGetPermFields(EntityPair<T, ID> pair, T entity) {
		handlePermFields(pair, PermFieldHandler.GET, entity, null);
	}

	public <T, ID extends Serializable> void handlePostPermFields(EntityPair<T, ID> pair, T entity) {
		handlePermFields(pair, PermFieldHandler.POST, entity, null);
	}

	public <T, ID extends Serializable> void handlePutPermFields(EntityPair<T, ID> pair, T entity, T oldEntity) {
		handlePermFields(pair, PermFieldHandler.PUT, entity, oldEntity);
	}
}
