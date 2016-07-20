package com.itdoes.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.itdoes.business.entity.InvCompany;
import com.itdoes.business.repository.InvCompanyDao;

@Component
@Transactional
public class InvCompanyService {
	@Autowired
	private InvCompanyDao invCompanyDao;

	public List<InvCompany> getAll() {
		return (List) invCompanyDao.findAll();
	}
}
