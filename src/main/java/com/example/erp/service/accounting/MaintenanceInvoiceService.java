package com.example.erp.service.accounting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.accounting.MaintenanceInvoice;
import com.example.erp.repository.accounting.MaintenanceInvoiceRepository;



@Service
public class MaintenanceInvoiceService {
	
	
	@Autowired
	private MaintenanceInvoiceRepository repo;

	public List<MaintenanceInvoice> invoiceList() {
		return repo.findAll();
	}

	public void saveInvoice(MaintenanceInvoice invoice) {
		this.repo.save(invoice);
	}

	//////// delete/////////
	public void deleteInvoiceById(Long id) {
		repo.deleteById(id);
	}

	/////// edit///////
	public MaintenanceInvoice findInvoiceById(Long id) {
		return repo.findById(id).get();
	}

	

}
