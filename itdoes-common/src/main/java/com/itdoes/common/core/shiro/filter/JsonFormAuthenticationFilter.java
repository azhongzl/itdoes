package com.itdoes.common.core.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.itdoes.common.core.shiro.Shiros;
import com.itdoes.common.core.web.Webs;

/**
 * @author Jalen Zhong
 */
public class JsonFormAuthenticationFilter extends FormAuthenticationFilter {
	@Override
	protected void saveRequest(ServletRequest request) {
		final HttpServletRequest httpRequest = WebUtils.toHttp(request);
		if (Webs.isAjaxRequest(httpRequest)) {
			Shiros.saveRequestAjax(request);
		} else {
			super.saveRequest(httpRequest);
		}
	}

	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		final HttpServletRequest httpRequest = WebUtils.toHttp(request);
		if (Webs.isAjaxRequest(httpRequest)) {
			Shiros.redirectToLoginAjax(request, response, getLoginUrl());
		} else {
			super.redirectToLogin(httpRequest, response);
		}
	}

	@Override
	protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
		Shiros.issueSuccessRedirectAjax(request, response, getSuccessUrl());
	}
}
