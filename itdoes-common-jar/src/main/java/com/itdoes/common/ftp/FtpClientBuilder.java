package com.itdoes.common.ftp;

import org.apache.commons.net.ftp.FTPClient;

/**
 * @author Jalen Zhong
 */
public class FtpClientBuilder implements IFtpClientBuilder<FTPClient> {
	private static final FtpClientBuilder INSTANCE = new FtpClientBuilder();

	public static FtpClientBuilder getInstance() {
		return INSTANCE;
	}

	private FtpClientBuilder() {
	}

	@Override
	public FTPClient build() {
		return new FTPClient();
	}
}
