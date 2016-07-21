package com.itdoes.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jalen Zhong
 */
public class LogExceptionRunnable implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(LogExceptionRunnable.class);

	private final Runnable runnable;

	public LogExceptionRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void run() {
		try {
			runnable.run();
		} catch (Throwable t) {
			LOGGER.error("Unexpected error occurred in Runnable", t);
		}
	}
}
