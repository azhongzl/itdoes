package com.itdoes.business.service;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	FacadeService facadeService;

	@PostConstruct
	public void initRoles() {
		for (String ec : facadeService.getEntityClassSimpleNames()) {
			ROLE_SYSTEM.getPermissionList().add(ec);
		}

		ROLE_USER.getPermissionList().add("TempInvCompany");
	}

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

	private static final TempRole ROLE_SYSTEM = new TempRole("system");
	private static final TempRole ROLE_USER = new TempRole("user");

	private static final TempUser JALEN = new TempUser();
	private static final TempUser USER = new TempUser();
	static {
		JALEN.setUsername("jalen");
		JALEN.setPlainPassword("jalen");
		JALEN.setStatus("A");
		JALEN.getRoleList().add(ROLE_SYSTEM);
		encryptUser(JALEN);

		USER.setUsername("user");
		USER.setPlainPassword("user");
		USER.setStatus("A");
		USER.getRoleList().add(ROLE_USER);
		encryptUser(USER);
	}

	private static void encryptUser(TempUser user) {
		final byte[] salt = Digests.generateSalt();
		user.setSalt(Codecs.hexEncode(salt));
		final byte[] hashedPassword = Digests.sha256(user.getPlainPassword().getBytes(), salt);
		user.setPassword(Codecs.hexEncode(hashedPassword));
	}
}
