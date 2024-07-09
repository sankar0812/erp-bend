package com.example.erp.service.payroll;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.erp.entity.payroll.PayrollTypeList;
import com.example.erp.repository.payroll.PayrollTypeRepository;

@Service
public class PayrollTypeService {

	@Autowired
	private PayrollTypeRepository payrollTypeRepository;

	public List<PayrollTypeList> listPayrollType() {
		return this.payrollTypeRepository.findAll();
	}

	// save
	public void SavePayrollType(PayrollTypeList client) {
		payrollTypeRepository.save(client);
	}

	// edit
	public PayrollTypeList findPayrollTypeById(Long id) {
		return payrollTypeRepository.findById(id).get();
	}

	public PayrollTypeList findByEmployeeIdAndPaymentDate(Long id, LocalDate date) {
		return payrollTypeRepository.findByEmployeeIdAndPaymentDate(id, date).get();
	}
	
	// delete
	public void deletePayrollTypeById(Long id) {
		payrollTypeRepository.deleteById(id);
	}
	
	public Optional<PayrollTypeList> findByEmployeeIdAndDate(Long employeeId, LocalDate date) {
		return payrollTypeRepository.findByEmployeeIdAndPaymentDate(employeeId, date);
	}
	
	public Optional<PayrollTypeList> findByTraineeIdAndDate(Long traineeId, LocalDate date) {
		return payrollTypeRepository.findByTraineeIdAndPaymentDate(traineeId, date);
	}
}
