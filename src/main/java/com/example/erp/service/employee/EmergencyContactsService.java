package com.example.erp.service.employee;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.employee.EmergencyContacts;
import com.example.erp.repository.employee.EmergencyContactsRepository;

@Service
public class EmergencyContactsService {

	@Autowired
	private EmergencyContactsRepository repo;

	public List<EmergencyContacts> listAll() {
		return repo.findAll();
	}

	public void saveOrUpdate(EmergencyContacts EmergencyContacts) {
		repo.save(EmergencyContacts);
	}

	public EmergencyContacts getByEmployeeId(long id) {
		return repo.findByEmployeeId(id).get();
	}
	public EmergencyContacts getByEmployeeId1(long id) {
		return repo.findById(id).get();
	}
	public void deleteById(long id) {
		repo.deleteById(id);
	}

	public Optional<EmergencyContacts> getDesignationById(Long bankId) {
		return repo.findById(bankId);

	}

}
