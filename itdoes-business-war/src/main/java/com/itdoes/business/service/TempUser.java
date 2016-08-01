package com.itdoes.business.service;

import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.itdoes.common.business.BaseBean;

/**
 * @author Jalen Zhong
 */
public class TempUser extends BaseBean {
	private static final long serialVersionUID = -4308946657484267535L;

	@NotBlank
	private String username;
	@JsonIgnore
	private String plainPassword;
	private String password;
	private String salt;
	private String status;
	private List<TempRole> roleList = Lists.newArrayList();

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<TempRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<TempRole> roleList) {
		this.roleList = roleList;
	}

	public static boolean isActive(TempUser user) {
		return user.getStatus().equals("A");
	}
}
