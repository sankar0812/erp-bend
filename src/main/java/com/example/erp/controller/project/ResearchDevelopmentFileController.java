package com.example.erp.controller.project;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.erp.entity.project.ProjectAssigning;
import com.example.erp.entity.project.ResearchDevelopment;
import com.example.erp.entity.project.ResearchDevelopmentFile;
import com.example.erp.repository.project.ResearchDevelopmentFileRepository;
import com.example.erp.service.project.ProjectAssigningService;
import com.example.erp.service.project.ResearchDevelopmentFileService;

@RestController
@CrossOrigin
public class ResearchDevelopmentFileController {
	@Autowired
	private ResearchDevelopmentFileService researchDevelopmentFileService;
	@Autowired
	private ResearchDevelopmentFileRepository developmentFileRepository;
	@Autowired
	private ProjectAssigningService projectAssigningService;

	@DeleteMapping("/researchDevelopmentFile/delete/{id}")
	public ResponseEntity<String> deleteResearchDevelopmentFileId(@PathVariable("id") Long researchDevelopmentFileId) {
		researchDevelopmentFileService.deleteResearchDevelopmentFileById(researchDevelopmentFileId);
		return ResponseEntity.ok("ResearchDevelopmentFile deleted successfully With Id :" + researchDevelopmentFileId);
	}



	@PutMapping("/researchDevelopmentFile/edit/{id}")
	public ResponseEntity<?> updateResearchDevelopment(@PathVariable("id") Long researchDevelopmentFileId,
			@RequestBody ResearchDevelopmentFile researchDevelopmentFile) {
		try {
			ResearchDevelopmentFile existingresearchDevelopment = researchDevelopmentFileService
					.findResearchDevelopmentFileById(researchDevelopmentFileId);

			if (existingresearchDevelopment == null) {
				return ResponseEntity.notFound().build();
			}	
			existingresearchDevelopment.setEmployeeId(researchDevelopmentFile.getEmployeeId());
			researchDevelopmentFileService.SaveResearchDevelopmentFile(existingresearchDevelopment);
			return ResponseEntity.ok(existingresearchDevelopment);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PutMapping("/research/project/details/edit/{id}")
	public ResponseEntity<?> updateClientFormForProject(@PathVariable("id") long id,
			@RequestBody ResearchDevelopmentFile researchDevelopmentFile) {
		try {

			ResearchDevelopmentFile existingResearchDevelopmentClientForm = researchDevelopmentFileService
					.findResearchDevelopmentFileById(id);

			if (existingResearchDevelopmentClientForm == null) {
				return ResponseEntity.notFound().build();
			}

			if (existingResearchDevelopmentClientForm.isAccepted()) {
				String errorMessage = "A Project is already approved and moved to Project Assigning";
				System.out.println(errorMessage);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}

			if (existingResearchDevelopmentClientForm.isRejected()) {
				String errorMessage = "A Project is rejected";
				System.out.println(errorMessage);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}

			existingResearchDevelopmentClientForm.setProjectStatus(researchDevelopmentFile.getProjectStatus());

			if (researchDevelopmentFile.getProjectStatus().equals("approved")) {
				existingResearchDevelopmentClientForm.setAccepted(true);
			} else if (researchDevelopmentFile.getProjectStatus().equals("rejected")) {
				existingResearchDevelopmentClientForm.setRejected(true);
			} else {
				existingResearchDevelopmentClientForm.setAccepted(false);
				existingResearchDevelopmentClientForm.setRejected(false);
			}

			researchDevelopmentFileService.SaveResearchDevelopmentFile(existingResearchDevelopmentClientForm);

			if (existingResearchDevelopmentClientForm.getProjectStatus().equals("approved")) {
				long clientId = existingResearchDevelopmentClientForm.getClientId();
				long projectId = existingResearchDevelopmentClientForm.getProjectId();
				String url = existingResearchDevelopmentClientForm.getUrl();

				ProjectAssigning projectAssigning = new ProjectAssigning();
				projectAssigning.setClientId(clientId);
				projectAssigning.setProjectId(projectId);
				projectAssigning.setDate(new Date(System.currentTimeMillis()));
				projectAssigning.setProjectStatus("pending");
				projectAssigning.setTypeOfProject("project");
				projectAssigning.setDate(new Date(System.currentTimeMillis()));
				projectAssigning.setUrl(url);

				projectAssigningService.SaveProjectAssigningDetails(projectAssigning);

			}

			return ResponseEntity.ok(existingResearchDevelopmentClientForm);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private String getFileExtensionForImage(Map<String, Object> employeeDetail) {
		if (employeeDetail == null || !employeeDetail.containsKey("url") || employeeDetail.get("url") == null) {
			return "jpg";
		}
		String url = (String) employeeDetail.get("url");
		if (url.endsWith(".png")) {
			return "png";
		} else if (url.endsWith(".jpg")) {
			return "jpg";
		} 
		else if (url.endsWith(".pdf")) {
			return "pdf";
		}else {
			return "pdf";
		}
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000); // Change the upper limit if needed
	}


	
	@GetMapping("/researchDevelopmentFile/view")
	public ResponseEntity<?> getTaskAssignedToProjectHead(@RequestParam(required = true) String view) {
	    try {
	        if ("researchDevelopmentFile".equals(view)) {
	            List<Map<String, Object>> tasks = developmentFileRepository.getAllProjectresearchDevelopmentFile();
	            List<Map<String, Object>> taskList = new ArrayList<>();

	            for (Map<String, Object> taskAssigned : tasks) {
	                Map<String, Object> taskAssignedMap = new HashMap<>();
	                String fileExtension = getFileExtensionForImage(taskAssigned);
	                int randomNumber = generateRandomNumber();
	                String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employee_id") + "." + fileExtension;
	                taskAssignedMap.put("profile", imageUrl);
	                taskAssignedMap.putAll(taskAssigned);
	                taskList.add(taskAssignedMap);
	            }
	            return ResponseEntity.ok(taskList);
	        } else {
	            String errorMessage = "Invalid value for 'dashboard'. Expected 'projectmanager'.";
	            return ResponseEntity.badRequest().body(Collections.singletonMap("error", errorMessage));
	        }
	    } catch (Exception e) {
	        String errorMessage = "Error occurred while retrieving task assigned details";
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Collections.singletonMap("error", errorMessage));
	    }
	}
}
