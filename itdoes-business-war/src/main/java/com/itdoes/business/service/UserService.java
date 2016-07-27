package com.itdoes.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.itdoes.common.business.BaseService;
import com.itdoes.common.security.Digests;
import com.itdoes.common.util.Codecs;

/**
 * @author Jalen Zhong
 */
@Service
public class UserService extends BaseService {
	public TempUser findUser(String username) {
		if (username.equals(JALEN.getUsername())) {
			return JALEN;
		} else {
			return USER;
		}
	}

	public List<TempUser> getAllUsers() {
		return Lists.newArrayList(JALEN, USER);
	}

	private static final TempRole ROLE_SYSTEM = new TempRole();
	private static final TempRole ROLE_USER = new TempRole();
	static {
		ROLE_SYSTEM.setName("system");
		ROLE_SYSTEM.getPermissionList().add("TempPart:*,TempInvCompany:*");

		ROLE_USER.setName("user");
		ROLE_USER.getPermissionList().add("TempInvCompany:*");
	}

	private static final TempUser JALEN = new TempUser();
	private static final TempUser USER = new TempUser();
	static {
		JALEN.setUsername("jalen");
		JALEN.setPlainPassword("jalen");
		JALEN.setStatus("A");
		JALEN.getRoleList().add(ROLE_SYSTEM);

		USER.setUsername("user");
		USER.setPlainPassword("user");
		USER.setStatus("A");
		USER.getRoleList().add(ROLE_USER);

		encryptUser(JALEN);
		encryptUser(USER);
	}

	private static void encryptUser(TempUser user) {
		final byte[] salt = Digests.generateSalt();
		user.setSalt(Codecs.hexEncode(salt));
		final byte[] hashedPassword = Digests.sha256(user.getPlainPassword().getBytes(), salt);
		user.setPassword(Codecs.hexEncode(hashedPassword));
	}
}
