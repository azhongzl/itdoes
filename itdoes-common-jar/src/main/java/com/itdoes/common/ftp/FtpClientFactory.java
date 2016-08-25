package com.itdoes.common.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @author Jalen Zhong
 */
public class FtpClientFactory<T extends FTPClient> implements PooledObjectFactory<T> {
	private final IFtpClientBuilder<T> builder;

	public FtpClientFactory(IFtpClientBuilder<T> builder) {
		this.builder = builder;
	}

	public IFtpClientBuilder<T> getBuilder() {
		return builder;
	}

	@Override
	public PooledObject<T> makeObject() throws Exception {
		final T ftp = builder.build();
		return new DefaultPooledObject<T>(ftp);
	}

	@Override
	public void destroyObject(PooledObject<T> p) throws Exception {
		final T ftp = p.getObject();
		if (ftp == null) {
			return;
		}

		FtpClients.close(ftp);
	}

	@Override
	public boolean validateObject(PooledObject<T> p) {
		final FTPClient ftp = p.getObject();
		if (ftp == null) {
			return false;
		}

		try {
			return ftp.sendNoOp();
		} catch (IOException e) {
			return false;
		}
	}

	@Override
	public void activateObject(PooledObject<T> p) throws Exception {
	}

	@Override
	public void passivateObject(PooledObject<T> p) throws Exception {
	}
}
