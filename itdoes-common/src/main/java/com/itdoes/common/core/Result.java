package com.itdoes.common.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author Jalen Zhong
 */
@JsonPropertyOrder({ "success", "code", "message", "data" })
public class Result implements Serializable {
	private static final long serialVersionUID = 3897944851811754407L;

	public static Result success() {
		return new Result(true, 0, null);
	}

	public static Result fail(int code, Object message) {
		return new Result(false, code, message);
	}

	private final boolean success;
	private final int code;
	private final Object message;
	private Map<Object, Object> data;

	public Result(boolean success, int code, Object message) {
		this.success = success;
		this.code = code;
		this.message = message;
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

	public Map<Object, Object> getData() {
		return data;
	}

	public Result addData(Object key, Object value) {
		if (data == null) {
			data = new HashMap<>();
		}
		data.put(key, value);
		return this;
	}
}
