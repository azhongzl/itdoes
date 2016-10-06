package com.itdoes.common.business.web;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.Permissions;
import com.itdoes.common.core.shiro.AbstractShiroFilterChainDefinitionMap;

/**
 * @author Jalen Zhong
 */
public class ShiroFilterChainDefinitionMap extends AbstractShiroFilterChainDefinitionMap {
	private static final String PERMS_PATTERN = "perms[\"{0}\"]";
	private static final String URL_ANY = "/**";

	@Autowired
	private EntityEnv env;

	@Override
	protected Map<String, String> getDynamicDefinitions() {
		final Set<String> entityNames = env.getPairMap().keySet();
		final Map<String, String> dynamicDefinitions = new LinkedHashMap<String, String>(entityNames.size() * 6);
		for (String entityName : entityNames) {
			addDynamicDefinition(dynamicDefinitions, entityName, EntityBaseController.ENTITY_COMMAND_FIND);
			addDynamicDefinition(dynamicDefinitions, entityName, EntityBaseController.ENTITY_COMMAND_FIND_ONE);
			addDynamicDefinition(dynamicDefinitions, entityName, EntityBaseController.ENTITY_COMMAND_COUNT);
			addDynamicDefinition(dynamicDefinitions, entityName, EntityBaseController.ENTITY_COMMAND_GET);
			addDynamicDefinition(dynamicDefinitions, entityName, EntityBaseController.ENTITY_COMMAND_DELETE);
			addDynamicDefinition(dynamicDefinitions, entityName, EntityBaseController.ENTITY_COMMAND_POST);
			addDynamicDefinition(dynamicDefinitions, entityName, EntityBaseController.ENTITY_COMMAND_PUT);
			addDynamicDefinition(dynamicDefinitions, entityName, EntityBaseController.ENTITY_COMMAND_POST_UPLOAD);
			addDynamicDefinition(dynamicDefinitions, entityName, EntityBaseController.ENTITY_COMMAND_PUT_UPLOAD);
		}
		return dynamicDefinitions;
	}

	private void addDynamicDefinition(Map<String, String> dynamicDefinitions, String entityName, String command) {
		dynamicDefinitions.put(EntityBaseController.ENTITY_URL_PREFIX + "/" + entityName + "/" + command + URL_ANY,
				MessageFormat.format(PERMS_PATTERN,
						Permissions.getEntityOneEntityClassPermission(entityName, command)));
	}
}
