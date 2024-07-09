package com.example.erp.service.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.admin.Role;
import com.example.erp.repository.admin.RoleRepository;


@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	public void addAdmin() {
		
		Role admin = new Role();
		admin.setRoleId(1);
		admin.setRoleName("Admin");
		roleRepository.save(admin);
		
		Role manager = new Role();
		manager.setRoleId(2);
		manager.setRoleName("Manager");
		roleRepository.save(manager);
		
		Role customer = new Role();
		customer.setRoleId(3);
		customer.setRoleName("Customer");
		roleRepository.save(customer);
		
		Role employee = new Role();
		employee.setRoleId(4);
		employee.setRoleName("Employee");
		roleRepository.save(employee);
		
		Role selectors = new Role();
		selectors.setRoleId(5);
		selectors.setRoleName("Training");
		roleRepository.save(selectors);
		
		Role accountant = new Role();
		accountant.setRoleId(5);
		accountant.setRoleName("Accountant");
		roleRepository.save(accountant);
		
		Role projectHead = new Role();
		projectHead.setRoleId(6);
		projectHead.setRoleName("ProjectHead");
		roleRepository.save(projectHead);
		
		Role TL = new Role();
		TL.setRoleId(7);
		TL.setRoleName("TL");
		roleRepository.save(TL);
		
		Role training = new Role();
		training.setRoleId(8);
		training.setRoleName("Training");
		roleRepository.save(training);
		
		Role superAdmin = new Role();
		superAdmin.setRoleId(9);
		superAdmin.setRoleName("SuperAdmin");
		roleRepository.save(superAdmin);
		
		Role projectManager = new Role();
		projectManager.setRoleId(10);
		projectManager.setRoleName("projectManager");
		roleRepository.save(projectManager);
	}
	
}
