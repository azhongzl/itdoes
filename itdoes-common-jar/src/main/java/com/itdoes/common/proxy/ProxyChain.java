package com.itdoes.common.proxy;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.cglib.proxy.MethodProxy;

/**
 * @author Jalen Zhong
 */
public class ProxyChain {
	private final Object object;
	private final Method method;
	private final Object[] args;
	private final MethodProxy methodProxy;

	private List<Proxy> proxyList;
	private int proxyIndex = 0;

	public ProxyChain(Object object, Method method, Object[] args, MethodProxy methodProxy, List<Proxy> proxyList) {
		this.object = object;
		this.method = method;
		this.args = args;
		this.methodProxy = methodProxy;
		this.proxyList = proxyList;
	}

	public Object getObject() {
		return object;
	}

	public Method getMethod() {
		return method;
	}

	public Object[] getArgs() {
		return args;
	}

	public MethodProxy getMethodProxy() {
		return methodProxy;
	}

	public List<Proxy> getProxyList() {
		return proxyList;
	}

	public Object doProxyChain() throws Throwable {
		if (proxyIndex < proxyList.size()) {
			return proxyList.get(proxyIndex++).doProxy(this);
		} else {
			return methodProxy.invokeSuper(object, args);
		}
	}
}
