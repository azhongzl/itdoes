package com.itdoes.common.pool;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.pool2.impl.BaseGenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jalen Zhong
 */
public class ShutdownThread extends Thread {
	private static final ShutdownThread INSTANCE = new ShutdownThread();

	private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownThread.class);

	public static ShutdownThread getInstance() {
		return INSTANCE;
	}

	private final List<BaseGenericObjectPool<?>> poolList = new CopyOnWriteArrayList<BaseGenericObjectPool<?>>();
	private boolean hooked;

	public synchronized void register(ExecutorPool<?>... pools) {
		poolList.addAll(Arrays.asList(pools));
		if (poolList.size() > 0) {
			hook();
		}
	}

	public synchronized void register(int index, ExecutorPool<?>... pools) {
		poolList.addAll(index, Arrays.asList(pools));
		if (poolList.size() > 0) {
			hook();
		}
	}

	public synchronized void unregister(ExecutorPool<?> pool) {
		poolList.remove(pool);
		if (poolList.size() == 0) {
			unhook();
		}
	}

	public synchronized boolean isRegistered(ExecutorPool<?> pool) {
		return poolList.contains(pool);
	}

	private synchronized void hook() {
		try {
			if (!hooked) {
				Runtime.getRuntime().addShutdownHook(this);
				hooked = true;
			}
		} catch (Exception e) {
			LOGGER.warn("addShutdownHook() fail", e);
		}
	}

	private synchronized void unhook() {
		try {
			if (hooked) {
				Runtime.getRuntime().removeShutdownHook(this);
				hooked = false;
			}
		} catch (Exception e) {
			LOGGER.warn("removeShutdownHook() fail", e);
		}
	}

	@Override
	public void run() {
		for (BaseGenericObjectPool<?> pool : poolList) {
			try {
				if (!pool.isClosed()) {
					pool.close();
					LOGGER.debug("Pool {} closed", pool);
				}
			} catch (Exception e) {
				LOGGER.warn("Close ExecutorPool fail", e);
			}
		}
	}
}
