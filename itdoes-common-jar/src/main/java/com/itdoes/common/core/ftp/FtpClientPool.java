package com.itdoes.common.core.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.itdoes.common.core.pool.ExecutorPool;

/**
 * @author Jalen Zhong
 */
public class FtpClientPool<T extends FTPClient> extends ExecutorPool<T> {
	public FtpClientPool(PooledObjectFactory<T> factory, GenericObjectPoolConfig config) {
		super(factory, config);
	}

	public FtpClientFactory<T> getFactory() {
		return (FtpClientFactory<T>) super.getFactory();
	}
}
