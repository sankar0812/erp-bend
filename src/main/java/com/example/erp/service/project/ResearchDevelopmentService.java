package com.example.erp.service.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.project.ResearchDevelopment;
import com.example.erp.repository.project.ResearchDevelopmentRepository;

@Service
public class ResearchDevelopmentService {

	@Autowired
	private ResearchDevelopmentRepository researchDevelopmentRepository;

	// view
	public List<ResearchDevelopment> listAll() {
		return this.researchDevelopmentRepository.findAll();
	}

	// save
	public ResearchDevelopment SaveResearchDevelopmentDetails(ResearchDevelopment researchDevelopment) {
		return researchDevelopmentRepository.save(researchDevelopment);
	}

	public ResearchDevelopment findResearchDevelopmentById(Long researchDevelopment) {
		return researchDevelopmentRepository.findById(researchDevelopment).get();
	}

	// delete
	public void deleteResearchDevelopmentId(Long id) {
		researchDevelopmentRepository.deleteById(id);
	}
}
