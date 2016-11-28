package com.itdoes.common.core.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.itdoes.common.core.shutdownhook.ShutdownHookThread;
import com.itdoes.common.core.shutdownhook.ShutdownHookThread.ShutdownHookCallback;
import com.itdoes.common.core.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class ExecutorPool<T> extends GenericObjectPool<T> {
	private static class PoolShutdownHookCallback<T> implements ShutdownHookCallback {
		private final ExecutorPool<T> pool;

		public PoolShutdownHookCallback(ExecutorPool<T> pool) {
			this.pool = pool;
		}

		@Override
		public void shutdown() {
			if (!pool.isClosed()) {
				pool.close();
			}
		}
	}

	public interface PoolCaller<T, V> {
		V call(T t) throws Exception;
	}

	public interface PoolRunner<T> {
		void run(T t) throws Exception;
	}

	private boolean stopAtShutdown;

	public ExecutorPool(PooledObjectFactory<T> factory, GenericObjectPoolConfig config) {
		super(factory, config);
	}

	public <V> V execute(PoolCaller<T, V> caller, long borrowMaxWaitMillis) {
		T t = null;
		try {
			t = borrowObject(borrowMaxWaitMillis);
			return caller.call(t);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		} finally {
			if (t != null) {
				returnObject(t);
			}
		}
	}

	public <V> V execute(PoolCaller<T, V> caller) {
		return execute(caller, getMaxWaitMillis());
	}

	public void execute(PoolRunner<T> runner, long borrowMaxWaitMillis) {
		T t = null;
		try {
			t = borrowObject(borrowMaxWaitMillis);
			runner.run(t);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		} finally {
			if (t != null) {
				returnObject(t);
			}
		}
	}

	public void execute(PoolRunner<T> runner) {
		execute(runner, getMaxWaitMillis());
	}

	/**
	 * Set stop server at shutdown behaviour.
	 * 
	 * @param stop
	 *            If true, this server instance will be explicitly stopped when the JVM is shutdown. Otherwise the JVM
	 *            is stopped with the server running.
	 * @see Runtime#addShutdownHook(Thread)
	 * @see ShutdownHookThread
	 */
	public void setStopAtShutdown(boolean stop) {
		// if we now want to stop
		if (stop) {
			// and we weren't stopping before
			if (!stopAtShutdown) {
				// only register to stop if we're already started
				if (!isClosed()) {
					ShutdownHookThread.getInstance().register(this, new PoolShutdownHookCallback<T>(this));
				}
			}
		} else {
			if (stopAtShutdown) {
				ShutdownHookThread.getInstance().unregister(this);
			}
		}
		stopAtShutdown = stop;
	}
}
