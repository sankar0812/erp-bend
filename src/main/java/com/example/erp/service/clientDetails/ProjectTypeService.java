package com.example.erp.service.clientDetails;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.erp.entity.clientDetails.ProjectType;
import com.example.erp.repository.clientDetails.ProjectTypeRepository;

@Service
public class ProjectTypeService {
@Autowired
private ProjectTypeRepository projectTypeRepository;

//view
	public List<ProjectType> listAll() {
		return this.projectTypeRepository.findAll();
	}

//save
	public void SaveProjectType(ProjectType projectType) {
		projectTypeRepository.save(projectType);
	}

//edit
	public ProjectType findById(Long id) {
		return projectTypeRepository.findById(id).get();
	}

//delete
	public void deleteProjectTypeId(Long id) {
		projectTypeRepository.deleteById(id);
	}
}
