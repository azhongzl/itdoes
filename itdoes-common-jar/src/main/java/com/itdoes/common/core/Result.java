package com.itdoes.common.core;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Jalen Zhong
 */
@JsonPropertyOrder({ "success", "code", "message", "data" })
public class Result implements Serializable {
	private static final long serialVersionUID = 3897944851811754407L;

	public static Result success() {
		return new Result(true);
	}

	public static Result fail() {
		return new Result(false);
	}

	private final boolean success;
	private int code;
	private Object message;
	private Object data;

	private Result(boolean success) {
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public int getCode() {
		return code;
	}

	public Object getMessage() {
		return message;
	}

	public Object getData() {
		return data;
	}

	public Result setCode(int code) {
		this.code = code;
		return this;
	}

	public Result setMessage(Object message) {
		this.message = message;
		return this;
	}

	public Result setData(Object data) {
		this.data = data;
		return this;
	}
}
