package com.itdoes.common.proxy;

/**
 * @author Jalen Zhong
 */
public interface Proxy {
	Object doProxy(ProxyChain proxyChain) throws Throwable;
}
