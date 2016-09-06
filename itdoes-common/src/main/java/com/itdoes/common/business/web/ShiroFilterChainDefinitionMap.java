package com.itdoes.common.business.web;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.itdoes.common.business.Env;
import com.itdoes.common.business.Permissions;
import com.itdoes.common.core.shiro.AbstractShiroFilterChainDefinitionMap;

/**
 * @author Jalen Zhong
 */
public class ShiroFilterChainDefinitionMap extends AbstractShiroFilterChainDefinitionMap {
	private static final String PERMS_PATTERN = "perms[\"{0}\"]";
	private static final String URL_ANY = "/**";

	private Env env;

	@Override
	protected Map<String, String> getDynamicDefinitions() {
		final Set<String> entityNames = env.getEntityClassSimpleNames();
		final Map<String, String> dynamicDefinitions = new LinkedHashMap<String, String>(entityNames.size() * 6);
		for (String entityName : entityNames) {
			addDynamicDefinition(dynamicDefinitions, entityName, FacadeBaseController.FACADE_URL_SEARCH);
			addDynamicDefinition(dynamicDefinitions, entityName, FacadeBaseController.FACADE_URL_COUNT);
			addDynamicDefinition(dynamicDefinitions, entityName, FacadeBaseController.FACADE_URL_GET);
			addDynamicDefinition(dynamicDefinitions, entityName, FacadeBaseController.FACADE_URL_DELETE);
			addDynamicDefinition(dynamicDefinitions, entityName, FacadeBaseController.FACADE_URL_POST);
			addDynamicDefinition(dynamicDefinitions, entityName, FacadeBaseController.FACADE_URL_PUT);
		}
		return dynamicDefinitions;
	}

	private void addDynamicDefinition(Map<String, String> dynamicDefinitions, String entityName, String command) {
		dynamicDefinitions.put(FacadeBaseController.FACADE_URL_PREFIX + "/" + entityName + "/" + command + URL_ANY,
				MessageFormat.format(PERMS_PATTERN, Permissions.getEntityPermission(entityName, command)));
	}

	public void setEnv(Env env) {
		this.env = env;
	}
}
