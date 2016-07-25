package com.itdoes.common.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.tags.PermissionTag;

/**
 * @author Jalen Zhong
 */
public class HasAnyPermissionsTag extends PermissionTag {
	private static final long serialVersionUID = -3908313572475658927L;

	private static final String PERMISSION_SEPARATOR = ",";

	@Override
	protected boolean showTagBody(String permissionsString) {
		final Subject subject = getSubject();
		if (subject != null) {
			final String[] permissions = permissionsString.split(PERMISSION_SEPARATOR);
			for (String permission : permissions) {
				if (subject.isPermitted(permission.trim())) {
					return true;
				}
			}
		}

		return false;
	}
}
