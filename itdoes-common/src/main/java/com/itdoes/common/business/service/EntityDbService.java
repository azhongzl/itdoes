package com.itdoes.common.business.service;

import java.io.Serializable;
import java.util.Iterator;
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
import com.itdoes.common.core.jpa.Specifications;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.Objects;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
@Service
public class EntityDbService extends BaseTransactionalService {
	@Autowired
	private EntityEnv env;

	public <T, ID extends Serializable> Page<T> findPage(EntityPair<T, ID> pair, Specification<T> specification,
			PageRequest pageRequest) {
		return pair.getDao().findAll(specification, pageRequest);
	}

	public <T, ID extends Serializable> List<T> findAll(EntityPair<T, ID> pair, Specification<T> specification,
			Sort sort) {
		return sort == null ? pair.getDao().findAll(specification) : pair.getDao().findAll(specification, sort);
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

	@Transactional(readOnly = false)
	public <T, ID extends Serializable> T post(EntityPair<T, ID> pair, T entity) {
		saveFk(pair, entity);
		return pair.getDao().save(entity);
	}

	@Transactional(readOnly = false)
	public <T, ID extends Serializable> T put(EntityPair<T, ID> pair, T entity, T oldEntity) {
		savePk(pair, entity, oldEntity);
		saveFk(pair, entity);
		return pair.getDao().save(entity);

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T, ID extends Serializable> void savePk(EntityPair<T, ID> pair, T entity, T oldEntity) {
		if (pair.getPkFieldConstraintPairSet().isEmpty()) {
			return;
		}

		for (FieldConstraintPair constraint : pair.getPkFieldConstraintPairSet()) {
			final Object pkFieldValue = Reflections.getFieldValue(entity, constraint.getPkField());
			final Object oldPkFieldValue = Reflections.getFieldValue(oldEntity, constraint.getPkField());
			if (!Objects.isEqual(pkFieldValue, oldPkFieldValue)) {
				final FieldConstraintStrategy strategy = constraint.getUpdateStrategy();
				if (strategy.equals(FieldConstraintStrategy.CASCADE)) {
					final EntityPair fkPair = env.getPair(constraint.getFkEntity());
					final Specification spec = Specifications.build(constraint.getFkEntity(),
							Lists.newArrayList(FindFilter.equal(constraint.getFkField().getName(), oldPkFieldValue)));
					final List fkEntityList = findAll(fkPair, spec, null);
					if (!Collections3.isEmpty(fkEntityList)) {
						for (Object fkEntity : fkEntityList) {
							Reflections.setFieldValue(fkEntity, constraint.getFkField(), pkFieldValue);
						}
					}
				} else if (strategy.equals(FieldConstraintStrategy.RESTRICT)
						|| strategy.equals(FieldConstraintStrategy.NO_ACTION)) {
					throw new IllegalArgumentException("Cannot delete [" + constraint.getPkEntity().getSimpleName()
							+ "." + constraint.getPkField().getName() + "] = [" + pkFieldValue
							+ "] since it is referred by [" + constraint.getFkEntity().getSimpleName() + "."
							+ constraint.getFkField().getName() + "]");
				} else if (strategy.equals(FieldConstraintStrategy.SET_NULL)
						|| strategy.equals(FieldConstraintStrategy.SET_DEFAULT)) {
					final EntityPair fkPair = env.getPair(constraint.getFkEntity());
					final Specification spec = Specifications.build(constraint.getFkEntity(),
							Lists.newArrayList(FindFilter.equal(constraint.getFkField().getName(), oldPkFieldValue)));
					final List fkEntityList = findAll(fkPair, spec, null);
					if (!Collections3.isEmpty(fkEntityList)) {
						final Object fkFieldValue = strategy.equals(FieldConstraintStrategy.SET_NULL) ? null
								: constraint.getDefaultValue();
						for (Object fkEntity : fkEntityList) {
							Reflections.setFieldValue(fkEntity, constraint.getFkField(), fkFieldValue);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private <T, ID extends Serializable> void saveFk(EntityPair<T, ID> pair, T entity) {
		if (pair.getFkFieldConstraintPairSet().isEmpty()) {
			return;
		}

		for (FieldConstraintPair constraint : pair.getFkFieldConstraintPairSet()) {
			final Object fkFieldValue = Reflections.getFieldValue(entity, constraint.getFkField());
			if (fkFieldValue == null) {
				continue;
			}

			final EntityPair pkPair = env.getPair(constraint.getPkEntity());
			final Specification spec = Specifications.build(constraint.getPkEntity(),
					Lists.newArrayList(FindFilter.equal(constraint.getPkField().getName(), fkFieldValue)));
			final long count = count(pkPair, spec);
			if (count <= 0) {
				throw new IllegalArgumentException("Cannot save [" + constraint.getFkEntity().getSimpleName() + "."
						+ constraint.getFkField().getName() + "] = [" + fkFieldValue + "] since it is not in ["
						+ constraint.getPkEntity().getSimpleName() + "." + constraint.getPkField().getName() + "]");
			}
		}
	}

	@Transactional(readOnly = false)
	public <T, ID extends Serializable> Iterable<T> postIterable(EntityPair<T, ID> pair, Iterable<T> entities) {
		if (!pair.getFkFieldConstraintPairSet().isEmpty()) {
			for (T entity : entities) {
				post(pair, entity);
			}
			return entities;
		} else {
			return pair.getDao().save(entities);
		}
	}

	@Transactional(readOnly = false)
	public <T, ID extends Serializable> Iterable<T> putIterable(EntityPair<T, ID> pair, Iterable<T> entities,
			Iterable<T> oldEntities) {
		if (!pair.getPkFieldConstraintPairSet().isEmpty() || !pair.getFkFieldConstraintPairSet().isEmpty()) {
			final Iterator<T> entityIterator = entities.iterator();
			final Iterator<T> oldEntityIterator = oldEntities.iterator();
			while (entityIterator.hasNext()) {
				put(pair, entityIterator.next(), oldEntityIterator.next());
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

				final EntityPair fkPair = env.getPair(constraint.getFkEntity());
				final Specification spec = Specifications.build(constraint.getFkEntity(),
						Lists.newArrayList(FindFilter.equal(constraint.getFkField().getName(), pkFieldValue)));
				final long count = count(fkPair, spec);
				if (count > 0) {
					final FieldConstraintStrategy strategy = constraint.getDeleteStrategy();
					if (strategy.equals(FieldConstraintStrategy.CASCADE)) {
						final List fkEntityList = findAll(fkPair, spec, null);
						if (!Collections3.isEmpty(fkEntityList)) {
							deleteIterable(fkPair, fkEntityList);
						}
					} else if (strategy.equals(FieldConstraintStrategy.RESTRICT)
							|| strategy.equals(FieldConstraintStrategy.NO_ACTION)) {
						throw new IllegalArgumentException("Cannot delete [" + constraint.getPkEntity().getSimpleName()
								+ "." + constraint.getPkField().getName() + "] = [" + pkFieldValue
								+ "] since it is referred by [" + constraint.getFkEntity().getSimpleName() + "."
								+ constraint.getFkField().getName() + "]");
					} else if (strategy.equals(FieldConstraintStrategy.SET_NULL)
							|| strategy.equals(FieldConstraintStrategy.SET_DEFAULT)) {
						final List fkEntityList = findAll(fkPair, spec, null);
						if (!Collections3.isEmpty(fkEntityList)) {
							final Object fkFieldValue = strategy.equals(FieldConstraintStrategy.SET_NULL) ? null
									: constraint.getDefaultValue();
							for (Object fkEntity : fkEntityList) {
								Reflections.setFieldValue(fkEntity, constraint.getFkField(), fkFieldValue);
							}
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
