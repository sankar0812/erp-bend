
package com.example.erp.controller.eRecruitment;

import java.io.IOException;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;
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
import com.example.erp.entity.employee.Department;
import com.example.erp.entity.employee.EmergencyContacts;
import com.example.erp.entity.employee.Employee;
import com.example.erp.entity.employee.FamilyInformations;
import com.example.erp.entity.employee.Personal;
import com.example.erp.entity.employee.Qualification;
import com.example.erp.entity.erecruitment.Candidate;
import com.example.erp.entity.erecruitment.Hiring;
import com.example.erp.entity.erecruitment.HrInterview;
import com.example.erp.entity.erecruitment.Training;
import com.example.erp.entity.message.MemberList;
import com.example.erp.repository.erecruitment.CandidateRepository;
import com.example.erp.service.eRecruitment.CandidateService;
import com.example.erp.service.eRecruitment.HiringService;
import com.example.erp.service.eRecruitment.TrainingService;
import com.example.erp.service.employee.BankService;
import com.example.erp.service.employee.DepartmentService;
import com.example.erp.service.employee.EmergencyContactsService;
import com.example.erp.service.employee.EmployeeService;
import com.example.erp.service.employee.FamilyInformationsService;
import com.example.erp.service.employee.PersonalService;
import com.example.erp.service.employee.QualificationService;
import com.example.erp.service.message.MemberListService;

@RestController
@CrossOrigin
public class CandidateController {

	@Autowired
	private CandidateService candidateService;

	@Autowired
	private CandidateRepository candidateRepository;
	@Autowired
	private JavaMailSender emailSender;

	@Value("${spring.mail.username}")
	private String from;

	@Autowired
	private MemberListService listService;

	@Autowired
	private TrainingService trainingService;
	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private BankService bankService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private PersonalService personalService;

	@Autowired
	private EmergencyContactsService emergencyContactsService;

	@Autowired
	private QualificationService qualificationService;

	@Autowired
	private FamilyInformationsService familyInformationsService;
	
	@Autowired
	private HiringService service;

	@PostMapping("/candidate/save")
	public ResponseEntity<String> saveCandidate(@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "emailId", required = false) String emailId,
			@RequestParam(value = "mobileNumber", required = false) String mobileNumber,
			@RequestParam(value = "gender", required = false) String gender,
			@RequestParam(value = "dateofBirth", required = false) Date dateofBirth,
			@RequestParam(value = "branch", required = false) String branch,
			@RequestParam(value = "college") String college,
			@RequestParam(value = "skillDetails", required = false) String skillDetails,
			@RequestParam(value = "yearOfPassing", required = false) String yearOfPassing,
			@RequestParam(value = "education", required = false) String education,
			@RequestParam(value = "departmentId", required = false) Long departmentId,
			@RequestParam(value = "hiringId", required = false) Long hiringId,
			@RequestParam(value = "salaryExpectations", required = false) String salaryExpectations,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "jobRole", required = false) String jobRole,
			@RequestParam(value = "country", required = false) String country,
			@RequestParam(value = "maritalStatus", required = false) String maritalStatus,
			@RequestParam(value = "date", required = false) Date date,
			@RequestParam(value = "cgpa", required = false) String cgpa,
			@RequestParam(value = "year", required = false) String year,
			@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam(value = "position", required = false) String position,
			@RequestParam(value = "workExperience", required = false) String workExperience,
			@RequestParam(value = "resume", required = false) MultipartFile resume,
			@RequestParam(value = "coverLetter", required = false) String coverLetter) throws SQLException {
		try {
			Candidate candidate = new Candidate();
			candidate.setUserName(userName);
			candidate.setEmailId(emailId);
			candidate.setStatus(true);
			candidate.setCancelled(true);
			candidate.setStatusLevel("pending");
			candidate.setYear(year);
			candidate.setFileName(fileName);
			candidate.setPosition(position);
			candidate.setMobileNumber(mobileNumber);
			candidate.setGender(gender);
			candidate.setDateofBirth(dateofBirth);
			candidate.setEducation(education);
			candidate.setBranch(branch);
			candidate.setCgpa(cgpa);
			candidate.setCollege(college);
			candidate.setSkillDetails(skillDetails);
			candidate.setSalaryExpectations(salaryExpectations);
			candidate.setAddress(address);
			candidate.setCity(city);
			candidate.setCountry(country);
			candidate.setDepartmentId(departmentId);
			candidate.setHiringId(hiringId);
			candidate.setMaritalStatus(maritalStatus);
			candidate.setDate(date);
			candidate.setYearOfPassing(yearOfPassing);
			candidate.setCoverLetter(coverLetter);
			candidate.setJobRole(jobRole);
			candidate.setWorkExperience(workExperience);
			candidate.setResume(convertToBlob(resume));

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			
			
//			String emId = candidate.getEmailId();			
//			Optional<Date> latestApplicationDateOptional = candidateRepository.findLatestApplicationDateByEmailId(emId);
//			if (latestApplicationDateOptional.isPresent()) {
//				Date latestApplicationDate = latestApplicationDateOptional.get();
//			    calendar.setTime(latestApplicationDate); // Set the calendar's time to the latestApplicationDate
//			    calendar.add(Calendar.MONTH, -3); // Subtract three months
//			    Date threeMonthsAgo = new java.sql.Date(calendar.getTimeInMillis()); // Use java.util.Date, not java.sql.Date
//			    if (latestApplicationDate.after(threeMonthsAgo)) {
//			        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//			                .body("Email address '" + emId + "' has already applied within the last three months.");
//			    }
//			}



			
			Hiring hiring = service.getById(hiringId);
			
			 if (String.valueOf(yearOfPassing).length() == 4) {		        
			    } else {
			    	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Year of passing must be a 4-digit number.");
			    }
			
			 if (resume != null && !resume.isEmpty()) {
					String fileName1 = StringUtils.cleanPath(resume.getOriginalFilename());
					if ( fileName1.toLowerCase().endsWith(".pdf")) {

					} else {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body("Invalid attachment format. Allowed formats are  PDF.");
					}
				}
			java.util.Date currentDateMinusOneUtil = calendar.getTime();
			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());
			
//			Date post = hiring.getPosted();
			if (hiring.getPosted() != null && candidate.getDate() != null
					&& hiring.getPosted().after(candidate.getDate())) {
				String errorMessage = "PostedDate cannot be later than apply date .";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			if (candidate.getDate() != null && candidate.getDate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			if (candidate.getWorkExperience().equals("fresher")) {
				candidate.setFresher(true);
			} else if (candidate.getWorkExperience().equals("experience")) {
				candidate.setExperience(true);
			} else {
				candidate.setExperience(false);
				candidate.setFresher(false);
			}
			candidateService.SaveCandidateDetails(candidate);
			long id = candidate.getCandidateId();
			return ResponseEntity.ok("Candidate Details Saved Successfully with id: " + id);
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.badRequest().body("Mobile number already in use");
		}  catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while saving the candidate: " + e.getMessage());
		}
	}

	private Blob convertToBlob(MultipartFile file) throws IOException, SQLException {
		if (file != null && !file.isEmpty()) {
			byte[] bytes = file.getBytes();
			return new javax.sql.rowset.serial.SerialBlob(bytes);
		} else {
			return null;
		}
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000); // Change the upper limit if needed
	}

	@GetMapping("/candidate/candidate")
	public ResponseEntity<?> getCandidateDetails(@RequestParam(required = true) String candidate) {
		try {
			if ("candidate".equals(candidate)) {
				List<Candidate> candidateList = candidateRepository.findByCancelledTrue();
				List<Candidate> candidateResponseList = new ArrayList<>();
				for (Candidate candidate1 : candidateList) {
					int randomNumber = generateRandomNumber();
					String resumeUrl = "candidateResume/" + randomNumber + "/" + candidate1.getCandidateId() + "/"+ candidate1.getFileName();
					Candidate candidateResponse = new Candidate();
					candidateResponse.setCandidateId(candidate1.getCandidateId());
					candidateResponse.setAddress(candidate1.getAddress());
					candidateResponse.setStatusLevel(candidate1.getStatusLevel());
					candidateResponse.setYear(candidate1.getYear());
					candidateResponse.setUserName(candidate1.getUserName());
					candidateResponse.setCity(candidate1.getCity());
					candidateResponse.setFileName(candidate1.getFileName());
					candidateResponse.setCountry(candidate1.getCountry());
					candidateResponse.setDate(candidate1.getDate());
					candidateResponse.setDateofBirth(candidate1.getDateofBirth());
					candidateResponse.setBranch(candidate1.getBranch());
					candidateResponse.setEducation(candidate1.getEducation());
					candidateResponse.setEmailId(candidate1.getEmailId());
					candidateResponse.setPosition(candidate1.getPosition());
					candidateResponse.setYear(candidate1.getYear());
					candidateResponse.setGender(candidate1.getGender());
					candidateResponse.setCgpa(candidate1.getCgpa());
					candidateResponse.setCollege(candidate1.getCollege());
					candidateResponse.setJobRole(candidate1.getJobRole());
					candidateResponse.setYearOfPassing(candidate1.getYearOfPassing());
					candidateResponse.setSkillDetails(candidate1.getSkillDetails());
					candidateResponse.setMaritalStatus(candidate1.getMaritalStatus());
					candidateResponse.setMobileNumber(candidate1.getMobileNumber());
					candidateResponse.setSalaryExpectations(candidate1.getSalaryExpectations());
					candidateResponse.setWorkExperience(candidate1.getWorkExperience());
					candidateResponse.setDepartmentId(candidate1.getDepartmentId());
					candidateResponse.setHiringId(candidate1.getHiringId());
					candidateResponse.setEmployeeStatus(candidate1.getEmployeeStatus());
					candidateResponse.setStatusLevel(candidate1.getStatusLevel());
					candidateResponse.setCoverLetter(candidate1.getCoverLetter());
					candidateResponse.setResumeUrl(resumeUrl);
					candidateResponseList.add(candidateResponse);
				}

				return ResponseEntity.ok(candidateResponseList);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid parameter value for hrInterview");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while retrieving candidate details: " + e.getMessage());
		}
	}

	@GetMapping("candidateResume/{randomNumber}/{id}/{fileName}")
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

		Candidate image = candidateService.findById(imageId);
		if (image == null) {
			return ResponseEntity.notFound().build();
		}

		byte[] imageBytes;
		try {
			imageBytes = image.getResume().getBytes(1, (int) image.getResume().length());
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

	@PutMapping("/candidate/{candidateId}")
	public ResponseEntity<String> updateCandidateFile(@PathVariable long candidateId,

			@RequestParam(value = "userName", required = false) String userName,
			@RequestParam(value = "emailId", required = false) String emailId,
			@RequestParam(value = "mobileNumber", required = false) String mobileNumber,
			@RequestParam(value = "gender", required = false) String gender,
			@RequestParam(value = "dateofBirth", required = false) Date dateofBirth,
			@RequestParam(value = "branch", required = false) String branch,
			@RequestParam(value = "college", required = false) String college,
			@RequestParam(value = "skillDetails", required = false) String skillDetails,
			@RequestParam(value = "yearOfPassing", required = false) String yearOfPassing,
			@RequestParam(value = "education", required = false) String education,
			@RequestParam(value = "salaryExpectations", required = false) String salaryExpectations,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "departmentId", required = false) Long departmentId,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "jobRole", required = false) String jobRole,
			@RequestParam(value = "country", required = false) String country,
			@RequestParam(value = "maritalStatus", required = false) String maritalStatus,
			@RequestParam(value = "date", required = false) Date date,
			@RequestParam(value = "hiringId", required = false) Long hiringId,
			@RequestParam(value = "cgpa", required = false) String cgpa,
			@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam(value = "position", required = false) String position,
			@RequestParam(value = "workExperience", required = false) String workExperience,
			@RequestParam(value = "resume", required = false) MultipartFile resume,
			@RequestParam(value = "coverLetter", required = false) String coverLetter,
			@RequestParam(value = "year", required = false) String year) {

		try {
			Candidate candidate = candidateService.findById(candidateId);
			if (candidate != null) {
				if (resume != null && !resume.isEmpty()) {
					Blob resumeBlob = convertToBlob(resume);
					candidate.setResume(resumeBlob);
				}

				candidate.setUserName(userName);
				candidate.setYear(year);
				candidate.setEmailId(emailId);
				candidate.setMobileNumber(mobileNumber);
				candidate.setGender(gender);
				candidate.setDateofBirth(dateofBirth);
				candidate.setBranch(branch);
				candidate.setCollege(college);
				candidate.setSkillDetails(skillDetails);
				candidate.setYearOfPassing(yearOfPassing);
				candidate.setEducation(education);
				candidate.setSalaryExpectations(salaryExpectations);
				candidate.setAddress(address);
				candidate.setCity(city);
				candidate.setHiringId(hiringId);
				candidate.setFileName(fileName);
				candidate.setPosition(position);
				candidate.setDepartmentId(departmentId);
				candidate.setCountry(country);
				candidate.setJobRole(jobRole);
				candidate.setWorkExperience(workExperience);
				candidate.setMaritalStatus(maritalStatus);
				candidate.setCgpa(cgpa);
				candidate.setYear(year);
				candidate.setDate(date);
				candidate.setCoverLetter(coverLetter);
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DAY_OF_MONTH, -1);
				
//				String emId = candidate.getEmailId();
//				Optional<Date> latestApplicationDateOptional = candidateRepository.findLatestApplicationDateByEmailId(emId);
//				if (latestApplicationDateOptional.isPresent()) {
//				    Date latestApplicationDate = latestApplicationDateOptional.get();			   
//				    calendar.add(Calendar.MONTH, -3);
//				    java.util.Date threeMonthsAgo = calendar.getTime(); // Use java.util.Date for consistency
//
//				    if (latestApplicationDate.after(threeMonthsAgo)) {
//				        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//				                .body("Email address '" + emId + "' has already applied within the last three months.");
//				    }
//				}


				
				Hiring hiring = service.getById(hiringId);
				
				 if (String.valueOf(yearOfPassing).length() == 4) {		        
				    } else {
				    	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body("Year of passing must be a 4-digit number.");
				    }
				
				 if (resume != null && !resume.isEmpty()) {
						String fileName1 = StringUtils.cleanPath(resume.getOriginalFilename());
						if ( fileName1.toLowerCase().endsWith(".pdf")) {

						} else {
							return ResponseEntity.status(HttpStatus.BAD_REQUEST)
									.body("Invalid attachment format. Allowed formats are  PDF.");
						}
					}
				java.util.Date currentDateMinusOneUtil = calendar.getTime();
				Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());
				
//				Date post = hiring.getPosted();
				if (hiring.getPosted() != null && candidate.getDate() != null
						&& hiring.getPosted().after(candidate.getDate())) {
					String errorMessage = "PostedDate cannot be later than apply date .";
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				}

				if (candidate.getDate() != null && candidate.getDate().before(currentDateMinusOne)) {
					String errorMessage = "FromDate cannot be earlier than the current date.";
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
				}

				if (candidate.getWorkExperience().equals("fresher")) {
					candidate.setFresher(true);
				} else if (candidate.getWorkExperience().equals("experience")) {
					candidate.setExperience(true);
				} else {
					candidate.setExperience(false);
					candidate.setFresher(false);
				}
				candidateService.SaveCandidateDetails(candidate);

				return ResponseEntity.ok("Candidate updated successfully.");
			}
			return ResponseEntity.notFound().build();
		} catch (SQLException | IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PatchMapping("/candidate/cancelled/{candidateid}")
	public ResponseEntity<?> updateDesignation(@PathVariable("candidateid") Long candidateid,
			@RequestBody HrInterview requestBody) {

		try {
			Candidate existingCandidate = candidateService.findById(candidateid);

			if (existingCandidate == null) {
				return ResponseEntity.notFound().build();
			}
			existingCandidate.setCancellationReason(requestBody.getCancellationReason());
			existingCandidate.setApprovalType(requestBody.getApprovalType());
			if ("cancelled".equals(existingCandidate.getApprovalType())) {
				existingCandidate.setCancelled(false);
			
			}

			else {
				existingCandidate.setCancelled(false);
	

			}

			candidateService.SaveCandidateDetails(existingCandidate);
			sendEmailToCandidatecancelled(existingCandidate);
			return ResponseEntity.ok(existingCandidate);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private void sendEmailToCandidatecancelled(Candidate candidate1) {
		try {
			long candidateId = candidate1.getCandidateId();
			Candidate candidate = candidateService.findById(candidateId);
			String email = candidate.getEmailId();
			String name = candidate.getUserName();
			String subject =candidate.getCancellationReason();
			// Create email content based on the interview type
			String interviewType = candidate1.getApprovalType();
			String emailSubject = "";
			String emailContent = "";

			if ("cancelled".equals(interviewType)) {
				emailSubject = "interview not seleceted";
				emailContent = "Dear " + name + ",\n\n"
						+ "We regret to inform you that your"+subject+" interview has been cancelled.\n\n" + "Regards,\nHR Team";
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

	@PatchMapping("/interview/edit/{candidateid}")
	public ResponseEntity<?> updateDesignation(@PathVariable("candidateid") Long candidateid,
			@RequestBody Candidate requestBody) {

		try {
			Candidate existingCandidate = candidateService.findById(candidateid);

			if (existingCandidate == null) {
				return ResponseEntity.notFound().build();
			}

			existingCandidate.setStatusLevel(requestBody.getStatusLevel());

			candidateService.SaveCandidateDetails(existingCandidate);

			return ResponseEntity.ok(existingCandidate);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/candidateDetail/delete/{id}")
	public ResponseEntity<String> deleteappointment(@PathVariable("id") Long id) {
		candidateService.deleteCandidateId(id);
		return ResponseEntity.ok("Candidate details deleted successfully");
	}

	//// candidate information by id
	@GetMapping("/view")
	public ResponseEntity<?> getCandidateDetails(@RequestParam(name = "candidateId") Long candidateId) {
		try {
			Candidate candidate = candidateService.findById(candidateId);

			if (candidate == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Candidate not found for ID: " + candidateId);
			}

			Candidate candidateResponse = new Candidate();

			candidateResponse.setCandidateId(candidate.getCandidateId());
			candidateResponse.setAddress(candidate.getAddress());
			candidateResponse.setCity(candidate.getCity());
			candidateResponse.setCountry(candidate.getCountry());
			candidateResponse.setDate(candidate.getDate());
			candidateResponse.setDateofBirth(candidate.getDateofBirth());
			candidateResponse.setBranch(candidate.getBranch());
			candidateResponse.setEducation(candidate.getEducation());
			candidateResponse.setEmailId(candidate.getEmailId());
			candidateResponse.setGender(candidate.getGender());
			candidateResponse.setCgpa(candidate.getCgpa());
			candidateResponse.setCollege(candidate.getCollege());
			candidateResponse.setJobRole(candidate.getJobRole());
			candidateResponse.setYearOfPassing(candidate.getYearOfPassing());
			candidateResponse.setSkillDetails(candidate.getSkillDetails());
			candidateResponse.setUserName(candidate.getUserName());
			candidateResponse.setMaritalStatus(candidate.getMaritalStatus());
			candidateResponse.setMobileNumber(candidate.getMobileNumber());
			candidateResponse.setStatusLevel(candidate.getStatusLevel());
			candidateResponse.setSalaryExpectations(candidate.getSalaryExpectations());
			candidateResponse.setWorkExperience(candidate.getWorkExperience());
			candidateResponse.setResumeUrl("/candidateResume/" + candidateId);

			return ResponseEntity.ok(candidateResponse);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error occurred while retrieving candidate details");
		}
	}



//////number of candidates registered by currentDate, current month, year	
	@GetMapping("findCandidatesCount")
	public List<Map<String, Object>> findCandidatesCount() {
		return candidateRepository.countOfCandidatesDetails();
	}

	@GetMapping("findCandidatesCounts")
	public List<Map<String, Object>> findCandidatesCount1(@RequestParam(name = "date", required = false) String date,
			@RequestParam(name = "month", required = false) String month,
			@RequestParam(name = "year", required = false) String year) {
		return candidateRepository.countOfCandidatesDetails();
	}

	//// candidate details by current Date
	@GetMapping("/currentDateCandidatesDetails")
	public ResponseEntity<?> getCandidateDetail() {
		try {
			List<Map<String, Object>> candidates = candidateRepository.findCurrentDateCandidatesDetails();
			List<Map<String, Object>> candidateList = new ArrayList<>();

			for (Map<String, Object> candidate : candidates) {
				Map<String, Object> candidateMap = new HashMap<>();

				String resumeUrl = "/candidateResume/" + candidate.get("candidate_id");
				String coverLetterUrl = "/candidateCoverLetter/" + candidate.get("candidate_id");

				candidateMap.put("resumeUrl", resumeUrl);
				candidateMap.put("coverLetterUrl", coverLetterUrl);
				candidateMap.putAll(candidate);
				candidateList.add(candidateMap);
			}
			return ResponseEntity.ok(candidateList);
		} catch (Exception e) {
			String errorMessage = "Error occurred while retrieving candidate details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
		}
	}

	@GetMapping("/highestExperienceCandidateDetails")
	public ResponseEntity<?> getCandidateDetails11() {
		try {
			List<Map<String, Object>> candidates = candidateRepository.findHighestExperienceDetails();
			List<Map<String, Object>> candidateList = new ArrayList<>();

			for (Map<String, Object> candidate : candidates) {
				Map<String, Object> candidateMap = new HashMap<>();

				Object candidateId = candidate.get("candidateId");
				String resumeUrl = (candidateId != null) ? "/candidateResume/" + candidateId : null;
				String coverLetterUrl = (candidateId != null) ? "/candidateCoverLetter/" + candidateId : null;

				candidateMap.put("resumeUrl", resumeUrl);
				candidateMap.put("coverLetterUrl", coverLetterUrl);
				candidateMap.put("CandidateId", candidateId);
				candidateMap.put("FirstName", candidate.get("firstName"));
				candidateMap.put("LastName", candidate.get("lastName"));
				candidateMap.put("date", candidate.get("date"));
				candidateMap.put("jobRole", candidate.get("jobRole"));
				candidateMap.put("skillDetails", candidate.get("skillDetails"));
				candidateList.add(candidateMap);
			}
			return ResponseEntity.ok(candidateList);
		} catch (Exception e) {
			String errorMessage = "Error occurred while retrieving candidate details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
		}
	}

	@GetMapping("/application/vacancy/role")
	public ResponseEntity<Object> getInterviewSchedulingDetails(@RequestParam(required = true) String application) {
		if ("roleType".equals(application)) {
			List<Map<String, Object>> interviewProcesss = candidateRepository.getAllQuotationByClientDetails1();
			return ResponseEntity.ok(interviewProcesss);
		} else {
			String errorMessage = "Invalid value for 'roleType'. Expected 'application'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}

	}

	@PutMapping("/candidate/details/edit/{id}")
	public ResponseEntity<?> updateClientFormForProject(@PathVariable("id") long candidateId,
			@RequestBody Candidate candidate) {
		try {
			Candidate existingCandidate = candidateService.findById(candidateId);
			if (existingCandidate == null) {
				return ResponseEntity.notFound().build();
			}
//			if (existingCandidate.isFresher()) {
//				String errormessage = "A candidate is moved to Training";
//				return ResponseEntity.status(HttpStatus.CONFLICT).body(errormessage);
//			}
//
//			if (existingCandidate.isExperience()) {
//				String errormessage = "A candidate is moved to Employee";
//				return ResponseEntity.status(HttpStatus.CONFLICT).body(errormessage);
//			}

			existingCandidate.setEmployeeStatus(candidate.getEmployeeStatus());
			if (candidate.getEmployeeStatus().equals("fresher")) {
				existingCandidate.setFresher(true);
				existingCandidate.setStatusLevel("Training");
			} else if (candidate.getEmployeeStatus().equals("experience")) {
				existingCandidate.setExperience(true);
				existingCandidate.setStatusLevel("Employee");
			} else {
				existingCandidate.setFresher(false);
				existingCandidate.setExperience(false);
			}

			candidateService.SaveCandidateDetails(existingCandidate);

			if (existingCandidate.getEmployeeStatus().equals("fresher")) {

				String userName = existingCandidate.getUserName();
				String mobileNumber = existingCandidate.getMobileNumber();
				String gender = existingCandidate.getGender();
				Date dateOfBirth = existingCandidate.getDateofBirth();
				String address = existingCandidate.getAddress();
				String city = existingCandidate.getCity();
				String country = existingCandidate.getCountry();
				String martialStatus = existingCandidate.getMaritalStatus();
				long departmentId = existingCandidate.getDepartmentId();

				Training training = new Training();
				training.setUserName(userName);
				training.setAddress(address);
				training.setGender(gender);
				training.setMobileNumber(mobileNumber);
				training.setDepartmentId(departmentId);
				training.setStatus(true);
				training.setRoleId(8);
				training.setStarted(true);
				training.setTraineeStatus("started");
				training.setRoleName("Training");
				training.setMaritalStatus(martialStatus);
				training.setDateOfBirth(dateOfBirth);
				training.setCity(city);
				training.setCountry(country);
				training.setStartDate(LocalDate.now());

			
				Department requirement = departmentService.findById(departmentId);
		
		

				trainingService.SaveTrainee(training);
				String departmentName = requirement.getDepartmentName();
				String departmentCode = departmentName.substring(0, 2).toUpperCase();
				String roleName = training.getRoleName();
				String roleCode = roleName.substring(0, 2).toUpperCase();
				LocalDate date = training.getStartDate();
				int year = date.getYear();
				long demoId = training.getTraineeId();
				String userId1 = roleCode + year + departmentCode + demoId;
				training.setUserId(userId1);
				
				MemberList list = new MemberList();
//				list.setEmail(email);
				list.setPhoneNumber(mobileNumber);
				list.setUserName(userName);
				list.setRoleType("Training");
//				list.setProfile(blob);
				list.setId(demoId);
				list.setRoleId(8);
				listService.Save(list);

			}

			else if (existingCandidate.getEmployeeStatus().equals("experience")) {
				String userName = existingCandidate.getUserName();
				String mobileNumber = existingCandidate.getMobileNumber();
				String gender = existingCandidate.getGender();
				Date dateOfBirth = existingCandidate.getDateofBirth();
				String address = existingCandidate.getAddress();
				String city = existingCandidate.getCity();
				String country = existingCandidate.getCountry();
				String martialStatus = existingCandidate.getMaritalStatus();
				long departmentId = existingCandidate.getDepartmentId();

				Employee employee = new Employee();
				employee.setUserName(userName);
				employee.setGender(gender);
				employee.setCity(city);
				employee.setDepartmentId(departmentId);
				employee.setCountry(country);
				employee.setRoleId(4);
				employee.setCompanyId(1);
				employee.setRoleType("Employee");
				employee.setStatus(true);
				employee.setDob(dateOfBirth);
				employee.setDate(LocalDate.now());
				employee.setPhoneNumber(mobileNumber);
				employee.setAddress(address);
				employeeService.saveOrUpdate(employee);

				long id = employee.getEmployeeId();
				Department requirement = departmentService.findById(departmentId);
				String departmentName = requirement.getDepartmentName();
				String departmentCode = departmentName.substring(0, 2).toUpperCase();
				LocalDate date = employee.getDate();
				int year = date.getYear();
//				long roleId = employee.getRoleId();

				String rolename = employee.getRoleType();
				String roleCode = rolename.substring(0, 2).toUpperCase();
				String userId1 = roleCode + year + departmentCode + id;

				employee.setUserId(userId1);

				MemberList list = new MemberList();
				list.setId(id);
				list.setRoleId(4);
//				list.setEmail(email);
				list.setPhoneNumber(mobileNumber);
				list.setUserName(userName);
				list.setRoleType("Employee");
//				list.setProfile(blob);
				listService.Save(list);

				Bank bank = new Bank();
				bank.setEmployeeId(id);
				bank.setRoleId(4);
				bankService.save(bank);

				Personal personal = new Personal();
				personal.setMarried(martialStatus);
				personal.setEmployeeId(id);
				personal.setRoleId(4);
				personalService.SaveorUpdate(personal);

				EmergencyContacts contact = new EmergencyContacts();
				contact.setEmployeeId(id);
				contact.setRoleId(4);
				emergencyContactsService.saveOrUpdate(contact);

				FamilyInformations family = new FamilyInformations();
				family.setEmployeeId(id);
				family.setRoleId(4);
				familyInformationsService.SaveorUpdate(family);

				Qualification qualification = new Qualification();
				qualification.setEmployeeId(id);
				qualification.setRoleId(4);
				qualificationService.update(qualification);
			}

			return ResponseEntity.ok(existingCandidate);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
