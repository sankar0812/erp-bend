package com.example.erp.service.employee;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.EmployeeAttendance;
import com.example.erp.repository.employee.EmployeeAttendanceRepository;

@Service
public class EmployeeAttendanceService {

	@Autowired
	private EmployeeAttendanceRepository repo;

	public List<EmployeeAttendance> listAll1() {
		return repo.findAll();
	}

	public void saveOrUpdate(EmployeeAttendance employeeAtt) {
		repo.save(employeeAtt);
	}

	public EmployeeAttendance getById(long id) {
		return repo.findById(id).get();
	}

	public void deleteById(long id) {
		repo.deleteById(id);
	}

	public EmployeeAttendance getEmployeeById(Long employeeAttId) {
		return repo.findById(employeeAttId).orElse(null);
	}

	public void save(EmployeeAttendance existingEntry) {
		repo.save(existingEntry);

	}

}
