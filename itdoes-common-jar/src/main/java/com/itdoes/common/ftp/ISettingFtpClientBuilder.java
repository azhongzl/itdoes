package com.itdoes.common.ftp;

import java.net.InetAddress;
import java.util.List;

import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.io.CopyStreamListener;

/**
 * @author Jalen Zhong
 */
public interface ISettingFtpClientBuilder<T extends FTPClient> extends IFtpClientBuilder<T> {
	CopyStreamListener getCopyStreamListener();

	Long getControlKeepAliveTimeout();

	Integer getControlKeepAliveReplyTimeout();

	Integer getConnectTimeout();

	Integer getDefaultTimeout();

	Integer getSoTimeout();

	String getControlEncoding();

	Boolean getListHiddenFiles();

	List<ProtocolCommandListener> getProtocolCommandListeners();

	FTPClientConfig getConfig();

	String getHost();

	Integer getPort();

	String getUsername();

	String getPassword();

	Integer getFileType();

	String getWorkingDirectory();

	Integer getConnectionMode();

	InetAddress getRemoteActiveHost();

	int getRemoteActivePort();

	Boolean getUseEpsvwithIpv4();

	Integer getDataTimeout();

	Integer getBufferSize();
}
