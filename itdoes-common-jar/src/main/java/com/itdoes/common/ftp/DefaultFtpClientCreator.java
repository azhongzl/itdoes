package com.itdoes.common.ftp;

import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itdoes.common.util.Collections3;
import com.itdoes.common.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class DefaultFtpClientCreator<T extends FTPClient> implements IFtpClientCreator<T> {
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultFtpClientCreator.class);

	public static <T extends FTPClient> DefaultFtpClientCreator<T> getInstance(IFtpClientCreator<T> instanceCreator) {
		return new DefaultFtpClientCreator<T>(instanceCreator);
	}

	private IFtpClientCreator<T> instanceCreator;

	private CopyStreamListener copyStreamListener;
	private Long controlKeepAliveTimeout;
	private Integer controlKeepAliveReplyTimeout;
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
	protected Integer clientMode = FTPClient.ACTIVE_LOCAL_DATA_CONNECTION_MODE;
	private Boolean useEpsvwithIpv4;
	private Integer bufferSize;
	private Integer dataTimeout;

	public DefaultFtpClientCreator(IFtpClientCreator<T> instanceCreator) {
		this.instanceCreator = instanceCreator;
	}

	@Override
	public T create() {
		final T ftp = instanceCreator.create();

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
				ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out), true));
			}

			if (config != null) {
				ftp.configure(config);
			}

			// Connect and login
			if (port != null && port > 0) {
				ftp.connect(host, port);
			} else {
				ftp.connect(host);
			}

			if (!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
				throw new IllegalArgumentException(
						"Connecting to server [" + host + ":" + port + "] failed, please check the connection");
			}

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Connected to server [" + host + ":" + port + "]");
			}

			if (!ftp.login(username, password)) {
				throw new IllegalArgumentException("Login failed. Please check the username and password.");
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
							"Could not change directory to '" + workingDirectory + "'. Please check the path.");
				}
			}

			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("working directory is: " + ftp.printWorkingDirectory());
			}

			if (bufferSize != null) {
				ftp.setBufferSize(bufferSize);
			}

			if (dataTimeout != null) {
				ftp.setDataTimeout(dataTimeout);
			}

			return ftp;
		} catch (Throwable t) {
			FtpClients.close(ftp);

			throw Exceptions.unchecked(t);
		}
	}
}
