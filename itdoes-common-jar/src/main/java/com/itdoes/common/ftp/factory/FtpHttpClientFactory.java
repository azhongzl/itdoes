package com.itdoes.common.ftp.factory;

import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPHTTPClient;

import com.itdoes.common.ftp.FtpClientInfo;

/**
 * @author Jalen Zhong
 */
public class FtpHttpClientFactory extends AbstractFtpClientFactory<FTPHTTPClient> {
	private final String proxyHost;
	private final int proxyPort;
	private final String proxyUser;
	private final String proxyPass;

	public FtpHttpClientFactory(FtpClientInfo info, FTPClientConfig config, String proxyHost, int proxyPort,
			String proxyUser, String proxyPass) {
		super(info, config);

		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		this.proxyUser = proxyUser;
		this.proxyPass = proxyPass;
	}

	@Override
	protected FTPHTTPClient newInstance() {
		return new FTPHTTPClient(proxyHost, proxyPort, proxyUser, proxyPass);
	}
}
