package com.itdoes.common.ftp;

import java.security.GeneralSecurityException;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;

import com.itdoes.common.util.Exceptions;

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
	private TrustManager trustManager;

	private FtpsClientCreator() {
	}

	@Override
	public FTPSClient create() {
		final FTPSClient ftp;

		if (protocol != null) {
			if (isImplicit != null) {
				ftp = new FTPSClient(protocol, isImplicit);
			} else {
				ftp = new FTPSClient(protocol);
			}
		} else if (context != null) {
			if (isImplicit != null) {
				ftp = new FTPSClient(isImplicit, context);
			} else {
				ftp = new FTPSClient(context);
			}
		} else if (isImplicit != null) {
			ftp = new FTPSClient(isImplicit);
		} else {
			ftp = new FTPSClient();
		}

		if (trustManager != null) {
			ftp.setTrustManager(trustManager);
		}

		return ftp;
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

	public FtpsClientCreator setTrustManager(TrustManager trustManager) {
		this.trustManager = trustManager;
		return this;
	}

	public FtpsClientCreator setAllTrustManager() {
		return setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
	}

	public FtpsClientCreator setValidTrustManager() {
		return setTrustManager(TrustManagerUtils.getValidateServerCertificateTrustManager());
	}

	public FtpsClientCreator setDefaultTrustManager(KeyStore keyStore) {
		try {
			return setTrustManager(TrustManagerUtils.getDefaultTrustManager(keyStore));
		} catch (GeneralSecurityException e) {
			throw Exceptions.unchecked(e);
		}
	}
}
