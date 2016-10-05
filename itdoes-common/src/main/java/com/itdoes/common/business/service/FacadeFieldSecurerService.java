package com.itdoes.common.business.service;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
public class FacadeFieldSecurerService extends BaseService {
	@Autowired
	private FacadeTransactionalService facadeService;

	public <T, ID extends Serializable> Page<T> secureFind(EntityPair<T, ID> pair, Specification<T> specification,
			PageRequest pageRequest) {
		final Page<T> page = facadeService.find(pair, specification, pageRequest);
		Permissions.handleGetSecureFields(pair, page.getContent());
		return page;
	}

	public <T, ID extends Serializable> T secureFindOne(EntityPair<T, ID> pair, Specification<T> specification) {
		final T entity = facadeService.findOne(pair, specification);
		Permissions.handleGetSecureFields(pair, entity);
		return entity;
	}

	public <T, ID extends Serializable> T secureGet(EntityPair<T, ID> pair, ID id) {
		final T entity = facadeService.get(pair, id);
		Permissions.handleGetSecureFields(pair, entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> ID securePost(EntityPair<T, ID> pair, T entity, String realRootPath,
			List<MultipartFile> files) {
		if (pair.getUploadField() != null
				&& Permissions.isPermitted(Permissions.getFacadeOneEntityOneFieldWritePermission(
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

		Permissions.handlePostSecureFields(pair, entity);
		entity = facadeService.save(pair, entity);
		final ID id = (ID) Reflections.getFieldValue(entity, pair.getIdField().getName());

		if (pair.getUploadField() != null
				&& Permissions.isPermitted(Permissions.getFacadeOneEntityOneFieldWritePermission(
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
				&& Permissions.isPermitted(Permissions.getFacadeOneEntityOneFieldWritePermission(
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

		Permissions.handlePutSecureFields(pair, entity, oldEntity);
		facadeService.save(pair, entity);
	}
}
