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

import com.example.erp.entity.project.Hosting;
import com.example.erp.entity.project.Testing;
import com.example.erp.entity.project.TestingDocumentation;
import com.example.erp.repository.project.TestingDocumentationRepository;
import com.example.erp.service.project.HostingService;
import com.example.erp.service.project.TestingDocumentationService;

@RestController
@CrossOrigin
public class TestingDocumentationController {

	
	@Autowired
	private HostingService hostingService;
	
	@Autowired
	private TestingDocumentationService testingDocumentationService;
	
	@Autowired
	private TestingDocumentationRepository documentationRepository;
	
	@GetMapping("/testingDocumentation")
	public ResponseEntity<Object> getTestingDocumentationDetails(@RequestParam(required = true) String testingDocumentation) {
		if ("testingDocumentationDetails".equals(testingDocumentation)) {
			return ResponseEntity.ok(testingDocumentationService.listTestingDocumentation());
		} else {
			String errorMessage = "Invalid value for 'testingDocumentation'. Expected 'testingDocumentationDetails'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}
	
	
	@PostMapping("/testingDocumentationDetail/save")
	public ResponseEntity<String> saveTestingDocumentationDetails(@RequestBody TestingDocumentation testingDocumentation) {
		try {
			testingDocumentationService.SaveTestingDocumentationDetails(testingDocumentation);
			long id = testingDocumentation.getTestingDocumentationId();
			return ResponseEntity.ok("TestingDocumentation Details saved successfully. TestingDocumentation ID: " + id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving testingDocumentation: " + e.getMessage());
		}
	}
	@PutMapping("/testingDocumentation/edit/{id}")
	public ResponseEntity<TestingDocumentation> updateTestingDocumentation(@PathVariable("id") Long testingDocumentationId, @RequestBody TestingDocumentation testingDocumentation) {
		try {
			TestingDocumentation existingTestingDocumentation = testingDocumentationService.findTestingDocumentationId(testingDocumentationId);

			if (existingTestingDocumentation == null) {
				return ResponseEntity.notFound().build();
			}
			existingTestingDocumentation.setEmployeeId(testingDocumentation.getEmployeeId());

			testingDocumentationService.SaveTestingDocumentationDetails(existingTestingDocumentation);
			return ResponseEntity.ok(existingTestingDocumentation);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	@PutMapping("/testingDocumentation/status/edit/{id}")
	public ResponseEntity<?> updateClientApprovelStatus(@PathVariable("id") long id, @RequestBody TestingDocumentation quotation) {
		try {
			TestingDocumentation existingTestingDocumentation = testingDocumentationService.findTestingDocumentationId(id);
			if (existingTestingDocumentation == null) {
				return ResponseEntity.notFound().build();
			}

			existingTestingDocumentation.setProjectStatus(quotation.getProjectStatus());
			if (quotation.getProjectStatus().equals("approved")) {
				existingTestingDocumentation.setAccepted(true);
				existingTestingDocumentation.setRejected(false);
			} else if (quotation.getProjectStatus().equals("rejected")) {
				existingTestingDocumentation.setRejected(true);
				existingTestingDocumentation.setAccepted(false);
			} else {
				existingTestingDocumentation.setAccepted(false);
				existingTestingDocumentation.setRejected(false);
			}

			testingDocumentationService.SaveTestingDocumentationDetails(existingTestingDocumentation);

			if (existingTestingDocumentation.getProjectStatus().equals("approved")) {
				long clientId = existingTestingDocumentation.getClientId();
				long projectId = existingTestingDocumentation.getProjectId();
				String url = existingTestingDocumentation.getUrl();

				Hosting hosting = new Hosting();
				hosting.setClientId(clientId);
				hosting.setProjectId(projectId);
				hosting.setDate(new Date(System.currentTimeMillis()));
				hosting.setProjectStatus("pending");
				hosting.setTypeOfProject("hosting");
				hosting.setUrl(url);
				
				hostingService.SaveHostingDetails(hosting);

			}
			return ResponseEntity.ok(existingTestingDocumentation);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@DeleteMapping("/testingDocumentation/delete/{id}")
	public ResponseEntity<String> deleteTestingDocumentationId(@PathVariable("id") Long testingDocumentationId) {
		testingDocumentationService.deleteTestingDocumentationById(testingDocumentationId);
		return ResponseEntity.ok("TestingDocumentation detail deleted successfully With Id :" + testingDocumentationId);

	}
	
	@GetMapping("/testingDocumentation/view")
	public ResponseEntity<?> getTaskAssignedDetailsp(@RequestParam(required = true) String view) {
		try {
			if ("testingDocumentation".equals(view)) {
				List<Map<String, Object>> tasks = documentationRepository.getAllProjecttestingDocumentation();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();

					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(taskAssigned);
					String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employeeId") + "." + fileExtension;

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
	
	
	@GetMapping("/testingDocumentation/view/accepted")
	public ResponseEntity<?> getAllProjecttestingDocumentation(@RequestParam(required = true) String view) {
		try {
			if ("accepted".equals(view)) {
				List<Map<String, Object>> tasks = documentationRepository.getAllProjecttestingDocumentationaccepted();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();

					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(taskAssigned);
					String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employeeId") + "." + fileExtension;

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
	
	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000); // Change the upper limit if needed
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
