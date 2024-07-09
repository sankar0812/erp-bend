package com.example.erp.controller.project;

import java.sql.Date;
import java.time.LocalDate;
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

import com.example.erp.entity.project.Testing;
import com.example.erp.repository.project.TestingRepository;
import com.example.erp.entity.clientDetails.ClientRequirement;
import com.example.erp.entity.project.Task;
import com.example.erp.entity.project.TaskList;
import com.example.erp.entity.project.Testing;
import com.example.erp.service.clientDetails.ClientRequirementService;
import com.example.erp.service.project.TaskService;
import com.example.erp.service.project.TestingService;

@RestController
@CrossOrigin
public class TestingController {

	@Autowired
	private TestingService testingService;

	
	@Autowired
	private ClientRequirementService clientRequirementService;
	
	
	@Autowired
	private TestingRepository testingrepository;
	
	
	@Autowired
	private TaskService taskService;

	@GetMapping("/testingDetail")
	public ResponseEntity<Object> getTestingDetails(@RequestParam(required = true) String testing) {
		if ("testingDetails".equals(testing)) {
			return ResponseEntity.ok(testingService.listTesting());
		} else {
			String errorMessage = "Invalid value for 'testing'. Expected 'testingDetails'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@PostMapping("/testingDetail/save")
	public ResponseEntity<String> saveTestingDetails(@RequestBody Testing testing) {
		try {
			testingService.SaveTestingDetails(testing);
			long id = testing.getTestingId();
			return ResponseEntity.ok("Testing Details saved successfully. Testing ID: " + id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving testing: " + e.getMessage());
		}
	}

	@PutMapping("/testing/edit/{id}")
	public ResponseEntity<Testing> updateTesting(@PathVariable("id") Long testingId, @RequestBody Testing testing) {
		try {
			Testing existingTesting = testingService.findTestingId(testingId);

			if (existingTesting == null) {
				return ResponseEntity.notFound().build();
			}
			existingTesting.setDepartmentId(testing.getDepartmentId());
			existingTesting.setEmployeeId(testing.getEmployeeId());

			testingService.SaveTestingDetails(existingTesting);
			return ResponseEntity.ok(existingTesting);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/testing/details/edit/{id}")
	public ResponseEntity<?> updateClientFormForProject(@PathVariable("id") long testingId,
			@RequestBody Testing testing) {
		try {
			Testing existingTesting = testingService.findTestingId(testingId);
			if (existingTesting == null) {
				return ResponseEntity.notFound().build();
			}
			if (existingTesting.isAccepted()) {
				String errormessage = "a project is moved to research first step task";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errormessage);
			}

			if (existingTesting.isRejected()) {
				String errormessage = "a project of research is rejected";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errormessage);
			}		
			String ERT=existingTesting.getTypeOfProject();

			existingTesting.setProjectStatus(testing.getProjectStatus());
			if (testing.getProjectStatus().equals("approved")) {
				existingTesting.setAccepted(true);
			} else if (testing.getProjectStatus().equals("rejected")) {
				existingTesting.setRejected(true);
			} else {
				existingTesting.setAccepted(false);
				existingTesting.setRejected(false);
			}

			testingService.SaveTestingDetails(existingTesting);

			if (existingTesting.getProjectStatus().equals("approved")) {
				long projectId = existingTesting.getProjectId();
				long employeeId = existingTesting.getEmployeeId();
				long clientId = existingTesting.getClientId();
				long departmentId = existingTesting.getDepartmentId();
				ClientRequirement requirement = clientRequirementService.getById(projectId);
				String nameConverter = requirement.getProjectName();
				Task task = new Task();
				List<TaskList> taskList = new ArrayList<>();

				TaskList taskLoop = new TaskList();
				taskList.add(taskLoop);

				for (TaskList taskListItem : taskList) {
					taskListItem.setEmployeeReportId(employeeId);
					taskListItem.setDepartmentId(departmentId);
					taskListItem.setProjectStatus("pending");
					
					long id = nextId++;
			    	String com = nameConverter.substring(0, 3).toUpperCase();
			    	String rr = ERT.substring(0, 2).toUpperCase();

			    	String projectKey = String.format("%s(%s)%s", com, rr, id);			
			    	taskListItem.setProjectkey(projectKey);
				}
				task.setTestingDate(LocalDate.now());
				task.setEmployeeReportId(employeeId);
				task.setTaskList(taskList);
				task.setProjectId(projectId);
				task.setClientId(clientId);
				task.setTypeOfProject("testing");
				task.setProjectStatus("pending");
				task.setTodayDate(LocalDate.now());
				task.setDate(new Date(System.currentTimeMillis()));

				taskService.SaveTaskDetails(task);
			}

			return ResponseEntity.ok(existingTesting);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	private static long nextId = 1;
	@DeleteMapping("/testingDetail/delete/{id}")
	public ResponseEntity<String> deleteTestingId(@PathVariable("id") Long testingId) {
		testingService.deleteTestingById(testingId);
		return ResponseEntity.ok("Testing detail deleted successfully With Id :" + testingId);

	}

	@GetMapping("/testing/view")
	public ResponseEntity<?> getTaskAssignedDetailsp(@RequestParam(required = true) String view) {
		try {
			if ("testingDetail".equals(view)) {
				List<Map<String, Object>> tasks = testingrepository.getAllProjecttesting();
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
