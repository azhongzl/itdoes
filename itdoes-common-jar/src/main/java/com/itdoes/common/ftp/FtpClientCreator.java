package com.itdoes.common.ftp;

import org.apache.commons.net.ftp.FTPClient;

/**
 * @author Jalen Zhong
 */
public class FtpClientCreator implements IFtpClientCreator<FTPClient> {
	private static final FtpClientCreator INSTANCE = new FtpClientCreator();

	public static FtpClientCreator getInstance() {
		return INSTANCE;
	}

	private FtpClientCreator() {
	}

	@Override
	public FTPClient create() {
		return new FTPClient();
	}
}
