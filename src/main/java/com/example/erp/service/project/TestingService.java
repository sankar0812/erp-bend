package com.example.erp.service.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.project.Testing;
import com.example.erp.repository.project.TestingRepository;

@Service
public class TestingService {

	@Autowired
	private TestingRepository testingRepository;
	
	
	// view
		public List<Testing> listTesting() {
			return this.testingRepository.findAll();
		}

		// save
		public Testing SaveTestingDetails(Testing testing) {
			return testingRepository.save(testing);
		}

		public Testing findTestingId(Long testingId) {
			return testingRepository.findById(testingId).get();
		}

		// delete
		public void deleteTestingById(Long id) {
			testingRepository.deleteById(id);
		}
}
