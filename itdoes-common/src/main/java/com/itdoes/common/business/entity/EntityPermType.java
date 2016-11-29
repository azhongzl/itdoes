package com.itdoes.common.business.entity;

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

	private static final EntityPermType[] READ_COMMAND_TYPES = new EntityPermType[] { FIND, FIND_ALL, FIND_ONE, COUNT,
			GET };
	private static final EntityPermType[] WRITE_COMMAND_TYPES = new EntityPermType[] { DELETE, POST, PUT, POST_UPLOAD,
			PUT_UPLOAD };
	private static final EntityPermType[] ALL_COMMAND_TYPES = new EntityPermType[READ_COMMAND_TYPES.length
			+ WRITE_COMMAND_TYPES.length];
	static {
		int index = 0;
		for (int i = 0; i < READ_COMMAND_TYPES.length; i++, index++) {
			ALL_COMMAND_TYPES[i] = READ_COMMAND_TYPES[i];
		}
		for (int i = 0; i < WRITE_COMMAND_TYPES.length; i++) {
			ALL_COMMAND_TYPES[i + index] = WRITE_COMMAND_TYPES[i];
		}
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

	public EntityPermType[] getCommandTypes() {
		if (this == ALL) {
			return ALL_COMMAND_TYPES;
		} else if (this == READ) {
			return READ_COMMAND_TYPES;
		} else if (this == WRITE) {
			return WRITE_COMMAND_TYPES;
		} else {
			return new EntityPermType[] { this };
		}
	}
}
