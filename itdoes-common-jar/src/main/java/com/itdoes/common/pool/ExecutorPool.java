package com.itdoes.common.pool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.itdoes.common.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class ExecutorPool<T> extends GenericObjectPool<T> {
	private boolean stopAtShutdown;

	public interface PoolCaller<T, V> {
		V call(T t) throws Exception;
	}

	public interface PoolRunner<T> {
		void run(T t) throws Exception;
	}

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

	public void setStopAtShutdown(boolean stop) {
		if (stop) {
			if (!stopAtShutdown) {
				if (!isClosed()) {
					ShutdownThread.getInstance().register(this);
				}
			}
		} else {
			if (stopAtShutdown) {
				ShutdownThread.getInstance().unregister(this);
			}
		}
		stopAtShutdown = stop;
	}
}
