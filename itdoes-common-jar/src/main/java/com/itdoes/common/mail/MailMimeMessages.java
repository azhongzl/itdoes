package com.itdoes.common.mail;

import java.io.File;
import java.util.Date;

import javax.activation.FileTypeMap;
import javax.mail.internet.MimeMessage;

/**
 * @author Jalen Zhong
 */
public interface MailMimeMessages {
	MimeMessage getMimeMessage();

	MailMimeMessages setSubject(String subject);

	MailMimeMessages setText(String text, boolean isHtml);

	MailMimeMessages setText(String plainText, String htmlText);

	MailMimeMessages setSentDate(Date sentDate);

	MailMimeMessages setFrom(String from);

	MailMimeMessages addTo(String to);

	MailMimeMessages setTo(String to);

	MailMimeMessages setTo(String[] to);

	MailMimeMessages addCc(String cc);

	MailMimeMessages setCc(String cc);

	MailMimeMessages setCc(String[] cc);

	MailMimeMessages addBcc(String bcc);

	MailMimeMessages setBcc(String bcc);

	MailMimeMessages setBcc(String[] bcc);

	MailMimeMessages setReplyTo(String replyTo);

	MailMimeMessages addAttachment(String attachmentFilename, String fullFilename);

	MailMimeMessages addAttachment(String attachmentFilename, File file);

	MailMimeMessages addInline(String contentId, String fullFilename);

	MailMimeMessages addInline(String contentId, File file);

	MailMimeMessages setPriority(int priority);

	MailMimeMessages setFileTypeMap(FileTypeMap fileTypeMap);

	MailMimeMessages setValidateAddresses(boolean validateAddresses);
}
