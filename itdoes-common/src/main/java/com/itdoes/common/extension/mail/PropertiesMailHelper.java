package com.itdoes.common.extension.mail;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.collect.Maps;
import com.itdoes.common.core.freemarker.FreeMarkers;
import com.itdoes.common.core.mail.MailMimeMessages;
import com.itdoes.common.core.mail.MailSenders;
import com.itdoes.common.core.mail.Mails;
import com.itdoes.common.core.mail.NopMailMimeMessages;
import com.itdoes.common.core.mail.NopMailSenders;
import com.itdoes.common.core.security.Cryptos;
import com.itdoes.common.core.util.Collections3;
import com.itdoes.common.core.util.Exceptions;
import com.itdoes.common.core.util.PropertiesLoader;

/**
 * @author Jalen Zhong
 */
public class PropertiesMailHelper implements MailHelper {
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private final PropertiesLoader pl;
	private final MailSenders sender;

	public PropertiesMailHelper(PropertiesLoader pl) {
		this.pl = pl;
		this.sender = createSender();
	}

	@Override
	public MailSenders getSender() {
		return sender;
	}

	@Override
	public MailMimeMessages createMimeMessage(boolean multipart, boolean success) {
		if (!pl.getBoolean("mail.on")) {
			return NopMailMimeMessages.INSTANCE;
		}

		final MailMimeMessages mimeMessage = Mails.createMimeMessage(sender, multipart);

		final String from = pl.getString("mail.from");
		final String personal = from.substring(0, from.indexOf("@"));
		mimeMessage.setFrom(from, personal).setReplyTo(from, personal);

		// Required
		final String[] to;
		final String subject;
		// Optional
		final String[] cc;
		final String[] bcc;
		if (success) {
			to = getAddresses("mail.to.success");
			subject = getSubject(true);

			cc = getAddresses("mail.cc.success");
			bcc = getAddresses("mail.bcc.success");
		} else {
			to = getAddresses("mail.to.fail", "mail.to.success");
			subject = getSubject(false);

			cc = getAddresses("mail.cc.fail", "mail.cc.success");
			bcc = getAddresses("mail.bcc.fail", "mail.bcc.success");
		}

		mimeMessage.setTo(to).setSubject(subject);
		if (!Collections3.isEmpty(cc)) {
			mimeMessage.setCc(cc);
		}
		if (!Collections3.isEmpty(bcc)) {
			mimeMessage.setBcc(bcc);
		}

		return mimeMessage;
	}

	@Override
	public void send(MailMimeMessages mimeMessage) {
		sender.send(mimeMessage);
	}

	@Override
	public void sendHtml(boolean success, String html) {
		final MailMimeMessages mimeMessage = createMimeMessage(false, success).setText(html, true);
		send(mimeMessage);
	}

	@Override
	public void sendHtml(boolean success, String html, File... attachments) {
		if (Collections3.isEmpty(attachments)) {
			sendHtml(success, html);
			return;
		}

		final MailMimeMessages mimeMessage = createMimeMessage(true, success).setText(html, true);
		for (File attachment : attachments) {
			mimeMessage.addAttachment(attachment.getName(), attachment);
		}
		send(mimeMessage);
	}

	@Override
	public void sendText(boolean success, String text) {
		final MailMimeMessages mimeMessage = createMimeMessage(false, success).setText(text, false);
		send(mimeMessage);
	}

	@Override
	public void sendText(boolean success, String text, File... attachments) {
		if (Collections3.isEmpty(attachments)) {
			sendText(success, text);
			return;
		}

		final MailMimeMessages mimeMessage = createMimeMessage(true, success).setText(text, false);
		for (File attachment : attachments) {
			mimeMessage.addAttachment(attachment.getName(), attachment);
		}
		send(mimeMessage);
	}

	@Override
	public void sendException(Throwable t) {
		sendText(false, Exceptions.getStackTraceString(t));
	}

	@Override
	public void sendException(Throwable t, File... attachments) {
		sendText(false, Exceptions.getStackTraceString(t), attachments);
	}

	private String[] getAddresses(String key, String defaultKey) {
		final String[] values = getAddresses(key);
		return !Collections3.isEmpty(values) ? values : getAddresses(defaultKey);
	}

	private String[] getAddresses(String key) {
		final String value = pl.getString(key);
		if (StringUtils.isNotBlank(value)) {
			final String[] values = StringUtils.split(value, ",");
			if (!Collections3.isEmpty(values)) {
				return values;
			}
		}

		return ArrayUtils.EMPTY_STRING_ARRAY;
	}

	private String getSubject(boolean success) {
		final String templateString = pl.getString("mail.subject");
		final Map<String, String> model = Maps.newHashMap();
		model.put("result", success ? "success" : "fail");
		model.put("date", DATE_FORMAT.format(new Date()));
		return FreeMarkers.render(templateString, model);
	}

	private MailSenders createSender() {
		if (!pl.getBoolean("mail.on")) {
			return NopMailSenders.INSTANCE;
		}

		final MailSenders sender = Mails.createSender().setHost(pl.getString("mail.host"))
				.setPort(pl.getInteger("mail.port")).setUsername(pl.getString("mail.username"))
				.setPassword(Cryptos.aesDecryptDefault(pl.getString("mail.password")));

		final String mailProperties = pl.getString("mail.properties");
		if (StringUtils.isNotBlank(mailProperties)) {
			final String[] mailPropertiesEntries = StringUtils.split(mailProperties, ",");
			if (!Collections3.isEmpty(mailPropertiesEntries)) {
				for (String mailPropertiesEntry : mailPropertiesEntries) {
					final String[] mailPropertyPair = StringUtils.split(mailPropertiesEntry, "|");
					Validate.isTrue(mailPropertyPair.length == 2,
							"mail.properties should be: <key>|<value>, but now it is: " + mailPropertyPair);
					final String key = mailPropertyPair[0];
					final String value = mailPropertyPair[1];
					sender.setJavaMailProperty(key, value);
				}
			}
		}

		return sender;
	}
}
