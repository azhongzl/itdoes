package com.itdoes.business.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.itdoes.business.service.TempUser;
import com.itdoes.business.service.UserService;

/**
 * @author Jalen Zhong
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		List<TempUser> users = userService.getAllUsers();
		model.addAttribute("users", users);

		return "userList";
	}
}
