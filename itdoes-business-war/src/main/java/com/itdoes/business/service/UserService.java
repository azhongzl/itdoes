package com.itdoes.business.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itdoes.business.entity.EmployeeMaster;

/**
 * @author Jalen Zhong
 */
@Component
@Transactional(readOnly = true)
public class UserService {
	public EmployeeMaster findUser(String username) {
		// TODO
		final EmployeeMaster user = new EmployeeMaster();
		user.setEmployeeId(1);
		user.setLoginId(username);
		user.setPassword(username);
		return user;
	}
}
