package com.example.erp.service.clientDetails;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.erp.entity.clientDetails.Receipts;
import com.example.erp.repository.clientDetails.ReceiptRepository;

@Service
public class ReceiptService {

	@Autowired
	private ReceiptRepository receiptRepository;

	// view
	public List<Receipts> balanceReceipts() {
		return this.receiptRepository.findAll();
	}

	// save
	public void SaveReceipt(Receipts receipt) {
		receiptRepository.save(receipt);
	}

	// edit
	public Receipts findReceiptById(Long id) {
		return receiptRepository.findById(id).get();
	}

	// delete
	public void deleteReceiptId(Long id) {
		receiptRepository.deleteById(id);
	}
}
