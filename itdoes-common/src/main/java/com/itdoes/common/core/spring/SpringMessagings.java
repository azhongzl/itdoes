package com.itdoes.common.core.spring;

import java.util.Collections;
import java.util.Map;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

/**
 * @author Jalen Zhong
 */
public class SpringMessagings {
	public static Map<?, ?> getNativeHeaders(Message<?> message) {
		if (message != null) {
			final MessageHeaders messageHeaders = message.getHeaders();
			if (messageHeaders != null) {
				final Object simpConnectMessageObj = messageHeaders.get("simpConnectMessage");
				if (simpConnectMessageObj != null && simpConnectMessageObj instanceof GenericMessage) {
					final GenericMessage<?> simpConnectMessage = (GenericMessage<?>) simpConnectMessageObj;
					final MessageHeaders simpConnectMessageHeaders = simpConnectMessage.getHeaders();
					if (simpConnectMessageHeaders != null) {
						final Object nativeHeadersObj = simpConnectMessageHeaders.get("nativeHeaders");
						if (nativeHeadersObj != null && nativeHeadersObj instanceof Map) {
							return (Map<?, ?>) nativeHeadersObj;
						}
					}
				}
			}
		}

		return Collections.EMPTY_MAP;
	}

	private SpringMessagings() {
	}
}
