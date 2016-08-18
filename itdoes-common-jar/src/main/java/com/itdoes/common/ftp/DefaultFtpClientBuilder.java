package com.itdoes.common.ftp;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.itdoes.common.util.Collections3;
import com.itdoes.common.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class DefaultFtpClientBuilder<T extends FTPClient> implements IFtpClientBuilder<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFtpClientBuilder.class);

	public static <T extends FTPClient> DefaultFtpClientBuilder<T> getInstance(IFtpClientBuilder<T> instanceCreator) {
		return new DefaultFtpClientBuilder<T>(instanceCreator);
	}

	private IFtpClientBuilder<T> instanceBuilder;

	private CopyStreamListener copyStreamListener;
	private Long controlKeepAliveTimeout;
	private Integer controlKeepAliveReplyTimeout;
	private Integer connectTimeout;
	private Integer defaultTimeout;
	private Integer soTimeout;
	private String controlEncoding;
	private Boolean ListHiddenFiles;
	private List<ProtocolCommandListener> protocolCommandListeners;
	private FTPClientConfig config;
	private String host;
	private Integer port;
	private String username;
	private String password;
	private Integer fileType;
	private String workingDirectory;
	private Integer clientMode;
	private Boolean useEpsvwithIpv4;
	private Integer dataTimeout;
	private Integer bufferSize;

	public DefaultFtpClientBuilder(IFtpClientBuilder<T> instanceBuilder) {
		this.instanceBuilder = instanceBuilder;
	}

	@Override
	public T build() {
		final T ftp = instanceBuilder.build();

		try {
			// Configuration
			if (copyStreamListener != null) {
				ftp.setCopyStreamListener(copyStreamListener);
			}

			if (controlKeepAliveTimeout != null && controlKeepAliveTimeout >= 0) {
				ftp.setControlKeepAliveTimeout(controlKeepAliveTimeout);
			}

			if (controlKeepAliveReplyTimeout != null & controlKeepAliveReplyTimeout >= 0) {
				ftp.setControlKeepAliveReplyTimeout(controlKeepAliveReplyTimeout);
			}

			if (connectTimeout != null && connectTimeout >= 0) {
				ftp.setConnectTimeout(connectTimeout);
			}

			if (defaultTimeout != null && defaultTimeout >= 0) {
				ftp.setDefaultTimeout(defaultTimeout);
			}

			if (soTimeout != null && soTimeout >= 0) {
				ftp.setSoTimeout(soTimeout);
			}

			if (controlEncoding != null) {
				ftp.setControlEncoding(controlEncoding);
			}

			if (ListHiddenFiles != null) {
				ftp.setListHiddenFiles(ListHiddenFiles);
			}

			if (!Collections3.isEmpty(protocolCommandListeners)) {
				for (ProtocolCommandListener protocolCommandListener : protocolCommandListeners) {
					ftp.addProtocolCommandListener(protocolCommandListener);
				}
			}

			if (config != null) {
				ftp.configure(config);
			}

			// Connect and login
			Validate.notBlank(host, "Host is blank");
			if (port != null && port > 0) {
				ftp.connect(host, port);
			} else {
				ftp.connect(host);
			}

			if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
				throw new IllegalArgumentException(
						"Connecting to server [" + host + ":" + port + "] failed. Please check the connection");
			}

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Connected to server [" + host + ":" + port + "]");
			}

			Validate.notBlank(username, "Username is blank");
			Validate.notNull(password, "Password is null");
			if (!ftp.login(username, password)) {
				throw new IllegalArgumentException("Login failed. Please check the username and password");
			}

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("login successful");
			}

			if (fileType != null) {
				ftp.setFileType(this.fileType);
			}

			if (clientMode != null) {
				switch (clientMode) {
				case FTPClient.ACTIVE_LOCAL_DATA_CONNECTION_MODE:
					ftp.enterLocalActiveMode();
					break;
				case FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE:
					ftp.enterLocalPassiveMode();
					break;
				default:
					break;
				}
			}

			if (useEpsvwithIpv4 != null) {
				ftp.setUseEPSVwithIPv4(useEpsvwithIpv4);
			}

			if (workingDirectory != null) {
				if (!workingDirectory.equals(ftp.printWorkingDirectory())
						&& !ftp.changeWorkingDirectory(workingDirectory)) {
					throw new IllegalArgumentException(
							"Could not change directory to '" + workingDirectory + "'. Please check the path");
				}
			}

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("working directory is: " + ftp.printWorkingDirectory());
			}

			if (dataTimeout != null && dataTimeout >= 0) {
				ftp.setDataTimeout(dataTimeout);
			}

			if (bufferSize != null && bufferSize >= 0) {
				ftp.setBufferSize(bufferSize);
			}

			return ftp;
		} catch (Throwable t) {
			FtpClients.close(ftp);

			throw Exceptions.unchecked(t);
		}
	}

	public DefaultFtpClientBuilder<T> setCopyStreamListener(CopyStreamListener copyStreamListener) {
		this.copyStreamListener = copyStreamListener;
		return this;
	}

	public DefaultFtpClientBuilder<T> setControlKeepAliveTimeout(Long controlKeepAliveTimeout) {
		this.controlKeepAliveTimeout = controlKeepAliveTimeout;
		return this;
	}

	public DefaultFtpClientBuilder<T> setControlKeepAliveReplyTimeout(Integer controlKeepAliveReplyTimeout) {
		this.controlKeepAliveReplyTimeout = controlKeepAliveReplyTimeout;
		return this;
	}

	public DefaultFtpClientBuilder<T> setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}

	public DefaultFtpClientBuilder<T> setDefaultTimeout(Integer defaultTimeout) {
		this.defaultTimeout = defaultTimeout;
		return this;
	}

	public DefaultFtpClientBuilder<T> setSoTimeout(Integer soTimeout) {
		this.soTimeout = soTimeout;
		return this;
	}

	public DefaultFtpClientBuilder<T> setControlEncoding(String controlEncoding) {
		this.controlEncoding = controlEncoding;
		return this;
	}

	public DefaultFtpClientBuilder<T> setListHiddenFiles(Boolean listHiddenFiles) {
		ListHiddenFiles = listHiddenFiles;
		return this;
	}

	public DefaultFtpClientBuilder<T> setProtocolCommandListeners(
			List<ProtocolCommandListener> protocolCommandListeners) {
		this.protocolCommandListeners = protocolCommandListeners;
		return this;
	}

	public DefaultFtpClientBuilder<T> addProtocolCommandListener(ProtocolCommandListener protocolCommandListener) {
		if (protocolCommandListeners == null) {
			protocolCommandListeners = Lists.newArrayList();
		}

		protocolCommandListeners.add(protocolCommandListener);
		return this;
	}

	public DefaultFtpClientBuilder<T> setConfig(FTPClientConfig config) {
		this.config = config;
		return this;
	}

	public DefaultFtpClientBuilder<T> setHost(String host) {
		this.host = host;
		return this;
	}

	public DefaultFtpClientBuilder<T> setPort(Integer port) {
		this.port = port;
		return this;
	}

	public DefaultFtpClientBuilder<T> setUsername(String username) {
		this.username = username;
		return this;
	}

	public DefaultFtpClientBuilder<T> setPassword(String password) {
		this.password = password;
		return this;
	}

	public DefaultFtpClientBuilder<T> setFileType(Integer fileType) {
		this.fileType = fileType;
		return this;
	}

	public DefaultFtpClientBuilder<T> setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
		return this;
	}

	public DefaultFtpClientBuilder<T> setClientMode(Integer clientMode) {
		this.clientMode = clientMode;
		return this;
	}

	public DefaultFtpClientBuilder<T> setUseEpsvwithIpv4(Boolean useEpsvwithIpv4) {
		this.useEpsvwithIpv4 = useEpsvwithIpv4;
		return this;
	}

	public DefaultFtpClientBuilder<T> setDataTimeout(Integer dataTimeout) {
		this.dataTimeout = dataTimeout;
		return this;
	}

	public DefaultFtpClientBuilder<T> setBufferSize(Integer bufferSize) {
		this.bufferSize = bufferSize;
		return this;
	}
}
