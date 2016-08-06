package com.itdoes.common.business;

import org.springframework.http.HttpStatus;

/**
 * @author Jalen Zhong
 */
public class Result {
	public static Result success(Object[] data) {
		return new Result(HttpStatus.OK.value(), null, data);
	}

	public static Result fail(int code, Object message) {
		return new Result(code, message, null);
	}

	private final int code;
	private final Object message;
	private final Object[] data;

	private Result(int code, Object message, Object[] data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public Object getMessage() {
		return message;
	}

	public Object[] getData() {
		return data;
	}
}
