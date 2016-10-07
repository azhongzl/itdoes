package com.itdoes.common.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

	public static boolean isEmpty(Object... objects) {
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
		if (isEmpty(a)) {
			return asList(b);
		} else if (isEmpty(b)) {
			return asList(a);
		}

		final List<T> result = new ArrayList<T>(a);
		result.addAll(b);
		return result;
	}

	public static <T> List<T> subtract(final Collection<T> a, final Collection<T> b) {
		if (isEmpty(a)) {
			return Collections.emptyList();
		} else if (isEmpty(b)) {
			return asList(a);
		}

		final List<T> result = new ArrayList<T>(a);
		for (T t : b) {
			result.remove(t);
		}
		return result;
	}

	public static <T> List<T> intersection(final Collection<T> a, final Collection<T> b) {
		if (isEmpty(a) || isEmpty(b)) {
			return Collections.emptyList();
		}

		final List<T> result = new ArrayList<T>();
		for (T t : a) {
			if (b.contains(t)) {
				result.add(t);
			}
		}
		return result;
	}

	public static Map<String, String> asMap(Properties properties) {
		final Map<String, String> map = new HashMap<String, String>(properties.size());
		for (Object key : properties.keySet()) {
			final String keyStr = (String) key;
			map.put(keyStr, properties.getProperty(keyStr));
		}
		return map;
	}

	public static <T> List<T> asList(Collection<T> collection) {
		if (isEmpty(collection)) {
			return Collections.emptyList();
		}

		if (collection instanceof List) {
			return (List<T>) collection;
		} else {
			return new ArrayList<T>(collection);
		}
	}

	@SafeVarargs
	public static <T> List<T> asList(T... array) {
		if (isEmpty(array)) {
			return Collections.emptyList();
		}

		return Arrays.asList(array);
	}

	@SafeVarargs
	public static <T> HashSet<T> asHashSet(T... array) {
		return new HashSet<T>(asList(array));
	}

	@SafeVarargs
	public static <T> LinkedHashSet<T> asLinkedHashSet(T... array) {
		return new LinkedHashSet<T>(asList(array));
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
