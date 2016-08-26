package com.itdoes.extension.mail;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.collect.Maps;
import com.itdoes.common.freemarker.FreeMarkers;
import com.itdoes.common.mail.MailMimeMessages;
import com.itdoes.common.mail.MailSenders;
import com.itdoes.common.mail.Mails;
import com.itdoes.common.mail.NopMailMimeMessages;
import com.itdoes.common.mail.NopMailSenders;
import com.itdoes.common.security.Cryptos;
import com.itdoes.common.util.Collections3;
import com.itdoes.common.util.PropertiesLoader;

/**
 * @author Jalen Zhong
 */
public class PropertiesMailHelper {
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private final PropertiesLoader pl;
	private final MailSenders sender;

	public PropertiesMailHelper(PropertiesLoader pl) {
		this.pl = pl;
		this.sender = createSender();
	}

	public MailMimeMessages createMimeMessage(boolean multipart, boolean success) {
		if (!pl.getBoolean("mail.on")) {
			return NopMailMimeMessages.INSTANCE;
		}

		final MailMimeMessages mimeMessage = Mails.createMimeMessage(sender, multipart);

		final String from = pl.getProperty("mail.from");
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

	private String[] getAddresses(String key, String defaultKey) {
		final String[] values = getAddresses(key);
		return !Collections3.isEmpty(values) ? values : getAddresses(defaultKey);
	}

	private String[] getAddresses(String key) {
		final String value = pl.getProperty(key);
		if (StringUtils.isNotBlank(value)) {
			final String[] values = StringUtils.split(value, ",");
			if (!Collections3.isEmpty(values)) {
				return values;
			}
		}

		return ArrayUtils.EMPTY_STRING_ARRAY;
	}

	private String getSubject(boolean success) {
		final String templateString = pl.getProperty("mail.subject");
		final Map<String, String> model = Maps.newHashMap();
		model.put("result", success ? "success" : "fail");
		model.put("date", toString(new Date()));
		return FreeMarkers.render(templateString, model);
	}

	private String toString(Date date) {
		return DATE_FORMAT.format(date);
	}

	private MailSenders createSender() {
		if (!pl.getBoolean("mail.on")) {
			return NopMailSenders.INSTANCE;
		}

		final MailSenders sender = Mails.createSender().setHost(pl.getProperty("mail.host"))
				.setPort(pl.getInteger("mail.port")).setUsername(pl.getProperty("mail.username"))
				.setPassword(Cryptos.aesDecryptDefault(pl.getProperty("mail.password")));

		final String mailProperties = pl.getProperty("mail.properties");
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
