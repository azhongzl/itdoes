package com.itdoes.common.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * @author Jalen Zhong
 */
public class Urls {
	public static URI toUri(String urlString) {
		try {
			final URL url = new URL(urlString);
			final URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
					url.getQuery(), url.getRef());
			return uri;
		} catch (Throwable t) {
			throw Exceptions.unchecked(t, IllegalArgumentException.class);
		}
	}

	public static URL createUrl(String urlString) {
		try {
			return new URL(urlString);
		} catch (MalformedURLException e) {
			throw Exceptions.unchecked(e, IllegalArgumentException.class);
		}
	}

	private Urls() {
	}
}
