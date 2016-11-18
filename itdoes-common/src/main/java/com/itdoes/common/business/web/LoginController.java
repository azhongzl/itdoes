package com.itdoes.common.business.web;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.itdoes.common.core.shiro.Shiros;

/**
 * @author Jalen Zhong
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController extends BaseController {
	@RequestMapping(method = RequestMethod.GET)
	public String login(
			@RequestParam(value = FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, required = false) String username,
			@RequestParam(value = Shiros.SUCCESS_URL_KEY, required = false) String successUrl, Model model) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute(Shiros.SUCCESS_URL_KEY, successUrl);
		return "login";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String fail(
			@RequestParam(value = FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, required = false) String username,
			@RequestParam(value = Shiros.SUCCESS_URL_KEY, required = false) String successUrl, Model model) {
		model.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		model.addAttribute(Shiros.SUCCESS_URL_KEY, successUrl);
		return "login";
	}
}
