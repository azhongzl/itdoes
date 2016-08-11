package com.itdoes.common.mail;

import java.util.Properties;

import javax.activation.FileTypeMap;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * @author Jalen Zhong
 */
public interface MailSenders {
	MimeMessage createMimeMessage();

	void send(MimeMessage mimeMessage);

	void send(MimeMessage... mimeMessages);

	void send(MailMimeMessages mailMimeMessage);

	void send(MailMimeMessages... mailMimeMessages);

	void testConnection() throws MessagingException;

	boolean isConnectionOk();

	MailSenders setJavaMailProperties(Properties javaMailProperties);

	MailSenders setJavaMailProperty(String key, String value);

	MailSenders setSession(Session session);

	MailSenders setProtocol(String protocol);

	MailSenders setHost(String host);

	MailSenders setPort(int port);

	MailSenders setUsername(String username);

	MailSenders setPassword(String password);

	MailSenders setDefaultEncoding(String defaultEncoding);

	MailSenders setDefaultFileTypeMap(FileTypeMap defaultFileTypeMap);
}
