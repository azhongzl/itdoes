package com.itdoes.common.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author Jalen Zhong
 */
public class Ftps<T extends FTPClient> {
	public static FtpClientPool<FTPClient> createFtpClientPool(String host, int port, String username,
			String password) {
		return createFtpClientPool(DefaultFtpClientCreator.getInstance(FtpClientCreator.getInstance()).setHost(host)
				.setPort(port).setUsername(username).setPassword(password), createDefaultPoolConfig());
	}

	public static <T extends FTPClient> FtpClientPool<T> createFtpClientPool(IFtpClientCreator<T> ftpClientCreator,
			GenericObjectPoolConfig poolConfig) {
		return new FtpClientPool<T>(new FtpClientFactory<T>(ftpClientCreator), poolConfig);
	}

	public static GenericObjectPoolConfig createDefaultPoolConfig() {
		final GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		return poolConfig;
	}

	private Ftps() {
	}
}
