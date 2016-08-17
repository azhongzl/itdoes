package com.itdoes.common.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author Jalen Zhong
 */
public class Ftps<T extends FTPClient> {
	public static FtpClientPool<FTPClient> getFtp(String host) {
		return getFtp(DefaultFtpClientCreator.getInstance(FtpClientCreator.getInstance()), null);
	}

	public static <T extends FTPClient> FtpClientPool<T> getFtp(IFtpClientCreator<T> ftpClientCreator,
			GenericObjectPoolConfig poolConfig) {
		return new FtpClientPool<T>(new FtpClientFactory<T>(ftpClientCreator), poolConfig);
	}

	private Ftps() {
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		FtpClientPool<FTPClient> ftps = Ftps.getFtp(FtpClientCreator.getInstance(), null);
		FtpClientPool<FTPSClient> ftps2 = Ftps.getFtp(FtpsClientCreator.getInstance(), null);
		FtpClientPool<FTPHTTPClient> ftps3 = Ftps.getFtp(new IFtpClientCreator<FTPHTTPClient>() {
			@Override
			public FTPHTTPClient create() {
				final FTPHTTPClient client = FtpHttpClientCreator.getInstance().setProxyHost("").create();
				client.configure(new FTPClientConfig());
				client.setBufferSize(3000);
				return client;
			}
		}, null);
		boolean store = ftps.storeFile("", null);
		boolean store2 = ftps2.storeFile("", null);
		boolean store3 = ftps3.storeFile("", null);
	}
}
