package com.example.erp.service.employee;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.Department;
import com.example.erp.repository.employee.DepartmentRepository;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository repo;

	public Iterable<Department> listAll() {
		return this.repo.findAll();

	}

	public void SaveorUpdate(Department Department) {
		repo.save(Department);
	}

	public void save(Department Department) {
		repo.save(Department);

	}

	public Department findById(Long Department) {
		return repo.findById(Department).get();

	}

	public void deleteDepartmentRollById(Long Department) {
		repo.deleteById(Department);
	}

	public Optional<Department> getdepartmentRollById(Long Department) {
		return repo.findById(Department);

	}

	public void deleteById(Long DepartmentId) {
		repo.deleteById(DepartmentId);

	}

}
