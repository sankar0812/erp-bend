package com.example.erp.controller.eRecruitment;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.*;
import java.util.Optional;
import java.util.Random;
import javax.mail.internet.MimeMessage;
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
import com.example.erp.entity.erecruitment.Candidate;
import com.example.erp.entity.erecruitment.InterviewSchedule;
import com.example.erp.entity.erecruitment.TaskAssigned;
import com.example.erp.repository.erecruitment.TaskAssignedRepository;
import com.example.erp.service.eRecruitment.CandidateService;
import com.example.erp.service.eRecruitment.InterviewSchedulingService;
import com.example.erp.service.eRecruitment.TaskAssignedService;

@RestController
@CrossOrigin
public class TaskAssignedController {

	@Autowired
	private TaskAssignedService taskAssignedService;

//	@Autowired
//	private GroupDiscussionService discussionService;
	
	@Autowired
	private CandidateService candidateService;

	@Autowired
	private InterviewSchedulingService interviewSchedulingService;
	
	@Autowired
	private JavaMailSender emailSender;

	@Value("${spring.mail.username}")
	private String from;
	@Autowired
	private TaskAssignedRepository taskAssignedRepository;

	@PostMapping("/taskAssigned/save")
	public ResponseEntity<?> saveTaskAssigned11(@RequestParam("date") Date date,
			@RequestParam("startTime") String startTime, @RequestParam("endTime") String endTime,
			@RequestParam("taskPriority") String taskPriority, @RequestParam("candidateId") long candidateId,
			@RequestParam("departmentId") long departmentId, @RequestParam("employeeId") long employeeId,
			@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam("taskFile") MultipartFile file) throws ParseException {
		try {
			byte[] bytes = file.getBytes();
			Blob blob = new SerialBlob(bytes);

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);

			java.util.Date currentDateMinusOneUtil = calendar.getTime();
			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());
			
			TaskAssigned taskAssigned = new TaskAssigned();
			
	Optional<TaskAssigned> schedule =taskAssignedRepository.findByCandidateIdAndDepartmentId(candidateId,departmentId);
			
			if (schedule.isPresent()) {
				return ResponseEntity.badRequest().body("same entry not allowed");
			}
			

			taskAssigned.setCandidateId(candidateId);
			taskAssigned.setDepartmentId(departmentId);
			taskAssigned.setEmployeeId(employeeId);
			taskAssigned.setTaskFile(blob);
			taskAssigned.setFileName(fileName);
			taskAssigned.setDate(date);
			taskAssigned.setTaskPriority(taskPriority);
			taskAssigned.setStartTime(startTime);
			taskAssigned.setEndTime(endTime);
			taskAssigned.setApprovalType("scheduled");
			taskAssigned.setScheduled(true);
			
		InterviewSchedule interviewSchedule =interviewSchedulingService.getByCandidateId(candidateId);
			
			interviewSchedule.getCandidateId();
			interviewSchedule.getDate();
	
			if (interviewSchedule.getDate() != null && taskAssigned.getDate() != null
					&& interviewSchedule.getDate().after(taskAssigned.getDate())) {
				String errorMessage = "Interview Date cannot be later than Task Date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			if (taskAssigned.getStartTime() != null && taskAssigned.getEndTime() != null) {
			    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
			    try {
			        // Parse the string as java.util.Date first
			        java.util.Date utilStartTime = sdf.parse(taskAssigned.getStartTime());
			        java.util.Date utilEndTime = sdf.parse(taskAssigned.getEndTime());

			        // Convert java.util.Date to java.sql.Date
			        java.sql.Date startTime1 = new java.sql.Date(utilStartTime.getTime());
			        java.sql.Date endTime1 = new java.sql.Date(utilEndTime.getTime());

			        if (startTime1.after(endTime1)) {
			            String errorMessage = "StartTime cannot be later than EndTime.";
			            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			        }
			    } catch (ParseException e) {
			        String errorMessage = "Error parsing dates";
			        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			    }
			}
			SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
			try {
				if (taskAssigned.getDate() != null && taskAssigned.getDate().before(currentDateMinusOne)) {
					String errorMessage = "FromDate cannot be earlier than the current date.";
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				}

				if (taskAssigned.getStartTime() != null && !taskAssigned.getStartTime().isEmpty()) {
					String formattedIntime = formatTime(taskAssigned.getStartTime(), timeFormatter);
					taskAssigned.setStartTime(formattedIntime);
				}

				if (taskAssigned.getEndTime() != null && !taskAssigned.getEndTime().isEmpty()) {
					String formattedOuttime = formatTime(taskAssigned.getEndTime(), timeFormatter);
					taskAssigned.setEndTime(formattedOuttime);
				}
			} catch (Exception e) {
			}

			taskAssignedService.SaveTaskAssigned(taskAssigned);

			sendEmailToCandidate(taskAssigned);
			long taskAssignedId = taskAssigned.getCandidateId();

			return ResponseEntity.ok("TaskAssigned saved successfully." + taskAssignedId);
		} catch (IOException | SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving the taskAssigned: " + e.getMessage());
		}
	}

	private void sendEmailToCandidate(TaskAssigned taskAssigned) {
		try {
			long candidateId = taskAssigned.getCandidateId();
			Candidate candidate = candidateService.findById(candidateId);
			String email = candidate.getEmailId();
			String name = candidate.getUserName();

			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(email);
			helper.setSubject("Task Assigned");

			String emailContent = "Dear " + name + ",\n\n" + "You have been assigned a task:\n\n" + "Date: "
					+ taskAssigned.getDate() + "\n" + "Time: " + taskAssigned.getStartTime() + " - "
					+ taskAssigned.getEndTime() + "\n" + "Task Priority: " + taskAssigned.getTaskPriority() + "\n\n"
					+ "Please review the task details and ensure timely completion.\n\n" + "Regards,\nHR Team";
			helper.setText(emailContent);

			emailSender.send(message);
		} catch (Exception e) {
			// Handle email sending failure
			e.printStackTrace();
		}
	}

	private String formatTime(String time, SimpleDateFormat formatter) throws Exception {
		SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
		java.util.Date date = inputFormat.parse(time);
		return formatter.format(date);
	}

	@GetMapping("/taskAssigned/view")
	public ResponseEntity<?> getTaskAssigned() {
		try {
			List<TaskAssigned> taskAssigned = taskAssignedService.listAll();
			List<TaskAssigned> taskAssignedList = new ArrayList<>();
			for (TaskAssigned tasks : taskAssigned) {
				String taskUrl = "/taskAssigned/" + tasks.getTaskId();

				TaskAssigned taskAssignedResponse = new TaskAssigned();
				taskAssignedResponse.setTaskId(tasks.getTaskId());
				taskAssignedResponse.setCandidateId(tasks.getCandidateId());
				taskAssignedResponse.setDate(tasks.getDate());
				taskAssignedResponse.setEndTime(tasks.getEndTime());
				taskAssignedResponse.setInterviewProcessId(tasks.getInterviewProcessId());

				taskAssignedResponse.setStartTime(tasks.getStartTime());
				taskAssignedResponse.setCompleted(tasks.isCompleted());
				taskAssignedResponse.setTaskPriority(tasks.getTaskPriority());
				taskAssignedResponse.setTaskAssignee(tasks.getTaskAssignee());
				taskAssignedResponse.setTaskUrl(taskUrl);

				taskAssignedList.add(taskAssignedResponse);
			}

			return ResponseEntity.ok(taskAssignedList);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while retrieving taskAssigned");
		}
	}

	@GetMapping("taskAssigned/{randomNumber}/{id}/{fileName}")
	public ResponseEntity<Resource> serveImage(@PathVariable("randomNumber") int randomNumber,
			@PathVariable("id") String id, @PathVariable("fileName") String fileName) {
		String combinedIdFileName = id + "." + fileName;

		String[] parts = combinedIdFileName.split("\\.");
		if (parts.length != 2) {
			return ResponseEntity.badRequest().build();
		}
		String fileExtension = parts[1];

		Long imageId;
		try {
			imageId = Long.parseLong(parts[0]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build(); // Invalid image ID format
		}
		Optional<TaskAssigned> optionalImage = taskAssignedService.getByCandidateId1(imageId);
		if (optionalImage.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		TaskAssigned image = optionalImage.get();
		byte[] imageBytes;
		try {
			imageBytes = image.getTaskFile().getBytes(1, (int) image.getTaskFile().length());
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		ByteArrayResource resource = new ByteArrayResource(imageBytes);
		HttpHeaders headers = new HttpHeaders();
		if ("jpg".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.IMAGE_JPEG);
		} else if ("png".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.IMAGE_PNG);
		} else if ("pdf".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.APPLICATION_PDF);
		} else if ("mp4".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.valueOf("video/mp4"));
		} else if ("avi".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.valueOf("video/avi"));
		} else if ("mov".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.valueOf("video/quicktime"));
		} else {
			// If the file extension is not recognized, return a 404 Not Found response
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().headers(headers).body(resource);
	}

	@PutMapping("/taskAssigned/edit/{candidateid}")
	public ResponseEntity<String> updateTaskAssigned(@PathVariable long candidateid,
			@RequestParam(value = "date", required = false) Date date,
			@RequestParam(value = "startTime", required = false) String startTime,
			@RequestParam(value = "endTime", required = false) String endTime,
			@RequestParam(value = "taskPriority", required = false) String taskPriority,
			@RequestParam(value = "departmentId", required = false) long departmentId,
			@RequestParam(value = "employeeId", required = false) long employeeId,
			@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam(value = "taskFile", required = false) MultipartFile file) {

		TaskAssigned taskAssigned = taskAssignedService.getByCandidateId(candidateid);

		if (taskAssigned == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("TaskAssigned not found for candidate ID: " + candidateid);
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);

		java.util.Date currentDateMinusOneUtil = calendar.getTime();
		Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());
		
		try {
			if (file != null) {
				byte[] bytes = file.getBytes();
				Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
				taskAssigned.setTaskFile(blob);
			}
			
			InterviewSchedule interviewSchedule =interviewSchedulingService.getByCandidateId(candidateid);
			
			interviewSchedule.getCandidateId();
			interviewSchedule.getDate();
	
			taskAssigned.setEmployeeId(employeeId);
			taskAssigned.setDepartmentId(departmentId);
			taskAssigned.setDate(date);
			taskAssigned.setStartTime(startTime);
			taskAssigned.setFileName(fileName);
			taskAssigned.setEndTime(endTime);
			taskAssigned.setTaskPriority(taskPriority);
			if (taskAssigned.getStartTime() != null && taskAssigned.getEndTime() != null) {
			    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
			    try {
			        // Parse the string as java.util.Date first
			        java.util.Date utilStartTime = sdf.parse(taskAssigned.getStartTime());
			        java.util.Date utilEndTime = sdf.parse(taskAssigned.getEndTime());

			        // Convert java.util.Date to java.sql.Date
			        java.sql.Date startTime1 = new java.sql.Date(utilStartTime.getTime());
			        java.sql.Date endTime1 = new java.sql.Date(utilEndTime.getTime());

			        if (startTime1.after(endTime1)) {
			            String errorMessage = "StartTime cannot be later than EndTime.";
			            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			        }
			    } catch (ParseException e) {
			        String errorMessage = "Error parsing dates";
			        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			    }
			}

			if (interviewSchedule.getDate() != null && taskAssigned.getDate() != null
					&& interviewSchedule.getDate().after(taskAssigned.getDate())) {
				String errorMessage = "Interview Date cannot be later than Task Date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
			try {
				if (taskAssigned.getDate() != null && taskAssigned.getDate().before(currentDateMinusOne)) {
					String errorMessage = "FromDate cannot be earlier than the current date.";
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				}

				if (taskAssigned.getStartTime() != null && !taskAssigned.getStartTime().isEmpty()) {
					String formattedIntime = formatTime(taskAssigned.getStartTime(), timeFormatter);
					taskAssigned.setStartTime(formattedIntime);
				}

				if (taskAssigned.getEndTime() != null && !taskAssigned.getEndTime().isEmpty()) {
					String formattedOuttime = formatTime(taskAssigned.getEndTime(), timeFormatter);
					taskAssigned.setEndTime(formattedOuttime);
				}
			} catch (Exception e) {
			}
			taskAssignedService.SaveTaskAssigned(taskAssigned);
			sendEmailToCandidate(taskAssigned);
			return ResponseEntity.ok("TaskAssigned updated successfully.");

		} catch (Exception e) {
			e.printStackTrace(); // Log the exception for debugging purposes
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating TaskAssigned.");
		}
	}

	@PatchMapping("/taskAssigned/{candidateid}")
	public ResponseEntity<?> updateDesignation(@PathVariable("candidateid") Long candidateid,
			@RequestBody TaskAssigned designationDetails) {

		try {
			TaskAssigned existingdesignation = taskAssignedService.getByCandidateId(candidateid);

			if (existingdesignation == null) {
				return ResponseEntity.notFound().build();
			}
			existingdesignation.setApprovalType(designationDetails.getApprovalType());
			existingdesignation.setCancellationReason(designationDetails.getCancellationReason());
			if ("cancelled".equals(existingdesignation.getApprovalType())) {
				existingdesignation.setCanceled(true);
				existingdesignation.setCompleted(false);
				existingdesignation.setScheduled(false);
			} else if ("completed".equals(existingdesignation.getApprovalType())) {
				existingdesignation.setCanceled(false);
				existingdesignation.setCompleted(true);
				existingdesignation.setScheduled(false);
			} else {
				existingdesignation.setCanceled(false);
				existingdesignation.setCompleted(false);
				existingdesignation.setScheduled(false);
			}

			taskAssignedService.SaveTaskAssigned(existingdesignation);
			sendEmailToCandidatecancelled(existingdesignation);

			return ResponseEntity.ok(existingdesignation);

		} catch (Exception e) {

			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private void sendEmailToCandidatecancelled(TaskAssigned taskassigned) {
		try {
			long candidateId = taskassigned.getCandidateId();
			Candidate candidate = candidateService.findById(candidateId);
			String email = candidate.getEmailId();
			String name = candidate.getUserName();

			// Create email content based on the interview type
			String interviewType = taskassigned.getApprovalType();
			String emailSubject = "";
			String emailContent = "";

			if ("cancelled".equals(interviewType)) {
				emailSubject = "TaskAssigned Cancelled";
				emailContent = "Dear " + name + ",\n\n"
						+ "We regret to inform you that your interview has been cancelled.\n\n" + "Regards,\nHR Team";
			} else if ("completed".equals(interviewType)) {
				emailSubject = "TaskAssigned Completed";
				emailContent = "Dear " + name + ",\n\n"
						+ "We are pleased to inform you that your interview has been completed successfully.\n\n"
						+ "Regards,\nHR Team";
			}

			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(email);
			helper.setSubject(emailSubject);
			helper.setText(emailContent);

			emailSender.send(message);
		} catch (Exception e) {
			// Handle email sending failure
			e.printStackTrace();
		}
	}

	@DeleteMapping("/taskAssigned/delete/{id}")
	public ResponseEntity<String> deletetaskAssigned(@PathVariable("id") Long id) {
		taskAssignedService.deleteTaskAssignedId(id);
		return ResponseEntity.ok("TaskAssigned details deleted successfully");
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000); // Change the upper limit if needed
	}

	@GetMapping("/findTaskAssignedDetails")
	public ResponseEntity<?> getTaskAssignedDetails(@RequestParam(required = true) String TaskAssigned) {
		try {
			if ("findTaskAssignedDetails".equals(TaskAssigned)) {
				List<Map<String, Object>> tasks = taskAssignedRepository.findTaskAssignedDetails();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();
					int randomNumber = generateRandomNumber();
					String imageUrl = "taskAssigned/" + randomNumber + "/" + taskAssigned.get("taskId") + "/"
							+ taskAssigned.get("fileName");
					taskAssignedMap.put("taskUrl", imageUrl);
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

}
