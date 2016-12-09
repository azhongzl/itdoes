package com.itdoes.common.business.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.itdoes.common.business.EntityPair;

/**
 * @author Jalen Zhong
 */
public class EntityUploadServiceBuilder<T, ID extends Serializable> {
	private final EntityPair<T, ID> pair;

	public EntityUploadServiceBuilder(EntityPair<T, ID> pair) {
		this.pair = pair;
	}

	public T exePost(T entity, List<MultipartFile> uploadFileList) {
		return pair.getUploadService().post(pair, entity, uploadFileList);
	}

	public T exePut(T entity, T oldEntity, List<MultipartFile> uploadFileList) {
		return pair.getUploadService().put(pair, entity, oldEntity, uploadFileList);
	}

	public void exeDelete(ID id) {
		pair.getUploadService().delete(pair, id);
	}

	public void exeDeleteIterable(Iterable<T> entities) {
		pair.getUploadService().deleteIterable(pair, entities);
	}
}
