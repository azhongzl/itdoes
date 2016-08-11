package com.itdoes.common.mail;

/**
 * @author Jalen Zhong
 */
public class Mails {
	public static MailSenders createSender() {
		return MailSenders.create();
	}

	public static MailMimeMessages createMimeMessage(MailSenders sender) {
		return MailMimeMessages.create(sender.createMimeMessage());
	}

	private Mails() {
	}
}
