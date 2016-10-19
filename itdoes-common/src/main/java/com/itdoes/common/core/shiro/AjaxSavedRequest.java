package com.itdoes.common.core.shiro;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.SavedRequest;

import com.itdoes.common.core.web.Webs;

/**
 * @author Jalen Zhong
 */
public class AjaxSavedRequest extends SavedRequest {
	private static final long serialVersionUID = -8951746564990944031L;

	private final String referer;

	public AjaxSavedRequest(HttpServletRequest request) {
		super(request);

		this.referer = Webs.getReferer(request);
	}

	public String getReferer() {
		return referer;
	}
}
