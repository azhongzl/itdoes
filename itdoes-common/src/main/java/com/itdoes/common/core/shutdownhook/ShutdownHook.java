package com.itdoes.common.core.shutdownhook;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ShutdownThread is a shutdown hook thread implemented as singleton that maintains a list of ShutdownCallback instances
 * that are registered with it and provides ability to stop them upon shutdown of the Java Virtual Machine.
 * 
 * @author Jalen Zhong
 */
public enum ShutdownHook implements Runnable {
	INSTANCE;

	public static interface ShutdownHookCallback {
		void shutdown();
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownHook.class);

	private final Thread thread = new Thread(this);
	private final ConcurrentMap<Object, ShutdownHookCallback> callbackMap = new ConcurrentHashMap<Object, ShutdownHookCallback>();
	private boolean hooked;

	public synchronized void register(Object key, ShutdownHookCallback callback) {
		callbackMap.putIfAbsent(key, callback);
		if (callbackMap.size() > 0) {
			hook();
		}
	}

	public synchronized void unregister(Object key) {
		callbackMap.remove(key);
		if (callbackMap.size() == 0) {
			unhook();
		}
	}

	public synchronized boolean isRegistered(Object key) {
		return callbackMap.containsKey(key);
	}

	private synchronized void hook() {
		if (!hooked) {
			Runtime.getRuntime().addShutdownHook(thread);
			hooked = true;
		}
	}

	private synchronized void unhook() {
		if (hooked) {
			Runtime.getRuntime().removeShutdownHook(thread);
			hooked = false;
		}
	}

	@Override
	public void run() {
		for (ShutdownHookCallback callback : callbackMap.values()) {
			try {
				callback.shutdown();
			} catch (Exception e) {
				LOGGER.warn("Call ShutdownCallback {} fail", callback, e);
			}
		}
	}
}
