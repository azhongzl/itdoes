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
public class ShutdownThread extends Thread {
	public static interface ShutdownCallback {
		void shutdown();
	}

	private static final ShutdownThread INSTANCE = new ShutdownThread();

	private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownThread.class);

	public static ShutdownThread getInstance() {
		return INSTANCE;
	}

	private final ConcurrentMap<Object, ShutdownCallback> callbackMap = new ConcurrentHashMap<Object, ShutdownCallback>();
	private boolean hooked;

	public synchronized void register(Object key, ShutdownCallback callback) {
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
			Runtime.getRuntime().addShutdownHook(this);
			hooked = true;
		}
	}

	private synchronized void unhook() {
		if (hooked) {
			Runtime.getRuntime().removeShutdownHook(this);
			hooked = false;
		}
	}

	@Override
	public void run() {
		for (ShutdownCallback callback : callbackMap.values()) {
			try {
				callback.shutdown();
			} catch (Exception e) {
				LOGGER.warn("Call ShutdownCallback {} fail", callback, e);
			}
		}
	}
}
