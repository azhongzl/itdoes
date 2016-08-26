package com.itdoes.common.core.web;

import org.springframework.http.HttpStatus;

import com.itdoes.common.core.Result;

/**
 * @author Jalen Zhong
 */
public class HttpResults {
	public static Result success() {
		return Result.success().setCode(HttpStatus.OK.value());
	}

	public static Result success(Object data) {
		return success().setData(data);
	}

	public static Result fail(int code, Object message) {
		return Result.fail().setCode(code).setMessage(message);
	}

	private HttpResults() {
	}
}
