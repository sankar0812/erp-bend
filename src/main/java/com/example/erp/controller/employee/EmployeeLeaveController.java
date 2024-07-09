package com.example.erp.controller.employee;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
import com.example.erp.entity.employee.EmployeeAttendance;
import com.example.erp.entity.employee.EmployeeLeave;
import com.example.erp.entity.employee.EmployeeLeaveList;
import com.example.erp.entity.erecruitment.Candidate;
import com.example.erp.entity.erecruitment.InterviewSchedule;
import com.example.erp.entity.erecruitment.Training;
import com.example.erp.repository.employee.EmployeeLeaveRepository;
import com.example.erp.service.eRecruitment.TrainingService;
import com.example.erp.service.employee.EmployeeLeaveService;
import com.example.erp.service.employee.EmployeeService;

@RestController
@CrossOrigin
public class EmployeeLeaveController {
	@Autowired
	private EmployeeLeaveService service;
	@Autowired
	private EmployeeLeaveRepository repo;
	
	@Autowired
	private TrainingService trainingService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
    private JavaMailSender emailSender;
	
	
	 @Value("${spring.mail.username}")
	    private String from;

	@GetMapping("/employeeleave")
	public ResponseEntity<?> getEmployeeLeaves(@RequestParam(required = true) String leave) {
		try {
			if ("leave".equals(leave)) {
				List<EmployeeLeave> employeeLeaves = service.listAll();
				return ResponseEntity.ok(employeeLeaves);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided leave is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving EmployeeLeaves: " + e.getMessage());
		}
	}

	@PostMapping("/employeeleave/save")
	public ResponseEntity<String> saveEmployeeLeave(@RequestBody EmployeeLeave employeeLeave) {
		try {

			Long employeeIdObject = employeeLeave.getEmployeeId();
			Long traineeIdObject = employeeLeave.getTraineeId();		
			long defaultObj = 0L;  
			long traineeId = traineeIdObject != null ? traineeIdObject : employeeIdObject != null ? employeeIdObject : defaultObj;
			long employeeId = employeeIdObject != null ? employeeIdObject : traineeIdObject != null ? traineeIdObject : defaultObj;

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);

			java.util.Date currentDateMinusOneUtil = calendar.getTime();
			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());

			if (employeeLeave.getDate() != null && employeeLeave.getDate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			if (employeeLeave.getDate() != null && employeeLeave.getToDate() != null
					&& employeeLeave.getDate().after(employeeLeave.getToDate())) {
				String errorMessage = "FromDate cannot be later than ToDate.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			
		
			List<LocalDate> datesList = new ArrayList<>();
			Date fromDate = employeeLeave.getDate();
			Date toDate = employeeLeave.getToDate();

			LocalDate localFromDate = fromDate.toLocalDate();
			
			LocalDate localToDate = toDate.toLocalDate();

			Optional<EmployeeLeave> existingAttendance;

			if (employeeLeave.getEmployeeId() != null) {
				existingAttendance = repo.findByEmployeeIdAndDateAndToDate(employeeLeave.getEmployeeId(),
						employeeLeave.getDate(),employeeLeave.getToDate());
			} else if (employeeLeave.getTraineeId() != 0) {
				existingAttendance = repo.findByTraineeIdAndDateAndToDate(employeeLeave.getTraineeId(),
						employeeLeave.getDate(),employeeLeave.getToDate());
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Both employeeId and traineeId are null. Please provide either employeeId or traineeId.");
			}

			if (existingAttendance.isPresent()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("EmployeeLeave entry already exists for "
								+ (employeeLeave.getEmployeeId() != null ? "employee ID " + employeeLeave.getEmployeeId()
										: "trainee ID " + employeeLeave.getTraineeId())
								+ " on date " + employeeLeave.getDate() + ".");
			}
			
			
			while (!localFromDate.isAfter(localToDate)) {
			    datesList.add(localFromDate);
			    localFromDate = localFromDate.plusDays(1);
			}

			List<EmployeeLeaveList> employeeLeaveList = new ArrayList<>();
			for (LocalDate date : datesList) {
			    EmployeeLeaveList empLeaveList = new EmployeeLeaveList();
			    empLeaveList.setAllDates(Date.valueOf(date));
			    employeeLeaveList.add(empLeaveList);
			}
			employeeLeave.setEmployeeLeaveList(employeeLeaveList);


			employeeLeave.setLeaveType("pending");
			employeeLeave.setPending(true);
			employeeLeave.setStatus(true);
			service.saveOrUpdate(employeeLeave);
			sendEmailToLeave(employeeLeave);

			return ResponseEntity.ok("EmployeeLeave saved with id: " + employeeLeave.getEmployeeLeaveId());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving EmployeeLeave: " + e.getMessage());
		}
	}
	

	 public void sendEmailToLeave(EmployeeLeave employeeLeave) {
	        try {
	            String email;
	            String name;
	            String subject = employeeLeave.getReason();
	            String dateRange = employeeLeave.getDate() + " - " + employeeLeave.getToDate();
	            String status = employeeLeave.getReasonDescription();

	            if (employeeLeave.getEmployeeId() != null) { 
	                Employee employee = employeeService.getEmployeeById(employeeLeave.getEmployeeId());
	                email = employee.getEmail();
	                name = employee.getUserName();
	            } else if (employeeLeave.getTraineeId() != 0) { 
	                Training trainee = trainingService.findById(employeeLeave.getTraineeId());
	                email = trainee.getEmail();
	                name = trainee.getUserName();
	            } else {
	                return; 
	            }

	            MimeMessage message = emailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setTo(email);
	            helper.setFrom(from);
	            helper.setSubject(subject);

	            String emailContent = "Dear " + name + ",\n\n"
	                    + "Your leave application for " + dateRange + " has been submitted successfully.\n\n"
	                    + "Please wait for " + status + " approval from the HR team.\n\n"
	                    + "Regards,\nHR Team";
	            helper.setText(emailContent);

	            emailSender.send(message);
	        } catch (MessagingException e) {
	            e.printStackTrace();
	            // Log or handle the exception appropriately
	        } catch (Exception e) {
	            e.printStackTrace();
	            // Log or handle the exception appropriately
	        }
	    }


	
	@PutMapping("/employeeleave/or/{id}")
	public ResponseEntity<?> getEmployeeLeaveById(@PathVariable(name = "id") long id) {
		try {
			EmployeeLeave EmployeeLeave = service.getById(id);
			if (EmployeeLeave != null) {
				boolean currentStatus = EmployeeLeave.isStatus();
				EmployeeLeave.setStatus(!currentStatus);
				service.saveOrUpdate(EmployeeLeave);
			} else {
				return ResponseEntity.ok(false);
			}

			return ResponseEntity.ok(EmployeeLeave.isStatus());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}

	@PutMapping("/employeeleave/edit/{id}")
	public ResponseEntity<?> updateEmployeeLeave(@PathVariable("id") long id,
			@RequestBody EmployeeLeave EmployeeLeave) {
		try {

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);

			java.util.Date currentDateMinusOneUtil = calendar.getTime();
			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());

			if (EmployeeLeave.getDate() != null && EmployeeLeave.getDate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			if (EmployeeLeave.getDate() != null && EmployeeLeave.getToDate() != null
					&& EmployeeLeave.getDate().after(EmployeeLeave.getToDate())) {
				String errorMessage = "FromDate cannot be later than ToDate.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			EmployeeLeave existingEmployeeLeave = service.getById(id);
			if (existingEmployeeLeave == null) {
				return ResponseEntity.notFound().build();
			}
			if (EmployeeLeave.getDate() != null) {
				existingEmployeeLeave.setDate(EmployeeLeave.getDate());
			}
			if (EmployeeLeave.getEmployeeId() != null) {
				existingEmployeeLeave.setEmployeeId(EmployeeLeave.getEmployeeId());
			}
			if (EmployeeLeave.getReason() != null) {
				existingEmployeeLeave.setReason(EmployeeLeave.getReason());
			}
			if (EmployeeLeave.getCancellationReason() != null) {
				existingEmployeeLeave.setCancellationReason(EmployeeLeave.getCancellationReason());
			}
			if (EmployeeLeave.getToDate() != null) {
				existingEmployeeLeave.setToDate(EmployeeLeave.getToDate());
			}
			if (EmployeeLeave.getReasonDescription() != null) {
				existingEmployeeLeave.setReasonDescription(EmployeeLeave.getReasonDescription());
			}
			if (EmployeeLeave.getLeaveType() != null) {
				existingEmployeeLeave.setLeaveType(EmployeeLeave.getLeaveType());
			}
			if ("pending".equals(existingEmployeeLeave.getLeaveType())) {
				existingEmployeeLeave.setCanceled(false);
				existingEmployeeLeave.setCompleted(false);
				existingEmployeeLeave.setPending(true);

			} else if ("rejected".equals(existingEmployeeLeave.getLeaveType())) {
				existingEmployeeLeave.setCanceled(true);
				existingEmployeeLeave.setCompleted(false);
				existingEmployeeLeave.setPending(false);

			} else if ("approved".equals(existingEmployeeLeave.getLeaveType())) {
				existingEmployeeLeave.setCanceled(false);
				existingEmployeeLeave.setCompleted(true);
				existingEmployeeLeave.setApprovedBy(true);
				existingEmployeeLeave.setPending(false);
			} else {
				existingEmployeeLeave.setCanceled(false);
				existingEmployeeLeave.setCompleted(false);
				existingEmployeeLeave.setPending(false);
			}

			service.saveOrUpdate(existingEmployeeLeave);
			return ResponseEntity.ok(existingEmployeeLeave);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/employeeleave/delete/{id}")
	public ResponseEntity<String> deleteEmployeeLeave(@PathVariable("id") long id) {
		try {
			service.deleteById(id);
			return ResponseEntity.ok("EmployeeLeave deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting EmployeeLeave: " + e.getMessage());
		}
	}

	@GetMapping("/employeeleave/view")
	public ResponseEntity<?> getTaskAssignedDetails(@RequestParam(required = true) String employeeleave) {
		try {
			if ("employeeleave".equals(employeeleave)) {
				List<Map<String, Object>> tasks = repo.getAllProjectWork();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();

					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(taskAssigned);

					if (taskAssigned.get("employeeId") != null) {
						String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employeeId") + "."
								+ fileExtension;
						taskAssignedMap.put("profile", imageUrl);
						taskAssignedMap.put("id", taskAssigned.get("employeeId"));
						taskAssignedMap.put("userName", taskAssigned.get("emp_name"));
						taskAssignedMap.put("date", taskAssigned.get("date"));
						taskAssignedMap.put("roleName", taskAssigned.get("roleName"));
						taskAssignedMap.put("toDate", taskAssigned.get("toDate"));
						taskAssignedMap.put("reason", taskAssigned.get("reason"));
						taskAssignedMap.put("employeeLeaveId", taskAssigned.get("employeeLeaveId"));
						taskAssignedMap.put("totalDay", taskAssigned.get("totalDay"));
						taskAssignedMap.put("leavetype", taskAssigned.get("leavetype"));
						taskAssignedMap.put("cancellationReason", taskAssigned.get("cancellationReason"));
						taskAssignedMap.put("departmentName", taskAssigned.get("departmentName"));
						taskAssignedMap.put("departmentId", taskAssigned.get("departmentId"));
						taskAssignedMap.put("reasonDescription", taskAssigned.get("reasonDescription"));
						taskAssignedMap.put("roleId", taskAssigned.get("roleId"));
					} else if (taskAssigned.get("traineeId") != null) {
						String imageUrl = "training/" + randomNumber + "/" + taskAssigned.get("traineeId");
						taskAssignedMap.put("profile", imageUrl);
						taskAssignedMap.put("id", taskAssigned.get("traineeId"));
						taskAssignedMap.put("userName", taskAssigned.get("userName"));
						taskAssignedMap.put("date", taskAssigned.get("date"));
						taskAssignedMap.put("roleName", taskAssigned.get("roleName"));
						taskAssignedMap.put("toDate", taskAssigned.get("toDate"));
						taskAssignedMap.put("reason", taskAssigned.get("reason"));
						taskAssignedMap.put("employeeLeaveId", taskAssigned.get("employeeLeaveId"));
						taskAssignedMap.put("totalDay", taskAssigned.get("totalDay"));
						taskAssignedMap.put("leavetype", taskAssigned.get("leavetype"));
						taskAssignedMap.put("cancellationReason", taskAssigned.get("cancellationReason"));
						taskAssignedMap.put("departmentName", taskAssigned.get("departmentName"));
						taskAssignedMap.put("departmentId", taskAssigned.get("departmentId"));
						taskAssignedMap.put("reasonDescription", taskAssigned.get("reasonDescription"));
						taskAssignedMap.put("roleId", taskAssigned.get("roleId"));
					}

//	                taskAssignedMap.putAll(taskAssigned);
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

	@GetMapping("/employeeleave/dashboad")
	public ResponseEntity<?> getAssignedDetails(@RequestParam(required = true) String Dashboad) {

		try {
			if ("employeeleave".equals(Dashboad)) {
				List<Map<String, Object>> tasks = repo.getAllProjectWork();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();

					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(taskAssigned);

					if (taskAssigned.get("employeeId") != null) {
						String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employeeId") + "."
								+ fileExtension;
						taskAssignedMap.put("profile", imageUrl);
						taskAssignedMap.put("id", taskAssigned.get("employeeId"));
						taskAssignedMap.put("employeeId", taskAssigned.get("employeeId"));
						taskAssignedMap.put("userName", taskAssigned.get("emp_name"));
						taskAssignedMap.put("date", taskAssigned.get("date"));
						taskAssignedMap.put("roleName", taskAssigned.get("roleName"));
						taskAssignedMap.put("toDate", taskAssigned.get("toDate"));
						taskAssignedMap.put("reason", taskAssigned.get("reason"));
						taskAssignedMap.put("employeeLeaveId", taskAssigned.get("employeeLeaveId"));
						taskAssignedMap.put("totalDay", taskAssigned.get("totalDay"));
						taskAssignedMap.put("leavetype", taskAssigned.get("leavetype"));
						taskAssignedMap.put("cancellationReason", taskAssigned.get("cancellationReason"));
						taskAssignedMap.put("departmentName", taskAssigned.get("departmentName"));
						taskAssignedMap.put("departmentId", taskAssigned.get("departmentId"));
						taskAssignedMap.put("reasonDescription", taskAssigned.get("reasonDescription"));
						taskAssignedMap.put("roleId", taskAssigned.get("roleId"));
					} else if (taskAssigned.get("traineeId") != null) {
						String imageUrl = "training/" + randomNumber + "/" + taskAssigned.get("traineeId");
						taskAssignedMap.put("profile", imageUrl);
						taskAssignedMap.put("id", taskAssigned.get("traineeId"));
						taskAssignedMap.put("traineeId", taskAssigned.get("traineeId"));
						taskAssignedMap.put("userName", taskAssigned.get("userName"));
						taskAssignedMap.put("date", taskAssigned.get("date"));
						taskAssignedMap.put("roleName", taskAssigned.get("roleName"));
						taskAssignedMap.put("toDate", taskAssigned.get("toDate"));
						taskAssignedMap.put("reason", taskAssigned.get("reason"));
						taskAssignedMap.put("employeeLeaveId", taskAssigned.get("employeeLeaveId"));
						taskAssignedMap.put("totalDay", taskAssigned.get("totalDay"));
						taskAssignedMap.put("leavetype", taskAssigned.get("leavetype"));
						taskAssignedMap.put("cancellationReason", taskAssigned.get("cancellationReason"));
						taskAssignedMap.put("departmentName", taskAssigned.get("departmentName"));
						taskAssignedMap.put("departmentId", taskAssigned.get("departmentId"));
						taskAssignedMap.put("reasonDescription", taskAssigned.get("reasonDescription"));
						taskAssignedMap.put("roleId", taskAssigned.get("roleId"));
					}

//		                taskAssignedMap.putAll(taskAssigned);
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

	@GetMapping("/employeeleave/dashboad1")
	public List<Map<String, Object>> AllWorksyyyy(@RequestParam(required = true) String Dashboad) {
		try {
			if ("employeeleave".equals(Dashboad)) {
				return repo.getAllProjectWorkaa();
			} else {
				// Handle the case when the provided departmentParam is not supported
				throw new IllegalArgumentException("The provided employeeleave is not supported.");
			}
		} catch (Exception e) {
			e.printStackTrace(); // Log the error for debugging
			// Handle the exception and return an appropriate response
			return Collections.emptyList(); // Or handle the exception appropriately
		}
	}

	@GetMapping("/employeeleave/PL")
	public ResponseEntity<?> getTaskAssignedDetailsp(@RequestParam(required = true) String PL) {
		try {
			if ("employeeleave".equals(PL)) {
				List<Map<String, Object>> tasks = repo.getAllProjectWorkaaggg();
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

//	@GetMapping("/employeeleave/PL")
//	public List<Map<String, Object>> AllWorksyyyyuk(@RequestParam(required = true) String PL) {
//		try {
//			if ("employeeleave".equals(PL)) {
//				return repo.getAllProjectWorkaaggg();
//			} else {
//				
//				throw new IllegalArgumentException("The provided employeeleave is not supported.");
//			}
//		} catch (Exception e) {
//			e.printStackTrace(); 
//			
//			return Collections.emptyList(); 
//		}
//	}
	@GetMapping("/employeeleave/departmant/{id}")
	public ResponseEntity<Object> getHrInterviewDetails11(@PathVariable("id") Long departmentId) {
		repo.getAllProjectWorkout(departmentId);
		return ResponseEntity.ok(repo.getAllProjectWorkout(departmentId));
	}

	@GetMapping("/employeeleave1/{id}")
	public ResponseEntity<Object> getHrInterviewDetails(@PathVariable("id") long employee_id) {
		List<Map<String, Object>> hrInterviews = repo.Allemployeeleave(employee_id);
		return ResponseEntity.ok(hrInterviews);
	}

	@GetMapping("/employeeleave/{id}/{roleId}")
	public ResponseEntity<Object> getHrInterviewDetailsWithId(@PathVariable("id") long id,
			@PathVariable("roleId") long roleId) {
		try {
			List<Map<String, Object>> training = repo.getAllProjectWorktraningWithTrainee(id, roleId);
			Map<String, List<Map<String, Object>>> trainingGroupMap = training.stream()
					.collect(Collectors.groupingBy(action -> String.valueOf(action.get("employeeLeaveId"))));

			List<Map<String, Object>> emp = repo.getAllProjectWorktraningWithEmployee(id, roleId);
			Map<String, List<Map<String, Object>>> empGroupMap = emp.stream()
					.collect(Collectors.groupingBy(action -> String.valueOf(action.get("employeeLeaveId"))));

			if (!training.isEmpty() && training.get(0).get("traineeId") != null
					&& training.get(0).get("roleId") != null) {

				List<Map<String, Object>> trainingList = new ArrayList<>();
				for (Entry<String, List<Map<String, Object>>> trainingLoop : trainingGroupMap.entrySet()) {
					for (Map<String, Object> entry : trainingLoop.getValue()) {
						Map<String, Object> trainingMap = new HashMap<>();
						trainingMap.putAll(entry);
						trainingList.add(trainingMap);
					}
				}

				return ResponseEntity.ok(trainingList);
			} else if (!emp.isEmpty() && emp.get(0).get("employeeId") != null && emp.get(0).get("roleId") != null) {

				List<Map<String, Object>> empList = new ArrayList<>();
				for (Entry<String, List<Map<String, Object>>> empLoop : empGroupMap.entrySet()) {
					for (Map<String, Object> entry : empLoop.getValue()) {
						Map<String, Object> empMap = new HashMap<>();
						empMap.putAll(entry);
						empList.add(empMap);
					}
				}

				return ResponseEntity.ok(empList);
			} else {
				String errorMessage = "The Id does not exist";
				return ResponseEntity.ok(Collections.emptyList());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", "An internal server error occurred"));
		}
	}

	@PostMapping("/employeeleave/manager")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndYearOrMonth(
			@RequestBody Map<String, Object> requestBody) {

		if (!requestBody.containsKey("choose")) {
			return ResponseEntity.badRequest().build();
		}

		String choose = requestBody.get("choose").toString();

		switch (choose) {
		case "date":
			if (requestBody.containsKey("startDate") && requestBody.containsKey("endDate")) {
				LocalDate startDate = LocalDate.parse(requestBody.get("startDate").toString(),
						DateTimeFormatter.ISO_DATE);
				LocalDate endDate = LocalDate.parse(requestBody.get("endDate").toString(), DateTimeFormatter.ISO_DATE);

				List<Map<String, Object>> images = repo.getAllReceiptBetweenDate(startDate, endDate);
				List<Map<String, Object>> imageResponses = new ArrayList<>();

				for (Map<String, Object> image : images) {
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

		case "year":
			if (requestBody.containsKey("year")) {
				String year = requestBody.get("year").toString();
				List<Map<String, Object>> leaveData = repo.getAllLeaveYear(year);
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
				List<Map<String, Object>> leaveData1 = repo.getAllleave(monthName);
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

	@PostMapping("/employeeleave/trainee/manager")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndYearOrMonthtrainee(
			@RequestBody Map<String, Object> requestBody) {

		if (!requestBody.containsKey("choose")) {
			return ResponseEntity.badRequest().build();
		}

		String choose = requestBody.get("choose").toString();

		switch (choose) {
		case "date":
			if (requestBody.containsKey("startDate") && requestBody.containsKey("endDate")) {
				LocalDate startDate = LocalDate.parse(requestBody.get("startDate").toString(),
						DateTimeFormatter.ISO_DATE);
				LocalDate endDate = LocalDate.parse(requestBody.get("endDate").toString(), DateTimeFormatter.ISO_DATE);

				List<Map<String, Object>> images = repo.getAllReceiptBetweenDateTrainee(startDate, endDate);
				List<Map<String, Object>> imageResponses = new ArrayList<>();

				for (Map<String, Object> image : images) {
					int randomNumber = generateRandomNumber();
					String imageUrl1 = "training/" + randomNumber + "/" + image.get("traineeId");
					Map<String, Object> imageResponse = new HashMap<>();
					imageResponse.put("profile", imageUrl1);

					imageResponse.putAll(image);
					imageResponses.add(imageResponse);
				}
				return ResponseEntity.ok(imageResponses);
			}

			break;

		case "year":
			if (requestBody.containsKey("year")) {
				String year = requestBody.get("year").toString();
				List<Map<String, Object>> leaveData = repo.getAllLeaveYearTrainee(year);
				List<Map<String, Object>> imageResponses = new ArrayList<>();

				for (Map<String, Object> image : leaveData) {
					int randomNumber = generateRandomNumber();
					String imageUrl1 = "training/" + randomNumber + "/" + image.get("traineeId");
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
				List<Map<String, Object>> leaveData1 = repo.getAllleavetrainee(monthName);
				List<Map<String, Object>> imageResponses = new ArrayList<>();

				for (Map<String, Object> image : leaveData1) {
					int randomNumber = generateRandomNumber();
					String imageUrl1 = "training/" + randomNumber + "/" + image.get("traineeId");
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

	@PostMapping("/employeeleave/datesmonth")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndYearOrMonthGJK(
			@RequestBody Map<String, Object> requestBody) {

		if (!requestBody.containsKey("choose") || !requestBody.containsKey("employeeId")
				|| !requestBody.containsKey("roleId")) {
			return ResponseEntity.badRequest().build();
		}

		String choose = requestBody.get("choose").toString();
		long employeeId = Long.parseLong(requestBody.get("employeeId").toString());
		long roleId = Long.parseLong(requestBody.get("roleId").toString());

		switch (choose) {
		case "date":
			if (requestBody.containsKey("startDate") && requestBody.containsKey("endDate")) {
				LocalDate startDate = LocalDate.parse(requestBody.get("startDate").toString(),
						DateTimeFormatter.ISO_DATE);
				LocalDate endDate = LocalDate.parse(requestBody.get("endDate").toString(), DateTimeFormatter.ISO_DATE);
				return ResponseEntity.ok(repo.getAllReceiptBetweenDate1(startDate, endDate, employeeId, roleId));
			}
			break;

		case "year":
			if (requestBody.containsKey("year")) {
				String year = requestBody.get("year").toString();
				List<Map<String, Object>> leaveData = repo.getAllLeaveYear1(year, employeeId, roleId);
				return ResponseEntity.ok(leaveData);
			}
			break;

		case "month":
			if (requestBody.containsKey("monthName")) {
				String monthName = requestBody.get("monthName").toString();
				List<Map<String, Object>> leaveData1 = repo.getAllleave1(monthName, employeeId, roleId);
				return ResponseEntity.ok(leaveData1);
			}
			break;

		default:
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.badRequest().build();
	}

	@PostMapping("/employeeleave/trainee")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweentrainee(
			@RequestBody Map<String, Object> requestBody) {

		if (!requestBody.containsKey("choose") || !requestBody.containsKey("traineeId")
				|| !requestBody.containsKey("roleId")) {
			return ResponseEntity.badRequest().build();
		}

		String choose = requestBody.get("choose").toString();
		long employeeId = Long.parseLong(requestBody.get("traineeId").toString());
		long roleId = Long.parseLong(requestBody.get("roleId").toString());

		switch (choose) {
		case "date":
			if (requestBody.containsKey("startDate") && requestBody.containsKey("endDate")) {
				LocalDate startDate = LocalDate.parse(requestBody.get("startDate").toString(),
						DateTimeFormatter.ISO_DATE);
				LocalDate endDate = LocalDate.parse(requestBody.get("endDate").toString(), DateTimeFormatter.ISO_DATE);
				return ResponseEntity
						.ok(repo.getAllReceiptBetweenDatetraineeId(startDate, endDate, employeeId, roleId));
			}
			break;

		case "year":
			if (requestBody.containsKey("year")) {
				String year = requestBody.get("year").toString();
				List<Map<String, Object>> leaveData = repo.getAllLeaveYeartraineeId(year, employeeId, roleId);
				return ResponseEntity.ok(leaveData);
			}
			break;

		case "month":
			if (requestBody.containsKey("monthName")) {
				String monthName = requestBody.get("monthName").toString();
				List<Map<String, Object>> leaveData1 = repo.getAllleavetraineeId(monthName, employeeId, roleId);
				return ResponseEntity.ok(leaveData1);
			}
			break;

		default:
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.badRequest().build();
	}

	@GetMapping("/employee/trainee/dashboard/{id}/{roleId}")
	public ResponseEntity<Object> getHrInterviewDetailsWithIdTrainee(@PathVariable("id") long id,
			@PathVariable("roleId") long roleId) {
		List<Map<String, Object>> training = repo.getAllProjectWorktraningWithTraineeEmployee(id, roleId);
		List<Map<String, Object>> emp = repo.getAllProjectWorktraningWithTraineeleave(id, roleId);

		if (!training.isEmpty()) {
			return ResponseEntity.ok(training);
		} else if (!emp.isEmpty()) {
			return ResponseEntity.ok(emp);
		} else {
			String errorMessage = "No data found for the provided ID and Role ID combination";
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", errorMessage));
		}
	}

}
