package com.itdoes.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * @author Jalen Zhong
 */
public class Threads {
	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public static void sleep(long duration, TimeUnit unit) {
		sleep(unit.toMillis(duration));
	}

	public static ThreadFactory buildThreadFactory(String nameFormat) {
		return new ThreadFactoryBuilder().setNameFormat(nameFormat).build();
	}

	public static ThreadFactory buildThreadFactory(String nameFormat, boolean daemon) {
		return new ThreadFactoryBuilder().setNameFormat(nameFormat).setDaemon(daemon).build();
	}

	public static boolean shutdown(ExecutorService pool, int timeout, TimeUnit unit) {
		return MoreExecutors.shutdownAndAwaitTermination(pool, timeout, unit);
	}

	private Threads() {
	}
}
