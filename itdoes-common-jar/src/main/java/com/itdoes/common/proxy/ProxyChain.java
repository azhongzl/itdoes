package com.itdoes.common.proxy;

import java.lang.reflect.Method;
import java.util.List;

import org.springframework.cglib.proxy.MethodProxy;

/**
 * @author Jalen Zhong
 */
public class ProxyChain {
	private final Class<?> targetClass;
	private final List<Proxy> proxyList;
	private final Object object;
	private final Method method;
	private final Object[] args;
	private final MethodProxy methodProxy;

	private int proxyIndex = 0;

	public ProxyChain(Class<?> targetClass, List<Proxy> proxyList, Object object, Method method, Object[] args,
			MethodProxy methodProxy) {
		this.targetClass = targetClass;
		this.proxyList = proxyList;
		this.object = object;
		this.method = method;
		this.args = args;
		this.methodProxy = methodProxy;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public List<Proxy> getProxyList() {
		return proxyList;
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

	public Object doProxyChain() throws Throwable {
		if (proxyIndex < proxyList.size()) {
			return proxyList.get(proxyIndex++).doProxy(this);
		} else {
			return methodProxy.invokeSuper(object, args);
		}
	}
}
