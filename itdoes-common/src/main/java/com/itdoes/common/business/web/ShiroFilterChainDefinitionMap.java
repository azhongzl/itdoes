package com.itdoes.common.business.web;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;
import com.itdoes.common.business.EntityEnv;
import com.itdoes.common.business.EntityPair;
import com.itdoes.common.business.Perms;
import com.itdoes.common.business.entity.EntityPerm;
import com.itdoes.common.business.entity.EntityPermType;
import com.itdoes.common.core.shiro.AbstractShiroFilterChainDefinitionMap;
import com.itdoes.common.core.util.Collections3;

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
		final Map<String, String> dynamicDefinitions = Maps.newLinkedHashMap();
		for (EntityPair<?, ? extends Serializable> pair : env.getPairMap().values()) {
			final EntityPerm entityPerm = pair.getEntityPerm();
			if (entityPerm == null) {
				continue;
			}
			if (Collections3.isEmpty(entityPerm.types())) {
				continue;
			}
			final List<EntityPermType> entityPermTypeList = Collections3.asList(entityPerm.types());

			if (EntityPermType.FIND.isIn(entityPermTypeList)) {
				addDynamicDefinition(dynamicDefinitions, pair.getEntityClass(),
						EntityBaseController.ENTITY_COMMAND_FIND);
			}
			if (EntityPermType.FIND_ALL.isIn(entityPermTypeList)) {
				addDynamicDefinition(dynamicDefinitions, pair.getEntityClass(),
						EntityBaseController.ENTITY_COMMAND_FIND_ALL);
			}
			if (EntityPermType.FIND_ONE.isIn(entityPermTypeList)) {
				addDynamicDefinition(dynamicDefinitions, pair.getEntityClass(),
						EntityBaseController.ENTITY_COMMAND_FIND_ONE);
			}
			if (EntityPermType.COUNT.isIn(entityPermTypeList)) {
				addDynamicDefinition(dynamicDefinitions, pair.getEntityClass(),
						EntityBaseController.ENTITY_COMMAND_COUNT);
			}
			if (EntityPermType.GET.isIn(entityPermTypeList)) {
				addDynamicDefinition(dynamicDefinitions, pair.getEntityClass(),
						EntityBaseController.ENTITY_COMMAND_GET);
			}
			if (EntityPermType.DELETE.isIn(entityPermTypeList)) {
				addDynamicDefinition(dynamicDefinitions, pair.getEntityClass(),
						EntityBaseController.ENTITY_COMMAND_DELETE);
			}
			if (EntityPermType.POST.isIn(entityPermTypeList)) {
				addDynamicDefinition(dynamicDefinitions, pair.getEntityClass(),
						EntityBaseController.ENTITY_COMMAND_POST);
			}
			if (EntityPermType.PUT.isIn(entityPermTypeList)) {
				addDynamicDefinition(dynamicDefinitions, pair.getEntityClass(),
						EntityBaseController.ENTITY_COMMAND_PUT);
			}
			if (EntityPermType.POST_UPLOAD.isIn(entityPermTypeList)) {
				addDynamicDefinition(dynamicDefinitions, pair.getEntityClass(),
						EntityBaseController.ENTITY_COMMAND_POST_UPLOAD);
			}
			if (EntityPermType.PUT_UPLOAD.isIn(entityPermTypeList)) {
				addDynamicDefinition(dynamicDefinitions, pair.getEntityClass(),
						EntityBaseController.ENTITY_COMMAND_PUT_UPLOAD);
			}
		}
		return dynamicDefinitions;
	}

	private void addDynamicDefinition(Map<String, String> dynamicDefinitions, Class<?> entityClass, String command) {
		dynamicDefinitions.put(
				EntityBaseController.ENTITY_URL_PREFIX + "/" + entityClass.getSimpleName() + "/" + command + URL_ANY,
				MessageFormat.format(PERMS_PATTERN,
						Perms.getEntityOneEntityClassPerm(entityClass.getSimpleName(), command)));
	}
}
