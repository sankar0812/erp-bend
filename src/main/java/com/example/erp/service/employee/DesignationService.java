package com.example.erp.service.employee;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.Designation;
import com.example.erp.repository.employee.DesignationRepository;






@Service
public class DesignationService {
	
	@Autowired
	private DesignationRepository designationrepository;
	
	public Iterable<Designation> listAll(){
		return  this.designationrepository.findAll();
		
		
	}
	public void SaveorUpdate(Designation designation) {
		designationrepository.save(designation);
	}
	
	
	public void save(Designation designation) {
		designationrepository.save(designation);

		}
	
	public Designation findById(Long designationId) {
		return designationrepository.findById(designationId).get();

		}
	
	public void deleteDesignationtById(Long designationId) {
		designationrepository.deleteById(designationId);
	}
	

	public Optional<Designation> getDesignationById(Long designationId) {
		return	designationrepository.findById(designationId);
		 
	}

}
