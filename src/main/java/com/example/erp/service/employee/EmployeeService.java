package com.example.erp.service.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.Employee;
import com.example.erp.repository.employee.EmployeeRepository;



@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository repo;

	public List<Employee> listAll1() {
		return repo.findAll();
	}
	
	public List<Employee> listAll() {
	    List<Employee> allEmployees = repo.findAll();
	    
	    List<Employee> trueStatusEmployees = new ArrayList<>();

	    for (Employee employee : allEmployees) {
	        if (employee.isStatus()) {
	            trueStatusEmployees.add(employee);
	        }
	    }

	    return trueStatusEmployees;
	}

	public void saveOrUpdate(Employee employee) {
		repo.save(employee);
	}

	public Employee getById(long id) {
		return repo.findById(id).get();
	}
	
	

	public void deleteById(long id) {
		repo.deleteById(id);
	}

	public Employee getEmployeeById(Long employeeId) {
		return repo.findById(employeeId).orElse(null);
	}

	public Optional<Employee> getEmployeeById1(Long employeeId) {
		return repo.findById(employeeId);
	}




	


	
	
	

	
	
	
	
	
	
	
	
	
	


	

}
