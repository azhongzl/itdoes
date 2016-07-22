package com.itdoes.common.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

/**
 * @author Jalen Zhong
 */
public class ExceptionsTest {
	@Test
	public void unchecked() {
		final Exception e = new Exception("My Exception");
		final RuntimeException re = Exceptions.unchecked(e);
		assertThat(re.getCause()).isEqualTo(e);

		final RuntimeException re2 = Exceptions.unchecked(re);
		assertThat(re2).isSameAs(re);

		final InvocationTargetException ite = new InvocationTargetException(re);
		final RuntimeException re3 = Exceptions.unchecked(ite);
		assertThat(re3).isEqualTo(re);
	}

	@Test
	public void getStackTraceString() {
		final Exception e = new Exception("My Exception");
		final RuntimeException re = new RuntimeException(e);
		Exceptions.getStackTraceString(re);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void isCausedBy() {
		final IOException ioe = new IOException("My IOException");
		final IllegalStateException ise = new IllegalStateException(ioe);
		final RuntimeException re = new RuntimeException(ise);

		assertThat(Exceptions.isCausedBy(re, IOException.class)).isTrue();
		assertThat(Exceptions.isCausedBy(re, IllegalStateException.class, IOException.class)).isTrue();
		assertThat(Exceptions.isCausedBy(re, Exception.class)).isTrue();
		assertThat(Exceptions.isCausedBy(re, IllegalAccessException.class)).isFalse();
	}
}
