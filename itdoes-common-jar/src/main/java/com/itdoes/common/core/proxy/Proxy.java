package com.itdoes.common.core.proxy;

/**
 * @author Jalen Zhong
 */
public interface Proxy {
	Object doProxy(ProxyChain proxyChain) throws Throwable;
}
