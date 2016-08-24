package com.itdoes.common.ftp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.net.ftp.FTPClient;

import com.itdoes.common.util.Collections3;
import com.itdoes.common.util.Exceptions;
import com.itdoes.common.util.Files;

/**
 * @author Jalen Zhong
 */
public class FtpClients {
	public static boolean makeDirectory(FTPClient client, String baseDir, String relativeDirs) throws IOException {
		return makeDirectory(client, baseDir, relativeDirs, true);
	}

	public static boolean makeDirectory(FTPClient client, String baseDir, String relativeDirs, boolean createIfNotExist)
			throws IOException {
		Validate.notNull(client, "FTPClient is null");
		Validate.notNull(baseDir, "BaseDir is null");
		Validate.notNull(relativeDirs, "RelativeDirs is null");

		final String relativeDirsStr = Files.toUnixPath(relativeDirs);

		if (!createIfNotExist) {
			return client.makeDirectory(relativeDirsStr);
		}

		final String[] myRemoteRelativeDirs = relativeDirsStr.split("/");
		if (Collections3.isEmpty(myRemoteRelativeDirs)) {
			return true;
		}

		String dir = Files.toUnixPath(baseDir);
		System.out.println("----------------------------working dir-------------------------" + dir);
		for (String myRemoteRelativeDir : myRemoteRelativeDirs) {
			dir += "/" + myRemoteRelativeDir;
			System.out.println("-----------------------------------------------------" + relativeDirs + "===" + dir);
			client.makeDirectory(dir);
		}

		return true;
	}

	public static boolean retrieveFile(FTPClient client, String remote, String local) throws IOException {
		Validate.notNull(client, "FTPClient is null");
		Validate.notNull(remote, "Remote is null");
		Validate.notNull(local, "Local is null");

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
		Validate.notNull(client, "FTPClient is null");
		Validate.notNull(remote, "Remote is null");
		Validate.notNull(local, "Local is null");

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

	public static void close(FTPClient client) {
		if (client == null) {
			return;
		}

		try {
			client.logout();
		} catch (IOException e) {
		}

		try {
			client.disconnect();
		} catch (IOException e) {
		}
	}

	private FtpClients() {
	}
}
