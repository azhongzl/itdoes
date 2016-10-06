package com.itdoes.common.business.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Permissions;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.Reflections;
import com.itdoes.common.core.web.MultipartFiles;

/**
 * @author Jalen Zhong
 */
@Service
public class EntityFieldSecurerService extends BaseService {
	@Autowired
	private EntityTransactionalService entityService;

	public <T, ID extends Serializable> Page<T> secureFind(EntityPair<T, ID> pair, Specification<T> specification,
			PageRequest pageRequest) {
		final Page<T> page = entityService.find(pair, specification, pageRequest);
		handleGetSecureFields(pair, page.getContent());
		return page;
	}

	public <T, ID extends Serializable> T secureFindOne(EntityPair<T, ID> pair, Specification<T> specification) {
		final T entity = entityService.findOne(pair, specification);
		handleGetSecureFields(pair, entity);
		return entity;
	}

	public <T, ID extends Serializable> T secureGet(EntityPair<T, ID> pair, ID id) {
		final T entity = entityService.get(pair, id);
		handleGetSecureFields(pair, entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> ID securePost(EntityPair<T, ID> pair, T entity, String realRootPath,
			List<MultipartFile> files) {
		if (pair.getUploadField() != null
				&& isPermitted(Permissions.getEntityOneEntityOneFieldWritePermission(
						pair.getEntityClass().getSimpleName(), pair.getUploadField().getName()))
				&& !Collections3.isEmpty(files)) {
			final StringBuilder sb = new StringBuilder();
			for (MultipartFile file : files) {
				if (sb.length() != 0) {
					sb.append(',');
				}
				sb.append(file.getOriginalFilename());
			}
			Reflections.setFieldValue(entity, pair.getUploadField().getName(), sb.toString());
		}

		handlePostSecureFields(pair, entity);
		entity = entityService.save(pair, entity);
		final ID id = (ID) Reflections.getFieldValue(entity, pair.getIdField().getName());

		if (pair.getUploadField() != null
				&& isPermitted(Permissions.getEntityOneEntityOneFieldWritePermission(
						pair.getEntityClass().getSimpleName(), pair.getUploadField().getName()))
				&& !Collections3.isEmpty(files)) {
			for (MultipartFile file : files) {
				MultipartFiles.save(realRootPath + "/upload/" + pair.getEntityClass().getSimpleName(),
						id + "_" + file.getOriginalFilename(), file);
			}
		}

		return id;
	}

	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> void securePut(EntityPair<T, ID> pair, T entity, T oldEntity,
			String realRootPath, List<MultipartFile> files) {
		if (pair.getUploadField() != null
				&& isPermitted(Permissions.getEntityOneEntityOneFieldWritePermission(
						pair.getEntityClass().getSimpleName(), pair.getUploadField().getName()))
				&& !Collections3.isEmpty(files)) {
			final ID id = (ID) Reflections.getFieldValue(oldEntity, pair.getIdField().getName());
			final StringBuilder sb = new StringBuilder();
			final String attachments = (String) Reflections.getFieldValue(oldEntity, pair.getUploadField().getName());
			if (StringUtils.isNotBlank(attachments)) {
				sb.append(attachments);
			}
			for (MultipartFile file : files) {
				if (sb.length() != 0) {
					sb.append(',');
				}
				sb.append(file.getOriginalFilename());
				MultipartFiles.save(realRootPath + "/upload/" + pair.getEntityClass().getSimpleName(),
						id + "_" + file.getOriginalFilename(), file);
			}
			Reflections.setFieldValue(entity, pair.getUploadField().getName(), sb.toString());
		}

		handlePutSecureFields(pair, entity, oldEntity);
		entityService.save(pair, entity);
	}

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

	private static <T, ID extends Serializable> void handleGetSecureFields(EntityPair<T, ID> pair, List<T> entityList) {
		if (!pair.hasSecureFields()) {
			return;
		}

		for (T entity : entityList) {
			handleGetSecureFields(pair, entity);
		}
	}

	private static <T, ID extends Serializable> void handleGetSecureFields(EntityPair<T, ID> pair, T entity) {
		handleSecureFields(pair, SecureFieldHandler.GET, entity, null);
	}

	private static <T, ID extends Serializable> void handlePostSecureFields(EntityPair<T, ID> pair, T entity) {
		handleSecureFields(pair, SecureFieldHandler.POST, entity, null);
	}

	private static <T, ID extends Serializable> void handlePutSecureFields(EntityPair<T, ID> pair, T entity,
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

	public static boolean isPermitted(String permission) {
		final Subject subject = SecurityUtils.getSubject();
		return subject.isPermitted(permission);
	}
}
