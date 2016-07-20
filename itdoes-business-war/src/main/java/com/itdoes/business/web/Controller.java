package com.itdoes.business.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itdoes.business.entity.InvCompany;
import com.itdoes.business.service.InvCompanyService;

@RestController
@RequestMapping(value = "/")
public class Controller {
	@Autowired
	private InvCompanyService invCompanyService;

	@RequestMapping(value = "search", method = RequestMethod.GET)
	public Result search() {
		List<InvCompany> list = invCompanyService.getAll();
		return Result.success(list.toArray(new InvCompany[list.size()]));
	}
}
