package com.itdoes.common.core.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Jalen Zhong
 */
public class FtpClientFactory<T extends FTPClient> implements PooledObjectFactory<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(FtpClientFactory.class);

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
		LOGGER.info("makeObject()");
		return new DefaultPooledObject<T>(ftp);
	}

	@Override
	public void destroyObject(PooledObject<T> p) throws Exception {
		final T ftp = p.getObject();
		if (ftp == null) {
			return;
		}

		LOGGER.info("destroyObject()");
		FtpClients.close(ftp);
	}

	@Override
	public boolean validateObject(PooledObject<T> p) {
		final FTPClient ftp = p.getObject();
		if (ftp == null) {
			return false;
		}

		boolean valid;
		try {
			valid = ftp.sendNoOp();
		} catch (IOException e) {
			valid = false;
		}
		if (valid) {
			LOGGER.debug("validateObject() = true");
		} else {
			LOGGER.warn("validateObject() = false");
		}
		return valid;
	}

	@Override
	public void activateObject(PooledObject<T> p) throws Exception {
	}

	@Override
	public void passivateObject(PooledObject<T> p) throws Exception {
	}
}
