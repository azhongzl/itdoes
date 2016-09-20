package com.itdoes.common.core.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Jalen Zhong
 */
public class Nets {
	public static InetAddress getLocalHost() {
		try {
			return InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			throw Exceptions.unchecked(e);
		}
	}

	private Nets() {
	}
}
