package com.itdoes.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jalen Zhong
 */
public class Reflections {
	private static final String GET_PREFIX = "get";
	private static final String SET_PREFIX = "set";
	private static final String CGLIB_CLASS_SEPARATOR = "$$";

	private static final Class<?>[] EMPTY_CLASSES = new Class[] {};
	private static final Object[] EMPTY_OBJECTS = new Object[] {};

	private static final Logger LOGGER = LoggerFactory.getLogger(Reflections.class);

	public static Object invokeGet(Object obj, String propertyName) {
		final String getMethodName = GET_PREFIX + StringUtils.capitalize(propertyName);
		return invokeMethod(obj, getMethodName, EMPTY_CLASSES, EMPTY_OBJECTS);
	}

	public static void invokeSet(Object obj, String propertyName, Object value) {
		final String setMethodName = SET_PREFIX + StringUtils.capitalize(propertyName);
		invokeMethodByName(obj, setMethodName, new Object[] { value });
	}

	public static Object invokeMethod(Object obj, String methodName, Class<?>[] paramTypes, Object[] args) {
		final Method method = getAccessibleMethod(obj, methodName, paramTypes);
		try {
			return method.invoke(obj, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static Object invokeMethodByName(Object obj, String methodName, Object[] args) {
		final Method method = getAccessibleMethodByName(obj, methodName);
		try {
			return method.invoke(obj, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static Object getFieldValue(Object obj, String fieldName) {
		final Field field = getAccessibleField(obj, fieldName);
		try {
			return field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static void setFieldValue(Object obj, String fieldName, Object fieldValue) {
		final Field field = getAccessibleField(obj, fieldName);
		try {
			field.set(obj, fieldValue);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static Method getAccessibleMethod(Object obj, String methodName, Class<?>[] paramTypes,
			boolean errorIfNotFound) {
		Validate.notNull(obj, "Object cannot be null");
		Validate.notBlank(methodName, "MethodName cannot be blank");

		for (Class<?> superclass = obj.getClass(); !Object.class.equals(superclass); superclass = superclass
				.getSuperclass()) {
			try {
				final Method method = superclass.getDeclaredMethod(methodName, paramTypes);
				makeAccessible(method);
				return method;
			} catch (NoSuchMethodException e) {
			}
		}

		if (errorIfNotFound) {
			throw new IllegalArgumentException("Cannot find method [" + methodName + "] on object [" + obj
					+ "] by param classes [" + paramTypes + "]");
		} else {
			return null;
		}
	}

	public static Method getAccessibleMethod(Object obj, String methodName, Class<?>[] paramTypes) {
		return getAccessibleMethod(obj, methodName, paramTypes, true);
	}

	public static Method getAccessibleMethodByName(Object obj, String methodName, boolean errorIfNotFound) {
		Validate.notNull(obj, "Object cannot be null");
		Validate.notBlank(methodName, "MethodName cannot be null");

		for (Class<?> superclass = obj.getClass(); !Object.class.equals(superclass); superclass = superclass
				.getSuperclass()) {
			final Method[] methods = superclass.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					makeAccessible(method);
					return method;
				}
			}
		}

		if (errorIfNotFound) {
			throw new IllegalArgumentException("Cannot find method [" + methodName + "] on object [" + obj + "]");
		} else {
			return null;
		}
	}

	public static Method getAccessibleMethodByName(Object obj, String methodName) {
		return getAccessibleMethodByName(obj, methodName, true);
	}

	public static Field getAccessibleField(Object obj, String fieldName, boolean errorIfNotFound) {
		Validate.notNull(obj, "Object cannot be null");
		Validate.notBlank(fieldName, "FieldName cannot be null");

		for (Class<?> superclass = obj.getClass(); !Object.class.equals(superclass); superclass = superclass
				.getSuperclass()) {
			try {
				final Field field = superclass.getDeclaredField(fieldName);
				makeAccessible(field);
				return field;
			} catch (NoSuchFieldException e) {
			}
		}

		if (errorIfNotFound) {
			throw new IllegalArgumentException("Cannot find field [" + fieldName + "] on object [" + obj + "]");
		} else {
			return null;
		}
	}

	public static Field getAccessibleField(Object obj, String fieldName) {
		return getAccessibleField(obj, fieldName, true);
	}

	public static void makeAccessible(Method method) {
		if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
				&& !method.isAccessible()) {
			method.setAccessible(true);
		}
	}

	public static void makeAccessible(Field field) {
		if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
				|| Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
			field.setAccessible(true);
		}
	}

	public static Class<?> getClassGenericType(Class<?> clazz) {
		return getClassGenericType(clazz, 0);
	}

	public static Class<?> getClassGenericType(Class<?> clazz, int index) {
		final Type genericSuperclass = clazz.getGenericSuperclass();
		if (!(genericSuperclass instanceof ParameterizedType)) {
			LOGGER.warn("{}'s generic superclass {} is not ParameterizedType", clazz.getSimpleName(),
					genericSuperclass);
			return Object.class;
		}

		final ParameterizedType paramType = (ParameterizedType) genericSuperclass;
		final Type[] actualTypes = paramType.getActualTypeArguments();

		if (index < 0 || index >= actualTypes.length) {
			LOGGER.warn("Index {} is out of bound for class {} whose actual type length is {}", index,
					clazz.getSimpleName(), actualTypes.length);
			return Object.class;
		}

		if (!(actualTypes[index] instanceof Class)) {
			LOGGER.warn("{} does not set the actual class on superclass generic parameter", clazz.getSimpleName());
			return Object.class;
		}

		return (Class<?>) actualTypes[index];
	}

	public static Class<?> getUserClass(Object obj) {
		Validate.notNull(obj, "Object cannot be null");

		final Class<?> clazz = obj.getClass();
		if (clazz != null && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
			final Class<?> superclass = clazz.getSuperclass();
			if (superclass != null && !Object.class.equals(superclass)) {
				return superclass;
			}
		}

		return clazz;
	}

	private Reflections() {
	}
}
