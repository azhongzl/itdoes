package com.itdoes.common.core.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Jalen Zhong
 */
public class Nets {
	public static String getLocalHost() {
		try {
			final InetAddress address = InetAddress.getLocalHost();
			return address.getCanonicalHostName();
		} catch (UnknownHostException e) {
			throw Exceptions.unchecked(e);
		}
	}

	private Nets() {
	}
}
