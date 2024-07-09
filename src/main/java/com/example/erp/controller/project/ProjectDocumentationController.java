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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.erp.entity.project.ProjectDocumentation;
import com.example.erp.entity.project.Testing;
import com.example.erp.repository.project.ProjectDocumentationRepository;
import com.example.erp.service.project.ProjectDocumentationService;
import com.example.erp.service.project.TestingService;

@RestController
@CrossOrigin
public class ProjectDocumentationController {

	@Autowired
	private ProjectDocumentationService projectDocumentationService;

	@Autowired
	private ProjectDocumentationRepository documentationRepository;
	@Autowired
	private TestingService testingService;

	@GetMapping("/projectDocumentation")
	public ResponseEntity<Object> getProjectDocumentationDetails(
			@RequestParam(required = true) String projectDocumentation) {
		if ("projectDocumentationDetails".equals(projectDocumentation)) {
			return ResponseEntity.ok(projectDocumentationService.listAll());
		} else {
			String errorMessage = "Invalid value for ' projectDocumentation'. Expected ' projectDocumentationDetails'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@PostMapping("/projectDocumentation/save")
	public ResponseEntity<String> saveProjectDocumentationDetails(
			@RequestBody ProjectDocumentation projectDocumentation) {
		try {
			projectDocumentationService.SaveProjectDocumentationDetails(projectDocumentation);
			long id = projectDocumentation.getProjectDocumentationId();
			return ResponseEntity.ok("ProjectDocumentation Details saved successfully. ProjectDocumentation ID: " + id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving projectDocumentation: " + e.getMessage());
		}
	}

	@PutMapping("/projectDocumentation/edit/{id}")
	public ResponseEntity<ProjectDocumentation> updateProjectDocumentationDetails(
			@PathVariable("id") Long projectDocumentationId, @RequestBody ProjectDocumentation projectDocumentation) {
		try {
			ProjectDocumentation existingProjectDocumentation = projectDocumentationService
					.findProjectDocumentationId(projectDocumentationId);

			if (existingProjectDocumentation == null) {
				return ResponseEntity.notFound().build();
			}
			existingProjectDocumentation.setEmployeeId(projectDocumentation.getEmployeeId());

			projectDocumentationService.SaveProjectDocumentationDetails(existingProjectDocumentation);
			return ResponseEntity.ok(existingProjectDocumentation);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/projectDocumentation/delete/{id}")
	public ResponseEntity<String> deleteProjectDocumentationId(@PathVariable("id") Long projectDocumentationId) {
		projectDocumentationService.deleteProjectDocumentationById(projectDocumentationId);
		return ResponseEntity.ok("ProjectDocumentation detail deleted successfully With Id :" + projectDocumentationId);

	}

	@GetMapping("/projectDocumentation/view")
	public ResponseEntity<?> getTaskAssignedDetailsp(@RequestParam(required = true) String view) {
		try {
			if ("projectDocumentation".equals(view)) {
				List<Map<String, Object>> tasks = documentationRepository.getAllprojectDocumentation();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();

					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(taskAssigned);
					String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employeeId") + "."
							+ fileExtension;

					taskAssignedMap.put("profile", imageUrl);
					taskAssignedMap.putAll(taskAssigned);
					taskList.add(taskAssignedMap);
				}
				return ResponseEntity.ok(taskList);
			} else {
				String errorMessage = "Invalid value for 'TaskAssigned'. Expected 'findTaskAssignedDetails'.";
				return ResponseEntity.badRequest().body(Collections.singletonMap("error", errorMessage));
			}
		} catch (Exception e) {
			String errorMessage = "Error occurred while retrieving task assigned details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
		}
	}

	@PutMapping("/project/file/details/edit/{id}")
	public ResponseEntity<?> updateClientFormForProject(@PathVariable("id") long id,
			@RequestBody ProjectDocumentation projectDocumentation) {
		try {

			ProjectDocumentation existingProjectDocumentation = projectDocumentationService
					.findProjectDocumentationId(id);

			if (existingProjectDocumentation == null) {
				return ResponseEntity.notFound().build();
			}

			if (existingProjectDocumentation.isAccepted()) {
				String errorMessage = "A Project is already approved and moved to testing";
				System.out.println(errorMessage);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}

			if (existingProjectDocumentation.isRejected()) {
				String errorMessage = "A Project is rejected";
				System.out.println(errorMessage);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}

			existingProjectDocumentation.setProjectStatus(projectDocumentation.getProjectStatus());

			if (projectDocumentation.getProjectStatus().equals("approved")) {
				existingProjectDocumentation.setAccepted(true);
			} else if (projectDocumentation.getProjectStatus().equals("rejected")) {
				existingProjectDocumentation.setRejected(true);
			} else {
				existingProjectDocumentation.setAccepted(false);
				existingProjectDocumentation.setRejected(false);
			}

			projectDocumentationService.SaveProjectDocumentationDetails(existingProjectDocumentation);

			if (existingProjectDocumentation.getProjectStatus().equals("approved")) {
				long clientId = existingProjectDocumentation.getClientId();
				long projectId = existingProjectDocumentation.getProjectId();
				String url = existingProjectDocumentation.getUrl();

				Testing testing = new Testing();
				testing.setClientId(clientId);
				testing.setProjectId(projectId);
				testing.setDate(new Date(System.currentTimeMillis()));
				testing.setProjectStatus("pending");
				testing.setTypeOfProject("testing");
				testing.setUrl(url);

				testingService.SaveTestingDetails(testing);

			}

			return ResponseEntity.ok(existingProjectDocumentation);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
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
		} else {
			return "jpg";
		}
	}

}
