package com.itdoes.common.ftp.factory;

import java.io.IOException;

import org.apache.commons.lang3.Validate;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itdoes.common.ftp.FtpClientInfo;

/**
 * @author Jalen Zhong
 */
public abstract class AbstractFtpClientFactory<T extends FTPClient> implements PooledObjectFactory<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFtpClientFactory.class);

	private final FtpClientInfo info;
	private final FTPClientConfig config;

	public AbstractFtpClientFactory(FtpClientInfo info, FTPClientConfig config) {
		this.info = info;
		this.config = config;
	}

	@Override
	public PooledObject<T> makeObject() throws Exception {
		final T client = newInstance();
		client.configure(config);

		Validate.notNull(info.getHost(), "Host is null");

		if (info.getPort() != null) {
			client.connect(info.getHost(), info.getPort());
		} else {
			client.connect(info.getHost());
		}

		if (!FTPReply.isPositiveCompletion(client.getReplyCode())) {
			// throw new MessagingException("Connecting to server [" + host + ":" + port + "] failed, please check the
			// connection");
		}

		LOGGER.debug("");

		if (!client.login(info.getUsername(), info.getPassword())) {
			// throw new MessagingException("Login failed. Please check the username and password.");
		}

		LOGGER.debug("");

		switch (info.getMode()) {
		case FTPClient.ACTIVE_LOCAL_DATA_CONNECTION_MODE:
			client.enterLocalActiveMode();
			break;
		case FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE:
			client.enterLocalPassiveMode();
			break;
		default:
			break;
		}

		client.setFileType(info.getFileType());

		// if (!remoteWorkingDirectory.equals(client.printWorkingDirectory())
		// && !client.changeWorkingDirectory(remoteWorkingDirectory)) {
		// throw new MessagingException("Could not change directory to '" + remoteWorkingDirectory + "'. Please
		// check the path.");
		// }

		LOGGER.debug("");

		return new DefaultPooledObject<T>(client);
	}

	@Override
	public void destroyObject(PooledObject<T> p) throws Exception {
		final T ftpClient = p.getObject();
		if (ftpClient.isConnected()) {
			try {
				try {
					ftpClient.logout();
				} catch (Exception e) {
				}
				ftpClient.disconnect();
			} catch (Exception e) {
			}
		}
	}

	@Override
	public boolean validateObject(PooledObject<T> p) {
		final FTPClient ftpClient = p.getObject();
		try {
			return ftpClient.sendNoOp();
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

	protected abstract T newInstance();
}
