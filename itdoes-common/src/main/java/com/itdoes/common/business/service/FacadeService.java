package com.itdoes.common.business.service;

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

import com.itdoes.common.business.Businesses;
import com.itdoes.common.business.Businesses.EntityPair;
import com.itdoes.common.business.entity.BaseEntity;
import com.itdoes.common.core.jpa.SearchFilter;
import com.itdoes.common.core.jpa.Specifications;
import com.itdoes.common.core.util.Reflections;

/**
 * @author Jalen Zhong
 */
@Service
public class FacadeService extends BaseService implements ApplicationContextAware {
	private ApplicationContext applicationContext;

	private String entityPackage;

	private Map<String, EntityPair> entityPairs;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}

	@PostConstruct
	public void init() {
		entityPairs = Businesses.getEntityPairs(entityPackage, applicationContext);
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> Page<T> search(String ec, List<SearchFilter> filters, PageRequest pageRequest) {
		final EntityPair pair = getEntityPair(ec);
		final Page<T> page = (Page<T>) pair.getDao().findAll(Specifications.build(pair.getEntityClass(), filters),
				pageRequest);

		if (pair.hasSecureFields()) {
			final List<T> entityList = page.getContent();
			for (BaseEntity entity : entityList) {
				handleSecureFields(pair, OperationMode.GET, entity, null);
			}
		}

		return page;
	}

	public BaseEntity get(String ec, String idString) {
		final EntityPair pair = getEntityPair(ec);
		final Serializable id = convertId(idString, pair.getIdField().getType());
		final BaseEntity entity = pair.getDao().findOne(id);

		if (pair.hasSecureFields()) {
			handleSecureFields(pair, OperationMode.GET, entity, null);
		}

		return entity;
	}

	public long count(String ec, List<SearchFilter> filters) {
		final EntityPair pair = getEntityPair(ec);
		return pair.getDao().count(Specifications.build(pair.getEntityClass(), filters));
	}

	@Transactional(readOnly = false)
	public <T extends BaseEntity> void post(String ec, T entity) {
		final EntityPair pair = getEntityPair(ec);

		if (pair.hasSecureFields()) {
			handleSecureFields(pair, OperationMode.POST, entity, null);
		}

		pair.getDao().save(entity);
	}

	@Transactional(readOnly = false)
	public <T extends BaseEntity> void put(String ec, T entity, T oldEntity) {
		final EntityPair pair = getEntityPair(ec);

		if (pair.hasSecureFields()) {
			handleSecureFields(pair, OperationMode.PUT, entity, oldEntity);
		}

		pair.getDao().save(entity);
	}

	@Transactional(readOnly = false)
	public void delete(String ec, String idString) {
		final EntityPair pair = getEntityPair(ec);
		final Serializable id = convertId(idString, pair.getIdField().getType());
		pair.getDao().delete(id);
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

	private Serializable convertId(String id, Class<?> idClass) {
		return (Serializable) Reflections.convert(id, idClass);
	}

	private enum OperationMode {
		GET, POST, PUT
	}

	private void handleSecureFields(EntityPair pair, OperationMode mode, BaseEntity entity, BaseEntity oldEntity) {
		final Subject subject = SecurityUtils.getSubject();

		final String entityName = pair.getEntityClass().getSimpleName();
		for (Field secureField : pair.getSecureFields()) {
			final String secureFieldName = secureField.getName();
			switch (mode) {
			case GET:
				if (!subject.isPermitted(Businesses.getReadPermission(entityName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, null);
				}
				break;
			case POST:
				if (!subject.isPermitted(Businesses.getWritePermission(entityName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, null);
				}
				break;
			case PUT:
				if (!subject.isPermitted(Businesses.getWritePermission(entityName, secureFieldName))) {
					Reflections.invokeSet(entity, secureFieldName, Reflections.invokeGet(oldEntity, secureFieldName));
				}
				break;
			default:
				throw new IllegalArgumentException(
						"OperationMode \"" + mode + "\" is not supported by handleSecureFields()");
			}
		}
	}
}
