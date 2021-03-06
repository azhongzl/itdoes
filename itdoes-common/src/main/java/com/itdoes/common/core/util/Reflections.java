package com.itdoes.common.core.util;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.google.common.collect.Lists;

/**
 * @author Jalen Zhong
 */
public class Reflections {
	private static final String GET_PREFIX = "get";
	private static final String SET_PREFIX = "set";
	private static final String CGLIB_CLASS_SEPARATOR = "$$";

	private static final Class<?>[] EMPTY_CLASSES = new Class[] {};
	private static final Object[] EMPTY_OBJECTS = new Object[] {};

	private static final char PACKAGE_SEPARATOR = '.';
	private static final char PATH_SEPARATOR = '/';

	private static final String CLASSPATH_ALL_URL_PREFIX = "classpath*:";
	private static final String ALL_CLASS_PATTERN = "**/*.class";

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
		final Method method = getMethod(obj.getClass(), methodName, paramTypes);
		return invokeMethod(obj, method, args);
	}

	public static Object invokeMethodByName(Object obj, String methodName, Object[] args) {
		final Method method = getMethodByName(obj.getClass(), methodName);
		return invokeMethod(obj, method, args);
	}

	public static Object invokeMethod(Object obj, Method method, Object[] args) {
		makeAccessible(method);
		try {
			return method.invoke(obj, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static Object getFieldValue(Object obj, String fieldName) {
		final Field field = getField(obj.getClass(), fieldName);
		return getFieldValue(obj, field);
	}

	public static Object getFieldValue(Object obj, Field field) {
		makeAccessible(field);
		try {
			return field.get(obj);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static void setFieldValue(Object obj, String fieldName, Object fieldValue) {
		final Field field = getField(obj.getClass(), fieldName);
		setFieldValue(obj, field, fieldValue);
	}

	public static void setFieldValue(Object obj, Field field, Object fieldValue) {
		makeAccessible(field);
		try {
			field.set(obj, fieldValue);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes, boolean errorIfNotFound) {
		Validate.notNull(clazz, "Class cannot be null");
		Validate.notBlank(methodName, "MethodName cannot be blank");

		for (Class<?> superclass = clazz; !Object.class.equals(superclass); superclass = superclass.getSuperclass()) {
			try {
				final Method method = superclass.getDeclaredMethod(methodName, paramTypes);
				return method;
			} catch (NoSuchMethodException e) {
			}
		}

		if (errorIfNotFound) {
			throw new IllegalArgumentException("Cannot find method [" + methodName + "] on class [" + clazz
					+ "] by param classes [" + paramTypes + "]");
		} else {
			return null;
		}
	}

	public static Method getMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
		return getMethod(clazz, methodName, paramTypes, true);
	}

	public static Method getMethodByName(Class<?> clazz, String methodName, boolean errorIfNotFound) {
		Validate.notNull(clazz, "Class cannot be null");
		Validate.notBlank(methodName, "MethodName cannot be null");

		for (Class<?> superclass = clazz; !Object.class.equals(superclass); superclass = superclass.getSuperclass()) {
			final Method[] methods = superclass.getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equals(methodName)) {
					return method;
				}
			}
		}

		if (errorIfNotFound) {
			throw new IllegalArgumentException("Cannot find method [" + methodName + "] on class [" + clazz + "]");
		} else {
			return null;
		}
	}

	public static Method getMethodByName(Class<?> clazz, String methodName) {
		return getMethodByName(clazz, methodName, true);
	}

	public static Field getField(Class<?> clazz, String fieldName, boolean errorIfNotFound) {
		Validate.notNull(clazz, "Class cannot be null");
		Validate.notBlank(fieldName, "FieldName cannot be null");

		for (Class<?> superclass = clazz; !Object.class.equals(superclass); superclass = superclass.getSuperclass()) {
			try {
				final Field field = superclass.getDeclaredField(fieldName);
				return field;
			} catch (NoSuchFieldException e) {
			}
		}

		if (errorIfNotFound) {
			throw new IllegalArgumentException("Cannot find field [" + fieldName + "] on class [" + clazz + "]");
		} else {
			return null;
		}
	}

	public static Field getField(Class<?> clazz, String fieldName) {
		return getField(clazz, fieldName, true);
	}

	public static void makeAccessible(Method method) {
		if (!method.isAccessible() && (!Modifier.isPublic(method.getModifiers())
				|| !Modifier.isPublic(method.getDeclaringClass().getModifiers()))) {
			method.setAccessible(true);
		}
	}

	public static void makeAccessible(Field field) {
		if (!field.isAccessible() && (!Modifier.isPublic(field.getModifiers())
				|| !Modifier.isPublic(field.getDeclaringClass().getModifiers())
				|| Modifier.isFinal(field.getModifiers()))) {
			field.setAccessible(true);
		}
	}

	public static Class<?> getGenericType(Class<?> clazz) {
		return getGenericType(clazz, 0);
	}

	public static Class<?> getGenericType(Class<?> clazz, int index) {
		final Type genericSuperclass = clazz.getGenericSuperclass();
		if (!(genericSuperclass instanceof ParameterizedType)) {
			LOGGER.warn("{}'s generic superclass {} is not ParameterizedType", clazz.getSimpleName(),
					genericSuperclass.getTypeName());
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

	public static List<Class<?>> getClasses(String basePackage, ClassLoader classLoader) {
		return getClasses(basePackage, ClassFilter.ALL, classLoader);
	}

	/**
	 * @see org.springframework.core.io.support.PathMatchingResourcePatternResolver#getResources(String)
	 * @see org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider#findCandidateComponents(String)
	 */
	public static List<Class<?>> getClasses(String basePackage, ClassFilter classFilter, ClassLoader classLoader) {
		final PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
		final String packageSearchPath = getPackageSearchPath(basePackage);
		final Resource[] resources;
		try {
			resources = resourcePatternResolver.getResources(packageSearchPath);
		} catch (IOException e) {
			throw new IllegalArgumentException("Error in getResources for base package: " + basePackage
					+ " from package search path: " + packageSearchPath, e);
		}

		final List<Class<?>> classes = Lists.newArrayList();
		for (Resource resource : resources) {
			final String className = getClassName(resource, basePackage);
			final Class<?> clazz;
			try {
				clazz = classLoader.loadClass(className);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("Class not found: " + className, e);
			}

			if (classFilter.isOk(clazz)) {
				classes.add(clazz);
			}
		}
		return classes;
	}

	private static String getClassName(Resource resource, String basePackage) {
		final String uri;
		try {
			uri = resource.getURI().toString();
		} catch (IOException e) {
			throw new IllegalArgumentException("Error in getURI from Resource", e);
		}

		final String dotUri = pathToPackage(uri);
		final int index = dotUri.indexOf(basePackage);
		if (index == -1) {
			throw new IllegalArgumentException(
					"getClassName failed, \"" + dotUri + "\".indexOf(\"" + basePackage + "\") == -1");
		}

		final int lastIndex = dotUri.lastIndexOf(PACKAGE_SEPARATOR);
		if (lastIndex == -1) {
			throw new IllegalArgumentException(
					"getClassName failed, \"" + dotUri + "\".lastIndexOf(\"" + PACKAGE_SEPARATOR + "\") == -1");
		}

		final String className = dotUri.substring(index, lastIndex);
		return className;
	}

	private static String getPackageSearchPath(String basePackage) {
		return CLASSPATH_ALL_URL_PREFIX + packageToPath(basePackage) + PATH_SEPARATOR + ALL_CLASS_PATTERN;
	}

	public static interface ClassFilter {
		ClassFilter ALL = new ClassFilter() {
			@Override
			public boolean isOk(Class<?> clazz) {
				return true;
			}
		};

		class SuperClassFilter implements ClassFilter {
			private final Class<?> superclass;

			public SuperClassFilter(Class<?> superclass) {
				this.superclass = superclass;
			}

			@Override
			public boolean isOk(Class<?> clazz) {
				return superclass.isAssignableFrom(clazz) && !superclass.equals(clazz);
			}
		}

		class AnnotationClassFilter implements ClassFilter {
			private final Class<? extends Annotation> annotationClass;

			public AnnotationClassFilter(Class<? extends Annotation> annotationClass) {
				this.annotationClass = annotationClass;
			}

			@Override
			public boolean isOk(Class<?> clazz) {
				return clazz.isAnnotationPresent(annotationClass);
			}
		}

		class InterfaceClassFilter implements ClassFilter {
			private final Class<?> interfaceClass;

			public InterfaceClassFilter(Class<?> interfaceClass) {
				this.interfaceClass = interfaceClass;
			}

			@Override
			public boolean isOk(Class<?> clazz) {
				return interfaceClass.isAssignableFrom(clazz) && !interfaceClass.equals(clazz);
			}
		}

		boolean isOk(Class<?> clazz);
	}

	public static <A extends Annotation> Field getFieldWithAnnotation(Class<?> clazz, Class<A> annotationClass) {
		final Field[] fields = clazz.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				if (field.isAnnotationPresent(annotationClass)) {
					return field;
				}
			}
		}
		return null;
	}

	public static <A extends Annotation> List<Field> getFieldsWithAnnotation(Class<?> clazz, Class<A> annotationClass) {
		final List<Field> result = Lists.newArrayList();
		final Field[] fields = clazz.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				if (field.isAnnotationPresent(annotationClass)) {
					result.add(field);
				}
			}
		}
		return result;
	}

	public static Object convert(String value, Class<?> toClass) {
		Validate.notBlank(value, "Value is blank");
		Validate.notNull(toClass, "ToClass is null");

		if (toClass.equals(String.class)) {
			return value;
		} else if (toClass.equals(Integer.class)) {
			return Integer.valueOf(value);
		} else if (toClass.equals(Long.class)) {
			return Long.valueOf(value);
		} else if (toClass.equals(UUID.class)) {
			return UUID.fromString(value);
		} else if (toClass.equals(Byte.class)) {
			return Byte.valueOf(value);
		} else if (toClass.equals(Short.class)) {
			return Short.valueOf(value);
		} else if (toClass.equals(Float.class)) {
			return Float.valueOf(value);
		} else if (toClass.equals(Double.class)) {
			return Double.valueOf(value);
		} else if (toClass.equals(Boolean.class)) {
			return Boolean.valueOf(value);
		} else if (toClass.equals(LocalDateTime.class)) {
			return LocalDateTime.parse(value);
		} else if (toClass.equals(LocalDate.class)) {
			return LocalDate.parse(value);
		} else if (toClass.equals(LocalTime.class)) {
			return LocalTime.parse(value);
		}

		throw new IllegalArgumentException("Class [" + toClass + "] is not supported for converting");
	}

	public static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw Exceptions.unchecked(e, IllegalArgumentException.class);
		}
	}

	public static <T> T newInstance(Class<T> clazz, Class<?>[] paramClasses, Object[] args) {
		try {
			return clazz.getConstructor(paramClasses).newInstance(args);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw Exceptions.unchecked(e, IllegalArgumentException.class);
		}
	}

	public static String packageToPath(String packageName) {
		Validate.notBlank(packageName, "Package is blank");

		return packageName.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
	}

	public static String packageToPath(Class<?> clazz) {
		Validate.notNull(clazz, "Class is null");

		return packageToPath(clazz.getPackage().getName());
	}

	public static String pathToPackage(String path) {
		Validate.notBlank(path, "Path is blank");

		return path.replace(PATH_SEPARATOR, PACKAGE_SEPARATOR);
	}

	private Reflections() {
	}
}
