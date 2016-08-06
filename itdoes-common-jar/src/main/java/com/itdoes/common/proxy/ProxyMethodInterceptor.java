package com.itdoes.common.proxy;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * @author Jalen Zhong
 */
public class ProxyMethodInterceptor implements MethodInterceptor {
	private final List<Proxy> proxyList;

	public ProxyMethodInterceptor(List<Proxy> proxyList) {
		this.proxyList = proxyList;
	}

	@Override
	public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		return new ProxyChain(object, method, args, methodProxy, proxyList).doProxyChain();
	}
}
