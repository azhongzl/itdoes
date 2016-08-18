package com.itdoes.common.ftp;

import java.util.concurrent.CountDownLatch;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.itdoes.common.test.category.Unstable;
import com.itdoes.common.util.Exceptions;

/**
 * @author Jalen Zhong
 */
@Category(Unstable.class)
public class FtpsTest {
	private static final String HOST = "kuzcolighting.com";
	private static final String USERNAME = "kuzco";
	private static final String PASSWORD = "!f35B22zS10!";
	private static final String WORKING_DIRECTORY = "/myTmp";

	private static final String LOCAL_DIR = "D:/myTmp/";
	private static final String LOCAL_FILENAME = "myTmp.txt";
	private static final int MAX_TOTAL = 4;

	@Test
	public void test() throws InterruptedException {
		GenericObjectPoolConfig poolConfig = Ftps.createFtpClientPoolConfig();
		poolConfig.setMaxTotal(MAX_TOTAL);
		FtpClientPool<FTPClient> pool = Ftps.createFtpClientPool(FtpClientBuilder.getInstance().setHost(HOST)
				.setUsername(USERNAME).setPassword(PASSWORD).setWorkingDirectory(WORKING_DIRECTORY)
				.setClientMode(FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE).setConfig(Ftps.createFtpClientConfig()),
				poolConfig);

		int threadCount = 100;
		CountDownLatch lock = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			final int no = i + 1;
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					lock.countDown();
					try {
						lock.await();
					} catch (InterruptedException e) {
						throw Exceptions.unchecked(e);
					}

					String remoteFilename = "abc_" + no + ".txt";
					System.out.println("Store " + remoteFilename + " ...");
					boolean success = pool.storeFile(remoteFilename, LOCAL_DIR + LOCAL_FILENAME);
					System.out.println("Store result: " + success);
					success = pool.retrieveFile(remoteFilename, LOCAL_DIR + remoteFilename);
					System.out.println("Retrieve result: " + success);
				}
			});
			t.start();
		}

		lock.await();
	}
}
