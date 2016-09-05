package com.itdoes.common.core.proxy;

/**
 * @author Jalen Zhong
 */
public class AbstractProxy implements Proxy {
	@Override
	public Object doProxy(ProxyChain proxyChain) throws Throwable {
		begin(proxyChain);
		try {
			if (filter(proxyChain)) {
				before(proxyChain);
				final Object result = proxyChain.doProxyChain();
				after(proxyChain);
				return result;
			} else {
				return proxyChain.doProxyChain();
			}
		} catch (Throwable t) {
			error(proxyChain, t);
			throw t;
		} finally {
			end(proxyChain);
		}
	}

	protected void begin(ProxyChain proxyChain) {
	}

	protected void end(ProxyChain proxyChain) {
	}

	protected boolean filter(ProxyChain proxyChain) {
		return true;
	}

	protected void before(ProxyChain proxyChain) {
	}

	protected void after(ProxyChain proxyChain) {
	}

	protected void error(ProxyChain proxyChain, Throwable t) {
	}
}
