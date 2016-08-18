package com.itdoes.common.ftp;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.experimental.categories.Category;

import com.itdoes.common.Constants;
import com.itdoes.common.pool.ExecutorPool.PoolRunner;
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

	private static final String CASE_SIMPLE_NAME = FtpsTest.class.getSimpleName();

	private static final String WORKING_DIRECTORY = "/" + CASE_SIMPLE_NAME;

	private static final String LOCAL_DIR = System.getProperty("java.io.tmpdir") + "/" + CASE_SIMPLE_NAME + "/";
	private static final String LOCAL_FILENAME = CASE_SIMPLE_NAME + ".txt";
	private static final String LOCAL_FILENAME_PATTERN = CASE_SIMPLE_NAME + "_%03d.txt";
	private static final int POOL_MAX_TOTAL = 3;

	// @Before
	public void init() throws IOException {
		// GenericObjectPoolConfig poolConfig = Ftps.createFtpClientPoolConfig();
		// poolConfig.setMaxTotal(1);
		// FtpClientPool<FTPClient> pool = Ftps.createFtpClientPool(FtpClientBuilder.getInstance().setHost(HOST)
		// .setUsername(USERNAME).setPassword(PASSWORD).setClientMode(FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE)
		// .setConfig(Ftps.createFtpClientConfig()), poolConfig);
		// pool.makeDirectory(WORKING_DIRECTORY);
		// pool.close();

		System.out.println("Local dir is: " + LOCAL_DIR);
		FileUtils.writeStringToFile(new File(LOCAL_DIR + LOCAL_FILENAME), "Jalen line1\nJalen line2\nJalen line3",
				Constants.UTF8_CHARSET);
	}

	// @Test
	public void multiThreads() throws InterruptedException {
		GenericObjectPoolConfig poolConfig = Ftps.createFtpClientPoolConfig();
		poolConfig.setMaxTotal(POOL_MAX_TOTAL);
		FtpClientPool<FTPClient> pool = Ftps.createFtpClientPool(FtpClientBuilder.getInstance().setHost(HOST)
				.setUsername(USERNAME).setPassword(PASSWORD).setClientMode(FTPClient.PASSIVE_LOCAL_DATA_CONNECTION_MODE)
				.setConfig(Ftps.createFtpClientConfig()), poolConfig);

		int threadCount = 1;
		CountDownLatch startLock = new CountDownLatch(threadCount);
		CountDownLatch endLock = new CountDownLatch(threadCount);

		for (int i = 0; i < threadCount; i++) {
			final int no = i + 1;
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					startLock.countDown();
					try {
						startLock.await();
					} catch (InterruptedException e) {
						throw Exceptions.unchecked(e);
					}

					pool.execute(new PoolRunner<FTPClient>() {
						@Override
						public void run(FTPClient t) throws Exception {
							boolean success;
							success = t.makeDirectory(WORKING_DIRECTORY);
							System.out.println("makeDirectory(\"" + WORKING_DIRECTORY + "\") success: " + success);

							success = t.changeWorkingDirectory(WORKING_DIRECTORY);
							System.out.println(
									"changeWorkingDirectory(\"" + WORKING_DIRECTORY + "\") success: " + success);

							String remoteFilename = String.format(LOCAL_FILENAME_PATTERN, no);
							success = pool.storeFile(remoteFilename, LOCAL_DIR + LOCAL_FILENAME);
							System.out.println("storeFile(\"" + remoteFilename + "\", \"" + LOCAL_DIR + LOCAL_FILENAME
									+ "\") success: " + success);

							success = pool.retrieveFile(remoteFilename, LOCAL_DIR + remoteFilename);
							System.out.println("retrieveFile(\"" + remoteFilename + "\", \"" + LOCAL_DIR
									+ remoteFilename + "\") success: " + success);
						}
					});

					endLock.countDown();
				}
			});
			t.start();
		}

		endLock.await();

		pool.close();
	}
}
