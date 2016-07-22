package com.itdoes.common.dozer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.dozer.DozerBeanMapper;

/**
 * @author Jalen Zhong
 */
public class BeanMapper {
	private static final DozerBeanMapper DOZER = new DozerBeanMapper();

	public static <T> T map(Object source, Class<T> targetClass) {
		return DOZER.map(source, targetClass);
	}

	public static <T> List<T> mapList(Collection<?> collection, Class<T> targetClass) {
		final List<T> list = new ArrayList<T>(collection.size());
		for (Object source : collection) {
			list.add(map(source, targetClass));
		}
		return list;
	}

	public static void copy(Object source, Object target) {
		DOZER.map(source, target);
	}

	private BeanMapper() {
	}
}
