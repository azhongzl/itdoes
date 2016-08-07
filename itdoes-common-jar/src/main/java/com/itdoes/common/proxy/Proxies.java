package com.itdoes.common.proxy;

import java.util.List;

import org.springframework.cglib.proxy.Enhancer;

/**
 * @author Jalen Zhong
 */
public class Proxies {
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(Class<?> targetClass, List<Proxy> proxyList) {
		return (T) Enhancer.create(targetClass, new ProxyMethodInterceptor(targetClass, proxyList));
	}

	private Proxies() {
	}
}
