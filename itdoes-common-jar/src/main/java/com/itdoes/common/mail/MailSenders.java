package com.itdoes.common.mail;

import java.util.Properties;

import javax.activation.FileTypeMap;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * @author Jalen Zhong
 */
public class MailSenders {
	public static MailSenders create() {
		return new MailSenders();
	}

	private final JavaMailSenderImpl sender;

	private MailSenders() {
		sender = new JavaMailSenderImpl();
	}

	public MimeMessage createMimeMessage() {
		return sender.createMimeMessage();
	}

	public void send(MimeMessage mimeMessage) {
		sender.send(mimeMessage);
	}

	public void send(MimeMessage... mimeMessages) {
		sender.send(mimeMessages);
	}

	public void testConnection() throws MessagingException {
		sender.testConnection();
	}

	public boolean isConnectionOk() {
		try {
			testConnection();
			return true;
		} catch (MessagingException e) {
			return false;
		}
	}

	public MailSenders setJavaMailProperties(Properties javaMailProperties) {
		sender.setJavaMailProperties(javaMailProperties);
		return this;
	}

	public MailSenders setJavaMailProperty(String key, String value) {
		sender.getJavaMailProperties().setProperty(key, value);
		return this;
	}

	public MailSenders setSession(Session session) {
		sender.setSession(session);
		return this;
	}

	public MailSenders setProtocol(String protocol) {
		sender.setProtocol(protocol);
		return this;
	}

	public MailSenders setHost(String host) {
		sender.setHost(host);
		return this;
	}

	public MailSenders setPort(int port) {
		sender.setPort(port);
		return this;
	}

	public MailSenders setUsername(String username) {
		sender.setUsername(username);
		return this;
	}

	public MailSenders setPassword(String password) {
		sender.setPassword(password);
		return this;
	}

	public MailSenders setDefaultEncoding(String defaultEncoding) {
		sender.setDefaultEncoding(defaultEncoding);
		return this;
	}

	public MailSenders setDefaultFileTypeMap(FileTypeMap defaultFileTypeMap) {
		sender.setDefaultFileTypeMap(defaultFileTypeMap);
		return this;
	}
}
