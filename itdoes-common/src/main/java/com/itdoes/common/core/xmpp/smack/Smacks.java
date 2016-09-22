package com.itdoes.common.core.xmpp.smack;

import java.io.File;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import com.itdoes.common.core.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class Smacks {
	public static AbstractXMPPConnection connect(String serviceName, int port, String username, String password,
			boolean compressionEnabled) {
		try {
			final XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder()
					.setUsernameAndPassword(username, password).setServiceName(serviceName)
					.setCompressionEnabled(compressionEnabled);
			if (port > 0) {
				builder.setPort(port);
			}

			TLSUtils.acceptAllCertificates(builder);
			TLSUtils.disableHostnameVerificationForTlsCertificicates(builder);
			ReconnectionManager.setEnabledPerDefault(true);

			AbstractXMPPConnection connection = new XMPPTCPConnection(builder.build());
			connection.connect();
			connection.login();
			return connection;
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}

	public static void addChatManagerListener(XMPPConnection connection, ChatManagerListener listener) {
		final ChatManager chatManager = ChatManager.getInstanceFor(connection);
		chatManager.addChatListener(listener);
	}

	public static void addFileTransferListener(XMPPConnection connection, FileTransferListener listener) {
		final FileTransferManager fileTransferManager = FileTransferManager.getInstanceFor(connection);
		fileTransferManager.addFileTransferListener(listener);
	}

	public static void sendFile(XMPPConnection connection, String userId, File file) throws Exception {
		final FileTransferManager fileTransferManager = FileTransferManager.getInstanceFor(connection);
		final OutgoingFileTransfer fileTransfer = fileTransferManager.createOutgoingFileTransfer(userId);
		fileTransfer.sendFile(file, "Send");
	}

	private Smacks() {
	}
}
