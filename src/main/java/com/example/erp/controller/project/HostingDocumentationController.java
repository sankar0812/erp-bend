package com.example.erp.controller.project;

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

import com.example.erp.entity.project.HostingDocumentation;
import com.example.erp.entity.project.TestingDocumentation;
import com.example.erp.repository.project.HostingDocumentationRepository;
import com.example.erp.repository.project.TestingDocumentationRepository;
import com.example.erp.service.project.HostingDocumentationService;
import com.example.erp.service.project.TestingDocumentationService;

@RestController
@CrossOrigin
public class HostingDocumentationController {

	@Autowired
	private HostingDocumentationService documentationService;
	
	@Autowired
	private HostingDocumentationRepository documentationRepository;
	
	@GetMapping("/hostingDocumentation")
	public ResponseEntity<Object> getTestingDocumentationDetails(@RequestParam(required = true) String testingDocumentation) {
		if ("hostingDocumentation".equals(testingDocumentation)) {
			return ResponseEntity.ok(documentationService.listHosting());
		} else {
			String errorMessage = "Invalid value for 'testingDocumentation'. Expected 'testingDocumentationDetails'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}
	
	
	@PostMapping("/hostingDocumentation/save")
	public ResponseEntity<String> saveTestingDocumentationDetails(@RequestBody HostingDocumentation hostingDocumentation) {
		try {
			documentationService.SaveHostingDetails(hostingDocumentation);
			long id = hostingDocumentation.getHostingDocumentationId();
			return ResponseEntity.ok("HostingDocumentation Details saved successfully. HostingDocumentation ID: " + id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving testingDocumentation: " + e.getMessage());
		}
	}
//	@PutMapping("/hostingDocumentation/edit/{id}")
//	public ResponseEntity<?> updateTestingDocumentation(@PathVariable("id") Long testingDocumentationId, @RequestBody HostingDocumentation hostingDocumentation) {
//		try {
//			HostingDocumentation existinghostingDocumentation = documentationService.findHostingId(hostingDocumentation);
//
//			if (existingTestingDocumentation == null) {
//				return ResponseEntity.notFound().build();
//			}
//			existingTestingDocumentation.setEmployeeId(hostingDocumentation.getEmployeeId());
//
//			documentationService.SaveHostingDetails(existingTestingDocumentation);
//			return ResponseEntity.ok(existingTestingDocumentation);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//	}
	
	
	@PutMapping("/hostingDocumentation/status/edit/{id}")
	public ResponseEntity<?> updateClientApprovelStatus(@PathVariable("id") long id, @RequestBody HostingDocumentation quotation) {
		try {
			HostingDocumentation existinghostingDocumentation = documentationService.findTestingDocumentationId(id);
			if (existinghostingDocumentation == null) {
				return ResponseEntity.notFound().build();
			}

			existinghostingDocumentation.setProjectStatus(quotation.getProjectStatus());
			if (quotation.getProjectStatus().equals("approved")) {
				existinghostingDocumentation.setAccepted(true);
				existinghostingDocumentation.setRejected(false);
			} else if (quotation.getProjectStatus().equals("rejected")) {
				existinghostingDocumentation.setRejected(true);
				existinghostingDocumentation.setAccepted(false);
			} else {
				existinghostingDocumentation.setAccepted(false);
				existinghostingDocumentation.setRejected(false);
			}

			documentationService.SaveHostingDetails(existinghostingDocumentation);
			return ResponseEntity.ok(existinghostingDocumentation);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@DeleteMapping("/hostingDocumentation/delete/{id}")
	public ResponseEntity<String> deleteTestingDocumentationId(@PathVariable("id") Long id) {
		documentationService.deleteHostingById(id);
		return ResponseEntity.ok("TestingDocumentation detail deleted successfully With Id :" + id);

	}
	
	@GetMapping("/hostingDocumentation/view")
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
	
	
	@GetMapping("/hostingDocumentation/view/accepted")
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

