package com.itdoes.common.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

/**
 * @author Jalen Zhong
 */
public class Collections3 {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map propToMap(final Collection collection, final String keyPropertyName,
			final String valuePropertyName) {
		final Map map = new HashMap(collection.size());

		try {
			for (Object obj : collection) {
				map.put(PropertyUtils.getProperty(obj, keyPropertyName),
						PropertyUtils.getProperty(obj, valuePropertyName));
			}
			return map;
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List propToList(final Collection collection, final String propertyName) {
		final List list = new ArrayList(collection.size());

		try {
			for (Object obj : collection) {
				list.add(PropertyUtils.getProperty(obj, propertyName));
			}
			return list;
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static String propToString(final Collection<?> collection, final String propertyName,
			final String separator) {
		return StringUtils.join(propToList(collection, propertyName), separator);
	}

	public static String prePost(final Collection<?> collection, final String prefix, final String postfix) {
		final StringBuilder builder = new StringBuilder();
		for (Object obj : collection) {
			builder.append(prefix).append(obj).append(postfix);
		}
		return builder.toString();
	}

	public static boolean isEmpty(Object[] objects) {
		return objects == null || objects.length == 0;
	}

	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

	public static boolean isEmpty(Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	public static <T> T getFirst(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}

		return collection.iterator().next();
	}

	public static <T> T getLast(Collection<T> collection) {
		if (isEmpty(collection)) {
			return null;
		}

		if (collection instanceof List) {
			final List<T> list = (List<T>) collection;
			return list.get(list.size() - 1);
		}

		final Iterator<T> it = collection.iterator();
		while (true) {
			final T current = it.next();
			if (!it.hasNext()) {
				return current;
			}
		}
	}

	public static <T> List<T> union(final Collection<T> a, final Collection<T> b) {
		final List<T> result = new ArrayList<T>(a);
		result.addAll(b);
		return result;
	}

	public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
		final List<T> result = new ArrayList<T>(a);
		for (T t : b) {
			result.remove(t);
		}
		return result;
	}

	public static <T> List<T> intersection(final Collection<T> a, final Collection<T> b) {
		final List<T> result = new ArrayList<T>();
		for (T t : a) {
			if (b.contains(t)) {
				result.add(t);
			}
		}
		return result;
	}

	public static List<Long> asList(long... array) {
		return Longs.asList(array);
	}

	public static List<Integer> asList(int... array) {
		return Ints.asList(array);
	}

	public static List<Double> asList(double... array) {
		return Doubles.asList(array);
	}

	private Collections3() {
	}
}
