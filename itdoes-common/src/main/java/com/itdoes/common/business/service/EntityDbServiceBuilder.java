package com.itdoes.common.business.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.Lists;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.core.jpa.FindFilter;
import com.itdoes.common.core.jpa.FindFilter.Operator;
import com.itdoes.common.core.jpa.Specifications;
import com.itdoes.common.core.spring.SpringDatas;

/**
 * @author Jalen Zhong
 */
public class EntityDbServiceBuilder<T, ID extends Serializable> {
	private final EntityPair<T, ID> pair;

	private List<FindFilter> filterList;
	private int pageNo;
	private int pageSize;
	private int maxPageSize;
	private String sortField;
	private boolean sortAsc;

	public EntityDbServiceBuilder(EntityPair<T, ID> pair) {
		this.pair = pair;
	}

	public EntityDbServiceBuilder<T, ID> filter(String name, Operator operator, Object value) {
		if (filterList == null) {
			filterList = Lists.newLinkedList();
		}

		filterList.add(new FindFilter(name, operator, value));
		return this;
	}

	public EntityDbServiceBuilder<T, ID> page(int pageNo, int pageSize, int maxPageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.maxPageSize = maxPageSize;
		return this;
	}

	public EntityDbServiceBuilder<T, ID> sort(String sortField, boolean sortAsc) {
		this.sortField = sortField;
		this.sortAsc = sortAsc;
		return this;
	}

	public Page<T> exeFindPage() {
		return pair.getDbService().findPage(pair, buildSpecification(), buildPageRequest());
	}

	public List<T> exeFindAll() {
		return pair.getDbService().findAll(pair, buildSpecification(), buildSort());
	}

	public T exeFindOne() {
		return pair.getDbService().findOne(pair, buildSpecification());
	}

	public long exeCount() {
		return pair.getDbService().count(pair, buildSpecification());
	}

	public T exeGet(ID id) {
		return pair.getDbService().get(pair, id);
	}

	public T exePost(T entity) {
		return pair.getDbService().post(pair, entity);
	}

	public T exePut(T entity, T oldEntity) {
		return pair.getDbService().put(pair, entity, oldEntity);
	}

	public Iterable<T> exePostIterable(Iterable<T> entities) {
		return pair.getDbService().postIterable(pair, entities);
	}

	public Iterable<T> exePutIterable(Iterable<T> entities, Iterable<T> oldEntities) {
		return pair.getDbService().putIterable(pair, entities, oldEntities);
	}

	public void exeDelete(ID id) {
		pair.getDbService().delete(pair, id);
	}

	public void exeDeleteIterable(Iterable<T> entities) {
		pair.getDbService().deleteIterable(pair, entities);
	}

	private Specification<T> buildSpecification() {
		return filterList == null ? null : Specifications.build(pair.getEntityClass(), filterList);
	}

	private PageRequest buildPageRequest() {
		return SpringDatas.newPageRequest(pageNo, pageSize, maxPageSize, buildSort());
	}

	private Sort buildSort() {
		return sortField == null ? null : SpringDatas.newSort(sortField, sortAsc);
	}
}