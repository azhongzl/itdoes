package com.itdoes.common.core.spring;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.context.ApplicationContext;

/**
 * @author Jalen Zhong
 */
public class LazyInitBeanLoader {
	private static final LazyInitBeanLoader INSTANCE = new LazyInitBeanLoader();

	public static LazyInitBeanLoader getInstance() {
		return INSTANCE;
	}

	private LazyInitBeanLoader() {
	}

	public Object loadBean(ApplicationContext context, String beanName, Class<?>[] interfaces) {
		return Proxy.newProxyInstance(context.getClassLoader(), interfaces, new BeanProxyHandler(context, beanName));
	}

	private static class BeanProxyHandler implements InvocationHandler {
		private final ApplicationContext context;
		private final String beanName;

		private BeanProxyHandler(ApplicationContext context, String beanName) {
			this.context = context;
			this.beanName = beanName;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return method.invoke(context.getBean(beanName), args);
		}
	}
}
