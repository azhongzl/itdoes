package com.itdoes.common.core.cglib;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.cglib.beans.BeanCopier;

import com.itdoes.common.core.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class CglibMapper {
	private static final ConcurrentMap<Class<?>, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<Class<?>, BeanCopier>();

	public static Object copy(Object source, Object target) {
		getBeanCopier(source.getClass()).copy(source, target, null);
		return target;
	}

	public static Object copy(Object source, Class<?> targetClass) {
		try {
			return copy(source, targetClass.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static Object copy(Object source) {
		return copy(source, source.getClass());
	}

	public static BeanCopier getBeanCopier(Class<?> clazz) {
		if (BEAN_COPIERS.containsKey(clazz)) {
			return BEAN_COPIERS.get(clazz);
		} else {
			final BeanCopier beanCopier = BeanCopier.create(clazz, clazz, false);
			BEAN_COPIERS.putIfAbsent(clazz, beanCopier);
			return beanCopier;
		}
	}

	private CglibMapper() {
	}
}
