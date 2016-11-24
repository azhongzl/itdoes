package com.itdoes.common.business.entity;

import java.util.List;

import com.itdoes.common.core.util.Collections3;

/**
 * @author Jalen Zhong
 */
public enum EntityPermType {
	/**
	 * <pre>
	 * All: includes all types
	 * READ: includes FIND, FIND_ALL, FIND_ONE, COUNT, GET
	 * WRITE: includes DELETE, POST, PUT, POST_UPLOAD, PUT_UPLOAD
	 * [other]: specific command
	 * </pre>
	 */
	ALL, READ, WRITE, FIND(Command.FIND), FIND_ALL(Command.FIND_ALL), FIND_ONE(Command.FIND_ONE), COUNT(Command.COUNT), GET(Command.GET), DELETE(Command.DELETE), POST(Command.POST), PUT(Command.PUT), POST_UPLOAD(Command.POST_UPLOAD), PUT_UPLOAD(Command.PUT_UPLOAD);

	public static interface Command {
		String FIND = "find";
		String FIND_ALL = "findAll";
		String FIND_ONE = "findOne";
		String COUNT = "count";
		String GET = "get";
		String DELETE = "delete";
		String POST = "post";
		String PUT = "put";
		String POST_UPLOAD = "postUpload";
		String PUT_UPLOAD = "putUpload";
	}

	private final String command;

	private EntityPermType(String command) {
		this.command = command;
	}

	private EntityPermType() {
		this(null);
	}

	public String getCommand() {
		return command;
	}

	public boolean isIn(List<EntityPermType> typeList) {
		if (Collections3.isEmpty(typeList)) {
			return false;
		}

		if (typeList.contains(this)) {
			return true;
		}
		if (typeList.contains(ALL)) {
			return true;
		}
		if (typeList.contains(READ)) {
			return this == FIND || this == FIND_ALL || this == FIND_ONE || this == COUNT || this == GET;
		}
		if (typeList.contains(WRITE)) {
			return this == DELETE || this == POST || this == PUT || this == POST_UPLOAD || this == PUT_UPLOAD;
		}

		return false;
	}
}
