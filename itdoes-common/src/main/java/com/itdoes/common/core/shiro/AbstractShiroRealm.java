package com.itdoes.common.core.shiro;

import javax.annotation.PostConstruct;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author Jalen Zhong
 */
public abstract class AbstractShiroRealm extends AuthorizingRealm {
	@Override
	protected final AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		final UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		return doAuthentication(token);
	}

	@Override
	protected final AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		final Object principal = principals.getPrimaryPrincipal();
		return doAuthorization(principal);
	}

	@PostConstruct
	public final void initCredenticalsMatcher() {
		final HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(getHashAlgrithm());
		matcher.setHashIterations(getHashIterations());
		setCredentialsMatcher(matcher);
	}

	protected int getHashIterations() {
		return 1;
	}

	protected abstract AuthenticationInfo doAuthentication(UsernamePasswordToken authcToken);

	protected abstract AuthorizationInfo doAuthorization(Object principal);

	protected abstract String getHashAlgrithm();
}