package com.itdoes.common.core.xmpp.smack;

import java.io.File;
import java.io.IOException;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.TLSUtils;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import com.itdoes.common.core.util.Threads;

/**
 * @author Jalen Zhong
 */
public class SmackMain {
	public static void main(String[] args) throws Exception {
		String username = "dhxlsfn";
		String password = "dhxlsfn";
		String serviceName = "0nl1ne.cc";

		String userJid = "jalen@0nl1ne.cc/Spark";

		XMPPTCPConnectionConfiguration.Builder builder = XMPPTCPConnectionConfiguration.builder();
		builder.setUsernameAndPassword(username, password);
		builder.setServiceName(serviceName);
		// builder.setDebuggerEnabled(true);
		TLSUtils.acceptAllCertificates(builder);
		TLSUtils.disableHostnameVerificationForTlsCertificicates(builder);
		ReconnectionManager.setEnabledPerDefault(true);

		AbstractXMPPConnection connection = new XMPPTCPConnection(builder.build());
		connection.connect();
		connection.login();

		Chat chat = ChatManager.getInstanceFor(connection).createChat(userJid, new ChatMessageListener() {
			public void processMessage(Chat chat, Message message) {
				System.out.println("Received message: " + message);
			}
		});
		int a = 1;
//		 while(a==1){
		chat.sendMessage("Hello, Jalen!");
//		 Threads.sleep(3000);
//		 }

		ChatManager chatManager = ChatManager.getInstanceFor(connection);
		chatManager.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean createdLocally) {
				if (!createdLocally)
					chat.addMessageListener(new ChatMessageListener() {
						@Override
						public void processMessage(Chat chat, Message message) {
							if (message.getBody() != null)
								System.out.println("Received message: " + message.getBody());

						}
					});
			}
		});

		sendFile(userJid, new File("d:/aaa.txt"), connection);

		FileTransferManager fileTransferManager = FileTransferManager.getInstanceFor(connection);
		fileTransferManager.addFileTransferListener(new FileTransferListener() {
			@Override
			public void fileTransferRequest(FileTransferRequest request) {
				IncomingFileTransfer accept = request.accept();
				File file = new File("d:/" + request.getFileName());
				try {
					accept.recieveFile(file);
				} catch (SmackException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("接收文件=====" + file);
			}
		});

		while (true) {
		}
	}

	public static void sendFile(String user, File file, XMPPConnection connection) throws Exception {
		FileTransferManager fileTransferManager = FileTransferManager.getInstanceFor(connection);
		OutgoingFileTransfer fileTransfer = fileTransferManager.createOutgoingFileTransfer(user);
		fileTransfer.sendFile(file, "Send");
		System.out.println("发送文件----" + file);
	}
}
