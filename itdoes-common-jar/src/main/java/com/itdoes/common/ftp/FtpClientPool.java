package com.itdoes.common.ftp;

import java.io.InputStream;
import java.io.OutputStream;

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

	public boolean storeFile(String remote, InputStream localInput) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.storeFile(remote, localInput);
			}
		});
	}

	public boolean retrieveFile(String remote, OutputStream localOutput) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.retrieveFile(remote, localOutput);
			}
		});
	}
}
