package com.example.erp.service.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.project.HostingDocumentation;
import com.example.erp.repository.project.HostingDocumentationRepository;



@Service
public class HostingDocumentationService {

	@Autowired
	private HostingDocumentationRepository documentationRepository;
	
	// view
			public List<HostingDocumentation> listHosting() {
				return this.documentationRepository.findAll();
			}

			// save
			public HostingDocumentation SaveHostingDetails(HostingDocumentation hosting) {
				return documentationRepository.save(hosting);
			}

			public HostingDocumentation findHostingId(Long hostingId) {
				return documentationRepository.findById(hostingId).get();
			}

			// delete
			public void deleteHostingById(Long id) {
				documentationRepository.deleteById(id);
			}
			public HostingDocumentation findTestingDocumentationId(long id) {
				return documentationRepository.findById(id).get();
			}
}
