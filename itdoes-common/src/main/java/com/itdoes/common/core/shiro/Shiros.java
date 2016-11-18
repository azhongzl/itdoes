package com.itdoes.common.core.shiro;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;

import com.itdoes.common.core.util.Reflections;
import com.itdoes.common.core.util.Urls;

/**
 * @author Jalen Zhong
 */
public class Shiros {
	public static final int SC_UNAUTHENTICATED = 499;

	public static final String SAVED_REQUEST_KEY = WebUtils.SAVED_REQUEST_KEY;

	public static final String SUCCESS_URL_KEY = "successUrl";

	public static void saveRequestAjax(ServletRequest request) {
		final Subject subject = SecurityUtils.getSubject();
		final Session session = subject.getSession();
		final SavedRequest savedRequest = new AjaxSavedRequest(WebUtils.toHttp(request));
		session.setAttribute(SAVED_REQUEST_KEY, savedRequest);
	}

	public static void redirectToLoginAjax(ServletRequest request, ServletResponse response, String loginUrl)
			throws IOException {
		final HttpServletResponse httpResponse = WebUtils.toHttp(response);
		httpResponse.sendError(SC_UNAUTHENTICATED, loginUrl);
	}

	public static void issueSuccessRedirectAjax(ServletRequest request, ServletResponse response, String fallbackUrl)
			throws IOException {
		String successUrl = null;
		boolean contextRelative = true;

		// 1) Use "successUrl" provided in current login request
		successUrl = request.getParameter(SUCCESS_URL_KEY);
		if (successUrl != null) {
			contextRelative = Urls.isRelative(successUrl);
		}

		// 2) Use accessed request url cached in previous session
		if (successUrl == null) {
			final SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);
			if (savedRequest != null && savedRequest.getMethod().equalsIgnoreCase(AccessControlFilter.GET_METHOD)) {
				if (savedRequest instanceof AjaxSavedRequest) {
					successUrl = ((AjaxSavedRequest) savedRequest).getReferer();
					contextRelative = false;
				} else {
					successUrl = savedRequest.getRequestUrl();
					contextRelative = false;
				}
			}
		}

		// 3) Use pre-configured "successUrl" defined in shiro xml
		if (successUrl == null) {
			successUrl = fallbackUrl;
			if (successUrl != null) {
				contextRelative = Urls.isRelative(successUrl);
			}
		}

		if (successUrl == null) {
			throw new IllegalStateException("Success URL not available via saved request or via the "
					+ "successUrlFallback method parameter. One of these must be non-null for "
					+ "issueSuccessRedirect() to work.");
		}

		WebUtils.issueRedirect(request, response, successUrl, null, contextRelative);
	}

	public static ShiroUser getShiroUser(Principal principal) {
		final ShiroUser shiroUser = (ShiroUser) Reflections.invokeGet(principal, "object");
		return shiroUser;
	}

	private Shiros() {
	}
}
