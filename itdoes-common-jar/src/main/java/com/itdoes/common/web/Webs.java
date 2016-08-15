package com.itdoes.common.web;

import java.util.Enumeration;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.Maps;
import com.google.common.net.HttpHeaders;
import com.itdoes.common.Constants;
import com.itdoes.common.util.Codecs;
import com.itdoes.common.util.Collections3;

import eu.bitwalker.useragentutils.UserAgent;

/**
 * @author Jalen Zhong
 */
public class Webs {
	public static final long ONE_YEAR_SECONDS = 365 * 24 * 60 * 60;

	public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
		// HTTP 1.0
		response.setDateHeader(HttpHeaders.EXPIRES, System.currentTimeMillis() + (expiresSeconds * 1000));
		// HTTP 1.1
		response.setHeader(HttpHeaders.CACHE_CONTROL, "private, max-age=" + expiresSeconds);
	}

	public static void setNoCacheHeader(HttpServletResponse response) {
		// HTTP 1.0
		response.setDateHeader(HttpHeaders.EXPIRES, 1L);
		response.addHeader(HttpHeaders.PRAGMA, "no-cache");
		// HTTP 1.1
		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0");
	}

	public static void setLastModifiedHeader(HttpServletResponse response, long lastModified) {
		response.setDateHeader(HttpHeaders.LAST_MODIFIED, lastModified);
	}

	public static void setEtagHeader(HttpServletResponse response, String etag) {
		response.setHeader(HttpHeaders.ETAG, etag);
	}

	public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response,
			long lastModified) {
		final long ifModifiedSince = request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE);
		if ((ifModifiedSince != -1) && (lastModified < (ifModifiedSince + 1000))) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		return true;
	}

	public static boolean checkIfNoneMatch(HttpServletRequest request, HttpServletResponse response, String etag) {
		final String ifNoneMatch = request.getHeader(HttpHeaders.IF_NONE_MATCH);
		if (ifNoneMatch != null) {
			boolean matched = false;
			if ("*".equals(ifNoneMatch)) {
				matched = true;
			} else {
				final StringTokenizer tokenizer = new StringTokenizer(ifNoneMatch, ",");
				while (tokenizer.hasMoreTokens()) {
					final String currentToken = tokenizer.nextToken();
					if (currentToken.trim().equals(etag)) {
						matched = true;
						break;
					}
				}
			}

			if (matched) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				// TODO Need write back?
				// response.setHeader(HttpHeaders.ETAG, etag);
				return false;
			}
		}

		return true;
	}

	public static UserAgent getUserAgent(HttpServletRequest request) {
		return UserAgent.parseUserAgentString(request.getHeader(HttpHeaders.USER_AGENT));
	}

	public static void setFileDownloadHeader(HttpServletRequest request, HttpServletResponse response,
			String filename) {
		String encodedFilename = filename.replaceAll(" ", "_");
		encodedFilename = Codecs.urlEncode(encodedFilename);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFilename
				+ "\"; filename*=" + Constants.UTF8 + "''" + encodedFilename);
	}

	public static String httpBasicEncode(String username, String password) {
		final String toBeEncoded = username + ":" + password;
		return "Basic " + Codecs.base64Encode(toBeEncoded.getBytes());
	}

	public static Map<String, Object> getRequestParamMap(HttpServletRequest request) {
		final Map<String, Object> paramMap = Maps.newLinkedHashMap();
		final Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			final String paramName = paramNames.nextElement();
			final String[] paramValues = request.getParameterValues(paramName);
			if (!Collections3.isEmpty(paramValues)) {
				if (paramValues.length == 1) {
					paramMap.put(paramName, paramValues[0]);
				} else {
					final StringBuilder sb = new StringBuilder();
					for (int i = 0; i < paramValues.length; i++) {
						sb.append(paramValues[i]);
						if (i != paramValues.length - 1) {
							sb.append(",");
						}
					}
					paramMap.put(paramName, sb.toString());
				}
			}
		}
		return paramMap;
	}

	public static void main(String[] args) {
		System.out.println(String.valueOf((char) 29));
	}

	private Webs() {
	}
}
