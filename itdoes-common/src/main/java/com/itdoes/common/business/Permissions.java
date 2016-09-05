package com.itdoes.common.business;

/**
 * @author Jalen Zhong
 */
public class Permissions {
	private static final char ENTITY_FIELD_SEPARATOR = '.';
	private static final char PERM_SEPARATOR = ':';
	private static final String PERM_READ = "read";
	private static final String PERM_WRITE = "write";
	private static final String PERM_ANY = "*";

	public static String getEntityAllPermission(String entityName) {
		return entityName;
	}

	public static String getEntityPermission(String entityName, String command) {
		return entityName + PERM_SEPARATOR + command;
	}

	public static String getFieldAllPermission(String entityName, String fieldName) {
		return getFieldPermission(entityName, fieldName, PERM_ANY);
	}

	public static String getFieldReadPermission(String entityName, String fieldName) {
		return getFieldPermission(entityName, fieldName, PERM_READ);
	}

	public static String getFieldWritePermission(String entityName, String fieldName) {
		return getFieldPermission(entityName, fieldName, PERM_WRITE);
	}

	private static String getFieldPermission(String entityName, String fieldName, String mode) {
		return entityName + ENTITY_FIELD_SEPARATOR + fieldName + PERM_SEPARATOR + mode;
	}

	private Permissions() {
	}
}
