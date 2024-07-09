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
import com.example.erp.entity.project.Issue;
import com.example.erp.repository.project.IssueRepository;
import com.example.erp.service.project.IssueService;

@RestController
@CrossOrigin
public class IssueController {

	@Autowired
	private IssueService issueService;
	
	@Autowired
	private IssueRepository issueRepository;

	@GetMapping("/issue")
	public ResponseEntity<Object> getIssueDetails(@RequestParam(required = true) String issue) {
		if ("issueDetails".equals(issue)) {
			return ResponseEntity.ok(issueService.listIssue());
		} else {
			String errorMessage = "Invalid value for 'issue'. Expected 'issueDetails'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@PostMapping("/issue/save")
	public ResponseEntity<String> saveProjectHostingDetails(@RequestBody Issue issue) {
		try {
			issueService.SaveIssueDetails(issue);
			long id = issue.getIssueId();
			return ResponseEntity.ok("Issue Details saved successfully. Issue ID: " + id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving issue: " + e.getMessage());
		}
	}

	@PutMapping("/issue/edit/{id}")
	public ResponseEntity<Issue> updateIssueDetails(@PathVariable("id") Long issueId, @RequestBody Issue issue) {
		try {
			Issue existingIssue = issueService.findIssueId(issueId);

			if (existingIssue == null) {
				return ResponseEntity.notFound().build();
			}
			existingIssue.setDate(issue.getDate());
			existingIssue.setEmployeeId(issue.getEmployeeId());
			existingIssue.setProjectId(issue.getProjectId());
			existingIssue.setProjectStatus(issue.getProjectStatus());
			existingIssue.setAccepted(issue.isAccepted());
			existingIssue.setRejected(issue.isRejected());
			existingIssue.setCancellationReason(issue.getCancellationReason());

			issueService.SaveIssueDetails(existingIssue);
			return ResponseEntity.ok(existingIssue);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/issue/delete/{id}")
	public ResponseEntity<String> deleteIssueId(@PathVariable("id") Long issueId) {
		issueService.deleteIssueById(issueId);
		return ResponseEntity.ok("Issue detail deleted successfully With Id :" + issueId);

	}
	
	

	@GetMapping("/issue/view")
	public ResponseEntity<?> getIssueType(@RequestParam(required = true) String view) {
		try {
			if ("issue".equals(view)) {
				List<Map<String, Object>> issue = issueRepository.getAllProjectissue();
				List<Map<String, Object>> issueList = new ArrayList<>();

				for (Map<String, Object> issueAssigned : issue) {
					Map<String, Object> issueAssignedMap = new HashMap<>();

					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(issueAssigned);
					String imageUrl = "profile/" + randomNumber + "/" + issueAssigned.get("employeeId") + "." + fileExtension;

					issueAssignedMap.put("profile", imageUrl);
					issueAssignedMap.putAll(issueAssigned);
					issueList.add(issueAssignedMap);
				}
				return ResponseEntity.ok(issueList);
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
