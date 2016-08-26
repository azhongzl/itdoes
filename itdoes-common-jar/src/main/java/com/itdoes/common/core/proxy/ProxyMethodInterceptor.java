package com.itdoes.common.core.proxy;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

/**
 * @author Jalen Zhong
 */
public class ProxyMethodInterceptor implements MethodInterceptor {
	private final Class<?> targetClass;
	private final List<Proxy> proxyList;

	public ProxyMethodInterceptor(Class<?> targetClass, List<Proxy> proxyList) {
		this.targetClass = targetClass;
		this.proxyList = proxyList;
	}

	@Override
	public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
		return new ProxyChain(targetClass, proxyList, object, method, args, methodProxy).doProxyChain();
	}
}
