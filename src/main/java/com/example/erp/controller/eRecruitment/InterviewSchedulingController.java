
package com.example.erp.controller.eRecruitment;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import com.example.erp.entity.erecruitment.HrInterview;
import com.example.erp.entity.erecruitment.InterviewSchedule;
import com.example.erp.entity.erecruitment.TaskAssigned;
import com.example.erp.repository.erecruitment.InterviewSchedulingRepository;
import com.example.erp.repository.erecruitment.TaskAssignedRepository;
import com.example.erp.service.eRecruitment.CandidateService;
import com.example.erp.service.eRecruitment.InterviewSchedulingService;
import java.text.ParseException;

@RestController
@CrossOrigin
public class InterviewSchedulingController {
	@Autowired
	private InterviewSchedulingService interviewScheduleService;

	@Autowired
	private InterviewSchedulingRepository interviewSchedulingRepository;
	@Autowired
	private TaskAssignedRepository taskAssignedRepository;
	
	@Autowired
	private CandidateService candidateService;
	
	 @Autowired
	    private JavaMailSender emailSender;
	 
	 @Value("${spring.mail.username}")
	    private String from;

	@GetMapping("/interviewSchedule")
	public ResponseEntity<Object> getInterviewSchedule(@RequestParam(required = true) String interviewSchedule) {
		if ("interviewScheduleDetails".equals(interviewSchedule)) {
			return ResponseEntity.ok(interviewScheduleService.listAll());
		} else {
			String errorMessage = "Invalid value for 'interviewSchedule'. Expected 'interviewSchedule'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	


	
	@PostMapping("/interviewSchedule/save")
	public ResponseEntity<String> saveGroup1(@RequestBody InterviewSchedule interviewSchedule) {
	    try {
	    	
	    	Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			java.util.Date currentDateMinusOneUtil = calendar.getTime();
			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());
			if (interviewSchedule.getDate() != null && interviewSchedule.getDate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}  
		long id =	interviewSchedule.getCandidateId();
			long depId=interviewSchedule.getDepartmentId();
			
			Optional<InterviewSchedule> schedule =interviewSchedulingRepository.findByCandidateIdAndDepartmentId(id,depId);
			
			if (schedule.isPresent()) {
				return ResponseEntity.badRequest().body("same entry not allowed");
			}
			
			
	        interviewSchedule.setInterviewType("scheduled");
	        interviewSchedule.setScheduled(true);
	        interviewSchedule.setStatus(true);	        	       
	        if (interviewSchedule.getStartTime() != null &&
	                interviewSchedule.getEndTime() != null) {	           
	            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
	            Date startTime = sdf.parse(interviewSchedule.getStartTime());
	            Date endTime = sdf.parse(interviewSchedule.getEndTime());	            
	            if (startTime.after(endTime)) {
	                String errorMessage = "StartTime cannot be later than EndTime.";
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	            }
	        }	 
	        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
	        if (interviewSchedule.getStartTime() != null) {
	            try {
	                String formattedStartTime = formatTime(interviewSchedule.getStartTime(), timeFormatter);
	                interviewSchedule.setStartTime(formattedStartTime);
	            } catch (ParseException e) {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error formatting start time.");
	            }
	        }

	        if (interviewSchedule.getEndTime() != null) {
	            try {
	                String formattedEndTime = formatTime(interviewSchedule.getEndTime(), timeFormatter);
	                interviewSchedule.setEndTime(formattedEndTime);
	            } catch (ParseException e) {
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error formatting end time.");
	            }
	        }
	        interviewScheduleService.save(interviewSchedule);
	        sendEmailToCandidate(interviewSchedule);

	        long interviewScheduleId = interviewSchedule.getCandidateId();
	        return ResponseEntity.ok("Interview Schedule Saved Successfully with id: " + interviewScheduleId);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("Error occurred while saving interview schedule.");
	    }
	}

	 
	 private void sendEmailToCandidate(InterviewSchedule interviewSchedule) {
	        try {
	            long candidateId = interviewSchedule.getCandidateId();
	            Candidate candidate = candidateService.findById(candidateId);
	            String email = candidate.getEmailId();
	            String name = candidate.getUserName();

	            MimeMessage message = emailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setTo(email);
	            helper.setFrom(from);
	            helper.setSubject("Interview Scheduled");

	            String emailContent = "Dear " + name + ",\n\n"
	                    + "Your Interview has been scheduled for " + interviewSchedule.getDate()
	                    + " at " + interviewSchedule.getStartTime() + ".\n\n"
	                    + " at " + interviewSchedule.getEndTime() + ".\n\n"
	                    + "Please be prepared and arrive on time.\n\n"
	                    + "Regards,\nHR Team";
	            helper.setText(emailContent);

	            emailSender.send(message);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	private String formatTime(String time, SimpleDateFormat formatter) throws ParseException {
	    SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
	    java.util.Date date = inputFormat.parse(time);
	    return formatter.format(date);
	}


	@PatchMapping("/interviewSchedule/edit/{candidateid}")
	public ResponseEntity<?> updateDesignation(@PathVariable("candidateid") Long candidateid,
	        @RequestBody InterviewSchedule designationDetails) {

	    try {
	        InterviewSchedule existingdesignation = interviewScheduleService.getByCandidateId(candidateid);

	        if (existingdesignation == null) {
	            return ResponseEntity.notFound().build();
	        }
	        existingdesignation.setInterviewType(designationDetails.getInterviewType());

	        if ("cancelled".equals(existingdesignation.getInterviewType())) {
	            existingdesignation.setCanceled(true);
	            existingdesignation.setCompleted(false);
	            existingdesignation.setScheduled(false);
	        } else if ("completed".equals(existingdesignation.getInterviewType())) {
	            existingdesignation.setCanceled(false);
	            existingdesignation.setCompleted(true);
	            existingdesignation.setScheduled(false);
	        } else {
	            existingdesignation.setCanceled(false);
	            existingdesignation.setCompleted(false);
	            existingdesignation.setScheduled(false);
	        }

	        interviewScheduleService.save(existingdesignation);
	        sendEmailToCandidatecancelled (existingdesignation);

	        return ResponseEntity.ok(existingdesignation);

	    } catch (Exception e) {
	        // Handle or log the exception
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}

	private void sendEmailToCandidatecancelled(InterviewSchedule interviewSchedule) {
        try {
            long candidateId = interviewSchedule.getCandidateId();
            Candidate candidate = candidateService.findById(candidateId);
            String email = candidate.getEmailId();
            String name = candidate.getUserName();

            // Create email content based on the interview type
            String interviewType = interviewSchedule.getInterviewType();
            String emailSubject = "";
            String emailContent = "";

            if ("cancelled".equals(interviewType)) {
                emailSubject = "Interview Cancelled";
                emailContent = "Dear " + name + ",\n\n"
                        + "We regret to inform you that your interview has been cancelled.\n\n"
                        + "Regards,\nHR Team";
            } else if ("completed".equals(interviewType)) {
                emailSubject = "Interview Completed";
                emailContent = "Dear " + name + ",\n\n"
                        + "We are pleased to inform you that your interview has been completed successfully.\n\n"
                        + "Regards,\nHR Team";
            }

            // Send the email
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
	
	@PutMapping("/interviewSchedule/edit/{candidateid}")
	public ResponseEntity<?> updateDesignationp(@PathVariable("candidateid") Long candidateid,
	        @RequestBody InterviewSchedule designationDetails) {

	    try {
	        InterviewSchedule existingdesignation = interviewScheduleService.getByCandidateId(candidateid);

	        if (existingdesignation == null) {
	            return ResponseEntity.notFound().build();
	        }
	    	Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			java.util.Date currentDateMinusOneUtil = calendar.getTime();
			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());
			
	
	        existingdesignation.setCancellationReason(designationDetails.getCancellationReason());
	        existingdesignation.setEndTime(designationDetails.getEndTime());
	        existingdesignation.setDate(designationDetails.getDate());
	        existingdesignation.setDepartmentId(designationDetails.getDepartmentId());
	        existingdesignation.setEmployeeId(designationDetails.getEmployeeId());
	        existingdesignation.setInterviewType(designationDetails.getInterviewType());
	        existingdesignation.setStartTime(designationDetails.getStartTime());

	        existingdesignation.setInterviewType("scheduled");
	        existingdesignation.setScheduled(true);
	        if (designationDetails.getDate() != null && designationDetails.getDate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}  
	        if (existingdesignation.getStartTime() != null &&
	                existingdesignation.getEndTime() != null) {
	           
	            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
	            Date startTime = sdf.parse(existingdesignation.getStartTime());
	            Date endTime = sdf.parse(existingdesignation.getEndTime());

	    	            
		            if (startTime.after(endTime)) {
		                String errorMessage = "StartTime cannot be later than EndTime.";
		                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
		            }
		        	
	        }
	        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
	        try {
	            if (existingdesignation.getStartTime() != null && !existingdesignation.getStartTime().isEmpty()) {
	                String formattedIntime = formatTime(existingdesignation.getStartTime(), timeFormatter);
	                existingdesignation.setStartTime(formattedIntime);
	            }

	            if (existingdesignation.getEndTime() != null && !existingdesignation.getEndTime().isEmpty()) {
	                String formattedOuttime = formatTime(existingdesignation.getEndTime(), timeFormatter);
	                existingdesignation.setEndTime(formattedOuttime);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        interviewScheduleService.save(existingdesignation);
	        sendEmailToCandidate(existingdesignation);
	        return ResponseEntity.ok(existingdesignation);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}


	@DeleteMapping("/interviewSchedule/delete/{id}")
	public ResponseEntity<String> deleteInterviewSchedule(@PathVariable("id") Long id) {
		interviewScheduleService.deleteInterviewSchedulingId(id);
		return ResponseEntity.ok("InterviewSchedule details deleted successfully");
	}



	@GetMapping("/schedulingDetails")
	public ResponseEntity<Object> getInterviewSchedulingDetails(
			@RequestParam(required = true) String interviewSchedule) {
		if ("interviewschedulingDetails".equals(interviewSchedule)) {
			List<Map<String, Object>> interviewSchedules = interviewSchedulingRepository
					.FindInterviewSchedulingDetails();
			return ResponseEntity.ok(interviewSchedules);
		} else {
			String errorMessage = "Invalid value for 'interviewSchedule'. Expected 'interviewSchedule'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}
	
	
	@GetMapping("/schedulingDetails/true")
	public ResponseEntity<Object> getInterviewSchedulingDetails1(@RequestParam(required = true) String Schedule) {
		if ("schedulingDetails".equals(Schedule)) {
			List<Map<String, Object>> interviewSchedules = interviewSchedulingRepository
					.FindInterviewSchedulingDetails2();
			return ResponseEntity.ok(interviewSchedules);
		} else {
			String errorMessage = "Invalid value for 'interviewSchedule'. Expected 'interviewSchedule'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}
	
	@GetMapping("/schedulingDetails/candidate")
	public ResponseEntity<Object> getInterviewSchedulingDetails2(@RequestParam(required = true) String Candidate) {
		if ("schedulingDetails".equals(Candidate)) {
			List<Map<String, Object>> interviewSchedules = interviewSchedulingRepository
					.FindInterviewSchedulingDetails1();
			return ResponseEntity.ok(interviewSchedules);
		} else {
			String errorMessage = "Invalid value for 'interviewSchedule'. Expected 'interviewSchedule'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@GetMapping("notification/interviewScheduled")
	public List<Map<String, Object>> interviewScheduled() {
		return interviewSchedulingRepository.findInterviewScheduleNotification();
	}

}
