package com.example.erp.controller.admin;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.erp.JwtUtils;
import com.example.erp.entity.admin.AdminLogin;
import com.example.erp.entity.admin.LoginRequest;
import com.example.erp.repository.admin.AdminLoginRepository;
import com.example.erp.repository.admin.UserRepository;
import com.example.erp.repository.clientDetails.ClientRepository;
import com.example.erp.repository.eRecruitments.TraineeDetailsRepository;
import com.example.erp.repository.employee.EmployeeAttendanceRepository;
import com.example.erp.repository.employee.EmployeeRepository;
import com.example.erp.repository.erecruitment.TrainingRepository;
import com.example.erp.service.admin.AdminLoginService;
import com.example.erp.service.message.MemberListService;

@RestController
@CrossOrigin
public class AdminController {

	@Autowired
	private AdminLoginRepository adminLoginRepository;

	@Autowired
	private EmployeeAttendanceRepository employeeAtten;

	@Autowired
	private UserRepository repository;

	@Autowired
	private ClientRepository clientRepository;
	@Autowired
	private AdminLoginService adminLoginService;
	@Autowired
	private MemberListService listService;
	@Autowired
	private EmployeeRepository repo;

	@Autowired
	private TrainingRepository trarepository2;
	@Autowired
	private TraineeDetailsRepository traineerepo;

	@GetMapping("/admin")
	public ResponseEntity<?> displayAllImages1(@RequestParam(required = true) String viewType) {
		if ("AdminLogin".equals(viewType)) {
			List<AdminLogin> images = adminLoginService.listAll();
			Map<String, Object> imageObjects = new HashMap<>();
			for (AdminLogin image : images) {
				int randomNumber = generateRandomNumber();
				String imageUrl = "admin/" + randomNumber + "/" + image.getId();
				image.setUrl(imageUrl);
				imageObjects.put("Id", image.getId());
				imageObjects.put("name", image.getName());
				imageObjects.put("Date", image.getDate());
				imageObjects.put("inTime", image.getIntime());
				imageObjects.put("roleType", image.getRoleType());
				imageObjects.put("image", image.getUrl());
				imageObjects.put("password", image.getPassword());
				imageObjects.put("confirmPassword", image.getConfirmPassword());
				imageObjects.put("email", image.getEmail());
			}
			return ResponseEntity.ok(imageObjects);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
		}
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}

	@GetMapping("/admin1/view/{id}/{roleId}")
	public ResponseEntity<Map<String, Object>> displayAllEmployeesWithDetailsWithId(@PathVariable("id") Long admin_id,
			@PathVariable("roleId") Long role_id) {
		try {
			List<Map<String, Object>> employeeDetails = adminLoginRepository.getAllEmployeesWithDetailsWithId(admin_id,
					role_id);
			Map<String, Object> employeeResponses = new HashMap<>();
			for (Map<String, Object> employeeDetail : employeeDetails) {
				int randomNumber = generateRandomNumber();

				String imageUrl = "admin/" + randomNumber + "/" + employeeDetail.get("id");

				Map<String, Object> employeeResponse = new HashMap<>();
				employeeResponse.put("id", employeeDetail.get("id"));
				employeeResponse.put("name", employeeDetail.get("name"));
				employeeResponse.put("date", employeeDetail.get("date"));
				employeeResponse.put("inTime", employeeDetail.get("intime"));
				employeeResponse.put("roleType", employeeDetail.get("role_type"));
				employeeResponse.put("confirmPassword", employeeDetail.get("confirm_password"));
				employeeResponse.put("password", employeeDetail.get("password"));
				employeeResponse.put("email", employeeDetail.get("email"));

				employeeResponse.put("image", imageUrl);
				employeeResponses.putAll(employeeResponse);
			}
			return ResponseEntity.ok(employeeResponses);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/admin/view/{id}/{roleId}")
	public ResponseEntity<?> getHrInterviewDetailsWithId(@PathVariable("id") long id,
			@PathVariable("roleId") long roleId) {
		try {
		List<Map<String, Object>> training = trarepository2.getAllEmployeesWithDetailsWithIdEmployeeTrainee(id, roleId);
		List<Map<String, Object>> emp = repo.getAllEmployeesWithDetailsWithIdEmployee(id, roleId);
		List<Map<String, Object>> user = repository.getAllEmployeesWithDetailsWithIdUser(id, roleId);
		List<Map<String, Object>> client = clientRepository.getAllEmployeesWithDetailsWithIdEmployeeClient(id, roleId);
		List<Map<String, Object>> admin = adminLoginRepository.getAllEmployeesWithDetailsWithId(id,roleId);

		if (!training.isEmpty() && training.get(0).get("trainee_id") != null && training.get(0).get("role_id") != null) {
			List<Map<String, Object>> traineeList = new ArrayList<>();
			Map<String, Object> trainingMap = new HashMap<>();
			trainingMap.put("id", training.get(0).get("trainee_id"));
			trainingMap.put("confirmPassword", training.get(0).get("confirm_password"));
			trainingMap.put("email", training.get(0).get("email"));
			trainingMap.put("name", training.get(0).get("user_name"));
			trainingMap.put("password", training.get(0).get("password"));
			trainingMap.put("roleType", training.get(0).get("role_name"));
			trainingMap.put("status", training.get(0).get("status"));
			trainingMap.put("date", training.get(0).get("date"));
			trainingMap.put("inTime", training.get(0).get("intime"));
			trainingMap.put("roleId", training.get(0).get("role_id"));
			int randomNumber = generateRandomNumber();
			Object complaintId = training.get(0).get("trainee_id");
			String imageUrl = "training/" + randomNumber + "/" + complaintId;
			trainingMap.put("image", imageUrl);
//			traineeList.add(trainingMap);
			trainingMap.putAll(trainingMap);
			return ResponseEntity.ok(trainingMap);

		}if (!admin.isEmpty() && admin.get(0).get("id") != null && admin.get(0).get("role_id") != null) {
				List<Map<String, Object>> adminList = new ArrayList<>();
				Map<String, Object> adminMap = new HashMap<>();
				adminMap.put("id", admin.get(0).get("id"));
				adminMap.put("confirmPassword", admin.get(0).get("confirm_password"));
				adminMap.put("email", admin.get(0).get("email"));
				adminMap.put("name", admin.get(0).get("name"));
				adminMap.put("password", admin.get(0).get("password"));
				adminMap.put("roleType", admin.get(0).get("role_type"));
				adminMap.put("status", admin.get(0).get("status"));
				adminMap.put("date", admin.get(0).get("date"));
				adminMap.put("inTime", admin.get(0).get("intime"));
				adminMap.put("roleId", admin.get(0).get("role_id"));
				int randomNumber = generateRandomNumber();
				Object complaintId1 = admin.get(0).get("id");
				String imageUrl1 = "admin/" + randomNumber + "/" + complaintId1;
				adminMap.put("image", imageUrl1);
				adminMap.putAll(adminMap);
				return ResponseEntity.ok(adminMap);
		} else if (!emp.isEmpty() && emp.get(0).get("employee_id") != null && emp.get(0).get("role_id") != null) {
		    Map<String, Object> employeeMap = new HashMap<>();
		    employeeMap.put("id", emp.get(0).get("employee_id"));
		    employeeMap.put("confirmPassword", emp.get(0).get("confirm_password"));
		    employeeMap.put("email", emp.get(0).get("email"));
		    employeeMap.put("name", emp.get(0).get("user_name"));
		    employeeMap.put("password", emp.get(0).get("password"));
		    employeeMap.put("roleType", emp.get(0).get("role_type"));
		    employeeMap.put("status", emp.get(0).get("status"));
		    employeeMap.put("date", emp.get(0).get("date"));
		    employeeMap.put("inTime", emp.get(0).get("intime"));
		    employeeMap.put("roleId", emp.get(0).get("role_id"));

		    int randomNumber = generateRandomNumber();
		    String fileExtension = getFileExtensionForImage1(emp);
		    String imageUrl = "profile/" + randomNumber + "/" + emp.get(0).get("employee_id") + "." + fileExtension;
		    employeeMap.put("image", imageUrl);

		    return ResponseEntity.ok(employeeMap);
		}

		else if (!user.isEmpty() && user.get(0).get("user_id") != null && user.get(0).get("role_id") != null) {
		    Map<String, Object> userMap = new HashMap<>();
		    userMap.put("id", user.get(0).get("user_id"));
		    userMap.put("email", user.get(0).get("email"));
		    userMap.put("confirmPassword", user.get(0).get("confirm_password"));
		    userMap.put("name", user.get(0).get("username"));
		    userMap.put("password", user.get(0).get("password"));
		    userMap.put("roleType", user.get(0).get("role_type"));
		    userMap.put("status", user.get(0).get("status"));
		    userMap.put("date", user.get(0).get("date"));
		    userMap.put("inTime", user.get(0).get("intime"));
		    userMap.put("roleId", user.get(0).get("role_id"));

		    int randomNumber = generateRandomNumber();
		    String fileExtension = getFileExtensionForImage1(user);
		    String imageUrl = "user/" + randomNumber + "/" + user.get(0).get("user_id") + "." + fileExtension;
		    userMap.put("image", imageUrl);

		    return ResponseEntity.ok(userMap);
		}
		

		
		else if (!client.isEmpty() && client.get(0).get("client_id") != null && client.get(0).get("role_id") != null) {
		    Map<String, Object> clientrMap = new HashMap<>();
			clientrMap.put("id", client.get(0).get("client_id"));
			clientrMap.put("email", client.get(0).get("email"));
			clientrMap.put("confirmPassword", client.get(0).get("confirm_password"));
			clientrMap.put("name", client.get(0).get("client_name"));
			clientrMap.put("password", client.get(0).get("password"));
			clientrMap.put("roleType", client.get(0).get("role_name"));
			clientrMap.put("status", client.get(0).get("status"));
			clientrMap.put("date", client.get(0).get("date"));
			clientrMap.put("inTime", client.get(0).get("intime"));
			clientrMap.put("roleId", client.get(0).get("role_id"));

		    int randomNumber = generateRandomNumber();
		    String imageUrl = "clientProfile/" + randomNumber + "/" + client.get(0).get("client_id") ;
		    clientrMap.put("image", imageUrl);

		    return ResponseEntity.ok(clientrMap);
		} else {
            return ResponseEntity.notFound().build(); // No data found
        }

    } catch (Exception e) {
        // Log the exception for debugging purposes
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching data from the database.");
    }
}


	private String getFileExtensionForImage1(List<Map<String, Object>> user) {
		if (user == null || !user.get(0).containsKey("url") || user.get(0).get("url") == null) {
	        return "jpg";
	    }
	    
	    String url = user.get(0).get("url").toString();
	    
	    if (url.endsWith(".png")) {
	        return "png";
	    } else if (url.endsWith(".jpg")) {
	        return "jpg";
	    } else {
	        return "jpg"; 
	    }
	}
	

	@GetMapping("/admin/{randomNumber}/{id}")
	public ResponseEntity<ByteArrayResource> serveFile(@PathVariable("randomNumber") int randomNumber,
			@PathVariable("id") Long id) {
		Optional<AdminLogin> complaintsOptional = adminLoginService.getById1(id);

		if (complaintsOptional.isPresent()) {
			AdminLogin complaints = complaintsOptional.get();
			Blob image = complaints.getImage();

			if (image != null) {
				String filename = "file_" + randomNumber + "_" + id;
				byte[] fileBytes;

				try {
					fileBytes = image.getBytes(1, (int) image.length());
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
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PutMapping("admin/edit/{id}")
	public ResponseEntity<?> updateAdmin(@PathVariable Long id,
			@RequestParam(value = "image", required = false) MultipartFile file,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "confirmPassword", required = false) String confirmPassword) {
		try {
			AdminLogin existingAdmin = adminLoginService.getById(id);
			if (existingAdmin == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found.");
			}

			if (password != null && !password.equals(confirmPassword)) {
				String errorMessage = "Password and ConfirmPassword do not match.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			// Update fields
			existingAdmin.setEmail(email);
			existingAdmin.setName(name);
//			existingAdmin.setPassword(passwordEncoder.encode(password)); // Hash the password
			existingAdmin.setPassword(password);
			existingAdmin.setConfirmPassword(confirmPassword);
			existingAdmin.setDate(LocalDate.now());
			String intime = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a"));
			existingAdmin.setIntime(intime);
			long role_id=existingAdmin.getRoleId();
			if (file != null && !file.isEmpty()) {
				byte[] bytes = file.getBytes();
				Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
				existingAdmin.setImage(blob);
			}

			adminLoginService.saveOrUpdate(existingAdmin);
			
			List<Map<String, Object>> employeeDetails = adminLoginRepository.getAllEmployeesWithDetailsWithId(id,
					role_id);
			Map<String, Object> employeeResponses = new HashMap<>();
			for (Map<String, Object> employeeDetail : employeeDetails) {
				int randomNumber = generateRandomNumber();

				String imageUrl = "admin/" + randomNumber + "/" + employeeDetail.get("id");

				Map<String, Object> employeeResponse = new HashMap<>();
				employeeResponse.put("id", employeeDetail.get("id"));
				employeeResponse.put("name", employeeDetail.get("name"));
				employeeResponse.put("date", employeeDetail.get("date"));
				employeeResponse.put("inTime", employeeDetail.get("intime"));
				employeeResponse.put("roleType", employeeDetail.get("role_type"));
				employeeResponse.put("confirmPassword", employeeDetail.get("confirm_password"));
				employeeResponse.put("password", employeeDetail.get("password"));
				employeeResponse.put("email", employeeDetail.get("email"));

				employeeResponse.put("image", imageUrl);
				employeeResponses.putAll(employeeResponse);
			}
			return ResponseEntity.ok(employeeResponses);
		} catch (IOException | SQLException e) {
			// Log the exception for debugging purposes
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating admin.");
		}
	}

	@PostMapping("/admin/login")
	public ResponseEntity<String> processLogin(@RequestBody LoginRequest loginRequest) {
		String email = loginRequest.getEmail();
		String password = loginRequest.getPassword();

		AdminLogin admin = adminLoginRepository.findByEmail(email);

		if (admin != null && admin.getPassword().equals(password)) {
			String token = JwtUtils.generateToken(admin);

			return ResponseEntity.ok(token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}
	}

	@PostMapping("/admin/changepassword")
	public ResponseEntity<String> processChangePassword(@RequestParam String email, @RequestParam String oldPassword,
			@RequestParam String newPassword) {
		AdminLogin admin = adminLoginRepository.findByEmail(email);
		if (admin != null && admin.getPassword().equals(oldPassword)) {
			admin.setPassword(newPassword);
			adminLoginRepository.save(admin);
			return ResponseEntity.ok("Password changed successfully");
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		}
	}

	@GetMapping("/dashboard")
	public Map<String, Object> getAllDetails(@RequestParam(required = true) String attendance) {
		Map<String, Object> productMap = new HashMap<>();

		if ("dashboardcount".equals(attendance)) {
			List<Map<String, Object>> attendanceData = employeeAtten.getAllpresent();

			Object presentCount = 0;
			Object absentCount = 0;

			if (!attendanceData.isEmpty() && attendanceData.get(0) != null) {
				presentCount = attendanceData.get(0).get("present_count");
				absentCount = attendanceData.get(0).get("absent_count");
			}

			productMap.put("present_count", presentCount);
			productMap.put("absent_count", absentCount);
			productMap.put("employee", repo.countByStatusTrue());
			productMap.put("trainee", traineerepo.countByStatusTrue());
		}

		return productMap;
	}

	@GetMapping("/dashboard/attendace")
	public List<Map<String, Object>> INeedList(@RequestParam(required = true) String attendace) {
		try {
			if ("dashboard".equals(attendace)) {
				return employeeAtten.getAllpresent1();
			} else {
				throw new IllegalArgumentException("The provided dashboard is not supported.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@GetMapping("/admin/count/dashboard")
	public Map<String, Object> IHaveNeedList(@RequestParam(required = true) String dashboard) {
		try {
			if ("count".equals(dashboard)) {
				return adminLoginRepository.ProjectTypeDetails();
			} else {
				throw new IllegalArgumentException("The provided dashboard is not supported.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return (Map<String, Object>) Collections.emptyList();
		}
	}
}
