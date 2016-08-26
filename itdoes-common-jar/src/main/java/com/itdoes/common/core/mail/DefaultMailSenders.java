package com.itdoes.common.core.mail;

import java.util.Properties;

import javax.activation.FileTypeMap;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author Jalen Zhong
 */
public class DefaultMailSenders implements MailSenders {
	public static MailSenders create() {
		return new DefaultMailSenders();
	}

	private final JavaMailSenderImpl sender;

	private DefaultMailSenders() {
		sender = new JavaMailSenderImpl();
	}

	@Override
	public MimeMessage createMimeMessage() {
		return sender.createMimeMessage();
	}

	@Override
	public void send(MimeMessage mimeMessage) {
		sender.send(mimeMessage);
	}

	@Override
	public void send(MimeMessage... mimeMessages) {
		sender.send(mimeMessages);
	}

	@Override
	public void send(MailMimeMessages mailMimeMessage) {
		sender.send(mailMimeMessage.getMimeMessage());
	}

	@Override
	public void send(MailMimeMessages... mailMimeMessages) {
		for (MailMimeMessages mailMimeMessage : mailMimeMessages) {
			send(mailMimeMessage);
		}
	}

	@Override
	public void testConnection() throws MessagingException {
		sender.testConnection();
	}

	@Override
	public boolean isConnectionOk() {
		try {
			testConnection();
			return true;
		} catch (MessagingException e) {
			return false;
		}
	}

	@Override
	public MailSenders setJavaMailProperties(Properties javaMailProperties) {
		sender.setJavaMailProperties(javaMailProperties);
		return this;
	}

	@Override
	public MailSenders setJavaMailProperty(String key, String value) {
		sender.getJavaMailProperties().setProperty(key, value);
		return this;
	}

	@Override
	public MailSenders setSession(Session session) {
		sender.setSession(session);
		return this;
	}

	@Override
	public MailSenders setProtocol(String protocol) {
		sender.setProtocol(protocol);
		return this;
	}

	@Override
	public MailSenders setHost(String host) {
		sender.setHost(host);
		return this;
	}

	@Override
	public MailSenders setPort(int port) {
		sender.setPort(port);
		return this;
	}

	@Override
	public MailSenders setUsername(String username) {
		sender.setUsername(username);
		return this;
	}

	@Override
	public MailSenders setPassword(String password) {
		sender.setPassword(password);
		return this;
	}

	@Override
	public MailSenders setDefaultEncoding(String defaultEncoding) {
		sender.setDefaultEncoding(defaultEncoding);
		return this;
	}

	@Override
	public MailSenders setDefaultFileTypeMap(FileTypeMap defaultFileTypeMap) {
		sender.setDefaultFileTypeMap(defaultFileTypeMap);
		return this;
	}
}
