package com.example.erp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.erp.service.admin.AdminLoginService;
import com.example.erp.service.admin.RoleService;
import com.example.erp.service.attendance.ShiftTypeService;

@Component
public class ApplicationRunner implements CommandLineRunner {

	@Autowired
	private RoleService roleService;

	@Autowired
	private AdminLoginService adminLoginService;
	
	@Autowired
	private ShiftTypeService shiftTypeService;

	@Override
	public void run(String... args) {
		roleService.addAdmin();
		adminLoginService.addAdminLoginService();
		shiftTypeService.addShiftTypeService();
	}
}
