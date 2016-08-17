package com.itdoes.common.ftp;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import com.itdoes.common.pool.ExecutorPool;

/**
 * @author Jalen Zhong
 */
public class FtpClientPool<T extends FTPClient> extends ExecutorPool<T> {
	public FtpClientPool(PooledObjectFactory<T> factory, GenericObjectPoolConfig config) {
		super(factory, config);
	}

	public boolean storeFile(String remote, InputStream localInput) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.storeFile(remote, localInput);
			}
		});
	}

	public OutputStream storeFileStream(String remote) {
		return execute(new PoolCaller<T, OutputStream>() {
			@Override
			public OutputStream call(T t) throws Exception {
				return t.storeFileStream(remote);
			}
		});
	}

	public boolean storeUniqueFile(String remote, InputStream localInput) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.storeUniqueFile(remote, localInput);
			}
		});
	}

	public boolean storeUniqueFile(InputStream localInput) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.storeUniqueFile(localInput);
			}
		});
	}

	public OutputStream storeUniqueFileStream(String remote) {
		return execute(new PoolCaller<T, OutputStream>() {
			@Override
			public OutputStream call(T t) throws Exception {
				return t.storeUniqueFileStream(remote);
			}
		});
	}

	public OutputStream storeUniqueFileStream() {
		return execute(new PoolCaller<T, OutputStream>() {
			@Override
			public OutputStream call(T t) throws Exception {
				return t.storeUniqueFileStream();
			}
		});
	}

	public boolean retrieveFile(String remote, OutputStream localOutput) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.retrieveFile(remote, localOutput);
			}
		});
	}

	public InputStream retrieveFileStream(String remote) {
		return execute(new PoolCaller<T, InputStream>() {
			@Override
			public InputStream call(T t) throws Exception {
				return t.retrieveFileStream(remote);
			}
		});
	}

	public String getModificationTime(String pathname) {
		return execute(new PoolCaller<T, String>() {
			@Override
			public String call(T t) throws Exception {
				return t.getModificationTime(pathname);
			}
		});
	}

	public boolean setModificationTime(String pathname, String timeValue) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.rename(pathname, timeValue);
			}
		});
	}

	public boolean deleteFile(String pathname) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.deleteFile(pathname);
			}
		});
	}

	public boolean rename(String from, String to) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.rename(from, to);
			}
		});
	}

	public FTPFile[] mlistDir() {
		return execute(new PoolCaller<T, FTPFile[]>() {
			@Override
			public FTPFile[] call(T t) throws Exception {
				return t.mlistDir();
			}
		});
	}

	public FTPFile[] mlistDir(String pathname) {
		return execute(new PoolCaller<T, FTPFile[]>() {
			@Override
			public FTPFile[] call(T t) throws Exception {
				return t.mlistDir(pathname);
			}
		});
	}

	public FTPFile mlistFile(String pathname) {
		return execute(new PoolCaller<T, FTPFile>() {
			@Override
			public FTPFile call(T t) throws Exception {
				return t.mlistFile(pathname);
			}
		});
	}

	public FTPFile mdtmFile(String pathname) {
		return execute(new PoolCaller<T, FTPFile>() {
			@Override
			public FTPFile call(T t) throws Exception {
				return t.mdtmFile(pathname);
			}
		});
	}

	public String[] listNames() {
		return execute(new PoolCaller<T, String[]>() {
			@Override
			public String[] call(T t) throws Exception {
				return t.listNames();
			}
		});
	}

	public String[] listNames(String pathname) {
		return execute(new PoolCaller<T, String[]>() {
			@Override
			public String[] call(T t) throws Exception {
				return t.listNames(pathname);
			}
		});
	}

	public FTPFile[] listDirectories() {
		return execute(new PoolCaller<T, FTPFile[]>() {
			@Override
			public FTPFile[] call(T t) throws Exception {
				return t.listDirectories();
			}
		});
	}

	public FTPFile[] listDirectories(String parent) {
		return execute(new PoolCaller<T, FTPFile[]>() {
			@Override
			public FTPFile[] call(T t) throws Exception {
				return t.listDirectories(parent);
			}
		});
	}

	public boolean makeDirectory(String pathname) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.makeDirectory(pathname);
			}
		});
	}

	public FTPFile[] listFiles() {
		return execute(new PoolCaller<T, FTPFile[]>() {
			@Override
			public FTPFile[] call(T t) throws Exception {
				return t.listFiles();
			}
		});
	}

	public FTPFile[] listFiles(String pathname) {
		return execute(new PoolCaller<T, FTPFile[]>() {
			@Override
			public FTPFile[] call(T t) throws Exception {
				return t.listFiles(pathname);
			}
		});
	}

	public String[] featureValues(String feature) {
		return execute(new PoolCaller<T, String[]>() {
			@Override
			public String[] call(T t) throws Exception {
				return t.featureValues(feature);
			}
		});
	}

	public boolean features() {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.features();
			}
		});
	}

	public boolean hasFeature(String feature) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.hasFeature(feature);
			}
		});
	}

	public boolean hasFeature(String feature, String value) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.hasFeature(feature, value);
			}
		});
	}

	public boolean doCommand(String command, String params) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.doCommand(command, params);
			}
		});
	}

	public String[] doCommandAsStrings(String command, String params) {
		return execute(new PoolCaller<T, String[]>() {
			@Override
			public String[] call(T t) throws Exception {
				return t.doCommandAsStrings(command, params);
			}
		});
	}

	public String printWorkingDirectory() {
		return execute(new PoolCaller<T, String>() {
			@Override
			public String call(T t) throws Exception {
				return t.printWorkingDirectory();
			}
		});
	}

	public boolean sendSiteCommand(String arguments) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.sendSiteCommand(arguments);
			}
		});
	}

	public String listHelp() {
		return execute(new PoolCaller<T, String>() {
			@Override
			public String call(T t) throws Exception {
				return t.listHelp();
			}
		});
	}

	public String listHelp(String command) {
		return execute(new PoolCaller<T, String>() {
			@Override
			public String call(T t) throws Exception {
				return t.listHelp(command);
			}
		});
	}
}
