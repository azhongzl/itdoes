package com.itdoes.common.ftp;

import javax.net.ssl.TrustManager;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.itdoes.common.ftp.factory.FtpClientFactory;
import com.itdoes.common.ftp.factory.FtpHttpClientFactory;
import com.itdoes.common.ftp.factory.FtpsClientFactory;
import com.itdoes.common.pool.ExecutorPool;

/**
 * @author Jalen Zhong
 */
public class FtpClientPoolBuilder {
	public static FtpClientPoolBuilder create() {
		return new FtpClientPoolBuilder();
	}

	private final FtpClientInfo clientInfo = new FtpClientInfo();
	private final FTPClientConfig clientConfig = new FTPClientConfig();
	private final GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();

	private FtpClientPoolBuilder() {
	}

	public ExecutorPool<FTPClient> buildFtpClientPool() {
		final PooledObjectFactory<FTPClient> factory = new FtpClientFactory(clientInfo, clientConfig);
		return new ExecutorPool<FTPClient>(factory, poolConfig);
	}

	public ExecutorPool<FTPSClient> buildFtpsClientPool(String protocol, Boolean isImplicit,
			TrustManager trustManager) {
		final PooledObjectFactory<FTPSClient> factory = new FtpsClientFactory(clientInfo, clientConfig, protocol,
				isImplicit, trustManager);
		return new ExecutorPool<FTPSClient>(factory, poolConfig);
	}

	public ExecutorPool<FTPHTTPClient> buildFtpHttpClientPool(String proxyHost, int proxyPort, String proxyUser,
			String proxyPass) {
		final PooledObjectFactory<FTPHTTPClient> factory = new FtpHttpClientFactory(clientInfo, clientConfig, proxyHost,
				proxyPort, proxyUser, proxyPass);
		return new ExecutorPool<FTPHTTPClient>(factory, poolConfig);
	}

	public FtpClientPoolBuilder setHost(String host) {
		clientInfo.setHost(host);
		return this;
	}

	public FtpClientPoolBuilder setPort(Integer port) {
		clientInfo.setPort(port);
		return this;
	}

	public FtpClientPoolBuilder setUsername(String username) {
		clientInfo.setUsername(username);
		return this;
	}

	public FtpClientPoolBuilder setPassword(String password) {
		clientInfo.setPassword(password);
		return this;
	}

	public FtpClientPoolBuilder setFileType(Integer fileType) {
		clientInfo.setFileType(fileType);
		return this;
	}
}
