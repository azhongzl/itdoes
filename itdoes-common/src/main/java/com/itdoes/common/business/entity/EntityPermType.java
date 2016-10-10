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
	 * READ: includes FIND, FIND_ONE, COUNT, GET
	 * WRITE: includes DELETE, POST, PUT, POST_UPLOAD, PUT_UPLOAD
	 * <other>: specific command
	 * </pre>
	 */
	ALL, READ, WRITE, FIND, FIND_ONE, COUNT, GET, DELETE, POST, PUT, POST_UPLOAD, PUT_UPLOAD;

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
			return this == FIND || this == FIND_ONE || this == COUNT || this == GET;
		}
		if (typeList.contains(WRITE)) {
			return this == DELETE || this == POST || this == PUT || this == POST_UPLOAD || this == PUT_UPLOAD;
		}

		return false;
	}
}
