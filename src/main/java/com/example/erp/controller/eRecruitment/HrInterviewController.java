package com.example.erp.controller.eRecruitment;

import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.example.erp.entity.employee.Department;
import com.example.erp.entity.erecruitment.Application;
import com.example.erp.entity.erecruitment.Candidate;
import com.example.erp.entity.erecruitment.GroupDiscussion;
import com.example.erp.entity.erecruitment.HrInterview;
import com.example.erp.entity.erecruitment.InterviewSchedule;
import com.example.erp.repository.erecruitment.HrInterviewRepository;
import com.example.erp.service.eRecruitment.CandidateService;
import com.example.erp.service.eRecruitment.GroupDiscussionService;
import com.example.erp.service.eRecruitment.HrInterviewService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
@RestController
@CrossOrigin
public class HrInterviewController {
	@Autowired
	private HrInterviewService hrInterviewService;
	
	@Autowired
	private CandidateService candidateService;

	@Autowired
	private GroupDiscussionService groupDiscussionService;
	
	@Autowired
	private HrInterviewRepository hrInterviewRepository;
	
	@GetMapping("/hrInterview")
	public ResponseEntity<?> getApplications(@RequestParam(required = true) String hrInterview) {
	    try {
	        if ("hrInterview".equals(hrInterview)) {
	            List<HrInterview> application = hrInterviewService.listAll();
	            return ResponseEntity.ok(application);
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("The provided leave is not supported.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("Error retrieving Applications: " + e.getMessage());
	    }
	}


	

	 @Autowired
	    private JavaMailSender emailSender;
	 
	 @Value("${spring.mail.username}")
	    private String from;
	 

	    @PostMapping("/hrInterview/save")
	    public ResponseEntity<?> saveDepartment(@RequestBody HrInterview hrInterview) {
	        try {
	            Calendar calendar = Calendar.getInstance();
	            calendar.add(Calendar.DAY_OF_MONTH, -1);
	            java.util.Date currentDateMinusOneUtil = calendar.getTime();
	            Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());

	            if (hrInterview.getDate() != null && hrInterview.getDate().before(currentDateMinusOne)) {
	                String errorMessage = "FromDate cannot be earlier than the current date.";
	                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
	            }
	            long id =	hrInterview.getCandidateId();
				long depId=hrInterview.getDepartmentId();
				
				Optional<HrInterview> schedule =hrInterviewRepository.findByCandidateIdAndDepartmentId(id,depId);
				
				if (schedule.isPresent()) {
					return ResponseEntity.badRequest().body("same entry not allowed");
				}
				
					GroupDiscussion interviewSchedule =groupDiscussionService.getByCandidateId(id);

				if (interviewSchedule.getDate() != null && hrInterview.getDate() != null
						&& interviewSchedule.getDate().after(hrInterview.getDate())) {
					String errorMessage = "Discussion Date cannot be later than HR Interview Date.";
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				}

	            hrInterview.setApprovalType("scheduled");
	            hrInterview.setStatus(true);

	            SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
	            try {
	                if (hrInterview.getTime() != null && !hrInterview.getTime().isEmpty()) {
	                    String formattedIntime = formatTime(hrInterview.getTime(), timeFormatter);
	                    hrInterview.setTime(formattedIntime);
	                }

	            } catch (Exception e) {
	                // handle the exception appropriately
	            }

	            hrInterviewService.SaveHrInterview(hrInterview);

	            // Send email to the candidate
	            sendEmailToCandidate(hrInterview);

//	            long id = hrInterview.getCandidateId();
	            return ResponseEntity.status(HttpStatus.OK).body("HrInterview with CandidateId details saved successfully." + id);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            String errorMessage = "An error occurred while saving groupDiscussion details.";
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
	        }
	    }

		
	    private void sendEmailToCandidate(HrInterview hrInterview) {
	        try {
	            long candidateId = hrInterview.getCandidateId();
	            Candidate candidate = candidateService.findById(candidateId);
	            String email = candidate.getEmailId();
	            String name = candidate.getUserName();

	            MimeMessage message = emailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);
	            helper.setFrom(from);
	            helper.setTo(email);
	            helper.setSubject("HR Interview Scheduled");

	            // You can customize the email content here
	            String emailContent = "Dear " + name + ",\n\n"
	                    + "Your HR interview has been scheduled for " + hrInterview.getDate()
	                    + " at " + hrInterview.getTime() + ".\n\n"
	                    + "Please be prepared and arrive on time.\n\n"
	                    + "Regards,\nHR Team";
	            helper.setText(emailContent);

	            emailSender.send(message);
	        } catch (Exception e) {
	           e.printStackTrace();
	        }
	    }

	
	private String formatTime(String time, SimpleDateFormat formatter) throws Exception {
	    SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
	    java.util.Date date = inputFormat.parse(time);
	    return formatter.format(date);
	}
	
	@PutMapping("/hrInterview/{id}")
	public ResponseEntity<?> updateApplication(@PathVariable("id") long id, @RequestBody HrInterview application) {
		try {
			
			
			HrInterview existingApplication = hrInterviewService.getByCandidateId(id);
			if (existingApplication == null) {
				return ResponseEntity.notFound().build();
			}
			GroupDiscussion interviewSchedule =groupDiscussionService.getByCandidateId(id);

			if (interviewSchedule.getDate() != null && application.getDate() != null
					&& interviewSchedule.getDate().after(application.getDate())) {
				String errorMessage = "Discussion Date cannot be later than HR Interview Date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            java.util.Date currentDateMinusOneUtil = calendar.getTime();
            Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());

            if (application.getDate() != null && application.getDate().before(currentDateMinusOne)) {
                String errorMessage = "FromDate cannot be earlier than the current date.";
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }
			SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
	        try {
	            if (existingApplication.getTime() != null && !existingApplication.getTime().isEmpty()) {
	                String formattedIntime = formatTime(existingApplication.getTime(), timeFormatter);
	                existingApplication.setTime(formattedIntime);
	            }

	           
	        } catch (Exception e) {
	            
	        }
			existingApplication.setDate(application.getDate());
			existingApplication.setFeedback(application.getFeedback());
			existingApplication.setTime(application.getTime());
			existingApplication.setDepartmentId(application.getDepartmentId());
			existingApplication.setEmployeeId(application.getEmployeeId());
			existingApplication.setCandidateId(application.getCandidateId());					
			hrInterviewService.SaveHrInterview(existingApplication);
			 sendEmailToCandidate(existingApplication);
			return ResponseEntity.ok(existingApplication);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	private void sendEmailToCandidatecancelled(HrInterview hrInterview) {
        try {
            long candidateId = hrInterview.getCandidateId();
            Candidate candidate = candidateService.findById(candidateId);
            String email = candidate.getEmailId();
            String name = candidate.getUserName();

            // Create email content based on the interview type
            String interviewType = hrInterview.getApprovalType();
            String emailSubject = "";
            String emailContent = "";

            if ("cancelled".equals(interviewType)) {
                emailSubject = "HrInterview Cancelled";
                emailContent = "Dear " + name + ",\n\n"
                        + "We regret to inform you that your interview has been cancelled.\n\n"
                        + "Regards,\nHR Team";
            } else if ("completed".equals(interviewType)) {
                emailSubject = "HrInterview Completed";
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
	
	
	
	@PatchMapping("/hrInterview/{candidateid}")
	public ResponseEntity<?> updateDesignation(
	        @PathVariable("candidateid") Long candidateid,
	        @RequestBody HrInterview requestBody) {

	    try {
	    	HrInterview existingCandidate = hrInterviewService.getByCandidateId(candidateid);

	        if (existingCandidate == null) {
	            return ResponseEntity.notFound().build();
	        }
	        existingCandidate.setCancellationReason(requestBody.getCancellationReason());	
	        existingCandidate.setApprovalType(requestBody.getApprovalType());	      
	        if ("cancelled".equals(existingCandidate.getApprovalType())) {
	            existingCandidate.setCanceled(true);
	            existingCandidate.setCompleted(false);	         
	        } else if ("completed".equals(existingCandidate.getApprovalType())) {
	            existingCandidate.setCanceled(false);
	            existingCandidate.setCompleted(true);
	        
	        } else {
	            existingCandidate.setCanceled(false);
	            existingCandidate.setCompleted(false);
	            
	        }

	        hrInterviewService.SaveHrInterview(existingCandidate);
	        sendEmailToCandidatecancelled(existingCandidate);
	        return ResponseEntity.ok(existingCandidate);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	

//	@PutMapping("/hrInterview/{id}")
//	public ResponseEntity<HrInterview> updateHrInterview(@PathVariable("id") long id,
//			@RequestParam("candidateId") long candidateId, @RequestParam("interviewerName") String interviewerName,
//			@RequestParam("date") Date date, @RequestParam("time") Time time,
//			@RequestParam("interviewerContact") String interviewerContact, @RequestParam("feedback") String feedback,
//			@RequestParam("candidateQuestions") String candidateQuestions,
//			@RequestParam("groupDiscussionId") Long groupDiscussionId,
//			@RequestParam(value = "started", defaultValue = "false") boolean started,
//			@RequestParam(value = "inProgress", defaultValue = "false") boolean inProgress,
//			@RequestParam(value = "completed", defaultValue = "false") boolean completed,
//			@RequestParam("status") boolean status) {
//		try {
//			HrInterview existingHrInterview = hrInterviewService.findById(id);
//			if (existingHrInterview == null) {
//				return ResponseEntity.notFound().build();
//			}
//
//			existingHrInterview.setCandidateId(candidateId);
//			existingHrInterview.setInterviewerName(interviewerName);
//			existingHrInterview.setInterviewerContact(interviewerContact);
//			existingHrInterview.setDate(date);
////			existingHrInterview.setTime(time);
//			existingHrInterview.setCandidateQuestions(candidateQuestions);
//			existingHrInterview.setFeedback(feedback);
//			existingHrInterview.setGroupDiscussionId(groupDiscussionId);
//			existingHrInterview.setStarted(started);
//			existingHrInterview.setInProgress(inProgress);
//			existingHrInterview.setCompleted(completed);
//			existingHrInterview.setStatus(status);
//
//			if (started) {
//				existingHrInterview.setInProgress(false);
//				existingHrInterview.setCompleted(false);
//			} else if (inProgress) {
//				existingHrInterview.setStarted(false);
//				existingHrInterview.setCompleted(false);
//			} else if (completed) {
//				existingHrInterview.setStarted(false);
//				existingHrInterview.setInProgress(false);
//			}
//
//			hrInterviewService.SaveHrInterview(existingHrInterview);
//			return ResponseEntity.ok(existingHrInterview);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//	}

	@DeleteMapping("/hrInterview/delete/{id}")
	public ResponseEntity<String> deleteHrInterview(@PathVariable("id") Long id) {
		hrInterviewService.deleteHrInterviewId(id);
		return ResponseEntity.ok("HrInterview details deleted successfully");
	}

	@GetMapping("/hrInterviewDetails")
	public ResponseEntity<Object> getHrInterviewDetails(@RequestParam(required = true) String hrInterview) {
	    if ("hrInterviewDetails".equals(hrInterview)) {
	        List<Map<String, Object>> hrInterviews = hrInterviewRepository.findHrInterviewDetails();
	        return ResponseEntity.ok(hrInterviews);
	    } else {
	        String errorMessage = "Invalid value for 'hrInterview'. Expected 'hrInterview'.";
	        return ResponseEntity.badRequest().body(errorMessage);
	    }
	
	}
}
