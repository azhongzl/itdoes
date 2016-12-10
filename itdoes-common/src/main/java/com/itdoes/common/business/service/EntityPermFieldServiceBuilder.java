package com.itdoes.common.business.service;

import java.io.Serializable;
import java.util.List;

import com.itdoes.common.business.EntityPair;

/**
 * @author Jalen Zhong
 */
public class EntityPermFieldServiceBuilder<T, ID extends Serializable> {
	private final EntityPair<T, ID> pair;

	public EntityPermFieldServiceBuilder(EntityPair<T, ID> pair) {
		this.pair = pair;
	}

	public void exeReadPermFields(List<T> entityList) {
		pair.getPermFieldService().handleReadPermFields(pair, entityList);
	}

	public void exeReadPermFields(T entity) {
		pair.getPermFieldService().handleReadPermFields(pair, entity);
	}

	public void exePostPermFields(T entity) {
		pair.getPermFieldService().handlePostPermFields(pair, entity);
	}

	public void exePutPermFields(T entity, T oldEntity) {
		pair.getPermFieldService().handlePutPermFields(pair, entity, oldEntity);
	}
}
