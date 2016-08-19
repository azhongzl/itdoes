package com.itdoes.common.ftp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;

import com.itdoes.common.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class FtpClients {
	public static boolean retrieveFile(FTPClient client, String remote, String local) throws IOException {
		OutputStream localOutput = null;
		try {
			localOutput = new FileOutputStream(local);
			return client.retrieveFile(remote, localOutput);
		} catch (FileNotFoundException e) {
			throw Exceptions.unchecked(e);
		} finally {
			IOUtils.closeQuietly(localOutput);
		}
	}

	public static boolean storeFile(FTPClient client, String remote, String local) throws IOException {
		InputStream localInput = null;
		try {
			localInput = new FileInputStream(local);
			return client.storeFile(remote, localInput);
		} catch (FileNotFoundException e) {
			throw Exceptions.unchecked(e);
		} finally {
			IOUtils.closeQuietly(localInput);
		}
	}

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
