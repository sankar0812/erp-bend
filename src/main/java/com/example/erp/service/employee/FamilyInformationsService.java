package com.example.erp.service.employee;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.Bank;
import com.example.erp.entity.employee.FamilyInformations;
import com.example.erp.repository.employee.FamilyInformationsRepository;

@Service
public class FamilyInformationsService {

	@Autowired
	private FamilyInformationsRepository repo;

	public Iterable<FamilyInformations> listAll() {
		return this.repo.findAll();

	}

	public void SaveorUpdate(FamilyInformations FamilyInformations) {
		repo.save(FamilyInformations);
	}

	public FamilyInformations getByEmployeeId(long id) {
		return repo.findByEmployeeId(id).get();
	}

	public FamilyInformations getById(long id) {
		return repo.findById(id).get();
	}

	public void deleteDepartmentRollById(Long FamilyInformations) {
		repo.deleteById(FamilyInformations);
	}

	public Optional<FamilyInformations> getdepartmentRollById(Long FamilyInformations) {
		return repo.findById(FamilyInformations);

	}

	public void deleteById(Long FamilyInformationsId) {
		repo.deleteById(FamilyInformationsId);

	}

}
