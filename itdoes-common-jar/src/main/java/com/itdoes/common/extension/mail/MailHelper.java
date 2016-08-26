package com.itdoes.common.extension.mail;

import com.itdoes.common.core.mail.MailMimeMessages;
import com.itdoes.common.core.mail.MailSenders;

/**
 * @author Jalen Zhong
 */
public interface MailHelper {
	MailSenders getSender();

	MailMimeMessages createMimeMessage(boolean multipart, boolean success);
}
