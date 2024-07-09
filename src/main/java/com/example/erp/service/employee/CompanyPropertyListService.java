package com.example.erp.service.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.erp.entity.employee.CompanyPropertyList;
import com.example.erp.repository.employee.CompanyPropertyListRepository;

@Service
public class CompanyPropertyListService {
	
	@Autowired
	private CompanyPropertyListRepository repo;
	
	public void save(CompanyPropertyList companyPropertyList) {
		repo.save(companyPropertyList);
	}
	public CompanyPropertyList findById(Long companyPropertyListId) {
		return repo.findById(companyPropertyListId).get();
	}
	

}
