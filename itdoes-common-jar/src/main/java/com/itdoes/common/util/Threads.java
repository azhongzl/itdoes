package com.itdoes.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * @author Jalen Zhong
 */
public class Threads {
	private static final Logger LOGGER = LoggerFactory.getLogger(Threads.class);

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

	public static void shutdownThenNow(ExecutorService pool, int shutdownTimeout, int shutdownNowTimeout,
			TimeUnit unit) {
		pool.shutdown();
		try {
			if (!pool.awaitTermination(shutdownTimeout, unit)) {
				pool.shutdownNow();
				if (!pool.awaitTermination(shutdownNowTimeout, unit)) {
					LOGGER.warn("ExecutorService cannot shutdown in {}s and shutdownNow in {}s",
							unit.toSeconds(shutdownTimeout), unit.toSeconds(shutdownNowTimeout));
				}
			}
		} catch (InterruptedException e) {
			pool.shutdownNow();

			Thread.currentThread().interrupt();
		}
	}

	public static void shutdownNow(ExecutorService pool, int timeout, TimeUnit unit) {
		pool.shutdownNow();
		try {
			if (!pool.awaitTermination(timeout, unit)) {
				LOGGER.warn("ExecutorService cannot shutdownNow in {}s", unit.toSeconds(timeout));
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private Threads() {
	}
}
