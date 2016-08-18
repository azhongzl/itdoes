package com.itdoes.common.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author Jalen Zhong
 */
public class Ftps {
	/**
	 * <blockquote>
	 * 
	 * <pre>
	 * final FTPClientConfig clientConfig = createFtpClientConfig();
	 * ...
	 * clientConfig.setXXX();
	 * ...
	 * 
	 * final IFtpClientBuilder<FTPClient> clientBuilder = createFtpClientBuilder().setHost(host).setUsername(username)
	 * 		.setPassword(password).setWorkingDirectory(workingDirectory).setConfig(clientConfig).setXXX(...);
	 * 
	 * final GenericObjectPoolConfig poolConfig = createFtpClientPoolConfig();
	 * poolConfig.setMaxTotal(maxTotal);
	 * ...
	 * poolConfig.setXXX();
	 * ...
	 * 
	 * return createFtpClientPool(clientBuilder, poolConfig);
	 * </pre>
	 * 
	 * </blockquote>
	 */
	public static <T extends FTPClient> FtpClientPool<T> createFtpClientPool(IFtpClientBuilder<T> ftpClientBuilder,
			GenericObjectPoolConfig poolConfig) {
		return new FtpClientPool<T>(new FtpClientFactory<T>(ftpClientBuilder), poolConfig);
	}

	public static FtpClientBuilder createFtpClientBuilder() {
		return FtpClientBuilder.getInstance();
	}

	public static FtpsClientBuilder createFtpsClientBuilder() {
		return FtpsClientBuilder.getInstance();
	}

	public static FtpHttpClientBuilder createFtpHttpClientBuilder() {
		return FtpHttpClientBuilder.getInstance();
	}

	public static FTPClientConfig createFtpClientConfig() {
		return new FTPClientConfig();
	}

	public static GenericObjectPoolConfig createFtpClientPoolConfig() {
		return new GenericObjectPoolConfig();
	}

	private Ftps() {
	}
}
