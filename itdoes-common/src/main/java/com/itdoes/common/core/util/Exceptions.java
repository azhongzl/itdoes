package com.itdoes.common.core.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Jalen Zhong
 */
public class Exceptions {
	public static RuntimeException unchecked(Throwable t) {
		return unchecked(t, null);
	}

	public static RuntimeException unchecked(Throwable t, Class<? extends RuntimeException> expectedExceptionClass) {
		if (t instanceof InvocationTargetException) {
			return unchecked(((InvocationTargetException) t).getTargetException(), expectedExceptionClass);
		}

		if (t instanceof RuntimeException) {
			return (RuntimeException) t;
		} else if (expectedExceptionClass != null) {
			try {
				return expectedExceptionClass.getConstructor(Throwable.class).newInstance(t);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// Continue
			}
		}

		return new RuntimeException(t);
	}

	public static String getStackTraceString(Throwable t) {
		final StringWriter stringWriter = new StringWriter();
		t.printStackTrace(new PrintWriter(stringWriter));
		return stringWriter.toString();
	}

	public static String getErrorMessageWithNestedException(Throwable t) {
		final Throwable nestedException = t.getCause();
		if (nestedException == null) {
			return t.getMessage();
		}

		return new StringBuilder().append(t.getMessage()).append(" nested exception is ")
				.append(nestedException.getClass().getName()).append(":").append(nestedException.getMessage())
				.toString();
	}

	public static Throwable getRootCause(Throwable t) {
		Throwable cause;
		while ((cause = t.getCause()) != null) {
			t = cause;
		}
		return t;
	}

	@SuppressWarnings("unchecked")
	public static boolean isCausedBy(Throwable t, Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = t;
		while (cause != null) {
			for (Class<? extends Exception> causeExceptionClass : causeExceptionClasses) {
				if (causeExceptionClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}

	private Exceptions() {
	}
}
