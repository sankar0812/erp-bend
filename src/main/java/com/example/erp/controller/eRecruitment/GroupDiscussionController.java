package com.example.erp.controller.eRecruitment;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.erp.entity.erecruitment.Candidate;
import com.example.erp.entity.erecruitment.GroupDiscussion;
import com.example.erp.entity.erecruitment.HrInterview;
import com.example.erp.entity.erecruitment.InterviewSchedule;
import com.example.erp.entity.erecruitment.TaskAssigned;
import com.example.erp.repository.erecruitment.GroupDiscussionRepository;
import com.example.erp.service.eRecruitment.CandidateService;
import com.example.erp.service.eRecruitment.GroupDiscussionService;
import com.example.erp.service.eRecruitment.TaskAssignedService;

@RestController
@CrossOrigin
public class GroupDiscussionController {

	@Autowired
	private GroupDiscussionService groupDiscussionService;
	
	@Autowired
	private TaskAssignedService taskAssignedService;

	@Autowired
	private CandidateService candidateService;

	@Autowired
	private GroupDiscussionRepository groupDiscussionRepository;

	@Autowired
	private JavaMailSender emailSender;

	@Value("${spring.mail.username}")
	private String from;

	@GetMapping("/groupDiscussion")
	public ResponseEntity<Object> getGroupDiscussion(@RequestParam(required = true) String groupDiscussion) {
		if ("groupDiscussionDetails".equals(groupDiscussion)) {
			return ResponseEntity.ok(groupDiscussionService.listAll());
		} else {
			String errorMessage = "Invalid value for 'Discussion'. Expected 'groupDiscussion'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@PostMapping("/groupDiscussion/save")
	public ResponseEntity<?> saveDepartment(@RequestBody GroupDiscussion groupDiscussion) {
		try {
			groupDiscussion.setApprovalType("scheduled");
			groupDiscussion.setScheduled(true);
			groupDiscussion.setStatus(true);
			long candidateId = groupDiscussion.getCandidateId();
			String top =groupDiscussion.getTopic();
	Optional<GroupDiscussion> schedule =groupDiscussionRepository.findByCandidateIdAndTopic(candidateId,top);
			
			if (schedule.isPresent()) {
				return ResponseEntity.badRequest().body("same entry not allowed");
			}
			
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			
			


			java.util.Date currentDateMinusOneUtil = calendar.getTime();
			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());

			if (groupDiscussion.getDate() != null && groupDiscussion.getDate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
		
			TaskAssigned interviewSchedule =taskAssignedService.getByCandidateId(candidateId);

			if (interviewSchedule.getDate() != null && groupDiscussion.getDate() != null
					&& interviewSchedule.getDate().after(groupDiscussion.getDate())) {
				String errorMessage = "task Date cannot be later than Discussion Date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
			try {
				if (groupDiscussion.getTime() != null && !groupDiscussion.getTime().isEmpty()) {
					String formattedIntime = formatTime(groupDiscussion.getTime(), timeFormatter);
					groupDiscussion.setTime(formattedIntime);
				}

			} catch (Exception e) {

			}

			groupDiscussionService.SaveGroupDiscussion(groupDiscussion);
			sendEmailToCandidate(groupDiscussion);
			long id = groupDiscussion.getGroupDiscussionId();
			return ResponseEntity.status(HttpStatus.OK).body("groupDiscussion details saved successfully." + id);
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "An error occurred while saving groupDiscussion details.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	private void sendEmailToCandidate(GroupDiscussion groupDiscussion) {
		try {
			long candidateId = groupDiscussion.getCandidateId();
			Candidate candidate = candidateService.findById(candidateId);
			String email = candidate.getEmailId();
			String name = candidate.getUserName();

			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(email);
			helper.setSubject("Group Discussion Scheduled");

			String emailContent = "Dear " + name + ",\n\n" + "Your group discussion has been scheduled for "
					+ groupDiscussion.getDate() + " at " + groupDiscussion.getTime() + ".\n\n"
					+ "Please be prepared and arrive on time.\n\n" + "Regards,\nHR Team";
			helper.setText(emailContent);

			emailSender.send(message);
		} catch (Exception e) {
			// Handle email sending failure
		}
	}

	private String formatTime(String time, SimpleDateFormat formatter) throws Exception {
		SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
		java.util.Date date = inputFormat.parse(time);
		return formatter.format(date);
	}

	@PutMapping("/groupDiscussion/edit/{id}")
	public ResponseEntity<?> updateApplication(@PathVariable("id") long id, @RequestBody GroupDiscussion application) {
		try {
			// Retrieve the existing GroupDiscussion entity based on the ID
			GroupDiscussion existingApplication = groupDiscussionService.getByCandidateId(id);

			// Check if the GroupDiscussion entity exists
			if (existingApplication == null) {
				return ResponseEntity.notFound().build();
				
				
			}Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			
	
//			if (application.getTime() != null) {
//			    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
//
//			    try {
//			        // Convert input string to lowercase to match "hh:mm a" pattern if needed
//			        String timeInput = application.getTime().toLowerCase(); // Adjust to "11:54 am" if input is "11:54 AM"
//
//			        // Parse the string as LocalTime
//			        LocalTime startTime = LocalTime.parse(timeInput, formatter);
//
//			        LocalTime currentTime = LocalTime.now();
//
//			        if (!startTime.isAfter(currentTime)) {
//			            String errorMessage = "Current time must be later than the start time.";
//			            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//			        }
//			    } catch (DateTimeParseException e) {
//			        e.printStackTrace();
//			        String errorMessage = "Error parsing time";
//			        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//			    }
//			}

			java.util.Date currentDateMinusOneUtil = calendar.getTime();
			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());

			if (application.getDate() != null && application.getDate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			
			long candidateId1 = application.getCandidateId();

			
			TaskAssigned interviewSchedule =taskAssignedService.getByCandidateId(candidateId1);

			
			
			if (interviewSchedule.getDate() != null && application.getDate() != null
					&& interviewSchedule.getDate().after(application.getDate())) {
				String errorMessage = "task Date cannot be later than Discussion Date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			// Format the time if it is not null or empty
			SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
			try {
				if (existingApplication.getTime() != null && !existingApplication.getTime().isEmpty()) {
					String formattedIntime = formatTime(existingApplication.getTime(), timeFormatter);
					existingApplication.setTime(formattedIntime);
				}
			} catch (Exception e) {
				// Handle any exception that might occur during time formatting (you may want to
				// log it)
			}

			// Update the attributes of the existing GroupDiscussion entity
			existingApplication.setDate(application.getDate());
			existingApplication.setFeedback(application.getFeedback());
			existingApplication.setTime(application.getTime());
			existingApplication.setTopic(application.getTopic());
			existingApplication.setCandidateId(application.getCandidateId());

			// Save the updated GroupDiscussion entity
			groupDiscussionService.SaveGroupDiscussion(existingApplication);
			sendEmailToCandidate(existingApplication);
			return ResponseEntity.ok(existingApplication);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private void sendEmailToCandidatecancelled(GroupDiscussion groupDiscussion) {
		try {
			long candidateId = groupDiscussion.getCandidateId();
			Candidate candidate = candidateService.findById(candidateId);
			String email = candidate.getEmailId();
			String name = candidate.getUserName();

			// Create email content based on the interview type
			String interviewType = groupDiscussion.getApprovalType();
			String emailSubject = "";
			String emailContent = "";

			if ("cancelled".equals(interviewType)) {
				emailSubject = "GroupDiscussion Cancelled";
				emailContent = "Dear " + name + ",\n\n"
						+ "We regret to inform you that your interview has been cancelled.\n\n" + "Regards,\nHR Team";
			} else if ("completed".equals(interviewType)) {
				emailSubject = "GroupDiscussion Completed";
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

	@PatchMapping("/groupDiscussion/edit/{candidateid}")
	public ResponseEntity<?> updateDesignation(@PathVariable("candidateid") Long candidateid,
			@RequestBody GroupDiscussion requestBody) {

		try {
			GroupDiscussion existingCandidate = groupDiscussionService.getByCandidateId(candidateid);

			if (existingCandidate == null) {
				return ResponseEntity.notFound().build();
			}
			existingCandidate.setCancellationReason(requestBody.getCancellationReason());
			existingCandidate.setApprovalType(requestBody.getApprovalType());
			if ("cancelled".equals(existingCandidate.getApprovalType())) {
				existingCandidate.setCanceled(true);
				existingCandidate.setScheduled(false);
				existingCandidate.setCompleted(false);

			} else if ("completed".equals(existingCandidate.getApprovalType())) {
				existingCandidate.setCanceled(false);
				existingCandidate.setScheduled(false);
				existingCandidate.setCompleted(true);

			} else {
				existingCandidate.setCanceled(false);
				existingCandidate.setCompleted(false);
				existingCandidate.setScheduled(false);

			}

			groupDiscussionService.SaveGroupDiscussion(existingCandidate);
			sendEmailToCandidatecancelled(existingCandidate);
			return ResponseEntity.ok(existingCandidate);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

//	
//	@PatchMapping("/interview/edit1/{candidateid}")
//	public ResponseEntity<?> updateDesignation(
//	        @PathVariable("candidateid") Long candidateid,
//	        @RequestBody Candidate requestBody) {
//
//	    try {
//	        GroupDiscussion existingCandidate = groupDiscussionService.getByCandidateId(candidateid);
//
//	        if (existingCandidate == null) {
//	            return ResponseEntity.notFound().build();
//	        }
//
//	        
//
//	        groupDiscussionService.SaveGroupDiscussion(existingCandidate);
//
//	        return ResponseEntity.ok(existingCandidate);
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//	    }
//	}
//	@PutMapping("/groupDiscussion/{id}")
//	public ResponseEntity<GroupDiscussion> updateGroupDiscussion(@PathVariable("id") long id,
//			@RequestParam("candidateId") long candidateId, @RequestParam("topic") String topic,
//			@RequestParam("date") Date date, @RequestParam("time") Time time, @RequestParam("feedback") String feedback,
//			@RequestParam("rules") String rules, @RequestParam("format") String format,
//			@RequestParam("taskId") Long taskId,
//			@RequestParam(value = "started", defaultValue = "false") boolean started,
//			@RequestParam(value = "inProgress", defaultValue = "false") boolean inProgress,
//			@RequestParam(value = "completed", defaultValue = "false") boolean completed
//			) {
//		try {
//			GroupDiscussion existingDiscussion = groupDiscussionService.findById(id);
//			if (existingDiscussion == null) {
//				return ResponseEntity.notFound().build();
//			}
//
//			existingDiscussion.setCandidateId(candidateId);
//			existingDiscussion.setTopic(topic);
//			existingDiscussion.setDate(date);
//			existingDiscussion.setTime(time);
//			existingDiscussion.setFeedback(feedback);
//			existingDiscussion.setRules(rules);
//			existingDiscussion.setFormat(format);
//			existingDiscussion.setTaskId(taskId);		
//			existingDiscussion.setStatus(true);
//
//			if (started) {
//				existingDiscussion.setInProgress(false);
//				existingDiscussion.setCompleted(false);
//			} else if (inProgress) {
//				existingDiscussion.setStarted(false);
//				existingDiscussion.setCompleted(false);
//			} else if (completed) {
//				existingDiscussion.setStarted(false);
//				existingDiscussion.setInProgress(false);
//			}
//
//			groupDiscussionService.SaveGroupDiscussion(existingDiscussion);
//			return ResponseEntity.ok(existingDiscussion);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//	}

	@DeleteMapping("/groupDiscussion/delete/{id}")
	public ResponseEntity<String> deleteGroupDiscussion(@PathVariable("id") Long id) {
		groupDiscussionService.deletegroupDiscussionId(id);
		return ResponseEntity.ok("Group Discussion details deleted successfully");
	}

	@GetMapping("/groupDiscussionDetails")
	public ResponseEntity<Object> getGroupDiscussionDetails(@RequestParam(required = true) String groupDiscussion) {
		if ("groupDiscussionDetail".equals(groupDiscussion)) {
			List<Map<String, Object>> groupDiscussions = groupDiscussionRepository.findGroupDiscussionDetails();
			return ResponseEntity.ok(groupDiscussions);
		} else {
			String errorMessage = "Invalid value for 'groupDiscussion'. Expected 'groupDiscussion'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}

	}
}
