package com.example.erp.controller.eRecruitment;

import java.sql.Date;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
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
import com.example.erp.entity.erecruitment.Hiring;
import com.example.erp.repository.erecruitment.HiringRepository;
import com.example.erp.service.eRecruitment.HiringService;


@RestController
@CrossOrigin
public class HiringController {
	@Autowired
	private HiringService service;
	@Autowired
	private HiringRepository repo;

	@GetMapping("/hiring")
	public ResponseEntity<?> getHirings(@RequestParam(required = true) String Hiring) {
		try {
			if ("hiring".equals(Hiring)) {
				List<Hiring> Hirings = service.listAll();
				return ResponseEntity.ok(Hirings);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided leave is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving Hirings: " + e.getMessage());
		}
	}

	@PostMapping("/hiring/save")
	public ResponseEntity<String> saveHiring(@RequestBody Hiring hiring) {
		try {
			Date podate = hiring.getPosted();
			Date codate = hiring.getClosing();
			long departmentId = hiring.getDepartmentId();
			String position =hiring.getPosition();
			Optional<Hiring> existingHiring = repo.findByDepartmentIdAndPositionAndPostedAndClosing(departmentId,position, podate, codate);


             if (existingHiring.isPresent()) {
                 return ResponseEntity.badRequest().body("hiring with ID " + departmentId + " already exists.");
             }
         
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);

			java.util.Date currentDateMinusOneUtil = calendar.getTime();
			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());

			if (hiring.getCurrentdate() != null && hiring.getCurrentdate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			if (hiring.getPosted() != null && hiring.getPosted().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			
			if (hiring.getPosted() != null && hiring.getClosing() != null
					&& hiring.getPosted().after(hiring.getClosing())) {
				String errorMessage = "PostedDate cannot be later than ClosingDate.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			hiring.setCompanyId(1);
			hiring.setStatus(true);
			service.saveOrUpdate(hiring);

			return ResponseEntity.ok("Hiring saved with id: " + hiring.getHiringId());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving Hiring: " + e.getMessage());
		}
	}

	@PutMapping("/hiring/status/{id}")
	public ResponseEntity<?> getHiringById(@PathVariable(name = "id") long id) {
		try {
			Hiring hiring = service.getById(id);
			if (hiring != null) {
				boolean currentStatus = hiring.isStatus();
				hiring.setStatus(!currentStatus);
				service.saveOrUpdate(hiring); // Save the updated complaints
			} else {
				return ResponseEntity.ok(false); // Complaints with the given ID does not exist, return false
			}

			return ResponseEntity.ok(hiring.isStatus()); // Return the new status (true or false)
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false); // Set response to false in case
																						// of an error
		}
	}

	@PutMapping("/hiring/edit/{id}")
	public ResponseEntity<?> updateHiring(@PathVariable("id") long id, @RequestBody Hiring hiring) {
		try {
			
//			Date podate = hiring.getPosted();
//			Date codate = hiring.getClosing();
//			long departmentId = hiring.getDepartmentId();
//			String position =hiring.getPosition();
//			Optional<Hiring> existingHiringList = repo.findByDepartmentIdAndPositionAndPostedAndClosing(departmentId,position, podate, codate);


//            if (existingHiringList.isPresent()) {
//                return ResponseEntity.badRequest().body("hiring with ID " + departmentId + " already exists.");
//            }

            Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);

			java.util.Date currentDateMinusOneUtil = calendar.getTime();
			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());

			if (hiring.getCurrentdate() != null && hiring.getCurrentdate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			if (hiring.getPosted() != null && hiring.getPosted().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			if (hiring.getPosted() != null && hiring.getClosing() != null
					&& hiring.getPosted().after(hiring.getClosing())) {
				String errorMessage = "PostedDate cannot be later than ClosingDate.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			Hiring existingHiring = service.getById(id);
			if (existingHiring == null) {
				return ResponseEntity.notFound().build();
			}
			existingHiring.setDepartmentId(hiring.getDepartmentId());
			existingHiring.setClosing(hiring.getClosing());
			existingHiring.setEmail(hiring.getEmail());
			existingHiring.setPreferredList(hiring.getPreferredList());
			existingHiring.setBriefList(hiring.getBriefList());
			existingHiring.setPosition(hiring.getPosition());
			existingHiring.setRequests(hiring.getRequests());
			existingHiring.setVacancy(hiring.getVacancy());
			existingHiring.setPosted(hiring.getPosted());

			service.saveOrUpdate(existingHiring);
			return ResponseEntity.ok(existingHiring);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/hiring/delete/{id}")
	public ResponseEntity<String> deleteHiring(@PathVariable("id") long id) {
		try {
			service.deleteById(id);
			return ResponseEntity.ok("Hiring deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting Hiring: " + e.getMessage());
		}
	}



	@GetMapping("/hiring/view")
	public ResponseEntity<Object> getAllRoleByEmployee(@RequestParam(required = true) String view) {
		if ("hiring".equals(view)) {
			List<Map<String, Object>> assestList = new ArrayList<>();

			List<Map<String, Object>> assestRole = repo.getAllByClientDetails();

			// Grouping by hiring_id
			Map<String, List<Map<String, Object>>> assestGroupMap = assestRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("hiring_id").toString()));

			for (Entry<String, List<Map<String, Object>>> assestLoop : assestGroupMap.entrySet()) {
				Map<String, Object> innerMap = assestLoop.getValue().get(0);

				Map<String, Object> assestMap = new HashMap<>();
				List<Map<String, Object>> demoList = new ArrayList<>();
				Set<Object> uniqueBriefIds = new HashSet<>(); // Set to track unique brief_ids

				assestMap.put("country", innerMap.get("country"));
				assestMap.put("address", innerMap.get("address"));
				assestMap.put("companyName", innerMap.get("company_name"));
				assestMap.put("jobTitle", innerMap.get("department_name"));
				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(innerMap);
				String imageUrl = "company/" + randomNumber + "/" + innerMap.get("company_id") + "." + fileExtension;
				assestMap.put("profile", imageUrl);
				assestMap.put("companyPhoneNumber", innerMap.get("phone_number1"));
				assestMap.put("companyMobileNumber", innerMap.get("phone_number2"));
				assestMap.put("posted", innerMap.get("posted"));
				assestMap.put("companyEmail", innerMap.get("companyemail"));
				assestMap.put("companyId", innerMap.get("company_id"));
				assestMap.put("hiringId", Long.parseLong(assestLoop.getKey()));
				assestMap.put("closing", innerMap.get("closing"));
				assestMap.put("requests", innerMap.get("requests"));
				assestMap.put("companyId", innerMap.get("company_id"));
				assestMap.put("applicationId", innerMap.get("department_id"));
				assestMap.put("email", innerMap.get("email"));
				assestMap.put("vacancy", innerMap.get("vacancy"));
				assestMap.put("vacancy", innerMap.get("vacancy"));
				assestMap.put("position", innerMap.get("position"));
				assestMap.put("status", innerMap.get("status"));
				
				for (Map<String, Object> assestSubLoop : assestLoop.getValue()) {
					Object briefId = assestSubLoop.get("brief_id");

		
					if (uniqueBriefIds.contains(briefId)) {
						continue; // Skip this iteration if briefId is already processed
					}

					Map<String, Object> briefMap = new HashMap<>();
					briefMap.put("briefId", briefId);
					briefMap.put("briefDescription", assestSubLoop.get("brief_description"));
					demoList.add(briefMap);
					uniqueBriefIds.add(briefId);
				}
				assestMap.put("briefList", demoList);
				
				List<Map<String, Object>> demo1List = new ArrayList<>();
				Set<Object> uniquePreferredIds = new HashSet<>(); // Corrected set name

				for (Map<String, Object> assestSubLoop : assestLoop.getValue()) {
				    Object preferredId = assestSubLoop.get("preferred_id");
				    if (uniquePreferredIds.contains(preferredId)) {
				        continue; 
				    }

				    Map<String, Object> preferredMap = new HashMap<>();
				    preferredMap.put("preferredId", preferredId);
				    preferredMap.put("preferredSkills", assestSubLoop.get("preferred_skills"));
				    demo1List.add(preferredMap);

				    // Add the preferredId to the set to mark it as processed
				    uniquePreferredIds.add(preferredId);
				}

				assestMap.put("preferredList", demo1List);


				assestList.add(assestMap);
			}

			return ResponseEntity.ok(assestList);
		} else {
			String errorMessage = "Invalid value for 'assest'. Expected 'Assest'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@GetMapping("/hiring/view/{id}")
	public ResponseEntity<?> getAllQuotationByClientDetailsById(@PathVariable("id") Long hiringId) {
				List<Map<String, Object>> assestList = new ArrayList<>();

				List<Map<String, Object>> assestRole = repo.getAllQuotationByClientDetailss(hiringId);

				// Grouping by hiring_id
				Map<String, List<Map<String, Object>>> assestGroupMap = assestRole.stream()
						.collect(Collectors.groupingBy(action -> action.get("hiring_id").toString()));

				for (Entry<String, List<Map<String, Object>>> assestLoop : assestGroupMap.entrySet()) {
					Map<String, Object> innerMap = assestLoop.getValue().get(0);

					Map<String, Object> assestMap = new HashMap<>();
					List<Map<String, Object>> demoList = new ArrayList<>();
					Set<Object> uniqueBriefIds = new HashSet<>(); // Set to track unique brief_ids

					assestMap.put("country", innerMap.get("country"));
					assestMap.put("address", innerMap.get("address"));
					assestMap.put("companyName", innerMap.get("company_name"));
					assestMap.put("jobTitle", innerMap.get("department_name"));
					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(innerMap);
					String imageUrl = "company/" + randomNumber + "/" + innerMap.get("company_id") + "." + fileExtension;
					assestMap.put("profile", imageUrl);
					assestMap.put("companyPhoneNumber", innerMap.get("phone_number1"));
					assestMap.put("companyMobileNumber", innerMap.get("phone_number2"));
					assestMap.put("posted", innerMap.get("posted"));
					assestMap.put("companyEmail", innerMap.get("companyemail"));
					assestMap.put("companyId", innerMap.get("company_id"));
					assestMap.put("hiringId", Long.parseLong(assestLoop.getKey()));
					assestMap.put("closing", innerMap.get("closing"));
					assestMap.put("requests", innerMap.get("requests"));
					assestMap.put("companyId", innerMap.get("company_id"));
					assestMap.put("applicationId", innerMap.get("department_id"));
					assestMap.put("email", innerMap.get("email"));
					assestMap.put("vacancy", innerMap.get("vacancy"));
					assestMap.put("position", innerMap.get("position"));
					assestMap.put("status", innerMap.get("status"));

					for (Map<String, Object> assestSubLoop : assestLoop.getValue()) {
						Object briefId = assestSubLoop.get("brief_id");
						if (uniqueBriefIds.contains(briefId)) {
							continue; 
						}
						Map<String, Object> briefMap = new HashMap<>();
						briefMap.put("briefId", briefId);
						briefMap.put("briefDescription", assestSubLoop.get("brief_description"));
						demoList.add(briefMap);

						// Add the briefId to the set to mark it as processed
						uniqueBriefIds.add(briefId);
					}
					assestMap.put("briefList", demoList);					
					List<Map<String, Object>> demo1List = new ArrayList<>();
					Set<Object> uniquePreferredIds = new HashSet<>(); // Corrected set name

					for (Map<String, Object> assestSubLoop : assestLoop.getValue()) {
					    Object preferredId = assestSubLoop.get("preferred_id");					 
					    if (uniquePreferredIds.contains(preferredId)) {
					        continue; 
					    }

					    Map<String, Object> preferredMap = new HashMap<>();
					    preferredMap.put("preferredId", preferredId);
					    preferredMap.put("preferredSkills", assestSubLoop.get("preferred_skills"));
					    demo1List.add(preferredMap);

					    // Add the preferredId to the set to mark it as processed
					    uniquePreferredIds.add(preferredId);
					}

					assestMap.put("preferredList", demo1List);
					assestMap.putAll(assestMap);
//					assestList.add(assestMap);
					return ResponseEntity.ok(assestMap);
				}

				return ResponseEntity.ok(assestList);
			} 
		


	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000); // Change the upper limit if needed
	}

	private String getFileExtensionForImage(Map<String, Object> object) {
		if (object == null || !object.containsKey("url") || object.get("url") == null) {
			return "jpg";
		}

		String url = object.get("url").toString();

		if (url.endsWith(".png")) {
			return "png";
		} else if (url.endsWith(".jpg")) {
			return "jpg";
		} else {
			return "jpg";
		}
	}
	
	
	@GetMapping("/hiring/dashboard")
	public List<Map<String, Object>> AllWorksyyyy(@RequestParam(required = true) String Dashboard) {
		try {
			if ("hiring".equals(Dashboard)) {
				return repo.getAllProjectWorkaa();
			} else {			
				throw new IllegalArgumentException("The provided Dashboard is not supported.");
			}
		} catch (Exception e) {
			e.printStackTrace(); 		
			return Collections.emptyList(); 
		}
	}
	
	@GetMapping("/hiring/position")
	public List<Map<String, Object>> AllWorksPosition(@RequestParam(required = true) String position) {
		try {
			if ("hiring".equals(position)) {
				return repo.getAllByClientDetailsposition();
			} else {			
				throw new IllegalArgumentException("The provided position is not supported.");
			}
		} catch (Exception e) {
			e.printStackTrace(); 		
			return Collections.emptyList(); 
		}
	}
	
	@GetMapping("/hiring/role")
	public ResponseEntity<Object> getAllRoleByEmployeelist(@RequestParam(required = true) String department) {
		if ("hiring".equals(department)) {
			List<Map<String, Object>> departmentList = new ArrayList<>();
			List<Map<String, Object>> departmentRole = repo.getAllByClientDetailsposition();
			Map<String, List<Map<String, Object>>> departmentGroupMap = departmentRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("applicationId").toString()));

			for (Entry<String, List<Map<String, Object>>> departmentLoop : departmentGroupMap.entrySet()) {
				Map<String, Object> departmentMap = new HashMap<>();
				departmentMap.put("applicationId", Long.parseLong(departmentLoop.getKey()));
				departmentMap.put("departmentName", departmentLoop.getValue().get(0).get("departmentName"));
				List<Map<String, Object>> departmentSubList = new ArrayList<>();
				for (Map<String, Object> departmentsubLoop : departmentLoop.getValue()) {

					Map<String, Object> departmentSubMap = new HashMap<>();
					departmentSubMap.put("applicationId", departmentsubLoop.get("applicationId"));
					departmentSubMap.put("departmentName", departmentsubLoop.get("departmentName"));
					departmentSubMap.put("closing", departmentsubLoop.get("closing"));
					departmentSubMap.put("posted", departmentsubLoop.get("posted"));
					departmentSubMap.put("hiringId", departmentsubLoop.get("hiringId"));
					departmentSubMap.put("position", departmentsubLoop.get("position"));
					departmentSubMap.put("vacancy", departmentsubLoop.get("vacancy"));
					departmentSubList.add(departmentSubMap);
				}
				departmentMap.put("departmentDetails", departmentSubList);
				departmentList.add(departmentMap);
			}

			return ResponseEntity.ok(departmentList);
		} else {
			String errorMessage = "Invalid value for 'department'. Expected 'Department'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

}
