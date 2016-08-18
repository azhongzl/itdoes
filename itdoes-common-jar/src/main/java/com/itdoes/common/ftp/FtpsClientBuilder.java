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
public class FtpsClientBuilder implements IFtpClientBuilder<FTPSClient> {
	public static FtpsClientBuilder getInstance() {
		return new FtpsClientBuilder();
	}

	private String protocol;
	private Boolean isImplicit;
	private SSLContext context;
	private TrustManager trustManager;

	private FtpsClientBuilder() {
	}

	@Override
	public FTPSClient build() {
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

	public FtpsClientBuilder setProtocol(String protocol) {
		this.protocol = protocol;
		return this;
	}

	public FtpsClientBuilder setIsImplicit(Boolean isImplicit) {
		this.isImplicit = isImplicit;
		return this;
	}

	public FtpsClientBuilder setContext(SSLContext context) {
		this.context = context;
		return this;
	}

	public FtpsClientBuilder setTrustManager(TrustManager trustManager) {
		this.trustManager = trustManager;
		return this;
	}

	public FtpsClientBuilder setAllTrustManager() {
		return setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
	}

	public FtpsClientBuilder setValidTrustManager() {
		return setTrustManager(TrustManagerUtils.getValidateServerCertificateTrustManager());
	}

	public FtpsClientBuilder setDefaultTrustManager(KeyStore keyStore) {
		try {
			return setTrustManager(TrustManagerUtils.getDefaultTrustManager(keyStore));
		} catch (GeneralSecurityException e) {
			throw Exceptions.unchecked(e);
		}
	}
}
