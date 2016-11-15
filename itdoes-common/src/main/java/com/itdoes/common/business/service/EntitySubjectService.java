package com.itdoes.common.business.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.business.EntityPair;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
@Service
public class EntitySubjectService extends BaseService {
	@Autowired
	private EntityDbService dbService;
	@Autowired
	private EntityPermFieldService permFieldService;
	@Autowired
	private EntityUploadService uploadService;

	public <T, ID extends Serializable> Page<T> find(EntityPair<T, ID> pair, Specification<T> specification,
			PageRequest pageRequest) {
		final Page<T> page = dbService.find(pair, specification, pageRequest);
		permFieldService.handleGetPermFields(pair, page.getContent());
		return page;
	}

	public <T, ID extends Serializable> List<T> findAll(EntityPair<T, ID> pair, Specification<T> specification,
			Sort sort) {
		final List<T> list = dbService.findAll(pair, specification, sort);
		permFieldService.handleGetPermFields(pair, list);
		return list;
	}

	public <T, ID extends Serializable> T findOne(EntityPair<T, ID> pair, Specification<T> specification) {
		final T entity = dbService.findOne(pair, specification);
		permFieldService.handleGetPermFields(pair, entity);
		return entity;
	}

	public <T, ID extends Serializable> T get(EntityPair<T, ID> pair, ID id) {
		final T entity = dbService.get(pair, id);
		permFieldService.handleGetPermFields(pair, entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> ID post(EntityPair<T, ID> pair, T entity) {
		permFieldService.handlePostPermFields(pair, entity);
		entity = dbService.save(pair, entity);
		final ID id = (ID) Reflections.getFieldValue(entity, pair.getIdField().getName());
		return id;
	}

	public <T, ID extends Serializable> void put(EntityPair<T, ID> pair, T entity, T oldEntity) {
		permFieldService.handlePutPermFields(pair, entity, oldEntity);
		dbService.save(pair, entity);
	}

	public <T, ID extends Serializable> ID postUpload(EntityPair<T, ID> pair, T entity, String realRootPath,
			List<MultipartFile> uploadFileList) {
		final String tempUploadDir = uploadService.postUploadPre(pair, entity, realRootPath, uploadFileList);
		final ID id = post(pair, entity);
		uploadService.postUploadPost(pair, entity, realRootPath, uploadFileList, id, tempUploadDir);
		return id;
	}

	public <T, ID extends Serializable> void putUpload(EntityPair<T, ID> pair, T entity, T oldEntity,
			String realRootPath, List<MultipartFile> uploadFileList, boolean uploadDeleteOrphanFiles) {
		uploadService.putUpload(pair, entity, realRootPath, uploadFileList, uploadDeleteOrphanFiles);
		put(pair, entity, oldEntity);
	}

	public <T, ID extends Serializable> long count(EntityPair<T, ID> pair, Specification<T> specification) {
		return dbService.count(pair, specification);
	}

	public <T, ID extends Serializable> void delete(EntityPair<T, ID> pair, ID id, String realRootPath,
			boolean uploadDeleteOrphanFiles) {
		dbService.delete(pair, id);
		uploadService.deleteUpload(pair, id, realRootPath, uploadDeleteOrphanFiles);
	}
}
