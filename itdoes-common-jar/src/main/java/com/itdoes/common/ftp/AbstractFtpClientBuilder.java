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
public abstract class AbstractFtpClientBuilder<T extends FTPClient> implements IFtpClientBuilder<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFtpClientBuilder.class);

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

	@Override
	public final T build() {
		return buildInternal(newInstance());
	}

	public AbstractFtpClientBuilder<T> setCopyStreamListener(CopyStreamListener copyStreamListener) {
		this.copyStreamListener = copyStreamListener;
		return this;
	}

	public AbstractFtpClientBuilder<T> setControlKeepAliveTimeout(Long controlKeepAliveTimeout) {
		this.controlKeepAliveTimeout = controlKeepAliveTimeout;
		return this;
	}

	public AbstractFtpClientBuilder<T> setControlKeepAliveReplyTimeout(Integer controlKeepAliveReplyTimeout) {
		this.controlKeepAliveReplyTimeout = controlKeepAliveReplyTimeout;
		return this;
	}

	public AbstractFtpClientBuilder<T> setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}

	public AbstractFtpClientBuilder<T> setDefaultTimeout(Integer defaultTimeout) {
		this.defaultTimeout = defaultTimeout;
		return this;
	}

	public AbstractFtpClientBuilder<T> setSoTimeout(Integer soTimeout) {
		this.soTimeout = soTimeout;
		return this;
	}

	public AbstractFtpClientBuilder<T> setControlEncoding(String controlEncoding) {
		this.controlEncoding = controlEncoding;
		return this;
	}

	public AbstractFtpClientBuilder<T> setListHiddenFiles(Boolean listHiddenFiles) {
		ListHiddenFiles = listHiddenFiles;
		return this;
	}

	public AbstractFtpClientBuilder<T> setProtocolCommandListeners(
			List<ProtocolCommandListener> protocolCommandListeners) {
		this.protocolCommandListeners = protocolCommandListeners;
		return this;
	}

	public AbstractFtpClientBuilder<T> addProtocolCommandListener(ProtocolCommandListener protocolCommandListener) {
		if (protocolCommandListeners == null) {
			protocolCommandListeners = Lists.newArrayList();
		}

		protocolCommandListeners.add(protocolCommandListener);
		return this;
	}

	public AbstractFtpClientBuilder<T> setConfig(FTPClientConfig config) {
		this.config = config;
		return this;
	}

	public AbstractFtpClientBuilder<T> setHost(String host) {
		this.host = host;
		return this;
	}

	public AbstractFtpClientBuilder<T> setPort(Integer port) {
		this.port = port;
		return this;
	}

	public AbstractFtpClientBuilder<T> setUsername(String username) {
		this.username = username;
		return this;
	}

	public AbstractFtpClientBuilder<T> setPassword(String password) {
		this.password = password;
		return this;
	}

	public AbstractFtpClientBuilder<T> setFileType(Integer fileType) {
		this.fileType = fileType;
		return this;
	}

	public AbstractFtpClientBuilder<T> setFileTypeAscii() {
		return setFileType(FTPClient.ASCII_FILE_TYPE);
	}

	public AbstractFtpClientBuilder<T> setFileTypeEbcdic() {
		return setFileType(FTPClient.EBCDIC_FILE_TYPE);
	}

	public AbstractFtpClientBuilder<T> setFileTypeBinary() {
		return setFileType(FTPClient.BINARY_FILE_TYPE);
	}

	public AbstractFtpClientBuilder<T> setFileTypeLocal() {
		return setFileType(FTPClient.LOCAL_FILE_TYPE);
	}

	public AbstractFtpClientBuilder<T> setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
		return this;
	}

	public AbstractFtpClientBuilder<T> setClientMode(Integer clientMode) {
		this.clientMode = clientMode;
		return this;
	}

	public AbstractFtpClientBuilder<T> setUseEpsvwithIpv4(Boolean useEpsvwithIpv4) {
		this.useEpsvwithIpv4 = useEpsvwithIpv4;
		return this;
	}

	public AbstractFtpClientBuilder<T> setDataTimeout(Integer dataTimeout) {
		this.dataTimeout = dataTimeout;
		return this;
	}

	public AbstractFtpClientBuilder<T> setBufferSize(Integer bufferSize) {
		this.bufferSize = bufferSize;
		return this;
	}

	protected T buildInternal(T ftp) {
		try {
			// Configuration
			if (copyStreamListener != null) {
				ftp.setCopyStreamListener(copyStreamListener);
			}

			if (controlKeepAliveTimeout != null && controlKeepAliveTimeout >= 0) {
				ftp.setControlKeepAliveTimeout(controlKeepAliveTimeout);
			}

			if (controlKeepAliveReplyTimeout != null && controlKeepAliveReplyTimeout >= 0) {
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

	protected abstract T newInstance();
}
