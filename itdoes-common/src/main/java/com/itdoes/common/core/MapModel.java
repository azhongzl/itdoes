package com.itdoes.common.core;

import java.util.HashMap;

/**
 * @author Jalen Zhong
 */
public class MapModel extends HashMap<Object, Object> {
	private static final long serialVersionUID = -8078539341799761187L;

	private static final MapModel EMPTY_MAP_MODEL = new MapModel();

	public static MapModel emptyMapModel() {
		return EMPTY_MAP_MODEL;
	}
}
