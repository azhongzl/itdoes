package com.itdoes.common.business.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.entity.FieldConstraintPair;
import com.itdoes.common.business.entity.FieldConstraintStrategy;
import com.itdoes.common.core.jpa.FindFilter;
import com.itdoes.common.core.jpa.FindFilter.Operator;
import com.itdoes.common.core.jpa.Specifications;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
@Service
public class EntityDbService extends BaseTransactionalService {
	@Autowired
	private EntityEnv env;

	public <T, ID extends Serializable> Page<T> find(EntityPair<T, ID> pair, Specification<T> specification,
			PageRequest pageRequest) {
		return pair.getDao().findAll(specification, pageRequest);
	}

	public <T, ID extends Serializable> List<T> findAll(EntityPair<T, ID> pair, Specification<T> specification,
			Sort sort) {
		if (sort == null) {
			return pair.getDao().findAll(specification);
		} else {
			return pair.getDao().findAll(specification, sort);
		}
	}

	public <T, ID extends Serializable> T findOne(EntityPair<T, ID> pair, Specification<T> specification) {
		return pair.getDao().findOne(specification);
	}

	public <T, ID extends Serializable> long count(EntityPair<T, ID> pair, Specification<T> specification) {
		return pair.getDao().count(specification);
	}

	public <T, ID extends Serializable> T get(EntityPair<T, ID> pair, ID id) {
		return pair.getDao().findOne(id);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(readOnly = false)
	public <T, ID extends Serializable> T save(EntityPair<T, ID> pair, T entity) {
		if (!pair.getPkFieldConstraintPairSet().isEmpty()) {
			for (FieldConstraintPair constraint : pair.getPkFieldConstraintPairSet()) {

			}
		}

		if (!pair.getFkFieldConstraintPairSet().isEmpty()) {
			for (FieldConstraintPair constraint : pair.getFkFieldConstraintPairSet()) {
				final Object fkFieldValue = Reflections.getFieldValue(entity, constraint.getFkField());
				if (fkFieldValue == null) {
					continue;
				}

				final EntityPair pkPair = env.getPair(constraint.getPkEntity().getSimpleName());
				final Specification spec = Specifications.build(constraint.getPkEntity(), Lists
						.newArrayList(new FindFilter(constraint.getPkField().getName(), Operator.EQ, fkFieldValue)));
				final long count = count(pkPair, spec);
				if (count <= 0) {
					throw new IllegalArgumentException("[" + fkFieldValue + "] in ["
							+ constraint.getFkEntity().getSimpleName() + "." + constraint.getFkField().getName()
							+ "] is invalid since it is not in [" + constraint.getPkEntity().getSimpleName() + "."
							+ constraint.getPkField().getName() + "]");
				}
			}
		}
		return pair.getDao().save(entity);
	}

	@Transactional(readOnly = false)
	public <T, ID extends Serializable> Iterable<T> saveIterable(EntityPair<T, ID> pair, Iterable<T> entities) {
		if (!pair.getPkFieldConstraintPairSet().isEmpty() || !pair.getFkFieldConstraintPairSet().isEmpty()) {
			for (T entity : entities) {
				save(pair, entity);
			}
			return entities;
		} else {
			return pair.getDao().save(entities);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional(readOnly = false)
	public <T, ID extends Serializable> void delete(EntityPair<T, ID> pair, ID id) {
		T entity = null;
		if (!pair.getPkFieldConstraintPairSet().isEmpty()) {
			for (FieldConstraintPair constraint : pair.getPkFieldConstraintPairSet()) {
				final Object pkFieldValue;
				if (constraint.getPkField().equals(pair.getIdField())) {
					pkFieldValue = id;
				} else {
					if (entity == null) {
						entity = get(pair, id);
					}
					pkFieldValue = Reflections.getFieldValue(entity, constraint.getPkField());
				}

				final EntityPair fkPair = env.getPair(constraint.getFkEntity().getSimpleName());
				final Specification spec = Specifications.build(constraint.getFkEntity(), Lists
						.newArrayList(new FindFilter(constraint.getFkField().getName(), Operator.EQ, pkFieldValue)));
				final long count = count(fkPair, spec);
				if (count > 0) {
					final FieldConstraintStrategy strategy = constraint.getDeleteStragety();
					if (strategy.equals(FieldConstraintStrategy.CASCADE)) {
						final List fkEntityList = findAll(fkPair, spec, null);
						if (!Collections3.isEmpty(fkEntityList)) {
							deleteIterable(fkPair, fkEntityList);
						}
					} else if (strategy.equals(FieldConstraintStrategy.RESTRICT)
							|| strategy.equals(FieldConstraintStrategy.NO_ACTION)) {
						throw new IllegalStateException("Cannot delete [" + pkFieldValue + "] from ["
								+ constraint.getPkEntity().getSimpleName() + "." + constraint.getPkField().getName()
								+ "] since it is referred by [" + constraint.getFkEntity().getSimpleName() + "."
								+ constraint.getFkField().getName() + "]");
					} else if (strategy.equals(FieldConstraintStrategy.SET_NULL)
							|| strategy.equals(FieldConstraintStrategy.SET_DEFAULT)) {
						final List fkEntityList = findAll(fkPair, spec, null);
						if (!Collections3.isEmpty(fkEntityList)) {
							for (Object fkEntry : fkEntityList) {
								Reflections.setFieldValue(fkEntry, constraint.getFkField(), null);
							}
							saveIterable(fkPair, fkEntityList);
						}
					}
				}
			}
		}

		pair.getDao().delete(id);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public <T, ID extends Serializable> void deleteIterable(EntityPair<T, ID> pair, Iterable<T> entities) {
		if (!pair.getPkFieldConstraintPairSet().isEmpty()) {
			for (T entity : entities) {
				delete(pair, (ID) Reflections.getFieldValue(entity, pair.getIdField()));
			}
		} else {
			pair.getDao().delete(entities);
		}
	}
}
