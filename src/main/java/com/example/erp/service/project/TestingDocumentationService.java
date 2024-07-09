package com.example.erp.service.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.erp.entity.project.TestingDocumentation;
import com.example.erp.repository.project.TestingDocumentationRepository;

@Service

public class TestingDocumentationService {

	@Autowired
	private TestingDocumentationRepository testingDocumentationRepository;
	
	// view
			public List<TestingDocumentation> listTestingDocumentation() {
				return this.testingDocumentationRepository.findAll();
			}

			// save
			public TestingDocumentation SaveTestingDocumentationDetails(TestingDocumentation testingDocumentation) {
				return testingDocumentationRepository.save(testingDocumentation);
			}

			public TestingDocumentation findTestingDocumentationId(Long testingDocumentationId) {
				return testingDocumentationRepository.findById(testingDocumentationId).get();
			}

			// delete
			public void deleteTestingDocumentationById(Long id) {
				testingDocumentationRepository.deleteById(id);
			}
	
}
