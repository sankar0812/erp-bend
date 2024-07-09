package com.example.erp.controller.employee;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.transaction.Transactional;

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
import com.example.erp.entity.employee.Employee;
import com.example.erp.entity.employee.Resignations;
import com.example.erp.repository.employee.ResignationsRepository;
import com.example.erp.service.employee.EmployeeService;
import com.example.erp.service.employee.ResignationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin
@RestController
public class ResignationsController {
	@Autowired
	private ResignationsService service;

	@Autowired
	private ResignationsRepository repo;

	@Autowired
	private EmployeeService employeeService;

	private static final Logger logger = LoggerFactory.getLogger(ResignationsController.class);

	@GetMapping("/resignations")
	public ResponseEntity<?> getResignations(@RequestParam(required = true) String resignationsParam) {
		try {
			if ("resignations".equalsIgnoreCase(resignationsParam)) {
				List<Resignations> resignations = service.listAll();
				return ResponseEntity.ok(resignations);
			} else {
				return ResponseEntity.badRequest().body("Invalid parameter value. 'personalParam' must be 'Personal'.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving resignations: " + e.getMessage());
		}
	}

	@PostMapping("/resignations/save")
	public ResponseEntity<String> saveResignation(@RequestBody Resignations resignations) {
		try {
			Date resignationsDate = resignations.getResignationsDate();
			resignations.setFromDate(resignationsDate);
			resignations.setType("pending");
			resignations.setResignationsList("Resignations");
			resignations.setPending(true);
//	        resignations.setStatus(true);
			service.saveOrUpdate(resignations);

			return ResponseEntity.ok("Resignation saved with id: " + resignations.getResignationsId());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving Resignation: " + e.getMessage());
		}
	}

	@PutMapping("/resignations/or/{id}")
	public ResponseEntity<?> toggleResignationStatus(@PathVariable(name = "id") long id) {
		try {
			Resignations resignation = service.getById(id);
			if (resignation != null) {
				boolean currentStatus = resignation.isStatus();
				resignation.setStatus(!currentStatus);
				service.saveOrUpdate(resignation);
				return ResponseEntity.ok(resignation.isStatus());
			} else {
				return ResponseEntity.ok(false);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}

	@PutMapping("/resignations/edit/{id}")
	public ResponseEntity<?> updateResignations(@PathVariable("id") long id, @RequestBody Resignations resignations) {
		try {
			if (resignations.getFromDate() != null && resignations.getToDate() != null
					&& resignations.getFromDate().after(resignations.getToDate())) {
				String errorMessage = "FromDate cannot be later than ToDate.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			Resignations existingResignations = service.getById(id);
			if (existingResignations == null) {
				return ResponseEntity.notFound().build();
			}

			if (resignations.getResignationsDate() != null) {
				existingResignations.setResignationsDate(resignations.getResignationsDate());
			}
			if (resignations.getReason() != null) {
				existingResignations.setReason(resignations.getReason());
			}
			if (resignations.getType() != null) {
				existingResignations.setType(resignations.getType());
			}
			if (resignations.getFromDate() != null) {
				existingResignations.setFromDate(resignations.getFromDate());
			}
			if (resignations.getToDate() != null) {
				existingResignations.setToDate(resignations.getToDate());
			}
			if (resignations.getTitle() != null) {
				existingResignations.setTitle(resignations.getTitle());
			}

			if ("pending".equals(existingResignations.getType())) {
				existingResignations.setApproved(false);
				existingResignations.setPending(true);
			} else if ("approved".equals(existingResignations.getType())) {
				existingResignations.setApproved(true);
				existingResignations.setPending(false);
			} else {
				existingResignations.setApproved(false);
				existingResignations.setPending(false);
			}
			Date currentDate = new Date(System.currentTimeMillis());
			Optional<Employee> employeeOptional = employeeService.getEmployeeById1(resignations.getEmployeeId());

			if (employeeOptional.isPresent()) {
				Employee employee = employeeOptional.get();

				if (resignations.getToDate() != null && currentDate != null) {
					logger.info("resignations.getToDate(): {}", resignations.getToDate());
					logger.info("currentDate: {}", currentDate);

					Date toDate = resignations.getToDate();

					LocalDate currentDateWithoutTime = currentDate.toInstant().atZone(ZoneId.systemDefault())
							.toLocalDate();

					if (toDate != null && currentDateWithoutTime != null) {
						if (toDate.equals(currentDate) || toDate.before(currentDate)) {
							logger.info("Setting status to false for employee and resignation");
							employee.setStatus(false);
							resignations.setStatus(false);

							// Log the current status values
							logger.info("Employee Status: {}", employee.isStatus());
							logger.info("Resignation Status: {}", resignations.isStatus());

							// Assuming your EmployeeService and ResignationService have saveOrUpdate
							// methods
							employeeService.saveOrUpdate(employee);
						}
					}
				}
			} else {
				logger.warn("Employee not found for id: {}", resignations.getEmployeeId());
			}

			service.saveOrUpdate(existingResignations);
			return ResponseEntity.ok(existingResignations);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Internal Server Error: " + e.getMessage());
		}

	}

	@DeleteMapping("/resignations/delete/{id}")
	public ResponseEntity<String> deleteResignations(@PathVariable("id") long id) {
		try {
			service.deleteById(id);
			return ResponseEntity.ok("Resignations deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting Resignations: " + e.getMessage());
		}
	}

//	@GetMapping("/resignations/view")
//	public List<Map<String, Object>> getAllReg(@RequestParam(required = true) String resignationsParam) {
//		try {
//			if ("resignationsview".equalsIgnoreCase(resignationsParam)) {
//				return service.ALLOver();
//			} else {
//				throw new IllegalArgumentException(
//						"Invalid parameter value. 'resignationsParam' must be 'resignations'.");
//			}
//		} catch (Exception e) {
//
//			e.printStackTrace();
//			return Collections.emptyList();
//		}
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
		return random.nextInt(1000000);
	}

	@GetMapping("/resignations/view")
	public ResponseEntity<?> getTaskAssignedDetailsp(@RequestParam(required = true) String resignationsParam) {
		try {
			if ("resignationsview".equals(resignationsParam)) {
				List<Map<String, Object>> tasks = service.ALLOver();
				List<Map<String, Object>> taskList = new ArrayList<>();

				Map<String, List<Map<String, Object>>> resignGroupMap = tasks.stream()
						.collect(Collectors.groupingBy(action -> action.get("resignationsId").toString()));

				for (Entry<String, List<Map<String, Object>>> resignLoop : resignGroupMap.entrySet()) {
					for (Map<String, Object> entryLoop : resignLoop.getValue()) {
						Map<String, Object> taskAssignedMap = new HashMap<>();

						if (resignLoop.getValue().get(0).get("btype").equals("timeEnd")) {

							long employeeId = Long.parseLong((String) resignLoop.getValue().get(0).get("employeeId"));

							if (employeeId != 0) {
								Employee employee = employeeService.getById(employeeId);
								employee.setStatus(false);
								employeeService.saveOrUpdate(employee);
							}
						}

						int randomNumber = generateRandomNumber();
						String fileExtension = getFileExtensionForImage(entryLoop);
						String imageUrl = "profile/" + randomNumber + "/"
								+ resignLoop.getValue().get(0).get("employeeId") + "." + fileExtension;
						taskAssignedMap.put("profile", imageUrl);
						taskAssignedMap.putAll(entryLoop);
						taskList.add(taskAssignedMap);

					}
				}
				return ResponseEntity.ok(taskList);
			} else {
				String errorMessage = "Invalid value for 'resignationsParam'. Expected 'resignationsview'.";
				return ResponseEntity.badRequest().body(Collections.singletonMap("error", errorMessage));
			}
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "Error occurred while retrieving task assigned details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
		}
	}

	@GetMapping("/resignations/employees/{employeeId}")
	public ResponseEntity<String> updateEmployeeStatus(@PathVariable Long employeeId) {
		try {
			repo.updateEmployeeStatus(employeeId);
			System.out.println("Employee status updated successfully");
			return ResponseEntity.ok("Employee status updated successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating employee status");
		}
	}

	@GetMapping("/employee/experience/{id}")
	public ResponseEntity<List<Map<String, Object>>> displayAllImages1(@PathVariable("id") Long employee_id) {
		try {

			List<Map<String, Object>> images = repo.AllNotifications1(employee_id);
			List<Map<String, Object>> imageResponses = new ArrayList<>();

			for (Map<String, Object> image : images) {
				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(image);
				String imageUrl = "company/" + randomNumber + "/" + image.get("company_id") + "." + fileExtension;
				Map<String, Object> imageResponse = new HashMap<>();
				imageResponse.put("profile", imageUrl);
				imageResponse.put("designationName", image.get("designation_name"));
				imageResponse.put("employeeId", image.get("employee_id"));
				imageResponse.put("designationId", image.get("designation_id"));
				imageResponse.put("companyId", image.get("company_id"));
				imageResponse.put("companyName", image.get("company_name"));
				imageResponse.put("date", image.get("date"));
				imageResponse.put("userName", image.get("user_name"));
				imageResponse.put("noticeDate", image.get("noticeDate"));
				imageResponse.put("country", image.get("country"));
				imageResponse.put("email", image.get("email"));
				imageResponse.put("gstNo", image.get("gst_no"));
				imageResponse.put("address", image.get("address"));
				imageResponse.put("phoneNumber1", image.get("phone_number1"));
				imageResponse.put("phoneNumber2", image.get("phone_number2"));
				imageResponse.put("state", image.get("state"));
				imageResponse.put("pincode", image.get("pincode"));
				imageResponses.add(imageResponse);
			}
			return ResponseEntity.ok(imageResponses);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

//	@GetMapping("/resignations/dashboard")
//	public List<Map<String, Object>> getAllReggjtyui8(@RequestParam(required = true) String resignations) {
//		try {
//			if ("dashboard".equalsIgnoreCase(resignations)) {				
//				return repo.AllResignationsrepository();
//			} else {
//				throw new IllegalArgumentException(
//						"Invalid parameter value. 'resignations' must be 'dashboard'.");
//			}
//		} catch (Exception e) {
//
//			e.printStackTrace();
//			return Collections.emptyList();
//		}
//	}
	@GetMapping("/resignations/dashboard")
	public ResponseEntity<?> getTaskAssignedDetailspsfghtr(@RequestParam(required = true) String resignations) {
		try {
			if ("dashboard".equals(resignations)) {
				List<Map<String, Object>> tasks = repo.AllResignationsrepository();
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

	@PostMapping("/resignations/manager")
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
				List<Map<String, Object>> leaveData = repo.getAllresignations(year);
				List<Map<String, Object>> imageResponses = new ArrayList<>();

				for (Map<String, Object> image : leaveData) {
					int randomNumber = generateRandomNumber();

					String fileExtension = getFileExtensionForImage(image);
					String imageUrl1 = "profile/" + randomNumber + "/" + image.get("employeeId") + "." + fileExtension;
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
				List<Map<String, Object>> leaveData1 = repo.getAllresignationsemployee(monthName);
				List<Map<String, Object>> imageResponses = new ArrayList<>();

				for (Map<String, Object> image : leaveData1) {
					int randomNumber = generateRandomNumber();

					String fileExtension = getFileExtensionForImage(image);
					String imageUrl1 = "profile/" + randomNumber + "/" + image.get("employeeId") + "." + fileExtension;
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

	@GetMapping("/resignations/{employee_id}")
	private List<Map<String, Object>> idbasedAnnouncement(@PathVariable("employee_id") Long employee_id) {
		List<Map<String, Object>> announcementlist = new ArrayList<>();
		List<Map<String, Object>> list = repo.AllTimeGoat(employee_id);
		Map<String, List<Map<String, Object>>> announcementGroupMap = StreamSupport.stream(list.spliterator(), false)
				.collect(Collectors.groupingBy(action -> String.valueOf(action.get("employee_id"))));

		for (Map.Entry<String, List<Map<String, Object>>> totalList : announcementGroupMap.entrySet()) {
			Map<String, Object> announcementMap = new HashMap<>();
			announcementMap.put("employee_id", totalList.getKey());
			announcementMap.put("resignations_date", totalList.getValue().get(0).get("resignations_date"));
			announcementMap.put("resignations Details", totalList.getValue());
			announcementlist.add(announcementMap);
		}
		return announcementlist;
	}

	/////////////// 18////////////////////

	@PostMapping("/resignations/date")
	public List<Map<String, Object>> getAllVoucherBetweenDates(@RequestBody Map<String, Object> requestBody) {
		LocalDate startDate = LocalDate.parse(requestBody.get("startDate").toString(), DateTimeFormatter.ISO_DATE);
		LocalDate endDate = LocalDate.parse(requestBody.get("endDate").toString(), DateTimeFormatter.ISO_DATE);
		return repo.getAllReceiptBetweenDate(startDate, endDate);
	}

	/////////////// 19 /////////////////////

	@GetMapping("/resignations/durationcount")
	public List<Map<String, Object>> getAllDurationSDate() {
		return repo.getAllDurationDate();
	}

}
