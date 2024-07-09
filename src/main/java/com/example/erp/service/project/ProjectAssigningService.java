package com.example.erp.service.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.project.ProjectAssigning;
import com.example.erp.repository.project.ProjectAssigningRepository;

@Service
public class ProjectAssigningService {

	@Autowired
	private ProjectAssigningRepository projectAssigningRepository;
	
	// view
		public List<ProjectAssigning>listprojectAssigning() {
			return this.projectAssigningRepository.findAll();
		}

		// save
		public ProjectAssigning SaveProjectAssigningDetails(ProjectAssigning projectAssigning) {
			return projectAssigningRepository.save(projectAssigning);
		}

		public ProjectAssigning findProjectAssigningId(Long projectAssigningId) {
			return projectAssigningRepository.findById(projectAssigningId).get();
		}

		// delete
		public void deleteProjectAssigningById(Long id) {
			projectAssigningRepository.deleteById(id);
		}
}
