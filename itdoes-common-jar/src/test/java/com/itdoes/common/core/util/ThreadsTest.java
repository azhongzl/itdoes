package com.itdoes.common.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itdoes.common.core.test.category.Unstable;
import com.itdoes.common.core.test.logback.LogbackListAppender;

/**
 * @author Jalen Zhong
 */
@Category(Unstable.class)
public class ThreadsTest {
	@Test
	public void shutdown() throws InterruptedException {
		Logger logger = LoggerFactory.getLogger("test");
		LogbackListAppender appender = new LogbackListAppender();
		appender.addToLogger("test");

		ExecutorService pool = Executors.newSingleThreadExecutor();
		Runnable task = new Task(logger, 500, 0);
		pool.execute(task);
		Threads.shutdown(pool, 1200, TimeUnit.MILLISECONDS);
		assertThat(pool.isTerminated()).isTrue();
		assertThat(appender.getFirst()).isNull();

		appender.clear();
		pool = Executors.newSingleThreadExecutor();
		task = new Task(logger, 1000, 0);
		pool.execute(task);
		Threads.shutdown(pool, 500, TimeUnit.MILLISECONDS);
		assertThat(pool.isTerminated()).isTrue();
		assertThat(appender.getFirst().getMessage()).isEqualTo(InterruptedException.class.getName());

		appender.clear();
		ExecutorService self = Executors.newSingleThreadExecutor();
		task = new Task(logger, 100000, 0);
		self.execute(task);
		CountDownLatch lock = new CountDownLatch(1);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				lock.countDown();
				Threads.shutdown(self, 200000, TimeUnit.MILLISECONDS);
			}
		});
		thread.start();
		lock.await();
		thread.interrupt();
		Threads.sleep(500);
		assertThat(appender.getFirst().getMessage()).isEqualTo(InterruptedException.class.getName());
	}

	static class Task implements Runnable {
		private final Logger logger;
		private final int sleepTime;
		private final int runTime;

		Task(Logger logger, int sleepTime, int runTime) {
			this.logger = logger;
			this.sleepTime = sleepTime;
			this.runTime = runTime;
		}

		@Override
		public void run() {
			if (runTime > 0) {
				long start = System.currentTimeMillis();
				while ((System.currentTimeMillis() - start) < runTime) {
				}
			}

			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				logger.warn(e.getClass().getName());
			}
		}
	}
}
