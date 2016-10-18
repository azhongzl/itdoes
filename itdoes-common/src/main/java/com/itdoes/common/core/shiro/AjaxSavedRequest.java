package com.itdoes.common.core.shiro;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.SavedRequest;

import com.google.common.net.HttpHeaders;

/**
 * @author Jalen Zhong
 */
public class AjaxSavedRequest extends SavedRequest {
	private static final long serialVersionUID = -8951746564990944031L;

	private final String referer;

	public AjaxSavedRequest(HttpServletRequest request) {
		super(request);

		this.referer = request.getHeader(HttpHeaders.REFERER);
	}

	public String getReferer() {
		return referer;
	}
}
