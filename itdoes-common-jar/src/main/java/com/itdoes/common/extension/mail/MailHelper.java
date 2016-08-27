package com.itdoes.common.extension.mail;

import java.io.File;

import com.itdoes.common.core.mail.MailMimeMessages;
import com.itdoes.common.core.mail.MailSenders;

/**
 * @author Jalen Zhong
 */
public interface MailHelper {
	MailSenders getSender();

	MailMimeMessages createMimeMessage(boolean multipart, boolean success);

	void send(MailMimeMessages mimeMessage);

	void sendHtml(boolean success, String html);

	void sendHtml(boolean success, String html, File... attachments);

	void sendText(boolean success, String text);

	void sendText(boolean success, String text, File... attachments);

	void sendException(Throwable t);

	void sendException(Throwable t, File... attachments);
}
