package com.itdoes.common.core.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

/**
 * @author Jalen Zhong
 */
public class Urls {
	public static URI toUri(String urlString) {
		try {
			final URL url = createUrl(urlString);
			final URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
					url.getQuery(), url.getRef());
			return uri;
		} catch (Throwable t) {
			throw Exceptions.unchecked(t, IllegalArgumentException.class);
		}
	}

	public static URL createUrl(String urlString) {
		Validate.notNull(urlString, "urlString is null");

		try {
			return new URL(urlString);
		} catch (MalformedURLException e) {
			throw Exceptions.unchecked(e, IllegalArgumentException.class);
		}
	}

	public static String concat(String beforeUrlString, String afterUrlString) {
		Validate.notNull(beforeUrlString, "beforeUrlString is null");
		Validate.notNull(afterUrlString, "afterUrlString is null");

		if (!isRelative(afterUrlString)) {
			return afterUrlString;
		}

		if (StringUtils.isBlank(beforeUrlString)) {
			return afterUrlString;
		}

		if (beforeUrlString.endsWith("/")) {
			if (afterUrlString.startsWith("/")) {
				return beforeUrlString + afterUrlString.substring(1);
			} else {
				return beforeUrlString + afterUrlString;
			}
		} else {
			if (afterUrlString.startsWith("/")) {
				return beforeUrlString + afterUrlString;
			} else {
				return beforeUrlString + "/" + afterUrlString;
			}
		}
	}

	public static String concat(String... urls) {
		Validate.isTrue(!Collections3.isEmpty(urls), "Urls are empty");

		if (urls.length == 1) {
			return urls[0];
		} else if (urls.length == 2) {
			return concat(urls[0], urls[1]);
		} else {
			String result = concat(urls[0], urls[1]);
			for (int i = 2; i < urls.length; i++) {
				result = concat(result, urls[i]);
			}
			return result;
		}
	}

	public static boolean isRelative(String urlString) {
		Validate.notNull(urlString, "urlString is null");
		return urlString.indexOf("://") == -1;
	}

	private Urls() {
	}
}
