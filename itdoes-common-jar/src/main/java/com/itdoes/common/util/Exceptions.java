package com.itdoes.common.util;

/**
 * @author Jalen Zhong
 */
public class Exceptions {
	public static RuntimeException unchecked(Throwable t) {
		if (t instanceof RuntimeException) {
			return (RuntimeException) t;
		} else {
			return new RuntimeException(t);
		}
	}

    public static String getStackTraceString(Throwable t){
        final StringWriter stringWriter = new StringWriter();
        t.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

	private Exceptions() {
	}
}
