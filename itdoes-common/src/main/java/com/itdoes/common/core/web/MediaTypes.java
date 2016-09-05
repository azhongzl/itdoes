package com.itdoes.common.core.web;

import com.itdoes.common.core.Constants;

/**
 * @author Jalen Zhong
 */
public interface MediaTypes {
	String APPLICATION_XML = "application/xml";
	String APPLICATION_XML_UTF_8 = "application/xml; charset=" + Constants.UTF8;

	String APPLICATION_JSON = "application/json";
	String APPLICATION_JSON_UTF_8 = "application/json; charset=" + Constants.UTF8;

	String APPLICATION_JAVASCRIPT = "application/javascript";
	String APPLICATION_JAVASCRIPT_UTF_8 = "application/javascript; charset=" + Constants.UTF8;

	String APPLICATION_XHTML_XML = "application/xhtml+xml";
	String APPLICATION_XHTML_XML_UTF_8 = "application/xhtml+xml; charset=" + Constants.UTF8;

	String TEXT_PLAIN = "text/plain";
	String TEXT_PLAIN_UTF_8 = "text/plain; charset=" + Constants.UTF8;

	String TEXT_XML = "text/xml";
	String TEXT_XML_UTF_8 = "text/xml; charset=" + Constants.UTF8;

	String TEXT_HTML = "text/html";
	String TEXT_HTML_UTF_8 = "text/html; charset=" + Constants.UTF8;
}
