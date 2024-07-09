package com.example.erp.service.project;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.erp.entity.project.ResearchDevelopmentFile;
import com.example.erp.repository.project.ResearchDevelopmentFileRepository;

@Service
public class ResearchDevelopmentFileService {

	@Autowired
	private ResearchDevelopmentFileRepository reDevelopmentFileRepository;
	
	
	// view
		public List<ResearchDevelopmentFile> listAll() {
			return this.reDevelopmentFileRepository.findAll();
		}

		// save
		public ResearchDevelopmentFile SaveResearchDevelopmentFile(ResearchDevelopmentFile researchDevelopmentFile) {
			return reDevelopmentFileRepository.save(researchDevelopmentFile);
		}

		public ResearchDevelopmentFile findResearchDevelopmentFileById(Long researchDevelopmentFileId) {
			return reDevelopmentFileRepository.findById(researchDevelopmentFileId).get();
		}

		// delete
		public void deleteResearchDevelopmentFileById(Long id) {
			reDevelopmentFileRepository.deleteById(id);
		}
}
