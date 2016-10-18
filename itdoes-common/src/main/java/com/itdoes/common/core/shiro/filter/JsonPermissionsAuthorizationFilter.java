package com.itdoes.common.core.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;

import com.itdoes.common.core.shiro.Shiros;
import com.itdoes.common.core.web.Webs;

/**
 * @author Jalen Zhong
 */
public class JsonPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {
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
}
