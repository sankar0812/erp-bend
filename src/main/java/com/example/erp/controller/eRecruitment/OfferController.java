package com.example.erp.controller.eRecruitment;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.example.erp.entity.employee.Bank;
import com.example.erp.entity.erecruitment.Candidate;
import com.example.erp.entity.erecruitment.GroupDiscussion;
import com.example.erp.entity.erecruitment.HrInterview;
import com.example.erp.entity.erecruitment.Offer;
import com.example.erp.entity.erecruitment.TaskAssigned;
import com.example.erp.repository.erecruitment.OfferRepository;
import com.example.erp.service.eRecruitment.CandidateService;
import com.example.erp.service.eRecruitment.GroupDiscussionService;
import com.example.erp.service.eRecruitment.HrInterviewService;
import com.example.erp.service.eRecruitment.OfferService;

@RestController
@CrossOrigin
public class OfferController {
	@Autowired
	private OfferService offerService;
	@Autowired
	private CandidateService candidateService;
	@Autowired
	private HrInterviewService hrInterviewService;
	@Autowired
	private OfferRepository offerRepository;

	@Autowired
	private JavaMailSender emailSender;

	@Value("${spring.mail.username}")
	private String from;

	@GetMapping("/offer")
	public ResponseEntity<?> getDetails(@RequestParam(required = true) String Offer) {
		try {
			if ("offer".equals(Offer)) {
				Iterable<Offer> departmentDetails = offerService.listAll();
				return new ResponseEntity<>(departmentDetails, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided offer is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving bank details: " + e.getMessage());
		}
	}

	@PostMapping("/offer/save")
	public ResponseEntity<?> saveDepartment(@RequestBody Offer offer) {
		try {
			
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);

			java.util.Date currentDateMinusOneUtil = calendar.getTime();
			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());
			
			
			 long id1 =	offer.getCandidateId();
				long depId=offer.getAppointmentId();
				
				Optional<Offer> schedule =offerRepository.findByCandidateIdAndAppointmentId(id1,depId);
				
				if (schedule.isPresent()) {
					return ResponseEntity.badRequest().body("same entry not allowed");
				
					
					
					
					
				}
				
					HrInterview interviewSchedule =hrInterviewService.getByCandidateId(id1);

				if (interviewSchedule.getDate() != null && offer.getDate() != null
						&& interviewSchedule.getDate().after(offer.getDate())) {
					String errorMessage = "HR Interview Date cannot be later than Offer Date.";
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				}

			if (offer.getDate() != null && offer.getDate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			
			
			if (offer.getJoiningDate() != null && offer.getJoiningDate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			
			if (offer.getJoiningDate() != null && offer.getExpiryDate() != null
					&& offer.getJoiningDate().after(offer.getExpiryDate())) {
				String errorMessage = "PostedDate cannot be later than ClosingDate.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			offer.setApprovalType("scheduled");
			offer.setStatus(true); 	
			offerService.SaveOfferLetter(offer);
			 sendEmailToCandidate(offer);
			long id = offer.getCandidateId();
			return ResponseEntity.status(HttpStatus.OK).body("offer details saved successfully."  + id);
		} catch (Exception e) {
			String errorMessage = "An error occurred while saving Department details.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	private void sendEmailToCandidate(Offer offer) {
		try {
			long candidateId = offer.getCandidateId();
			Candidate candidate = candidateService.findById(candidateId);
			String email = candidate.getEmailId();
			String name = candidate.getUserName();

			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(email);
			helper.setSubject("Offer Letter");

			String emailContent = "Dear " + name + ",\n\n" + "Congratulations! You have received an offer letter:\n\n"
					+ "Offer Date: " + offer.getDate() + "\n" + "Joining Date: " + offer.getJoiningDate() + "\n"
					+ "Expiry Date: " + offer.getExpiryDate() + "\n" + "Salary Package: " + offer.getSalaryPackage()
					+ "\n\n" + "Please review the offer details and let us know your decision.\n\n"
					+ "Regards,\nHR Team";
			helper.setText(emailContent);

			emailSender.send(message);
		} catch (Exception e) {
			// Handle email sending failure
			e.printStackTrace();
		}
	}

	@PutMapping("/offerLetter/{Id}")
	public ResponseEntity<?> updateDepartmentId(@PathVariable("Id") Long Id, @RequestBody Offer offer) {
		try {
			Offer existingoffer = offerService.getByCandidateId(Id);
			if (existingoffer == null) {
				return ResponseEntity.notFound().build();
			}
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);

			java.util.Date currentDateMinusOneUtil = calendar.getTime();
			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());
			
			
			 long id1 =	offer.getCandidateId();

				
		
				
					HrInterview interviewSchedule =hrInterviewService.getByCandidateId(id1);

				if (interviewSchedule.getDate() != null && offer.getDate() != null
						&& interviewSchedule.getDate().after(offer.getDate())) {
					String errorMessage = "HR Interview Date cannot be later than Offer Date.";
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				}

			if (offer.getDate() != null && offer.getDate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			
			
			if (offer.getJoiningDate() != null && offer.getJoiningDate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			
			if (offer.getJoiningDate() != null && offer.getExpiryDate() != null
					&& offer.getJoiningDate().after(offer.getExpiryDate())) {
				String errorMessage = "PostedDate cannot be later than ClosingDate.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			existingoffer.setJoiningDate(offer.getJoiningDate());
			existingoffer.setDate(offer.getDate());
			existingoffer.setExpiryDate(offer.getExpiryDate());
			existingoffer.setSalaryPackage(offer.getSalaryPackage());
			existingoffer.setAppointmentId(offer.getAppointmentId());
			existingoffer.setCandidateId(offer.getCandidateId());
			offerService.SaveOfferLetter(existingoffer);
			sendEmailToCandidate(existingoffer);
			return ResponseEntity.ok(existingoffer);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PatchMapping("/offerLetter/{candidateid}")
	public ResponseEntity<?> updateDesignation(@PathVariable("candidateid") Long candidateid,
			@RequestBody Offer requestBody) {

		try {
			Offer existingCandidate = offerService.getByCandidateId(candidateid);

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

			offerService.SaveOfferLetter(existingCandidate);

			return ResponseEntity.ok(existingCandidate);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/offer/delete/{id}")
	public ResponseEntity<String> deleteOffer(@PathVariable("id") Long id) {
		offerService.deleteOfferId(id);
		return ResponseEntity.ok("Offer details deleted successfully");
	}

	@GetMapping("/FindOfferDetails")
	public ResponseEntity<Object> getHrInterviewDetails(@RequestParam(required = true) String offer) {
		if ("FindOfferDetails".equals(offer)) {
			List<Map<String, Object>> hrInterviews = offerRepository.FindOfferDetails();
			return ResponseEntity.ok(hrInterviews);
		} else {
			String errorMessage = "Invalid value for 'offer'. Expected 'FindOfferDetails'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}

	}

}
