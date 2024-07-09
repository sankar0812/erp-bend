package com.example.erp.service.clientDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.accounting.Server;
import com.example.erp.entity.clientDetails.Quotation;
import com.example.erp.repository.clientDetails.QuotationRepository ;


@Service
public class QuotationService {
	
	
	@Autowired
	private QuotationRepository QuotationRepository;

	public List<Quotation> listAll() {
		return this.QuotationRepository.findAll();

	}

	public void SaveorUpdate(Quotation quotation) {
		QuotationRepository.save(quotation);
	}

	public void save(Quotation quotation) {
		QuotationRepository.save(quotation);

	}

	public Quotation findById(Long quotationId) {
		return QuotationRepository.findById(quotationId).get();

	}

	public Quotation getByClientId(long id) {
		return QuotationRepository.findByClientId(id).get();
	}
	
	public void deleteById(Long quotationId) {
		QuotationRepository.deleteById(quotationId);
	}

	public Optional<Quotation> getQuotationById(Long quotationId) {
		return QuotationRepository.findById(quotationId);

	}

	public Optional<Quotation> getById1(Long id) {
        return Optional.of(QuotationRepository.findById(id).get());
	}


	

	


	
}
