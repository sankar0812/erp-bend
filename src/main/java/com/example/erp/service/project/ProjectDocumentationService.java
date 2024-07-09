package com.example.erp.service.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.project.ProjectDocumentation;
import com.example.erp.repository.project.ProjectDocumentationRepository;

@Service
public class ProjectDocumentationService {

	@Autowired

	private ProjectDocumentationRepository projectDocumentationRepository;

	// view
	public List<ProjectDocumentation> listAll() {
		return this.projectDocumentationRepository.findAll();
	}

	// save
	public ProjectDocumentation SaveProjectDocumentationDetails(ProjectDocumentation projectDocumentation) {
		return projectDocumentationRepository.save(projectDocumentation);
	}

	public ProjectDocumentation findProjectDocumentationId(Long projectDocumentationId) {
		return projectDocumentationRepository.findById(projectDocumentationId).get();
	}

	// delete
	public void deleteProjectDocumentationById(Long id) {
		projectDocumentationRepository.deleteById(id);
	}
}
