package com.itdoes.common.ftp.factory;

import javax.net.ssl.TrustManager;

import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPSClient;

import com.itdoes.common.ftp.FtpClientInfo;

/**
 * @author Jalen Zhong
 */
public class FtpsClientFactory extends AbstractFtpClientFactory<FTPSClient> {
	private final String protocol;
	private final Boolean isImplicit;
	private final TrustManager trustManager;

	public FtpsClientFactory(FtpClientInfo info, FTPClientConfig config, String protocol, Boolean isImplicit,
			TrustManager trustManager) {
		super(info, config);

		this.protocol = protocol;
		this.isImplicit = isImplicit;
		this.trustManager = trustManager;
	}

	@Override
	protected FTPSClient newInstance() {
		final FTPSClient client;

		if (protocol == null) {
			if (isImplicit == null) {
				client = new FTPSClient();
			} else {
				client = new FTPSClient(isImplicit);
			}
		} else {
			if (isImplicit == null) {
				client = new FTPSClient(protocol);
			} else {
				client = new FTPSClient(protocol, isImplicit);
			}
		}

		if (trustManager != null) {
			client.setTrustManager(trustManager);
		}

		return client;
	}
}
