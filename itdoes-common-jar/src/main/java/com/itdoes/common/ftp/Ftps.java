package com.itdoes.common.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPHTTPClient;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.itdoes.common.pool.ExecutorPool;
import com.itdoes.common.pool.ExecutorPool.PoolCaller;

/**
 * @author Jalen Zhong
 */
public class Ftps<T extends FTPClient> {
	private ExecutorPool<T> pool;

	public Ftps(IFtpClientCreator<T> ftpClientCreator, GenericObjectPoolConfig poolConfig) {
		pool = new ExecutorPool<T>(new FtpClientFactory<T>(ftpClientCreator), poolConfig);
	}

	public String update() {
		return pool.execute(new PoolCaller<T, String>() {
			@Override
			public String call(T t) {
				return "";
			}
		});
	}

	public static void main(String[] args) {
		Ftps<FTPClient> ftps = new Ftps<FTPClient>(FtpClientCreator.getInstance(), null);
		Ftps<FTPSClient> ftps2 = new Ftps<FTPSClient>(FtpsClientCreator.getInstance(), null);
		Ftps<FTPHTTPClient> ftps3 = new Ftps<FTPHTTPClient>(new IFtpClientCreator<FTPHTTPClient>() {
			@Override
			public FTPHTTPClient create() {
				final FTPHTTPClient client = FtpHttpClientCreator.getInstance().setProxyHost("").create();
				client.configure(new FTPClientConfig());
				client.setBufferSize(3000);
				return client;
			}
		}, null);
		String update = ftps.update();
		String update2 = ftps2.update();
		String update3 = ftps3.update();
	}
}
