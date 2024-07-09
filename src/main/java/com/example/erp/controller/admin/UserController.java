package com.example.erp.controller.admin;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.example.erp.entity.admin.AdminLogin;
import com.example.erp.entity.admin.LoginRequest;
import com.example.erp.entity.admin.User;
import com.example.erp.entity.clientDetails.ClientProfile;
import com.example.erp.entity.employee.Bank;
import com.example.erp.entity.employee.Department;
import com.example.erp.entity.employee.EmergencyContacts;
import com.example.erp.entity.employee.Employee;
import com.example.erp.entity.employee.FamilyInformations;
import com.example.erp.entity.employee.Personal;
import com.example.erp.entity.employee.Qualification;
import com.example.erp.entity.erecruitment.Training;
import com.example.erp.entity.message.MemberList;
import com.example.erp.repository.admin.AdminLoginRepository;
import com.example.erp.repository.admin.UserRepository;
import com.example.erp.repository.clientDetails.ClientRepository;
import com.example.erp.repository.employee.EmployeeRepository;
import com.example.erp.repository.erecruitment.TrainingRepository;
import com.example.erp.repository.message.MemberListRepository;
import com.example.erp.service.admin.AdminLoginService;
import com.example.erp.service.admin.UserService;
import com.example.erp.service.clientDetails.ClientService;
import com.example.erp.service.eRecruitment.TrainingService;
import com.example.erp.service.employee.BankService;
import com.example.erp.service.employee.EmergencyContactsService;
import com.example.erp.service.employee.EmployeeService;
import com.example.erp.service.employee.FamilyInformationsService;
import com.example.erp.service.employee.PersonalService;
import com.example.erp.service.employee.QualificationService;
import com.example.erp.service.message.MemberListService;

@RestController
@CrossOrigin
public class UserController {

	@Autowired
	private BankService bankService;

	@Autowired
	private ClientService clientService;

	@Autowired
	private AdminLoginService adminLoginService;

	@Autowired
	private TrainingService traineeDetailsService;

	@Autowired
	private PersonalService personalService;

	@Autowired
	private EmergencyContactsService emergencyContactsService;

	@Autowired
	private FamilyInformationsService familyInformationsService;

	@Autowired
	private QualificationService qualificationService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private UserService userService;

//	@Autowired
//	private PasswordEncoder passwordEncoder;

	@Autowired
	private MemberListRepository listRepository;
	@Autowired
	private AdminLoginRepository adminLoginRepository;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MemberListService listService;

	@Autowired
	private TrainingRepository trainingRepository;
//
//	@GetMapping("/user/withoutrole")
//	public ResponseEntity<Object> getUserDetails(@RequestParam(required = true) String user) {
//		if ("manager".equals(user)) {
//			return ResponseEntity.ok(userService.singleUser());
//		} else {
//			String errorMessage = "Invalid value for 'user'. Expected 'manager'.";
//			return ResponseEntity.badRequest().body(errorMessage);
//		}
//	}

	@PostMapping("/user/save")
	public ResponseEntity<?> addEmployeeWithImage(@RequestParam("userProfile") MultipartFile file,
			@RequestParam("username") String username, @RequestParam("address") String address,
			@RequestParam("location") String location, @RequestParam("country") String country,
			@RequestParam("email") String email, @RequestParam("password") String password,
			@RequestParam("confirmPassword") String confirmPassword, @RequestParam("city") String city,
			@RequestParam("roleType") String roleType, @RequestParam("mobileNumber") String mobileNumber) {
		try {
			User existingUser = userService.getUserByEmail(email);
			if (existingUser != null) {
				return ResponseEntity.badRequest().body("Email is already in use. Please use a different email.");
			}
			User existingUser1 = userService.getUserByMobileNumber(mobileNumber);
			if (existingUser1 != null) {
				return ResponseEntity.badRequest()
						.body("Mobile number is already in use. Please use a different mobile number.");
			}

			if (!password.equals(confirmPassword)) {
				return ResponseEntity.badRequest().body("Password and Confirm Password do not match");
			}
			Optional<User> existingmobile1 = userRepository.findByMobileNumberAndEmail(mobileNumber, email);
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

			byte[] bytes = file.getBytes();
			Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

			User user = new User();
			user.setUserProfile(blob);
			user.setStatus(true);
			user.setUsername(username);
			user.setCountry(country);
			user.setEmail(email);
			user.setCity(city);
			user.setAddress(address);
			user.setConfirmPassword(confirmPassword);
			user.setPassword(password);
			user.setMobileNumber(mobileNumber);
			user.setRoleType(roleType);
			user.setLocation(location);

			if (user.getRoleType().equals("Manager")) {
				user.setRoleId(2);
			} else if (user.getRoleType().equals("Accountant")) {
				user.setRoleId(5);
			} else if (user.getRoleType().equals("ProjectManager")) {
				user.setRoleId(10);
			} else {
				user.setRoleId(0);
			}

			userService.saveSingleUser(user);
			long id = user.getUserId();

			long roleId = user.getRoleId();

			String roleName = user.getRoleType();
			String roleCode = roleName.substring(0, 2).toUpperCase();
			LocalDate date = user.getDate();
			int year = date.getYear();
			String userId1 = roleCode + year + "ID" + id;
			user.setManagerId(userId1);

			Bank bank = new Bank();
			bank.setUserId(id);
			bank.setRoleId(roleId);
			bankService.save(bank);

			Personal personal = new Personal();
			personal.setUserId(id);
			personal.setRoleId(roleId);
//			personal.setTel(mobileNumber);
			personalService.SaveorUpdate(personal);

			EmergencyContacts contact = new EmergencyContacts();
			contact.setUserId(id);
			contact.setRoleId(roleId);
			emergencyContactsService.saveOrUpdate(contact);

			FamilyInformations family = new FamilyInformations();
			family.setUserId(id);
			family.setRoleId(roleId);
			familyInformationsService.SaveorUpdate(family);

			Qualification qualification = new Qualification();
			qualification.setUserId(id);
			qualification.setRoleId(roleId);
			qualificationService.update(qualification);

			MemberList list = new MemberList();
			list.setId(id);
			list.setEmail(email);
			list.setPhoneNumber(mobileNumber);
			list.setUserName(username);
			list.setRoleId(roleId);
			list.setRoleType(roleType);
			list.setProfile(blob);
			listService.Save(list);
			updateLists();
			return ResponseEntity.ok("user added successfully. user ID: " + id);
		} catch (IOException | SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding user.");
		}
	}

	private List<Training> traineeDetails;
	private List<ClientProfile> clientDetails;
	private List<AdminLogin> adminDetail;
	private List<Employee> employeeDetails;
	private List<User> userDetails;

	@PostConstruct
	public void init() {
		traineeDetails = traineeDetailsService.listAll();
		clientDetails = clientService.listAll();
		adminDetail = adminLoginService.listAll();
		employeeDetails = employeeService.listAll();
		userDetails = userService.listAll();
	}

	private void updateLists() {
		traineeDetails = traineeDetailsService.listAll();
		clientDetails = clientService.listAll();
		adminDetail = adminLoginService.listAll();
		employeeDetails = employeeService.listAll();
	}

	private boolean emailExistsInAnyList(String email) {
		return traineeDetails.stream().anyMatch(trainee -> trainee.getEmail().equals(email))
				|| clientDetails.stream().anyMatch(client -> client.getEmail().equals(email))
				|| adminDetail.stream().anyMatch(admin -> admin.getEmail().equals(email))
				|| employeeDetails.stream().anyMatch(employee -> employee.getEmail().equals(email))
				|| userDetails.stream().anyMatch(user -> user.getEmail().equals(email));
	}

	private boolean mobileNumberInAnyList(String mobileNumber) {
		return traineeDetails.stream().anyMatch(trainee -> trainee.getMobileNumber().equals(mobileNumber))
				|| clientDetails.stream().anyMatch(client -> client.getMobileNumber().equals(mobileNumber))
				|| employeeDetails.stream().anyMatch(employee -> employee.getPhoneNumber().equals(mobileNumber))
				|| userDetails.stream().anyMatch(user -> user.getMobileNumber().equals(mobileNumber));
	}

	@PutMapping("/user/status/{id}")
	public ResponseEntity<Boolean> toggleComplaintsStatus(@PathVariable(name = "id") long id) {
		try {
			User user = userService.getCompanyById(id);
			if (user != null) {
				boolean currentStatus = user.isStatus();
				user.setStatus(!currentStatus);
				userService.saveSingleUser(user);
			} else {
				return ResponseEntity.ok(false);
			}
			return ResponseEntity.ok(user.isStatus());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}

	@GetMapping("/user/{id}")
	public ResponseEntity<?> displayUserDetailsAndImageUrls(@PathVariable("id") Long userId) {
		List<User> users = userService.getUserById(userId);
		Map<String, Object> userDetailsWithImages = new HashMap<>();

		if (users != null && !users.isEmpty()) {
			for (User user : users) {
				Map<String, Object> userDetails = new HashMap<>();
				userDetails.put("userId", user.getUserId());
				userDetails.put("username", user.getUsername());
				userDetails.put("address", user.getAddress());
				userDetails.put("city", user.getCity());
				userDetails.put("location", user.getLocation());
				userDetails.put("country", user.getCountry());
				userDetails.put("email", user.getEmail());
				userDetails.put("password", user.getPassword());
				userDetails.put("confirmPassword", user.getConfirmPassword());
				userDetails.put("roleType", user.getRoleType());
				userDetails.put("roleId", user.getRoleId());
				userDetails.put("mobileNumber", user.getMobileNumber());
				userDetails.put("managerId", user.getManagerId());
				userDetails.put("userStatus", user.isStatus());

				// Generate image URL for this user (change this logic as needed)
				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(user);
				String imageUrl = "user/" + randomNumber + "/" + user.getUserId() + "." + fileExtension;
				userDetails.put("userProfile", imageUrl);

				userDetailsWithImages.putAll(userDetails);
			}
			return ResponseEntity.ok(userDetailsWithImages);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
		}
	}

	@GetMapping("/User")
	public ResponseEntity<List<User>> displayAllUsers(@RequestParam(required = true) String User) {
		try {
			if ("User".equals(User)) {
				List<User> users = userService.getTrueUsers();
				List<User> userObjects = new ArrayList<>();
				for (User user : users) {
					int randomNumber = generateRandomNumber(); // Implement generateRandomNumber() method
					String fileExtension = getFileExtensionForImage(user); // Implement getFileExtensionForImage()
																			// method
					String imageUrl = "user/" + randomNumber + "/" + user.getUserId() + "." + fileExtension;

					User employeeObject = new User();
					employeeObject.setUserId(user.getUserId());
					employeeObject.setUrl(imageUrl);
					employeeObject.setAddress(user.getAddress());
					employeeObject.setCity(user.getCity());
					employeeObject.setCountry(user.getCountry());
					employeeObject.setMobileNumber(user.getMobileNumber());
					employeeObject.setEmail(user.getEmail());
					employeeObject.setPassword(user.getPassword());
					employeeObject.setConfirmPassword(user.getConfirmPassword());
					employeeObject.setRoleType(user.getRoleType());
					employeeObject.setRoleId(user.getRoleId());
					employeeObject.setUsername(user.getUsername());
					employeeObject.setLocation(user.getLocation());
					employeeObject.setStatus(user.isStatus());

					userObjects.add(employeeObject);
				}
				return ResponseEntity.ok().body(userObjects);
			} else {
				// Handle the case when the provided departmentParam is not supported
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
		} catch (Exception e) {
			e.printStackTrace(); // Log the error for debugging
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/user/false")
	public ResponseEntity<List<User>> displayAllUsersFalse(@RequestParam(required = true) String User) {
		try {
			if ("false".equals(User)) {
				List<User> users = userService.getFalseUsers();
				List<User> userObjects = new ArrayList<>();
				for (User user : users) {
					int randomNumber = generateRandomNumber(); // Implement generateRandomNumber() method
					String fileExtension = getFileExtensionForImage(user); // Implement getFileExtensionForImage()
																			// method
					String imageUrl = "user/" + randomNumber + "/" + user.getUserId() + "." + fileExtension;

					User employeeObject = new User();
					employeeObject.setUserId(user.getUserId());
					employeeObject.setUrl(imageUrl);
					employeeObject.setAddress(user.getAddress());
					employeeObject.setCity(user.getCity());
					employeeObject.setCountry(user.getCountry());
					employeeObject.setMobileNumber(user.getMobileNumber());
					employeeObject.setEmail(user.getEmail());
					employeeObject.setPassword(user.getPassword());
					employeeObject.setConfirmPassword(user.getConfirmPassword());
					employeeObject.setRoleType(user.getRoleType());
					employeeObject.setRoleId(user.getRoleId());
					employeeObject.setUsername(user.getUsername());
					employeeObject.setLocation(user.getLocation());
					employeeObject.setStatus(user.isStatus());

					userObjects.add(employeeObject);
				}
				return ResponseEntity.ok().body(userObjects);
			} else {
				// Handle the case when the provided departmentParam is not supported
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
		} catch (Exception e) {
			e.printStackTrace(); // Log the error for debugging
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}

	@GetMapping("user/{randomNumber}/{id:.+}")
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
			return ResponseEntity.badRequest().build(); // Invalid image ID format
		}

		User image = userService.findUserById(imageId);
		if (image == null) {
			return ResponseEntity.notFound().build();
		}

		byte[] imageBytes;
		try {
			imageBytes = image.getUserProfile().getBytes(1, (int) image.getUserProfile().length());
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

	private String getFileExtensionForImage(User image) {
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

	@PutMapping("/user/edit/{userId}")
	public ResponseEntity<String> updateCompany(@PathVariable long userId,
			@RequestParam(value = "userProfile", required = false) MultipartFile file,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "confirmPassword", required = false) String confirmPassword,
			@RequestParam(value = "location", required = false) String location,
			@RequestParam(value = "country", required = false) String country,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "roleType", required = false) String roleType,
			@RequestParam(value = "mobileNumber", required = false) String mobileNumber) {
		try {
			User user = userService.getCompanyById(userId);

			if (user == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found.");
			}

			
			

			if (file != null && !file.isEmpty()) {
				byte[] bytes = file.getBytes();
				Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
				user.setUserProfile(blob);
			}

			if (username != null) {
				user.setUsername(username);
			}

			if (country != null) {
				user.setCountry(country);
			}

			if (email != null) {
//				Optional<MemberList> existingClientOptional = listRepository.findByEmail(email);
//
//				if (existingClientOptional.isPresent()) {
//					MemberList existingClient = existingClientOptional.get();
//					if (existingClient.getEmail() != null && existingClient.getEmail().equals(email)) {
//						return ResponseEntity.badRequest().body("Email ID " + email + " already exists for another.");
//					}
//
//				}
				user.setEmail(email);
			}

			if (location != null) {
				user.setLocation(location);
			}

			if (address != null) {
				user.setAddress(address);
			}

			if (password != null) {
				user.setPassword(password);
			}

			if (confirmPassword != null) {
				if (!password.equals(confirmPassword)) {
					return ResponseEntity.badRequest().body("Password and confirm password do not match");
				}
				user.setConfirmPassword(confirmPassword);
			}

			if (city != null) {
				user.setCity(city);
			}

			if (roleType != null) {
				user.setRoleType(roleType);
			}

			if (mobileNumber != null) {
//				Optional<MemberList> existingMobileOptional = listRepository.findByPhoneNumber(mobileNumber);
//
//				if (existingMobileOptional.isPresent()) {
//					MemberList existingClient = existingMobileOptional.get();
//					if (existingClient.getPhoneNumber() != null && existingClient.getPhoneNumber().equals(mobileNumber)) {
//						return ResponseEntity.badRequest().body("This number" + mobileNumber + " already exists for another.");
//					}
//
//				}
				user.setMobileNumber(mobileNumber);
			}
			if (user.getRoleType().equals("Manager")) {
				user.setRoleId(2);
			} else if (user.getRoleType().equals("Accountant")) {
				user.setRoleId(5);
			} else if (user.getRoleType().equals("ProjectManager")) {
				user.setRoleId(10);
			} else {
				user.setRoleId(0);
			}
			long roleid = user.getRoleId();
			userService.saveSingleUser(user);
			MemberList list = new MemberList();
			Optional<MemberList> existingMember = listRepository.findByIdAndRoleId(userId, roleid);

			if (existingMember.isPresent()) {
				list = existingMember.get();
				if (email != null) {
				list.setEmail(email);
				}
				if (mobileNumber != null) {
				list.setPhoneNumber(mobileNumber);
				}
				list.setUserName(username);
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
				return ResponseEntity.ok("MemberList not found for clientId: " + userId);
			}

			return ResponseEntity.ok("User updated successfully. user ID: " + userId);
		} catch (IOException | SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user.");
		}
	}

	private boolean emailExistsInAnyList(String email, long userIdToExclude) {
		boolean exists = clientDetails.stream().anyMatch(client -> client.getEmail().equals(email))
				|| adminDetail.stream().anyMatch(admin -> admin.getEmail().equals(email))
				|| traineeDetails.stream().anyMatch(trainee -> trainee.getEmail().equals(email))
				|| employeeDetails.stream().anyMatch(employee -> employee.getEmail().equals(email))
				|| userDetails.stream().anyMatch(user -> user.getEmail().equals(email));

		System.out.println("Email exists: " + exists); // Add this line for debugging
		return exists;
	}

	private boolean mobileNumberInAnyList(String mobileNumber, long userIdToExclude) {
		boolean exists = clientDetails.stream().anyMatch(client -> client.getMobileNumber().equals(mobileNumber))
				|| traineeDetails.stream().anyMatch(trainee -> trainee.getMobileNumber().equals(mobileNumber))
				|| employeeDetails.stream().anyMatch(employee -> employee.getPhoneNumber().equals(mobileNumber))
				|| userDetails.stream().anyMatch(user -> user.getMobileNumber().equals(mobileNumber));
		;

		System.out.println("Mobile number exists: " + exists); // Add this line for debugging
		return exists;
	}

//	@PutMapping("/user/edit/{id}")
//	public ResponseEntity<User> updateOrder(@PathVariable("id") Long userId, @RequestBody User user) {
//		try {
//			User existingUser = userService.findUserById(userId);
//			if (existingUser == null) {
//				return ResponseEntity.notFound().build();
//			}
//
//			existingUser.setAddress(user.getAddress());
//			existingUser.setCity(user.getCity());
//			existingUser.setCountry(user.getCountry());
//			existingUser.setEmail(user.getEmail());
//			existingUser.setLocation(user.getLocation());
//			existingUser.setMobileNumber(user.getMobileNumber());
//			existingUser.setUsername(user.getUsername());
//
//			userService.saveSingleUser(existingUser);
//			return ResponseEntity.ok(existingUser);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//	}

	@DeleteMapping("/user/delete/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable("id") Long userId) {
		userService.deleteUserById(userId);
		return ResponseEntity.ok("User deleted successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<?> processLogin(@RequestBody LoginRequest loginRequest) {

		String email = loginRequest.getEmail();
		String password = loginRequest.getPassword();

		User user = userRepository.findByEmail(email);
		AdminLogin admin = adminLoginRepository.findByEmail(email);
		Employee employee = employeeRepository.findByEmail(email);
		ClientProfile client = clientRepository.findByEmail(email);
		Training training = trainingRepository.findByEmail(email);

		if (user != null && user.getEmail().equals(email) && user.getPassword().equals(password) && user.isStatus()) {
			Map<String, Object> userDetails = getUserDetails(user);
			return ResponseEntity.ok(userDetails);
		} else if (admin != null && admin.getEmail().equals(email) && admin.getPassword().equals(password)) {
			Map<String, Object> adminDetails = getAdminDetails(admin);
			return ResponseEntity.ok(adminDetails);
		} else if (employee != null && employee.getEmail().equals(email) && employee.getPassword().equals(password)
				&& employee.isStatus()) {
			Map<String, Object> employeeDetails = getAllRoleDetails(employee);
			return ResponseEntity.ok(employeeDetails);
		} else if (client != null && client.getEmail().equals(email) && client.getPassword().equals(password)
				&& client.isStatus()) {
			Map<String, Object> clientDetails = getAllClientDetails(client);
			return ResponseEntity.ok(clientDetails);
		} else if (training != null && training.getEmail().equals(email) && training.getPassword().equals(password)
				&& training.isStatus()) {
			Map<String, Object> trainingDetails = getAllTrainingDetails(training);
			return ResponseEntity.ok(trainingDetails);
		} else {
			Map<String, Object> loginFailedResponse = createLoginFailedResponse();
			return ResponseEntity.badRequest().body(loginFailedResponse);
		}
	}

	private Map<String, Object> getUserDetails(User user) {
		List<Map<String, Object>> userDetails = userRepository.getManagerDetailsById(user.getUserId());
		Map<String, List<Map<String, Object>>> userGroupMap = userDetails.stream()
				.collect(Collectors.groupingBy(action -> action.get("userId").toString()));
		Map<String, Object> userDetailsMap = new HashMap<>();

		for (Entry<String, List<Map<String, Object>>> userLoop : userGroupMap.entrySet()) {
			Map<String, Object> userMap = new HashMap<>();
			int randomNumber = generateRandomNumber();
			String fileExtension = getFileExtensionForImage1(userLoop);
			String imageUrl = "user/" + randomNumber + "/" + userLoop.getValue().get(0).get("userId") + "."
					+ fileExtension;
			userMap.put("id", userLoop.getKey());
			userMap.put("name", userLoop.getValue().get(0).get("username"));
			userMap.put("roleId", userLoop.getValue().get(0).get("roleId"));
			userMap.put("roleName", userLoop.getValue().get(0).get("roleName"));
			userMap.put("memberListId", userLoop.getValue().get(0).get("memberListId"));
			userMap.put("roleType", userLoop.getValue().get(0).get("roleName"));
			userMap.put("profile", imageUrl);
			userMap.put("token", "2wCEAAkGBwgHBgkIBwgKCgkLDRYPDQwMDRsUFRAWIB0iIiAdHx8kKDQsJCYxJx8fLT0tMTU3Ojo6Iys");
			userDetailsMap.putAll(userMap);
		}

		return userDetailsMap;
	}

	private Map<String, Object> getAdminDetails(AdminLogin admin) {
		List<Map<String, Object>> adminDetails = userRepository.getAllAdminDetailsById(admin.getId());
		Map<String, Object> adminMainMap = new HashMap<>();
		Map<String, List<Map<String, Object>>> adminGroupMap = adminDetails.stream()
				.collect(Collectors.groupingBy(action -> action.get("id").toString()));
		for (Entry<String, List<Map<String, Object>>> adminLoop : adminGroupMap.entrySet()) {
			Map<String, Object> adminMap = new HashMap<>();
			int randomNumber = generateRandomNumber();
			String imageUrl = "admin/" + randomNumber + "/" + adminLoop.getValue().get(0).get("id");
			adminMap.put("id", adminLoop.getKey());
			adminMap.put("name", adminLoop.getValue().get(0).get("name"));
			adminMap.put("roleId", adminLoop.getValue().get(0).get("role_id"));
			adminMap.put("roleName", adminLoop.getValue().get(0).get("role_name"));
			adminMap.put("roleType", adminLoop.getValue().get(0).get("role_type"));
			adminMap.put("departmentId", adminLoop.getValue().get(0).get("department_id"));
			adminMap.put("profile", imageUrl);
			adminMap.put("token", "2wCEAAkGBwgHBgkIBwgKCgkLDRYPDQwMDRsUFRAWIB0iIiAdHx8kKDQsJCYxJx8fLT0tMTU3Ojo6Iys");
			adminMainMap.putAll(adminMap);
		}
		return adminMainMap;
	}

	private Map<String, Object> getAllRoleDetails(Employee employee) {
		List<Map<String, Object>> employeeDetails = employeeRepository.getAllRoleDetails(employee.getEmployeeId());
		Map<String, Object> employeeMainMap = new HashMap<>();
		Map<String, List<Map<String, Object>>> employeeGroupMap = employeeDetails.stream()
				.collect(Collectors.groupingBy(action -> action.get("employee_id").toString()));
		for (Entry<String, List<Map<String, Object>>> employeeLoop : employeeGroupMap.entrySet()) {
			int randomNumber = generateRandomNumber();
			String fileExtension = getFileExtensionForImage1(employeeLoop);
			String imageUrl = "profile/" + randomNumber + "/" + employeeLoop.getValue().get(0).get("employee_id") + "."
					+ fileExtension;
			Map<String, Object> employeeMap = new HashMap<>();
			employeeMap.put("id", employeeLoop.getKey());
			employeeMap.put("name", employeeLoop.getValue().get(0).get("user_name"));
			employeeMap.put("employeeId", employeeLoop.getValue().get(0).get("employee_id"));
			employeeMap.put("roleId", employeeLoop.getValue().get(0).get("role_id"));
			employeeMap.put("roleName", employeeLoop.getValue().get(0).get("role_name"));
			employeeMap.put("roleType", employeeLoop.getValue().get(0).get("role_type"));
			employeeMap.put("memberListId", employeeLoop.getValue().get(0).get("memberListId"));
			employeeMap.put("departmentId", employeeLoop.getValue().get(0).get("department_id"));
			employeeMap.put("profile", imageUrl);
			employeeMap.put("token", "2wCEAAkGBwgHBgkIBwgKCgkLDRYPDQwMDRsUFRAWIB0iIiAdHx8kKDQsJCYxJx8fLT0tMTU3Ojo6Iys");
			employeeMainMap.putAll(employeeMap);
		}
		return employeeMainMap;
	}

	private Map<String, Object> getAllClientDetails(ClientProfile clientProfile) {
		List<Map<String, Object>> clientDetails = clientRepository.getAllClientDetails(clientProfile.getClientId());
		Map<String, Object> clientMainMap = new HashMap<>();
		Map<String, List<Map<String, Object>>> clientGroupMap = clientDetails.stream()
				.collect(Collectors.groupingBy(action -> action.get("client_id").toString()));
		int randomNumber = generateRandomNumber();

		for (Entry<String, List<Map<String, Object>>> employeeLoop : clientGroupMap.entrySet()) {
			Map<String, Object> clientMap = new HashMap<>();

			String imageUrl = "clientProfile/" + randomNumber + "/" + employeeLoop.getValue().get(0).get("client_id");
			clientMap.put("id", employeeLoop.getKey());
			clientMap.put("profile", imageUrl);
			clientMap.put("name", employeeLoop.getValue().get(0).get("client_name"));
			clientMap.put("roleId", employeeLoop.getValue().get(0).get("role_id"));
			clientMap.put("roleName", employeeLoop.getValue().get(0).get("role_name"));
			clientMap.put("memberListId", employeeLoop.getValue().get(0).get("memberListId"));
			clientMap.put("roleType", employeeLoop.getValue().get(0).get("role_name"));
			clientMap.put("token", "2wCEAAkGBwgHBgkIBwgKCgkLDRYPDQwMDRsUFRAWIB0iIiAdHx8kKDQsJCYxJx8fLT0tMTU3Ojo6Iys");
			clientMainMap.putAll(clientMap);
		}
		return clientMainMap;
	}

	private Map<String, Object> getAllTrainingDetails(Training training) {
		List<Map<String, Object>> trainingDetails = trainingRepository.getAllClientDetails(training.getTraineeId());
		Map<String, Object> clientMainMap = new HashMap<>();
		Map<String, List<Map<String, Object>>> trainingGroupMap = trainingDetails.stream()
				.collect(Collectors.groupingBy(action -> action.get("trainee_id").toString()));
		int randomNumber = generateRandomNumber();

		for (Entry<String, List<Map<String, Object>>> employeeLoop : trainingGroupMap.entrySet()) {
			Map<String, Object> clientMap = new HashMap<>();

			String traineeId = employeeLoop.getValue().get(0).get("trainee_id") != null
					? employeeLoop.getValue().get(0).get("trainee_id").toString()
					: "default_trainee_id";

			String imageUrl = "training/" + randomNumber + "/" + traineeId;
			clientMap.put("id", employeeLoop.getKey());
			clientMap.put("profile", imageUrl);
			clientMap.put("name", employeeLoop.getValue().get(0).get("user_name"));
			clientMap.put("roleId", employeeLoop.getValue().get(0).get("role_id"));
			clientMap.put("memberListId", employeeLoop.getValue().get(0).get("memberListId"));
			clientMap.put("roleName", employeeLoop.getValue().get(0).get("role_name"));
			clientMap.put("roleType", employeeLoop.getValue().get(0).get("role_name"));
			clientMap.put("departmentId", employeeLoop.getValue().get(0).get("department_id"));
			clientMap.put("token", "2wCEAAkGBwgHBgkIBwgKCgkLDRYPDQwMDRsUFRAWIB0iIiAdHx8kKDQsJCYxJx8fLT0tMTU3Ojo6Iys");
			clientMainMap.putAll(clientMap);
		}
		return clientMainMap;
	}

	private String getFileExtensionForImage1(Entry<String, List<Map<String, Object>>> employeeLoop) {
		if (employeeLoop == null || !employeeLoop.getValue().get(0).containsKey("url")
				|| employeeLoop.getValue().get(0).get("url") == null) {
			return "jpg";
		}

		String url = employeeLoop.getValue().get(0).get("url").toString();

		if (url.endsWith(".png")) {
			return "png";
		} else if (url.endsWith(".jpg")) {
			return "jpg";
		} else {
			return "jpg"; // You might want to handle other cases appropriately
		}
	}

//	@GetMapping("/logout")
//	public ResponseEntity<Map<String, Object>> getManagerDetails(
//	        @RequestParam(required = false) Long employeeId,
//	        @RequestParam(required = false) Long userId,
//	        @RequestParam(required = false) Long clientId
//	) {
//	    try {
//	        Map<String, Object> managerDetails;
//
//	        if (employeeId != null) {
//	            managerDetails = userRepository.getManagerDetailsById1(employeeId);
//	        } else if (userId != null) {
//	            managerDetails = userRepository.getManagerDetailsById2(userId);
//	        } else if (clientId != null) {
//	            managerDetails = userRepository.getManagerDetailsById3(clientId);
//	        } else {
//	            // Handle the case where none of the parameters is provided
//	            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid parameters"));
//	        }
//
//	        if (managerDetails == null) {
//	            // Handle the case where manager details are not found
//	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//	                    .body(Collections.singletonMap("error", "Manager details not found"));
//	        }
//
//	        // Check for null values in the managerDetails map
//	        if (managerDetails.containsValue(null)) {
//	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//	                    .body(Collections.singletonMap("error", "Manager details not found"));
//	        }
//
//	        return ResponseEntity.ok(managerDetails);
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                .body(Collections.singletonMap("error", "Internal Server Error"));
//	    }
//	}

	@GetMapping("/logout/{id}/{roleId}/{roleName}")
	public ResponseEntity<Map<String, Object>> getManagerDetailsById(@PathVariable long id, @PathVariable long roleId,
			@PathVariable String roleName) {
		Map<String, Object> result;
		switch (roleName.toLowerCase()) {
		case "employee":
		case "tl":
		case "projecthead":
			result = userRepository.getManagerDetailsById1(id, roleId);
			break;

		case "manager":
		case "accountant":
			result = userRepository.getManagerDetailsById2(id, roleId);
			break;
		case "training":
			result = userRepository.gettrainingById3(id, roleId);
			break;
		case "customer":
			result = userRepository.getManagerDetailsById3(id, roleId);
			break;
		default:
			return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid entity type"));
		}
		return ResponseEntity.ok(result);
	}

	private Map<String, Object> createLoginFailedResponse() {
		Map<String, Object> response = new HashMap<>();
		Map<String, Object> adminMap = new HashMap<>();
//		adminMap.put("token", "2wCEAAkGBwgHBgkIBwgKCgkLDRYPDQwMDRsUFRAWIB0iIiAdHx8kKDQsJCYbhTrg789Iys");
		adminMap.put("result", "Login Failed");
		response.putAll(adminMap);
		return response;
	}

	@PostMapping("/changepassword")
	public ResponseEntity<String> processChangePassword(@RequestParam String email, @RequestParam String oldPassword,
			@RequestParam String newPassword) {
		User user = userRepository.findByEmail(email);
		AdminLogin admin = adminLoginRepository.findByEmail(email);
		Employee employee = employeeRepository.findByEmail(email);
		ClientProfile client = clientRepository.findByEmail(email);
		if (user != null && user.getPassword().equals(oldPassword)) {
			user.setPassword(newPassword);
			userRepository.save(user);
			return ResponseEntity.ok("Password changed successfully");
		} else if (admin != null && admin.getPassword().equals(oldPassword)) {
			admin.setPassword(newPassword);
			adminLoginRepository.save(admin);
			return ResponseEntity.ok("Password changed successfully");
		} else if (employee != null && employee.getPassword().equals(oldPassword)) {
			employee.setPassword(newPassword);
			employeeRepository.save(employee);
			return ResponseEntity.ok("Password changed successfully");
		} else if (client != null && client.getPassword().equals(oldPassword)) {
			client.setPassword(newPassword);
			clientRepository.save(client);
			return ResponseEntity.ok("Password changed successfully");
		} else {
			return ResponseEntity.badRequest().body("Invalid credentials");
		}
	}

	@GetMapping("/user/withrole")
	public ResponseEntity<Object> getManagerDetils(@RequestParam(required = true) String user) {
		if ("manager".equals(user)) {
			return ResponseEntity.ok(userRepository.getManagerDetails());
		} else {
			String errorMessage = "Invalid value for 'user'. Expected 'manager'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@GetMapping("/manager/count/dashboard")
	public ResponseEntity<Object> getManagerDetilsfdh(@RequestParam(required = true) String dashboard) {
		if ("count".equals(dashboard)) {
			return ResponseEntity.ok(userRepository.getManagerDetailsmanager());
		} else {
			String errorMessage = "Invalid value for 'user'. Expected 'manager'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}
}
