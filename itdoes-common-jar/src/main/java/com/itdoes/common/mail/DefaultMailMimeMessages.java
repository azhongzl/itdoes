package com.itdoes.common.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.activation.FileTypeMap;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessageHelper;

import com.itdoes.common.util.Exceptions;

/**
 * @author Jalen Zhong
 */
public class DefaultMailMimeMessages implements MailMimeMessages {
	public static MailMimeMessages create(MimeMessage message, boolean multipart, String encoding) {
		return new DefaultMailMimeMessages(message, multipart, encoding);
	}

	private final MimeMessage mimeMessage;
	private final MimeMessageHelper helper;

	private DefaultMailMimeMessages(MimeMessage mimeMessage, boolean multipart, String encoding) {
		this.mimeMessage = mimeMessage;

		try {
			this.helper = new MimeMessageHelper(mimeMessage, multipart, encoding);
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MimeMessage getMimeMessage() {
		return mimeMessage;
	}

	@Override
	public MailMimeMessages setSubject(String subject) {
		try {
			helper.setSubject(subject);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages setText(String text, boolean isHtml) {
		try {
			helper.setText(text, isHtml);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages setText(String plainText, String htmlText) {
		try {
			helper.setText(plainText, htmlText);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages setSentDate(Date sentDate) {
		try {
			helper.setSentDate(sentDate);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages setFrom(String from) {
		try {
			helper.setFrom(from);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages setFrom(String from, String personal) {
		try {
			helper.setFrom(from, personal);
			return this;
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages addTo(String to) {
		try {
			helper.addTo(to);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages setTo(String to) {
		try {
			helper.setTo(to);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages setTo(String[] to) {
		try {
			helper.setTo(to);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages addCc(String cc) {
		try {
			helper.addCc(cc);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages setCc(String cc) {
		try {
			helper.setCc(cc);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages setCc(String[] cc) {
		try {
			helper.setCc(cc);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages addBcc(String bcc) {
		try {
			helper.addBcc(bcc);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages setBcc(String bcc) {
		try {
			helper.setBcc(bcc);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages setBcc(String[] bcc) {
		try {
			helper.setBcc(bcc);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages setReplyTo(String replyTo) {
		try {
			helper.setReplyTo(replyTo);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages setReplyTo(String replyTo, String personal) {
		try {
			helper.setReplyTo(replyTo, personal);
			return this;
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages addAttachment(String attachmentFilename, String fullFilename) {
		return addAttachment(attachmentFilename, new File(fullFilename));
	}

	@Override
	public MailMimeMessages addAttachment(String attachmentFilename, File file) {
		try {
			helper.addAttachment(attachmentFilename, file);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages addInline(String contentId, String fullFilename) {
		return addInline(contentId, new File(fullFilename));
	}

	@Override
	public MailMimeMessages addInline(String contentId, File file) {
		try {
			helper.addInline(contentId, file);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages setPriority(int priority) {
		try {
			helper.setPriority(priority);
			return this;
		} catch (MessagingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Override
	public MailMimeMessages setFileTypeMap(FileTypeMap fileTypeMap) {
		helper.setFileTypeMap(fileTypeMap);
		return this;
	}

	@Override
	public MailMimeMessages setValidateAddresses(boolean validateAddresses) {
		helper.setValidateAddresses(validateAddresses);
		return this;
	}
}
