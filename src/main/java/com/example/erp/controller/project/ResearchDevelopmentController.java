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
import com.example.erp.entity.project.ResearchDevelopment;
import com.example.erp.entity.project.Task;
import com.example.erp.entity.project.TaskList;
import com.example.erp.repository.project.ResearchDevelopmentRepository;
import com.example.erp.service.clientDetails.ClientRequirementService;
import com.example.erp.service.project.ResearchDevelopmentService;
import com.example.erp.service.project.TaskService;

@RestController
@CrossOrigin
public class ResearchDevelopmentController {

	@Autowired
	private ResearchDevelopmentService researchDevelopmentService;

	@Autowired
	private ResearchDevelopmentRepository developmentRepository;
	@Autowired
	private TaskService taskService;

	@Autowired
	private ClientRequirementService clientRequirementService;
	
	@GetMapping("/researchDevelopment")
	public ResponseEntity<Object> getResearchDevelopmentDetails(
			@RequestParam(required = true) String researchDevelopment) {
		if ("researchDevelopmentDetails".equals(researchDevelopment)) {
			return ResponseEntity.ok(researchDevelopmentService.listAll());
		} else {
			String errorMessage = "Invalid value for 'researchDevelopment'. Expected 'researchDevelopmentDetails'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@PostMapping("/researchDevelopment/save")
	public ResponseEntity<String> saveResearchDevelopmentDetails(@RequestBody ResearchDevelopment researchDevelopment) {
		try {
			researchDevelopmentService.SaveResearchDevelopmentDetails(researchDevelopment);
			long id = researchDevelopment.getResearchDevelopmentId();
			return ResponseEntity.ok("ResearchDevelopment Details saved successfully. ResearchDevelopment ID: " + id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving researchDevelopment: " + e.getMessage());
		}
	}

	@PutMapping("/researchDevelopment/edit/{id}")
	public ResponseEntity<ResearchDevelopment> updateResearchDevelopment(@PathVariable("id") Long researchDevelopmentId,
			@RequestBody ResearchDevelopment researchDevelopment) {
		try {
			ResearchDevelopment existingresearchDevelopment = researchDevelopmentService
					.findResearchDevelopmentById(researchDevelopmentId);

			if (existingresearchDevelopment == null) {
				return ResponseEntity.notFound().build();
			}
			existingresearchDevelopment.setDate(researchDevelopment.getDate());
			existingresearchDevelopment.setDepartmentId(researchDevelopment.getDepartmentId());
			existingresearchDevelopment.setEmployeeId(researchDevelopment.getEmployeeId());

			researchDevelopmentService.SaveResearchDevelopmentDetails(existingresearchDevelopment);
			return ResponseEntity.ok(existingresearchDevelopment);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/research/development/details/edit/{id}")
	public ResponseEntity<?> updateClientFormForProject(@PathVariable("id") long researchDevelopmentId,
			@RequestBody ResearchDevelopment researchDevelopment) {
		try {
			ResearchDevelopment existingResearchDevelopment = researchDevelopmentService
					.findResearchDevelopmentById(researchDevelopmentId);
			if (existingResearchDevelopment == null) {
				return ResponseEntity.notFound().build();
			}
			if (existingResearchDevelopment.isAccepted()) {
				String errormessage = "a project is moved to research first step task";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errormessage);
			}

			if (existingResearchDevelopment.isRejected()) {
				String errormessage = "a project of research is rejected";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errormessage);
			}

			existingResearchDevelopment.setProjectStatus(researchDevelopment.getProjectStatus());
			if (researchDevelopment.getProjectStatus().equals("approved")) {
				existingResearchDevelopment.setAccepted(true);
			} else if (researchDevelopment.getProjectStatus().equals("rejected")) {
				existingResearchDevelopment.setRejected(true);
			} else {
				existingResearchDevelopment.setAccepted(false);
				existingResearchDevelopment.setRejected(false);
			}

			researchDevelopmentService.SaveResearchDevelopmentDetails(existingResearchDevelopment);

			if (existingResearchDevelopment.getProjectStatus().equals("approved")) {
				long projectId = existingResearchDevelopment.getProjectId();
				long employeeId = existingResearchDevelopment.getEmployeeId();
				long departmentId = existingResearchDevelopment.getDepartmentId();
				long clientId = existingResearchDevelopment.getClientId();
				  String ERT=existingResearchDevelopment.getTypeOfProject();
				Task task = new Task();
				List<TaskList> taskList = new ArrayList<>();
				ClientRequirement requirement = clientRequirementService.getById(projectId);
				 String nameConverter=requirement.getProjectName();
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
				task.setEmployeeReportId(employeeId);
				task.setTaskList(taskList);
				task.setProjectId(projectId);
				task.setClientId(clientId);
				task.setTodayDate(LocalDate.now());
				task.setTypeOfProject("development");
				task.setDate(new Date(System.currentTimeMillis()));
				task.setProjectStatus("pending");

				taskService.SaveTaskDetails(task);
			}

			return ResponseEntity.ok(existingResearchDevelopment);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	private static long nextId = 1;
	@DeleteMapping("/researchDevelopment/delete/{id}")
	public ResponseEntity<String> deleteResearchDevelopmentId(@PathVariable("id") Long researchDevelopmentId) {
		researchDevelopmentService.deleteResearchDevelopmentId(researchDevelopmentId);
		return ResponseEntity.ok("ResearchDevelopment detail deleted successfully With Id :" + researchDevelopmentId);

	}

	@GetMapping("/researchDevelopment/view")
	public ResponseEntity<?> getTaskAssignedDetailsp(@RequestParam(required = true) String view) {
		try {
			if ("researchDevelopment".equals(view)) {
				List<Map<String, Object>> tasks = developmentRepository.getAllProjectresearchDevelopment();
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
