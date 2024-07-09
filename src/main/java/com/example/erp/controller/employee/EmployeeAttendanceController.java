package com.example.erp.controller.employee;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.example.erp.entity.employee.Assest;
import com.example.erp.entity.employee.EmployeeAttendance;
import com.example.erp.entity.employee.EmployeeLeave;
import com.example.erp.repository.employee.EmployeeAttendanceRepository;
import com.example.erp.service.employee.EmployeeAttendanceService;
import com.example.erp.service.employee.EmployeeLeaveService;

import java.net.InetAddress;
import java.sql.Date;
import java.time.Duration;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;

@RestController
@CrossOrigin
public class EmployeeAttendanceController {

	@Autowired
	private EmployeeAttendanceService service;

	@Autowired
	private EmployeeAttendanceRepository attendanceRepository;

	@Autowired
	private EmployeeLeaveService employeeLeaveService;

	@GetMapping("/attendance")
	public ResponseEntity<?> getUser1(@RequestParam(required = true) String attendance) {
		try {
			if ("employeeattendance".equals(attendance)) {
				Iterable<EmployeeAttendance> employeeAttendanceList = service.listAll1();
				return ResponseEntity.ok(employeeAttendanceList);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("The provided attendance parameter is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while retrieving employee attendance");
		}
	}

	@PostMapping(value = "/attendance/save")
	public ResponseEntity<?> saveMember(@RequestBody EmployeeAttendance attendance) {
		try {

			Long employeeIdObject = attendance.getEmployeeId();
			Long traineeIdObject = attendance.getTraineeId();		
			long defaultObj = 0L;  
			long traineeId = traineeIdObject != null ? traineeIdObject : employeeIdObject != null ? employeeIdObject : defaultObj;
			long employeeId = employeeIdObject != null ? employeeIdObject : traineeIdObject != null ? traineeIdObject : defaultObj;


			
			Date date = new Date(System.currentTimeMillis());

			Map<String, Object> empLeave = employeeLeaveService.getAllEmpLeave(employeeId, date);
			Map<String, Object> trainingLeave = employeeLeaveService.getAllTrainingLeave(traineeId, date);

			if (!empLeave.isEmpty() && empLeave.get("employee_id") != null && empLeave.get("all_dates") != null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Employee is Already in Leave");
			}else if (!trainingLeave.isEmpty() && trainingLeave.get("trainee_id") != null && trainingLeave.get("all_dates") != null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Trainee is Already in Leave");
			}
			
			InetAddress localhost = InetAddress.getLocalHost();
			String ipAddress = localhost.getHostAddress();
			attendance.setIpAddress(ipAddress);
			attendance.setInDate(LocalDate.now());
			LocalTime localTime = LocalTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
			String formattedTime = localTime.format(formatter);
			attendance.setInTime(formattedTime);

			Optional<EmployeeAttendance> existingAttendance;

			if (attendance.getEmployeeId() != null) {
				existingAttendance = attendanceRepository.findByEmployeeIdAndInDate(attendance.getEmployeeId(),
						attendance.getInDate());
			} else if (attendance.getTraineeId() != null) {
				existingAttendance = attendanceRepository.findByTraineeIdAndInDate(attendance.getTraineeId(),
						attendance.getInDate());
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Both employeeId and traineeId are null. Please provide either employeeId or traineeId.");
			}

			if (existingAttendance.isPresent()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Attendance entry already exists for "
								+ (attendance.getEmployeeId() != null ? "employee ID " + attendance.getEmployeeId()
										: "trainee ID " + attendance.getTraineeId())
								+ " on date " + attendance.getInDate() + ".");
			} else {
				if ("PunchIn".equals(attendance.getAttendance())) {
					attendance.setPunchIn(true);
				} else {
					attendance.setPunchIn(false);
				}
				attendanceRepository.save(attendance);
				System.out.println("Saving attendance: " + attendance);
				String employeeAttId = attendance.getEmployeeAttId() != null ? attendance.getEmployeeAttId().toString()
						: "N/A";
				return ResponseEntity.ok("Attendance entry saved successfully. EmployeeAttId: " + employeeAttId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving attendance: " + e.getMessage());
		}
	}


	@PutMapping("/attendance/edit/{id}")
	public ResponseEntity<?> updateAttendance(@PathVariable("id") Long attendanceId,
			@RequestBody EmployeeAttendance attendance) {
		try {
			EmployeeAttendance existingAttendance = service.getById(attendanceId);
			if (existingAttendance == null) {
				return ResponseEntity.notFound().build();
			}

			Long employeeId = existingAttendance.getEmployeeId();
			Long traineeId = existingAttendance.getTraineeId();

			if (existingAttendance.isPunchOut()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Attendance entry for "
								+ (employeeId != null ? "employee ID " + employeeId : "trainee ID " + traineeId)
								+ " already exists.");
			}

			existingAttendance.setAttendance(attendance.getAttendance());
			if (("PunchOut").equals(attendance.getAttendance())) {
				existingAttendance.setPunchOut(true);

				LocalTime inTime = LocalTime.parse(existingAttendance.getInTime(),
						DateTimeFormatter.ofPattern("hh:mm a"));
				LocalTime outTime = LocalTime.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
				String formattedTime = outTime.format(formatter);
				existingAttendance.setOutTime(formattedTime);

				Duration duration = Duration.between(inTime, outTime);

				long minutes = duration.toMinutes();
				long hours = minutes / 60;
				minutes = minutes % 60;
				String formattedDuration = String.format("%02d:%02d", hours, minutes);
				existingAttendance.setWorkingHour(formattedDuration);
			}
			service.save(existingAttendance);
			return ResponseEntity.ok(existingAttendance);
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Trainee ID is null.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

//	@PutMapping("/attendance/edit/{id}")
//	public ResponseEntity<?> updateAssest(@PathVariable("id") Long attendanceId,
//			@RequestBody EmployeeAttendance attendance) {
//		try {
//			EmployeeAttendance existingAttendance = service.getById(attendanceId);
//			if (existingAttendance == null) {
//				return ResponseEntity.notFound().build();
//			}
//
//			long employeeId = existingAttendance.getEmployeeId();
//			long traineeId = existingAttendance.getTraineeId();
//			if (existingAttendance.isPunchOut()) {
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//						.body("Attendance entry for employee ID " + employeeId + " already exists.");
//			}
//			
//
//			existingAttendance.setAttendance(attendance.getAttendance());
//			if (("PunchOut").equals(attendance.getAttendance())) {
//				existingAttendance.setPunchOut(true);
//
//				LocalTime inTime = LocalTime.parse(existingAttendance.getInTime(),
//						DateTimeFormatter.ofPattern("hh:mm a"));
//				LocalTime outTime = LocalTime.now();
//				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
//				String formattedTime = outTime.format(formatter);
//				existingAttendance.setOutTime(formattedTime);
//
//				Duration duration = Duration.between(inTime, outTime);
//
//				long minutes = duration.toMinutes();
//				long hours = minutes / 60;
//				minutes = minutes % 60;
//				String formattedDuration = String.format("%02d:%02d", hours, minutes);
//				existingAttendance.setWorkingHour(formattedDuration);
//			}
//			service.save(existingAttendance);
//			return ResponseEntity.ok(existingAttendance);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//	}

//	@PutMapping("/attendance/demo/edit/{id}")
//	public ResponseEntity<String> updateAttendance1(@PathVariable Long id,
//			@RequestBody EmployeeAttendance updatedAttendance) {
//		try {
//			LocalTime localTime = LocalTime.now();
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
//			String formattedTime = localTime.format(formatter);
//			Optional<EmployeeAttendance> existingAttendance = attendanceRepository.findById(id);
//			if (existingAttendance.isPresent()) {
//				EmployeeAttendance oldAttendance = existingAttendance.get();
//				String inTime = oldAttendance.getInTime();
//				String outTime = updatedAttendance.getOutTime();
//				oldAttendance.setPunchOut(updatedAttendance.isPunchOut());
//				oldAttendance.setAttendance("PunchOut");
//				oldAttendance.setOutTime(formattedTime);
//				DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
//				LocalTime inTimeLocal = LocalTime.parse(inTime, timeFormatter);
//				LocalTime outTimeLocal;
//				try {
//					outTimeLocal = LocalTime.parse(outTime, timeFormatter);
//				} catch (DateTimeParseException e) {
//					e.printStackTrace();
//					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//							.body("Error parsing outTime: " + e.getMessage());
//				}
//				Duration duration = Duration.between(inTimeLocal, outTimeLocal);
//				long minutes = duration.toMinutes();
//				long hours = minutes / 60;
//				minutes = minutes % 60;
//				String workingHour = String.format("%02d:%02d", hours, minutes);
//				oldAttendance.setWorkingHour(workingHour);
//				attendanceRepository.save(oldAttendance);
//				System.out.println("Updated attendance: " + oldAttendance);
//				return ResponseEntity.ok("Attendance entry updated successfully.");
//			} else {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND)
//						.body("Attendance entry with ID " + id + " not found.");
//			}
//		} catch (DateTimeParseException e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error parsing time: " + e.getMessage());
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Error updating attendance: " + e.getMessage());
//		}
//	}

	private Long parseLong(Object obj) {
		if (obj instanceof Number) {
			return ((Number) obj).longValue();
		} else if (obj instanceof String) {
			try {
				return Long.parseLong((String) obj);
			} catch (NumberFormatException e) {
				return null; // Handle the case where parsing fails
			}
		} else {
			return null; // Handle the case where the object is neither Number nor String
		}
	}

	private Integer parseInt(Object obj) {
		if (obj instanceof Number) {
			return ((Number) obj).intValue();
		} else if (obj instanceof String) {
			try {
				return Integer.parseInt((String) obj);
			} catch (NumberFormatException e) {
				return null; // Handle the case where parsing fails
			}
		} else {
			return null; // Handle the case where the object is neither Number nor String
		}
	}

	@PostMapping("/attendance/month")
	public ResponseEntity<?> getAllMemberDetailsByMemberId(@RequestBody Map<String, Object> requestBody) {
		try {
			Object memberIdObject = requestBody.get("employee_id");
			Object traineeIdObject = requestBody.get("trainee_id");
			Object yearObject = requestBody.get("year");
			Object roleIdObject = requestBody.get("role_id");

			if ((memberIdObject == null && traineeIdObject == null) || yearObject == null || roleIdObject == null) {
				return ResponseEntity.badRequest().build();
			}

			Long memberId = parseLong(memberIdObject);
			Long traineeId = parseLong(traineeIdObject);
			Integer year = parseInt(yearObject);
			Long roleId = parseLong(roleIdObject);

			if ((memberId == null && traineeId == null) || year == null || roleId == null) {
				return ResponseEntity.badRequest().build();
			}

			String month = (String) requestBody.get("month");

			System.out.println("Request Parameters:");
			System.out.println("memberId: " + memberId);
			System.out.println("traineeId: " + traineeId);
			System.out.println("roleId: " + roleId);
			System.out.println("year: " + year);
			System.out.println("month: " + month);

			List<Map<String, Object>> attendanceDetails;

			if (memberId != null) {
				attendanceDetails = attendanceRepository.Allfilter(memberId, roleId, month, year);
			} else {
				attendanceDetails = attendanceRepository.Allfiltert(traineeId, roleId, month, year);
			}
			System.out.println("Attendance Details: " + attendanceDetails);

			if (!attendanceDetails.isEmpty()) {
				return ResponseEntity.ok(attendanceDetails);
			} else {
				System.out.println("No Content - Attendance Details is empty.");
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("No attendance details found for the specified criteria.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	

	@GetMapping("/attendance/view/today")
	public ResponseEntity<?> getTaskAssignedDetailsp(@RequestParam(required = true) String attendance) {
		try {
			if ("attendance".equals(attendance)) {
				List<Map<String, Object>> tasks = attendanceRepository.ALLOverattendance();
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
				String errorMessage = "Invalid value for 'resignationsParam'. Expected 'resignationsview'.";
				return ResponseEntity.badRequest().body(Collections.singletonMap("error", errorMessage));
			}
		} catch (Exception e) {
			String errorMessage = "Error occurred while retrieving task assigned details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
		}
	}

	@GetMapping("/attendance/current/{employee_id}")
	public List<Map<String, Object>> getAllMemberDetailsByMemberId1(@PathVariable Long employee_id) {
		return attendanceRepository.Allfilter2(employee_id);
	}

//	@GetMapping("/attendance/{employeeId}/{roleId}")
//	public List<Map<String, Object>> getAllMemberDetailsByMemberId8(@PathVariable Long employeeId,@PathVariable Long roleId) {
//		return attendanceRepository.AttendanceforEmployeeId1(employeeId,roleId);
//	}

	@GetMapping("/attendance/{id}/{roleId}")
	public ResponseEntity<Object> getHrInterviewDetailsWithId(@PathVariable("id") long id,
			@PathVariable("roleId") long roleId) {
		List<Map<String, Object>> training = attendanceRepository.Attendancefortrainee(id, roleId);
		List<Map<String, Object>> emp = attendanceRepository.AttendanceforEmployeeId1(id, roleId);

		if (!training.isEmpty() && training.get(0).get("trainee_id") != null
				&& training.get(0).get("role_id") != null) {
			return ResponseEntity.ok(training);
		} else if (!emp.isEmpty() && emp.get(0).get("employee_id") != null && emp.get(0).get("role_id") != null) {
			return ResponseEntity.ok(emp);
		} else {
			String errorMessage = "The Id does not exist";
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

	@GetMapping("/attendance/today/{id}/{roleId}")
	public ResponseEntity<?> getHrInterviewDetailssWithId(@PathVariable("id") long id,
			@PathVariable("roleId") long roleId) {
		List<Map<String, Object>> training = attendanceRepository.AttendanceforEmployeeIdtrainee(id, roleId);
		Map<String, List<Map<String, Object>>> trainingGroupMap = training.stream()
				.collect(Collectors.groupingBy(action -> String.valueOf(action.get("trainee_id"))));

		List<Map<String, Object>> emp = attendanceRepository.AttendanceforEmployeeId(id, roleId);
		Map<String, List<Map<String, Object>>> empGroupMap = emp.stream()
				.collect(Collectors.groupingBy(action -> String.valueOf(action.get("employee_id"))));

		if (!training.isEmpty() && training.get(0).get("trainee_id") != null
				&& training.get(0).get("role_id") != null) {
			Map<String, Object> trainingMap = new HashMap<>();
			for (Entry<String, List<Map<String, Object>>> trainingLoop : trainingGroupMap.entrySet()) {
				for (Map<String, Object> entry : trainingLoop.getValue()) {
					if (training.get(0).get("total_working_hour") == null) {
						trainingMap.put("trainee_id", entry.get("trainee_id"));
						trainingMap.put("weekly_working_hour", entry.get("weekly_working_hour"));
						trainingMap.put("monthly_working_hour", entry.get("monthly_working_hour"));
						trainingMap.put("to_day", entry.get("to_day"));
						trainingMap.put("role_id", entry.get("role_id"));

					} else {
						trainingMap.put("trainee_id", entry.get("trainee_id"));
						trainingMap.put("weekly_working_hour", entry.get("weekly_working_hour"));
						trainingMap.put("monthly_working_hour", entry.get("monthly_working_hour"));
						trainingMap.put("to_day", entry.get("total_working_hour"));
						trainingMap.put("role_id", entry.get("role_id"));
					}
				}
			}

			return ResponseEntity.ok(trainingMap);
		} else if (!emp.isEmpty() && emp.get(0).get("employee_id") != null && emp.get(0).get("role_id") != null) {
			Map<String, Object> empMap = new HashMap<>();
			for (Entry<String, List<Map<String, Object>>> empLoop : empGroupMap.entrySet()) {
				for (Map<String, Object> entry : empLoop.getValue()) {
					if (emp.get(0).get("total_working_hour") == null) {
						empMap.put("employee_id", entry.get("employee_id"));
						empMap.put("weekly_working_hour", entry.get("weekly_working_hour"));
						empMap.put("monthly_working_hour", entry.get("monthly_working_hour"));
						empMap.put("to_day", entry.get("to_day"));
						empMap.put("role_id", entry.get("role_id"));

					} else {
						empMap.put("employee_id", entry.get("employee_id"));
						empMap.put("weekly_working_hour", entry.get("weekly_working_hour"));
						empMap.put("monthly_working_hour", entry.get("monthly_working_hour"));
						empMap.put("to_day", entry.get("total_working_hour"));
						empMap.put("role_id", entry.get("role_id"));
					}
				}
			}

			return ResponseEntity.ok(empMap);
		} else {
			String errorMessage = "The Id does not exist";
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

//	
//	@GetMapping("/attendance/today/{employeeId}")
//	public Map<String, Object> getAllMemberDetails(@PathVariable Long employeeId) {
//		return attendanceRepository.AttendanceforEmployeeId(employeeId);
//	}

//	@GetMapping("/attendance/dashboard/{employeeId}")
//	public List<Map<String, Object>> getAllMemberDetailsemployee(@PathVariable Long employeeId) {
//		return attendanceRepository.AttendanceForEmployeeId123(employeeId);
//	}

	@GetMapping("/attendance/dashboard/{id}/{roleId}")
	public ResponseEntity<Object> getHrInterviewDetails(@PathVariable("id") long id,
			@PathVariable("roleId") long roleId) {
		List<Map<String, Object>> training = attendanceRepository.AttendanceforEmployeeIdtraineedashboard(id, roleId);
		List<Map<String, Object>> emp = attendanceRepository.AttendanceForEmployeeId123(id, roleId);

		if (!training.isEmpty() && training.get(0).get("trainee_id") != null
				&& training.get(0).get("role_id") != null) {
			return ResponseEntity.ok(training);
		} else if (!emp.isEmpty() && emp.get(0).get("employee_id") != null && emp.get(0).get("role_id") != null) {
			return ResponseEntity.ok(emp);
		} else {
			String errorMessage = "The Id does not exist";
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

//	@GetMapping("/attendance/employees/{employeeId}")
//	public List<Map<String, Object>> getAllAttendanceDetails(@PathVariable Long employeeId) {
//	    return attendanceRepository.attendanceForEmployeeIdDD(employeeId);
//	}


	@GetMapping("/attendance/employees/{id}/{roleId}")
	public ResponseEntity<Object> getAllAttendanceDetails(@PathVariable("id") long id,
			@PathVariable("roleId") long roleId) {
		List<Map<String, Object>> training = attendanceRepository.attendanceForTraineeIdAndRoleId(id, roleId);
		List<Map<String, Object>> emp = attendanceRepository.attendanceForEmployeeIdAndRoleId(id, roleId);

		if (!training.isEmpty() && training.get(0).get("trainee_id") != null
				&& training.get(0).get("role_id") != null) {
			return ResponseEntity.ok(training);
		} else if (!emp.isEmpty() && emp.get(0).get("employee_id") != null && emp.get(0).get("role_id") != null) {
			return ResponseEntity.ok(emp);
		} else {
			String errorMessage = "The Id does not exist";
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", errorMessage));
		}

	   

	}

	@GetMapping("/attendance/date")
	public List<Map<String, Object>> getAllMemberDetailsByMemberId3(@RequestParam(required = true) String date) {
		try {
			if ("date".equals(date)) {
				return attendanceRepository.Allfilter3();
			} else {

				throw new IllegalArgumentException("The provided date parameter is not supported.");
			}
		} catch (Exception e) {
			e.printStackTrace();

			return Collections.emptyList();
		}
	}

	@GetMapping("/attendance/current/absentlist")
	public List<Map<String, Object>> getAllMemberDetailsByMemberId3absentlist(
			@RequestParam(required = true) String absentlist) {
		try {
			if ("absentlist".equals(absentlist)) {
				return attendanceRepository.Allfilter3();
			} else {

				throw new IllegalArgumentException("The provided date parameter is not supported.");
			}
		} catch (Exception e) {
			e.printStackTrace();

			return Collections.emptyList();
		}
	}

	@GetMapping("/attendance/view/trainee")
	public ResponseEntity<?> getTaskAssignedDetailsptraineed(@RequestParam(required = true) String trainee) {
		try {
			if ("attendance".equals(trainee)) {
				List<Map<String, Object>> tasks = attendanceRepository.GoodAllWorkstrainee();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();

					int randomNumber = generateRandomNumber();

					String imageUrl = "training/" + randomNumber + "/" + taskAssigned.get("traineeId");

					taskAssignedMap.put("profile", imageUrl);
					taskAssignedMap.putAll(taskAssigned);
					taskList.add(taskAssignedMap);
				}
				return ResponseEntity.ok(taskList);
			} else {
				String errorMessage = "Invalid value for 'resignationsParam'. Expected 'resignationsview'.";
				return ResponseEntity.badRequest().body(Collections.singletonMap("error", errorMessage));
			}
		} catch (Exception e) {
			String errorMessage = "Error occurred while retrieving task assigned details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
		}
	}

	@GetMapping("/attendance/current/presentlist")
	public List<Map<String, Object>> getAllMemberDetailsByMemberId3absentlistpr(
			@RequestParam(required = true) String presentlist) {
		try {
			if ("presentlist".equals(presentlist)) {
				return attendanceRepository.Allfilter3();
			} else {

				throw new IllegalArgumentException("The provided date parameter is not supported.");
			}
		} catch (Exception e) {
			e.printStackTrace();

			return Collections.emptyList();
		}
	}

	@GetMapping("/attendance/dashboard/percentage")
	public List<Map<String, Object>> getAllMemberDetailsByMemberIdNotcome(
			@RequestParam(required = true) String percentage) {
		try {
			if ("dashboard".equals(percentage)) {
				return attendanceRepository.getAllAvarageData();
			} else {

				throw new IllegalArgumentException("The provided date parameter is not supported.");
			}
		} catch (Exception e) {
			e.printStackTrace();

			return Collections.emptyList();
		}
	}
	
	@GetMapping("/attendance/month/{id}/{roleId}")
	public List<Map<String, Object>> getAllMemberDetailsByMemberId8(@PathVariable Long id,@PathVariable Long roleId) {
		return attendanceRepository.AttendanceforEmployeeId1Traineee(id,roleId);
	}

	@GetMapping("/attendance/month/count")
	public List<Map<String, Object>> getAllMemberDetailsBy(@RequestParam(required = true) String attendance) {
		try {
			if ("count".equals(attendance)) {
				List<Map<String, Object>> employeeDetails = attendanceRepository.GoodAllWorks22();
				List<Map<String, Object>> employeeResponses = new ArrayList<>();

				for (Map<String, Object> employeeDetail : employeeDetails) {
					int randomNumber = generateRandomNumber(); // Replace with your implementation
					String fileExtension = getFileExtensionForImage(employeeDetail); // Replace with your implementation
					String imageUrl = "profile/" + randomNumber + "/" + employeeDetail.get("employee_id") + "."
							+ fileExtension;

					Map<String, Object> employeeResponse = new HashMap<>();
					employeeResponse.put("employeeId", employeeDetail.get("employee_id"));
//	                employeeResponse.put("departmentId", employeeDetail.get("department_id"));
//	                employeeResponse.put("designationId", employeeDetail.get("designation_id"));
					employeeResponse.put("departmentName", employeeDetail.get("department_name"));
					employeeResponse.put("userName", employeeDetail.get("user_name"));
					employeeResponse.put("userId", employeeDetail.get("user_id"));
					employeeResponse.put("profile", imageUrl);
					employeeResponse.put("presentDays", employeeDetail.get("present_days"));
					employeeResponse.put("totalDays", employeeDetail.get("total_days"));
					employeeResponse.put("absentDays", employeeDetail.get("absent_days"));
					employeeResponse.put("workingHour", employeeDetail.get("workingHours"));

					employeeResponses.add(employeeResponse);
				}

				return employeeResponses;
			} else {
				throw new IllegalArgumentException("The provided attendance parameter is not supported.");
			}
		} catch (Exception e) {
			e.printStackTrace(); // Log the exception properly in a production environment
			return Collections.emptyList();
		}
	}

	@GetMapping("/attendance/view")
	public List<Map<String, Object>> INeedList(@RequestParam(required = true) String view) {
		try {
			if ("attendance".equals(view)) {
				return attendanceRepository.GoodAllWorks();
			} else {

				throw new IllegalArgumentException("The provided attendance parameter is not supported.");
			}
		} catch (Exception e) {
			e.printStackTrace();

			return Collections.emptyList();
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
//	@PostMapping("/attendance/manager")
//	public List<Map<String, Object>> getAllVoucherBetweenDates(@RequestBody Map<String, Object> requestBody) {
//	    String monthName = requestBody.get("monthName").toString();
//	    String year = requestBody.get("year").toString();
//
//	    return attendanceRepository.getAllattendance(monthName, year);
//	}

	@PostMapping("/attendance/employee/monthname")
	public List<Map<String, Object>> getAllVoucherattendance(@RequestBody Map<String, Object> requestBody) {
		String monthName = requestBody.get("monthName").toString();
		String year = requestBody.get("year").toString();
		long employeeId = Long.parseLong(requestBody.get("employeeId").toString());
		long roleId = Long.parseLong(requestBody.get("roleId").toString());

		return attendanceRepository.getAllattendanceenamoOnuThaa(monthName, year, employeeId, roleId);
	}

	@PostMapping("/attendance/trainee/monthname")
	public List<Map<String, Object>> getAllVoucherattendancetrainee(@RequestBody Map<String, Object> requestBody) {
		String monthName = requestBody.get("monthName").toString();
		String year = requestBody.get("year").toString();
		long traineeId = Long.parseLong(requestBody.get("traineeId").toString());
		long roleId = Long.parseLong(requestBody.get("roleId").toString());

		return attendanceRepository.getAllattendanceenamoOnuThaatrainee(monthName, year, traineeId, roleId);
	}

	@PostMapping("/attendance/manager")
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
		case "month":
			return handleMonthScenario33(requestBody);

		default:
			return ResponseEntity.badRequest().build();
		}
	}

	private ResponseEntity<List<Map<String, Object>>> handleGstScenario(Map<String, Object> requestBody) {
		if (requestBody.containsKey("data")) {
			String gstData = requestBody.get("data").toString();

			switch (gstData) {
			case "present":
				return handleWithTaxScenario();
			case "absent":
				return handleWithoutTaxScenario();
			default:
				return ResponseEntity.badRequest().build();
			}
		}
		return ResponseEntity.badRequest().build();
	}

	private ResponseEntity<List<Map<String, Object>>> handleWithTaxScenario() {
		List<Map<String, Object>> leaveData = attendanceRepository.Allfilterpresentlist();
		return processResponse(leaveData);
	}

	private ResponseEntity<List<Map<String, Object>>> handleWithoutTaxScenario() {
		List<Map<String, Object>> leaveData = attendanceRepository.Allfilter3absentlist();
		return processResponse(leaveData);
	}

	private ResponseEntity<List<Map<String, Object>>> handleMonthScenario(Map<String, Object> requestBody) {
		if (requestBody.containsKey("monthName") && requestBody.containsKey("year")) {

			String monthName = requestBody.get("monthName").toString();
			String year = requestBody.get("year").toString();
			List<Map<String, Object>> leaveData1 = attendanceRepository.getAllattendance(monthName, year);
			return processResponse(leaveData1);
		}
		return ResponseEntity.badRequest().build();
	}

	private ResponseEntity<List<Map<String, Object>>> handleMonthScenario33(Map<String, Object> requestBody) {
		if (requestBody.containsKey("monthName") && requestBody.containsKey("year")
				&& requestBody.containsKey("employeeId")) {

			String monthName = requestBody.get("monthName").toString();
			String year = requestBody.get("year").toString();
			Long employeeId = Long.parseLong(requestBody.get("employeeId").toString());
			List<Map<String, Object>> leaveData1 = attendanceRepository.getAllattendanceenamoOnuThaainMonth(monthName,
					year, employeeId);
			return processResponse(leaveData1);
		}
		return ResponseEntity.badRequest().build();
	}

	private ResponseEntity<List<Map<String, Object>>> processResponse(List<Map<String, Object>> leaveData) {
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

	@GetMapping("/attendance/report")
	public ResponseEntity<List<Map<String, Object>>> displayAllImages1(@RequestParam(required = true) String type) {
		try {
			if ("report".equals(type)) {
				List<Map<String, Object>> images = attendanceRepository.getAllassetsedetail();
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
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/attendance/trainee/dashboard/percentage")
	public List<Map<String, Object>> getAllMemberDetailsBygjkMemberIdNotcome(
			@RequestParam(required = true) String trainee) {
		try {
			if ("dashboard".equals(trainee)) {
				return attendanceRepository.GetTraineeAttendancepersentage();
			} else {

				throw new IllegalArgumentException("The provided date parameter is not supported.");
			}
		} catch (Exception e) {
			e.printStackTrace();

			return Collections.emptyList();
		}
	}

	@PostMapping("/attendance/manager/trainee")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndtrainee(
			@RequestBody Map<String, Object> requestBody) {

		if (!requestBody.containsKey("choose")) {
			return ResponseEntity.badRequest().build();
		}

		String choose = requestBody.get("choose").toString();

		switch (choose) {

		case "currentday":
			return handleGstScenario1(requestBody);
		case "month":
			return handleMonthScenario2(requestBody);

		case "year":
			return handleMonthScenario1(requestBody);

		default:
			return ResponseEntity.badRequest().build();
		}
	}

	private ResponseEntity<List<Map<String, Object>>> handleGstScenario1(Map<String, Object> requestBody) {
		if (requestBody.containsKey("data")) {
			String gstData = requestBody.get("data").toString();

			switch (gstData) {
			case "present":
				return handleWithTaxScenario1();
			case "absent":
				return handleWithoutTaxScenario1();
			default:
				return ResponseEntity.badRequest().build();
			}
		}
		return ResponseEntity.badRequest().build();
	}

	private ResponseEntity<List<Map<String, Object>>> handleWithTaxScenario1() {
		List<Map<String, Object>> leaveData = attendanceRepository.Allfilterpresentlisttrainee();
		return processResponse1(leaveData);
	}

	private ResponseEntity<List<Map<String, Object>>> handleWithoutTaxScenario1() {
		List<Map<String, Object>> leaveData = attendanceRepository.Allfilter3absentlisttrainee();
		return processResponse1(leaveData);
	}

	private ResponseEntity<List<Map<String, Object>>> handleMonthScenario1(Map<String, Object> requestBody) {
		if (requestBody.containsKey("monthName") && requestBody.containsKey("year")) {

			String monthName = requestBody.get("monthName").toString();
			String year = requestBody.get("year").toString();
			List<Map<String, Object>> leaveData1 = attendanceRepository.getAllattendancetrainee(monthName, year);
			return processResponse1(leaveData1);
		}
		return ResponseEntity.badRequest().build();
	}

	private ResponseEntity<List<Map<String, Object>>> handleMonthScenario2(Map<String, Object> requestBody) {
		if (requestBody.containsKey("monthName") && requestBody.containsKey("year")
				&& requestBody.containsKey("traineeId")) {

			String monthName = requestBody.get("monthName").toString();
			String year = requestBody.get("year").toString();
			Long traineeId = Long.parseLong(requestBody.get("traineeId").toString());
			List<Map<String, Object>> leaveData1 = attendanceRepository
					.getAllattendanceenamoOnuThaatraineeinmonth(monthName, year, traineeId);
			return processResponse1(leaveData1);
		}
		return ResponseEntity.badRequest().build();
	}

	private ResponseEntity<List<Map<String, Object>>> processResponse1(List<Map<String, Object>> leaveData) {
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

	@GetMapping("/attendance/report/trainee")
	public ResponseEntity<List<Map<String, Object>>> displayAllImages1trainee(
			@RequestParam(required = true) String trainee) {
		try {
			if ("report".equals(trainee)) {
				List<Map<String, Object>> images = attendanceRepository.getAllassetsedetailtrainee_id();
				List<Map<String, Object>> imageResponses = new ArrayList<>();

				for (Map<String, Object> image : images) {
					int randomNumber = generateRandomNumber();
					String imageUrl1 = "profile/" + randomNumber + "/" + image.get("traineeId");
					Map<String, Object> imageResponse = new HashMap<>();
					imageResponse.put("profile", imageUrl1);
					imageResponse.putAll(image);
					imageResponses.add(imageResponse);
				}
				return ResponseEntity.ok(imageResponses);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
