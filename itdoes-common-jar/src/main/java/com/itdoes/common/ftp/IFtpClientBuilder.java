package com.itdoes.common.ftp;

import org.apache.commons.net.ftp.FTPClient;

/**
 * @author Jalen Zhong
 */
public interface IFtpClientBuilder<T extends FTPClient> {
	T build();
}
