package com.itdoes.common.business.web;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Perms;
import com.itdoes.common.business.entity.EntityPerm;
import com.itdoes.common.business.entity.EntityPermCommand;
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

			final EntityPermType[] entityPermTypes = entityPerm.value();
			if (Collections3.isEmpty(entityPermTypes)) {
				continue;
			}

			for (int i = 0; i < entityPermTypes.length; i++) {
				final EntityPermCommand[] subEntityPermCommands = entityPermTypes[i].command().getSubCommands();
				final EntityPermFilter entityPermFilter = entityPermTypes[i].filter();
				for (int j = 0; j < subEntityPermCommands.length; j++) {
					switch (entityPermFilter) {
					case PERMS:
						addPermsFilter(dynamicDefinitions, pair.getEntityClass(),
								subEntityPermCommands[j].getCommand());
						break;
					case ANON:
						addAnonFilter(dynamicDefinitions, pair.getEntityClass(), subEntityPermCommands[j].getCommand());
						break;
					case USER:
						addUserFilter(dynamicDefinitions, pair.getEntityClass(), subEntityPermCommands[j].getCommand());
						break;
					case AUTHC:
						addAuthcFilter(dynamicDefinitions, pair.getEntityClass(),
								subEntityPermCommands[j].getCommand());
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
