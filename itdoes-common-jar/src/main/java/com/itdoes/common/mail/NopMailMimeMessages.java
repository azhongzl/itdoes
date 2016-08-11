package com.itdoes.common.mail;

import java.io.File;
import java.util.Date;

import javax.activation.FileTypeMap;
import javax.mail.internet.MimeMessage;

/**
 * @author Jalen Zhong
 */
public class NopMailMimeMessages implements MailMimeMessages {
	public static final NopMailMimeMessages INSTANCE = new NopMailMimeMessages();

	private NopMailMimeMessages() {
	}

	@Override
	public MimeMessage getMimeMessage() {
		return null;
	}

	@Override
	public MailMimeMessages setSubject(String subject) {
		return this;
	}

	@Override
	public MailMimeMessages setText(String text, boolean isHtml) {
		return this;
	}

	@Override
	public MailMimeMessages setText(String plainText, String htmlText) {
		return this;
	}

	@Override
	public MailMimeMessages setSentDate(Date sentDate) {
		return this;
	}

	@Override
	public MailMimeMessages setFrom(String from) {
		return this;
	}

	@Override
	public MailMimeMessages addTo(String to) {
		return this;
	}

	@Override
	public MailMimeMessages setTo(String to) {
		return this;
	}

	@Override
	public MailMimeMessages setTo(String[] to) {
		return this;
	}

	@Override
	public MailMimeMessages addCc(String cc) {
		return this;
	}

	@Override
	public MailMimeMessages setCc(String cc) {
		return this;
	}

	@Override
	public MailMimeMessages setCc(String[] cc) {
		return this;
	}

	@Override
	public MailMimeMessages addBcc(String bcc) {
		return this;
	}

	@Override
	public MailMimeMessages setBcc(String bcc) {
		return this;
	}

	@Override
	public MailMimeMessages setBcc(String[] bcc) {
		return this;
	}

	@Override
	public MailMimeMessages setReplyTo(String replyTo) {
		return this;
	}

	@Override
	public MailMimeMessages addAttachment(String attachmentFilename, String fullFilename) {
		return this;
	}

	@Override
	public MailMimeMessages addAttachment(String attachmentFilename, File file) {
		return this;
	}

	@Override
	public MailMimeMessages addInline(String contentId, String fullFilename) {
		return this;
	}

	@Override
	public MailMimeMessages addInline(String contentId, File file) {
		return this;
	}

	@Override
	public MailMimeMessages setPriority(int priority) {
		return this;
	}

	@Override
	public MailMimeMessages setFileTypeMap(FileTypeMap fileTypeMap) {
		return this;
	}

	@Override
	public MailMimeMessages setValidateAddresses(boolean validateAddresses) {
		return this;
	}
}
