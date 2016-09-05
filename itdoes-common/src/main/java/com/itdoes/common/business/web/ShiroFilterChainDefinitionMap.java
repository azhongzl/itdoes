package com.itdoes.common.business.web;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.itdoes.common.business.service.FacadeService;
import com.itdoes.common.core.shiro.AbstractShiroFilterChainDefinitionMap;

/**
 * @author Jalen Zhong
 */
public class ShiroFilterChainDefinitionMap extends AbstractShiroFilterChainDefinitionMap {
	private static final String PERMS_PATTERN = "perms[\"{0}\"]";
	private static final char PERM_SEPARATOR = ':';
	private static final String URL_ANY = "/**";

	private FacadeService facadeService;

	@Override
	protected Map<String, String> getDynamicDefinitions() {
		final Set<String> entityClassSimpleNames = facadeService.getEntityClassSimpleNames();
		final Map<String, String> dynamicDefinitions = new HashMap<String, String>(entityClassSimpleNames.size() * 6);
		for (String entityClassSimpleName : entityClassSimpleNames) {
			addDynamicDefinition(dynamicDefinitions, entityClassSimpleName, FacadeBaseController.FACADE_URL_SEARCH);
			addDynamicDefinition(dynamicDefinitions, entityClassSimpleName, FacadeBaseController.FACADE_URL_COUNT);
			addDynamicDefinition(dynamicDefinitions, entityClassSimpleName, FacadeBaseController.FACADE_URL_GET);
			addDynamicDefinition(dynamicDefinitions, entityClassSimpleName, FacadeBaseController.FACADE_URL_DELETE);
			addDynamicDefinition(dynamicDefinitions, entityClassSimpleName, FacadeBaseController.FACADE_URL_POST);
			addDynamicDefinition(dynamicDefinitions, entityClassSimpleName, FacadeBaseController.FACADE_URL_PUT);
		}
		return dynamicDefinitions;
	}

	private void addDynamicDefinition(Map<String, String> dynamicDefinitions, String entityClassSimpleName,
			String command) {
		dynamicDefinitions.put(
				FacadeBaseController.FACADE_URL_PREFIX + "/" + entityClassSimpleName + "/" + command + URL_ANY,
				MessageFormat.format(PERMS_PATTERN, entityClassSimpleName + PERM_SEPARATOR + command));
	}

	public void setFacadeService(FacadeService facadeService) {
		this.facadeService = facadeService;
	}
}
