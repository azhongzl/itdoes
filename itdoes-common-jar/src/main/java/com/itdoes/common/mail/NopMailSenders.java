package com.itdoes.common.mail;

import java.util.Properties;

import javax.activation.FileTypeMap;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

/**
 * @author Jalen Zhong
 */
public class NopMailSenders implements MailSenders {
	public static final NopMailSenders INSTANCE = new NopMailSenders();

	private NopMailSenders() {
	}

	@Override
	public MimeMessage createMimeMessage() {
		return null;
	}

	@Override
	public void send(MimeMessage mimeMessage) {
	}

	@Override
	public void send(MimeMessage... mimeMessages) {
	}

	@Override
	public void send(MailMimeMessages mailMimeMessage) {
	}

	@Override
	public void send(MailMimeMessages... mailMimeMessages) {
	}

	@Override
	public void testConnection() throws MessagingException {
	}

	@Override
	public boolean isConnectionOk() {
		return true;
	}

	@Override
	public MailSenders setJavaMailProperties(Properties javaMailProperties) {
		return this;
	}

	@Override
	public MailSenders setJavaMailProperty(String key, String value) {
		return this;
	}

	@Override
	public MailSenders setSession(Session session) {
		return this;
	}

	@Override
	public MailSenders setProtocol(String protocol) {
		return this;
	}

	@Override
	public MailSenders setHost(String host) {
		return this;
	}

	@Override
	public MailSenders setPort(int port) {
		return this;
	}

	@Override
	public MailSenders setUsername(String username) {
		return this;
	}

	@Override
	public MailSenders setPassword(String password) {
		return this;
	}

	@Override
	public MailSenders setDefaultEncoding(String defaultEncoding) {
		return this;
	}

	@Override
	public MailSenders setDefaultFileTypeMap(FileTypeMap defaultFileTypeMap) {
		return this;
	}
}