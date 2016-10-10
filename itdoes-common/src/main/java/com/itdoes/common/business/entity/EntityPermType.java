package com.itdoes.common.business.entity;

/**
 * @author Jalen Zhong
 */
public enum EntityPermType {
	/**
	 * <pre>
	 * All: includes all types
	 * READ: includes FIND, FIND_ONE, COUNT, GET
	 * WRITE: includes DELETE, POST, PUT, POST_UPLOAD, PUT_UPLOAD
	 * <other>: specific command
	 * </pre>
	 */
	ALL, READ, WRITE, FIND, FIND_ONE, COUNT, GET, DELETE, POST, PUT, POST_UPLOAD, PUT_UPLOAD;
}
