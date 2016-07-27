package com.itdoes.business.service;

import java.util.List;

import com.google.common.collect.Lists;
import com.itdoes.common.business.BaseEntity;

/**
 * @author Jalen Zhong
 */
public class TempRole extends BaseEntity {
	private String name;

	private List<String> permissionList = Lists.newArrayList();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getPermissionList() {
		return permissionList;
	}

	public void setPermissionList(List<String> permissionList) {
		this.permissionList = permissionList;
	}
}
