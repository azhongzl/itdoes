package com.itdoes.common.ftp;

import org.apache.commons.net.ftp.FTPClient;

/**
 * @author Jalen Zhong
 */
public interface IFtpClientCreator<T extends FTPClient> {
	T create();
}
