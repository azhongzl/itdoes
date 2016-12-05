package com.itdoes.common.business.service.entity.external;

import java.io.Serializable;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Perms;
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.business.service.entity.internal.EntityInternalService;

/**
 * @author Jalen Zhong
 */
public class EntityExternalService extends BaseService {
	private final EntityInternalService internalService;
	private final EntityExternalPermFieldService permFieldService;

	public EntityExternalService(EntityInternalService internalService,
			EntityExternalPermFieldService permFieldService) {
		this.internalService = internalService;
		this.permFieldService = permFieldService;
	}

	public <T, ID extends Serializable> Page<T> find(EntityPair<T, ID> pair, Specification<T> specification,
			PageRequest pageRequest) {
		final Page<T> page = internalService.find(pair, specification, pageRequest);
		permFieldService.handleGetPermFields(pair, page.getContent());
		return page;
	}

	public <T, ID extends Serializable> List<T> findAll(EntityPair<T, ID> pair, Specification<T> specification,
			Sort sort) {
		final List<T> list = internalService.findAll(pair, specification, sort);
		permFieldService.handleGetPermFields(pair, list);
		return list;
	}

	public <T, ID extends Serializable> T findOne(EntityPair<T, ID> pair, Specification<T> specification) {
		final T entity = internalService.findOne(pair, specification);
		permFieldService.handleGetPermFields(pair, entity);
		return entity;
	}

	public <T, ID extends Serializable> T get(EntityPair<T, ID> pair, ID id) {
		final T entity = internalService.get(pair, id);
		permFieldService.handleGetPermFields(pair, entity);
		return entity;
	}

	public <T, ID extends Serializable> ID post(EntityPair<T, ID> pair, T entity) {
		permFieldService.handlePostPermFields(pair, entity);
		return internalService.post(pair, entity);
	}

	public <T, ID extends Serializable> void put(EntityPair<T, ID> pair, T entity, T oldEntity) {
		permFieldService.handlePutPermFields(pair, entity, oldEntity);
		internalService.put(pair, entity, oldEntity);
	}

	public <T, ID extends Serializable> ID postUpload(EntityPair<T, ID> pair, T entity,
			List<MultipartFile> uploadFileList) {
		if (isUploadPermitted(pair)) {
			permFieldService.handlePostPermFields(pair, entity);
			return internalService.postUpload(pair, entity, uploadFileList);
		} else {
			return post(pair, entity);
		}
	}

	public <T, ID extends Serializable> void putUpload(EntityPair<T, ID> pair, T entity, T oldEntity,
			List<MultipartFile> uploadFileList) {
		if (isUploadPermitted(pair)) {
			permFieldService.handlePutPermFields(pair, entity, oldEntity);
			internalService.putUpload(pair, entity, oldEntity, uploadFileList);
		} else {
			put(pair, entity, oldEntity);
		}
	}

	public <T, ID extends Serializable> long count(EntityPair<T, ID> pair, Specification<T> specification) {
		return internalService.count(pair, specification);
	}

	public <T, ID extends Serializable> void delete(EntityPair<T, ID> pair, ID id) {
		internalService.delete(pair, id);
	}

	private static <T, ID extends Serializable> boolean isUploadPermitted(EntityPair<T, ID> pair) {
		if (pair.getUploadField() == null) {
			return false;
		}

		final Subject subject = SecurityUtils.getSubject();
		return subject.isPermitted(Perms.getEntityOneEntityOneFieldWritePerm(pair.getEntityClass().getSimpleName(),
				pair.getUploadField().getName()));
	}
}
