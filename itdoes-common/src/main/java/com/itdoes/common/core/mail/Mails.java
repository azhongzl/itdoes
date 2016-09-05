package com.itdoes.common.core.mail;

import com.itdoes.common.core.Constants;

/**
 * @author Jalen Zhong
 */
public class Mails {
	private static final String DEFAULT_ENCODING = Constants.UTF8;

	public static MailSenders createSender() {
		return DefaultMailSenders.create();
	}

	public static MailMimeMessages createMimeMessage(MailSenders sender, boolean multipart, String encoding) {
		return DefaultMailMimeMessages.create(sender.createMimeMessage(), multipart, encoding);
	}

	public static MailMimeMessages createMimeMessage(MailSenders sender, boolean multipart) {
		return createMimeMessage(sender, multipart, DEFAULT_ENCODING);
	}

	private Mails() {
	}
}
