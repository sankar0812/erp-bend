package com.example.erp.controller.attendance;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;

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
import com.example.erp.entity.attendance.Attendance;
import com.example.erp.entity.attendance.AttendanceList;
import com.example.erp.repository.attendance.AttendanceListRdepository;
import com.example.erp.repository.attendance.AttendanceRepository;
import com.example.erp.service.attendance.AttendanceListService;
import com.example.erp.service.attendance.AttendanceService;

@RestController
@CrossOrigin
public class AttendanceController {

	@Autowired
	private AttendanceService service;

	@Autowired
	private AttendanceRepository attendanceRepository;

	@Autowired
	private AttendanceListRdepository attendanceListRdepository;

	@Autowired
	private AttendanceListService ser;

	@GetMapping("/attendance/list")
	public List<AttendanceList> getAllEmployeeDetails() {
		return attendanceListRdepository.findAll();
	}

	@GetMapping("/attendance1")
	public ResponseEntity<?> getUser(@RequestParam(required = true) String attendance1) {
		try {
			if ("member".equals(attendance1)) {
				Iterable<Attendance> members = service.listAll();
				return ResponseEntity.ok(members);
			} else {
				return ResponseEntity.badRequest().body("Invalid parameter value. Expected 'member'.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while retrieving members");
		}
	}

	@PostMapping(value = "/attendance1/save")
	public ResponseEntity<String> saveMember(@RequestBody Attendance attendance) {
		try {
//			Date attendanceDate = attendance.getAttendanceDate();
			List<AttendanceList> attendanceList = attendance.getAttendance();

			SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss a");

			for (AttendanceList attendanceLoop : attendanceList) {
				Optional<AttendanceList> empAndDateById = service
						.findByEmployeeIdAndDate(attendanceLoop.getEmployeeId(), attendanceLoop.getDate());
				if (empAndDateById.isPresent()) {
					String errorMessage = "A  employee and date for attendance already exists.";
					return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
				}
				if (attendanceLoop.isAttstatus()) {
					attendanceLoop.setPresent(true);
					attendanceLoop.setAbsent(false);
				} else {
					attendanceLoop.setPresent(false);
					attendanceLoop.setAbsent(true);
				}

				if (attendanceLoop.getIntime() != null && !attendanceLoop.getIntime().isEmpty()) {
					try {
						String formattedIntime = formatTime(attendanceLoop.getIntime(), timeFormatter);
						attendanceLoop.setIntime(formattedIntime);
					} catch (Exception e) {
					}
				}

				if (attendanceLoop.getOuttime() != null && !attendanceLoop.getOuttime().isEmpty()) {
					try {
						String formattedOuttime = formatTime(attendanceLoop.getOuttime(), timeFormatter);
						attendanceLoop.setOuttime(formattedOuttime);
					} catch (Exception e) {

					}
				}
			}

//			Optional<Attendance> existingAttendance = attendanceRepository.findByAttendanceDate(attendanceDate);
//			if (existingAttendance.isPresent()) {
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//						.body("Attendance for date " + attendanceDate + " already exists.");
//			} else {
			attendanceRepository.save(attendance);
			System.out.println("Saving attendance: " + attendance);
//			}

			long attendanceId = attendance.getAttendanceId();
			return ResponseEntity.ok("Attendance saved successfully. Attendance ID: " + attendanceId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving attendance: " + e.getMessage());
		}
	}

	private String formatTime(String time, SimpleDateFormat formatter) throws Exception {
		SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm:ss a");
		java.util.Date date = inputFormat.parse(time);
		return formatter.format(date);
	}

	@PutMapping("/attendance1/edit/{id}")
	public ResponseEntity<?> updateAttendanceList(@PathVariable("id") Long id,
			@RequestBody AttendanceList updatedAttendanceList) {
		try {
			AttendanceList existingAttendanceList = ser.findById(id);
			if (existingAttendanceList == null) {
				return ResponseEntity.notFound().build();
			}

			existingAttendanceList.setEmployeeId(updatedAttendanceList.getEmployeeId());
			existingAttendanceList.setAttstatus(updatedAttendanceList.isAttstatus());
			existingAttendanceList.setSection(updatedAttendanceList.getSection());
			existingAttendanceList.setIntime(updatedAttendanceList.getIntime());
			existingAttendanceList.setOuttime(updatedAttendanceList.getOuttime());
			ser.save(existingAttendanceList);
			return ResponseEntity.ok(existingAttendanceList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/attendance/date/edit/{id}/{date}")
	public ResponseEntity<?> updateAttendanceListWithDate(@PathVariable("id") Long employeeId,
			@PathVariable("date") Date date, @RequestBody AttendanceList updatedAttendanceList) {
		try {
			AttendanceList existingAttendanceList = ser.findByEmployeeIdAndDate(employeeId, date);
			if (existingAttendanceList == null) {
				return ResponseEntity.notFound().build();
			}

			existingAttendanceList.setAttstatus(updatedAttendanceList.isAttstatus());
			existingAttendanceList.setIntime(updatedAttendanceList.getIntime());
			existingAttendanceList.setOuttime(updatedAttendanceList.getOuttime());
			if (updatedAttendanceList.isAttstatus() == true) {
				existingAttendanceList.setAbsent(false);
				existingAttendanceList.setPresent(true);
			} else if (updatedAttendanceList.isAttstatus() == false) {
				existingAttendanceList.setAbsent(true);
				existingAttendanceList.setPresent(false);
			}

			ser.save(existingAttendanceList);
			return ResponseEntity.ok(existingAttendanceList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/attendance13/edit/{id}")
	public ResponseEntity<?> updateAttendanceList1(@PathVariable("id") Long id,
			@RequestBody AttendanceList updatedAttendanceList) {

		try {
			SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss a");
			AttendanceList existingAttendanceList = ser.findById(id);

			if (existingAttendanceList == null) {
				return ResponseEntity.notFound().build();
			}

			existingAttendanceList.setEmployeeId(updatedAttendanceList.getEmployeeId());
			existingAttendanceList.setAttstatus(updatedAttendanceList.isAttstatus());

			existingAttendanceList.setPresent(updatedAttendanceList.isAttstatus());
			existingAttendanceList.setAbsent(!updatedAttendanceList.isAttstatus());

			existingAttendanceList.setSection(updatedAttendanceList.getSection()); // Update section directly

			if ("Full".equals(updatedAttendanceList.getSection())) {
				existingAttendanceList.setFullDay(true);
				existingAttendanceList.setHalfDay(false);
			} else if ("Half".equals(updatedAttendanceList.getSection())) {
				existingAttendanceList.setFullDay(false);
				existingAttendanceList.setHalfDay(true);
			} else {
				existingAttendanceList.setFullDay(false);
				existingAttendanceList.setHalfDay(false);
			}

			if (updatedAttendanceList.getIntime() != null && !updatedAttendanceList.getIntime().isEmpty()) {
				try {
					String formattedIntime = formatTime(updatedAttendanceList.getIntime(), timeFormatter);
					existingAttendanceList.setIntime(formattedIntime);
				} catch (Exception e) {
					e.printStackTrace();
					return ResponseEntity.badRequest().body("Error formatting intime");
				}
			}

			if (updatedAttendanceList.getOuttime() != null && !updatedAttendanceList.getOuttime().isEmpty()) {
				try {
					String formattedOuttime = formatTime(updatedAttendanceList.getOuttime(), timeFormatter);
					existingAttendanceList.setOuttime(formattedOuttime);
				} catch (Exception e) {
					e.printStackTrace();
					return ResponseEntity.badRequest().body("Error formatting outtime");
				}
			}

			ser.save(existingAttendanceList);

			return ResponseEntity.ok("Attendance list updated successfully");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating attendance list");
		}
	}

	@GetMapping("/attendance1/employee")
	public List<Map<String, Object>> getAllMemberDetails() {
		return attendanceRepository.getAllMemberDetails();
	}

	@GetMapping("/attendance1/date")
	public List<Map<String, Object>> getAllemployeeDetails() {
		return attendanceRepository.getAllemployeeDetails();
	}

	@PostMapping("/attendance11/date/list")
	public List<Map<String, Object>> getAllVoucherBetweenDates2(@RequestBody Map<String, Object> requestBody) {
		LocalDate startDate = LocalDate.parse(requestBody.get("startDate").toString(), DateTimeFormatter.ISO_DATE);

		return attendanceRepository.getAllpromotionsBetweenDates(startDate);
	}

	@GetMapping("/attendance1/absent")
	public List<Map<String, Object>> getAllemployeeDetails2() {
		return attendanceRepository.getAllemployeeDetails2();
	}

	@GetMapping("/attendance1/absent/count")
	public ResponseEntity<?> getAllemployeeDetails5(@RequestParam(required = true) String attendance1) {
		List<Map<String, Object>> memberDetails = new ArrayList<>();
		if ("absent".equals(attendance1)) {
			memberDetails = attendanceRepository.getAllemployeeDetails5();
		} else {
			String errorMessage = "The provided attendance1 is not supported.";
			return ResponseEntity.ok(errorMessage);
		}
		return ResponseEntity.ok(memberDetails);
	}

	@GetMapping("/attendance1/currentdate")
	public List<Map<String, Object>> getAllMemberDetailsByMemberByDate() {
		return attendanceRepository.getAllMemberDetailsByMemberByDate();
	}

	@GetMapping("/attendance1/percentage")
	public ResponseEntity<?> getAllMemberDetailsByMemberByDate1(@RequestParam(required = true) String attendance1) {
		List<Map<String, Object>> memberDetails = new ArrayList<>();
		if ("percentage".equals(attendance1)) {
			memberDetails = attendanceRepository.getAllMemberDetailsByMemberByDate1();
		} else {
			String errorMessage = "The provided attendance1 is not supported.";
			return ResponseEntity.ok(errorMessage);
		}
		return ResponseEntity.ok(memberDetails);
	}

	@GetMapping("/attendance1/{employeeid}")
	public List<Map<String, Object>> getAllMemberDetailsByMemberId(@PathVariable Long employeeid) {
		return attendanceRepository.getAllMemberDetailsByMemberId(employeeid);
	}

	@GetMapping("/employees/attendance")
	public ResponseEntity<?> getAllAttendanceShiftType(@RequestParam(required = true) String employeesParam,
			@RequestParam(required = true) String attendanceParam, @RequestParam(required = true) Date date) {
		try {
			if ("employees".equals(employeesParam)) {
				if ("Regular".equals(attendanceParam)) {
					List<Map<String, Object>> employeeShift = new ArrayList<>();
					List<Map<String, Object>> shiftDetails = attendanceRepository.getAllAttendanceTypeDetails(date);

					Map<String, List<Map<String, Object>>> shiftGroupMap = shiftDetails.stream()
							.collect(Collectors.groupingBy(action -> action.get("shift_type_id").toString()));

					for (Entry<String, List<Map<String, Object>>> shiftLoop : shiftGroupMap.entrySet()) {
						Map<String, Object> shiftMap = new HashMap<>();
						shiftMap.put("shiftTypeId", shiftLoop.getKey());
						shiftMap.put("shiftName", shiftLoop.getValue().get(0).get("shift_name"));

						List<Map<String, Object>> empShiftTypeList = new ArrayList<>();
						for (Map<String, Object> shiftLoop1 : shiftLoop.getValue()) {
							Map<String, Object> shiftEmpTypeMap = new HashMap<>();
							shiftEmpTypeMap.put("userName", shiftLoop1.get("user_name"));
							shiftEmpTypeMap.put("employeeId", shiftLoop1.get("employee_id"));
							shiftEmpTypeMap.put("gender", shiftLoop1.get("gender"));
							shiftEmpTypeMap.put("phoneNumber", shiftLoop1.get("phone_number"));
							shiftEmpTypeMap.put("roleType", shiftLoop1.get("role_type"));
							shiftEmpTypeMap.put("designationName", shiftLoop1.get("designation_name"));
							shiftEmpTypeMap.put("attstatus", shiftLoop1.get("attstatus"));
							shiftEmpTypeMap.put("intime", shiftLoop1.get("intime"));
							shiftEmpTypeMap.put("outtime", shiftLoop1.get("outtime"));
							shiftEmpTypeMap.put("date", shiftLoop1.get("date"));
							shiftEmpTypeMap.put("departmentName", shiftLoop1.get("department_name"));
							empShiftTypeList.add(shiftEmpTypeMap);
						}

						shiftMap.put("attendanceDetails", empShiftTypeList);
						employeeShift.add(shiftMap);
					}

					return ResponseEntity.ok().body(employeeShift);
				} else if ("Shift".equals(attendanceParam)) {
					List<Map<String, Object>> employeeShift = new ArrayList<>();
					List<Map<String, Object>> shiftDetails = attendanceRepository
							.getAllAttendanceAndShiftTypeDetails(date);

					Map<String, List<Map<String, Object>>> shiftGroupMap = shiftDetails.stream()
							.collect(Collectors.groupingBy(action -> action.get("shift_type_id").toString()));

					for (Entry<String, List<Map<String, Object>>> shiftLoop : shiftGroupMap.entrySet()) {
						Map<String, Object> shiftMap = new HashMap<>();
						shiftMap.put("shiftTypeId", shiftLoop.getKey());
						shiftMap.put("shiftName", shiftLoop.getValue().get(0).get("shift_name"));

						List<Map<String, Object>> empShiftList = new ArrayList<>();

						for (Map<String, Object> shiftLoop1 : shiftLoop.getValue()) {
							Map<String, Object> shiftTypeMap = new HashMap<>();
							shiftTypeMap.put("shiftId", shiftLoop1.get("shift_id"));
							shiftTypeMap.put("shiftType", shiftLoop1.get("shift_type"));
							shiftTypeMap.put("inTime", shiftLoop1.get("in_time"));
							shiftTypeMap.put("outTime", shiftLoop1.get("out_time"));

							List<Map<String, Object>> empShiftTypeList = new ArrayList<>();
							Map<String, Object> shiftEmpTypeMap = new HashMap<>();
							shiftEmpTypeMap.put("userName", shiftLoop1.get("user_name"));
							shiftEmpTypeMap.put("employeeId", shiftLoop1.get("employee_id"));
							shiftEmpTypeMap.put("gender", shiftLoop1.get("gender"));
							shiftEmpTypeMap.put("phoneNumber", shiftLoop1.get("phone_number"));
							shiftEmpTypeMap.put("roleType", shiftLoop1.get("role_type"));
							shiftEmpTypeMap.put("designationName", shiftLoop1.get("designation_name"));
							shiftEmpTypeMap.put("attstatus", shiftLoop1.get("attstatus"));
							shiftEmpTypeMap.put("intime", shiftLoop1.get("intime"));
							shiftEmpTypeMap.put("outtime", shiftLoop1.get("outtime"));
							shiftEmpTypeMap.put("date", shiftLoop1.get("date"));
							shiftEmpTypeMap.put("departmentName", shiftLoop1.get("department_name"));
							empShiftTypeList.add(shiftEmpTypeMap);

							shiftTypeMap.put("shiftTypeDetails", empShiftTypeList);
							empShiftList.add(shiftTypeMap);
						}

						shiftMap.put("attendanceType", empShiftList);
						employeeShift.add(shiftMap);
					}

					return ResponseEntity.ok().body(employeeShift);
				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
				}
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
