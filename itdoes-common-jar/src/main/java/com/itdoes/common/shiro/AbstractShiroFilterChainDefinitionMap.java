package com.itdoes.common.shiro;

import java.util.Map;

import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author Jalen Zhong
 */
public abstract class AbstractShiroFilterChainDefinitionMap implements FactoryBean<Ini.Section> {
	private String staticDefinitions;

	@Override
	public Section getObject() throws Exception {
		final Ini ini = new Ini();
		ini.load(staticDefinitions);
		// did they explicitly state a 'urls' section? Not necessary, but just in case:
		Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
		if (CollectionUtils.isEmpty(section)) {
			// no urls section. Since this _is_ a urls chain definition property, just assume the default section
			// contains only the definitions:
			section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
		}

		final Map<String, String> dynamicDefinitions = getDynamicDefinitions();
		if (!CollectionUtils.isEmpty(dynamicDefinitions)) {
			section.putAll(dynamicDefinitions);
		}

		return section;
	}

	@Override
	public Class<?> getObjectType() {
		return Ini.Section.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public void setStaticDefinitions(String staticDefinitions) {
		this.staticDefinitions = staticDefinitions;
	}

	protected abstract Map<String, String> getDynamicDefinitions();
}
