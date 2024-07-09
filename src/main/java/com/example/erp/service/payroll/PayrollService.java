package com.example.erp.service.payroll;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.payroll.Payroll;
import com.example.erp.repository.payroll.PayrollRepository;

@Service
public class PayrollService {

	@Autowired
	private PayrollRepository payrollRepository;

	public List<Payroll> listPayroll() {
		return this.payrollRepository.findAll();
	}

	// save
	public void SavePayroll(Payroll client) {
		payrollRepository.save(client);
	}

	// edit
	public Payroll findPayrollById(Long id) {
		return payrollRepository.findById(id).get();
	}

	// delete
	public void deletePayrollById(Long id) {
		payrollRepository.deleteById(id);
	}
}
