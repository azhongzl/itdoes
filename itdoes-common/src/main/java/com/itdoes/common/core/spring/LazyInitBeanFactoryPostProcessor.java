package com.itdoes.common.core.spring;

import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.Validate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import com.google.common.collect.Sets;
import com.itdoes.common.core.util.Collections3;

/**
 * @author Jalen Zhong
 */
public class LazyInitBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
	private Set<Pattern> lazyInitPatterns;

	public void setLazyInitPatternStrings(Set<String> lazyInitPatternStrings) {
		if (Collections3.isEmpty(lazyInitPatternStrings)) {
			return;
		}

		this.lazyInitPatterns = Sets.newHashSetWithExpectedSize(lazyInitPatternStrings.size());
		for (String lazyInitPaternString : lazyInitPatternStrings) {
			lazyInitPatterns.add(Pattern.compile(lazyInitPaternString));
		}
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		Validate.isTrue((beanFactory instanceof DefaultListableBeanFactory),
				"Bean factory must be type of DefaultListableBeanFactory for lazy init");

		final DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory) beanFactory;
		for (String beanName : listableBeanFactory.getBeanDefinitionNames()) {
			if (isLazyInit(beanName)) {
				final BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
				beanDefinition.setLazyInit(true);
			}
		}
	}

	private boolean isLazyInit(String beanName) {
		if (Collections3.isEmpty(lazyInitPatterns)) {
			return false;
		}

		for (Pattern lazyInitPattern : lazyInitPatterns) {
			if (lazyInitPattern.matcher(beanName).matches()) {
				return true;
			}
		}

		return false;
	}
}
