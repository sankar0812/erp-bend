package com.example.erp.service.accounting;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.accounting.MaintenanceTerms;
import com.example.erp.repository.accounting.MaintenanceTermsRepository;



@Service
public class MaintenanceTermsService {

	@Autowired
	private MaintenanceTermsRepository Repo;

	public Iterable<MaintenanceTerms> listAll() {
		return this.Repo.findAll();

	}



	public void save(MaintenanceTerms server) {
		Repo.save(server);

	}

	public MaintenanceTerms findById(long maintenanceTermsId) {
		return Repo.findById(maintenanceTermsId).get();

	}

	public void deleteKeyboardBrandById(long maintenanceTermsId) {
		Repo.deleteById(maintenanceTermsId);
	}

	public MaintenanceTerms getById(long id) {
		return Repo.findById(id).get();
	}

	public Optional<MaintenanceTerms> getKeyboardBrandById(Long maintenanceTermsId) {
		return Repo.findById(maintenanceTermsId);

	}





}
