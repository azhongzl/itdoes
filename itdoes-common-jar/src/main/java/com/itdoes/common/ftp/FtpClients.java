package com.itdoes.common.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;

/**
 * @author Jalen Zhong
 */
public class FtpClients {
	public static void close(FTPClient ftp) {
		if (ftp == null) {
			return;
		}

		try {
			ftp.logout();
		} catch (IOException e) {
		}

		try {
			ftp.disconnect();
		} catch (IOException e) {
		}
	}

	private FtpClients() {
	}
}
