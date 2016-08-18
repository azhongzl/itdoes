package com.itdoes.common.ftp;

import org.apache.commons.lang3.Validate;
import org.apache.commons.net.ftp.FTPHTTPClient;

/**
 * @author Jalen Zhong
 */
public class FtpHttpClientBuilder implements IFtpClientBuilder<FTPHTTPClient> {
	public static FtpHttpClientBuilder getInstance() {
		return new FtpHttpClientBuilder();
	}

	private String proxyHost;
	private int proxyPort;
	private String proxyUsername;
	private String proxyPassword;

	private FtpHttpClientBuilder() {
	}

	@Override
	public FTPHTTPClient build() {
		Validate.notNull(proxyHost, "ProxyHost is null");
		Validate.isTrue(proxyPort > 0, "ProxyPort [" + proxyPort + "] is not positive");

		if (proxyUsername == null || proxyPassword == null) {
			return new FTPHTTPClient(proxyHost, proxyPort);
		} else {
			return new FTPHTTPClient(proxyHost, proxyPort, proxyUsername, proxyPassword);
		}
	}

	public FtpHttpClientBuilder setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
		return this;
	}

	public FtpHttpClientBuilder setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
		return this;
	}

	public FtpHttpClientBuilder setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
		return this;
	}

	public FtpHttpClientBuilder setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
		return this;
	}
}
