package com.itdoes.common.ftp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.commons.net.ftp.parser.FTPFileEntryParserFactory;
import org.apache.commons.net.io.CopyStreamListener;
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

	public boolean abort() {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.abort();
			}
		});
	}

	public boolean allocate(int bytes) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.allocate(bytes);
			}
		});
	}

	public boolean allocate(int bytes, int recordSize) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.allocate(bytes, recordSize);
			}
		});
	}

	public boolean appendFile(String remote, InputStream localInput) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.appendFile(remote, localInput);
			}
		});
	}

	public OutputStream appendFileStream(String remote) {
		return execute(new PoolCaller<T, OutputStream>() {
			@Override
			public OutputStream call(T t) throws Exception {
				return t.appendFileStream(remote);
			}
		});
	}

	public boolean changeToParentDirectory() {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.changeToParentDirectory();
			}
		});
	}

	public boolean changeWorkingDirectory(String pathname) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.changeWorkingDirectory(pathname);
			}
		});
	}

	public boolean completePendingCommand() {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.completePendingCommand();
			}
		});
	}

	public void configure(FTPClientConfig config) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.configure(config);
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

	public void enterLocalActiveMode() {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.enterLocalActiveMode();
			}
		});
	}

	public void enterLocalPassiveMode() {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.enterLocalPassiveMode();
			}
		});
	}

	public boolean enterRemoteActiveMode(InetAddress host, int port) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.enterRemoteActiveMode(host, port);
			}
		});
	}

	public boolean enterRemotePassiveMode() {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.enterRemotePassiveMode();
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

	public String featureValue(String feature) {
		return execute(new PoolCaller<T, String>() {
			@Override
			public String call(T t) throws Exception {
				return t.featureValue(feature);
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

	public boolean getAutodetectUTF8() {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.getAutodetectUTF8();
			}
		});
	}

	public int getBufferSize() {
		return execute(new PoolCaller<T, Integer>() {
			@Override
			public Integer call(T t) throws Exception {
				return t.getBufferSize();
			}
		});
	}

	public int getControlKeepAliveReplyTimeout() {
		return execute(new PoolCaller<T, Integer>() {
			@Override
			public Integer call(T t) throws Exception {
				return t.getControlKeepAliveReplyTimeout();
			}
		});
	}

	public long getControlKeepAliveTimeout() {
		return execute(new PoolCaller<T, Long>() {
			@Override
			public Long call(T t) throws Exception {
				return t.getControlKeepAliveTimeout();
			}
		});
	}

	public CopyStreamListener getCopyStreamListener() {
		return execute(new PoolCaller<T, CopyStreamListener>() {
			@Override
			public CopyStreamListener call(T t) throws Exception {
				return t.getCopyStreamListener();
			}
		});
	}

	public int getDataConnectionMode() {
		return execute(new PoolCaller<T, Integer>() {
			@Override
			public Integer call(T t) throws Exception {
				return t.getDataConnectionMode();
			}
		});
	}

	public boolean getListHiddenFiles() {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.getListHiddenFiles();
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

	public String getPassiveHost() {
		return execute(new PoolCaller<T, String>() {
			@Override
			public String call(T t) throws Exception {
				return t.getPassiveHost();
			}
		});
	}

	public InetAddress getPassiveLocalIPAddress() {
		return execute(new PoolCaller<T, InetAddress>() {
			@Override
			public InetAddress call(T t) throws Exception {
				return t.getPassiveLocalIPAddress();
			}
		});
	}

	public int getPassivePort() {
		return execute(new PoolCaller<T, Integer>() {
			@Override
			public Integer call(T t) throws Exception {
				return t.getPassivePort();
			}
		});
	}

	public int getReceiveDataSocketBufferSize() {
		return execute(new PoolCaller<T, Integer>() {
			@Override
			public Integer call(T t) throws Exception {
				return t.getReceiveDataSocketBufferSize();
			}
		});
	}

	public long getRestartOffset() {
		return execute(new PoolCaller<T, Long>() {
			@Override
			public Long call(T t) throws Exception {
				return t.getRestartOffset();
			}
		});
	}

	public int getSendDataSocketBufferSize() {
		return execute(new PoolCaller<T, Integer>() {
			@Override
			public Integer call(T t) throws Exception {
				return t.getSendDataSocketBufferSize();
			}
		});
	}

	public String getStatus() {
		return execute(new PoolCaller<T, String>() {
			@Override
			public String call(T t) throws Exception {
				return t.getStatus();
			}
		});
	}

	public String getStatus(String pathname) {
		return execute(new PoolCaller<T, String>() {
			@Override
			public String call(T t) throws Exception {
				return t.getStatus(pathname);
			}
		});
	}

	public String getSystemType() {
		return execute(new PoolCaller<T, String>() {
			@Override
			public String call(T t) throws Exception {
				return t.getSystemType();
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

	public FTPListParseEngine initiateListParsing() {
		return execute(new PoolCaller<T, FTPListParseEngine>() {
			@Override
			public FTPListParseEngine call(T t) throws Exception {
				return t.initiateListParsing();
			}
		});
	}

	public FTPListParseEngine initiateListParsing(String pathname) {
		return execute(new PoolCaller<T, FTPListParseEngine>() {
			@Override
			public FTPListParseEngine call(T t) throws Exception {
				return t.initiateListParsing(pathname);
			}
		});
	}

	public FTPListParseEngine initiateListParsing(String parserKey, String pathname) {
		return execute(new PoolCaller<T, FTPListParseEngine>() {
			@Override
			public FTPListParseEngine call(T t) throws Exception {
				return t.initiateListParsing(parserKey, pathname);
			}
		});
	}

	public boolean isRemoteVerificationEnabled() {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.isRemoteVerificationEnabled();
			}
		});
	}

	public boolean isUseEPSVwithIPv4() {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.isUseEPSVwithIPv4();
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

	public FTPFile[] listFiles(String pathname, FTPFileFilter filter) {
		return execute(new PoolCaller<T, FTPFile[]>() {
			@Override
			public FTPFile[] call(T t) throws Exception {
				return t.listFiles(pathname, filter);
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

	public boolean login(String username, String password) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.login(username, password);
			}
		});
	}

	public boolean login(String username, String password, String account) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.login(username, password, account);
			}
		});
	}

	public boolean logout() {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.logout();
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

	public FTPFile mdtmFile(String pathname) {
		return execute(new PoolCaller<T, FTPFile>() {
			@Override
			public FTPFile call(T t) throws Exception {
				return t.mdtmFile(pathname);
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

	public FTPFile[] mlistDir(String pathname, FTPFileFilter filter) {
		return execute(new PoolCaller<T, FTPFile[]>() {
			@Override
			public FTPFile[] call(T t) throws Exception {
				return t.mlistDir(pathname, filter);
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

	public String printWorkingDirectory() {
		return execute(new PoolCaller<T, String>() {
			@Override
			public String call(T t) throws Exception {
				return t.printWorkingDirectory();
			}
		});
	}

	public boolean reinitialize() {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.reinitialize();
			}
		});
	}

	public boolean remoteAppend(String filename) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.remoteAppend(filename);
			}
		});
	}

	public boolean remoteRetrieve(String filename) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.remoteRetrieve(filename);
			}
		});
	}

	public boolean remoteStore(String filename) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.remoteStore(filename);
			}
		});
	}

	public boolean remoteStoreUnique() {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.remoteStoreUnique();
			}
		});
	}

	public boolean remoteStoreUnique(String filename) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.remoteStoreUnique(filename);
			}
		});
	}

	public boolean removeDirectory(String filename) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.removeDirectory(filename);
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

	public boolean sendNoOp() {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.sendNoOp();
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

	public void setActiveExternalIPAddress(String ipAddress) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setActiveExternalIPAddress(ipAddress);
			}
		});
	}

	public void setActivePortRange(int minPort, int maxPort) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setActivePortRange(minPort, maxPort);
			}
		});
	}

	public void setAutodetectUTF8(boolean autoDefectUtf8) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setAutodetectUTF8(autoDefectUtf8);
			}
		});
	}

	public void setBufferSize(int bufferSize) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setBufferSize(bufferSize);
			}
		});
	}

	public void setControlKeepAliveReplyTimeout(int controlKeepAliveReplyTimeout) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setControlKeepAliveReplyTimeout(controlKeepAliveReplyTimeout);
			}
		});
	}

	public void setControlKeepAliveTimeout(long controlKeepAliveTimeout) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setControlKeepAliveTimeout(controlKeepAliveTimeout);
			}
		});
	}

	public void setCopyStreamListener(CopyStreamListener copyStreamListener) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setCopyStreamListener(copyStreamListener);
			}
		});
	}

	public void setDataTimeout(int dataTimeout) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setDataTimeout(dataTimeout);
			}
		});
	}

	public boolean setFileStructure(int fileStructure) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.setFileStructure(fileStructure);
			}
		});
	}

	public boolean setFileTransferMode(int fileTransferMode) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.setFileTransferMode(fileTransferMode);
			}
		});
	}

	public boolean setFileType(int fileType) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.setFileType(fileType);
			}
		});
	}

	public boolean setFileType(int fileType, int formatOrByteSize) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.setFileType(fileType, formatOrByteSize);
			}
		});
	}

	public void setListHiddenFiles(boolean listHiddenFiles) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setListHiddenFiles(listHiddenFiles);
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

	public void setParserFactory(FTPFileEntryParserFactory ftpFileEntryParserFactory) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setParserFactory(ftpFileEntryParserFactory);
			}
		});
	}

	public void setPassiveLocalIPAddress(String ipAddress) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setPassiveLocalIPAddress(ipAddress);
			}
		});
	}

	public void setPassiveLocalIPAddress(InetAddress passiveLocalIPAddress) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setPassiveLocalIPAddress(passiveLocalIPAddress);
			}
		});
	}

	public void setPassiveNatWorkaround(boolean passiveLocalIPAddress) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setPassiveNatWorkaround(passiveLocalIPAddress);
			}
		});
	}

	public void setReceieveDataSocketBufferSize(int receieveDataSocketBufferSize) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setReceieveDataSocketBufferSize(receieveDataSocketBufferSize);
			}
		});
	}

	public void setRemoteVerificationEnabled(boolean remoteVerificationEnabled) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setRemoteVerificationEnabled(remoteVerificationEnabled);
			}
		});
	}

	public void setReportActiveExternalIPAddress(String reportActiveExternalIPAddress) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setReportActiveExternalIPAddress(reportActiveExternalIPAddress);
			}
		});
	}

	public void setRestartOffset(long restartOffset) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setRestartOffset(restartOffset);
			}
		});
	}

	public void setSendDataSocketBufferSize(int sendDataSocketBufferSize) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setSendDataSocketBufferSize(sendDataSocketBufferSize);
			}
		});
	}

	public void setUseEPSVwithIPv4(boolean useEPSVwithIPv4) {
		execute(new PoolRunner<T>() {
			@Override
			public void run(T t) throws Exception {
				t.setUseEPSVwithIPv4(useEPSVwithIPv4);
			}
		});
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

	public boolean storeUniqueFile(InputStream localInput) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.storeUniqueFile(localInput);
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

	public OutputStream storeUniqueFileStream() {
		return execute(new PoolCaller<T, OutputStream>() {
			@Override
			public OutputStream call(T t) throws Exception {
				return t.storeUniqueFileStream();
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

	public boolean structureMount(String pathname) {
		return execute(new PoolCaller<T, Boolean>() {
			@Override
			public Boolean call(T t) throws Exception {
				return t.structureMount(pathname);
			}
		});
	}
}
