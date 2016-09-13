package com.itdoes.common.business.dao.loader;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.springframework.context.ApplicationContext;

import com.itdoes.common.business.dao.BaseDao;
import com.itdoes.common.business.dao.DaoLoader;

/**
 * @author Jalen Zhong
 */
public class LazyDaoLoader implements DaoLoader {
	private static LazyDaoLoader INSTANCE = new LazyDaoLoader();

	public static LazyDaoLoader getInstance() {
		return INSTANCE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, ID extends Serializable> BaseDao<T, ID> load(ApplicationContext context, String beanId) {
		final DaoProxy proxy = new DaoProxy(context, beanId);
		return (BaseDao<T, ID>) Proxy.newProxyInstance(context.getClassLoader(), new Class[] { BaseDao.class }, proxy);
	}

	private static class DaoProxy implements InvocationHandler {
		private final ApplicationContext context;
		private final String beanId;

		private DaoProxy(ApplicationContext context, String beanId) {
			this.context = context;
			this.beanId = beanId;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			return method.invoke(context.getBean(beanId), args);
		}
	}
}
