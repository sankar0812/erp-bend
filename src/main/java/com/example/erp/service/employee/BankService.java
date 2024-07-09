package com.example.erp.service.employee;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.erp.entity.employee.Bank;
import com.example.erp.repository.employee.BankRepository;

@Service
public class BankService {

	@Autowired
	private BankRepository repo;

	public Iterable<Bank> listAll() {
		return this.repo.findAll();

	}

	public void SaveorUpdate(Bank bank) {
		repo.save(bank);
	}

	public Bank getByEmployeeId(long id,long roleid) {
		return repo.findByEmployeeIdAndRoleId(id, roleid).get();
	}

	public void save(Bank bank) {
		repo.save(bank);

	}

	public Bank findById(Long bank) {
		return repo.findById(bank).get();

	}
	public Bank getByEmployeeId(long id) {
		return repo.findByEmployeeId(id).get();
		
	}
	public void deleteDepartmentRollById(Long bank) {
		repo.deleteById(bank);
	}

	public Optional<Bank> getdepartmentRollById(Long bank) {
		return repo.findById(bank);

	}

	public void deleteById(Long bankId) {
		repo.deleteById(bankId);

	}

	public Optional<Bank> getDesignationById(Long bankId) {
		return repo.findById(bankId);

	}

}
