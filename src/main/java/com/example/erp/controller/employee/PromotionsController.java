package com.example.erp.controller.employee;

import java.time.LocalDate;
import java.util.*;
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

import com.example.erp.entity.clientDetails.ClientInvoiceList;
import com.example.erp.entity.employee.Bank;
import com.example.erp.entity.employee.Department;
import com.example.erp.entity.employee.EmergencyContacts;
import com.example.erp.entity.employee.Employee;
import com.example.erp.entity.employee.EmployeeLeave;
import com.example.erp.entity.employee.FamilyInformations;
import com.example.erp.entity.employee.Personal;
import com.example.erp.entity.employee.Promotions;
import com.example.erp.entity.employee.Qualification;
import com.example.erp.repository.employee.PromotionsRepository;
import com.example.erp.service.employee.BankService;
import com.example.erp.service.employee.EmergencyContactsService;
import com.example.erp.service.employee.EmployeeService;
import com.example.erp.service.employee.FamilyInformationsService;
import com.example.erp.service.employee.PersonalService;
import com.example.erp.service.employee.PromotionsService;
import com.example.erp.service.employee.QualificationService;

@CrossOrigin
@RestController
public class PromotionsController {
	@Autowired
	private PromotionsService service;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private PromotionsRepository repo;

	@Autowired
	private BankService bankService;

	@Autowired
	private PersonalService personalService;

	@Autowired
	private EmergencyContactsService emergencyContactsService;

	@Autowired
	private FamilyInformationsService familyInformationsService;

	@Autowired
	private QualificationService qualificationService;

	@GetMapping("/promotions")
	public ResponseEntity<?> getPromotions(@RequestParam(required = true) String promotionsType) {
		try {
			if ("proemployee".equals(promotionsType)) {
				List<Promotions> promotions = service.listAll();
				return ResponseEntity.ok(promotions);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided LeaveType is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving Promotions: " + e.getMessage());
		}
	}

	@PostMapping("/promotions/save")
	public ResponseEntity<?> savePromotions(@RequestBody Promotions promotions) {
		try {
			promotions.setStatus(true);

			Long employeeId = promotions.getEmployeeId();

			Employee employee = employeeService.getEmployeeById(employeeId);

			Personal personal = personalService.getByEmployeeId(employeeId);
			Bank bank = bankService.getByEmployeeId(employeeId);
			EmergencyContacts contacts = emergencyContactsService.getByEmployeeId(employeeId);
			FamilyInformations family = familyInformationsService.getByEmployeeId(employeeId);
			Qualification qualification = qualificationService.getByEmployeeId(employeeId);
			String emp = employee.getUserId();
			 if (employee.getRoleType().equals(promotions.getRoleType())) {	
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("The Person is already in same Role");
			} else {
				if ("Employee".equals(promotions.getRoleType())) {
					employee.setRoleId(4);
					personal.setRoleId(4);
					contacts.setRoleId(4);
					family.setRoleId(4);
					bank.setRoleId(4);
					qualification.setRoleId(4);
					promotions.setRoleId(4);
					employee.setRoleType("Employee");
				} else if ("ProjectHead".equals(promotions.getRoleType())) {
					employee.setRoleId(6);
					personal.setRoleId(6);
					promotions.setRoleId(6);
					contacts.setRoleId(6);
					family.setRoleId(6);
					bank.setRoleId(6);
					qualification.setRoleId(6);
					employee.setRoleType("ProjectHead");
				} else if ("TL".equals(promotions.getRoleType())) {
					employee.setRoleId(7);
					promotions.setRoleId(7);
					personal.setRoleId(7);
					contacts.setRoleId(7);
					family.setRoleId(7);
					bank.setRoleId(7);
					qualification.setRoleId(7);
					employee.setRoleType("TL");
				} else if ("Research".equals(promotions.getRoleType())) {
					employee.setRoleId(10);
					promotions.setRoleId(10);
					personal.setRoleId(10);
					contacts.setRoleId(10);
					family.setRoleId(10);
					bank.setRoleId(10);
					qualification.setRoleId(10);
					employee.setRoleType("Research");
				} else {
					employee.setRoleId(0);
					personal.setRoleId(0);
					promotions.setRoleId(0);
					contacts.setRoleId(0);
					family.setRoleId(0);
					bank.setRoleId(0);
					qualification.setRoleId(0);
				}

				String newFirstTwoLetters = promotions.getRoleType().substring(0, 2).toUpperCase();
				String userId1 = newFirstTwoLetters + emp.substring(2);
				employee.setUserId(userId1);
				service.saveOrUpdate(promotions);
				employeeService.saveOrUpdate(employee);
			}

			return ResponseEntity.ok("Promotions saved with id: " + promotions.getPromotionsId());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving Promotions: " + e.getMessage());
		}
	}

	@PutMapping("/promotions/or/{id}")
	public ResponseEntity<?> getPromotionsById(@PathVariable(name = "id") long id) {
		try {
			Promotions Promotions = service.getById(id);
			if (Promotions != null) {
				boolean currentStatus = Promotions.isStatus();
				Promotions.setStatus(!currentStatus);
				service.saveOrUpdate(Promotions);
			} else {
				return ResponseEntity.ok(false);
			}
			return ResponseEntity.ok(Promotions.isStatus());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}

	@PutMapping("/promotions/edit/{id}")
	public ResponseEntity<?> updatePromotions(@PathVariable("id") long id, @RequestBody Promotions promotions) {
		try {
			Promotions existingPromotions = service.getById(id);
			if (existingPromotions == null) {
				return ResponseEntity.notFound().build();
			}
			Long employeeId = promotions.getEmployeeId();
			Employee employee = employeeService.getEmployeeById(employeeId);
			Personal personal = personalService.getByEmployeeId(employeeId);
			Bank bank = bankService.getByEmployeeId(employeeId);
			EmergencyContacts contacts = emergencyContactsService.getByEmployeeId(employeeId);
			FamilyInformations family = familyInformationsService.getByEmployeeId(employeeId);
			Qualification qualification = qualificationService.getByEmployeeId(employeeId);
			String emp = employee.getUserId();
			if ("Employee".equals(promotions.getRoleType())) {
				employee.setRoleId(4);
				personal.setRoleId(4);
				contacts.setRoleId(4);
				family.setRoleId(4);
				bank.setRoleId(4);
				qualification.setRoleId(4);
				promotions.setRoleId(4);
				employee.setRoleType("Employee");
			} else if ("ProjectHead".equals(promotions.getRoleType())) {
				employee.setRoleId(6);
				personal.setRoleId(6);
				promotions.setRoleId(6);
				contacts.setRoleId(6);
				family.setRoleId(6);
				bank.setRoleId(6);
				qualification.setRoleId(6);
				employee.setRoleType("ProjectHead");
			} else if ("TL".equals(promotions.getRoleType())) {
				employee.setRoleId(7);
				promotions.setRoleId(7);
				personal.setRoleId(7);
				contacts.setRoleId(7);
				family.setRoleId(7);
				bank.setRoleId(7);
				qualification.setRoleId(7);
				employee.setRoleType("TL");
			} else if ("Research".equals(promotions.getRoleType())) {
				employee.setRoleId(10);
				promotions.setRoleId(10);
				personal.setRoleId(10);
				contacts.setRoleId(10);
				family.setRoleId(10);
				bank.setRoleId(10);
				qualification.setRoleId(10);
				employee.setRoleType("Research");
			} else {
				employee.setRoleId(0);
				personal.setRoleId(0);
				promotions.setRoleId(0);
				contacts.setRoleId(0);
				family.setRoleId(0);
				bank.setRoleId(0);
				qualification.setRoleId(0);
			}

			String newFirstTwoLetters = promotions.getRoleType().substring(0, 2).toUpperCase();
			String userId1 = newFirstTwoLetters + emp.substring(2);
			employee.setUserId(userId1);
			service.saveOrUpdate(existingPromotions);
			return ResponseEntity.ok(existingPromotions);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/promotions/delete/{id}")
	public ResponseEntity<String> deletePromotions(@PathVariable("id") long id) {
		try {
			service.deleteById(id);
			return ResponseEntity.ok("Promotions deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting Promotions: " + e.getMessage());
		}
	}

//	@GetMapping("/promotions/view")
//	public List<Map<String, Object>> INeedList(@RequestParam(required = true) String promotions) {
//	    try {
//	        if ("promotions".equals(promotions)) {
//	            return service.AllFine();
//	        } else {	         
//	            throw new IllegalArgumentException("The provided LeaveType is not supported.");
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return Collections.emptyList();
//	    }
//	}

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

	@GetMapping("/promotions/view")
	public ResponseEntity<?> getTaskAssignedDetailsp(@RequestParam(required = true) String promotions) {
		try {
			if ("promotions".equals(promotions)) {
				List<Map<String, Object>> tasks = service.AllFine();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();

					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(taskAssigned);
					String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employee_id") + "."
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
//			String errorMessage = "Error occurred while retrieving task assigned details";
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e));
		}
	}

	@PostMapping("/promotions/manager")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndYearOrMonth(
			@RequestBody Map<String, Object> requestBody) {

		if (!requestBody.containsKey("choose")) {
			return ResponseEntity.badRequest().build();
		}

		String choose = requestBody.get("choose").toString();

		switch (choose) {
		case "year":
			if (requestBody.containsKey("year")) {
				String year = requestBody.get("year").toString();
				List<Map<String, Object>> leaveData = repo.getAllpromotions(year);
				List<Map<String, Object>> imageResponses = new ArrayList<>();

				for (Map<String, Object> image : leaveData) {
					int randomNumber = generateRandomNumber();

					String fileExtension = getFileExtensionForImage(image);
					String imageUrl1 = "profile/" + randomNumber + "/" + image.get("employee_id") + "." + fileExtension;
					Map<String, Object> imageResponse = new HashMap<>();
					imageResponse.put("profile", imageUrl1);

					imageResponse.putAll(image);
					imageResponses.add(imageResponse);
				}
				return ResponseEntity.ok(imageResponses);

			}
			break;

		case "month":
			if (requestBody.containsKey("monthName")) {
				String monthName = requestBody.get("monthName").toString();
				List<Map<String, Object>> leaveData1 = repo.getAllpromotionssemployee(monthName);
				List<Map<String, Object>> imageResponses = new ArrayList<>();

				for (Map<String, Object> image : leaveData1) {
					int randomNumber = generateRandomNumber();

					String fileExtension = getFileExtensionForImage(image);
					String imageUrl1 = "profile/" + randomNumber + "/" + image.get("employee_id") + "." + fileExtension;
					Map<String, Object> imageResponse = new HashMap<>();
					imageResponse.put("profile", imageUrl1);

					imageResponse.putAll(image);
					imageResponses.add(imageResponse);
				}
				return ResponseEntity.ok(imageResponses);

			}
			break;

		default:
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.badRequest().build();
	}

}
