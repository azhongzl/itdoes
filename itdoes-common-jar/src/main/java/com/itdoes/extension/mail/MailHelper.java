package com.itdoes.extension.mail;

import com.itdoes.common.mail.MailMimeMessages;
import com.itdoes.common.mail.MailSenders;

/**
 * @author Jalen Zhong
 */
public interface MailHelper {
	MailSenders getSender();

	MailMimeMessages createMimeMessage(boolean multipart, boolean success);
}
