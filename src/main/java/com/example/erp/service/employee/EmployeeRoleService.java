package com.example.erp.service.employee;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.EmployeeRole;
import com.example.erp.repository.employee.EmployeeRoleRepository;



@Service
public class EmployeeRoleService {

	
	
	@Autowired
	private EmployeeRoleRepository repo;
	
	public Iterable<EmployeeRole> listAll(){
		return  this.repo.findAll();
		
		
	}
	public void SaveorUpdate(EmployeeRole EmployeeRole) {
		repo.save(EmployeeRole);
	}
	
	
	public void save(EmployeeRole EmployeeRole) {
		repo.save(EmployeeRole);

		}
	
	public EmployeeRole findById(Long EmployeeRole) {
		return repo.findById(EmployeeRole).get();

		}
	
	public void deleteEmployeeRoleRollById(Long EmployeeRole) {
		repo.deleteById(EmployeeRole);
	}
	

	public Optional<EmployeeRole> getEmployeeRoleRollById(Long EmployeeRole) {
		return	repo.findById(EmployeeRole);
		 
	}
	public void deleteById(Long EmployeeRoleId) {
		repo.deleteById(EmployeeRoleId);
		
	}
}
