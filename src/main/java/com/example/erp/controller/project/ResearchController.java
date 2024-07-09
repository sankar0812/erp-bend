package com.example.erp.controller.project;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

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

import com.example.erp.entity.clientDetails.ClientRequirement;
import com.example.erp.entity.project.Research;
import com.example.erp.entity.project.Task;
import com.example.erp.entity.project.TaskList;
import com.example.erp.repository.project.ResearchRepository;
import com.example.erp.service.clientDetails.ClientRequirementService;
import com.example.erp.service.project.ResearchService;
import com.example.erp.service.project.TaskService;

@RestController
@CrossOrigin
public class ResearchController {

	@Autowired
	private ResearchService researchService;

	@Autowired
	private ResearchRepository researchrepository;

	@Autowired
	private TaskService taskService;

	@Autowired
	private ClientRequirementService clientRequirementService;
	
	
	
	@PutMapping("/research/edit/{id}")
	public ResponseEntity<Research> updateResearch(@PathVariable("id") Long researchId,
			@RequestBody Research research) {
		try {
			Research existingResearch = researchService.findResearchById(researchId);

			if (existingResearch == null) {
				return ResponseEntity.notFound().build();
			}
			existingResearch.setDate(research.getDate());
			existingResearch.setDepartmentId(research.getDepartmentId());
			existingResearch.setEmployeeId(research.getEmployeeId());
			existingResearch.setTypeOfProject(research.getTypeOfProject());
			existingResearch.setProjectId(research.getProjectId());
			existingResearch.setAccepted(research.isAccepted());
			existingResearch.setProjectStatus(research.getProjectStatus());
			existingResearch.setRejected(research.isRejected());
			existingResearch.setRoleId(7);

			researchService.SaveResearchDetails(existingResearch);
			return ResponseEntity.ok(existingResearch);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/research/details/edit/{id}")
	public ResponseEntity<?> updateClientFormForProject(@PathVariable("id") long researchId,
			@RequestBody Research research) {
		try {
			Research existingresearch = researchService.findResearchById(researchId);
			if (existingresearch == null) {
				return ResponseEntity.notFound().build();
			}
			if (existingresearch.isAccepted()) {
				String errormessage = "A project is moved to research first step task";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errormessage);
			}

			if (existingresearch.isRejected()) {
				String errormessage = "A project of research is rejected";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errormessage);
			}

			existingresearch.setProjectStatus(research.getProjectStatus());
			if (research.getProjectStatus().equals("approved")) {
				existingresearch.setAccepted(true);
			} else if (research.getProjectStatus().equals("rejected")) {
				existingresearch.setRejected(true);
			} else {
				existingresearch.setAccepted(false);
				existingresearch.setRejected(false);
			}

			researchService.SaveResearchDetails(existingresearch);

			if (existingresearch.getProjectStatus().equals("approved")) {
				long projectId = existingresearch.getProjectId();
				Date date = existingresearch.getDate();
				long clientId = existingresearch.getClientId();
				long employeeId = existingresearch.getEmployeeId();
               String ERT=existingresearch.getTypeOfProject();
				long departmentId = existingresearch.getDepartmentId();

				ClientRequirement requirement = clientRequirementService.getById(projectId);
				 String nameConverter=requirement.getProjectName();
						
				
				Task task = new Task();
				List<TaskList> taskList = new ArrayList<>();
				TaskList taskLoop = new TaskList();
				taskList.add(taskLoop);
				
				for (TaskList taskListItem : taskList) {
					taskListItem.setEmployeeReportId(employeeId);
					taskListItem.setDepartmentId(departmentId);
					taskListItem.setProjectStatus("pending");
					taskListItem.setPending(true);
					long id = nextId++;
			    	String com = nameConverter.substring(0, 3).toUpperCase();
			    	String rr = ERT.substring(0, 2).toUpperCase();

			    	String projectKey = String.format("%s(%s)%s", com, rr, id);			
			    	taskLoop.setProjectkey(projectKey);
				}
			
				task.setEmployeeReportId(employeeId);
				task.setProjectStatus("pending");
				task.setTaskList(taskList);
				task.setProjectId(projectId);
				task.setDate(new Date(System.currentTimeMillis()));
				task.setClientId(clientId);
				task.setTypeOfProject("research");
				taskService.SaveTaskDetails(task);
			}

			return ResponseEntity.ok(existingresearch);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	private static long nextId = 1;
	@DeleteMapping("/research/delete/{id}")
	public ResponseEntity<String> deleteResearchId(@PathVariable("id") Long researchId) {
		researchService.deleteResearchById(researchId);
		return ResponseEntity.ok("Research deleted successfully With Id :" + researchId);

	}

	@GetMapping("/research/view")
	public ResponseEntity<?> getTaskAssignedDetailsp(@RequestParam(required = true) String view) {
		try {
			if ("research".equals(view)) {
				return ResponseEntity.ok(researchrepository.getAllprojectresearch());
			} else {
				String errorMessage = "Invalid value for 'view'. Expected 'research'.";
				return ResponseEntity.badRequest().body(Collections.singletonMap("error", errorMessage));
			}
		} catch (Exception e) {
			String errorMessage = "Error occurred while retrieving Research assigned details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
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
		} else {
			return "jpg";
		}
	}
	
	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000); // Change the upper limit if needed
	}
	
	@GetMapping("/research/projectmanager/dashboard")
	public ResponseEntity<?> getTaskAssignedToProjectHead(@RequestParam(required = true) String dashboard) {
	    try {
	        if ("projectmanager".equals(dashboard)) {
	            List<Map<String, Object>> tasks = researchrepository.ProjectManagerShowingprojecthead();
	            List<Map<String, Object>> taskList = new ArrayList<>();

	            for (Map<String, Object> taskAssigned : tasks) {
	                Map<String, Object> taskAssignedMap = new HashMap<>();
	                String fileExtension = getFileExtensionForImage(taskAssigned);
	                int randomNumber = generateRandomNumber();
	                String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employee_report_id") + "." + fileExtension;
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

	


	
	@GetMapping("/research/project/dashboard/previous")
	public List<Map<String, Object>> allDeptDetails(@RequestParam(required = true) String dashboard) {
	    try {
	        if ("previous".equals(dashboard)) {
	            return researchrepository.ALLCountproject();
	        } else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();
	    }
	}
	
    @GetMapping("/research/project/dashboard/priority/count")
    public ResponseEntity<?> getTaskAssignedToProjectHeadINTask(@RequestParam(required = true) String dashboard) {
        try {
            if ("priority".equals(dashboard)) {
                List<Map<String, Object>> tasks = researchrepository.AllHighPriorityCountINtask();
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
            } else {
                String errorMessage = "Invalid value for 'dashboard'. Expected 'priority'.";
                return ResponseEntity.badRequest().body(Collections.singletonMap("error", errorMessage));
            }
        } catch (Exception e) {
        	e.printStackTrace();
            String errorMessage = "Error occurred while retrieving task assigned details";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", errorMessage));
        }
        
        
    }
    
    
	@GetMapping("/project/dashboard/enddate")
	public List<Map<String, Object>> allDeptDetailshhhhhhhhhhh(@RequestParam(required = true) String dashboard) {
	    try {
	        if ("enddate".equals(dashboard)) {
	            return researchrepository.ALLCountprojectEnddate();
	        } else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();	        
	      
	    }
	}
	
	@GetMapping("/newproject/dashboard/year")
	public List<Map<String, Object>> allDeptDetailshalbin(@RequestParam(required = true) String dashboard) {
	    try {
	        if ("newproject".equals(dashboard)) {
	            return researchrepository.GetAllNewProjectandcompletedPeoject();
	        } else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();	        
	      
	    }
	}
	
	  @GetMapping("/projectmanager/count/dashboard")
	    public Map<String, Object> allDeptDetailshalbinCount(@RequestParam(required = true) String dashboard) {
	        Map<String, Object> resultMap = new HashMap<>();
	        try {
	            if ("projectmanager".equals(dashboard)) {
	                resultMap = researchrepository.GetAllNewProjectandcompletedPeojectcount();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();	           
	        }
	        return resultMap;
	    }
}
