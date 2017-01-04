package com.itdoes.common.core.ftp;

import org.apache.commons.net.ftp.FTPClient;

/**
 * @author Jalen Zhong
 */
public class FtpClientBuilder extends AbstractFtpClientBuilder<FTPClient> {
	public static final FtpClientBuilder INSTANCE = new FtpClientBuilder();

	private FtpClientBuilder() {
	}

	@Override
	protected FTPClient newInstance() {
		return new FTPClient();
	}
}
