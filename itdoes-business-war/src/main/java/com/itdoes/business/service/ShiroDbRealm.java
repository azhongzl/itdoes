package com.itdoes.business.service;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.util.ByteSource;

import com.itdoes.common.shiro.AbstractShiroRealm;
import com.itdoes.common.util.Codecs;

/**
 * @author Jalen Zhong
 */
public class ShiroDbRealm extends AbstractShiroRealm {
	private UserService userService;

	@Override
	protected AuthenticationInfo doAuthentication(UsernamePasswordToken token) throws AuthenticationException {
		final TempUser user = userService.findUser(token.getUsername());
		if (user == null) {
			return null;
		}

		if (!TempUser.isActive(user)) {
			throw new DisabledAccountException();
		}

		final byte[] salt = Codecs.hexDecode(user.getSalt());
		return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(salt), getName());
	}

	@Override
	protected AuthorizationInfo doAuthorization(Object principal) {
		final TempUser user = (TempUser) principal;
		final SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		for (TempRole role : user.getRoleList()) {
			info.addRole(role.getName());
			for (String permission : role.getPermissionList()) {
				info.addStringPermission(permission);
			}
		}
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
