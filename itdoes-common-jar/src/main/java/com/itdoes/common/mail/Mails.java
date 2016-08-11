package com.itdoes.common.mail;

import com.itdoes.common.Constants;

/**
 * @author Jalen Zhong
 */
public class Mails {
	private static final String DEFAULT_ENCODING = Constants.UTF8;

	public static MailSenders createSender() {
		return MailSenders.create();
	}

	public static MailMimeMessages createMimeMessage(MailSenders sender, boolean multipart, String encoding) {
		return MailMimeMessages.create(sender.createMimeMessage(), multipart, encoding);
	}

	public static MailMimeMessages createMimeMessage(MailSenders sender, boolean multipart) {
		return MailMimeMessages.create(sender.createMimeMessage(), multipart, DEFAULT_ENCODING);
	}

	private Mails() {
	}
}
