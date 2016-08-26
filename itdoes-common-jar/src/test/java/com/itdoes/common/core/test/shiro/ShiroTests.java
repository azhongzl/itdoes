package com.itdoes.common.core.test.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.SubjectThreadState;
import org.apache.shiro.util.ThreadState;
import org.mockito.Mockito;

/**
 * @author Jalen Zhong
 */
public class ShiroTests {
	private static ThreadState threadState;

	public static void pushSubject(Object principal) {
		final Subject subject = Mockito.mock(Subject.class);
		Mockito.when(subject.isAuthenticated()).thenReturn(true);
		Mockito.when(subject.getPrincipal()).thenReturn(principal);

		popSubject();
		threadState = new SubjectThreadState(subject);
		threadState.bind();
	}

	public static void popSubject() {
		if (threadState != null) {
			threadState.clear();
			threadState = null;
		}
	}
}
