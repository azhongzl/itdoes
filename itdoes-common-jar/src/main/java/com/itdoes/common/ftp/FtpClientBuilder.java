package com.itdoes.common.ftp;

import org.apache.commons.net.ftp.FTPClient;

/**
 * @author Jalen Zhong
 */
public class FtpClientBuilder extends AbstractFtpClientBuilder<FTPClient> {
	private static final FtpClientBuilder INSTANCE = new FtpClientBuilder();

	public static FtpClientBuilder getInstance() {
		return INSTANCE;
	}

	private FtpClientBuilder() {
	}

	@Override
	protected FTPClient newInstance() {
		return new FTPClient();
	}
}