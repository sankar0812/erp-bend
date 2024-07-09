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

import com.example.erp.entity.clientDetails.ClientRequirement;
import com.example.erp.entity.project.Hosting;
import com.example.erp.entity.project.Task;
import com.example.erp.entity.project.TaskList;
import com.example.erp.entity.project.Testing;
import com.example.erp.repository.project.HostingRepository;
import com.example.erp.repository.project.TestingRepository;
import com.example.erp.service.clientDetails.ClientRequirementService;
import com.example.erp.service.project.HostingService;
import com.example.erp.service.project.TaskService;
import com.example.erp.service.project.TestingService;

@RestController
@CrossOrigin
public class HostingController {

	@Autowired
	private HostingService hostingService;
	
	@Autowired
	private HostingRepository hostingRepository;
	
	@Autowired
	private TestingService testingService;

	
	@Autowired
	private ClientRequirementService clientRequirementService;
	
	
	@Autowired
	private TestingRepository testingrepository;
	
	
	@Autowired
	private TaskService taskService;
	
	@GetMapping("/hosting/view")
	public ResponseEntity<Object> getProjectHostingDetails(@RequestParam(required = true) String hosting) {
		if ("hostingDetails".equals(hosting)) {
			return ResponseEntity.ok(hostingService.listHosting());
		} else {
			String errorMessage = "Invalid value for 'hosting'. Expected 'hostingDetails'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@PostMapping("/hosting/save")
	public ResponseEntity<String> saveProjectHostingDetails(@RequestBody Hosting hosting) {
		try {
			hostingService.SaveHostingDetails(hosting);
			long id = hosting.getHostingId();
			return ResponseEntity.ok("Project Hosting Details saved successfully. Hosting ID: " + id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving hosting: " + e.getMessage());
		}
	}

	@PutMapping("/hosting/edit/{id}")
	public ResponseEntity<Hosting> updateHostingDetails(@PathVariable("id") Long hostingId,
			@RequestBody Hosting hosting) {
		try {
			Hosting existingHosting = hostingService.findHostingId(hostingId);

			if (existingHosting == null) {
				return ResponseEntity.notFound().build();
			}
//			existingHosting.setDate(hosting.getDate());
			existingHosting.setDepartmentId(hosting.getDepartmentId());
			existingHosting.setEmployeeId(hosting.getEmployeeId());
//			existingHosting.setProjectId(hosting.getProjectId());
//			existingHosting.setAccepted(hosting.isAccepted());
//			existingHosting.setRejected(hosting.isRejected());
//			existingHosting.setProjectStatus(hosting.getProjectStatus());

			hostingService.SaveHostingDetails(existingHosting);
			return ResponseEntity.ok(existingHosting);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	
	
	@PutMapping("/hosting/details/edit/{id}")
	public ResponseEntity<?> updateClientFormForProject(@PathVariable("id") long hostingId,
			@RequestBody Hosting hosting) {
		try {
			Hosting existinghosting = hostingService.findHostingId(hostingId);
			if (existinghosting == null) {
				return ResponseEntity.notFound().build();
			}
			if (existinghosting.isAccepted()) {
				String errormessage = "a project is moved to research first step task";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errormessage);
			}

			if (existinghosting.isRejected()) {
				String errormessage = "a project of research is rejected";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errormessage);
			}		
			String ERT=existinghosting.getTypeOfProject();

			String projectStatus = hosting.getProjectStatus();
			existinghosting.setProjectStatus(projectStatus);

			if ("approved".equals(projectStatus)) {
			    existinghosting.setAccepted(true);
			} else if ("rejected".equals(projectStatus)) {
			    existinghosting.setRejected(true);
			} else {
			    existinghosting.setAccepted(false);
			    existinghosting.setRejected(false);
			}

			hostingService.SaveHostingDetails(existinghosting);

			if (projectStatus != null && projectStatus.equals("approved")) {
				long projectId = existinghosting.getProjectId();
				long employeeId = existinghosting.getEmployeeId();
				long clientId = existinghosting.getClientId();
				long departmentId = existinghosting.getDepartmentId();
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
				
				task.setTypeOfProject("hosting");
				task.setProjectStatus("pending");
				task.setTodayDate(LocalDate.now());
				task.setDate(new Date(System.currentTimeMillis()));

				taskService.SaveTaskDetails(task);
			}

			return ResponseEntity.ok(existinghosting);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	private static long nextId = 1;
	
	
	
	
	@DeleteMapping("/hosting/delete/{id}")
	public ResponseEntity<String> deleteHostingId(@PathVariable("id") Long hostingId) {
		hostingService.deleteHostingById(hostingId);
		return ResponseEntity.ok("Hosting detail deleted successfully With Id :" + hostingId);

	}
	
	@GetMapping("/hosting/view/table")
	public ResponseEntity<?> getTaskAssignedDetailsp(@RequestParam(required = true) String view) {
		try {
			if ("hosting".equals(view)) {
				List<Map<String, Object>> tasks = hostingRepository.getAllProjectWorkaaggg();
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
