package com.example.erp.controller.eRecruitment;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.erp.PasswordMismatchException;
import com.example.erp.entity.admin.AdminLogin;
import com.example.erp.entity.admin.User;
import com.example.erp.entity.clientDetails.ClientProfile;
import com.example.erp.entity.eRecruitments.TraineeDetails;
import com.example.erp.entity.employee.Bank;
import com.example.erp.entity.employee.Complaints;
import com.example.erp.entity.employee.Department;
import com.example.erp.entity.employee.EmergencyContacts;
import com.example.erp.entity.employee.Employee;
import com.example.erp.entity.employee.FamilyInformations;
import com.example.erp.entity.employee.Personal;
import com.example.erp.entity.employee.Qualification;
import com.example.erp.entity.erecruitment.Candidate;
import com.example.erp.entity.erecruitment.Training;
import com.example.erp.entity.message.MemberList;
import com.example.erp.repository.erecruitment.TrainingRepository;
import com.example.erp.repository.message.MemberListRepository;
import com.example.erp.service.admin.AdminLoginService;
import com.example.erp.service.admin.UserService;
import com.example.erp.service.clientDetails.ClientService;
import com.example.erp.service.eRecruitment.TrainingService;
import com.example.erp.service.employee.BankService;
import com.example.erp.service.employee.DepartmentService;
import com.example.erp.service.employee.EmergencyContactsService;
import com.example.erp.service.employee.EmployeeService;
import com.example.erp.service.employee.FamilyInformationsService;
import com.example.erp.service.employee.PersonalService;
import com.example.erp.service.employee.QualificationService;
import com.example.erp.service.message.MemberListService;

@RestController
@CrossOrigin
public class TrainingController {
	@Autowired
	private TrainingService trainingService;

	@Autowired
	private TrainingRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MemberListRepository listRepository;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private MemberListService listService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private UserService userService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private AdminLoginService adminLoginService;

	@Autowired
	private BankService bankService;

	@Autowired
	private PersonalService personalService;

	@Autowired
	private EmergencyContactsService emergencyContactsService;

	@Autowired
	private QualificationService qualificationService;

	@Autowired
	private FamilyInformationsService familyInformationsService;

	@PostMapping("/training/save")
	public ResponseEntity<String> saveComplaints(@RequestParam(value = "profile", required = false) MultipartFile file,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "confirmPassword", required = false) String confirmPassword,
			@RequestParam(value = "departmentId", required = false) long departmentId,
			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "gender", required = false) String gender,
			@RequestParam(value = "dateOfBirth", required = false) Date dateofBirth,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "country", required = false) String country,
			@RequestParam(value = "maritalStatus", required = false) String maritalStatus,
			@RequestParam(value = "mobileNumber", required = false) String mobileNumber,
			@RequestParam(value = "state", required = false) String state) {
		try {

			Training existingUser = repository.findByEmail(email);
			if (existingUser != null) {
				return ResponseEntity.badRequest().body("Email is already in use. Please use a different email.");
			}

			Training existingUser1 = repository.findByMobileNumber(mobileNumber);
			if (existingUser1 != null) {
				return ResponseEntity.badRequest()
						.body("Mobile number is already in use. Please use a different mobile number.");
			}
			if (!password.equals(confirmPassword)) {
				return ResponseEntity.badRequest().body("Error: Passwords do not match.");
			}
			Optional<Training> existingmobile1 = repository.findByMobileNumberAndEmail(mobileNumber, email);
			if (existingmobile1.isPresent()) {
				return ResponseEntity.badRequest()
						.body("This Number  " + mobileNumber + "  and  " + email + "  already use");
			}

			Optional<MemberList> existingemail = listRepository.findByEmail(email);
			if (existingemail.isPresent()) {
				return ResponseEntity.badRequest().body("email with ID " + email + " already exists.");
			}

			Optional<MemberList> existingmobile = listRepository.findByPhoneNumber(mobileNumber);
			if (existingmobile.isPresent()) {
				return ResponseEntity.badRequest().body("This Number" + mobileNumber + " already exists.");
			}

//			if (emailExistsInAnyList(email)) {
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
//			}
//			if (mobileNumberInAnyList(mobileNumber)) {
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("mobileNumber already exists");
//			}
			byte[] bytes = file.getBytes();
			Blob blob = new SerialBlob(bytes);
			Training training = new Training();
			training.setStatus(true);
			training.setProfile(blob);
			training.setAddress(address);
			training.setCity(city);
			training.setConfirmPassword(confirmPassword);
			training.setCountry(country);
			training.setDateOfBirth(dateofBirth);
			training.setDepartmentId(departmentId);
			training.setEmail(email);
			training.setGender(gender);
			training.setMaritalStatus(maritalStatus);
			training.setMobileNumber(mobileNumber);
			training.setPassword(password);
			training.setState(state);
			training.setRoleId(8);
			training.setStarted(true);
			training.setTraineeStatus("started");
			training.setRoleName("Training");
			training.setUserName(userName);

//	        String hashedPassword = passwordEncoder.encode(password);
//	        training.setPassword(hashedPassword); 

			trainingService.SaveTrainee(training);
			long id = training.getTraineeId();
			MemberList list = new MemberList();
			list.setId(id);
			list.setRoleId(8);
			list.setEmail(email);
			list.setPhoneNumber(mobileNumber);
			list.setUserName(userName);
			list.setRoleType("Traning");
			list.setProfile(blob);
			listService.Save(list);

			Department requirement = departmentService.findById(departmentId);
			String departmentName = requirement.getDepartmentName();
			String departmentCode = departmentName.substring(0, 2).toUpperCase();
			String roleName = training.getRoleName();
			String roleCode = roleName.substring(0, 2).toUpperCase();
			LocalDate date = training.getStartDate();
			int year = date.getYear();
			String userId1 = roleCode + year + departmentCode + id;
			training.setUserId(userId1);
			trainingService.SaveTrainee(training);

			updateLists();
			return ResponseEntity.ok("trainee saved with id: " + training.getTraineeId());
		} catch (IOException | SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving client: " + e.getMessage());
		}
	}

	private List<User> userDetail;
	private List<ClientProfile> clientDetails;
	private List<AdminLogin> adminDetail;
	private List<Employee> employeeDetails;

	@PostConstruct
	public void init() {
		userDetail = userService.listAll();
		clientDetails = clientService.listAll();
		adminDetail = adminLoginService.listAll();
		employeeDetails = employeeService.listAll();
	}

	private void updateLists() {
		userDetail = userService.listAll();
		clientDetails = clientService.listAll();
		adminDetail = adminLoginService.listAll();
		employeeDetails = employeeService.listAll();
	}

	private boolean emailExistsInAnyList(String email) {
		return userDetail.stream().filter(user -> user.getEmail() != null && user.getEmail().equals(email)).findAny()
				.isPresent()
				|| clientDetails.stream().filter(client -> client.getEmail() != null && client.getEmail().equals(email))
						.findAny().isPresent()
				|| adminDetail.stream().filter(admin -> admin.getEmail() != null && admin.getEmail().equals(email))
						.findAny().isPresent()
				|| employeeDetails.stream()
						.filter(trainee -> trainee.getEmail() != null && trainee.getEmail().equals(email)).findAny()
						.isPresent();
	}

	private boolean mobileNumberInAnyList(String mobileNumber) {
		return userDetail.stream()
				.filter(user -> user.getMobileNumber() != null && user.getMobileNumber().equals(mobileNumber)).findAny()
				.isPresent()
				|| clientDetails.stream().filter(
						client -> client.getMobileNumber() != null && client.getMobileNumber().equals(mobileNumber))
						.findAny().isPresent()
//	            || adminDetail.stream().filter(admin -> admin.getEmail() != null && admin.get().equals(mobileNumber)).findAny().isPresent()
				|| employeeDetails.stream().filter(
						trainee -> trainee.getPhoneNumber() != null && trainee.getPhoneNumber().equals(mobileNumber))
						.findAny().isPresent();
	}

	@GetMapping("/training")
	public ResponseEntity<?> displayAllImages(@RequestParam(required = true) String training) {
		try {
			if ("training".equals(training)) {
				List<Training> images = trainingService.listAll();

				List<Training> imageObjects = new ArrayList<>();
				for (Training image : images) {
					int randomNumber = generateRandomNumber();
					String imageUrl = "training/" + randomNumber + "/" + image.getTraineeId();
					Training imageObject = new Training();
					imageObject.setAddress(image.getAddress());
					imageObject.setTraineeId(image.getTraineeId());
					imageObject.setCity(image.getCity());
					imageObject.setCountry(image.getCountry());
					imageObject.setDateOfBirth(image.getDateOfBirth());
					imageObject.setDepartmentId(image.getDepartmentId());
					imageObject.setEmail(image.getEmail());
					imageObject.setGender(image.getGender());
					imageObject.setRoleId(image.getRoleId());
					imageObject.setRoleName(image.getRoleName());
					imageObject.setUserName(image.getUserName());
					imageObject.setMaritalStatus(image.getMaritalStatus());
					imageObject.setMobileNumber(image.getMobileNumber());
					imageObject.setStatus(image.isStatus());
					imageObject.setUrl(imageUrl);
					imageObjects.add(imageObject);
				}

				return ResponseEntity.ok().body(imageObjects);
			} else {

				String errorMessage = "The provided viewType is not supported.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}

	@PutMapping("/training/status/{id}")
	public ResponseEntity<Boolean> toggleComplaintsStatus(@PathVariable(name = "id") long id) {
		try {
			Training training = trainingService.findById(id);
			if (training != null) {
				boolean currentStatus = training.isStatus();
				training.setStatus(!currentStatus);
				trainingService.SaveTrainee(training);
			} else {
				return ResponseEntity.ok(false);
			}

			return ResponseEntity.ok(training.isStatus());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}

	@GetMapping("training/{randomNumber}/{id}")
	public ResponseEntity<ByteArrayResource> serveFile(@PathVariable("randomNumber") int randomNumber,
			@PathVariable("id") Long id) {
		Optional<Training> complaintsOptional = trainingService.getById1(id);
		if (complaintsOptional.isPresent()) {
			Training complaints = complaintsOptional.get();

			// Check if the profile Blob is not null
			if (complaints.getProfile() != null) {
				String filename = "file_" + randomNumber + "_" + id;
				byte[] fileBytes;
				try {
					fileBytes = complaints.getProfile().getBytes(1, (int) complaints.getProfile().length());
				} catch (SQLException e) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
				}

				String extension = determineFileExtension(fileBytes);
				MediaType mediaType = determineMediaType(extension);

				ByteArrayResource resource = new ByteArrayResource(fileBytes);
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(mediaType);
				headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename + "." + extension);
				return ResponseEntity.ok().headers(headers).body(resource);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ByteArrayResource(new byte[0]));
			}
		} else {
			return ResponseEntity.notFound().build();
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
		return "unknown";
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
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	@PutMapping("/training/edit/{id}")
	public ResponseEntity<?> updateTraining(@PathVariable Long id,
			@RequestParam(value = "profile", required = false) MultipartFile file,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "confirmPassword", required = false) String confirmPassword,
			@RequestParam(value = "departmentId", required = false) long departmentId,
			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "gender", required = false) String gender,
			@RequestParam(value = "dateOfBirth", required = false) Date dateofBirth,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "country", required = false) String country,
			@RequestParam(value = "maritalStatus", required = false) String maritalStatus,
			@RequestParam(value = "mobileNumber", required = false) String mobileNumber,
			@RequestParam(value = "state", required = false) String state) {
		try {
			Training existingTraining = trainingService.findById(id);

			if (existingTraining == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Training not found.");
			}

		

//			Optional<MemberList> existingMobileOptional = listRepository.findByPhoneNumber(mobileNumber);

		
			if (!password.equals(confirmPassword)) {
				return ResponseEntity.badRequest().body("Error: Passwords do not match.");
			}
			if (email != null) {
//				Optional<MemberList> existingClientOptional = listRepository.findByEmail(email);
//				if (existingClientOptional.isPresent()) {
//					MemberList existingClient = existingClientOptional.get();
//					if (existingClient.getEmail() != null && existingClient.getEmail().equals(email)) {
//						return ResponseEntity.badRequest().body("Email ID " + email + " already exists for another.");
//					}
//
//				}
				existingTraining.setEmail(email);
			}
			if (mobileNumber != null) {
//				if (existingMobileOptional.isPresent()) {
//					MemberList existingClient = existingMobileOptional.get();
//					if (existingClient.getPhoneNumber() != null && existingClient.getPhoneNumber().equals(mobileNumber)) {
//						return ResponseEntity.badRequest()
//								.body("This number " + mobileNumber + " already exists for another.");
//					}
//
//				}

				existingTraining.setMobileNumber(mobileNumber);
			}
			existingTraining.setDepartmentId(departmentId);
			existingTraining.setUserName(userName);
//			existingTraining.setEmail(email);
			existingTraining.setGender(gender);
			existingTraining.setPassword(password);
			existingTraining.setConfirmPassword(confirmPassword);
			existingTraining.setDateOfBirth(dateofBirth);
			existingTraining.setAddress(address);
			existingTraining.setCity(city);
			existingTraining.setCountry(country);
			existingTraining.setState(state);
			existingTraining.setMaritalStatus(maritalStatus);
//			existingTraining.setMobileNumber(mobileNumber);
			if (file != null && !file.isEmpty()) {
				byte[] bytes = file.getBytes();
				Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
				existingTraining.setProfile(blob);
			}
			if ("cancelled".equals(existingTraining.getTraineeStatus())) {
				existingTraining.setCancel(true);
				existingTraining.setCompleted(false);
				existingTraining.setStarted(false);
				existingTraining.setStatus(false);
			} else if ("completed".equals(existingTraining.getTraineeStatus())) {
				existingTraining.setCancel(false);
				existingTraining.setCompleted(true);
				existingTraining.setStarted(false);
			} else {
				existingTraining.setCancel(false);
				existingTraining.setCompleted(false);
				existingTraining.setStarted(false);
			}

			trainingService.SaveTrainee(existingTraining);

			MemberList list = new MemberList();
			Optional<MemberList> existingMember = listRepository.findByIdAndRoleId(id, 8);

			if (existingMember.isPresent()) {
				list = existingMember.get();
				if (email != null) {
					list.setEmail(email);
				}
				if (mobileNumber != null) {
					list.setPhoneNumber(mobileNumber);
				}
				list.setUserName(userName);
				list.setRoleId(8);
				list.setRoleType("Training");

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
				return ResponseEntity.ok("MemberList not found for clientId: " + id);
			}

			return ResponseEntity.ok("Training updated successfully. Training ID: " + id);
		} catch (IOException | SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating training.");
		}
	}

	@DeleteMapping("/training/delete/{id}")
	public ResponseEntity<String> deleteComplaints(@PathVariable("id") long id) {
		try {
			trainingService.deleteTrainingId(id);
			return ResponseEntity.ok("Complaints deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting Complaints: " + e.getMessage());
		}
	}

	@Autowired
	private TrainingRepository trainingRepository;

	@GetMapping("/training/view/details/{id}")
	public ResponseEntity<?> displayAllEmployeesWithDetailsWithId(@PathVariable("id") Long trainee_id) {

		try {

			List<Map<String, Object>> employeeDetails = trainingRepository.getAllEmployeesWithDetailsWithId(trainee_id);
			List<Map<String, Object>> clientMainMap = new ArrayList<>();

			for (Map<String, Object> clientDetails : employeeDetails) {
				Map<String, Object> clientMap = new HashMap<>();

				int randomNumber = generateRandomNumber();
				String imageUrl = "training/" + randomNumber + "/" + clientDetails.get("traineeId");

				clientMap.put("traineeId", clientDetails.get("traineeId"));
				clientMap.put("address", clientDetails.get("address"));
				clientMap.put("city", clientDetails.get("city"));
				clientMap.put("country", clientDetails.get("country"));
				clientMap.put("dateOfBirth", clientDetails.get("dateOfBirth"));
				clientMap.put("departmentId", clientDetails.get("departmentId"));
				clientMap.put("email", clientDetails.get("email"));
				clientMap.put("profile", imageUrl);
				clientMap.put("endDate", clientDetails.get("endDate"));
				clientMap.put("gender", clientDetails.get("gender"));
				clientMap.put("maritalStatus", clientDetails.get("maritalStatus"));
				clientMap.put("mobileNumber", clientDetails.get("mobileNumber"));
				clientMap.put("roleId", clientDetails.get("roleId"));
				clientMap.put("userName", clientDetails.get("userName"));
				clientMap.put("roleName", clientDetails.get("roleName"));
				clientMap.put("traineeStatus", clientDetails.get("traineeStatus"));
				clientMap.put("state", clientDetails.get("state"));
				clientMap.put("userId", clientDetails.get("userId"));
				clientMap.put("departmentName", clientDetails.get("departmentName"));
				clientMap.put("password", clientDetails.get("password"));
				clientMap.put("confirmPassword", clientDetails.get("confirmPassword"));
				clientMap.put("status", clientDetails.get("status"));

				clientMap.putAll(clientMap);
				return ResponseEntity.ok(clientMap);
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/training/view")
	public ResponseEntity<?> getTaskAssignedDetails(@RequestParam(required = true) String training) {
		try {
			if ("Details".equals(training)) {
				List<Map<String, Object>> Details = trainingRepository.findTrainingDetails();
				List<Map<String, Object>> taskList = new ArrayList<>();
				for (Map<String, Object> training1 : Details) {
					Map<String, Object> trainingkAssignedMap = new HashMap<>();
					int randomNumber = generateRandomNumber();
					String imageUrl = "training/" + randomNumber + "/" + training1.get("traineeId");
					trainingkAssignedMap.put("profile", imageUrl);

					trainingkAssignedMap.putAll(training1);
					taskList.add(trainingkAssignedMap);
				}
				return ResponseEntity.ok(taskList);
			} else {
				String errorMessage = "Invalid value for 'training'. Expected 'Details'.";
				return ResponseEntity.badRequest().body(Collections.singletonMap("error", errorMessage));
			}
		} catch (Exception e) {
			String errorMessage = "Error occurred while retrieving task assigned details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
		}

	}

	@PutMapping("/training/details/edit/{id}")
	public ResponseEntity<?> updateClientFormForProject(@PathVariable("id") long candidateId,
			@RequestBody Training candidate) {
		try {
			Training existingTraining = trainingService.findById(candidateId);
			if (existingTraining == null) {
				return ResponseEntity.notFound().build();
			}
			existingTraining.setTraineeStatus(candidate.getTraineeStatus());
			existingTraining.setEndDate(Date.valueOf(LocalDate.now()));
			if (existingTraining.isCompleted()) {
				String errormessage = "A trainee is moved to Employee";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errormessage);
			}
			if (existingTraining.isCancel()) {
				String errormessage = "Sorry trainee exit";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errormessage);
			}

			if ("cancelled".equals(existingTraining.getTraineeStatus())) {
				existingTraining.setCancel(true);
				existingTraining.setCompleted(false);
				existingTraining.setStarted(false);
				existingTraining.setStatus(false);
			} else if ("completed".equals(existingTraining.getTraineeStatus())) {
				existingTraining.setCancel(false);
				existingTraining.setCompleted(true);
				existingTraining.setStatus(false);
				existingTraining.setStarted(false);
			} else {
				existingTraining.setCancel(false);
				existingTraining.setCompleted(false);
				existingTraining.setStarted(false);
			}
	

			trainingService.SaveTrainee(existingTraining);

			if (existingTraining.getTraineeStatus().equals("completed")) {
				String userName = existingTraining.getUserName();
				String mobileNumber = existingTraining.getMobileNumber();
				String gender = existingTraining.getGender();
				String pass = existingTraining.getPassword();
				String copass = existingTraining.getConfirmPassword();
				String email = existingTraining.getEmail();
				Date dateOfBirth = existingTraining.getDateOfBirth();
				String address = existingTraining.getAddress();
				String city = existingTraining.getCity();
				String country = existingTraining.getCountry();
				String stst = existingTraining.getState();
				String martialStatus = existingTraining.getMaritalStatus();
				long departmentId = existingTraining.getDepartmentId();
				Blob profile = existingTraining.getProfile();

				Employee employee = new Employee();
				employee.setUserName(userName);
				employee.setGender(gender);
				employee.setCity(city);
//				employee.setPassword(pass);
//				employee.setConfirmPassword(copass);
//				employee.setEmail(email);
				employee.setDepartmentId(departmentId);
				employee.setCountry(country);
				employee.setState(stst);
				employee.setRoleId(4);
				employee.setRoleType("Employee");
				employee.setStatus(true);
				employee.setDob(dateOfBirth);
				employee.setPhoneNumber(mobileNumber);
				employee.setAddress(address);
				employee.setCompanyId(1);
				employee.setDate(LocalDate.now());
				employee.setProfile(profile);
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
					Optional<MemberList> existingMember = listRepository.findByIdAndRoleId(candidateId, 8);
					if (existingMember.isPresent()) {
					    list = existingMember.get();
					    list.setEmail(email);
				list.setId(id);
				list.setPhoneNumber(mobileNumber);
				list.setUserName(userName);
				list.setRoleId(4);
				list.setRoleType("Employee");
				list.setProfile(profile);
				listService.Save(list);
				}

				Bank bank = new Bank();
				bank.setEmployeeId(id);
				bank.setRoleId(roleId);
				bankService.save(bank);

				Personal personal = new Personal();
				personal.setMarried(martialStatus);
				personal.setEmployeeId(id);
				personal.setRoleId(roleId);
				personalService.SaveorUpdate(personal);

				EmergencyContacts contact = new EmergencyContacts();
				contact.setRoleId(roleId);
				contact.setEmployeeId(id);
				emergencyContactsService.saveOrUpdate(contact);

				FamilyInformations family = new FamilyInformations();
				family.setRoleId(roleId);
				family.setEmployeeId(id);
				familyInformationsService.SaveorUpdate(family);

				Qualification qualification = new Qualification();
				qualification.setRoleId(roleId);
				qualification.setEmployeeId(id);
				qualificationService.update(qualification);
			}

			return ResponseEntity.ok(existingTraining);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/trainee/manager")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndYearOrMonthtrainee(
			@RequestBody Map<String, Object> requestBody) {

		if (!requestBody.containsKey("choose")) {
			return ResponseEntity.badRequest().build();
		}

		String choose = requestBody.get("choose").toString();

		switch (choose) {
		case "completed":
			return processTrainingDetails(trainingRepository.findTrainingDetailscompleted());

		case "cancelled":
			return processTrainingDetails(trainingRepository.findTrainingDetailscancel());

		case "started":
			return processTrainingDetails(trainingRepository.findTrainingDetailsstarted());

		default:
			return ResponseEntity.badRequest().build();
		}
	}

	private ResponseEntity<List<Map<String, Object>>> processTrainingDetails(
			List<Map<String, Object>> trainingDetails) {
		List<Map<String, Object>> imageResponses = new ArrayList<>();

		for (Map<String, Object> image : trainingDetails) {
			int randomNumber = generateRandomNumber();
			String imageUrl = "training/" + randomNumber + "/" + image.get("traineeId");
			Map<String, Object> imageResponse = new HashMap<>();
			imageResponse.put("profile", imageUrl);
			imageResponse.putAll(image);
			imageResponses.add(imageResponse);
		}

		return ResponseEntity.ok(imageResponses);
	}

	@GetMapping("/department/trainee")
	public ResponseEntity<Object> getAllRoleByEmployee(@RequestParam(required = true) String trainee) {
		if ("Department".equals(trainee)) {
			List<Map<String, Object>> departmentList = new ArrayList<>();
			List<Map<String, Object>> departmentRole = trainingRepository.getAllRoleByEmployees();
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

}