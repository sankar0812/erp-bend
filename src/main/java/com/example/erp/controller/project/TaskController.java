package com.example.erp.controller.project;

import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.erp.entity.clientDetails.ClientRequirement;
import com.example.erp.entity.employee.Designation;
import com.example.erp.entity.employee.HolidaysList;
import com.example.erp.entity.project.Hosting;
import com.example.erp.entity.project.HostingDocumentation;
import com.example.erp.entity.project.ProjectDocumentation;
import com.example.erp.entity.project.Task;
import com.example.erp.entity.project.ResearchDevelopmentFile;
import com.example.erp.entity.project.ResearchQuotation;
import com.example.erp.entity.project.Task;
import com.example.erp.entity.project.TaskList;
import com.example.erp.entity.project.TaskListRequestDto;
import com.example.erp.entity.project.TestingDocumentation;
import com.example.erp.repository.project.TaskRepository;
import com.example.erp.service.clientDetails.ClientRequirementService;
import com.example.erp.service.project.HostingDocumentationService;
import com.example.erp.service.project.HostingService;
import com.example.erp.service.project.ProjectDocumentationService;
import com.example.erp.service.project.ResearchDevelopmentFileService;
import com.example.erp.service.project.ResearchQuotationService;
import com.example.erp.service.project.TaskListService;
import com.example.erp.service.project.TaskService;
import com.example.erp.service.project.TestingDocumentationService;

import io.jsonwebtoken.io.IOException;

@RestController
@CrossOrigin
public class TaskController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private TaskListService taskListService;

	@Autowired
	private TaskRepository taskrepository;
	@Autowired
	private HostingDocumentationService hostingService;
	@Autowired
	private ResearchDevelopmentFileService researchDevelopmentFileService;

	@Autowired
	private ProjectDocumentationService projectDocumentationService;

	@Autowired
	private ResearchQuotationService researchQuotationService;
	@Autowired
	private ClientRequirementService clientRequirementService;
	@Autowired
	private TestingDocumentationService testingDocumentationService;

	////////////////////////
	@PostMapping("/taskDetail/save")
	public ResponseEntity<String> saveTask(@RequestParam("projectId") long projectId, @RequestParam("date") Date date,
			@RequestParam("typeOfProject") String typeOfProject, @RequestParam("projectStatus") String projectStatus,
			@RequestParam("completed") boolean completed, @RequestParam("notCompleted") boolean notCompleted,
			@RequestParam("research") boolean research, @RequestParam("development") boolean development,
			@RequestParam("testing") boolean testing, @RequestPart("fileUpload") MultipartFile fileUpload,
			@RequestPart("taskList") List<TaskListRequestDto> taskListRequestDto)
			throws IOException, SQLException, java.io.IOException {
		try {
			Task task = new Task();
			task.setProjectId(projectId);
			task.setDate(date);
			task.setTypeOfProject(typeOfProject);
			task.setProjectStatus(projectStatus);
			task.setCompleted(completed);
			task.setNotCompleted(notCompleted);
			task.setResearch(research);
			task.setDevelopment(development);
			task.setTesting(testing);
			task.setFileUpload(convertToBlob(fileUpload));

			List<TaskList> taskList = taskListRequestDto.stream().map(dto -> dto.toTaskListEntity())
					.collect(Collectors.toList());

			task.setTaskList(taskList);

			taskService.SaveTaskDetails(task);

			return ResponseEntity.ok("Task details saved successfully.");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving the task: " + e.getMessage());
		}
	}

	private Blob convertToBlob(MultipartFile file) throws IOException, SQLException, java.io.IOException {
		if (file != null && !file.isEmpty()) {
			byte[] bytes = file.getBytes();
			return new javax.sql.rowset.serial.SerialBlob(bytes);
		} else {
			return null;
		}
	}

//////////////////////////
	@PostMapping("/taskDetails/save")
	public ResponseEntity<?> saveTaskDetails(@RequestBody Task task) {
		try {
			taskService.SaveTaskDetails(task);
			return ResponseEntity.ok(task);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving product: " + e.getMessage());
		}
	}

//	@PostMapping("/taskDetails/save")
//	public ResponseEntity<?> saveTaskDetails(@RequestBody Task task) {
//	    try {
//	    	
//	    	long id1 = task.getProjectId();
//	    	String pr = task.getTypeOfProject();
//	    	String com = pr.substring(0, 3); 
//
//	    	
//	        String base64Image = task.getUrl();
//
//	        if (base64Image != null) {
//	            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
//	            Blob blob = null;
//
//	            try {
//	                blob = new javax.sql.rowset.serial.SerialBlob(imageBytes);
//	            } catch (SQLException e) {
//	                e.printStackTrace();
//	            }
//
//	            task.setFileUpload(blob);	        
//	        }
//	        
//	        List<TaskList> taskList = task.getTaskList();
//            for (TaskList taskLoop : taskList) {
//            	taskLoop.setPending(true);
//            	
//            	long id = taskLoop.getTaskListId();
//                String userId = com + id;
//                taskLoop.setProjectkey(userId);
//            	
//            }
//
//	        taskService.SaveTaskDetails(task);
//	        return ResponseEntity.ok(task);
//	    } catch (Exception e) {
//	        // Log the exception for debugging purposes
//	        e.printStackTrace();
//	        
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                .body("Error saving product: " + e.getMessage());
//	    }
//	}

	@GetMapping("/taskDetail")
	public ResponseEntity<?> displayAllImages(@RequestParam(required = true) String task) {
		try {
			if ("taskDetails".equals(task)) {
				List<Task> tasks = taskService.listTask();
				List<Task> TaskResponses = new ArrayList<>();

				for (Task taskss : tasks) {
					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(taskss);
					String imageUrl = "taskFile/" + randomNumber + "/" + taskss.getProjectId() + "." + fileExtension;

					Task taskResponse = new Task();
					taskResponse.setProjectId(taskss.getProjectId());
					taskResponse.setUrl(imageUrl);
					taskResponse.setCompleted(taskss.isCompleted());
					taskResponse.setDate(taskss.getDate());
					taskResponse.setDevelopment(taskss.isDevelopment());
					taskResponse.setNotCompleted(taskss.isNotCompleted());
					taskResponse.setProjectId(taskss.getProjectId());
					taskResponse.setProjectStatus(taskss.getProjectStatus());
					taskResponse.setResearch(taskss.isResearch());
					taskResponse.setTaskList(taskss.getTaskList());
					taskResponse.setTesting(taskss.isTesting());
					taskResponse.setTypeOfProject(taskss.getTypeOfProject());
					taskResponse.setTaskId(taskss.getTaskId());
					taskResponse.setClientId(taskss.getClientId());

					TaskResponses.add(taskResponse);
				}

				return ResponseEntity.ok().body(TaskResponses);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("The provided CompanyAssetsType is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/task/edit/{id}")
	public ResponseEntity<?> updateResearch1ccc(@PathVariable("id") Long researchId, @RequestBody Task task) {
		try {
			Task existingTask = taskService.findProjectListDemoById(researchId);

			if (existingTask == null) {
				return ResponseEntity.notFound().build();
			}
			
			String ERT=existingTask.getTypeOfProject();
			long projectId = existingTask.getProjectId();
			ClientRequirement requirement = clientRequirementService.getById(projectId);
			String nameConverter = requirement.getProjectName();
		
			if(task.getTaskList() !=null) {
			existingTask.setTaskList(task.getTaskList());
			}
			
			List<TaskList> taskList = task.getTaskList();
			for (TaskList taskLoop : taskList) {
				if (taskLoop.getStartDate() != null && taskLoop.getUpdated() != null
						&& taskLoop.getStartDate().after(taskLoop.getUpdated())) {
					String errorMessage = "FromDate cannot be later than ToDate.";
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				}
				long id = nextId++;
		    	String com = nameConverter.substring(0, 3).toUpperCase();
		    	String rr = ERT.substring(0, 2).toUpperCase();

		    	String projectKey = String.format("%s(%s)%s", com, rr, id);			
		    	taskLoop.setProjectkey(projectKey);
			
			}

			existingTask.setFileUpload(task.getFileUpload());
			existingTask.setUrl(task.getUrl());
			String base64Image = task.getUrl();
			if (base64Image != null) {
				byte[] imageBytes = Base64.getDecoder().decode(base64Image);
				Blob blob = null;
				try {
					blob = new javax.sql.rowset.serial.SerialBlob(imageBytes);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				existingTask.setFileUpload(blob);
			}
			int randomNumber = generateRandomNumber();
			String fileExtension = getFileExtensionForImage(task);
			String imageUrl = "task/" + randomNumber + "/" + task.getTaskId() + "." + fileExtension;
			existingTask.setSharingFileUrl(imageUrl);
			taskService.SaveTaskDetails(existingTask);
			return ResponseEntity.ok(existingTask);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
	private static long nextId = 1;
	
	
	@PutMapping("/task/edit1/{id}")
	public ResponseEntity<Task> updateResearch(@PathVariable("id") Long researchId, @RequestBody Task task) {
		try {
			Task existingTask = taskService.findProjectListDemoById(researchId);
			if (existingTask == null) {
				return ResponseEntity.notFound().build();
			}
			existingTask.setTaskList(task.getTaskList());

			taskService.SaveTaskDetails(existingTask);
			return ResponseEntity.ok(existingTask);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/task/status/edit/{id}")
	public ResponseEntity<?> updateClientFormForProject(@PathVariable("id") long id, @RequestBody Task task) {
		try {
			Task existingTask = taskService.findProjectListDemoById(id);
			if (existingTask == null) {
				return ResponseEntity.notFound().build();
			}
//			if (existingTask.isCompleted() && existingTask.getTypeOfProject().equals("research")) {
//				String errorMessage = "A Project is Moved to Research Quotation File Table";
//				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
//			}

			if (existingTask.isCompleted() && existingTask.getTypeOfProject().equals("development")) {
				String errorMessage = "A Project is Moved to Research Development File Table";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}

			if (existingTask.isCompleted() && existingTask.getTypeOfProject().equals("testing")) {
				String errorMessage = "A Project is Moved to Testing Development File Table";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}

			if (existingTask.isNotCompleted() && existingTask.getTypeOfProject().equals("research")) {
				String errorMessage = "A Project is Rejected";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}

			if (existingTask.isNotCompleted() && existingTask.getTypeOfProject().equals("development")) {
				String errorMessage = "A Project is Rejected";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}
			if (existingTask.isNotCompleted() && existingTask.getTypeOfProject().equals("testing")) {
				String errorMessage = "A Project is Rejected";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}

			existingTask.setTypeOfProject(task.getTypeOfProject());
			existingTask.setProjectStatus(task.getProjectStatus());
			existingTask.setFileUpload(task.getFileUpload());
			existingTask.setUrl(task.getUrl());
			String base64Image = task.getUrl();
			if (base64Image != null) {
				byte[] imageBytes = Base64.getDecoder().decode(base64Image);
				Blob blob = null;
				try {
					blob = new javax.sql.rowset.serial.SerialBlob(imageBytes);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				existingTask.setFileUpload(blob);
			}
			int randomNumber = generateRandomNumber();
			String fileExtension = getFileExtensionForImage(task);

			if ("completed".equals(task.getProjectStatus()) && "research".equals(task.getTypeOfProject())) {
				existingTask.setCompleted(true);
				existingTask.setResearch(true);

				existingTask.setCompletedDate(LocalDate.now());
			} else if ("completed".equals(task.getProjectStatus()) && "development".equals(task.getTypeOfProject())) {
				existingTask.setCompleted(true);
				existingTask.setDevelopment(true);

				existingTask.setCompletedDate(LocalDate.now());
			} else if ("completed".equals(task.getProjectStatus()) && "project".equals(task.getTypeOfProject())) {
				existingTask.setCompleted(true);
				existingTask.setProject(true);

				existingTask.setCompletedDate(LocalDate.now());
			} else if ("completed".equals(task.getProjectStatus()) && "testing".equals(task.getTypeOfProject())) {
				existingTask.setCompleted(true);
				existingTask.setTesting(true);

				existingTask.setCompletedDate(LocalDate.now());
			} else if ("completed".equals(task.getProjectStatus()) && "hosting".equals(task.getTypeOfProject())) {
				existingTask.setCompleted(true);
				existingTask.setHosting(true);

				existingTask.setCompletedDate(LocalDate.now());
			}else if ("notCompleted".equals(task.getProjectStatus()) && "research".equals(task.getTypeOfProject())) {
				existingTask.setNotCompleted(true);
				existingTask.setCompletedDate(LocalDate.now());
			} else if ("notCompleted".equals(task.getProjectStatus())
					&& "development".equals(task.getTypeOfProject())) {
				existingTask.setNotCompleted(true);
				existingTask.setCompletedDate(LocalDate.now());
			} else if ("notCompleted".equals(task.getProjectStatus()) && "testing".equals(task.getTypeOfProject())) {
				existingTask.setNotCompleted(true);
				existingTask.setCompletedDate(LocalDate.now());
			} else if ("notCompleted".equals(task.getProjectStatus()) && "project".equals(task.getTypeOfProject())) {
				existingTask.setNotCompleted(true);
				existingTask.setCompletedDate(LocalDate.now());
			}else if ("notCompleted".equals(task.getProjectStatus()) && "hosting".equals(task.getTypeOfProject())) {
				existingTask.setNotCompleted(true);
				existingTask.setCompletedDate(LocalDate.now());
			} else {
				existingTask.setCompleted(false);
				existingTask.setNotCompleted(false);
			}

			taskService.SaveTaskDetails(existingTask);

			String imageUrl = "task/" + randomNumber + "/" + existingTask.getTaskId() + "." + fileExtension;
			existingTask.setSharingFileUrl(imageUrl);
			if ("completed".equals(existingTask.getProjectStatus())
					&& "development".equals(existingTask.getTypeOfProject())) {

				long projectId = existingTask.getProjectId();
				String sharingFileUrl = existingTask.getSharingFileUrl();
				long clientId = existingTask.getClientId();
				long empId = existingTask.getEmployeeReportId();
				ResearchDevelopmentFile development = new ResearchDevelopmentFile();

				development.setClientId(clientId);
				development.setProjectId(projectId);
				development.setDate(LocalDate.now());
				development.setUrl(sharingFileUrl);
				development.setEmployeeId(empId);
				researchDevelopmentFileService.SaveResearchDevelopmentFile(development);
			}

			if ("completed".equals(existingTask.getProjectStatus())
					&& "research".equals(existingTask.getTypeOfProject())) {
				long projectId = existingTask.getProjectId();
				String sharingFileUrl = existingTask.getSharingFileUrl();
				long clientId = existingTask.getClientId();
				long empId = existingTask.getEmployeeReportId();
				ResearchQuotation quotation = new ResearchQuotation();

				quotation.setClientId(clientId);
				quotation.setProjectId(projectId);
				quotation.setDate(LocalDate.now());
				quotation.setUrl(sharingFileUrl);
				researchQuotationService.SaveResearchQuotation(quotation);
			}

			if ("completed".equals(existingTask.getProjectStatus())
					&& "testing".equals(existingTask.getTypeOfProject())) {
				long projectId = existingTask.getProjectId();
				String sharingFileUrl = existingTask.getSharingFileUrl();
				long clientId = existingTask.getClientId();
				long empId = existingTask.getEmployeeReportId();
				TestingDocumentation testing = new TestingDocumentation();

				testing.setClientId(clientId);
				testing.setEmployeeId(empId);
				testing.setProjectId(projectId);
				testing.setDate(LocalDate.now());
				testing.setUrl(sharingFileUrl);
				testingDocumentationService.SaveTestingDocumentationDetails(testing);
			}

			if ("completed".equals(existingTask.getProjectStatus())
					&& "project".equals(existingTask.getTypeOfProject())) {
				long projectId = existingTask.getProjectId();
				String sharingFileUrl = existingTask.getSharingFileUrl();
				long clientId = existingTask.getClientId();
				long empId = existingTask.getEmployeeReportId();

				ProjectDocumentation project = new ProjectDocumentation();

				project.setClientId(clientId);
				project.setProjectId(projectId);
				project.setEmployeeId(empId);
				project.setDate(LocalDate.now());
				project.setUrl(sharingFileUrl);
				projectDocumentationService.SaveProjectDocumentationDetails(project);
			}
			if ("completed".equals(existingTask.getProjectStatus())
					&& "hosting".equals(existingTask.getTypeOfProject())) {
				long projectId = existingTask.getProjectId();
				String sharingFileUrl = existingTask.getSharingFileUrl();
				long clientId = existingTask.getClientId();
				long empId = existingTask.getEmployeeReportId();

				HostingDocumentation hosting = new HostingDocumentation();

				hosting.setClientId(clientId);
				hosting.setProjectId(projectId);
//				hosting.setEmployeeId(empId);
				hosting.setDate(LocalDate.now());
				hosting.setUrl(sharingFileUrl);
				hostingService.SaveHostingDetails(hosting);
			}

			return ResponseEntity.ok(existingTask);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}


    @GetMapping("task/{randomNumber}/{id:.+}")
    public ResponseEntity<Resource> serveImage(@PathVariable("randomNumber") int randomNumber,
                                                @PathVariable("id") String id) {
        String[] parts = id.split("\\.");

        if (parts.length != 2) {
            return ResponseEntity.badRequest().build();
        }

        String fileExtension = parts[1];

        Long imageId;
        try {
            imageId = Long.parseLong(parts[0]);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }

        Task image = taskService.findProjectListDemoById(imageId);

        if (image == null || image.getFileUpload() == null) { // Check if image or getFileUpload() is null
            return ResponseEntity.notFound().build();
        }

        byte[] imageBytes;
        try {
            imageBytes = image.getFileUpload().getBytes(1, (int) image.getFileUpload().length());
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        ByteArrayResource resource = new ByteArrayResource(imageBytes);
        HttpHeaders headers = new HttpHeaders();

        MediaType mediaType = determineMediaType(fileExtension);

        headers.setContentType(mediaType);

        return ResponseEntity.ok().headers(headers).body(resource);
    }

	private String getFileExtensionForImage(Task image) {
		if (image == null || image.getUrl() == null || image.getUrl().isEmpty()) {
			return "jpg";
		}

		String url = image.getUrl();
		if (url.endsWith(".png")) {
			return "png";
		} else if (url.endsWith(".jpg")) {
			return "jpg";
		} else if (url.endsWith(".pdf")) {
			return "pdf";
		} else if (url.endsWith(".mp4")) {
			return "mp4"; // Assuming .mp4 is a video format
		} else if (url.endsWith(".avi")) {
			return "avi"; // Assuming .avi is a video format
		} else if (url.endsWith(".mkv")) {
			return "mkv"; // Assuming .mkv is a video format
		} else {
			return "pdf"; // Default to jpg if the format is not recognized
		}
	}

	private String determineFileExtension(byte[] fileBytes) {
		try {
			String fileSignature = bytesToHex(Arrays.copyOfRange(fileBytes, 0, 4));
			if (fileSignature.startsWith("89504E47")) {
				return "png";
			} else if (fileSignature.startsWith("FFD8FF")) {
				return "jpg";
			} else if (fileSignature.startsWith("52494646")) {
				return "webp";
			} else if (fileSignature.startsWith("47494638")) {
				return "gif";
			} else if (fileSignature.startsWith("66747970") || fileSignature.startsWith("00000020")) {
				return "mp4";
			} else if (fileSignature.startsWith("25504446")) {
				return "pdf";
			}
		} catch (Exception e) {
		}
		return "pdf";
	}

	private MediaType determineMediaType(String extension) {
		switch (extension) {
		case "png":
			return MediaType.IMAGE_PNG;
		case "jpg":
			return MediaType.IMAGE_JPEG;
		case "pdf":
			return MediaType.APPLICATION_PDF;
		case "webp":
			return MediaType.parseMediaType("image/webp");
		case "gif":
			return MediaType.parseMediaType("image/gif");
		case "mp4":
			return MediaType.parseMediaType("video/mp4");
		default:
			return MediaType.APPLICATION_PDF;
		}
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	@DeleteMapping("/task/delete/{id}")
	public ResponseEntity<String> deleteprojectListDemoId(@PathVariable("id") Long projectListDemoId) {
		taskService.deleteProjectListDemoById(projectListDemoId);
		return ResponseEntity.ok("Task Detail deleted successfully With Id :" + projectListDemoId);

	}
	
	private String getStringValue(Map<String, Object> map, String key) {
	    Object value = map.get(key);
	    return (value != null) ? value.toString() : "";
	}

	@GetMapping("/demo")
	public ResponseEntity<?> getAllTaskDetailsfff() {
		List<Map<String, Object>> mainList = new ArrayList<>();
		List<Map<String, Object>> taskDetails = taskrepository.getAllprojectTask();
		Map<String, Map<String, List<Map<String, Object>>>> taskGroupMap = taskDetails.stream().collect(
				Collectors.groupingBy(action -> action.get("task_id").toString(), Collectors.groupingBy(action -> {
					Object taskListIdObj = action.get("task_list_id");
					return (taskListIdObj != null) ? taskListIdObj.toString() : "DefaultTaskListID";
				}, Collectors.toList())));
		for (Entry<String, Map<String, List<Map<String, Object>>>> taskLoop : taskGroupMap.entrySet()) {
			Map<String, Object> taskMap = new HashMap<>();
			taskMap.put("taskId", taskLoop.getKey());
			List<Map<String, Object>> taskList = new ArrayList<>();
			for (Entry<String, List<Map<String, Object>>> taskListLoop : taskLoop.getValue().entrySet()) {
				Map<String, Object> taskListMap = new HashMap<>();
				taskListMap.put("taskListId", taskListLoop.getKey());
				taskMap.put("date", taskListLoop.getValue().get(0).get("date"));
				taskMap.put("projectId", taskListLoop.getValue().get(0).get("project_id"));
				taskMap.put("projectName", taskListLoop.getValue().get(0).get("project_name"));
				taskMap.put("projectStatus", taskListLoop.getValue().get(0).get("task_status"));
				taskMap.put("todayDate", taskListLoop.getValue().get(0).get("today_date"));
				taskMap.put("typeOfProject", taskListLoop.getValue().get(0).get("type_of_project"));
				taskListMap.put("cancellationReason", taskListLoop.getValue().get(0).get("cancellation_reason"));
				taskListMap.put("category", taskListLoop.getValue().get(0).get("category"));
				taskListMap.put("comments", taskListLoop.getValue().get(0).get("comments"));
				taskListMap.put("completedDate", taskListLoop.getValue().get(0).get("completed_date"));
				taskListMap.put("created", taskListLoop.getValue().get(0).get("created"));
				taskListMap.put("departmentId", taskListLoop.getValue().get(0).get("department_id"));
				taskListMap.put("departmentName", taskListLoop.getValue().get(0).get("department_name"));
				taskListMap.put("employeeId", taskListLoop.getValue().get(0).get("employee_id"));
				taskListMap.put("employeeName", taskListLoop.getValue().get(0).get("user_name"));
				taskListMap.put("employeeReportId", taskListLoop.getValue().get(0).get("employee_report_id"));
				taskListMap.put("employeeReportName", taskListLoop.getValue().get(0).get("empName"));
				taskListMap.put("holdReason", taskListLoop.getValue().get(0).get("hold_reson"));
				taskListMap.put("traineeId", taskListLoop.getValue().get(0).get("trainee_id"));
				taskListMap.put("label", taskListLoop.getValue().get(0).get("label"));
				taskListMap.put("priority", taskListLoop.getValue().get(0).get("priority"));
				taskListMap.put("projectStatus", taskListLoop.getValue().get(0).get("project_status"));
				taskListMap.put("projectKey", taskListLoop.getValue().get(0).get("projectKey"));
				taskListMap.put("startDate", taskListLoop.getValue().get(0).get("start_date"));
				taskListMap.put("summary", taskListLoop.getValue().get(0).get("summary"));
				taskListMap.put("type", taskListLoop.getValue().get(0).get("type"));
				taskListMap.put("updated", taskListLoop.getValue().get(0).get("updated"));
				taskListMap.put("roleId", taskListLoop.getValue().get(0).get("role_id"));
				taskListMap.put("roleName", taskListLoop.getValue().get(0).get("role_name"));
				long taskId = Long.parseLong(taskLoop.getKey());
				List<Map<String, Object>> deptDetails = taskrepository.getAllDepartmentDetails(taskId);
				Map<String, List<Map<String, Object>>> deptGroupMap = deptDetails.stream()
						.collect(Collectors.groupingBy(action -> action.get("department_id").toString()));
				List<Map<String, Object>> deptList = new ArrayList<>();
				for (Entry<String, List<Map<String, Object>>> deptLoop : deptGroupMap.entrySet()) {
					Map<String, Object> deptMap = new HashMap<>();
					deptMap.put("departmentId", deptLoop.getKey());
					deptMap.put("departmentName", deptLoop.getValue().get(0).get("department_name"));
					deptList.add(deptMap);
				}
				taskMap.put("departmentDetails", deptList);
				taskList.add(taskListMap);
			}
			taskMap.put("taskList", taskList);
			mainList.add(taskMap);
		}
		return ResponseEntity.ok(mainList);
	}

	@GetMapping("/task/view/{id}")
	public ResponseEntity<?> getAllTaskDetailsId(@PathVariable("id") Long task_id) {

		Map<String, Object> mainList = new HashMap<>();
		List<Map<String, Object>> taskDetails = taskrepository.getProjectTask(task_id);
		Map<String, List<Map<String, Object>>> taskGroupMap = taskDetails.stream()
				.collect(Collectors.groupingBy(action -> action.get("task_id").toString()

				));

		for (Entry<String, List<Map<String, Object>>> taskLoop : taskGroupMap.entrySet()) {
			Map<String, Object> taskMap = new HashMap<>();
			taskMap.put("taskId", taskLoop.getKey());

			List<Map<String, Object>> taskList = new ArrayList<>();
			for (Map<String, Object> taskListLoop : taskLoop.getValue()) {
				Map<String, Object> taskListMap = new HashMap<>();  
				int randomNumber = generateRandomNumber();
				taskMap.put("date", taskListLoop.get("date"));
				taskMap.put("projectId", taskListLoop.get("project_id"));
				taskMap.put("projectName", taskListLoop.get("project_name"));
				taskMap.put("projectStatus", taskListLoop.get("task_status"));
				taskMap.put("todayDate", taskListLoop.get("today_date"));
				taskMap.put("typeOfProject", taskListLoop.get("type_of_project"));
				taskListMap.put("cancellationReason", taskListLoop.get("cancellation_reason"));
				taskListMap.put("category", taskListLoop.get("category"));
				taskListMap.put("traineeId", taskListLoop.get("trainee_id"));
				taskListMap.put("comments", taskListLoop.get("comments"));
				taskListMap.put("completedDate", taskListLoop.get("completed_date"));
				taskListMap.put("created", taskListLoop.get("created"));
				taskListMap.put("departmentId", taskListLoop.get("department_id"));
				taskListMap.put("departmentName", taskListLoop.get("department_name"));
				taskListMap.put("employeeId", taskListLoop.get("employee_id"));
				taskListMap.put("employeeName", taskListLoop.get("userName"));
				taskListMap.put("employeeReportId", taskListLoop.get("employee_report_id"));
				taskListMap.put("employeeReportName", taskListLoop.get("empName"));
				taskListMap.put("holdReason", taskListLoop.get("hold_reson"));
				taskListMap.put("label", taskListLoop.get("label"));
				taskListMap.put("priority", taskListLoop.get("priority"));
				taskListMap.put("projectStatus", taskListLoop.get("project_status"));
				taskListMap.put("projectKey", taskListLoop.get("projectKey"));
				taskListMap.put("startDate", taskListLoop.get("start_date"));
				taskListMap.put("summary", taskListLoop.get("summary"));
				taskListMap.put("type", taskListLoop.get("type"));
				taskListMap.put("updated", taskListLoop.get("updated"));
				taskListMap.put("taskListId", taskListLoop.get("task_list_id"));
				taskListMap.put("roleId", taskListLoop.get("role_id_coalesce"));
				taskListMap.put("roleName", taskListLoop.get("role_name_coalesce"));
				 if (taskListLoop.containsKey("employee_id") && taskListLoop.get("employee_id") != null) {
                     String fileExtension = getFileExtensionForImage(taskListLoop);
                     String imageUrl = "profile/" + randomNumber + "/" + taskListLoop.get("employee_id") + "." + fileExtension;
                     taskListMap.put("profile", imageUrl);
                 } 
                 
                 else if (taskListLoop.containsKey("trainee_id") && taskListLoop.get("trainee_id") != null) {
                     String imageUrl = "training/" + randomNumber + "/" + taskListLoop.get("trainee_id");
                     taskListMap.put("profile", imageUrl);
                 }
				taskList.add(taskListMap);

			}
			taskMap.put("taskList", taskList);
			mainList.putAll(taskMap);

		}

		return ResponseEntity.ok(mainList);

	}

	@GetMapping("/task/employee/report/{id}/{roleId}")
	public ResponseEntity<?> getEmployeeReportId(@PathVariable("id") Long employee_id,
			@PathVariable("roleId") Long roleId) {
		List<Map<String, Object>> taskDetails = taskrepository.getEmployeeReportId(employee_id, roleId);
		 Map<Object, Map<Object, Map<Object, List<Map<String, Object>>>>> taskGroupMap = taskDetails
				.stream()
				.collect(Collectors.groupingBy(action -> action.get("employee_report_id").toString(),
						(Collectors.groupingBy(action -> action.get("task_id").toString(),
										(Collectors.groupingBy(action -> action.get("task_list_id").toString()))))));
		Map<String, Object> employeeList = new HashMap<>();
		for (Entry<Object, Map<Object, Map<Object, List<Map<String, Object>>>>> employeeGroup : taskGroupMap
				.entrySet()) {
			Map<String, Object> employeeMap = new HashMap<>();
			employeeMap.put("employeeReportId", employeeGroup.getKey());
			List<Map<String, Object>> mainList = new ArrayList<>();
			for (Entry<Object, Map<Object, List<Map<String, Object>>>> taskLoop : employeeGroup.getValue()
					.entrySet()) {
				Map<String, Object> taskMap = new HashMap<>();
				taskMap.put("taskId", taskLoop.getKey());
				
					List<Map<String, Object>> taskList = new ArrayList<>();

					for (Entry<Object, List<Map<String, Object>>> taskListLoop : taskLoop.getValue().entrySet()) {
						Map<String, Object> taskListMap = new HashMap<>();
						taskListMap.put("taskListId", taskListLoop.getKey());
						taskMap.put("date", taskListLoop.getValue().get(0).get("date"));
						taskMap.put("projectId", taskListLoop.getValue().get(0).get("project_id"));
						taskMap.put("projectName", taskListLoop.getValue().get(0).get("project_name"));
						taskMap.put("projectStatus", taskListLoop.getValue().get(0).get("task_status"));
						taskMap.put("todayDate", taskListLoop.getValue().get(0).get("today_date"));
						taskMap.put("typeOfProject", taskListLoop.getValue().get(0).get("type_of_project"));
						taskListMap.put("cancellationReason",taskListLoop.getValue().get(0).get("cancellation_reason"));
						taskListMap.put("category", taskListLoop.getValue().get(0).get("category"));
						taskListMap.put("comments", taskListLoop.getValue().get(0).get("comments"));
						taskListMap.put("completedDate", taskListLoop.getValue().get(0).get("completed_date"));
						taskListMap.put("created", taskListLoop.getValue().get(0).get("created"));
						taskListMap.put("departmentId", taskListLoop.getValue().get(0).get("department_id"));
						taskListMap.put("departmentName", taskListLoop.getValue().get(0).get("department_name"));
						taskListMap.put("employeeId", taskListLoop.getValue().get(0).get("employee_id"));
						taskListMap.put("employeeName", taskListLoop.getValue().get(0).get("user_name"));
						taskListMap.put("employeeReportId", taskListLoop.getValue().get(0).get("employee_report_id"));
						taskListMap.put("employeeReportName", taskListLoop.getValue().get(0).get("empName"));
						employeeMap.put("employeeReportName", taskListLoop.getValue().get(0).get("empName"));
						taskListMap.put("holdReason", taskListLoop.getValue().get(0).get("hold_reson"));
						taskListMap.put("label", taskListLoop.getValue().get(0).get("label"));
						taskListMap.put("priority", taskListLoop.getValue().get(0).get("priority"));
						taskListMap.put("projectStatus", taskListLoop.getValue().get(0).get("project_status"));
						taskListMap.put("projectKey", taskListLoop.getValue().get(0).get("projectKey"));
						taskListMap.put("startDate", taskListLoop.getValue().get(0).get("start_date"));
						taskListMap.put("summary", taskListLoop.getValue().get(0).get("summary"));
						taskListMap.put("type", taskListLoop.getValue().get(0).get("type"));
						taskListMap.put("updated", taskListLoop.getValue().get(0).get("updated"));
						taskListMap.put("traineeId", taskListLoop.getValue().get(0).get("trainee_id"));
						employeeMap.put("roleId", taskListLoop.getValue().get(0).get("role_id"));
						employeeMap.put("roleName", taskListLoop.getValue().get(0).get("role_name"));
						taskListMap.put("roleId", taskListLoop.getValue().get(0).get("role_id"));
						taskListMap.put("roleName", taskListLoop.getValue().get(0).get("role_name"));
						long taskId = Long.parseLong((String) taskLoop.getKey());
						List<Map<String, Object>> deptDetails = taskrepository.getAllDepartmentDetails(taskId);
						Map<String, List<Map<String, Object>>> deptGroupMap = deptDetails.stream()
								.collect(Collectors.groupingBy(action -> action.get("department_id").toString()));
						List<Map<String, Object>> deptList = new ArrayList<>();
						for (Entry<String, List<Map<String, Object>>> deptLoop : deptGroupMap.entrySet()) {
							Map<String, Object> deptMap = new HashMap<>();
							deptMap.put("departmentId", deptLoop.getKey());
							deptMap.put("departmentName", deptLoop.getValue().get(0).get("department_name"));
							deptList.add(deptMap);
						}
						taskMap.put("departmentDetails", deptList);
					
						taskList.add(taskListMap);
					}
					
					taskMap.put("taskList", taskList);
			
				

				mainList.add(taskMap);
			}
			employeeMap.put("project", mainList);
			employeeList.putAll(employeeMap);
		}
		return ResponseEntity.ok(employeeList);
	}

//	@GetMapping("/task/employee/role/{id}/{roleId}")
//	public ResponseEntity<?> getAllTaskDetailsId(@PathVariable("id") Long employeeId,
//			@PathVariable("roleId") Long roleId) {
//		List<Map<String, Object>> taskDetails = taskrepository.getAllEmployeeWithRole(employeeId, roleId);
//	
//		        
//		    
//		Map<String, Map<String, Map<String, List<Map<String, Object>>>>> taskGroupMap = taskDetails.stream()
//				.collect(Collectors.groupingBy(action -> action.get("employee_id").toString(),
//						(Collectors.groupingBy(action -> action.get("task_id").toString(),
//								(Collectors.groupingBy(action -> action.get("task_list_id").toString()))))));
//		Map<String, Object> employeeList = new HashMap<>();
//		for (Entry<String, Map<String, Map<String, List<Map<String, Object>>>>> employeeGroup : taskGroupMap
//				.entrySet()) {
//			Map<String, Object> employeeMap = new HashMap<>();
//			employeeMap.put("employeeId", employeeGroup.getKey());
//			List<Map<String, Object>> mainList = new ArrayList<>();
//			for (Entry<String, Map<String, List<Map<String, Object>>>> taskLoop : employeeGroup.getValue().entrySet()) {
//				Map<String, Object> taskMap = new HashMap<>();
//				taskMap.put("taskId", taskLoop.getKey());
//				List<Map<String, Object>> taskList = new ArrayList<>();
//				for (Entry<String, List<Map<String, Object>>> taskListLoop : taskLoop.getValue().entrySet()) {
//					Map<String, Object> taskListMap = new HashMap<>();
//					taskListMap.put("taskListId", taskListLoop.getKey());
//					taskMap.put("date", taskListLoop.getValue().get(0).get("date"));
//					taskMap.put("projectId", taskListLoop.getValue().get(0).get("project_id"));
//					taskMap.put("projectName", taskListLoop.getValue().get(0).get("project_name"));
//					taskMap.put("projectStatus", taskListLoop.getValue().get(0).get("task_status"));
//					taskMap.put("todayDate", taskListLoop.getValue().get(0).get("today_date"));
//					taskMap.put("typeOfProject", taskListLoop.getValue().get(0).get("type_of_project"));
//					taskListMap.put("cancellationReason", taskListLoop.getValue().get(0).get("cancellation_reason"));
//					taskListMap.put("category", taskListLoop.getValue().get(0).get("category"));
//					taskListMap.put("comments", taskListLoop.getValue().get(0).get("comments"));
//					taskListMap.put("completedDate", taskListLoop.getValue().get(0).get("completed_date"));
//					taskListMap.put("created", taskListLoop.getValue().get(0).get("created"));
//					taskListMap.put("departmentId", taskListLoop.getValue().get(0).get("department_id"));
//					taskListMap.put("departmentName", taskListLoop.getValue().get(0).get("department_name"));
//					taskListMap.put("employeeId", taskListLoop.getValue().get(0).get("employee_id"));
//					employeeMap.put("employeeName", taskListLoop.getValue().get(0).get("user_name"));
//					employeeMap.put("employeeId", taskListLoop.getValue().get(0).get("employee_id"));
//					taskListMap.put("employeeName", taskListLoop.getValue().get(0).get("user_name"));
//					taskListMap.put("employeeReportId", taskListLoop.getValue().get(0).get("employee_report_id"));
//					taskListMap.put("employeeReportName", taskListLoop.getValue().get(0).get("empName"));
//					taskListMap.put("holdReason", taskListLoop.getValue().get(0).get("hold_reson"));
//					taskListMap.put("label", taskListLoop.getValue().get(0).get("label"));
//					taskListMap.put("priority", taskListLoop.getValue().get(0).get("priority"));
//					taskListMap.put("projectStatus", taskListLoop.getValue().get(0).get("project_status"));
//					taskListMap.put("projectKey", taskListLoop.getValue().get(0).get("projectKey"));
//					taskListMap.put("startDate", taskListLoop.getValue().get(0).get("start_date"));
//					taskListMap.put("summary", taskListLoop.getValue().get(0).get("summary"));
//					taskListMap.put("type", taskListLoop.getValue().get(0).get("type"));
//					taskListMap.put("updated", taskListLoop.getValue().get(0).get("updated"));
//					employeeMap.put("roleId", taskListLoop.getValue().get(0).get("role_id"));
//					employeeMap.put("roleName", taskListLoop.getValue().get(0).get("role_name"));
//					taskListMap.put("roleId", taskListLoop.getValue().get(0).get("role_id"));
//					taskListMap.put("roleName", taskListLoop.getValue().get(0).get("role_name"));
//					taskList.add(taskListMap);
//				
//			}
//				taskMap.put("taskList", taskList);
//				mainList.add(taskMap);
//			}
//			employeeMap.put("project", mainList);
//			employeeList.putAll(employeeMap);
//		}
//		return ResponseEntity.ok(employeeList);
//	}

	@GetMapping("/task/employee/role/{id}/{roleId}")
	public ResponseEntity<?> getAllTraineeandEmployeeWithRole(@PathVariable("id") Long id,
			@PathVariable("roleId") Long roleId) {
		List<Map<String, Object>> taskDetails = taskrepository.getAllTraineeandEmployeeWithRole(id, roleId);
		Map<String, Map<String, Map<String, List<Map<String, Object>>>>> taskGroupMap = taskDetails.stream()
				.collect(Collectors.groupingBy(action -> action.get("id").toString(),
						(Collectors.groupingBy(action -> action.get("task_id").toString(),
								(Collectors.groupingBy(action -> action.get("task_list_id").toString()))))));
		Map<String, Object> employeeList = new HashMap<>();
		for (Entry<String, Map<String, Map<String, List<Map<String, Object>>>>> employeeGroup : taskGroupMap
				.entrySet()) {
			Map<String, Object> employeeMap = new HashMap<>();
			employeeMap.put("id", employeeGroup.getKey());
			List<Map<String, Object>> mainList = new ArrayList<>();
			for (Entry<String, Map<String, List<Map<String, Object>>>> taskLoop : employeeGroup.getValue().entrySet()) {
				Map<String, Object> taskMap = new HashMap<>();
				taskMap.put("taskId", taskLoop.getKey());
				List<Map<String, Object>> taskList = new ArrayList<>();
				for (Entry<String, List<Map<String, Object>>> taskListLoop : taskLoop.getValue().entrySet()) {
					Map<String, Object> taskListMap = new HashMap<>();
					taskListMap.put("taskListId", taskListLoop.getKey());
					taskMap.put("date", taskListLoop.getValue().get(0).get("date"));
					taskMap.put("projectId", taskListLoop.getValue().get(0).get("project_id"));
					taskMap.put("projectName", taskListLoop.getValue().get(0).get("project_name"));
					taskMap.put("projectStatus", taskListLoop.getValue().get(0).get("task_status"));
					taskMap.put("todayDate", taskListLoop.getValue().get(0).get("today_date"));
					taskMap.put("typeOfProject", taskListLoop.getValue().get(0).get("type_of_project"));
					taskListMap.put("cancellationReason", taskListLoop.getValue().get(0).get("cancellation_reason"));
					taskListMap.put("category", taskListLoop.getValue().get(0).get("category"));
					taskListMap.put("comments", taskListLoop.getValue().get(0).get("comments"));
					taskListMap.put("completedDate", taskListLoop.getValue().get(0).get("completed_date"));
					taskListMap.put("created", taskListLoop.getValue().get(0).get("created"));
					taskListMap.put("departmentId", taskListLoop.getValue().get(0).get("department_id"));
					taskListMap.put("departmentName", taskListLoop.getValue().get(0).get("department_name"));
					taskListMap.put("id", taskListLoop.getValue().get(0).get("id"));
					employeeMap.put("userName", taskListLoop.getValue().get(0).get("user_name"));
					taskListMap.put("userName", taskListLoop.getValue().get(0).get("user_name"));
					taskListMap.put("employeeReportId", taskListLoop.getValue().get(0).get("empreporterroleid"));
					taskListMap.put("employeeReportName", taskListLoop.getValue().get(0).get("employeereportername"));
					taskListMap.put("holdReason", taskListLoop.getValue().get(0).get("hold_reson"));
					taskListMap.put("label", taskListLoop.getValue().get(0).get("label"));
					taskListMap.put("priority", taskListLoop.getValue().get(0).get("priority"));
					taskListMap.put("projectStatus", taskListLoop.getValue().get(0).get("project_status"));
					taskListMap.put("projectKey", taskListLoop.getValue().get(0).get("projectKey"));
					taskListMap.put("startDate", taskListLoop.getValue().get(0).get("start_date"));
					taskListMap.put("summary", taskListLoop.getValue().get(0).get("summary"));
					taskListMap.put("type", taskListLoop.getValue().get(0).get("type"));
					taskListMap.put("updated", taskListLoop.getValue().get(0).get("updated"));
					employeeMap.put("roleId", taskListLoop.getValue().get(0).get("role_id"));
					employeeMap.put("roleName", taskListLoop.getValue().get(0).get("role_name"));
					taskListMap.put("roleId", taskListLoop.getValue().get(0).get("role_id"));
					taskListMap.put("roleName", taskListLoop.getValue().get(0).get("role_name"));
					taskList.add(taskListMap);
				}
				taskMap.put("taskList", taskList);
				mainList.add(taskMap);
			}
			employeeMap.put("project", mainList);
			employeeList.putAll(employeeMap);
		}
		return ResponseEntity.ok(employeeList);
	}

	@GetMapping("/task/employee/role/task/{id}/{roleId}/{taskId}")
	public ResponseEntity<?> getAllEmployeeTaskWithRole(@PathVariable("id") Long employeeId,
			@PathVariable("roleId") Long roleId, @PathVariable("taskId") Long taskId) {
		List<Map<String, Object>> taskDetails = taskrepository.getAllEmployeeTaskWithRole(employeeId, roleId, taskId);
		Map<String, Map<String, Map<String, List<Map<String, Object>>>>> taskGroupMap = taskDetails.stream()
				.collect(Collectors.groupingBy(action -> action.get("id").toString(),
						(Collectors.groupingBy(action -> action.get("task_id").toString(),
								(Collectors.groupingBy(action -> action.get("task_list_id").toString()))))));
		Map<String, Object> employeeList = new HashMap<>();
		for (Entry<String, Map<String, Map<String, List<Map<String, Object>>>>> employeeGroup : taskGroupMap
				.entrySet()) {
			Map<String, Object> employeeMap = new HashMap<>();
			employeeMap.put("employeeId", employeeGroup.getKey());
			Map<String, Object> mainList = new HashMap<>();
			for (Entry<String, Map<String, List<Map<String, Object>>>> taskLoop : employeeGroup.getValue().entrySet()) {
				Map<String, Object> taskMap = new HashMap<>();
				taskMap.put("taskId", taskLoop.getKey());
				List<Map<String, Object>> taskList = new ArrayList<>();
				for (Entry<String, List<Map<String, Object>>> taskListLoop : taskLoop.getValue().entrySet()) {
					Map<String, Object> taskListMap = new HashMap<>();
					 int randomNumber = generateRandomNumber();
					taskListMap.put("taskListId", taskListLoop.getKey());
					taskMap.put("date", taskListLoop.getValue().get(0).get("date"));
					taskMap.put("projectId", taskListLoop.getValue().get(0).get("project_id"));
					taskMap.put("projectName", taskListLoop.getValue().get(0).get("project_name"));
					taskMap.put("projectStatus", taskListLoop.getValue().get(0).get("task_status"));
					taskMap.put("todayDate", taskListLoop.getValue().get(0).get("today_date"));
					taskMap.put("typeOfProject", taskListLoop.getValue().get(0).get("type_of_project"));
					taskListMap.put("cancellationReason", taskListLoop.getValue().get(0).get("cancellation_reason"));
					taskListMap.put("category", taskListLoop.getValue().get(0).get("category"));
					taskListMap.put("comments", taskListLoop.getValue().get(0).get("comments"));
					taskListMap.put("completedDate", taskListLoop.getValue().get(0).get("completed_date"));
					taskListMap.put("created", taskListLoop.getValue().get(0).get("created"));
					taskListMap.put("departmentId", taskListLoop.getValue().get(0).get("department_id"));
					taskListMap.put("departmentName", taskListLoop.getValue().get(0).get("department_name"));
					taskListMap.put("employeeId", taskListLoop.getValue().get(0).get("employee_id"));
					employeeMap.put("employeeName", taskListLoop.getValue().get(0).get("user_name"));
					employeeMap.put("employeeId", taskListLoop.getValue().get(0).get("employee_id"));
					taskListMap.put("employeeName", taskListLoop.getValue().get(0).get("user_name"));
					taskListMap.put("employeeReportId", taskListLoop.getValue().get(0).get("employee_report_id"));
					taskListMap.put("employeeReportName", taskListLoop.getValue().get(0).get("empName"));
					taskListMap.put("holdReason", taskListLoop.getValue().get(0).get("hold_reson"));
					taskListMap.put("label", taskListLoop.getValue().get(0).get("label"));
					taskListMap.put("priority", taskListLoop.getValue().get(0).get("priority"));
					taskListMap.put("projectStatus", taskListLoop.getValue().get(0).get("project_status"));
					taskListMap.put("projectKey", taskListLoop.getValue().get(0).get("projectKey"));
					taskListMap.put("startDate", taskListLoop.getValue().get(0).get("start_date"));
					taskListMap.put("summary", taskListLoop.getValue().get(0).get("summary"));
					taskListMap.put("type", taskListLoop.getValue().get(0).get("type"));
					taskListMap.put("updated", taskListLoop.getValue().get(0).get("updated"));
					employeeMap.put("roleId", taskListLoop.getValue().get(0).get("role_id"));
					employeeMap.put("roleName", taskListLoop.getValue().get(0).get("role_name"));
					taskListMap.put("roleId", taskListLoop.getValue().get(0).get("role_id"));
					taskListMap.put("roleName", taskListLoop.getValue().get(0).get("role_name"));
					if ( taskListLoop.getValue().get(0).get("employee_id") != null) {
					    String fileExtension = getFileExtensionForImage1(taskListLoop);
					    String imageUrl = "profile/" + randomNumber + "/" + taskListLoop.getValue().get(0).get("employee_id") + "." + fileExtension;
					    taskListMap.put("profile", imageUrl);

					} else if ( taskListLoop.getValue().get(0).get("trainee_id") != null) {
					    String imageUrl = "training/" + randomNumber + "/" + taskListLoop.getValue().get(0).get("trainee_id");
					    taskListMap.put("profile", imageUrl);

					}

					taskList.add(taskListMap);
				}
				taskMap.put("taskList", taskList);
				mainList.putAll(taskMap);
			}
			employeeMap.put("project", mainList);
			employeeList.putAll(employeeMap);
		}
		return ResponseEntity.ok(employeeList);
	}

	private String getFileExtensionForImage1(Entry<String, List<Map<String, Object>>> taskListLoop) {
		if (taskListLoop.getValue().get(0) == null || !taskListLoop.getValue().get(0).containsKey("url") || taskListLoop.getValue().get(0).get("url") == null) {
			return "jpg";
		}
		String url = (String) taskListLoop.getValue().get(0).get("url");
		if (url.endsWith(".png")) {
			return "png";
		} else if (url.endsWith(".jpg")) {
			return "jpg";
		} else {
			return "jpg";
		}	
	}

	@GetMapping("/task/employee/task/report/{id}/{roleId}/{taskId}")
	public ResponseEntity<?> getEmployeeTaskReportId(@PathVariable("id") Long employee_id,
			@PathVariable("roleId") Long roleId, @PathVariable("taskId") Long taskId) {
		List<Map<String, Object>> taskDetails = taskrepository.getEmployeeTaskReportId(employee_id, roleId, taskId);
		Map<String, Map<String, Map<String, List<Map<String, Object>>>>> taskGroupMap = taskDetails.stream()
				.collect(Collectors.groupingBy(action -> action.get("employee_report_id").toString(),
						(Collectors.groupingBy(action -> action.get("task_id").toString(),
								(Collectors.groupingBy(action -> action.get("task_list_id").toString()))))));
		Map<String, Object> employeeList = new HashMap<>();
		for (Entry<String, Map<String, Map<String, List<Map<String, Object>>>>> employeeGroup : taskGroupMap
				.entrySet()) {
			Map<String, Object> employeeMap = new HashMap<>();
			employeeMap.put("employeeReportId", employeeGroup.getKey());
			Map<String, Object> mainList = new HashMap<>();
			for (Entry<String, Map<String, List<Map<String, Object>>>> taskLoop : employeeGroup.getValue().entrySet()) {
				Map<String, Object> taskMap = new HashMap<>();
				taskMap.put("taskId", taskLoop.getKey());
				List<Map<String, Object>> taskList = new ArrayList<>();
				for (Entry<String, List<Map<String, Object>>> taskListLoop : taskLoop.getValue().entrySet()) {
					Map<String, Object> taskListMap = new HashMap<>();
					taskListMap.put("taskListId", taskListLoop.getKey());
					taskMap.put("date", taskListLoop.getValue().get(0).get("date"));
					taskMap.put("projectId", taskListLoop.getValue().get(0).get("project_id"));
					taskMap.put("projectName", taskListLoop.getValue().get(0).get("project_name"));
					taskMap.put("projectStatus", taskListLoop.getValue().get(0).get("task_status"));
					taskMap.put("todayDate", taskListLoop.getValue().get(0).get("today_date"));
					taskMap.put("typeOfProject", taskListLoop.getValue().get(0).get("type_of_project"));
					taskListMap.put("cancellationReason", taskListLoop.getValue().get(0).get("cancellation_reason"));
					taskListMap.put("category", taskListLoop.getValue().get(0).get("category"));
					taskListMap.put("comments", taskListLoop.getValue().get(0).get("comments"));
					taskListMap.put("completedDate", taskListLoop.getValue().get(0).get("completed_date"));
					taskListMap.put("created", taskListLoop.getValue().get(0).get("created"));
					taskListMap.put("departmentId", taskListLoop.getValue().get(0).get("department_id"));
					taskListMap.put("departmentName", taskListLoop.getValue().get(0).get("department_name"));
					taskListMap.put("employeeId", taskListLoop.getValue().get(0).get("employee_id"));
					taskListMap.put("employeeName", taskListLoop.getValue().get(0).get("user_name"));
					taskListMap.put("employeeReportId", taskListLoop.getValue().get(0).get("employee_report_id"));
					taskListMap.put("employeeReportName", taskListLoop.getValue().get(0).get("empName"));
					employeeMap.put("employeeReportName", taskListLoop.getValue().get(0).get("empName"));
					taskListMap.put("holdReason", taskListLoop.getValue().get(0).get("hold_reson"));
					taskListMap.put("label", taskListLoop.getValue().get(0).get("label"));
					taskListMap.put("priority", taskListLoop.getValue().get(0).get("priority"));
					taskListMap.put("projectStatus", taskListLoop.getValue().get(0).get("project_status"));
					taskListMap.put("projectKey", taskListLoop.getValue().get(0).get("projectKey"));
					taskListMap.put("startDate", taskListLoop.getValue().get(0).get("start_date"));
					taskListMap.put("summary", taskListLoop.getValue().get(0).get("summary"));
					taskListMap.put("type", taskListLoop.getValue().get(0).get("type"));
					taskListMap.put("updated", taskListLoop.getValue().get(0).get("updated"));
					employeeMap.put("roleId", taskListLoop.getValue().get(0).get("role_id"));
					employeeMap.put("roleName", taskListLoop.getValue().get(0).get("role_name"));
					taskListMap.put("roleId", taskListLoop.getValue().get(0).get("role_id"));
					taskListMap.put("roleName", taskListLoop.getValue().get(0).get("role_name"));
					taskList.add(taskListMap);
				}
				taskMap.put("taskList", taskList);
				mainList.putAll(taskMap);
			}
			employeeMap.put("project", mainList);
			employeeList.putAll(employeeMap);
		}
		return ResponseEntity.ok(employeeList);
	}

	@GetMapping("/employee/card/dashboard/{id}/{roleId}")
	public ResponseEntity<?> getEmployeeCardDashboardDetails(@PathVariable("id") long id,
	        @PathVariable("roleId") long roleId) {
	    Map<String, Object> empDetails = taskrepository.getAllProjectWorktraningWithTraineeEmployee(id, roleId);
	    Map<String, Object> traineeDetails = taskrepository.getAllProjectWorktraningWithTraineeTrainee(id, roleId);

	    if (!traineeDetails.isEmpty() && traineeDetails.containsKey("trainee_id") && traineeDetails.containsKey("role_id")) {
	        return ResponseEntity.ok(traineeDetails);
	    }

	    if (!empDetails.isEmpty() && empDetails.containsKey("employee_id") && empDetails.containsKey("role_id")) {
	        return ResponseEntity.ok(empDetails);
	    } else {
	        String errorMessage = "Employee details not found for the provided ID and Role ID combination";
	        return ResponseEntity.ok(Collections.emptyList());
	    }
	}

	@GetMapping("/projecthead/view")
	public ResponseEntity<?> getAllProjectHeadDetails() {
		List<Map<String, Object>> mainList = new ArrayList<>();
		List<Map<String, Object>> projectHeadDetails = taskrepository.getAllProjectHeadDetails();
		Map<String, List<Map<String, Object>>> taskGroupMap = projectHeadDetails.stream()
				.collect(Collectors.groupingBy(action -> action.get("employee_id").toString()));
		for (Entry<String, List<Map<String, Object>>> projectHeadLoop : taskGroupMap.entrySet()) {
			Map<String, Object> taskMap = new HashMap<>();
			taskMap.put("employeeId", projectHeadLoop.getKey());
			taskMap.put("employeeName", projectHeadLoop.getValue().get(0).get("user_name"));
			taskMap.put("designationId", projectHeadLoop.getValue().get(0).get("designation_id"));
			taskMap.put("designationName", projectHeadLoop.getValue().get(0).get("designation_name"));
			taskMap.put("departmentId", projectHeadLoop.getValue().get(0).get("department_id"));
			taskMap.put("departmentName", projectHeadLoop.getValue().get(0).get("department_name"));
			taskMap.put("roleId", projectHeadLoop.getValue().get(0).get("role_id"));
			taskMap.put("roleName", projectHeadLoop.getValue().get(0).get("role_name"));
			mainList.add(taskMap);
		}
		return ResponseEntity.ok(mainList);
	}

	@GetMapping("/project/team/member/{departmentId}")
	public ResponseEntity<?> getTaskAssignedDetails(@PathVariable("departmentId") Long departmentId) {
		try {
			List<Map<String, Object>> tasks = taskrepository.GetClientProjectWorkingMember(departmentId);
			List<Map<String, Object>> responseList = new ArrayList<>();

			for (Map<String, Object> taskAssigned : tasks) {
				Map<String, Object> taskAssignedMap = new HashMap<>();

				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(taskAssigned);

				if (taskAssigned.containsKey("employee_id") && taskAssigned.get("employee_id") != null) {
					String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employee_id") + "."
							+ fileExtension;
					taskAssignedMap.put("profile", imageUrl);
				} else if (taskAssigned.containsKey("trainee_id") && taskAssigned.get("trainee_id") != null) {
					String imageUrl = "training/" + randomNumber + "/" + taskAssigned.get("trainee_id");
					taskAssignedMap.put("profile", imageUrl);
				}

				taskAssignedMap.putAll(taskAssigned);

				// Create a new project or update an existing one
				String projectName = (String) taskAssigned.get("project_name");
				Map<String, Object> projectMap = responseList.stream()
						.filter(p -> p.containsKey("projectName") && p.get("projectName").equals(projectName))
						.findFirst().orElse(null);

				if (projectMap == null) {
					projectMap = new HashMap<>();
					projectMap.put("projectName", projectName);

					// Use taskAssigned instead of firstClient
					String imageUrl = "clientProfile/" + randomNumber + "/" + taskAssigned.get("client_id");
					projectMap.put("profile", imageUrl);

					projectMap.put("employees", new ArrayList<>());
					responseList.add(projectMap);
				}
				// Add the employee or trainee details to the project
				List<Map<String, Object>> employees = (List<Map<String, Object>>) projectMap.get("employees");
				employees.add(taskAssignedMap);
			}

			return ResponseEntity.ok(responseList);
		} catch (Exception e) {
			String errorMessage = "Error occurred while retrieving task assigned details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
		}
	}

	@GetMapping("/project1/my/task/{id}/{roleID}/{departmentId}")
	public ResponseEntity<?> getProjectWorkingStatus(@PathVariable("id") Long id, @PathVariable("roleID") Long roleID,
			@PathVariable("departmentId") Long departmentId) {
		try {
			List<Map<String, Object>> tasks = taskrepository.GetClientProjectWorkingStatus(id, roleID, departmentId);
			List<Map<String, Object>> responseList = new ArrayList<>();

			for (Map<String, Object> taskAssigned : tasks) {
				Map<String, Object> taskAssignedMap = new HashMap<>();

				generateProfileImage(taskAssigned, taskAssignedMap);

				taskAssignedMap.putAll(taskAssigned);

				// Create a new project or update an existing one
				String projectName = (String) taskAssigned.get("project_name");
				Map<String, Object> projectMap = responseList.stream()
						.filter(p -> p.containsKey("projectName") && p.get("projectName").equals(projectName))
						.findFirst().orElse(null);

				if (projectMap == null) {
					projectMap = new HashMap<>();
					projectMap.put("projectName", projectName);

					// Use taskAssigned instead of firstClient
					projectMap.put("profile", generateClientProfileImage(taskAssigned));

					projectMap.put("employees", new ArrayList<>());
					responseList.add(projectMap);
				}

				// Add the employee or trainee details to the project
				List<Map<String, Object>> employees = (List<Map<String, Object>>) projectMap.get("employees");
				employees.add(taskAssignedMap);
			}

			return ResponseEntity.ok(responseList);
		} catch (Exception e) {
			String errorMessage = "Error occurred while retrieving task assigned details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
		}
	}

	private void generateProfileImage(Map<String, Object> taskAssigned, Map<String, Object> taskAssignedMap) {
		int randomNumber = generateRandomNumber();

		if (taskAssigned.containsKey("employee_id") && taskAssigned.get("employee_id") != null) {
			String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employee_id") + "."
					+ getFileExtensionForImage(taskAssigned);
			taskAssignedMap.put("profile", imageUrl);
		} else if (taskAssigned.containsKey("trainee_id") && taskAssigned.get("trainee_id") != null) {
			String imageUrl = "training/" + randomNumber + "/" + taskAssigned.get("trainee_id");
			taskAssignedMap.put("profile", imageUrl);
		} else {
			// Log information for debugging
			System.out.println("No valid employee_id or trainee_id found in taskAssigned: " + taskAssigned);
		}
	}

	private String generateClientProfileImage(Map<String, Object> taskAssigned) {
		int randomNumber = generateRandomNumber();
		return "clientProfile/" + randomNumber + "/" + taskAssigned.get("client_id");
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

	@GetMapping("/project/my/task/{id}/{roleID}/{departmentId}")
	public ResponseEntity<?> getProjectWorkingStatus1(@PathVariable("id") Long id, @PathVariable("roleID") Long roleID,
			@PathVariable("departmentId") Long departmentId) {
		try {
			List<Map<String, Object>> tasks = taskrepository.GetClientProjectWorkingStatus(id, roleID, departmentId);
			List<Map<String, Object>> taskList = new ArrayList<>();

			for (Map<String, Object> taskAssigned : tasks) {
				Map<String, Object> taskAssignedMap = new HashMap<>();

				int randomNumber = generateRandomNumber();

				String imageUrl = "clientProfile/" + randomNumber + "/" + taskAssigned.get("client_id");
				taskAssignedMap.put("profile", imageUrl);

				taskAssignedMap.putAll(taskAssigned);
				taskList.add(taskAssignedMap);
			}

			return ResponseEntity.ok(taskList);

		} catch (Exception e) {
			String errorMessage = "Error occurred while retrieving task assigned details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
		}
	}

	@GetMapping("/project/head/working/member/{roleId}/{employeeId}")
	public ResponseEntity<?> getTaskAssignedDetailsDDDD(@PathVariable("roleId") Long roleId,
			@PathVariable("employeeId") Long employeeId) {
		try {
			List<Map<String, Object>> tasks = taskrepository.GetProjectHeadWorkingMember(roleId, employeeId);
			List<Map<String, Object>> responseList = new ArrayList<>();

			for (Map<String, Object> taskAssigned : tasks) {
				Map<String, Object> taskAssignedMap = new HashMap<>();

				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(taskAssigned);

				// Debugging statements
				System.out.println("taskAssigned: " + taskAssigned);
				System.out.println("Keys present: " + taskAssigned.keySet());

				if (taskAssigned.containsKey("employee_id") && taskAssigned.get("employee_id") != null) {
					String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employee_id") + "."
							+ fileExtension;
					taskAssignedMap.put("profile", imageUrl);
				} else if (taskAssigned.containsKey("trainee_id") && taskAssigned.get("trainee_id") != null) {
					String imageUrl = "training/" + randomNumber + "/" + taskAssigned.get("trainee_id");
					taskAssignedMap.put("profile", imageUrl);
				}
				taskAssignedMap.putAll(taskAssigned);
				String projectName = (String) taskAssigned.get("project_name");
				
				String projectType= (String) taskAssigned.get("type_of_project");
				Map<String, Object> projectMap = responseList.stream()
						.filter(p -> p.containsKey("projectName") && p.get("projectName").equals(projectName))
						.findFirst().orElse(null);

				if (projectMap == null) {
					projectMap = new HashMap<>();
					projectMap.put("projectName", projectName);
					projectMap.put("typeOfProject", projectType);

					projectMap.put("employees", new ArrayList<>());
					responseList.add(projectMap);
				}
				// Add the employee or trainee details to the project
				List<Map<String, Object>> employees = (List<Map<String, Object>>) projectMap.get("employees");
				employees.add(taskAssignedMap);
			}

			return ResponseEntity.ok(responseList);
		} catch (Exception e) {
			String errorMessage = "Error occurred while retrieving task assigned details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
		}
	}

	@GetMapping("/project/head1/working/task/priority/{roleId}/{employeeId}")
	public ResponseEntity<?> getTaskAssignedprioritytask(@PathVariable("roleId") Long roleId,
			@PathVariable("employeeId") Long employeeId) {
		try {
			List<Map<String, Object>> tasks = taskrepository.GetProjectpriority(roleId, employeeId);
			List<Map<String, Object>> responseList = new ArrayList<>();

			for (Map<String, Object> taskAssigned : tasks) {
				Map<String, Object> taskAssignedMap = new HashMap<>();

				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(taskAssigned);

				// Debugging statements
				System.out.println("taskAssigned: " + taskAssigned);
				System.out.println("Keys present: " + taskAssigned.keySet());

				if (taskAssigned.containsKey("employee_id") && taskAssigned.get("employee_id") != null) {
					String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employee_id") + "."
							+ fileExtension;
					taskAssignedMap.put("profile", imageUrl);
				} else if (taskAssigned.containsKey("trainee_id") && taskAssigned.get("trainee_id") != null) {
					String imageUrl = "training/" + randomNumber + "/" + taskAssigned.get("trainee_id");
					taskAssignedMap.put("profile", imageUrl);
				}
				taskAssignedMap.putAll(taskAssigned);
				String projectName = (String) taskAssigned.get("project_name");
				Map<String, Object> projectMap = responseList.stream()
						.filter(p -> p.containsKey("projectName") && p.get("projectName").equals(projectName))
						.findFirst().orElse(null);

				if (projectMap == null) {
					projectMap = new HashMap<>();
					projectMap.put("projectName", projectName);

					projectMap.put("employees", new ArrayList<>());
					responseList.add(projectMap);
				}
				// Add the employee or trainee details to the project
				List<Map<String, Object>> employees = (List<Map<String, Object>>) projectMap.get("employees");
				employees.add(taskAssignedMap);
			}

			return ResponseEntity.ok(responseList);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Error occurred while retrieving task assigned details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
		}
	}
	
	
//	@GetMapping("/project/head/working/task/priority/{roleId}/{employeeId}")
//    public List<Map<String, Object>> getAllAttendanceDetails(
//            @PathVariable("roleId") Long roleId,
//            @PathVariable("employeeId") Long employeeId) {
//        return taskrepository.GetProjectpriority(roleId, employeeId);
//    }
	
	@GetMapping("/project/head/working/task/priority/{roleId}/{employeeId}")
	    public ResponseEntity<?> getTaskAssignedToProjectHeadINTask(
	            @PathVariable("roleId") Long roleId,
	            @PathVariable("employeeId") Long employeeId) {
	        try {
	        
	                List<Map<String, Object>> tasks = taskrepository.GetProjectpriority(roleId, employeeId);
	                List<Map<String, Object>> taskList = new ArrayList<>();

	                for (Map<String, Object> taskAssigned : tasks) {
	                    Map<String, Object> taskAssignedMap = new HashMap<>();
	                    int randomNumber = generateRandomNumber();

	                    if (taskAssigned.containsKey("employee_id") && taskAssigned.get("employee_id") != null) {
	                        String fileExtension = getFileExtensionForImage(taskAssigned);
	                        String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employee_id") + "." + fileExtension;
	                        taskAssignedMap.put("profile", imageUrl);
	                    } 
	                    
	                    else if (taskAssigned.containsKey("trainee_id") && taskAssigned.get("trainee_id") != null) {
	                        String imageUrl = "training/" + randomNumber + "/" + taskAssigned.get("trainee_id");
	                        taskAssignedMap.put("profile", imageUrl);
	                    }

	                    taskAssignedMap.putAll(taskAssigned);
	                    taskList.add(taskAssignedMap);
	                }
	                return ResponseEntity.ok(taskList);
	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	            String errorMessage = "Error occurred while retrieving task assigned details";
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(Collections.singletonMap("error", errorMessage));
	        }
	        
	        
	    }
	
	
	@GetMapping("/project/head/working/leavelist/{roleId}/{employeeId}")
    public ResponseEntity<?> getTaskAssignedToProjectHeadINTaskLeavelist(
            @PathVariable("roleId") Long roleId,
            @PathVariable("employeeId") Long employeeId) {
        try {
        
                List<Map<String, Object>> tasks = taskrepository.GetProjectYearLeaveList(roleId, employeeId);
                List<Map<String, Object>> taskList = new ArrayList<>();

                for (Map<String, Object> taskAssigned : tasks) {
                    Map<String, Object> taskAssignedMap = new HashMap<>();
                    int randomNumber = generateRandomNumber();

                    if (taskAssigned.containsKey("employee_id") && taskAssigned.get("employee_id") != null) {
                        String fileExtension = getFileExtensionForImage(taskAssigned);
                        String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employee_id") + "." + fileExtension;
                        taskAssignedMap.put("profile", imageUrl);
                    } 
                    
                    else if (taskAssigned.containsKey("trainee_id") && taskAssigned.get("trainee_id") != null) {
                        String imageUrl = "training/" + randomNumber + "/" + taskAssigned.get("trainee_id");
                        taskAssignedMap.put("profile", imageUrl);
                    }

                    taskAssignedMap.putAll(taskAssigned);
                    taskList.add(taskAssignedMap);
                }
                return ResponseEntity.ok(taskList);
            
        } catch (Exception e) {
        	e.printStackTrace();
            String errorMessage = "Error occurred while retrieving task assigned details";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", errorMessage));
        }
        
        
    }
	
	
	@GetMapping("/task/enddate/{department_id}")
    public ResponseEntity<?> getTaskAssignedToProjectHeadINTaskLeavelisttaskenddate(
            @PathVariable("department_id") Long department_id) {
        try {
        
                List<Map<String, Object>> tasks = taskrepository.GetProjectYearLeaveListDepartment( department_id);
                List<Map<String, Object>> taskList = new ArrayList<>();

                for (Map<String, Object> taskAssigned : tasks) {
                    Map<String, Object> taskAssignedMap = new HashMap<>();
                    int randomNumber = generateRandomNumber();

                    if (taskAssigned.containsKey("employee_id") && taskAssigned.get("employee_id") != null) {
                        String fileExtension = getFileExtensionForImage(taskAssigned);
                        String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employee_id") + "." + fileExtension;
                        taskAssignedMap.put("profile", imageUrl);
                    } 
                    
                    else if (taskAssigned.containsKey("trainee_id") && taskAssigned.get("trainee_id") != null) {
                        String imageUrl = "training/" + randomNumber + "/" + taskAssigned.get("trainee_id");
                        taskAssignedMap.put("profile", imageUrl);
                    }

                    taskAssignedMap.putAll(taskAssigned);
                    taskList.add(taskAssignedMap);
                }
                return ResponseEntity.ok(taskList);
            
        } catch (Exception e) {
        	e.printStackTrace();
            String errorMessage = "Error occurred while retrieving task assigned details";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", errorMessage));
        }
        
        
    }
	
	@GetMapping("/project/employees/{employeeId}")
    public ResponseEntity<?> getTaskAssignedToProjectHeadINTaskLeavelisttaskenddateList(
            @PathVariable("employeeId") Long employeeId) {
        try {
        
                List<Map<String, Object>> tasks = taskrepository.attendanceForEmployeeIdDD( employeeId);
                List<Map<String, Object>> taskList = new ArrayList<>();

                for (Map<String, Object> taskAssigned : tasks) {
                    Map<String, Object> taskAssignedMap = new HashMap<>();
                    int randomNumber = generateRandomNumber();

                    if (taskAssigned.containsKey("employee_report_id") && taskAssigned.get("employee_report_id") != null) {
                        String fileExtension = getFileExtensionForImage(taskAssigned);
                        String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employee_report_id") + "." + fileExtension;
                        taskAssignedMap.put("profile", imageUrl);
                    } 
                    
//                    else if (taskAssigned.containsKey("trainee_id") && taskAssigned.get("trainee_id") != null) {
//                        String imageUrl = "training/" + randomNumber + "/" + taskAssigned.get("trainee_id");
//                        taskAssignedMap.put("profile", imageUrl);
//                    }

                    taskAssignedMap.putAll(taskAssigned);
                    taskList.add(taskAssignedMap);
                }
                return ResponseEntity.ok(taskList);
            
        } catch (Exception e) {
        	e.printStackTrace();
            String errorMessage = "Error occurred while retrieving task assigned details";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", errorMessage));
        }
        
        
    }
	@GetMapping("/project/head/count/{roleId}/{employeeId}")
    public Map<String, Object> getAllAttendanceDetailsproject(
            @PathVariable("roleId") Long roleId,
            @PathVariable("employeeId") Long employeeId) {
        return taskrepository.ALLCountprojectCount(roleId, employeeId);
    }
	
	@GetMapping("/project/head/year/{roleId}/{employeeId}")
    public List<Map<String, Object>> getAllAttendanceDetailsprojectyear(
            @PathVariable("roleId") Long roleId,
            @PathVariable("employeeId") Long employeeId) {
        return taskrepository.GetProjectYearVize(roleId, employeeId);
    }
	
//	@GetMapping("/project/employees/{employeeId}")
//	public List<Map<String, Object>> getAllAttendanceDetails(@PathVariable Long employeeId) {
//	    return taskrepository.attendanceForEmployeeIdDD(employeeId);
//	}
	@GetMapping("/task/working/{id}/{roleId}")
	public ResponseEntity<Object> getHrInterviewDetailsWithId(
	        @PathVariable("id") long id,
	        @PathVariable("roleId") long roleId
	) {
	    try {
	        List<Map<String, Object>> training = taskrepository.taskfortrainee(id, roleId);
	        List<Map<String, Object>> emp = taskrepository.taskforEmployeeId1(id, roleId);
	        if (!training.isEmpty() && training.get(0).get("trainee_id") != null && training.get(0).get("role_id") != null) {
	            return ResponseEntity.ok(training);
	        } else if (!emp.isEmpty() && emp.get(0).get("employee_id") != null && emp.get(0).get("role_id") != null) {
	            return ResponseEntity.ok(emp);
	        } else {
	            System.out.println("No records found for ID: " + id + " and Role ID: " + roleId);

	            String errorMessage = "The Id does not exist";
	            return ResponseEntity.ok(Collections.emptyList());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        String errorMessage = "Error occurred while retrieving task assigned details";
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Collections.singletonMap("error", errorMessage));
	    }
	}
	
	
	@GetMapping("/project/member")
	public ResponseEntity<?> getTaskAssignedDetailsEEE() {
		try {
			List<Map<String, Object>> tasks = taskrepository.GetProjectHeadWorkingMemberproject( );
			List<Map<String, Object>> responseList = new ArrayList<>();

			for (Map<String, Object> taskAssigned : tasks) {
				Map<String, Object> taskAssignedMap = new HashMap<>();

				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(taskAssigned);

				// Debugging statements
				System.out.println("taskAssigned: " + taskAssigned);
				System.out.println("Keys present: " + taskAssigned.keySet());

				if (taskAssigned.containsKey("employee_id") && taskAssigned.get("employee_id") != null) {
					String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employee_id") + "."
							+ fileExtension;
					taskAssignedMap.put("profile", imageUrl);
				} else if (taskAssigned.containsKey("trainee_id") && taskAssigned.get("trainee_id") != null) {
					String imageUrl = "training/" + randomNumber + "/" + taskAssigned.get("trainee_id");
					taskAssignedMap.put("profile", imageUrl);
				}
				taskAssignedMap.putAll(taskAssigned);
				String projectName = (String) taskAssigned.get("project_name");
				
				String projectType= (String) taskAssigned.get("type_of_project");
				Map<String, Object> projectMap = responseList.stream()
						.filter(p -> p.containsKey("projectName") && p.get("projectName").equals(projectName))
						.findFirst().orElse(null);

				if (projectMap == null) {
					projectMap = new HashMap<>();
					projectMap.put("projectName", projectName);
					projectMap.put("typeOfProject", projectType);

					projectMap.put("employees", new ArrayList<>());
					responseList.add(projectMap);
				}
				// Add the employee or trainee details to the project
				List<Map<String, Object>> employees = (List<Map<String, Object>>) projectMap.get("employees");
				employees.add(taskAssignedMap);
			}

			return ResponseEntity.ok(responseList);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Error occurred while retrieving task assigned details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
		}
	}


}
