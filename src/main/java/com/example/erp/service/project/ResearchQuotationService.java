package com.example.erp.service.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.project.ResearchQuotation;
import com.example.erp.repository.project.ResearchQuotationRepository;

@Service
public class ResearchQuotationService {

	@Autowired
	private ResearchQuotationRepository researchQuotationRepository;
	
	// view
			public List<ResearchQuotation> listAll() {
				return this.researchQuotationRepository.findAll();
			}

			// save
			public ResearchQuotation SaveResearchQuotation(ResearchQuotation researchQuotation) {
				return researchQuotationRepository.save(researchQuotation);
			}

			public ResearchQuotation findResearchQuotationById(Long researchQuotationId) {
				return researchQuotationRepository.findById(researchQuotationId).get();
			}

			// delete
			public void deleteResearchQuotationById(Long id) {
				researchQuotationRepository.deleteById(id);
			}
}
