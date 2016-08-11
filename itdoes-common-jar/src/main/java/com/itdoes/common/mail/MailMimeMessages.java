package com.itdoes.common.mail;

import java.io.File;
import java.util.Date;

import javax.activation.FileTypeMap;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessageHelper;

import com.itdoes.common.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class MailMimeMessages {
	public static MailMimeMessages create(MimeMessage message) {
		return new MailMimeMessages(message);
	}

	private final MimeMessage mimeMessage;
	private final MimeMessageHelper helper;

	private MailMimeMessages(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
		this.helper = new MimeMessageHelper(mimeMessage);
	}

	public MimeMessage getMimeMessage() {
		return mimeMessage;
	}

	public MailMimeMessages setSubject(String subject) {
		try {
			helper.setSubject(subject);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages setText(String text, boolean isHtml) {
		try {
			helper.setText(text, isHtml);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages setText(String plainText, String htmlText) {
		try {
			helper.setText(plainText, htmlText);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages setSentDate(Date sentDate) {
		try {
			helper.setSentDate(sentDate);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages setFrom(String from) {
		try {
			helper.setFrom(from);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages addTo(String to) {
		try {
			helper.addTo(to);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages setTo(String to) {
		try {
			helper.setTo(to);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages setTo(String[] to) {
		try {
			helper.setTo(to);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages addCc(String cc) {
		try {
			helper.addCc(cc);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages setCc(String cc) {
		try {
			helper.setCc(cc);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages setCc(String[] cc) {
		try {
			helper.setCc(cc);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages addBcc(String bcc) {
		try {
			helper.addBcc(bcc);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages setBcc(String bcc) {
		try {
			helper.setBcc(bcc);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages setBcc(String[] bcc) {
		try {
			helper.setBcc(bcc);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages setReplyTo(String replyTo) {
		try {
			helper.setReplyTo(replyTo);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages addAttachment(String attachmentFilename, String fullFilename) {
		return addAttachment(attachmentFilename, new File(fullFilename));
	}

	public MailMimeMessages addAttachment(String attachmentFilename, File file) {
		try {
			helper.addAttachment(attachmentFilename, file);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages addInline(String contentId, String fullFilename) {
		return addInline(contentId, new File(fullFilename));
	}

	public MailMimeMessages addInline(String contentId, File file) {
		try {
			helper.addInline(contentId, file);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages setPriority(int priority) {
		try {
			helper.setPriority(priority);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	public MailMimeMessages setFileTypeMap(FileTypeMap fileTypeMap) {
		helper.setFileTypeMap(fileTypeMap);
		return this;
	}

	public MailMimeMessages setValidateAddresses(boolean validateAddresses) {
		helper.setValidateAddresses(validateAddresses);
		return this;
	}
}
