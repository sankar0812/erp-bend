package com.example.erp.controller.clientDetails;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
import com.example.erp.entity.project.Research;
import com.example.erp.repository.clientDetails.ClientRequirementRepository;
import com.example.erp.service.clientDetails.ClientRequirementService;
import com.example.erp.service.project.ResearchService;
import java.util.Collections;

@RestController
@CrossOrigin
public class ClientRequirementController {
	@Autowired
	private ClientRequirementService clientRequirementService;

	@Autowired
	private ClientRequirementRepository clientRequirementRepository;

	@Autowired
	private ResearchService researchService;


	@PostMapping("/clientRequirement/save")
	public ResponseEntity<String> saveClientRequirement(@RequestBody ClientRequirement clientRequirement) {
		try {
			clientRequirement.setProjectStatus("pending");
			
		
			int duration = clientRequirement.getDuration();
			
		
			Date clientdateutil = clientRequirement.getDate();
			Date clientSql = convertUtilDateToSqlDate(clientdateutil);
			Date endDate = calculateVacateDate(clientSql, duration);
			clientRequirement.setDurationdate(endDate);
		
			clientRequirementService.SaveClientRequirmentDetails(clientRequirement);

			long projectId = clientRequirement.getProjectId();
			return ResponseEntity.ok("ClientRequirement saved successfully. Project ID: " + projectId);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving ClientRequirement: " + e.getMessage());
		}
	}
	
	

	private Date convertUtilDateToSqlDate(Date utilDate) {
		return new java.sql.Date(utilDate.getTime());
	}
	private Date calculateVacateDate(Date bookingDate, int noOfDays) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(bookingDate);
		calendar.add(Calendar.DAY_OF_MONTH, noOfDays);
		return new Date(calendar.getTimeInMillis());
	}

	@PutMapping("/clientRequirement/edit/{id}")
	public ResponseEntity<ClientRequirement> updateClientRequirement(@PathVariable("id") long id,
			@RequestBody ClientRequirement clientRequirement) {
		try {
			ClientRequirement existingClientRequirement = clientRequirementService.getById(id);
			if (existingClientRequirement == null) {
				return ResponseEntity.notFound().build();
			}
			existingClientRequirement.setFeatures(clientRequirement.getFeatures());
			existingClientRequirement.setProjectName(clientRequirement.getProjectName());
			existingClientRequirement.setSkillsAndDescription(clientRequirement.getSkillsAndDescription());
			existingClientRequirement.setDuration(clientRequirement.getDuration());
			existingClientRequirement.setProjectTypeId(clientRequirement.getProjectTypeId());
			int duration = clientRequirement.getDuration();
			
			
			Date clientdateutil = existingClientRequirement.getDate();
			Date clientSql = convertUtilDateToSqlDate(clientdateutil);
			Date endDate = calculateVacateDate(clientSql, duration);
			existingClientRequirement.setDurationdate(endDate);
			clientRequirementService.SaveClientRequirmentDetails(existingClientRequirement);
			return ResponseEntity.ok(existingClientRequirement);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/clientRequirement/details/edit/{id}")
	public ResponseEntity<?> updateClientFormForProject(@PathVariable("id") long id,
			@RequestBody ClientRequirement clientRequirement) {
		try {
			ClientRequirement existingClientRequirement = clientRequirementService.getById(id);
			if (existingClientRequirement == null) {
				return ResponseEntity.notFound().build();
			}
//			if (existingClientRequirement.isStatus()) {
//				String errorMessage = "A Project is Moved to Research And Development";
//				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
//			}
//
//			if (existingClientRequirement.isRejected()) {
//				String errorMessage = "A Project is Rejected";
//				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
//			}

			existingClientRequirement.setProjectStatus(clientRequirement.getProjectStatus());
			if (clientRequirement.getProjectStatus().equals("approved")) {
				existingClientRequirement.setStatus(true);
			} else if (clientRequirement.getProjectStatus().equals("rejected")) {
				existingClientRequirement.setRejected(true);
			} else {
				existingClientRequirement.setStatus(false);
				existingClientRequirement.setRejected(false);
			}

			clientRequirementService.SaveClientRequirmentDetails(existingClientRequirement);

			if (existingClientRequirement.getProjectStatus().equals("approved")) {
				long projectId = existingClientRequirement.getProjectId();
				Date date = existingClientRequirement.getDate();
				long clientId = existingClientRequirement.getClientId();
				Research research = new Research();

				research.setProjectId(projectId);
				research.setDate(date);
				research.setClientId(clientId);
				research.setTypeOfProject("research");
				research.setRoleId(7);
				researchService.SaveResearchDetails(research);
			}

			return ResponseEntity.ok(existingClientRequirement);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/clientRequirement/delete/{id}")
	public ResponseEntity<String> deleteClientProject(@PathVariable("id") Long id) {
		clientRequirementService.deleteClientRequirmentId(id);
		return ResponseEntity.ok("ClientProject details deleted successfully");
	}

	//////////////total project for one client/////////
	@GetMapping("/client/project")
	public ResponseEntity<Object> getAllDetails(@RequestParam(required = true) String project) {
		if ("client".equals(project)) {
			List<Map<String, Object>> departmentList = new ArrayList<>();
			List<Map<String, Object>> departmentRole = clientRequirementRepository.getAllRoleByEmployees();
			Map<String, List<Map<String, Object>>> departmentGroupMap = departmentRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("client_id").toString()));

			for (Entry<String, List<Map<String, Object>>> departmentLoop : departmentGroupMap.entrySet()) {
				Map<String, Object> departmentMap = new HashMap<>();
				departmentMap.put("clientId", Long.parseLong(departmentLoop.getKey()));
				departmentMap.put("clientName", departmentLoop.getValue().get(0).get("client_name"));
				List<Map<String, Object>> departmentSubList = new ArrayList<>();
				for (Map<String, Object> departmentsubLoop : departmentLoop.getValue()) {

					Map<String, Object> departmentSubMap = new HashMap<>();
					departmentSubMap.put("projectId", departmentsubLoop.get("project_id"));
					departmentSubMap.put("projectName", departmentsubLoop.get("project_name"));

					departmentSubList.add(departmentSubMap);
				}
				departmentMap.put("projectDetails", departmentSubList);
				departmentList.add(departmentMap);
			}

			return ResponseEntity.ok(departmentList);
		} else {
			String errorMessage = "Invalid value for 'department'. Expected 'Department'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@GetMapping("/clientRequirement/view")
	public ResponseEntity<Object> getClientRequirement(@RequestParam(required = true) String clientRequirement) {
		if ("clientRequirementDetails".equals(clientRequirement)) {
			List<Map<String, Object>> departmentList = new ArrayList<>();
			List<Map<String, Object>> departmentRole = clientRequirementRepository.getAllDetails();
			Map<String, List<Map<String, Object>>> departmentGroupMap = departmentRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("project_id").toString()));

			for (Entry<String, List<Map<String, Object>>> departmentLoop : departmentGroupMap.entrySet()) {
				Map<String, Object> departmentMap = new HashMap<>();
				departmentMap.put("projectId", Long.parseLong(departmentLoop.getKey()));
				departmentMap.put("clientName", departmentLoop.getValue().get(0).get("client_name"));
				departmentMap.put("clientId", departmentLoop.getValue().get(0).get("client_id"));
				departmentMap.put("date", departmentLoop.getValue().get(0).get("date"));
				departmentMap.put("duration", departmentLoop.getValue().get(0).get("duration"));
				departmentMap.put("features", departmentLoop.getValue().get(0).get("features"));
				departmentMap.put("projectName", departmentLoop.getValue().get(0).get("project_name"));
				departmentMap.put("projectStatus", departmentLoop.getValue().get(0).get("project_status"));
				departmentMap.put("projectTypeId", departmentLoop.getValue().get(0).get("project_type_id"));
				departmentMap.put("skillsAndDescription", departmentLoop.getValue().get(0).get("skills_and_description"));
				departmentMap.put("projectType", departmentLoop.getValue().get(0).get("project_type"));
				departmentMap.put("roleType", departmentLoop.getValue().get(0).get("role_type"));
				departmentList.add(departmentMap);
			}
			Collections.reverse(departmentList);
			return ResponseEntity.ok(departmentList);
		} else {
			String errorMessage = "Invalid value for 'clientRequirement'. Expected 'clientRequirementDetails'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}
	
	@GetMapping("/Requirement/view/{id}")
	public ResponseEntity<Object> getClientRequirementView(@PathVariable("id") long id) {
		
			List<Map<String, Object>> departmentList = new ArrayList<>();
			List<Map<String, Object>> departmentRole = clientRequirementRepository.getAllDetailsClient(id);
			Map<String, List<Map<String, Object>>> departmentGroupMap = departmentRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("project_id").toString()));

			for (Entry<String, List<Map<String, Object>>> departmentLoop : departmentGroupMap.entrySet()) {
				Map<String, Object> departmentMap = new HashMap<>();
				departmentMap.put("projectId", Long.parseLong(departmentLoop.getKey()));
				departmentMap.put("clientName", departmentLoop.getValue().get(0).get("client_name"));
				departmentMap.put("clientId", departmentLoop.getValue().get(0).get("client_id"));
				departmentMap.put("date", departmentLoop.getValue().get(0).get("date"));
				departmentMap.put("duration", departmentLoop.getValue().get(0).get("duration"));
				departmentMap.put("features", departmentLoop.getValue().get(0).get("features"));
				departmentMap.put("projectName", departmentLoop.getValue().get(0).get("project_name"));
				departmentMap.put("projectStatus", departmentLoop.getValue().get(0).get("project_status"));
				departmentMap.put("projectTypeId", departmentLoop.getValue().get(0).get("project_type_id"));
				departmentMap.put("skillsAndDescription", departmentLoop.getValue().get(0).get("skills_and_description"));
				departmentMap.put("projectType", departmentLoop.getValue().get(0).get("project_type"));
				departmentMap.put("roleType", departmentLoop.getValue().get(0).get("role_type"));
				departmentList.add(departmentMap);
			}

			return ResponseEntity.ok(departmentList);
		
	}
	
	 @PostMapping("/clientRequirement/manager")
	 public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAnd(
	         @RequestBody Map<String, Object> requestBody) {

	     if (!requestBody.containsKey("choose")) {
	         return ResponseEntity.badRequest().build();
	     }

	     String choose = requestBody.get("choose").toString();

	     switch (choose) {
	         
	         case "currentday":
	             return handleGstScenario(requestBody);

	         case "year":
	             return handleMonthScenario(requestBody);

	         default:
	             return ResponseEntity.badRequest().build();
	     }
	 }



	 private ResponseEntity<List<Map<String, Object>>> handleGstScenario(Map<String, Object> requestBody) {
	     if (requestBody.containsKey("data")) {
	         String gstData = requestBody.get("data").toString();

	         switch (gstData) {
	             case "approved":
	                 return handleWithTaxScenario();
	             case "pending":
	                 return handleWithoutTaxScenario1();
	             case "rejected":
	                 return handleWithoutTaxScenario();
	             default:
	                 return ResponseEntity.badRequest().build();
	         }
	     }
	     return ResponseEntity.badRequest().build();
	 }

	 private ResponseEntity<List<Map<String, Object>>> handleWithTaxScenario() {
	     List<Map<String, Object>> leaveData = clientRequirementRepository.Allfilterpresentlist();
	     return processResponse(leaveData);
	 }

	 private ResponseEntity<List<Map<String, Object>>> handleWithoutTaxScenario1() {
	     List<Map<String, Object>> leaveData = clientRequirementRepository.Allfilter3absentlist();
	     return processResponse(leaveData);
	 }
	 
	 private ResponseEntity<List<Map<String, Object>>> handleWithoutTaxScenario() {
	     List<Map<String, Object>> leaveData = clientRequirementRepository.Allfilter3absentlist33();
	     return processResponse(leaveData);
	 }

	 private ResponseEntity<List<Map<String, Object>>> handleMonthScenario(Map<String, Object> requestBody) {
		 if (requestBody.containsKey("monthName") && requestBody.containsKey("year")) {
 
	         String monthName = requestBody.get("monthName").toString();
	         String year = requestBody.get("year").toString();
	         List<Map<String, Object>> leaveData1 = clientRequirementRepository.getAllattendance(monthName,year);
	         return processResponse(leaveData1);
	     }
	     return ResponseEntity.badRequest().build();
	 }

	

	 private ResponseEntity<List<Map<String, Object>>> processResponse(List<Map<String, Object>> leaveData) {
	     List<Map<String, Object>> imageResponses = new ArrayList<>();
	     for (Map<String, Object> image : leaveData) {
	    	 int randomNumber = generateRandomNumber();
         
         	String imageUrl1 = "clientProfile/" + randomNumber + "/" + image.get("client_id");
	         Map<String, Object> imageResponse = new HashMap<>();
	         imageResponse.put("profile", imageUrl1);
	         imageResponse.putAll(image);
	         imageResponses.add(imageResponse);
	     }
	     return ResponseEntity.ok(imageResponses);
	 }

		private int generateRandomNumber() {
			Random random = new Random();
			return random.nextInt(1000000); // Change the upper limit if needed
		}
}
