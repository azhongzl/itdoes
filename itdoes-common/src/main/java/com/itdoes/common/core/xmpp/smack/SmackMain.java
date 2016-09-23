package com.itdoes.common.core.xmpp.smack;

import java.io.File;
import java.io.IOException;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import com.itdoes.common.core.util.Threads;

/**
 * @author Jalen Zhong
 */
public class SmackMain {
	private final String serviceName;
	private final String username;
	private final String password;
	private final String userJid;

	public SmackMain(String serviceName, String username, String password, String userJid) {
		this.serviceName = serviceName;
		this.username = username;
		this.password = password;
		this.userJid = userJid;
	}

	public void demo() throws Exception {
		final AbstractXMPPConnection connection = Smacks.connect(serviceName, username, password);
		Chat chat = ChatManager.getInstanceFor(connection).createChat(userJid);

		Smacks.addChatManagerListener(connection, new ChatManagerListener() {
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

		Smacks.addFileTransferListener(connection, new FileTransferListener() {
			@Override
			public void fileTransferRequest(FileTransferRequest request) {
				IncomingFileTransfer accept = request.accept();
				File file = new File("c:/aaa/temp/out-" + username + request.getFileName());
				try {
					accept.recieveFile(file);
				} catch (SmackException | IOException e) {
					e.printStackTrace();
				}
				System.out.println("接收文件=====" + file);
			}
		});

		while (true) {
			chat.sendMessage("Hello, " + userJid + "!");
			Smacks.sendFile(connection, userJid, new File("c:/aaa/temp/in-" + username + ".txt"));
			Threads.sleep(5000);
		}
	}

	public static void main(String[] args) throws Exception {
		String username = "jalen";
		String password = "dhxlsfn";
		String serviceName = "0nl1ne.cc";
		String userJid = "dhxlsfn@0nl1ne.cc/Spark";

		SmackMain main = new SmackMain(serviceName, username, password, userJid);
		main.demo();
	}
}
