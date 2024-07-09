package com.example.erp.service.employee;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.Bank;
import com.example.erp.entity.employee.EmployeeRole;
import com.example.erp.entity.employee.Personal;
import com.example.erp.repository.employee.PersonalRepository;


@Service
public class PersonalService {
	
	
	@Autowired
	private PersonalRepository repo;
	
	public Iterable<Personal> listAll(){
		return  this.repo.findAll();
		
		
	}
	public void SaveorUpdate(Personal Personal) {
		repo.save(Personal);
	}
	

	
	public Personal findById(Long Personal) {
		return repo.findById(Personal).get();

		}
	/////////employee///////
	public Personal getByEmployeeId(long id) {
		return repo.findByEmployeeId(id).get();
		
	}

	
	public Personal getByUserId(long id) {
		return repo.findByEmployeeId(id).get();
	}
	public void deleteDepartmentRollById(Long Personal) {
		repo.deleteById(Personal);
	}
	

	public Optional<Personal> getdepartmentRollById(Long Personal) {
		return	repo.findById(Personal);
		 
	}
	public void deleteById(Long PersonalId) {
		repo.deleteById(PersonalId);
		
	}
	public Optional<Personal> getDesignationById(Long personalId) {
		return repo.findById(personalId);

	}


}
