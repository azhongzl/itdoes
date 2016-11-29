package com.itdoes.common.business.web;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Perms;
import com.itdoes.common.business.entity.EntityPerm;
import com.itdoes.common.business.entity.EntityPermFilter;
import com.itdoes.common.business.entity.EntityPermType;
import com.itdoes.common.core.shiro.AbstractShiroFilterChainDefinitionMap;
import com.itdoes.common.core.util.Collections3;

/**
 * @author Jalen Zhong
 */
public class ShiroFilterChainDefinitionMap extends AbstractShiroFilterChainDefinitionMap {
	private static final String FILTER_PERMS_PATTERN = "perms[\"{0}\"]";
	private static final String FILTER_ANON = "anon";
	private static final String FILTER_USER = "user";
	private static final String FILTER_AUTHC = "authc";
	private static final String URL_ANY = "/**";

	@Autowired
	private EntityEnv env;

	@Override
	protected Map<String, String> getDynamicDefinitions() {
		final Map<String, String> dynamicDefinitions = Maps.newLinkedHashMap();
		for (EntityPair<?, ? extends Serializable> pair : env.getPairMap().values()) {
			final EntityPerm entityPerm = pair.getEntityPerm();
			if (entityPerm == null) {
				continue;
			}

			final EntityPermType[] entityPermTypes = entityPerm.types();
			final EntityPermFilter[] entityPermFilters = entityPerm.filters();
			Validate.isTrue(!Collections3.isEmpty(entityPermTypes), "EntityPermType[] is empty");
			Validate.isTrue(!Collections3.isEmpty(entityPermFilters), "EntityPermFilter[] is empty");
			Validate.isTrue(entityPermTypes.length == entityPermFilters.length,
					"EntityPermType[%d] != EntityPermFilter[%d]", entityPermTypes.length, entityPermFilters.length);

			for (int i = 0; i < entityPermTypes.length; i++) {
				final EntityPermType[] commandEntityPermTypes = entityPermTypes[i].getCommandTypes();
				final EntityPermFilter entityPermFilter = entityPermFilters[i];
				for (int j = 0; j < commandEntityPermTypes.length; j++) {
					switch (entityPermFilter) {
					case PERMS:
						addPermsFilter(dynamicDefinitions, pair.getEntityClass(),
								commandEntityPermTypes[j].getCommand());
						break;
					case ANON:
						addAnonFilter(dynamicDefinitions, pair.getEntityClass(),
								commandEntityPermTypes[j].getCommand());
						break;
					case USER:
						addUserFilter(dynamicDefinitions, pair.getEntityClass(),
								commandEntityPermTypes[j].getCommand());
						break;
					case AUTHC:
						addAuthcFilter(dynamicDefinitions, pair.getEntityClass(),
								commandEntityPermTypes[j].getCommand());
						break;
					default:
						throw new IllegalArgumentException(
								"EntityPermFilter " + entityPermFilter + " is not supported");
					}
				}
			}
		}
		return dynamicDefinitions;
	}

	private void addPermsFilter(Map<String, String> dynamicDefinitions, Class<?> entityClass, String command) {
		dynamicDefinitions.put(getFilterUrl(entityClass, command), MessageFormat.format(FILTER_PERMS_PATTERN,
				Perms.getEntityOneEntityClassPerm(entityClass.getSimpleName(), command)));
	}

	private void addAnonFilter(Map<String, String> dynamicDefinitions, Class<?> entityClass, String command) {
		dynamicDefinitions.put(getFilterUrl(entityClass, command), FILTER_ANON);
	}

	private void addUserFilter(Map<String, String> dynamicDefinitions, Class<?> entityClass, String command) {
		dynamicDefinitions.put(getFilterUrl(entityClass, command), FILTER_USER);
	}

	private void addAuthcFilter(Map<String, String> dynamicDefinitions, Class<?> entityClass, String command) {
		dynamicDefinitions.put(getFilterUrl(entityClass, command), FILTER_AUTHC);
	}

	private String getFilterUrl(Class<?> entityClass, String command) {
		return EntityBaseController.ENTITY_URL_PREFIX + "/" + entityClass.getSimpleName() + "/" + command + URL_ANY;
	}
}
