package com.itdoes.common.business.service.entity.internal;

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
import com.itdoes.common.business.service.BaseService;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
@Service
public class EntityInternalService extends BaseService {
	@Autowired
	private EntityInternalDbService dbService;
	@Autowired
	private EntityInternalUploadService uploadService;

	public <T, ID extends Serializable> Page<T> find(EntityPair<T, ID> pair, Specification<T> specification,
			PageRequest pageRequest) {
		return dbService.find(pair, specification, pageRequest);
	}

	public <T, ID extends Serializable> List<T> findAll(EntityPair<T, ID> pair, Specification<T> specification,
			Sort sort) {
		return dbService.findAll(pair, specification, sort);
	}

	public <T, ID extends Serializable> T findOne(EntityPair<T, ID> pair, Specification<T> specification) {
		return dbService.findOne(pair, specification);
	}

	public <T, ID extends Serializable> T get(EntityPair<T, ID> pair, ID id) {
		return dbService.get(pair, id);
	}

	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> ID post(EntityPair<T, ID> pair, T entity) {
		entity = dbService.post(pair, entity);
		return (ID) Reflections.getFieldValue(entity, pair.getIdField());
	}

	public <T, ID extends Serializable> void put(EntityPair<T, ID> pair, T entity, T oldEntity) {
		dbService.put(pair, entity, oldEntity);
	}

	public <T, ID extends Serializable> ID postUpload(EntityPair<T, ID> pair, T entity,
			List<MultipartFile> uploadFileList) {
		final String tempUploadDir = uploadService.postUploadPre(pair, entity, uploadFileList);
		final ID id = post(pair, entity);
		uploadService.postUploadPost(pair, entity, uploadFileList, id, tempUploadDir);
		return id;
	}

	public <T, ID extends Serializable> void putUpload(EntityPair<T, ID> pair, T entity, T oldEntity,
			List<MultipartFile> uploadFileList) {
		uploadService.putUpload(pair, entity, oldEntity, uploadFileList);
		put(pair, entity, oldEntity);
	}

	public <T, ID extends Serializable> Iterable<T> postIterable(EntityPair<T, ID> pair, Iterable<T> entities) {
		return dbService.postIterable(pair, entities);
	}

	public <T, ID extends Serializable> Iterable<T> putIterable(EntityPair<T, ID> pair, Iterable<T> entities,
			Iterable<T> oldEntities) {
		return dbService.putIterable(pair, entities, oldEntities);
	}

	public <T, ID extends Serializable> long count(EntityPair<T, ID> pair, Specification<T> specification) {
		return dbService.count(pair, specification);
	}

	public <T, ID extends Serializable> void delete(EntityPair<T, ID> pair, ID id) {
		dbService.delete(pair, id);
		uploadService.deleteUpload(pair, id);
	}

	@SuppressWarnings("unchecked")
	public <T, ID extends Serializable> void deleteIterable(EntityPair<T, ID> pair, Iterable<T> entities) {
		if (pair.getUploadField() != null) {
			for (T entity : entities) {
				delete(pair, (ID) Reflections.getFieldValue(entity, pair.getIdField()));
			}
		} else {
			dbService.deleteIterable(pair, entities);
		}
	}
}
