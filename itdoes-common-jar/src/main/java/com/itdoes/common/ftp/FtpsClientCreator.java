package com.itdoes.common.ftp;

import javax.net.ssl.SSLContext;

import org.apache.commons.net.ftp.FTPSClient;

/**
 * @author Jalen Zhong
 */
public class FtpsClientCreator implements IFtpClientCreator<FTPSClient> {
	public static FtpsClientCreator getInstance() {
		return new FtpsClientCreator();
	}

	private String protocol;
	private Boolean isImplicit;
	private SSLContext context;

	public FtpsClientCreator() {
	}

	@Override
	public FTPSClient create() {
		if (protocol != null) {
			if (isImplicit != null) {
				return new FTPSClient(protocol, isImplicit);
			} else {
				return new FTPSClient(protocol);
			}
		} else if (context != null) {
			if (isImplicit != null) {
				return new FTPSClient(isImplicit, context);
			} else {
				return new FTPSClient(context);
			}
		} else if (isImplicit != null) {
			return new FTPSClient(isImplicit);
		} else {
			return new FTPSClient();
		}
	}

	public FtpsClientCreator setProtocol(String protocol) {
		this.protocol = protocol;
		return this;
	}

	public FtpsClientCreator setIsImplicit(Boolean isImplicit) {
		this.isImplicit = isImplicit;
		return this;
	}

	public FtpsClientCreator setContext(SSLContext context) {
		this.context = context;
		return this;
	}
}
