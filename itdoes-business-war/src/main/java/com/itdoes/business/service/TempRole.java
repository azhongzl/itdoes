package com.itdoes.business.service;

import java.util.List;

import com.google.common.collect.Lists;
import com.itdoes.common.business.BaseEntity;

/**
 * @author Jalen Zhong
 */
public class TempRole extends BaseEntity {
	private final String name;

	private final List<String> permissionList = Lists.newArrayList();

	public TempRole(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public List<String> getPermissionList() {
		return permissionList;
	}
}
