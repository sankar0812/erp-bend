package com.example.erp.controller.eRecruitment;

import java.sql.Date;
import java.sql.Time;
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
import com.example.erp.entity.erecruitment.Appointment;
import com.example.erp.entity.erecruitment.Candidate;
import com.example.erp.entity.erecruitment.GroupDiscussion;
import com.example.erp.entity.erecruitment.Offer;
import com.example.erp.repository.erecruitment.AppointmentRepository;
import com.example.erp.service.eRecruitment.AppointmentService;
import com.example.erp.service.eRecruitment.CandidateService;

@RestController
@CrossOrigin
public class AppointmentController {
	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private CandidateService candidateService;

	@Autowired
	private JavaMailSender emailSender;

	@Value("${spring.mail.username}")
	private String from;

	@GetMapping("/appointment")
	public ResponseEntity<Object> getAppointment(@RequestParam(required = true) String appointment) {
		if ("appointments".equals(appointment)) {
			return ResponseEntity.ok(appointmentService.listAll());
		} else {
			String errorMessage = "Invalid value for 'appointment'. Expected 'appointment'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@PostMapping("/appointment/save")
	public ResponseEntity<?> saveDepartment(@RequestBody Appointment appointment) {
		try {
			appointment.setApprovalType("completed");
			appointment.setStatus(true);

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);

			java.util.Date currentDateMinusOneUtil = calendar.getTime();
			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());

			if (appointment.getDate() != null && appointment.getDate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			long id1 = appointment.getCandidateId();
			Date date = appointment.getDate();
			String po = appointment.getPosition();

			Optional<Offer> schedule = appointmentRepository.findByCandidateIdAndDateAndPosition(id1, date, po);

			if (schedule.isPresent()) {
				return ResponseEntity.badRequest().body("same entry not allowed");

			}
			appointmentService.SaveAppointment(appointment);
			sendEmailToCandidate(appointment);
			long id = appointment.getCandidateId();
			return ResponseEntity.status(HttpStatus.OK).body("offer details saved successfully." + id);
		} catch (Exception e) {
			String errorMessage = "An error occurred while saving Department details.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	private void sendEmailToCandidate(Appointment appointment) {
		try {
			long candidateId = appointment.getCandidateId();
			Candidate candidate = candidateService.findById(candidateId);
			String email = candidate.getEmailId();
			String name = candidate.getUserName();

			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(from);
			helper.setTo(email);
			helper.setSubject("Appointment Scheduled");

			String emailContent = "Dear " + name + ",\n\n"
					+ "We are pleased to inform you that your appointment has been scheduled:\n\n" + "Date: "
					+ appointment.getDate() + "\n" + "Time: " + appointment.getTime() + "\n\n"
					+ "Please ensure you arrive on time and bring any necessary documents.\n\n" + "Regards,\nHR Team";
			helper.setText(emailContent);

			emailSender.send(message);
		} catch (Exception e) {
			// Handle email sending failure
			e.printStackTrace(); // Print the stack trace for debugging purposes
		}
	}

	@PutMapping("/appointment/{Id}")
	public ResponseEntity<?> updateDepartmentId(@PathVariable("Id") Long Id, @RequestBody Appointment appointment) {
		try {
			Appointment existingDepartment = appointmentService.getByCandidateId(Id);
			if (existingDepartment == null) {
				return ResponseEntity.notFound().build();
			}
			existingDepartment.setApprovalType(appointment.getApprovalType());
			existingDepartment.setDate(appointment.getDate());
			existingDepartment.setCandidateId(appointment.getCandidateId());
			appointmentService.SaveAppointment(existingDepartment);
			sendEmailToCandidate(existingDepartment);
			return ResponseEntity.ok(existingDepartment);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PatchMapping("/appointment/{candidateid}")
	public ResponseEntity<?> updateDesignation(@PathVariable("candidateid") Long candidateid,
			@RequestBody Offer requestBody) {

		try {
			Appointment existingCandidate = appointmentService.getByCandidateId(candidateid);

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

			appointmentService.SaveAppointment(existingCandidate);

			return ResponseEntity.ok(existingCandidate);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/appointment/delete/{id}")
	public ResponseEntity<String> deleteAppointment(@PathVariable("id") Long id) {
		appointmentService.deleteAppointmentId(id);
		return ResponseEntity.ok("Appointment details deleted successfully");
	}

	@GetMapping("/findAppointmentDetails")
	public ResponseEntity<Object> getHrInterviewDetails(@RequestParam(required = true) String appointment) {
		if ("findAppointmentDetails".equals(appointment)) {
			List<Map<String, Object>> hrInterviews = appointmentRepository.findAppointmentDetails();
			return ResponseEntity.ok(hrInterviews);
		} else {
			String errorMessage = "Invalid value for 'offer'. Expected 'FindOfferDetails'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}

	}

	@PostMapping("/appointment/manager")
	public List<Map<String, Object>> getAllVoucherBetweenDates(@RequestBody Map<String, Object> requestBody) {
		String year = requestBody.get("year").toString();
		return appointmentRepository.findAppointmentDetail(year);
	}
}
