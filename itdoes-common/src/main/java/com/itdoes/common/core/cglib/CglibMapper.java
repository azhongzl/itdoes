package com.itdoes.common.core.cglib;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.cglib.beans.BeanCopier;

import com.itdoes.common.core.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class CglibMapper {
	private static final ConcurrentMap<Class<?>, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<>();

	public static <T> T copy(Object source, T target) {
		getBeanCopier(source.getClass()).copy(source, target, null);
		return target;
	}

	public static <T> T copy(Object source, Class<T> targetClass) {
		try {
			return copy(source, targetClass.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T copy(T source) {
		return copy(source, (Class<T>) source.getClass());
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
