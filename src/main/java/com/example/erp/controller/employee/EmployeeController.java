package com.example.erp.controller.employee;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.erp.entity.admin.AdminLogin;
import com.example.erp.entity.admin.User;
import com.example.erp.entity.clientDetails.ClientProfile;
import com.example.erp.entity.eRecruitments.TraineeDetails;
import com.example.erp.entity.employee.Bank;
import com.example.erp.entity.employee.Department;
import com.example.erp.entity.employee.EmergencyContacts;
import com.example.erp.entity.employee.Employee;
import com.example.erp.entity.employee.FamilyInformations;
import com.example.erp.entity.employee.Personal;
import com.example.erp.entity.employee.Qualification;
import com.example.erp.entity.erecruitment.Training;
import com.example.erp.entity.message.MemberList;
import com.example.erp.entity.payroll.SalaryTypeList;
import com.example.erp.repository.employee.BankRepository;
import com.example.erp.repository.employee.EmployeeRepository;
import com.example.erp.repository.message.MemberListRepository;
import com.example.erp.repository.payroll.SalaryTypeListRepository;
import com.example.erp.repository.project.TaskRepository;
import com.example.erp.service.admin.AdminLoginService;
import com.example.erp.service.admin.UserService;
import com.example.erp.service.clientDetails.ClientService;
import com.example.erp.service.eRecruitment.TrainingService;
import com.example.erp.service.eRecruitments.TraineeDetailsService;
import com.example.erp.service.employee.BankService;
import com.example.erp.service.employee.DepartmentService;
import com.example.erp.service.employee.EmergencyContactsService;
import com.example.erp.service.employee.EmployeeService;
import com.example.erp.service.employee.FamilyInformationsService;
import com.example.erp.service.employee.PersonalService;
import com.example.erp.service.employee.QualificationService;
import com.example.erp.service.message.MemberListService;

import ch.qos.logback.core.joran.action.Action;

@CrossOrigin
@RestController
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private BankRepository bankRepository;

	@Autowired
	private MemberListRepository listRepository;

	@Autowired
	private MemberListService listService;

	@Autowired
	private DepartmentService departmentService;

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

	@Autowired
	private SalaryTypeListRepository salaryTypeListRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private AdminLoginService adminLoginService;

	@Autowired
	private TrainingService traineeDetailsService;

	@Autowired
	private TaskRepository taskRepository;

	@GetMapping("/count/{employeeId}")
	public ResponseEntity<Map<String, List<Map<String, Object>>>> getAllDataBetweenDates(
			@PathVariable long employeeId) {
		Map<String, List<Map<String, Object>>> resultMap = new HashMap<>();
		List<Map<String, Object>> employeeResponses = new ArrayList<>();

		List<Map<String, Object>> employeeDetails = bankRepository.getAllRoleByEmployees3(employeeId);

		for (Map<String, Object> employeeDetail : employeeDetails) {
			int randomNumber = generateRandomNumber();
			String fileExtension = getFileExtensionForImage(employeeDetail);
			String imageUrl = "profile/" + randomNumber + "/" + employeeDetail.get("employee_id") + "." + fileExtension;

			Map<String, Object> employeeResponse = new HashMap<>();
			employeeResponse.put("employeeId", employeeDetail.get("employee_id"));
			employeeResponse.put("address", employeeDetail.get("address"));
			employeeResponse.put("city", employeeDetail.get("city"));
			employeeResponse.put("country", employeeDetail.get("country"));
			employeeResponse.put("date", employeeDetail.get("date"));
			employeeResponse.put("departmentId", employeeDetail.get("department_id"));
			employeeResponse.put("description", employeeDetail.get("description"));
			employeeResponse.put("designationId", employeeDetail.get("designation_id"));
			employeeResponse.put("dob", employeeDetail.get("dob"));
			employeeResponse.put("email", employeeDetail.get("email"));
			employeeResponse.put("gender", employeeDetail.get("gender"));
			employeeResponse.put("password", employeeDetail.get("password"));
			employeeResponse.put("phoneNumber", employeeDetail.get("phone_number"));
			employeeResponse.put("roleId", employeeDetail.get("role_id"));
			employeeResponse.put("state", employeeDetail.get("state"));
			employeeResponse.put("status", employeeDetail.get("status"));
			employeeResponse.put("departmentName", employeeDetail.get("department_name"));
			employeeResponse.put("userName", employeeDetail.get("user_name"));
			employeeResponse.put("userId", employeeDetail.get("user_id"));
			employeeResponse.put("profile", imageUrl);
			employeeResponses.add(employeeResponse);
		}

		resultMap.put("employee", employeeResponses);
		resultMap.put("bank", bankRepository.getAllBank(employeeId));
		resultMap.put("personal", bankRepository.getAllPersonal(employeeId));
		resultMap.put("contacts", bankRepository.getAllEmergencyContacts(employeeId));
		resultMap.put("familyInformations", bankRepository.getAllFamilyInformations(employeeId));
		resultMap.put("qualification", bankRepository.getQualifications(employeeId));

		List<Map<String, Object>> qualifications = bankRepository.getQualifications(employeeId);
		List<Map<String, Object>> qualificationResponses = new ArrayList<>();
		Map<String, List<Map<String, Object>>> qualificationsGroupMap = qualifications.stream()
				.collect(Collectors.groupingBy(action -> String.valueOf(action.get("qualification_id"))));

		for (Map.Entry<String, List<Map<String, Object>>> qualification : qualificationsGroupMap.entrySet()) {
			int resumeRandomNumber = generateRandomNumber();
			int tenRandomNumber = generateRandomNumber();
			int aadharRandomNumber = generateRandomNumber();
			int degreeRandomNumber = generateRandomNumber();
			int pannoRandomNumber = generateRandomNumber();
			int bankBookRandomNumber = generateRandomNumber();
			int twelveRandomNumber = generateRandomNumber();

			String resumeUrl = "/resumeUrl/" + resumeRandomNumber + "/"
					+ qualification.getValue().get(0).get("qualification_id");
			String tenUrl = "/tenUrl/" + tenRandomNumber + "/"
					+ qualification.getValue().get(0).get("qualification_id");
			String aadharUrl = "/aadharUrl/" + aadharRandomNumber + "/"
					+ qualification.getValue().get(0).get("qualification_id");
			String degreeUrl = "/degreeUrl/" + degreeRandomNumber + "/"
					+ qualification.getValue().get(0).get("qualification_id");
			String pannoUrl = "/pannoUrl/" + pannoRandomNumber + "/"
					+ qualification.getValue().get(0).get("qualification_id");
			String bankBookUrl = "/bankBookUrl/" + bankBookRandomNumber + "/"
					+ qualification.getValue().get(0).get("qualification_id");
			String twelveUrl = "/twelveUrl/" + twelveRandomNumber + "/"
					+ qualification.getValue().get(0).get("qualification_id");

			Map<String, Object> qualificationResponse = new HashMap<>();
			qualificationResponse.put("qualificationId", qualification.getValue().get(0).get("qualification_id"));
			qualificationResponse.put("status", qualification.getValue().get(0).get("status"));
			qualificationResponse.put("employeeId", qualification.getValue().get(0).get("employee_id"));
			qualificationResponse.put("highestQualification",
					qualification.getValue().get(0).get("highest_qualification"));
			qualificationResponse.put("aadharno", qualification.getValue().get(0).get("aadharno"));
			qualificationResponse.put("resumeurl", resumeUrl);
			qualificationResponse.put("tenUrl", tenUrl);
			qualificationResponse.put("aadharUrl", aadharUrl);
			qualificationResponse.put("degreeUrl", degreeUrl);
			qualificationResponse.put("pannoUrl", pannoUrl);
			qualificationResponse.put("bankBookUrl", bankBookUrl);
			qualificationResponse.put("twelveUrl", twelveUrl);
			qualificationResponse.put("usertName", qualification.getValue().get(0).get("user_name"));
			qualificationResponse.put("userId", qualification.getValue().get(0).get("user_id"));

			qualificationResponses.add(qualificationResponse);
		}

		resultMap.put("qualification", qualificationResponses);

		return ResponseEntity.ok(resultMap);
	}

	@GetMapping("/employees")
	public ResponseEntity<List<Employee>> displayAllImages(@RequestParam(required = true) String employeesParam) {
		try {
			if ("employees".equals(employeesParam)) {
				List<Employee> employees = employeeService.listAll1();
				List<Employee> employeeObjects = new ArrayList<>();
				for (Employee employee : employees) {

					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(employee);

					String imageUrl = "profile/" + randomNumber + "/" + employee.getEmployeeId() + "." + fileExtension;
					Employee employeeObject = new Employee();
					employeeObject.setEmployeeId(employee.getEmployeeId());
					employeeObject.setUrl(imageUrl);
					employeeObject.setAddress(employee.getAddress());
					employeeObject.setCity(employee.getCity());
					employeeObject.setCountry(employee.getCountry());

					employeeObject.setPhoneNumber(employee.getPhoneNumber());
					employeeObject.setDescription(employee.getDescription());
					employeeObject.setEmail(employee.getEmail());
					employeeObject.setPassword(employee.getPassword());
					employeeObject.setDesignationId(employee.getDesignationId());
					employeeObject.setGender(employee.getGender());
					employeeObject.setDob(employee.getDob());
					employeeObject.setState(employee.getState());
					employeeObject.setUserName(employee.getUserName());
					employeeObject.setDepartmentId(employee.getDepartmentId());
					employeeObject.setRoleId(employee.getRoleId());
					employeeObject.setStatus(employee.isStatus());
					employeeObject.setDate(employee.getDate());
					employeeObject.setUserId(employee.getUserId());
					employeeObject.setAttendanceType(employee.getAttendanceType());
					employeeObject.setShiftId(employee.getShiftId());
					employeeObject.setShiftTypeId(employee.getShiftTypeId());
					employeeObject.setRoleType(employee.getRoleType());
					employeeObjects.add(employeeObject);
				}
				return ResponseEntity.ok().body(employeeObjects);
			} else {
// Handle the case when the provided departmentParam is not supported
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
		} catch (Exception e) {
			e.printStackTrace(); // Log the error for debugging
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/employees/shift")
	public ResponseEntity<?> getAllShiftType(@RequestParam(required = true) String employeesParam,
			@RequestParam(required = true) String attendanceParam) {
		try {
			if ("employees".equals(employeesParam)) {
				if ("Regular".equals(attendanceParam)) {
					List<Map<String, Object>> employeeShift = new ArrayList<>();
					List<Map<String, Object>> shiftDetails = employeeRepository.getAllShiftTypeDetails();

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
							shiftEmpTypeMap.put("departmentName", shiftLoop1.get("department_name"));
							empShiftTypeList.add(shiftEmpTypeMap);
						}

						shiftMap.put("attendanceDetails", empShiftTypeList);
						employeeShift.add(shiftMap);
					}

					return ResponseEntity.ok().body(employeeShift);
				} else if ("Shift".equals(attendanceParam)) {
					List<Map<String, Object>> employeeShift = new ArrayList<>();
					List<Map<String, Object>> shiftDetails = employeeRepository.getAllshiftAndShiftTypeDetails();

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

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000); // Change the upper limit if needed
	}

	@GetMapping("profile/{randomNumber}/{id:.+}")
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
		Optional<Employee> optionalImage = employeeService.getEmployeeById1(imageId);
		if (optionalImage.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Employee image = optionalImage.get();

		Blob profileBlob = image.getProfile();
		if (profileBlob == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}

		byte[] imageBytes;
		try {
			imageBytes = profileBlob.getBytes(1, (int) profileBlob.length());
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		ByteArrayResource resource = new ByteArrayResource(imageBytes);
		HttpHeaders headers = new HttpHeaders();

		if ("jpg".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.IMAGE_JPEG);
		} else if ("png".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.IMAGE_PNG);
		} else {
			headers.setContentType(MediaType.IMAGE_JPEG);
		}
		return ResponseEntity.ok().headers(headers).body(resource);
	}

	private String getFileExtensionForImage(Employee image) {
		if (image == null || image.getUrl() == null || image.getUrl().isEmpty()) {
			return "jpg";
		}
		String url = image.getUrl();
		if (url.endsWith(".png")) {
			return "png";
		} else if (url.endsWith(".jpg")) {
			return "jpg";
		} else {
			return "jpg";
		}
	}

	@PostMapping("/employees/save")
	public ResponseEntity<String> addEmployeeWithImage(@RequestParam("profile") MultipartFile file,
			@RequestParam("userName") String userName, @RequestParam("gender") String gender,
			@RequestParam("country") String country, @RequestParam("email") String email,
			@RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword,
			@RequestParam("state") String state, @RequestParam("city") String city,
			@RequestParam("address") String address, @RequestParam("roleType") String roleType,
			@RequestParam("dob") Date dob, @RequestParam("description") String description,
			@RequestParam("designationId") long designationId, @RequestParam("departmentId") Long departmentId,
			@RequestParam("phoneNumber") String phoneNumber, @RequestParam("shiftId") long shiftId,
			@RequestParam("attendanceType") String attendanceType) {
		try {
			Employee existingUser = employeeRepository.findByEmail(email);
			if (existingUser != null) {
				return ResponseEntity.badRequest().body("Email is already in use. Please use a different email.");
			}
			Employee existingUser1 = employeeRepository.findByPhoneNumber(phoneNumber);
			if (existingUser1 != null) {
				return ResponseEntity.badRequest()
						.body("Mobile number is already in use. Please use a different mobile number.");
			}
			if (!password.equals(confirmPassword)) {
				return ResponseEntity.badRequest().body("Password and confirm password do not match");
			}

			Optional<MemberList> existingemail = listRepository.findByEmail(email);
			if (existingemail.isPresent()) {
				return ResponseEntity.badRequest().body("email with ID " + email + " already exists.");
			}
			Optional<Employee> existingmobile1 = employeeRepository.findByPhoneNumberAndEmail(phoneNumber, email);
			if (existingmobile1.isPresent()) {
				return ResponseEntity.badRequest()
						.body("This Number  " + phoneNumber + "  and  " + email + "  already use");
			}

			Optional<MemberList> existingmobile = listRepository.findByPhoneNumber(phoneNumber);
			if (existingmobile.isPresent()) {
				return ResponseEntity.badRequest().body("This Number" + phoneNumber + " already exists.");
			}

			byte[] bytes = file.getBytes();
			Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
			Employee employee = new Employee();
			employee.setRoleType(roleType);
			employee.setProfile(blob);
			employee.setStatus(true);
			employee.setUserName(userName);
			employee.setGender(gender);
			employee.setCountry(country);
			employee.setEmail(email);
			employee.setPassword(password);
			employee.setConfirmPassword(confirmPassword);
			employee.setState(state);
			employee.setCompanyId(1);
			employee.setCity(city);
			employee.setAddress(address);
			employee.setDob(dob);
			employee.setDescription(description);
			employee.setDesignationId(designationId);
			employee.setDepartmentId(departmentId);
			employee.setPhoneNumber(phoneNumber);
			employee.setAttendanceType(attendanceType);
			employee.setShiftId(shiftId);

			if ("Employee".equals(employee.getRoleType())) {
				employee.setRoleId(4);
			} else if ("ProjectHead".equals(employee.getRoleType())) {
				employee.setRoleId(6);
			} else if ("TL".equals(employee.getRoleType())) {
				employee.setRoleId(7);
			} else {
				employee.setRoleId(0);
			}

			if ("Regular".equals(employee.getAttendanceType())) {
				employee.setShiftTypeId(1);
				employee.setShiftId(0);
			} else if ("Shift".equals(employee.getAttendanceType())) {
				employee.setShiftTypeId(2);
			} else {
				employee.setShiftTypeId(0);
			}

			employeeService.saveOrUpdate(employee);
			Department requirement = departmentService.findById(departmentId);

			String departmentName = requirement.getDepartmentName();
			String departmentCode = departmentName.substring(0, 2).toUpperCase();
			LocalDate date = employee.getDate();
			int year = date.getYear();
			long id = employee.getEmployeeId();
			long roleId = employee.getRoleId();

			String rolename = employee.getRoleType();
			String roleCode = rolename.substring(0, 2).toUpperCase();
			String userId1 = roleCode + year + departmentCode + id;

			employee.setUserId(userId1);

			MemberList list = new MemberList();
			list.setId(id);
			list.setRoleId(roleId);
			list.setEmail(email);
			list.setPhoneNumber(phoneNumber);
			list.setUserName(userName);
			list.setRoleType(roleType);
			list.setProfile(blob);
			listService.Save(list);

			Bank bank = new Bank();
			bank.setEmployeeId(id);
			bank.setRoleId(roleId);
			bankService.save(bank);

			Personal personal = new Personal();
			personal.setEmployeeId(id);
			personal.setRoleId(roleId);
//			personal.setTel(phoneNumber);
			personalService.SaveorUpdate(personal);

			EmergencyContacts contact = new EmergencyContacts();
			contact.setEmployeeId(id);
			contact.setRoleId(roleId);
			emergencyContactsService.saveOrUpdate(contact);

			FamilyInformations family = new FamilyInformations();
			family.setEmployeeId(id);
			family.setRoleId(roleId);
			familyInformationsService.SaveorUpdate(family);

			Qualification qualification = new Qualification();
			qualification.setEmployeeId(id);
			qualification.setRoleId(roleId);
			qualificationService.update(qualification);

			return ResponseEntity.ok("Employee added successfully. Employee ID: " + employee.getEmployeeId());
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading file");
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error with SQL operation");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error occurred");
		}

	}

	private List<User> userDetail;
	private List<ClientProfile> clientDetails;
	private List<AdminLogin> adminDetail;
	private List<Training> traineeDetails;
	private List<Employee> empDetails;

	private void updateLists() {
		userDetail = userService.listAll();
		clientDetails = clientService.listAll();
		adminDetail = adminLoginService.listAll();
		traineeDetails = traineeDetailsService.listAll();
		empDetails = employeeService.listAll();
	}

	@PostConstruct
	public void init() {
		userDetail = userService.listAll();
		clientDetails = clientService.listAll();
		adminDetail = adminLoginService.listAll();
		traineeDetails = traineeDetailsService.listAll();
		empDetails = employeeService.listAll();
	}

	private boolean emailExistsInAnyList(String email) {
		return userDetail.stream().anyMatch(user -> user.getEmail().equals(email))
				|| clientDetails.stream().anyMatch(client -> client.getEmail().equals(email))
				|| adminDetail.stream().anyMatch(admin -> admin.getEmail().equals(email))
				|| traineeDetails.stream().anyMatch(trainee -> trainee.getEmail().equals(email))
				|| empDetails.stream().anyMatch(employee -> employee.getEmail().equals(email));
	}

	private boolean phoneNumberInAnyList(String phoneNumber) {
		return userDetail.stream().anyMatch(user -> user.getMobileNumber().equals(phoneNumber))
				|| clientDetails.stream().anyMatch(client -> client.getMobileNumber().equals(phoneNumber))
				|| traineeDetails.stream().anyMatch(trainee -> trainee.getMobileNumber().equals(phoneNumber))
				|| empDetails.stream().anyMatch(employee -> employee.getPhoneNumber().equals(phoneNumber));
	}

	@PutMapping("/employees/status/{id}")
	public ResponseEntity<Boolean> toggleComplaintsStatus(@PathVariable(name = "id") long id) {
		try {
			Employee complaints = employeeService.getById(id);
			if (complaints != null) {
				boolean currentStatus = complaints.isStatus();
				complaints.setStatus(!currentStatus);
				employeeService.saveOrUpdate(complaints);
			} else {
				return ResponseEntity.ok(false);
			}
			return ResponseEntity.ok(complaints.isStatus());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}

	@PutMapping("/employees/edit/{employeeId}")
	public ResponseEntity<String> updateEmployee(@PathVariable long employeeId,
			@RequestParam(value = "profile", required = false) MultipartFile file,
			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "gender", required = false) String gender,
			@RequestParam(value = "country", required = false) String country,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "confirmPassword", required = false) String confirmPassword,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "dob", required = false) Date dob,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "roleType", required = false) String roleType,
			@RequestParam(value = "designationId", required = false) Long designationId,
			@RequestParam(value = "departmentId", required = false) Long departmentId,
			@RequestParam(value = "roleId", required = false) Long roleId,
			@RequestParam(value = "phoneNumber", required = false) String phoneNumber,
			@RequestParam(value = "shiftId", required = false) Long shiftId,
			@RequestParam(value = "attendanceType", required = false) String attendanceType) {
		try {
			
			Employee employee = employeeService.getEmployeeById(employeeId);
			if (!password.equals(confirmPassword)) {
				return ResponseEntity.badRequest().body("Password and confirm password do not match");
			}
			
			if (employee == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
			}

			if (file != null) {
				byte[] bytes = file.getBytes();
				Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
				employee.setProfile(blob);
			}

		
			



	

			if (userName != null) {
				employee.setUserName(userName);
			}
			if (gender != null) {
				employee.setGender(gender);
			}
			if (country != null) {
				employee.setCountry(country);
			}
			if (email != null) {
				
//				Optional<MemberList> existingClientOptional = listRepository.findByEmail(email);
//
//				if (existingClientOptional.isPresent()) {
//					MemberList existingClient = existingClientOptional.get();
//					if (existingClient.getEmail() != null && existingClient.getEmail().equals(email)) {
//						return ResponseEntity.badRequest().body("This Email " + email + " already exists for another.");
//					}
//
//				}
				employee.setEmail(email);
			}
			if (password != null) {
				employee.setPassword(password);
			}
			if (confirmPassword != null) {
				employee.setConfirmPassword(confirmPassword);
			}
			if (state != null) {
				employee.setState(state);
			}
			if (city != null) {
				employee.setCity(city);
			}
			if (address != null) {
				employee.setAddress(address);
			}

			if (dob != null) {
				employee.setDob(dob);
			}
			if (description != null) {
				employee.setDescription(description);
			}
			if (roleType != null) {
				employee.setRoleType(roleType);
			}
			if (designationId != null) {
				employee.setDesignationId(designationId);
			}
			if (departmentId != null) {
				employee.setDepartmentId(departmentId);
			}
//			if (roleId != null) {
//				employee.setRoleId(roleId);
//			}
			if (phoneNumber != null) {
//				Optional<MemberList> existingMobileOptional = listRepository.findByPhoneNumber(phoneNumber);
//
//				if (existingMobileOptional.isPresent()) {
//					MemberList existingClient = existingMobileOptional.get();
//					if (existingClient.getPhoneNumber() != null && existingClient.getPhoneNumber().equals(phoneNumber)) {
//						return ResponseEntity.badRequest().body("This number " + phoneNumber + " already exists for another.");
//					}
//				}
				employee.setPhoneNumber(phoneNumber);
			}
			if (shiftId != null) {
				employee.setShiftId(shiftId);
			}
			if (attendanceType != null) {
				employee.setAttendanceType(attendanceType);
			}
			if ("Employee".equals(employee.getRoleType())) {
				employee.setRoleId(4);
			} else if ("ProjectHead".equals(employee.getRoleType())) {
				employee.setRoleId(6);
			} else if ("TL".equals(employee.getRoleType())) {
				employee.setRoleId(7);
			} else {
				employee.setRoleId(0);
			}
			if ("Regular".equals(employee.getAttendanceType())) {
				employee.setShiftTypeId(1);
				employee.setShiftId(0);
			} else if ("Shift".equals(employee.getAttendanceType())) {
				employee.setShiftTypeId(2);
			} else {
				employee.setShiftTypeId(0);
			}
			long roleid = employee.getRoleId();
			employeeService.saveOrUpdate(employee);

			MemberList list = new MemberList();
			Optional<MemberList> existingMember = listRepository.findByIdAndRoleId(employeeId, roleid);

			if (existingMember.isPresent()) {
				list = existingMember.get();
				if (email != null) {
				list.setEmail(email);}
				if (phoneNumber != null) {
				list.setPhoneNumber(phoneNumber);}
				list.setUserName(userName);
				list.setRoleId(roleid);
				list.setRoleType(roleType);

				if (file != null) {
					try {
						byte[] bytes = file.getBytes();
						Blob blob = new SerialBlob(bytes);
						list.setProfile(blob);
					} catch (IOException | SQLException e) {
						return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
								.body("Error updating MemberList profile: " + e.getMessage());
					}
				}

				listService.Save(list);
			} else {
				return ResponseEntity.ok("MemberList not found for clientId: " + employeeId);
			}

			return ResponseEntity.ok("Employee updated successfully. Employee ID: " + employeeId);
		} catch (IOException | SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating employee.");
		}
	}

	@DeleteMapping("/employees/delete/{id}")
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") long id) {
		try {
			employeeService.deleteById(id);
			return ResponseEntity.ok("Employee deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting Employee: " + e.getMessage());
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

	@GetMapping("/employees/view")
	public ResponseEntity<List<Map<String, Object>>> displayAllEmployeesWithDetails(
			@RequestParam(required = true) String employeesview) {
		try {
			if ("employeesview".equals(employeesview)) {
				List<Map<String, Object>> employeeDetails = employeeRepository.getAllEmployeesWithDetails();
				List<Map<String, Object>> employeeResponses = new ArrayList<>();
				for (Map<String, Object> employeeDetail : employeeDetails) {
					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(employeeDetail);
					String imageUrl = "profile/" + randomNumber + "/" + employeeDetail.get("employee_id") + "."
							+ fileExtension;
					Map<String, Object> employeeResponse = new HashMap<>();
					employeeResponse.put("employeeId", employeeDetail.get("employee_id"));
					employeeResponse.put("address", employeeDetail.get("address"));
					employeeResponse.put("city", employeeDetail.get("city"));
					employeeResponse.put("country", employeeDetail.get("country"));
					employeeResponse.put("date", employeeDetail.get("date"));
					employeeResponse.put("departmentId", employeeDetail.get("department_id"));
					employeeResponse.put("description", employeeDetail.get("description"));
					employeeResponse.put("designationId", employeeDetail.get("designation_id"));
					employeeResponse.put("dob", employeeDetail.get("dob"));
					employeeResponse.put("email", employeeDetail.get("email"));
					employeeResponse.put("gender", employeeDetail.get("gender"));
					employeeResponse.put("password", employeeDetail.get("password"));
					employeeResponse.put("confirmPassword", employeeDetail.get("confirm_password"));
					employeeResponse.put("phoneNumber", employeeDetail.get("phone_number"));
					employeeResponse.put("roleId", employeeDetail.get("role_id"));
					employeeResponse.put("roleName", employeeDetail.get("role_name"));
					employeeResponse.put("state", employeeDetail.get("state"));
					employeeResponse.put("status", employeeDetail.get("status"));
					employeeResponse.put("departmentName", employeeDetail.get("department_name"));
					employeeResponse.put("userName", employeeDetail.get("user_name"));
					employeeResponse.put("designationName", employeeDetail.get("designation_name"));
					employeeResponse.put("userId", employeeDetail.get("user_id"));
					employeeResponse.put("profile", imageUrl);
					employeeResponse.put("shiftId", employeeDetail.get("shift_id"));
					employeeResponse.put("shiftType", employeeDetail.get("shift_type"));
// employeeResponse.put("shiftTypeId", employeeDetail.get("shift_type_id"));
					employeeResponse.put("inTime", employeeDetail.get("in_time"));
					employeeResponse.put("outTime", employeeDetail.get("out_time"));
					employeeResponse.put("attendanceType", employeeDetail.get("attendance_type"));
					employeeResponses.add(employeeResponse);
				}
				return ResponseEntity.ok(employeeResponses);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/employees/inactive/view")
	public ResponseEntity<List<Map<String, Object>>> displayAllEmployeesWithDetailsiactivr(
			@RequestParam(required = true) String employeesview) {
		try {
			if ("inactive".equals(employeesview)) {
				List<Map<String, Object>> employeeDetails = employeeRepository.getAllEmployeesWithDetailsinactive();
				List<Map<String, Object>> employeeResponses = new ArrayList<>();
				for (Map<String, Object> employeeDetail : employeeDetails) {
					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(employeeDetail);
					String imageUrl = "profile/" + randomNumber + "/" + employeeDetail.get("employee_id") + "."
							+ fileExtension;
					Map<String, Object> employeeResponse = new HashMap<>();
					employeeResponse.put("employeeId", employeeDetail.get("employee_id"));
					employeeResponse.put("address", employeeDetail.get("address"));
					employeeResponse.put("city", employeeDetail.get("city"));
					employeeResponse.put("country", employeeDetail.get("country"));
					employeeResponse.put("date", employeeDetail.get("date"));
					employeeResponse.put("departmentId", employeeDetail.get("department_id"));
					employeeResponse.put("description", employeeDetail.get("description"));
					employeeResponse.put("designationId", employeeDetail.get("designation_id"));
					employeeResponse.put("dob", employeeDetail.get("dob"));
					employeeResponse.put("email", employeeDetail.get("email"));
					employeeResponse.put("gender", employeeDetail.get("gender"));
					employeeResponse.put("confirmPassword", employeeDetail.get("confirm_password"));
					employeeResponse.put("password", employeeDetail.get("password"));
					employeeResponse.put("phoneNumber", employeeDetail.get("phone_number"));
					employeeResponse.put("roleId", employeeDetail.get("role_id"));
					employeeResponse.put("roleName", employeeDetail.get("role_name"));
					employeeResponse.put("state", employeeDetail.get("state"));
					employeeResponse.put("exitType", employeeDetail.get("exit_type"));
					employeeResponse.put("status", employeeDetail.get("status"));
					employeeResponse.put("departmentName", employeeDetail.get("department_name"));
					employeeResponse.put("userName", employeeDetail.get("user_name"));
					employeeResponse.put("designationName", employeeDetail.get("designation_name"));
					employeeResponse.put("userId", employeeDetail.get("user_id"));
					employeeResponse.put("profile", imageUrl);
					employeeResponse.put("shiftId", employeeDetail.get("shift_id"));
					employeeResponse.put("shiftType", employeeDetail.get("shift_type"));
// employeeResponse.put("shiftTypeId", employeeDetail.get("shift_type_id"));
					employeeResponse.put("inTime", employeeDetail.get("in_time"));
					employeeResponse.put("outTime", employeeDetail.get("out_time"));
					employeeResponse.put("attendanceType", employeeDetail.get("attendance_type"));
					employeeResponses.add(employeeResponse);
				}
				return ResponseEntity.ok(employeeResponses);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/employees/other")
	public List<Map<String, Object>> getAllReg(@RequestParam(required = true) String employees) {
		try {
			if ("other".equalsIgnoreCase(employees)) {
				return employeeRepository.ALLOver();
			} else {
				throw new IllegalArgumentException("Invalid parameter value. 'employees' must be 'resignations'.");
			}
		} catch (Exception e) {

			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@GetMapping("/employees/year/count")
	public List<Map<String, Object>> getAllcount(@RequestParam(required = true) String employees) {
		try {
			if ("count".equalsIgnoreCase(employees)) {
				return employeeRepository.ALLCount();
			} else {
				throw new IllegalArgumentException("Invalid parameter value. 'employees' must be 'resignations'.");
			}
		} catch (Exception e) {

			e.printStackTrace();
			return Collections.emptyList();

		}
	}

	@GetMapping("/employees/department/training")
	public List<Map<String, Object>> getDeparment(@RequestParam(required = true) String employees) {
		try {
			if ("department".equalsIgnoreCase(employees)) {
				return employeeRepository.ALLDepatment();
			} else {
				throw new IllegalArgumentException("Invalid parameter value. 'employees' must be '.");
			}
		} catch (Exception e) {

			e.printStackTrace();
			return Collections.emptyList();

		}
	}

	@GetMapping("/notifications")
	public List<Map<String, Object>> getnoti(@RequestParam(required = true) String employees) {
		try {
			if ("Notifications".equalsIgnoreCase(employees)) {
				return employeeRepository.AllNotifications();
			} else {
				throw new IllegalArgumentException("Invalid parameter value. 'employees' must be 'resignations'.");
			}
		} catch (Exception e) {

			e.printStackTrace();
			return Collections.emptyList();

		}
	}

	@GetMapping("/notifications1")
	public List<Map<String, Object>> getnoti1(@RequestParam(required = true) String employees1) {
		try {
			if ("Notifications".equalsIgnoreCase(employees1)) {
				return employeeRepository.AllNotifications1();
			} else {
				throw new IllegalArgumentException("Invalid parameter value. 'employees' must be 'resignations'.");
			}
		} catch (Exception e) {

			e.printStackTrace();
			return Collections.emptyList();

		}
	}

	@GetMapping("/employees/inactive")
	public ResponseEntity<List<Map<String, Object>>> getInactiveEmployees(@RequestParam("employees") String employees) {
		try {
			if ("inactive".equalsIgnoreCase(employees)) {
				List<Map<String, Object>> employeeDetails = employeeRepository.getAllpromotionsemployees();
				List<Map<String, Object>> employeeResponses = new ArrayList<>();

				for (Map<String, Object> employeeDetail : employeeDetails) {
					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(employeeDetail);
					String imageUrl = "profile/" + randomNumber + "/" + employeeDetail.get("employee_id") + "."
							+ fileExtension;
					Map<String, Object> employeeResponse = new HashMap<>();
					employeeResponse.put("profile", imageUrl);
					employeeResponse.putAll(employeeDetail); // Fix here

					employeeResponses.add(employeeResponse);
				}

				return ResponseEntity.ok(employeeResponses);
			} else {
				throw new IllegalArgumentException("Invalid parameter value. 'employees' must be 'inactive'.");
			}
		} catch (Exception e) {
			// Log the exception or return a meaningful error response
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
		}
	}

	@GetMapping("/employees/view/{id}")
	public ResponseEntity<Map<String, Object>> displayAllEmployeesWithDetailsWithId(
			@PathVariable("id") Long employee_id) {
		try {
			List<Map<String, Object>> employeeDetails = employeeRepository
					.getAllEmployeesWithDetailsWithId(employee_id);
			Map<String, Object> employeeResponses = new HashMap<>();
			for (Map<String, Object> employeeDetail : employeeDetails) {
				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(employeeDetail);
				String imageUrl = "profile/" + randomNumber + "/" + employeeDetail.get("employee_id") + "."
						+ fileExtension;
				Map<String, Object> employeeResponse = new HashMap<>();
				employeeResponse.put("employeeId", employeeDetail.get("employee_id"));
				employeeResponse.put("address", employeeDetail.get("address"));
				employeeResponse.put("city", employeeDetail.get("city"));
				employeeResponse.put("country", employeeDetail.get("country"));
				employeeResponse.put("date", employeeDetail.get("date"));
				employeeResponse.put("departmentId", employeeDetail.get("department_id"));
				employeeResponse.put("description", employeeDetail.get("description"));
				employeeResponse.put("designationId", employeeDetail.get("designation_id"));
				employeeResponse.put("dob", employeeDetail.get("dob"));
				employeeResponse.put("email", employeeDetail.get("email"));
				employeeResponse.put("gender", employeeDetail.get("gender"));
				employeeResponse.put("password", employeeDetail.get("password"));
				employeeResponse.put("phoneNumber", employeeDetail.get("phone_number"));
				employeeResponse.put("roleId", employeeDetail.get("role_id"));
				employeeResponse.put("roleName", employeeDetail.get("role_name"));
				employeeResponse.put("exitType", employeeDetail.get("exit_type"));
				employeeResponse.put("confirmPassword", employeeDetail.get("confirm_password"));
				employeeResponse.put("state", employeeDetail.get("state"));
				employeeResponse.put("status", employeeDetail.get("status"));
				employeeResponse.put("departmentName", employeeDetail.get("department_name"));
				employeeResponse.put("userName", employeeDetail.get("user_name"));
				employeeResponse.put("designationName", employeeDetail.get("designation_name"));
				employeeResponse.put("userId", employeeDetail.get("user_id"));
				employeeResponse.put("shiftId", employeeDetail.get("shift_id"));
				employeeResponse.put("shiftType", employeeDetail.get("shift_type"));
// employeeResponse.put("shiftTypeId", employeeDetail.get("shift_type_id"));
				employeeResponse.put("inTime", employeeDetail.get("in_time"));
				employeeResponse.put("outTime", employeeDetail.get("out_time"));
				employeeResponse.put("attendanceType", employeeDetail.get("attendance_type"));
				employeeResponse.put("profile", imageUrl);
				employeeResponses.putAll(employeeResponse);
			}
			return ResponseEntity.ok(employeeResponses);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/employees/true")
	public ResponseEntity<List<Map<String, Object>>> displayAllImages2(
			@RequestParam(required = true) String trueParam) {
		try {
			if ("true".equals(trueParam)) {
				List<Map<String, Object>> employeeDetails = employeeRepository.AllEmployees();
				List<Map<String, Object>> employeeResponses = new ArrayList<>();
				for (Map<String, Object> employeeDetail : employeeDetails) {
					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(employeeDetail);
					String imageUrl = "profile/" + randomNumber + "/" + employeeDetail.get("employee_id") + "."
							+ fileExtension;
					Map<String, Object> employeeResponse = new HashMap<>();
					employeeResponse.put("employeeId", employeeDetail.get("employee_id"));
					employeeResponse.put("address", employeeDetail.get("address"));
					employeeResponse.put("city", employeeDetail.get("city"));
					employeeResponse.put("country", employeeDetail.get("country"));
					employeeResponse.put("date", employeeDetail.get("date"));
					employeeResponse.put("departmentId", employeeDetail.get("department_id"));
					employeeResponse.put("description", employeeDetail.get("description"));
					employeeResponse.put("designationId", employeeDetail.get("designation_id"));
					employeeResponse.put("dob", employeeDetail.get("dob"));
					employeeResponse.put("email", employeeDetail.get("email"));
					employeeResponse.put("gender", employeeDetail.get("gender"));
					employeeResponse.put("password", employeeDetail.get("password"));
					employeeResponse.put("phoneNumber", employeeDetail.get("phone_number"));
					employeeResponse.put("roleId", employeeDetail.get("role_id"));
					employeeResponse.put("state", employeeDetail.get("state"));
					employeeResponse.put("status", employeeDetail.get("status"));
					employeeResponse.put("departmentName", employeeDetail.get("department_name"));
					employeeResponse.put("userName", employeeDetail.get("user_name"));
					employeeResponse.put("userId", employeeDetail.get("user_id"));
					employeeResponse.put("shiftId", employeeDetail.get("shift_id"));
					employeeResponse.put("shiftType", employeeDetail.get("shift_type"));
// employeeResponse.put("shiftTypeId", employeeDetail.get("shift_type_id"));
					employeeResponse.put("inTime", employeeDetail.get("in_time"));
					employeeResponse.put("outTime", employeeDetail.get("out_time"));
					employeeResponse.put("attendanceType", employeeDetail.get("attendance_type"));
					employeeResponse.put("profile", imageUrl);
					employeeResponses.add(employeeResponse);
				}
				return ResponseEntity.ok(employeeResponses);
			} else {
// Handle the case when the provided departmentParam is not supported
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace(); // Log the error for debugging
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/employee/login")
	public ResponseEntity<String> processLogin(@RequestParam String email, @RequestParam String password) {
		Employee employee = employeeRepository.findByEmail(email);
		if (employee != null && employee.getPassword().equals(password)) {
			return ResponseEntity.ok("Login successful");
		} else {
			return ResponseEntity.badRequest().body("Invalid Email or Password");
		}
	}

	@PostMapping("/employee/changepassword")
	public ResponseEntity<String> processChangePassword(@RequestParam String email, @RequestParam String oldPassword,
			@RequestParam String newPassword) {
		Employee user = employeeRepository.findByEmail(email);
		if (user != null && user.getPassword().equals(oldPassword)) {
			user.setPassword(newPassword);
			employeeRepository.save(user);
			return ResponseEntity.ok("Password changed successfully");
		} else {
			return ResponseEntity.badRequest().body("Invalid credentials");
		}
	}

	@GetMapping("/department/role/filter/trainee")
	public ResponseEntity<Object> getAllRoleByEmployeeFilter(@RequestParam(required = true) String trainee) {

		if ("Department".equals(trainee)) {
			List<Map<String, Object>> departmentList = new ArrayList<>();
			List<Map<String, Object>> departmentRole = employeeRepository.getAllRoleByTrainee();
			Map<String, List<Map<String, Object>>> departmentGroupMap = departmentRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("department_id").toString()));

			List<SalaryTypeList> salaryTypeList = salaryTypeListRepository.findAll();

			Set<Long> employeesWithSalary = salaryTypeList.stream().map(SalaryTypeList::getTraineeId)
					.collect(Collectors.toSet());

			for (Entry<String, List<Map<String, Object>>> departmentLoop : departmentGroupMap.entrySet()) {
				String departmentId = departmentLoop.getKey();
				List<Map<String, Object>> departmentDetails = departmentLoop.getValue();

				int randomNumber = generateRandomNumber();
				String imageUrl = "training/" + randomNumber + "/" + departmentLoop.getValue().get(0).get("trainee_id");

				List<Map<String, Object>> filteredDepartmentDetails = departmentDetails.stream()
						.filter(employee -> !employeesWithSalary
								.contains(Long.parseLong(employee.get("trainee_id").toString())))
						.map(employee -> {
							Map<String, Object> modifiedEmployee = new HashMap<>(employee); // Create a new map
							modifiedEmployee.put("profile", imageUrl); // Set image_url in the new map
							return modifiedEmployee;
						}).collect(Collectors.toList());

				Map<String, Object> departmentMap = new HashMap<>();

				departmentMap.put("department_id", departmentId);
				departmentMap.put("department_name", departmentLoop.getValue().get(0).get("department_name"));
				departmentMap.put("department_details", filteredDepartmentDetails);
				departmentList.add(departmentMap);

			}

			return ResponseEntity.ok(departmentList);
		} else {
			String errorMessage = "Invalid value for 'department'. Expected 'Department'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	private String getFileExtensionForImage1(Entry<String, List<Map<String, Object>>> departmentLoop) {
		if (departmentLoop == null || departmentLoop.getValue() == null || departmentLoop.getValue().isEmpty()) {
			return "jpg";
		}

		String url = (String) departmentLoop.getValue().get(0).get("url");

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

	@GetMapping("/dashboard/priority/count/ooo")
	public ResponseEntity<?> getTaskAssignedToProjectHeadINTask(@RequestParam(required = true) String dashboard) {
		try {
			if ("priority".equals(dashboard)) {
				List<Map<String, Object>> tasks = employeeRepository.AllHighPriorityCountINtask();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();
					int randomNumber = generateRandomNumber();

					if (taskAssigned.containsKey("employee_id") && taskAssigned.get("employee_id") != null) {
						String fileExtension = getFileExtensionForImage(taskAssigned);
						String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employee_id") + "."
								+ fileExtension;
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

	@GetMapping("/department/role/filter")
	public ResponseEntity<Object> getAllRoleByEmployeeFilterDD(@RequestParam(required = true) String department) {
		if ("Department".equals(department)) {
			List<Map<String, Object>> departmentList = new ArrayList<>();
			List<Map<String, Object>> departmentRole = employeeRepository.getAllRoleByEmployees();
			Map<String, List<Map<String, Object>>> departmentGroupMap = departmentRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("department_id").toString()));

			List<SalaryTypeList> salaryTypeList = salaryTypeListRepository.findAll();

			Set<Long> employeesWithSalary = salaryTypeList.stream().map(SalaryTypeList::getEmployeeId)
					.collect(Collectors.toSet());

			for (Entry<String, List<Map<String, Object>>> departmentLoop : departmentGroupMap.entrySet()) {
				String departmentId = departmentLoop.getKey();
				List<Map<String, Object>> departmentDetails = departmentLoop.getValue();

				List<Map<String, Object>> filteredDepartmentDetails = departmentDetails.stream()
						.filter(employee -> !employeesWithSalary
								.contains(Long.parseLong(employee.get("employee_id").toString())))
						.map(employee -> {
							int employeeRandomNumber = generateRandomNumber(); // Generate a new random number for each
																				// employee
							String fileExtension = getFileExtensionForImage1(departmentLoop);
							String imageUrl = "profile/" + employeeRandomNumber + "/" + employee.get("employee_id")
									+ "." + fileExtension;

							Map<String, Object> modifiedEmployee = new HashMap<>(employee);
							modifiedEmployee.put("profile", imageUrl);
							return modifiedEmployee;
						}).collect(Collectors.toList());

				Map<String, Object> departmentMap = new HashMap<>();
				departmentMap.put("department_id", departmentId);
				departmentMap.put("department_name", departmentLoop.getValue().get(0).get("department_name"));
				departmentMap.put("department_details", filteredDepartmentDetails);
				departmentList.add(departmentMap);

			}

			return ResponseEntity.ok(departmentList);
		} else {
			String errorMessage = "Invalid value for 'department'. Expected 'Department'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@GetMapping("/department/role/project")
	public ResponseEntity<?> getAllRoleByEmployee1(@RequestParam(required = true) String department) {
		try {
			if ("project".equals(department)) {
				List<Map<String, Object>> departmentDetails = taskRepository.getALLEmpWithTaskListDetailsAndList();
				Map<String, Map<String, Map<String, List<Map<String, Object>>>>> deptGroupMap = departmentDetails
						.stream().collect(Collectors.groupingBy(action -> action.get("department_id").toString(),
								Collectors.groupingBy(action -> {
									Object employeeId = action.get("employee_id");
									return employeeId != null ? employeeId.toString() : "defaultEmployeeId";
								}, Collectors.groupingBy(action -> {
									Object traineeId = action.get("trainee_id");
									return traineeId != null ? traineeId.toString() : "defaultTraineeId";
								}, Collectors.toList()))));

				List<Map<String, Object>> departmentList = new ArrayList<>();
				for (Entry<String, Map<String, Map<String, List<Map<String, Object>>>>> deptLoop : deptGroupMap
						.entrySet()) {
					Map<String, Object> departmentMap = new HashMap<>();
					departmentMap.put("departmentId", deptLoop.getKey());

					List<Map<String, Object>> empList = new ArrayList<>();
					for (Entry<String, Map<String, List<Map<String, Object>>>> empLoop : deptLoop.getValue()
							.entrySet()) {
						Map<String, Object> empMap = new HashMap<>();
						empMap.put("employeeId", empLoop.getKey());

						List<Map<String, Object>> traineeList = new ArrayList<>();
						for (Entry<String, List<Map<String, Object>>> traineeLoop : empLoop.getValue().entrySet()) {
							Map<String, Object> traineeMap = new HashMap<>();
							traineeMap.put("traineeId", traineeLoop.getKey());
							traineeMap.put("userName", traineeLoop.getValue().get(0).get("traineeUserName"));
							traineeMap.put("userId", traineeLoop.getValue().get(0).get("traineeUserId"));
							traineeMap.put("departmentName", traineeLoop.getValue().get(0).get("department_name"));
							traineeMap.put("departmentId", deptLoop.getKey());

							empMap.put("userName", traineeLoop.getValue().get(0).get("empUserName"));
							empMap.put("userId", traineeLoop.getValue().get(0).get("empUserId"));
							empMap.put("departmentName", traineeLoop.getValue().get(0).get("department_name"));
							empMap.put("departmentId", deptLoop.getKey());

							departmentMap.put("departmentName", traineeLoop.getValue().get(0).get("department_name"));
							traineeList.add(traineeMap);

						}
						empList.add(empMap);
						departmentMap.put("traineeDetails", traineeList);
					}
					departmentMap.put("employeeDetails", empList);
					departmentList.add(departmentMap);
				}

				return ResponseEntity.ok(departmentList);
			} else {
				String errorMessage = "Invalid department. Please provide 'project' as the department.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "An unexpected error occurred.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@GetMapping("/department/role")
	public ResponseEntity<Object> getAllRoleByEmployee(@RequestParam(required = true) String department) {
		if ("Department".equals(department)) {
			List<Map<String, Object>> departmentList = new ArrayList<>();
			List<Map<String, Object>> departmentRole = employeeRepository.getAllRoleByEmployees();
			Map<String, List<Map<String, Object>>> departmentGroupMap = departmentRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("department_id").toString()));

			for (Entry<String, List<Map<String, Object>>> departmentLoop : departmentGroupMap.entrySet()) {
				Map<String, Object> departmentMap = new HashMap<>();
				departmentMap.put("departmentId", Long.parseLong(departmentLoop.getKey()));
				departmentMap.put("departmentName", departmentLoop.getValue().get(0).get("department_name"));
				List<Map<String, Object>> departmentSubList = new ArrayList<>();
				for (Map<String, Object> departmentsubLoop : departmentLoop.getValue()) {

					Map<String, Object> departmentSubMap = new HashMap<>();
					departmentSubMap.put("departmentId", departmentsubLoop.get("department_id"));
					departmentSubMap.put("departmentName", departmentsubLoop.get("department_name"));
					departmentSubMap.put("userName", departmentsubLoop.get("user_name"));
					departmentSubMap.put("userId", departmentsubLoop.get("user_id"));
					departmentSubMap.put("employeeId", departmentsubLoop.get("employee_id"));
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

	@GetMapping("/department/role/trainee")
	public ResponseEntity<Object> getAllRoleByTrainee(@RequestParam(required = true) String department) {
		if ("Department".equals(department)) {
			List<Map<String, Object>> departmentList = new ArrayList<>();
			List<Map<String, Object>> departmentRole = employeeRepository.getAllRoleByTrainee();
			Map<String, List<Map<String, Object>>> departmentGroupMap = departmentRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("department_id").toString()));

			for (Entry<String, List<Map<String, Object>>> departmentLoop : departmentGroupMap.entrySet()) {
				Map<String, Object> departmentMap = new HashMap<>();
				departmentMap.put("departmentId", Long.parseLong(departmentLoop.getKey()));
				departmentMap.put("departmentName", departmentLoop.getValue().get(0).get("department_name"));
				List<Map<String, Object>> departmentSubList = new ArrayList<>();
				for (Map<String, Object> departmentsubLoop : departmentLoop.getValue()) {

					Map<String, Object> departmentSubMap = new HashMap<>();
					departmentSubMap.put("departmentId", departmentsubLoop.get("department_id"));
					departmentSubMap.put("departmentName", departmentsubLoop.get("department_name"));
					departmentSubMap.put("userName", departmentsubLoop.get("user_name"));
					departmentSubMap.put("userId", departmentsubLoop.get("user_id"));
					departmentSubMap.put("id", departmentsubLoop.get("trainee_id"));
					departmentSubMap.put("traineeId", departmentsubLoop.get("trainee_id"));

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

	@GetMapping("/department/employees/{departmentId}")
	public ResponseEntity<Object> getAllEmployeesByDepartment(@PathVariable long departmentId) {
		try {
			List<Map<String, Object>> departmentList = new ArrayList<>();
			List<Map<String, Object>> departmentEmployees = new ArrayList<>(
					employeeRepository.getAllEmployeesByDepartment(departmentId));
			Map<String, List<Map<String, Object>>> departmentGroupMap = departmentEmployees.stream()
					.collect(Collectors.groupingBy(action -> action.get("department_id").toString()));
			for (Map.Entry<String, List<Map<String, Object>>> departmentLoop : departmentGroupMap.entrySet()) {
				Map<String, Object> departmentMap = new HashMap<>();
				departmentMap.put("department_id", departmentLoop.getKey());
				departmentMap.put("department_name", departmentLoop.getValue().get(0).get("department_name"));
				List<Map<String, Object>> employeeResponses = new ArrayList<>();
				for (Map<String, Object> employeeDetail : departmentLoop.getValue()) {

					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(employeeDetail);
					String imageUrl = "profile/" + randomNumber + "/" + employeeDetail.get("employee_id") + "."
							+ fileExtension;
					Map<String, Object> updatedEmployeeDetail = new HashMap<>(employeeDetail);
					updatedEmployeeDetail.put("profile", imageUrl);
					employeeResponses.add(updatedEmployeeDetail);
				}
				departmentMap.put("department_details", employeeResponses);
				departmentList.add(departmentMap);
			}
			return ResponseEntity.ok(departmentList);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}