package com.example.erp.controller.project;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
import com.example.erp.entity.project.DepartmentList;
import com.example.erp.entity.project.ProjectAssigning;
import com.example.erp.entity.project.Task;
import com.example.erp.entity.project.TaskList;
import com.example.erp.repository.project.ProjectAssigningRepository;
import com.example.erp.service.clientDetails.ClientRequirementService;
import com.example.erp.service.project.ProjectAssigningService;
import com.example.erp.service.project.TaskService;

@RestController
@CrossOrigin
public class ProjectAssigningController {

	@Autowired
	private ProjectAssigningService projectAssigningService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private ProjectAssigningRepository assigningRepository;

	@Autowired
	private ClientRequirementService clientRequirementService;
	
	@GetMapping("/projectAssigning")
	public ResponseEntity<Object> getProjectAssigningDetails(@RequestParam(required = true) String projectAssigning) {
		if ("projectAssigningDetails".equals(projectAssigning)) {
			return ResponseEntity.ok(projectAssigningService.listprojectAssigning());
		} else {
			String errorMessage = "Invalid value for ' projectAssigning'. Expected ' projectAssigningDetails'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@PostMapping("/projectAssigning/save")
	public ResponseEntity<String> saveProjectAssigningDetails(@RequestBody ProjectAssigning projectAssigning) {
		try {
			projectAssigningService.SaveProjectAssigningDetails(projectAssigning);
			long id = projectAssigning.getProjectAssigningId();
			return ResponseEntity.ok("ProjectAssigning Details saved successfully. ProjectAssigning ID: " + id);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving projectAssigning: " + e.getMessage());
		}
	}

	@PutMapping("/projectAssigning/edit/{id}")
	public ResponseEntity<ProjectAssigning> updateIssueDetails(@PathVariable("id") Long projectAssigningId,
			@RequestBody ProjectAssigning projectAssigning) {
		try {
			ProjectAssigning existingProjectAssign = projectAssigningService.findProjectAssigningId(projectAssigningId);

			if (existingProjectAssign == null) {
				return ResponseEntity.notFound().build();
			}
			existingProjectAssign.setDepartmentList(projectAssigning.getDepartmentList());
			existingProjectAssign.setEmployeeId(projectAssigning.getEmployeeId());
			projectAssigningService.SaveProjectAssigningDetails(existingProjectAssign);
			return ResponseEntity.ok(existingProjectAssign);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/project/details/edit/{id}")
	public ResponseEntity<?> updateClientFormForProject(@PathVariable("id") long projectAssigningId,
			@RequestBody ProjectAssigning project) {
		try {
			ProjectAssigning existingProjectAssigning = projectAssigningService
					.findProjectAssigningId(projectAssigningId);
			if (existingProjectAssigning == null) {
				return ResponseEntity.notFound().build();
			}
			if (existingProjectAssigning.isAccepted()) {
				String errormessage = "a project is moved to  first step task";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errormessage);
			}

			if (existingProjectAssigning.isRejected()) {
				String errormessage = "a project  is rejected";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errormessage);
			}

			existingProjectAssigning.setProjectStatus(project.getProjectStatus());
			if (project.getProjectStatus().equals("approved")) {
				existingProjectAssigning.setAccepted(true);
			} else if (project.getProjectStatus().equals("rejected")) {
				existingProjectAssigning.setRejected(true);
			} else {
				existingProjectAssigning.setAccepted(false);
				existingProjectAssigning.setRejected(false);
			}

			projectAssigningService.SaveProjectAssigningDetails(existingProjectAssigning);
			if (existingProjectAssigning.getProjectStatus().equals("approved")) {
			    long projectId = existingProjectAssigning.getProjectId();
			    long clientId = existingProjectAssigning.getClientId();
			    long employeeId1 = existingProjectAssigning.getEmployeeId();
			    String ERT=existingProjectAssigning.getTypeOfProject();
			    Task task = new Task();
			    List<DepartmentList> departmentList = existingProjectAssigning.getDepartmentList();
			    List<TaskList> taskList = new ArrayList<>();
				ClientRequirement requirement = clientRequirementService.getById(projectId);
				 String nameConverter=requirement.getProjectName();
			    for (DepartmentList departmentLoop : departmentList) {
			        long employeeId = departmentLoop.getEmployeeId();
			        long departmentId = departmentLoop.getDepartmentId();

			        TaskList taskListItem = new TaskList();
			        taskListItem.setEmployeeReportId(employeeId);
			        taskListItem.setDepartmentId(departmentId);
			        taskListItem.setProjectStatus("pending");
			        taskList.add(taskListItem);
			        long id = nextId++;
			    	String com = nameConverter.substring(0, 3).toUpperCase();
			    	String rr = ERT.substring(0, 2).toUpperCase();

			    	String projectKey = String.format("%s(%s)%s", com, rr, id);			
			    	taskListItem.setProjectkey(projectKey);
			    }

			    // Assuming employeeId is part of the existingProjectAssigning object
//			    long employeeId = existingProjectAssigning.get(); 

			    task.setEmployeeReportId(employeeId1);
			    task.setTaskList(taskList);
			    task.setProjectId(projectId);
			    task.setClientId(clientId);  
			    task.setDate(new Date(System.currentTimeMillis()));
			    task.setProjectDate(LocalDate.now());
			    task.setTypeOfProject("project");
			    task.setProjectStatus("pending");

			    taskService.SaveTaskDetails(task);

			}

			return ResponseEntity.ok(existingProjectAssigning);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	private static long nextId = 1;
	@DeleteMapping("/projectAssigning/delete/{id}")
	public ResponseEntity<String> deleteProjectAssigningId(@PathVariable("id") Long projectAssigningId) {
		projectAssigningService.deleteProjectAssigningById(projectAssigningId);
		return ResponseEntity.ok("ProjectAssigning detail deleted successfully With Id :" + projectAssigningId);

	}

	@GetMapping("/projectAssigning/view")
	public ResponseEntity<Object> getAllRoleByEmployee(@RequestParam(required = true) String view) {
		if ("projectAssigning".equals(view)) {
			List<Map<String, Object>> serverList = new ArrayList<>();
			List<Map<String, Object>> serverRole = assigningRepository.getAllprojectAssigning();
			Map<String, List<Map<String, Object>>> serverGroupMap = serverRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("projectAssigningId").toString()));

			for (Entry<String, List<Map<String, Object>>> serverLoop : serverGroupMap.entrySet()) {
				Map<String, Object> serverMap = new HashMap<>();
				serverMap.put("projectAssigningId", Long.parseLong(serverLoop.getKey()));
				serverMap.put("accepted", serverLoop.getValue().get(0).get("accepted"));
				serverMap.put("date", serverLoop.getValue().get(0).get("date"));
				serverMap.put("projectId", serverLoop.getValue().get(0).get("projectId"));
				serverMap.put("typeOfProject", serverLoop.getValue().get(0).get("typeOfProject"));
				serverMap.put("projectStatus", serverLoop.getValue().get(0).get("projectStatus"));
				serverMap.put("rejected", serverLoop.getValue().get(0).get("rejected"));
				serverMap.put("projectName", serverLoop.getValue().get(0).get("projectName"));
				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(serverLoop);
				String imageUrl = "profile/" + randomNumber + "/" + serverLoop.getValue().get(0).get("employeeId") + "."
						+ fileExtension;

				serverMap.put("profile", imageUrl);
				List<Map<String, Object>> serverSubList = new ArrayList<>();
				for (Map<String, Object> serverSubLoop : serverLoop.getValue()) {

					Map<String, Object> serverSubMap = new HashMap<>();
					serverSubMap.put("employeeId", serverSubLoop.get("employeeId"));
					serverSubMap.put("departmentListId", serverSubLoop.get("department_list_id"));
					serverSubMap.put("departmentId", serverSubLoop.get("departmentId"));
					serverSubMap.put("departmentName", serverSubLoop.get("departmentName"));
					serverSubMap.put("userId", serverSubLoop.get("userId"));
					serverSubMap.put("userName", serverSubLoop.get("userName"));
					serverSubList.add(serverSubMap);
				}
				serverMap.put("departmentList", serverSubList);
				serverList.add(serverMap);
			}

			return ResponseEntity.ok(serverList);
		} else {
			String errorMessage = "Invalid value for 'department'. Expected 'Department'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	private String getFileExtensionForImage(Entry<String, List<Map<String, Object>>> serverLoop) {
		if (serverLoop == null || serverLoop.getValue() == null || serverLoop.getValue().isEmpty()) {
			return "jpg";
		}

		String url = (String) serverLoop.getValue().get(0).get("url");

		if (url == null) {
			return "jpg";
		}

		if (url.endsWith(".png")) {
			return "png";
		} else if (url.endsWith(".jpg")) {
			return "jpg";
		} else {
			return "jpg";
		}
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}
	
	
	

}
