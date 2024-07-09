package com.example.erp.controller.clientDetails;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
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
import com.example.erp.entity.clientDetails.ClientInvoiceList;
import com.example.erp.entity.clientDetails.ClientProfile;
import com.example.erp.entity.clientDetails.ClientRequirement;
import com.example.erp.entity.employee.Complaints;
import com.example.erp.entity.employee.Employee;
import com.example.erp.entity.erecruitment.Training;
import com.example.erp.entity.message.MemberList;
import com.example.erp.repository.clientDetails.ClientRepository;
import com.example.erp.repository.message.MemberListRepository;
import com.example.erp.service.admin.AdminLoginService;
import com.example.erp.service.admin.UserService;
import com.example.erp.service.clientDetails.ClientRequirementService;
import com.example.erp.service.clientDetails.ClientService;
import com.example.erp.service.eRecruitment.TrainingService;
import com.example.erp.service.eRecruitments.TraineeDetailsService;
import com.example.erp.service.employee.EmployeeService;
import com.example.erp.service.message.MemberListService;

@RestController
@CrossOrigin
public class ClientController {

	@Autowired
	private MemberListService listService;

	@Autowired
	private MemberListRepository listRepository;
	@Autowired
	private ClientService clientService;

	@Autowired
	private ClientRequirementService clientRequirementService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private AdminLoginService adminLoginService;

	@Autowired
	private TrainingService traineeDetailsService;

	@Autowired
	private ClientRepository clientRepository;

	@GetMapping("/clientProfile")
	public ResponseEntity<Object> getClientProfile(@RequestParam(required = true) String client) {
		if ("clientDetails".equals(client)) {
			return ResponseEntity.ok(clientService.listAll());
		} else {
			String errorMessage = "Invalid value for 'client'. Expected 'clientDetails'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@PostMapping("/clientProfile/save")
	public ResponseEntity<String> saveSingleUser(@RequestParam("profile") MultipartFile file,
			@RequestParam("clientName") String clientName, @RequestParam("gender") String gender,
			@RequestParam("phoneNumber") String phoneNumber, @RequestParam("address") String address,
			@RequestParam("password") String password, @RequestParam("confirmPassword") String confirmPassword,
			@RequestParam("email") String email, @RequestParam("city") String city, @RequestParam("state") String state,
			@RequestParam("country") String country, @RequestParam("zipCode") int zipCode,
			@RequestParam("mobileNumber") String mobileNumber, @RequestParam("referral") String referral) {

		try {
			ClientProfile existingUser = clientRepository.findByEmail(email);
			if (existingUser != null) {
				return ResponseEntity.badRequest().body("Email is already in use. Please use a different email.");
			}
			ClientProfile existingUser1 = clientRepository.findByMobileNumber(mobileNumber);
			if (existingUser1 != null) {
				return ResponseEntity.badRequest()
						.body("Mobile number is already in use. Please use a different mobile number.");
			}

			if (!password.equals(confirmPassword)) {
				return ResponseEntity.badRequest().body("Password and Confirm Password do not match");
			}
			Optional<ClientProfile> existingmobile1 = clientRepository.findByMobileNumberAndEmail(mobileNumber, email);
			if (existingmobile1.isPresent()) {
				return ResponseEntity.badRequest()
						.body("This Number  " + mobileNumber + "  and  " + email + "  already use");
			}
			if (phoneNumber != null && phoneNumber.equals(mobileNumber)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phone numbers must be different.");
			}
			Optional<MemberList> existingemail = listRepository.findByEmail(email);
			if (existingemail.isPresent()) {
				return ResponseEntity.badRequest().body("email with ID " + email + " already exists.");
			}

			Optional<MemberList> existingmobile = listRepository.findByPhoneNumber(mobileNumber);
			if (existingmobile.isPresent()) {
				return ResponseEntity.badRequest().body("This Number" + mobileNumber + " already exists.");
			}

			Optional<MemberList> existingmobile11 = listRepository.findByPhoneNumber(phoneNumber);
			if (existingmobile11.isPresent()) {
				return ResponseEntity.badRequest().body("This Number" + phoneNumber + " already exists.");
			}

			byte[] bytes = file.getBytes();
			Blob blob = new SerialBlob(bytes);

			ClientProfile client = new ClientProfile();
			client.setStatus(true);
			client.setClientProfile(blob);
			client.setPhoneNumber(phoneNumber);
			client.setAddress(address);
			client.setClientName(clientName);
			client.setGender(gender);
			client.setPassword(password);
			client.setConfirmPassword(confirmPassword);
			client.setEmail(email);
			client.setCity(city);
			client.setState(state);
			client.setCountry(country);
			client.setZipCode(zipCode);
			client.setMobileNumber(mobileNumber);
			client.setReferral(referral);
			client.setRoleId(3);
			client.setRoleName("Customer");
			clientService.SaveClientProfile(client);

			long clientId = client.getClientId();

			MemberList list = new MemberList();
			list.setId(clientId);
			list.setRoleId(3);
			list.setEmail(email);
			list.setPhoneNumber(mobileNumber);
			list.setUserName(clientName);
			list.setRoleType("Customer");
			list.setProfile(blob);
			listService.Save(list);
			updateLists();

			return ResponseEntity.ok("Client saved with id: " + clientId);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving client: " + e.getMessage());
		}
	}

	private List<User> userDetail = new ArrayList<>();
	private List<Training> traineeDetails = new ArrayList<>();
	private List<AdminLogin> adminDetail = new ArrayList<>();
	private List<Employee> employeeDetails = new ArrayList<>();

	private void updateLists() {
		userDetail = userService.listAll();
		traineeDetails = traineeDetailsService.listAll();
		adminDetail = adminLoginService.listAll();
		employeeDetails = employeeService.listAll();
	}

	private boolean emailExistsInAnyList(String email) {
		return userDetail.stream().anyMatch(user -> email != null && email.equals(user.getEmail()))
				|| traineeDetails.stream().anyMatch(trainee -> email != null && email.equals(trainee.getEmail()))
				|| adminDetail.stream().anyMatch(admin -> email != null && email.equals(admin.getEmail()))
				|| employeeDetails.stream().anyMatch(employee -> email != null && email.equals(employee.getEmail()));
	}

	private boolean mobileNumberInAnyList(String mobileNumber) {
		return userDetail.stream().anyMatch(user -> mobileNumber != null && mobileNumber.equals(user.getMobileNumber()))
				|| traineeDetails.stream()
						.anyMatch(trainee -> mobileNumber != null && mobileNumber.equals(trainee.getMobileNumber()))
				|| employeeDetails.stream()
						.anyMatch(employee -> mobileNumber != null && mobileNumber.equals(employee.getPhoneNumber()));
	}

	@PutMapping("/client/edit/{clientId}")
	public ResponseEntity<String> updateSingleUser(@PathVariable Long clientId,
			@RequestParam(value = "profile", required = false) MultipartFile file,
			@RequestParam(value = "clientName", required = false) String clientName,
			@RequestParam(value = "gender", required = false) String gender,
			@RequestParam(value = "phoneNumber", required = false) String phoneNumber,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "confirmPassword", required = false) String confirmPassword,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "country", required = false) String country,
			@RequestParam(value = "zipCode", required = false) int zipCode,
			@RequestParam(value = "mobileNumber", required = false) String mobileNumber,
			@RequestParam(value = "referral", required = false) String referral) {

		try {
			if (!password.equals(confirmPassword)) {
				return ResponseEntity.badRequest().body("Password and Confirm Password do not match");
			}

			if (phoneNumber != null && phoneNumber.equals(mobileNumber)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phone numbers must be different.");
			}

			ClientProfile client = clientService.findById(clientId);

			if (client == null) {
				return ResponseEntity.notFound().build();
			}

			if (file != null) {
				byte[] bytes = file.getBytes();
				Blob blob = new SerialBlob(bytes);
				client.setClientProfile(blob);
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
				client.setEmail(email);
			}
			if (phoneNumber != null) {
//				Optional<MemberList> existingMobileOptional = listRepository.findByPhoneNumber(phoneNumber);
//				if (existingMobileOptional.isPresent()) {
//					MemberList existingClient = existingMobileOptional.get();
//					if (existingClient.getPhoneNumber() != null
//							&& existingClient.getPhoneNumber().equals(phoneNumber)) {
//						return ResponseEntity.badRequest()
//								.body("This number " + phoneNumber + " already exists for another client.");
//					} else if (existingClient.getPhoneNumber() != null
//							&& existingClient.getPhoneNumber().equals(mobileNumber)) {
//						return ResponseEntity.badRequest()
//								.body("Alternate number " + mobileNumber + " already exists for another client.");
//					}
//				}

				client.setPhoneNumber(phoneNumber);
			}
			if (mobileNumber != null) {
				client.setMobileNumber(mobileNumber);
			}
			client.setStatus(true);
//			client.setPhoneNumber(phoneNumber);
			client.setAddress(address);
			client.setClientName(clientName);
			client.setGender(gender);
			client.setPassword(password);
			client.setConfirmPassword(confirmPassword);
//			client.setEmail(email);
			client.setCity(city);
			client.setState(state);
			client.setCountry(country);
			client.setZipCode(zipCode);
//			client.setMobileNumber(mobileNumber);
			client.setReferral(referral);
			client.setRoleId(3);

			clientService.SaveClientProfile(client);

			MemberList list = new MemberList();
			Optional<MemberList> existingMember = listRepository.findByIdAndRoleId(clientId, 3);

			if (existingMember.isPresent()) {
				list = existingMember.get();
				if (email != null) {
					list.setEmail(email);
				}
				if (phoneNumber != null) {
					list.setPhoneNumber(phoneNumber);
				}
				list.setUserName(clientName);
				list.setRoleId(3);
				list.setRoleType("Customer");

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
				return ResponseEntity.ok("MemberList not found for clientId: " + clientId);
			}

			return ResponseEntity.ok("Complaints updated for client with id: " + client.getClientId());
		} catch (IOException | SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error updating Complaints: " + e.getMessage());
		}
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}

	@GetMapping("clientProfile/{randomNumber}/{id}")
	public ResponseEntity<ByteArrayResource> serveFile(@PathVariable("randomNumber") int randomNumber,
			@PathVariable("id") Long id) {
		Optional<ClientProfile> complaintsOptional = clientService.getById1(id);
		if (complaintsOptional.isPresent()) {
			ClientProfile complaints = complaintsOptional.get();
			String filename = "file_" + randomNumber + "_" + id;
			byte[] fileBytes;
			try {
				fileBytes = complaints.getClientProfile().getBytes(1, (int) complaints.getClientProfile().length());
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
			// Handle or log the exception
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

	@PutMapping("/clientProfile/{id}")
	public ResponseEntity<ClientProfile> updateClientProfile(@PathVariable("id") long id,
			@RequestParam("clientName") String clientName, @RequestParam("gender") String gender,
			@RequestParam("phoneNumber") String phoneNumber, @RequestParam("address") String address,
			@RequestParam("email") String email, @RequestParam("city") String city, @RequestParam("state") String state,
			@RequestParam("country") String country, @RequestParam("zipCode") int zipCode,
			@RequestParam("mobileNumber") String mobileNumber, @RequestParam("referral") String referral) {
		try {
			ClientProfile existingClient = clientService.findById(id);
			if (existingClient == null) {
				return ResponseEntity.notFound().build();
			}

			existingClient.setClientName(clientName);
			existingClient.setGender(gender);
			existingClient.setPhoneNumber(phoneNumber);
			existingClient.setAddress(address);
			existingClient.setEmail(email);
			existingClient.setCity(city);
			existingClient.setState(state);
			existingClient.setZipCode(zipCode);
			existingClient.setMobileNumber(mobileNumber);
			existingClient.setReferral(referral);

			clientService.SaveClientProfile(existingClient);
			return ResponseEntity.ok(existingClient);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/client1/edit/{id}")
	public ResponseEntity<ClientProfile> updateOrder(@PathVariable("id") Long client_id,
			@RequestBody ClientProfile client) {
		try {
			ClientProfile existingClient = clientService.findById(client_id);

			if (existingClient == null) {
				return ResponseEntity.notFound().build();
			}

			existingClient.setAddress(client.getAddress());
			existingClient.setCity(client.getCity());
			existingClient.setClientName(client.getClientName());
			existingClient.setCountry(client.getCountry());
			existingClient.setEmail(client.getEmail());
			existingClient.setGender(client.getGender());
			existingClient.setPhoneNumber(client.getPhoneNumber());
			existingClient.setMobileNumber(client.getMobileNumber());
			existingClient.setState(client.getState());
			existingClient.setZipCode(client.getZipCode());
			existingClient.setMobileNumber(client.getMobileNumber());
			existingClient.setReferral(client.getReferral());
			existingClient.setZipCode(client.getZipCode());

			clientService.SaveClientProfile(existingClient);
			return ResponseEntity.ok(existingClient);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/clientProfile/delete/{id}")
	public ResponseEntity<String> deleteclientProfile(@PathVariable("id") Long id) {
		clientService.deleteClientId(id);
		return ResponseEntity.ok("ClientProfile details deleted successfully");
	}

	@GetMapping("/client/true/false")
	public ResponseEntity<List<Map<String, Object>>> getAllClientDetails(@RequestParam(required = true) String client) {
		List<Map<String, Object>> clientDetails;

		if ("True".equals(client)) {
			clientDetails = clientRepository.getAllClientDetailsTrue();
		} else if ("False".equals(client)) {
			clientDetails = clientRepository.getAllClientDetailsFalse();
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}

		List<Map<String, Object>> clientMainMap = new ArrayList<>();
		Map<String, List<Map<String, Object>>> clientGroupMap = clientDetails.stream()
				.collect(Collectors.groupingBy(action -> action.get("client_id").toString()));

		for (Entry<String, List<Map<String, Object>>> entry : clientGroupMap.entrySet()) {
			Map<String, Object> clientMap = new HashMap<>();
			List<Map<String, Object>> clientList = entry.getValue();
			Map<String, Object> firstClient = clientList.get(0);
			int randomNumber = generateRandomNumber();
			String imageUrl = "clientProfile/" + randomNumber + "/" + firstClient.get("client_id");
			clientMap.put("clientId", entry.getKey());
			clientMap.put("clientName", firstClient.get("client_name"));
			clientMap.put("gender", firstClient.get("gender"));
			clientMap.put("phoneNumber", firstClient.get("phone_number"));
			clientMap.put("mobileNumber", firstClient.get("mobile_number"));
			clientMap.put("address", firstClient.get("address"));
			clientMap.put("password", firstClient.get("password"));
			clientMap.put("confirmPassword", firstClient.get("confirm_password"));
			clientMap.put("email", firstClient.get("email"));
			clientMap.put("clientProfile", imageUrl);
			clientMap.put("city", firstClient.get("city"));
			clientMap.put("state", firstClient.get("state"));
			clientMap.put("country", firstClient.get("country"));
			clientMap.put("referral", firstClient.get("referral"));
			clientMap.put("roleId", firstClient.get("role_id"));
			clientMap.put("roleName", firstClient.get("role_name"));
			clientMap.put("zipCode", firstClient.get("zip_code"));

			clientMainMap.add(clientMap);
		}

		return ResponseEntity.ok(clientMainMap);
	}

	@GetMapping("/clientProfile/view/{id}")
	public ResponseEntity<?> displayAllEmployeesWithDetailsWithId(@PathVariable("id") Long client_id) {

		try {

			List<Map<String, Object>> employeeDetails = clientRepository.getAllEmployeesWithDetailsWithId(client_id);
			List<Map<String, Object>> clientMainMap = new ArrayList<>();

			for (Map<String, Object> clientDetails : employeeDetails) {
				Map<String, Object> clientMap = new HashMap<>();

				int randomNumber = generateRandomNumber();
				String imageUrl = "clientProfile/" + randomNumber + "/" + clientDetails.get("client_id");

				clientMap.put("clientId", clientDetails.get("client_id"));
				clientMap.put("clientName", clientDetails.get("client_name"));
				clientMap.put("gender", clientDetails.get("gender"));
				clientMap.put("phoneNumber", clientDetails.get("phone_number"));
				clientMap.put("mobileNumber", clientDetails.get("mobile_number"));
				clientMap.put("address", clientDetails.get("address"));
				clientMap.put("email", clientDetails.get("email"));
				clientMap.put("clientProfile", imageUrl);
				clientMap.put("city", clientDetails.get("city"));
				clientMap.put("state", clientDetails.get("state"));
				clientMap.put("country", clientDetails.get("country"));
				clientMap.put("referral", clientDetails.get("referral"));
				clientMap.put("roleId", clientDetails.get("role_id"));
				clientMap.put("roleName", clientDetails.get("role_name"));
				clientMap.put("zipCode", clientDetails.get("zip_code"));
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

	@PutMapping("/client/status/{id}")
	public ResponseEntity<Boolean> toggleComplaintsStatus(@PathVariable(name = "id") long id) {
		try {
			ClientProfile clientProfile = clientService.findById(id);
			if (clientProfile != null) {
				boolean currentStatus = clientProfile.isStatus();
				clientProfile.setStatus(!currentStatus);
				clientService.SaveClientProfile(clientProfile);
			} else {
				return ResponseEntity.ok(false);
			}

			return ResponseEntity.ok(clientProfile.isStatus());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}

	@GetMapping("/client/payment")
	public List<Map<String, Object>> getAllcountInvolce(@RequestParam(required = true) String client) {
		try {
			if ("payment".equalsIgnoreCase(client)) {
				return clientRepository.getAllClientDetailsInvoice();
			} else {
				throw new IllegalArgumentException("Invalid parameter value. 'employees' must be 'resignations'.");
			}
		} catch (Exception e) {

			e.printStackTrace();
			return Collections.emptyList();

		}
	}

	@GetMapping("/client/year/count")
	public List<Map<String, Object>> getAllcount(@RequestParam(required = true) String client) {
		try {
			if ("count".equalsIgnoreCase(client)) {
				return clientRepository.ALLCountcustomer();
			} else {
				throw new IllegalArgumentException("Invalid parameter value. 'employees' must be 'resignations'.");
			}
		} catch (Exception e) {

			e.printStackTrace();
			return Collections.emptyList();

		}
	}

	@GetMapping("/client/count/{client_id}")
	public Map<String, Object> getAllMemberDetailsByMemberId1(@PathVariable Long client_id) {
		return clientRepository.Allfilter2(client_id);
	}

	@GetMapping("/client/project/status/{client_id}")
	public List<Map<String, Object>> getAllMemberDetailsByMemberIdClient(@PathVariable Long client_id) {
		return clientRepository.GetClientProjectStatus(client_id);
	}

	@GetMapping("/client/current/payment/{client_id}")
	public List<Map<String, Object>> getAllMemberDetailsByMemberIdPaymentList(@PathVariable Long client_id) {
		return clientRepository.GetClientCurrentMonthPayment(client_id);
	}

	@GetMapping("/client/working/menber222/{client_id}")
	public List<Map<String, Object>> getAllMemberDetailsByMemberIdWorkingMember(@PathVariable Long client_id) {

		return clientRepository.GetClientProjectWorkingMember(client_id);
	}

	@GetMapping("/client/quotation/{client_id}")
	public List<Map<String, Object>> getAllMemberDetailsByMemberIdWorkingMemberQuation(@PathVariable Long client_id) {

		return clientRepository.GetClientProjectWorkingMemberQuotaion(client_id);
	}

	@GetMapping("/client/working/member/{id}")
	public ResponseEntity<?> getTaskAssignedDetails(@PathVariable("id") Long clientId) {
		try {
			List<Map<String, Object>> tasks = clientRepository.GetClientProjectWorkingMember(clientId);
			List<Map<String, Object>> responseList = new ArrayList<>();

			for (Map<String, Object> taskAssigned : tasks) {
				Map<String, Object> taskAssignedMap = new HashMap<>();

				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(taskAssigned);

				if (taskAssigned.containsKey("employee_id") && taskAssigned.get("employee_id") != null) {
					String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employee_id") + "."
							+ fileExtension;
					taskAssignedMap.put("profile", imageUrl);
				} else if (taskAssigned.containsKey("trainee_id") && taskAssigned.get("trainee_id") != null) {
					String imageUrl = "training/" + randomNumber + "/" + taskAssigned.get("trainee_id");
					taskAssignedMap.put("profile", imageUrl);
				}

				taskAssignedMap.putAll(taskAssigned);
				String projectName = (String) taskAssigned.get("project_name");
				Map<String, Object> projectMap = responseList.stream()
						.filter(p -> p.containsKey("projectName") && p.get("projectName").equals(projectName))
						.findFirst().orElse(null);

				if (projectMap == null) {
					projectMap = new HashMap<>();
					projectMap.put("projectName", projectName);
					projectMap.put("employees", new ArrayList<>());
					responseList.add(projectMap);
				}
				List<Map<String, Object>> employees = (List<Map<String, Object>>) projectMap.get("employees");
				employees.add(taskAssignedMap);
			}

			return ResponseEntity.ok(responseList);
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

	@GetMapping("/client/maintenance/list/{client_id}")
	public List<Map<String, Object>> getAllMemberDetailsByMemberIdMaintenance(@PathVariable Long client_id) {
		return clientRepository.GetClientMaintenance(client_id);
	}

}
