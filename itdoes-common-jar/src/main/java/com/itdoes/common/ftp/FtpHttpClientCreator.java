package com.itdoes.common.ftp;

import org.apache.commons.lang3.Validate;
import org.apache.commons.net.ftp.FTPHTTPClient;

/**
 * @author Jalen Zhong
 */
public class FtpHttpClientCreator implements IFtpClientCreator<FTPHTTPClient> {
	public static FtpHttpClientCreator getInstance() {
		return new FtpHttpClientCreator();
	}

	private String proxyHost;
	private int proxyPort;
	private String proxyUsername;
	private String proxyPassword;

	private FtpHttpClientCreator() {
	}

	@Override
	public FTPHTTPClient create() {
		Validate.notNull(proxyHost, "ProxyHost is null");
		Validate.isTrue(proxyPort > 0, "ProxyPort [" + proxyPort + "] is not positive");

		if (proxyUsername == null || proxyPassword == null) {
			return new FTPHTTPClient(proxyHost, proxyPort);
		} else {
			return new FTPHTTPClient(proxyHost, proxyPort, proxyUsername, proxyPassword);
		}
	}

	public FtpHttpClientCreator setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
		return this;
	}

	public FtpHttpClientCreator setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
		return this;
	}

	public FtpHttpClientCreator setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
		return this;
	}

	public FtpHttpClientCreator setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
		return this;
	}
}
