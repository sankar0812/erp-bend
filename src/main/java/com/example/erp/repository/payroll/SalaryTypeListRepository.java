package com.example.erp.repository.payroll;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.erp.entity.payroll.SalaryTypeList;

public interface SalaryTypeListRepository extends JpaRepository<SalaryTypeList, Long> {

	Optional<SalaryTypeList> findByEmployeeId(Long employeeId);
	
	Optional<SalaryTypeList> findByTraineeId(Long traineeId);

	void save(Optional<SalaryTypeList> existingSalaryType);
	
//	SalaryTypeList findByEmployeeId(Long employeeId);

}
