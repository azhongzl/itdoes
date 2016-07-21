package com.itdoes.business.service;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;

import com.itdoes.business.entity.EmployeeMaster;
import com.itdoes.common.shiro.AbstractShiroRealm;

/**
 * @author Jalen Zhong
 */
public class ShiroDbRealm extends AbstractShiroRealm {
	private UserService userService;

	@Override
	protected AuthenticationInfo doAuthentication(UsernamePasswordToken token) throws AuthenticationException {
		EmployeeMaster user = userService.findUser(token.getUsername());
		if (user == null) {
			return null;
		}

		// TODO Check user status here
		if ("disabled".equals(user.getStatus())) {
			throw new DisabledAccountException();
		}

		// TODO Salt should be enabled in future
		// byte[] salt = Codecs.decodeHex(user.getSalt());
		// return new SimpleAuthenticationInfo(user, user.getPassword(),
		// ByteSource.Util.bytes(salt), getName());
		return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
	}

	@Override
	protected AuthorizationInfo doAuthorization(Object principal) {
		// TODO Roles and permissions should be derived from user
		// EmployeeMaster user = (EmployeeMaster) principal;
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addRole("admin");
		info.addStringPermission("user:edit");
		return info;
	}

	@Override
	protected String getHashAlgrithm() {
		return "SHA-256";
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
