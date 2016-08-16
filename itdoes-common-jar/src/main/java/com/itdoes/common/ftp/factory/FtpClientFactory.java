package com.itdoes.common.ftp.factory;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;

import com.itdoes.common.ftp.FtpClientInfo;

/**
 * @author Jalen Zhong
 */
public class FtpClientFactory extends AbstractFtpClientFactory<FTPClient> {
	public FtpClientFactory(FtpClientInfo info, FTPClientConfig config) {
		super(info, config);
	}

	@Override
	protected FTPClient newInstance() {
		return new FTPClient();
	}
}
