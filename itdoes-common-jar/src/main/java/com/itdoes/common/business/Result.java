package com.itdoes.common.business;

import org.springframework.http.HttpStatus;

/**
 * @author Jalen Zhong
 */
public class Result {
	public static Result success(Object[] data) {
		return new Result(HttpStatus.OK.value(), null, data);
	}

	private final int status;
	private final Object message;
	private final Object[] data;

	public Result(int status, Object message, Object[] data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public Object getMessage() {
		return message;
	}

	public Object[] getData() {
		return data;
	}
}
