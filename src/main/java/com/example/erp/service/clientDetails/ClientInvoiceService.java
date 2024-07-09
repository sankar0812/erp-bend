package com.example.erp.service.clientDetails;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.erp.entity.clientDetails.ClientInvoice;
import com.example.erp.repository.clientDetails.ClientInvoiceRepository;

@Service
public class ClientInvoiceService {

	@Autowired
	private ClientInvoiceRepository clientInvoiceRepository;

	public List<ClientInvoice> invoiceList() {
		return clientInvoiceRepository.findAll();
	}

	public void saveInvoice(ClientInvoice user) {
		this.clientInvoiceRepository.save(user);
	}

	//////// delete/////////
	public void deleteInvoiceById(Long id) {
		clientInvoiceRepository.deleteById(id);
	}

	/////// edit///////
	public ClientInvoice findInvoiceById1(Long id) {
		return clientInvoiceRepository.findById(id).get();
	}
}
