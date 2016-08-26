package com.itdoes.common.core.ftp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.Exceptions;
import com.itdoes.common.core.util.Files;
import com.itdoes.common.core.util.Urls;

/**
 * @author Jalen Zhong
 */
public class FtpClients {
	public static boolean makeDirectory(FTPClient client, String dir) throws IOException {
		return makeDirectory(client, dir, true);
	}

	/**
	 * 
	 * @param client
	 * @param dir
	 *            make directory always from root instead of working directory because working directory often changes
	 *            and is prone to error
	 * @param recursive
	 *            true means create all not exist sub directories, false means only create the first not exist directory
	 * @return
	 * @throws IOException
	 */
	public static boolean makeDirectory(FTPClient client, String dir, boolean recursive) throws IOException {
		Validate.notNull(client, "FTPClient is null");
		Validate.notNull(dir, "Dir is null");

		final String unixDir = Files.toUnixPath(dir);

		if (!recursive) {
			return client.makeDirectory(unixDir);
		}

		final String[] relativeDirs = StringUtils.split(unixDir, "/");
		if (Collections3.isEmpty(relativeDirs)) {
			return true;
		}

		String dirToMake = "/";
		for (String relativeDir : relativeDirs) {
			dirToMake = Urls.concat(dirToMake, relativeDir);
			client.makeDirectory(dirToMake);
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

	public static void setFileTypeAscii(FTPClient client) throws IOException {
		client.setFileType(FTP.ASCII_FILE_TYPE);
	}

	public static void setFileTypeBinary(FTPClient client) throws IOException {
		client.setFileType(FTP.BINARY_FILE_TYPE);
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
