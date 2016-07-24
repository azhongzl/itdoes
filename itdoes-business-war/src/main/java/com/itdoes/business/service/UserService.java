package com.itdoes.business.service;

import org.springframework.stereotype.Service;

import com.itdoes.business.entity.EmployeeMaster;

/**
 * @author Jalen Zhong
 */
@Service
public class UserService extends BaseService {
	public EmployeeMaster findUser(String username) {
		// TODO
		final EmployeeMaster user = new EmployeeMaster();
		user.setEmployeeId(1);
		user.setLoginId(username);
		user.setPassword(username);
		return user;
	}
}
