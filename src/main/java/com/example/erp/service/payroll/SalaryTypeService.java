package com.example.erp.service.payroll;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.payroll.SalaryType;
import com.example.erp.entity.payroll.SalaryTypeList;
import com.example.erp.repository.payroll.SalaryTypeListRepository;
import com.example.erp.repository.payroll.SalaryTypeRepository;

@Service
public class SalaryTypeService {

	@Autowired
	private SalaryTypeRepository salaryTypeRepository;

	@Autowired
	private SalaryTypeListRepository salaryTypeListRepository;

	// view
	public List<SalaryType> listSalaryType() {
		return this.salaryTypeRepository.findAll();
	}

	// save
	public void SaveSalaryType(SalaryType client) {
		salaryTypeRepository.save(client);
	}

	// edit
	public SalaryType findSalaryTypeById(Long id) {
		return salaryTypeRepository.findById(id).get();
	}

	// delete
	public void deleteSalaryTypeById(Long id) {
		salaryTypeRepository.deleteById(id);
	}

	public Optional<SalaryTypeList> getSalaryByEmployeeId(Long employeeId) {
		return salaryTypeListRepository.findByEmployeeId(employeeId);
	}
	
	public Optional<SalaryTypeList> getSalaryByTraineeId(Long traineeId) {
		return salaryTypeListRepository.findByTraineeId(traineeId);
	}

	public Long getEmployeeIdForSalaryType(SalaryType salary) {
		return salaryTypeRepository.findEmployeeIdBySalaryTypeList(salary);
	}
}
