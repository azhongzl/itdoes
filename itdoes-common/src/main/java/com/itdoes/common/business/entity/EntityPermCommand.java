package com.itdoes.common.business.entity;

/**
 * @author Jalen Zhong
 */
public enum EntityPermCommand {
	/**
	 * <pre>
	 * All: includes all commands
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

	private static final EntityPermCommand[] READ_COMMANDS = new EntityPermCommand[] { FIND, FIND_ALL, FIND_ONE, COUNT,
			GET };
	private static final EntityPermCommand[] WRITE_COMMANDS = new EntityPermCommand[] { DELETE, POST, PUT, POST_UPLOAD,
			PUT_UPLOAD };
	private static final EntityPermCommand[] ALL_COMMANDS = new EntityPermCommand[READ_COMMANDS.length
			+ WRITE_COMMANDS.length];
	static {
		int index = 0;
		for (int i = 0; i < READ_COMMANDS.length; i++, index++) {
			ALL_COMMANDS[i] = READ_COMMANDS[i];
		}
		for (int i = 0; i < WRITE_COMMANDS.length; i++) {
			ALL_COMMANDS[i + index] = WRITE_COMMANDS[i];
		}
	}

	private final String command;

	private EntityPermCommand(String command) {
		this.command = command;
	}

	private EntityPermCommand() {
		this(null);
	}

	public String getCommand() {
		return command;
	}

	public EntityPermCommand[] getSubCommands() {
		if (this.equals(ALL)) {
			return ALL_COMMANDS;
		} else if (this.equals(READ)) {
			return READ_COMMANDS;
		} else if (this.equals(WRITE)) {
			return WRITE_COMMANDS;
		} else {
			return new EntityPermCommand[] { this };
		}
	}
}
