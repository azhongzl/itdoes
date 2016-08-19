package com.itdoes.common.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.itdoes.common.pool.ExecutorPool;

/**
 * @author Jalen Zhong
 */
public class FtpClientPool<T extends FTPClient> extends ExecutorPool<T> {
	public FtpClientPool(PooledObjectFactory<T> factory, GenericObjectPoolConfig config) {
		super(factory, config);
	}
}
