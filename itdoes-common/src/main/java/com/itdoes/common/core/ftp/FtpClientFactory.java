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
		LOGGER.info("makeObject()");

		final T ftp = builder.build();
		return new DefaultPooledObject<T>(ftp);
	}

	@Override
	public void destroyObject(PooledObject<T> p) throws Exception {
		LOGGER.info("destroyObject()");

		final T ftp = p.getObject();
		if (ftp == null) {
			return;
		}

		FtpClients.close(ftp);
	}

	@Override
	public boolean validateObject(PooledObject<T> p) {
		LOGGER.info("validateObject()");

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
			LOGGER.info("validateObject() = true");
		} else {
			LOGGER.warn("validateObject() = false");
		}
		return valid;
	}

	@Override
	public void activateObject(PooledObject<T> p) throws Exception {
		LOGGER.info("activateObject()");
	}

	@Override
	public void passivateObject(PooledObject<T> p) throws Exception {
		LOGGER.info("passivateObject()");
	}
}
