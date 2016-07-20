package com.itdoes.business.service;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class ShiroDbRealm extends AuthorizingRealm {
	private AccountService accountService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
		User user = accountService.findUserByLoginName(shiroUser.loginName);
		SimpleAuthorizationInfo info = new SImpleAuthorizationInfo();
		for (Role role : user.getRoleList()) {
			info.addRole(role.getName());
			info.addStringPermissions(role.getPermissionList());
		}
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authcToken;
        User user = accountService.findUserByLoginName(token.getUsername());
        if(user == null){
            return null;
        }

        if("disabled".equals(user.getStatus())){
            throw new DisabledAccountException();
        }

        byte[] salt = Codecs.decodeHex(user.getSalt());
        return new SimpleAuthenticationInfo(new ShiroUser(user.getLoginName(), user.getName()), user.getPassword(), ByteSource.Util.byte(salt), getName());
	}

	@PostConstruct
	public void initCredenticalsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(AccountService.HASH_ALGORITHM);
		matcher.setHashIterations(AccountService.HASH_ITERATIONS);
		setCredentialsMatcher(matcher);
	}
}
