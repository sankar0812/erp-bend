package com.example.erp.service.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.project.Research;
import com.example.erp.repository.project.ResearchRepository;

@Service
public class ResearchService {

	@Autowired
	private ResearchRepository researchRepository;
	
	// view
		public List<Research> listResearch() {
			return this.researchRepository.findAll();
		}

		// save
		public Research SaveResearchDetails(Research research) {
			return researchRepository.save(research);
		}

		public Research findResearchById(Long researchId) {
			return researchRepository.findById(researchId).get();
		}

		// delete
		public void deleteResearchById(Long id) {
			researchRepository.deleteById(id);
		}
	
}
