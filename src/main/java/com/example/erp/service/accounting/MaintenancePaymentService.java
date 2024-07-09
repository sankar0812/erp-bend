package com.example.erp.service.accounting;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.accounting.MaintenancePayment;
import com.example.erp.repository.accounting.MaintenancePaymentRepository;


@Service
public class MaintenancePaymentService {

	@Autowired
	private MaintenancePaymentRepository Repo;

	public Iterable<MaintenancePayment> listAll() {
		return this.Repo.findAll();

	}

	public void save(MaintenancePayment server) {
		Repo.save(server);
	}

	public MaintenancePayment findById(long maintenancePaymentId) {
		return Repo.findById(maintenancePaymentId).get();
	}

	public void deleteKeyboardBrandById(long maintenancePaymentId) {
		Repo.deleteById(maintenancePaymentId);
	}

	public MaintenancePayment getById(long id) {
		return Repo.findById(id).get();
	}

	public Optional<MaintenancePayment> getKeyboardBrandById(Long maintenancePaymentId) {
		return Repo.findById(maintenancePaymentId);

	}



}
