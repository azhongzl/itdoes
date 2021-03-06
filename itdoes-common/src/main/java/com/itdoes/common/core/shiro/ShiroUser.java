package com.itdoes.common.core.shiro;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Jalen Zhong
 */
public class ShiroUser implements Serializable {
	private static final long serialVersionUID = 3843898429648345540L;

	private final String id;
	private final String username;

	public ShiroUser(String id, String username) {
		this.id = id;
		this.username = username;
	}

	public String getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!getClass().equals(obj.getClass())) {
			return false;
		}
		final ShiroUser other = (ShiroUser) obj;
		return username == null ? other.username == null : username.equals(other.username);
	}

	@Override
	public String toString() {
		return id;
	}
}
