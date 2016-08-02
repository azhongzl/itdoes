package com.itdoes.business.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itdoes.business.common.Constants;
import com.itdoes.common.business.BaseDao;
import com.itdoes.common.business.BaseEntity;
import com.itdoes.common.business.BaseService;
import com.itdoes.common.business.Businesses;
import com.itdoes.common.business.Businesses.EntityPair;
import com.itdoes.common.jpa.SearchFilter;
import com.itdoes.common.jpa.Specifications;
import com.itdoes.common.util.Reflections;

/**
 * @author Jalen Zhong
 */
@Service
public class FacadeService extends BaseService implements ApplicationContextAware {
	private static final String ENTITY_BASE_PACKAGE = Constants.ENTITY_BASE_PACKAGE;

	private Map<String, EntityPair> entityPairs;

	private ApplicationContext applicationContext;

	@PostConstruct
	public void init() {
		entityPairs = Businesses.getEntityPairs(ENTITY_BASE_PACKAGE, FacadeService.class.getClassLoader(),
				applicationContext);
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> Page<T> search(String ec, List<SearchFilter> filters, PageRequest pageRequest) {
		final EntityPair pair = getEntityPair(ec);
		final Page<T> page = (Page<T>) getDao(pair).findAll(Specifications.build(getEntityClass(pair), filters),
				pageRequest);

		if (hasSecureColumns(pair)) {
			final List<T> list = page.getContent();
			for (BaseEntity entity : list) {
				handleSecureColumns(pair, OperationMode.GET, entity, null);
			}
		}

		return page;
	}

	public BaseEntity get(String ec, String idString) {
		final EntityPair pair = getEntityPair(ec);
		final Serializable id = convertId(idString, pair.idField.getType());
		final BaseEntity entity = getDao(pair).findOne(id);

		if (hasSecureColumns(pair)) {
			handleSecureColumns(pair, OperationMode.GET, entity, null);
		}

		return entity;
	}

	public long count(String ec, List<SearchFilter> filters) {
		final EntityPair pair = getEntityPair(ec);
		return getDao(pair).count(Specifications.build(getEntityClass(pair), filters));
	}

	@Transactional(readOnly = false)
	public <T extends BaseEntity> void post(String ec, T entity) {
		final EntityPair pair = getEntityPair(ec);

		if (hasSecureColumns(pair)) {
			handleSecureColumns(pair, OperationMode.POST, entity, null);
		}

		getDao(pair).save(entity);
	}

	@Transactional(readOnly = false)
	public <T extends BaseEntity> void put(String ec, T entity, T oldEntity) {
		final EntityPair pair = getEntityPair(ec);

		if (hasSecureColumns(pair)) {
			handleSecureColumns(pair, OperationMode.PUT, entity, oldEntity);
		}

		getDao(pair).save(entity);
	}

	@Transactional(readOnly = false)
	public void delete(String ec, String idString) {
		final EntityPair pair = getEntityPair(ec);
		final Serializable id = convertId(idString, pair.idField.getType());
		getDao(pair).delete(id);
	}

	public EntityPair getEntityPair(String ec) {
		final EntityPair pair = entityPairs.get(ec);
		if (pair == null) {
			throw new IllegalArgumentException("Cannot find EntityPair for \"" + ec + "\" in FacadeService");
		}
		return pair;
	}

	public Set<String> getEntityClassSimpleNames() {
		return entityPairs.keySet();
	}

	@SuppressWarnings("unchecked")
	private <T extends BaseEntity> Class<T> getEntityClass(EntityPair pair) {
		return (Class<T>) pair.entityClass;
	}

	@SuppressWarnings("unchecked")
	private <T extends BaseEntity, ID extends Serializable> BaseDao<T, ID> getDao(EntityPair pair) {
		return (BaseDao<T, ID>) pair.dao;
	}

	private Serializable convertId(String id, Class<?> idClass) {
		return (Serializable) Reflections.convert(id, idClass);
	}

	private enum OperationMode {
		GET, POST, PUT
	}

	private boolean hasSecureColumns(EntityPair pair) {
		return !pair.secureFields.isEmpty();
	}

	private void handleSecureColumns(EntityPair pair, OperationMode mode, BaseEntity entity, BaseEntity oldEntity) {
		final Subject subject = SecurityUtils.getSubject();

		final String tableName = pair.entityClass.getSimpleName();
		for (Field secureField : pair.secureFields) {
			final String secureFieldName = secureField.getName();
			switch (mode) {
			case GET:
				if (!subject.isPermitted(Businesses.getReadPermission(tableName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, null);
				}
				break;
			case POST:
				if (!subject.isPermitted(Businesses.getWritePermission(tableName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, null);
				}
				break;
			case PUT:
				if (!subject.isPermitted(Businesses.getWritePermission(tableName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, Reflections.invokeGet(oldEntity, secureFieldName));
				}
				break;
			default:
				throw new IllegalArgumentException(
						"OperationMode \"" + mode + "\" is not supported by handleSecureColumns()");
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
