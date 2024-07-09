package com.example.erp.controller.employee;

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
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.erp.entity.accounting.CompanyAssets;
import com.example.erp.entity.employee.Complaints;
import com.example.erp.entity.employee.Employee;
import com.example.erp.entity.employee.Qualification;
import com.example.erp.entity.erecruitment.InterviewSchedule;
import com.example.erp.entity.erecruitment.Training;
import com.example.erp.entity.message.MemberList;
import com.example.erp.repository.employee.ComplaintsRepository;
import com.example.erp.service.eRecruitment.TrainingService;
import com.example.erp.service.employee.ComplaintsService;
import com.example.erp.service.employee.EmployeeService;

@CrossOrigin
@RestController
public class ComplaintsController {
	@Autowired
	private ComplaintsService service;

	@Autowired
	private ComplaintsRepository repo;
	@Autowired
	private TrainingService trainingService;
	
	@Autowired
	private EmployeeService employeeService;
	 @Autowired
	   private JavaMailSender emailSender;
	 
	 @Value("${spring.mail.username}")
	    private String from;

	@PostMapping("/complaints/save")
	public ResponseEntity<String> saveComplaints(
			@RequestParam(value = "attachments", required = false) MultipartFile file,
			@RequestParam(value = "employeeId", required = false) Long employeeId,
			@RequestParam(value = "complaintsTitle", required = false) String complaintsTitle,
			@RequestParam(value = "complaintsAgainst", required = false) Long complaintsAgainst,
			@RequestParam(value = "complaintsAgainstName", required = false) String complaintsAgainstName,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "traineeId", required = false) Long traineeId) {
		try {
			byte[] bytes = (file != null) ? file.getBytes() : null;
			Blob blob = (bytes != null) ? new SerialBlob(bytes) : null;

			Date date = new Date(System.currentTimeMillis());
			  if (employeeId != null && complaintsAgainst != null && employeeId.equals(complaintsAgainst)) {
		            return ResponseEntity.badRequest().body("employeeId and complaintsAgainst cannot be the same.");
		        }
				Optional<Complaints> existingemail = repo.findByComplaintsTitleAndComplaintsAgainst(complaintsTitle,complaintsAgainst);
				if (existingemail.isPresent()) {
					return ResponseEntity.badRequest().body("same entry not allowed");
				}
			Complaints complaints = new Complaints();
			complaints.setStatus(true);
			complaints.setImageStatus(true);
			complaints.setAttachments(blob);
			complaints.setEmployeeId((employeeId != null) ? employeeId : 0);
			complaints.setComplaintsAgainst((complaintsAgainst != null) ? complaintsAgainst : null);
			complaints.setTraineeId((traineeId != null) ? traineeId : 0);
			complaints.setComplaintsTitle(complaintsTitle);
			complaints.setComplaintsDate(date);
			complaints.setComplaintsAgainstName(complaintsAgainstName);
			complaints.setDescription(description);
	
			service.saveOrUpdate(complaints);	
			int randomNumber = generateRandomNumber();
			String imageUrl = "complaints/" + randomNumber + "/" + complaints.getComplaintsId();
			complaints.setAttachmentsUrl(imageUrl);
			service.saveOrUpdate(complaints);
			sendEmailToLeave(complaints);
			return ResponseEntity.ok("Complaints saved with id: " + complaints.getComplaintsId());
		} catch (IOException | SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving Complaints: " + e.getMessage());
		}
	}

	public void sendEmailToLeave(Complaints complaints) {
	    try {
	        String email;
	        String name;
	        String subject = "Your Subject"; // Replace with your subject
	        Date dateRange = complaints.getComplaintsDate();
	        String status = complaints.getDescription();
	        String complaintsAgainstName = complaints.getComplaintsAgainstName();


	        if (complaints.getEmployeeId() != null && complaints.getEmployeeId() != 0) {
	            Employee employee = employeeService.getEmployeeById(complaints.getEmployeeId());
	            email = employee.getEmail();
	            name = employee.getUserName();
	        } else if (complaints.getTraineeId() != null && complaints.getTraineeId() != 0) {
	            Training trainee = trainingService.findById(complaints.getTraineeId());
	            email = trainee.getEmail();
	            name = trainee.getUserName();
	        } else {
	            return; 
	        }

	        MimeMessage message = emailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	        helper.setTo(email);
	        helper.setFrom(new InternetAddress(from,name)); // Set sender name here
	        helper.setSubject(subject);


	        String imageUrl = complaints.getAttachmentsUrl();
	        String emailContent = "Dear " + name + ",<br/><br/>"
	                + "Your complaints application for " + dateRange + " has been submitted against " + complaintsAgainstName + " successfully.<br/><br/>"
	                + "Please wait for " + status + " approval from the HR team.<br/><br/>"
	                + "<img src=\"" + imageUrl + "\" alt=\"Attachment\"><br/><br/>"
	                + "Regards,<br/>HR Team";
	        helper.setText(emailContent, true); // Set HTML to true

	        emailSender.send(message);
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	        e.printStackTrace(); 
	    }
	}

	@PutMapping("/complaints/or/{id}")
	public ResponseEntity<Boolean> toggleComplaintsStatus(@PathVariable(name = "id") long id) {
		try {
			Complaints complaints = service.getById(id);
			if (complaints != null) {
				// Toggle the status
				boolean currentStatus = complaints.isStatus();
				complaints.setStatus(!currentStatus);
				service.saveOrUpdate(complaints); // Save the updated complaints
			} else {
				return ResponseEntity.ok(false); // Complaints with the given ID does not exist, return false
			}

			return ResponseEntity.ok(complaints.isStatus()); // Return the new status (true or false)
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false); // Set response to false in case
																						// of an error
		}
	}
	 
	@GetMapping("/complaints")
	public ResponseEntity<?> displayAllImages(@RequestParam(required = true) String viewType) {
		try {
			if ("complaints".equals(viewType)) {
				List<Complaints> images = service.listAll();

				List<Complaints> imageObjects = new ArrayList<>();
				for (Complaints image : images) {
					int randomNumber = generateRandomNumber();
					String imageUrl = "complaints/" + randomNumber + "/" + image.getComplaintsId();
					Complaints imageObject = new Complaints();
					imageObject.setComplaintsId(image.getComplaintsId());
					imageObject.setEmployeeId(image.getEmployeeId());
					imageObject.setComplaintsAgainst(image.getComplaintsAgainst());
					imageObject.setComplaintsDate(image.getComplaintsDate());
					imageObject.setComplaintsTitle(image.getComplaintsTitle());
					imageObject.setDescription(image.getDescription());
					imageObject.setComplaintsAgainst(image.getComplaintsAgainst());
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
		return random.nextInt(1000000); // Change the upper limit if needed
	}

	@GetMapping("complaints/{randomNumber}/{id}")
	public ResponseEntity<ByteArrayResource> serveFile(@PathVariable("randomNumber") int randomNumber,
			@PathVariable("id") Long id) {
		Optional<Complaints> complaintsOptional = service.getById1(id);
		if (complaintsOptional.isPresent()) {
			Complaints complaints = complaintsOptional.get();
			String filename = "file_" + randomNumber + "_" + id;
			byte[] fileBytes;
			try {
				fileBytes = complaints.getAttachments().getBytes(1, (int) complaints.getAttachments().length());
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

	@PutMapping("/complaints/edit/{id}")
	public ResponseEntity<?> updateComplaints(@PathVariable Long id,
	        @RequestParam(value = "attachments", required = false) MultipartFile file,
	        @RequestParam(value = "employeeId", required = false) Long employeeId,
	        @RequestParam(value = "complaintsTitle", required = false) String complaintsTitle,
	        @RequestParam(value = "complaintsAgainst", required = false) Long complaintsAgainst,
	        @RequestParam(value = "complaintsAgainstName", required = false) String complaintsAgainstName,
	        @RequestParam(value = "imageStatus", required = false) Boolean imageStatus,
	        @RequestParam(value = "description", required = false) String description,
	        @RequestParam(value = "traineeId", required = false) Long traineeId) {
	    try {
	        Complaints existingComplaints = service.getById(id);

	        if (existingComplaints == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Complaint not found.");
	        }
	  	  if (employeeId != null && complaintsAgainst != null && employeeId.equals(complaintsAgainst)) {
	            return ResponseEntity.badRequest().body("employeeId and complaintsAgainst cannot be the same.");
	        }
	        Date date = new Date(System.currentTimeMillis());
	        
	        existingComplaints.setDescription(description);
	        existingComplaints.setComplaintsAgainst(complaintsAgainst);
	        existingComplaints.setComplaintsAgainstName(description);
	        existingComplaints.setEmployeeId((employeeId != null) ? employeeId : 0);
	        existingComplaints.setTraineeId((traineeId != null) ? traineeId : 0);
	        existingComplaints.setStatus(true);
	        existingComplaints.setComplaintsDate(date);
	        existingComplaints.setComplaintsTitle(complaintsTitle);
	        existingComplaints.setComplaintsAgainstName(complaintsAgainstName);
	        if (imageStatus != null) {
	            existingComplaints.setImageStatus(imageStatus.booleanValue());
	        }

	        if (file != null && !file.isEmpty()) {
	            byte[] bytes = file.getBytes();
	            Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
	            existingComplaints.setAttachments(blob);
	        }
	        int randomNumber = generateRandomNumber();
			String imageUrl = "complaints/" + randomNumber + "/" + id;
			existingComplaints.setAttachmentsUrl(imageUrl);
	        service.saveOrUpdate(existingComplaints);

	        return ResponseEntity.ok("Complaint updated successfully. Complaint ID: " + id);
	    } catch (IOException | SQLException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating complaint.");
	    }
	}

	@DeleteMapping("/complaints/delete/{id}")
	public ResponseEntity<String> deleteComplaints(@PathVariable("id") long id) {
		try {
			service.deleteById(id);
			return ResponseEntity.ok("Complaints deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting Complaints: " + e.getMessage());
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

	@GetMapping("/complaints/view")
	public ResponseEntity<?> getTaskAssignedToProjectHeadINTask(@RequestParam(required = true) String type) {
		try {
			if ("complaintstable".equals(type)) {
				List<Map<String, Object>> tasks = repo.AllComplaints();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();
					int randomNumber = generateRandomNumber();

					if (taskAssigned.containsKey("employeeId") && taskAssigned.get("employeeId") != null) {

						String fileExtension = getFileExtensionForImage(taskAssigned);
						String imageUrl1 = "profile/" + randomNumber + "/" + taskAssigned.get("employeeId") + "."
								+ fileExtension;
						taskAssignedMap.put("profile", imageUrl1);
						 if (taskAssigned.containsKey("imageStatus") && (Boolean) taskAssigned.get("imageStatus").equals(true)) {
			                    String imageUrl = "complaints/" + randomNumber + "/" + taskAssigned.get("complaintsId");
			                    taskAssignedMap.put("attachments", imageUrl);
			                }
					}
					else if (taskAssigned.containsKey("traineeId") && taskAssigned.get("traineeId") != null) {
				
						String imageUrl11 = "training/" + randomNumber + "/" + taskAssigned.get("traineeId");
						taskAssignedMap.put("profile", imageUrl11);
						 if (taskAssigned.containsKey("imageStatus") && (Boolean) taskAssigned.get("imageStatus").equals(true)) {
			                    String imageUrl = "complaints/" + randomNumber + "/" + taskAssigned.get("complaintsId");
			                    taskAssignedMap.put("attachments", imageUrl);
			                }
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

	@GetMapping("/complaints1/view")
	public ResponseEntity<List<Map<String, Object>>> displayAllImages1(@RequestParam(required = true) String type) {
		try {
			if ("complaintstable".equals(type)) {
				List<Map<String, Object>> images = repo.AllComplaints();
				List<Map<String, Object>> imageResponses = new ArrayList<>();

				for (Map<String, Object> image : images) {
					int randomNumber = generateRandomNumber();

					Map<String, Object> imageResponse = new HashMap<>();
					if (image.get("employee_id") != null) {
						String imageUrl = "complaints/" + randomNumber + "/" + image.get("complaints_id");
						String fileExtension = getFileExtensionForImage(image);
						String imageUrl1 = "profile/" + randomNumber + "/" + image.get("employee_id") + "."
								+ fileExtension;

						imageResponse.put("profile", imageUrl1);
						imageResponse.put("complaintsId", image.get("complaints_id"));
						imageResponse.put("employeeId", image.get("employee_id"));
						imageResponse.put("id", image.get("employee_id"));
						imageResponse.put("complaintsAgainst", image.get("complaints_against"));
						imageResponse.put("complaintsTitle", image.get("complaints_title"));
						imageResponse.put("description", image.get("description"));
						imageResponse.put("complaintsDate", image.get("complaints_date"));
						imageResponse.put("attachments", imageUrl);
						imageResponse.put("userName", image.get("user_name"));
						imageResponse.put("userId", image.get("user_id"));
						imageResponse.put("complaintsAgainstName", image.get("complaints_against_name"));
						imageResponse.put("status", image.get("status"));
						imageResponse.put("roleId", image.get("role_id"));
						imageResponse.put("roleName", image.get("role_name"));
					} else if (image.get("trainee_id") != null) {
						String imageUrl = "complaints/" + randomNumber + "/" + image.get("complaints_id");
						String fileExtension = getFileExtensionForImage(image);
						String imageUrl1 = "training/" + randomNumber + "/" + image.get("trainee_id");

						imageResponse.put("profile", imageUrl1);
						imageResponse.put("complaintsId", image.get("complaints_id"));
						imageResponse.put("traineeId", image.get("trainee_id"));
						imageResponse.put("id", image.get("trainee_id"));
						imageResponse.put("complaintsAgainst", image.get("complaints_against"));
						imageResponse.put("complaintsTitle", image.get("complaints_title"));
						imageResponse.put("description", image.get("description"));
						imageResponse.put("complaintsDate", image.get("complaints_date"));
						imageResponse.put("attachments", imageUrl);
						imageResponse.put("userName", image.get("user_name"));
						imageResponse.put("userId", image.get("user_id"));
						imageResponse.put("complaintsAgainstName", image.get("complaints_against_name"));
						imageResponse.put("status", image.get("status"));
						imageResponse.put("roleId", image.get("role_id"));
						imageResponse.put("roleName", image.get("role_name"));
					}

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

	@GetMapping("/complaints/employee/{id}/{roleId}")
	public ResponseEntity<Object> getHrInterviewDetailsWithId(@PathVariable("id") long id,
			@PathVariable("roleId") long roleId) {
		List<Map<String, Object>> training = repo.Allcomplaintstraining(id, roleId);
		Map<String, List<Map<String, Object>>> trainingGroupMap = training.stream()
				.collect(Collectors.groupingBy(action -> String.valueOf(action.get("complaints_id"))));

		List<Map<String, Object>> emp = repo.Allcomplaints(id, roleId);
		Map<String, List<Map<String, Object>>> empGroupMap = emp.stream()
				.collect(Collectors.groupingBy(action -> String.valueOf(action.get("complaints_id"))));

		if (!training.isEmpty() && training.get(0).get("trainee_id") != null
				&& training.get(0).get("role_id") != null) {
			List<Map<String, Object>> traineeList = new ArrayList<>();

			for (Entry<String, List<Map<String, Object>>> trainingLoop : trainingGroupMap.entrySet()) {

				Map<String, Object> trainingMap = new HashMap<>();
				trainingMap.put("traineeId", trainingLoop.getValue().get(0).get("trainee_id"));
				trainingMap.put("userId", trainingLoop.getValue().get(0).get("user_id"));
				trainingMap.put("complaintsId", trainingLoop.getValue().get(0).get("complaints_id"));
				trainingMap.put("complaintsAgainst", trainingLoop.getValue().get(0).get("complaints_against"));
				trainingMap.put("complaintsTitle", trainingLoop.getValue().get(0).get("complaints_title"));
				trainingMap.put("description", trainingLoop.getValue().get(0).get("description"));
				trainingMap.put("complaintsDate", trainingLoop.getValue().get(0).get("complaints_date"));
				trainingMap.put("userName", trainingLoop.getValue().get(0).get("user_name"));
				trainingMap.put("imageStatus", trainingLoop.getValue().get(0).get("imageStatus"));
				trainingMap.put("complaintsAgainstName", trainingLoop.getValue().get(0).get("complaints_against_name"));
				trainingMap.put("status", trainingLoop.getValue().get(0).get("status"));

				int randomNumber = generateRandomNumber();
				if (trainingLoop.getValue().get(0).get("imageStatus").equals(true)) {
				    Object complaintId = trainingLoop.getValue().get(0).get("complaints_id");
				    String imageUrl = "complaints/" + randomNumber + "/" + complaintId;
				    trainingMap.put("attachments", imageUrl);
				}

				traineeList.add(trainingMap);
			
		}
			return ResponseEntity.ok(traineeList);
		} else if (!emp.isEmpty() && emp.get(0).get("employee_id") != null && emp.get(0).get("role_id") != null) {

			List<Map<String, Object>> employeeList = new ArrayList<>();
			for (Entry<String, List<Map<String, Object>>> empLoop : empGroupMap.entrySet()) {

				Map<String, Object> employeeMap = new HashMap<>();
				employeeMap.put("employeeId", empLoop.getValue().get(0).get("employee_id"));
				employeeMap.put("userId", empLoop.getValue().get(0).get("user_id"));
				employeeMap.put("complaintsId", empLoop.getValue().get(0).get("complaints_id"));
				employeeMap.put("complaintsAgainst", empLoop.getValue().get(0).get("complaints_against"));
				employeeMap.put("complaintsTitle", empLoop.getValue().get(0).get("complaints_title"));
				employeeMap.put("description", empLoop.getValue().get(0).get("description"));
				employeeMap.put("complaintsDate", empLoop.getValue().get(0).get("complaints_date"));
				employeeMap.put("userName", empLoop.getValue().get(0).get("user_name"));
				employeeMap.put("complaintsAgainstName", empLoop.getValue().get(0).get("complaints_against_name"));
				employeeMap.put("status", empLoop.getValue().get(0).get("status"));

				int randomNumber = generateRandomNumber();
				if (empLoop.getValue().get(0).get("imageStatus").equals(true)) {
				Object complaintId = empLoop.getValue().get(0).get("complaints_id");
				String imageUrl = "complaints/" + randomNumber + "/" + complaintId;
				employeeMap.put("attachments", imageUrl);}
				employeeList.add(employeeMap);

			}

			return ResponseEntity.ok(employeeList);
		} else {
			String errorMessage = "The Id does not exist";
			return ResponseEntity.ok(Collections.emptyList());
		}
	}

	@GetMapping("/complaints/dashboard")
	public ResponseEntity<List<Map<String, Object>>> displayAllImages1222(
			@RequestParam(required = true) String complaints) {
		try {
			if ("complaints".equals(complaints)) {
				List<Map<String, Object>> images = repo.AllComplaints111();
				if (!images.isEmpty() && images.get(0).get("employeeId") != null
						&& images.get(0).get("roleId") != null) {
					return ResponseEntity.ok(images);
				}
				List<Map<String, Object>> imageResponses = new ArrayList<>();

				for (Map<String, Object> image : images) {
					int randomNumber = generateRandomNumber();
					String imageUrl = "complaints/" + randomNumber + "/" + image.get("complaints_id");

					String fileExtension = getFileExtensionForImage(image);
					String imageUrl1 = "profile/" + randomNumber + "/" + image.get("employee_id") + "." + fileExtension;
					Map<String, Object> imageResponse = new HashMap<>();
					imageResponse.put("profile", imageUrl1);
					imageResponse.put("complaintsId", image.get("complaints_id"));
					imageResponse.put("employeeId", image.get("employee_id"));
					imageResponse.put("complaintsAgainst", image.get("complaints_against"));
					imageResponse.put("complaintsTitle", image.get("complaints_title"));
					imageResponse.put("description", image.get("description"));
					imageResponse.put("complaintsDate", image.get("complaints_date"));
					imageResponse.put("attachments", imageUrl);
					imageResponse.put("userName", image.get("user_name"));
					imageResponse.put("userId", image.get("user_id"));
					imageResponse.put("complaintsAgainstName", image.get("complaints_against_name"));
					imageResponse.put("status", image.get("status"));
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

	@PostMapping("/complaints/manager")
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
				List<Map<String, Object>> images = repo.getAllcomplaintsDate(startDate, endDate);
				List<Map<String, Object>> imageResponses = new ArrayList<>();

				for (Map<String, Object> image : images) {
					int randomNumber = generateRandomNumber();
					String imageUrl = "complaints/" + randomNumber + "/" + image.get("complaints_id");
					String fileExtension = getFileExtensionForImage(image);
					String imageUrl1 = "profile/" + randomNumber + "/" + image.get("employee_id") + "." + fileExtension;
					Map<String, Object> imageResponse = new HashMap<>();
					imageResponse.put("profile", imageUrl1);
					imageResponse.put("attachments", imageUrl);
					imageResponse.putAll(image);
					imageResponses.add(imageResponse);
				}

				return ResponseEntity.ok(imageResponses);
			}
			break;

		case "year":
			if (requestBody.containsKey("year")) {
				String year = requestBody.get("year").toString();
				List<Map<String, Object>> leaveData = repo.getAllcomplaintsYear(year);
				List<Map<String, Object>> imageResponses = new ArrayList<>();
				for (Map<String, Object> image : leaveData) {
					int randomNumber = generateRandomNumber();
					String imageUrl = "complaints/" + randomNumber + "/" + image.get("complaints_id");
					String fileExtension = getFileExtensionForImage(image);
					String imageUrl1 = "profile/" + randomNumber + "/" + image.get("employee_id") + "." + fileExtension;
					Map<String, Object> imageResponse = new HashMap<>();
					imageResponse.put("profile", imageUrl1);
					imageResponse.put("attachments", imageUrl);
					imageResponse.putAll(image);
					imageResponses.add(imageResponse);
				}

				return ResponseEntity.ok(imageResponses);

			}
			break;

		case "month":
			if (requestBody.containsKey("monthName")) {
				String monthName = requestBody.get("monthName").toString();
				List<Map<String, Object>> leaveData1 = repo.getAllcomplaints(monthName);
				List<Map<String, Object>> imageResponses = new ArrayList<>();

				for (Map<String, Object> image : leaveData1) {
					int randomNumber = generateRandomNumber();
					String imageUrl = "complaints/" + randomNumber + "/" + image.get("complaints_id");
					String fileExtension = getFileExtensionForImage(image);
					String imageUrl1 = "profile/" + randomNumber + "/" + image.get("employee_id") + "." + fileExtension;
					Map<String, Object> imageResponse = new HashMap<>();
					imageResponse.put("profile", imageUrl1);
					imageResponse.put("attachments", imageUrl);
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

	@PostMapping("/complaints/trainee/manager")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndYearOrMonthTrainees(
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
				List<Map<String, Object>> images = repo.getAllcomplaintsDate(startDate, endDate);
				List<Map<String, Object>> imageResponses = new ArrayList<>();

				for (Map<String, Object> image : images) {
					int randomNumber = generateRandomNumber();
					String imageUrl = "complaints/" + randomNumber + "/" + image.get("complaintsId");
					String imageUrl1 = "training/" + randomNumber + "/" + image.get("traineeId");
					Map<String, Object> imageResponse = new HashMap<>();
					imageResponse.put("profile", imageUrl1);
					imageResponse.put("attachments", imageUrl);
					imageResponse.putAll(image);
					imageResponses.add(imageResponse);
				}

				return ResponseEntity.ok(imageResponses);
			}
			break;

		case "year":
			if (requestBody.containsKey("year")) {
				String year = requestBody.get("year").toString();
				List<Map<String, Object>> leaveData = repo.getAllcomplaintsYeartrainee(year);
				List<Map<String, Object>> imageResponses = new ArrayList<>();
				for (Map<String, Object> image : leaveData) {
					int randomNumber = generateRandomNumber();
					String imageUrl = "complaints/" + randomNumber + "/" + image.get("complaintsId");
					String imageUrl1 = "training/" + randomNumber + "/" + image.get("traineeId");
					Map<String, Object> imageResponse = new HashMap<>();
					imageResponse.put("profile", imageUrl1);
					imageResponse.put("attachments", imageUrl);
					imageResponse.putAll(image);
					imageResponses.add(imageResponse);
				}

				return ResponseEntity.ok(imageResponses);

			}
			break;

		case "month":
			if (requestBody.containsKey("monthName")) {
				String monthName = requestBody.get("monthName").toString();
				List<Map<String, Object>> leaveData1 = repo.getAllcomplaintstrainee(monthName);
				List<Map<String, Object>> imageResponses = new ArrayList<>();

				for (Map<String, Object> image : leaveData1) {
					int randomNumber = generateRandomNumber();
					String imageUrl = "complaints/" + randomNumber + "/" + image.get("complaintsId");
					String imageUrl1 = "training/" + randomNumber + "/" + image.get("traineeId");
					Map<String, Object> imageResponse = new HashMap<>();
					imageResponse.put("profile", imageUrl1);
					imageResponse.put("attachments", imageUrl);
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

	@PostMapping("/complaints/datesmonth")
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
				return ResponseEntity.ok(repo.getAllReceiptcomplaints(startDate, endDate, employeeId, roleId));
			}
			break;

		case "year":
			if (requestBody.containsKey("year")) {
				String year = requestBody.get("year").toString();
				List<Map<String, Object>> leaveData = repo.getAllLeavecomplaints(year, employeeId, roleId);
				return ResponseEntity.ok(leaveData);
			}
			break;

		case "month":
			if (requestBody.containsKey("monthName")) {
				String monthName = requestBody.get("monthName").toString();
				List<Map<String, Object>> leaveData1 = repo.getAllcomplaints(monthName, employeeId, roleId);
				return ResponseEntity.ok(leaveData1);
			}
			break;

		default:
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.badRequest().build();
	}

	@PatchMapping("/complaints/edit/{id}")
	public ResponseEntity<?> updateDesignation(@PathVariable("id") Long complaintsid,
			@RequestBody Complaints designationDetails) {

		try {
			Complaints existingdesignation = service.getById(complaintsid);

			if (existingdesignation == null) {
				return ResponseEntity.notFound().build();
			}
			existingdesignation.setReason(designationDetails.getReason());

			service.saveOrUpdate(existingdesignation);

			return ResponseEntity.ok(existingdesignation);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/complaints/trainee")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndYearOrMonthGJKfghju567u(
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
				return ResponseEntity.ok(repo.getAllReceiptcomplaintstrainee(startDate, endDate, employeeId, roleId));
			}
			break;

		case "year":
			if (requestBody.containsKey("year")) {
				String year = requestBody.get("year").toString();
				List<Map<String, Object>> leaveData = repo.getAllLeavecomplaintstrainee(year, employeeId, roleId);
				return ResponseEntity.ok(leaveData);
			}
			break;

		case "month":
			if (requestBody.containsKey("monthName")) {
				String monthName = requestBody.get("monthName").toString();
				List<Map<String, Object>> leaveData1 = repo.getAllcomplaintstrainee(monthName, employeeId, roleId);
				return ResponseEntity.ok(leaveData1);
			}
			break;

		default:
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.badRequest().build();
	}
}
