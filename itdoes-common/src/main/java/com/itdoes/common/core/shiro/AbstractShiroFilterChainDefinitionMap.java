package com.itdoes.common.core.shiro;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.springframework.beans.factory.FactoryBean;

import com.itdoes.common.core.Constants;
import com.itdoes.common.core.util.Collections3;

/**
 * @author Jalen Zhong
 */
public abstract class AbstractShiroFilterChainDefinitionMap implements FactoryBean<Section> {
	private String beforeStaticDefinitions;
	private String afterStaticDefinitions;

	@Override
	public Section getObject() throws Exception {
		final Ini ini = new Ini();
		ini.load(beforeStaticDefinitions);
		// did they explicitly state a 'urls' section? Not necessary, but just in case:
		Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
		if (CollectionUtils.isEmpty(section)) {
			// no urls section. Since this _is_ a urls chain definition property, just assume the default section
			// contains only the definitions:
			section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
		}

		final Map<String, String> dynamicDefinitions = getDynamicDefinitions();
		if (!CollectionUtils.isEmpty(dynamicDefinitions)) {
			section.putAll(dynamicDefinitions);
		}

		if (StringUtils.isNotBlank(afterStaticDefinitions)) {
			final String[] lines = StringUtils.split(afterStaticDefinitions, Constants.LINE_SEPARATOR);
			if (!Collections3.isEmpty(lines)) {
				for (String line : lines) {
					if (StringUtils.isBlank(line)) {
						continue;
					}

					final String[] items = StringUtils.split(line, '=');
					if (!Collections3.isEmpty(items)) {
						Validate.isTrue(items.length == 2,
								"AfterStaticDefinitions should be in format [<url> = <value>], but now it is [%s]",
								line);

						final String key = StringUtils.trim(items[0]);
						final String value = StringUtils.trim(items[1]);
						if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
							section.put(key, value);
						}
					}
				}
			}
		}

		return section;
	}

	@Override
	public Class<?> getObjectType() {
		return Section.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	public void setBeforeStaticDefinitions(String beforeStaticDefinitions) {
		this.beforeStaticDefinitions = beforeStaticDefinitions;
	}

	public void setAfterStaticDefinitions(String afterStaticDefinitions) {
		this.afterStaticDefinitions = afterStaticDefinitions;
	}

	protected abstract Map<String, String> getDynamicDefinitions();
}
