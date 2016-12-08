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
				if (!subject.isPermitted(Perms.getEntityOneFieldReadPerm(entityName, permFieldName))) {
					Reflections.invokeSet(entity, permFieldName, null);
				}
			}
		};
		PermFieldHandler POST = new PermFieldHandler() {
			@Override
			public <T> void handle(Subject subject, String entityName, String permFieldName, T entity, T oldEntity) {
				if (!subject.isPermitted(Perms.getEntityOneFieldWritePerm(entityName, permFieldName))) {
					Reflections.invokeSet(entity, permFieldName, null);
				}
			}
		};
		PermFieldHandler PUT = new PermFieldHandler() {
			@Override
			public <T> void handle(Subject subject, String entityName, String permFieldName, T entity, T oldEntity) {
				if (!subject.isPermitted(Perms.getEntityOneFieldWritePerm(entityName, permFieldName))) {
					Reflections.invokeSet(entity, permFieldName, Reflections.invokeGet(oldEntity, permFieldName));
				}
			}
		};

		<T> void handle(Subject subject, String entityName, String permFieldName, T entity, T oldEntity);
	}

	private static <T, ID extends Serializable> void handlePermFields(EntityPair<T, ID> pair, PermFieldHandler handler,
			T entity, T oldEntity, boolean read) {
		if (read) {
			if (!pair.hasReadPermField()) {
				return;
			}
		} else {
			if (!pair.hasWritePermField()) {
				return;
			}
		}

		final Subject subject = SecurityUtils.getSubject();

		final String entityName = pair.getEntityClass().getSimpleName();
		final List<Field> permFieldList = read ? pair.getReadPermFieldList() : pair.getWritePermFieldList();
		for (Field permField : permFieldList) {
			final String permFieldName = permField.getName();
			handler.handle(subject, entityName, permFieldName, entity, oldEntity);
		}
	}

	private static <T, ID extends Serializable> void handleReadPermFields(EntityPair<T, ID> pair,
			PermFieldHandler handler, T entity, T oldEntity) {
		handlePermFields(pair, handler, entity, oldEntity, true);
	}

	private static <T, ID extends Serializable> void handleWritePermFields(EntityPair<T, ID> pair,
			PermFieldHandler handler, T entity, T oldEntity) {
		handlePermFields(pair, handler, entity, oldEntity, false);
	}

	public <T, ID extends Serializable> void handleGetPermFields(EntityPair<T, ID> pair, List<T> entityList) {
		if (!pair.hasReadPermField()) {
			return;
		}

		for (T entity : entityList) {
			handleGetPermFields(pair, entity);
		}
	}

	public <T, ID extends Serializable> void handleGetPermFields(EntityPair<T, ID> pair, T entity) {
		handleReadPermFields(pair, PermFieldHandler.GET, entity, null);
	}

	public <T, ID extends Serializable> void handlePostPermFields(EntityPair<T, ID> pair, T entity) {
		handleWritePermFields(pair, PermFieldHandler.POST, entity, null);
	}

	public <T, ID extends Serializable> void handlePutPermFields(EntityPair<T, ID> pair, T entity, T oldEntity) {
		handleWritePermFields(pair, PermFieldHandler.PUT, entity, oldEntity);
	}
}
