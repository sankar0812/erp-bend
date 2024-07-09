package com.example.erp.controller.organization;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.util.StringUtils;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.erp.entity.accounting.Accessories;
import com.example.erp.entity.employee.Employee;
import com.example.erp.entity.organization.Announcements;
import com.example.erp.entity.organization.Company;
import com.example.erp.repository.organization.AnnouncementsRepository;
import com.example.erp.service.organization.AnnouncementsService;

@RestController
@CrossOrigin
public class AnnouncementsController {

	@Autowired
	private AnnouncementsService service;

	@Autowired
	private AnnouncementsRepository repo;

	@GetMapping("/announcement1")
	public ResponseEntity<?> displayAllImages(@RequestParam(required = true) String announcementType) {
		try {
			if ("announcement".equals(announcementType)) {
				List<Announcements> announcements = service.listAll();
				List<Announcements> announcementResponses = new ArrayList<>();

				for (Announcements announcement : announcements) {
					int randomNumber = generateRandomNumber();

					String imageUrl = "announcement/" + randomNumber + "/" + announcement.getAnnouncementId();

					Announcements announcementResponse = new Announcements();
					announcementResponse.setAnnouncementId(announcement.getAnnouncementId());
					announcementResponse.setUrl(imageUrl);
					announcementResponse.setFromDate(announcement.getFromDate());
					announcementResponse.setToDate(announcement.getToDate());
					announcementResponse.setTitle(announcement.getTitle());
					announcementResponse.setInformedBy(announcement.getInformedBy());
					announcementResponse.setPublish(announcement.getPublish());
					announcementResponse.setPublished(announcement.isPublished());
					announcementResponse.setStatus(announcement.isStatus());
					announcementResponses.add(announcementResponse);
				}

				return ResponseEntity.ok().body(announcementResponses);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("The provided announcementType is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/announcement/save")
	public ResponseEntity<String> addAnnouncementWithImage(
			@RequestParam(value = "attachment", required = false) MultipartFile file,
			@RequestParam("title") String title, @RequestParam("fromDate") Date fromDate,
			@RequestParam("toDate") Date toDate,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam("informedBy") String informedBy,
			@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam("publish") String publish,
			@RequestParam(value = "imageStatus", required = false) Boolean imageStatus) {
		try {
			byte[] bytes = (file != null) ? file.getBytes() : null;
			Blob blob = (bytes != null) ? new SerialBlob(bytes) : null;

			Announcements announcement = new Announcements();
			announcement.setAttachment(blob);
			announcement.setFilename(fileName);

			announcement.setTitle(title);
			announcement.setFromDate(fromDate);
			announcement.setToDate(toDate);
			announcement.setInformedBy(informedBy);
			announcement.setPublish(publish);
			announcement.setDescription(description);
			if (imageStatus != null) {
				announcement.setImageStatus(imageStatus.booleanValue());
			}

			if ("published".equals(publish)) {
				announcement.setPublished(true);
			} else if ("unpublished".equals(publish)) {
				announcement.setPublished(false);
			} else {
				announcement.setPublished(false);
			}

			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			java.util.Date currentDateMinusOneUtil = calendar.getTime();
			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());

			if (announcement.getFromDate() != null && announcement.getFromDate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			if (announcement.getFromDate() != null && announcement.getToDate() != null
					&& announcement.getFromDate().after(announcement.getToDate())) {
				String errorMessage = "FromDate cannot be later than ToDate.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}
			// Validate and save attachment
			if (file != null && !file.isEmpty()) {
				String fileName1 = StringUtils.cleanPath(file.getOriginalFilename());
				if (fileName1.toLowerCase().endsWith(".png") || fileName1.toLowerCase().endsWith(".jpg")
						|| fileName1.toLowerCase().endsWith(".pdf") || fileName1.toLowerCase().endsWith(".svg") || fileName1.toLowerCase().endsWith(".mp4")) {

				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Invalid attachment format. Allowed formats are PNG, JPG,svg,mp4, and PDF.");
				}
			}
			announcement.setStatus(true);
			service.SaveorUpdate(announcement);

			return ResponseEntity.status(HttpStatus.CREATED).body("Announcement details saved successfully.");
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding announcement.");
		}
	}

//	@PostMapping("/announcement/save")
//	public ResponseEntity<String> addAnnouncementWithImage(
//			@RequestParam(value = "attachment", required = false) MultipartFile file,
//			@RequestParam("title") String title, @RequestParam("fromDate") Date fromDate,
//			@RequestParam("toDate") Date toDate,
//			@RequestParam(value = "description", required = false) String description,
//			@RequestParam("informedBy") String informedBy, @RequestParam("publish") String publish,
//			@RequestParam(value = "imageStatus", required = false) Boolean imageStatus) {
//		try {
//
//			byte[] bytes = (file != null) ? file.getBytes() : null;
//			Blob blob = (bytes != null) ? new SerialBlob(bytes) : null;
//		
//			Announcements announcement = new Announcements();
//			announcement.setAttachment(blob);
//			announcement.setTitle(title);
//			announcement.setFromDate(fromDate);
//			announcement.setToDate(toDate);
//			announcement.setInformedBy(informedBy);
//			announcement.setPublish(publish);
//			announcement.setDescription(description);
//			if (imageStatus != null) {
//				announcement.setImageStatus(imageStatus.booleanValue());
//			}
//
//			if ("published".equals(publish)) {
//				announcement.setPublished(true);
//			} else if ("unpublished".equals(publish)) {
//				announcement.setPublished(false);
//			} else {
//				announcement.setPublished(false);
//			}
//
//			Calendar calendar = Calendar.getInstance();
//			calendar.add(Calendar.DAY_OF_MONTH, -1);
//			java.util.Date currentDateMinusOneUtil = calendar.getTime();
//			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());
//
//			if (announcement.getFromDate() != null && announcement.getFromDate().before(currentDateMinusOne)) {
//				String errorMessage = "FromDate cannot be earlier than the current date.";
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//			}
//			if (announcement.getFromDate() != null && announcement.getToDate() != null
//					&& announcement.getFromDate().after(announcement.getToDate())) {
//				String errorMessage = "FromDate cannot be later than ToDate.";
//				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
//			}
//
//			announcement.setStatus(true);
//			service.SaveorUpdate(announcement);
//
//			return ResponseEntity.status(HttpStatus.CREATED).body("Announcement details saved successfully.");
//		} catch (IOException | SQLException e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding announcement.");
//		}
//	}

	@RequestMapping("/announcement/{announcementId}")
	private Optional<Announcements> getAnnouncement(@PathVariable(name = "announcementId") long announcementId) {
		return service.getAnnouncementsById(announcementId);

	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000); // Change the upper limit if needed
	}

	@GetMapping("announcement/{randomNumber}/{id}/{fileName}")
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

		Announcements image = service.findById(imageId);
		if (image == null) {
			return ResponseEntity.notFound().build();
		}

		byte[] imageBytes;
		try {
			imageBytes = image.getAttachment().getBytes(1, (int) image.getAttachment().length());
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



	@PutMapping("/announcement/or/{id}")
	public ResponseEntity<Boolean> toggleComplaintsStatus(@PathVariable(name = "id") long id) {
		try {
			Announcements complaints = service.findById(id);
			if (complaints != null) {
				boolean currentStatus = complaints.isPublished();
				complaints.setPublished(!currentStatus);
				if (complaints.isPublished()) {
					complaints.setPublish("published");
				} else {
					complaints.setPublish("unpublished");
				}

				service.SaveorUpdate(complaints);
			} else {
				return ResponseEntity.ok(false);
			}
			return ResponseEntity.ok(complaints.isPublished());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}

	@PutMapping("/announcement1/or/{announcement_id}")
	public ResponseEntity<Boolean> toggleAnnouncementStatus(
			@PathVariable(name = "announcement_id") long announcement_id) {
		try {
			Announcements announcement = service.findById(announcement_id);
			if (announcement != null) {
				boolean currentStatus = announcement.isStatus();
				announcement.setPublished(!currentStatus);
				if (announcement.isStatus()) {
					announcement.setPublish("published");
				} else {
					announcement.setPublish("unpublished");
				}
				service.SaveorUpdate(announcement);
			} else {
				return ResponseEntity.ok(false);
			}
			return ResponseEntity.ok(announcement.isStatus());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}

	@PutMapping("/announcement/edit/{announcementId}")
	public ResponseEntity<?> updateAnnouncement(@PathVariable Long announcementId,
			@RequestParam(value = "attachment", required = false) MultipartFile file,
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "fromDate", required = false) Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "informedBy", required = false) String email,
			@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam(value = "imageStatus", required = false) Boolean imageStatus,
			@RequestParam(value = "publish", required = false) String publish) {
		try {
			Announcements existingAnnouncement = service.findById(announcementId);

			if (existingAnnouncement == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
			}
			if ("published".equals(publish)) {
				existingAnnouncement.setPublished(true);
				existingAnnouncement.setPublish("published");
			} else if ("unpublished".equals(publish)) {
				existingAnnouncement.setPublished(false);
				existingAnnouncement.setPublish("unpublished");
			} else {
				existingAnnouncement.setPublished(false);
			}
			if (imageStatus != null) {
				existingAnnouncement.setImageStatus(imageStatus.booleanValue());
			}
			existingAnnouncement.setFilename(fileName);
			existingAnnouncement.setTitle(title);
			existingAnnouncement.setFromDate(fromDate);
			existingAnnouncement.setToDate(toDate);
			existingAnnouncement.setDescription(description);
			existingAnnouncement.setInformedBy(email);
			if (file != null && !file.isEmpty()) {
				byte[] bytes = file.getBytes();
				Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
				existingAnnouncement.setAttachment(blob);
			}
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			java.util.Date currentDateMinusOneUtil = calendar.getTime();
			Date currentDateMinusOne = new Date(currentDateMinusOneUtil.getTime());
			// Validate and save attachment
			if (file != null && !file.isEmpty()) {
				String fileName1 = StringUtils.cleanPath(file.getOriginalFilename());
				if (fileName1.toLowerCase().endsWith(".png") || fileName1.toLowerCase().endsWith(".jpg")
						|| fileName1.toLowerCase().endsWith(".pdf") || fileName1.toLowerCase().endsWith(".svg")|| fileName1.toLowerCase().endsWith(".mp4")) {

				} else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Invalid attachment format. Allowed formats are PNG, JPG,svg,mp4,and PDF.");
				}
			}
			if (existingAnnouncement.getFromDate() != null
					&& existingAnnouncement.getFromDate().before(currentDateMinusOne)) {
				String errorMessage = "FromDate cannot be earlier than the current date.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			if (existingAnnouncement.getFromDate() != null && existingAnnouncement.getToDate() != null
					&& existingAnnouncement.getFromDate().after(existingAnnouncement.getToDate())) {
				String errorMessage = "FromDate cannot be later than ToDate.";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
			}

			service.save(existingAnnouncement);
			return ResponseEntity.ok(existingAnnouncement);
		} catch (IOException | SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating announcement.");
		}
	}

	@DeleteMapping("/announcement/announcementdelete/{announcementId}")
	public ResponseEntity<String> deleteTitle(@PathVariable("announcementId") Long announcementId) {
		service.deleteAnnouncementsIdById(announcementId);
		return ResponseEntity.ok("announcement deleted successfully");

	}


	@GetMapping("/announcement/dashboard")
	public ResponseEntity<?> getTaskAssignedDetails(@RequestParam(required = true) String dashboard) {
		try {
			if ("announcement".equals(dashboard)) {
				List<Map<String, Object>> tasks = repo.getAllByClientDetails();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();

					int randomNumber = generateRandomNumber();
					String imageUrl = "announcement/" + randomNumber + "/" + taskAssigned.get("announcementId") + "/"
							+ taskAssigned.get("fileName");

					if (taskAssigned.containsKey("from_date") && taskAssigned.get("from_date") != null) {
						java.sql.Date sqlDate = (java.sql.Date) taskAssigned.get("from_date");
						LocalDate holidayDate = sqlDate.toLocalDate();

						LocalDate currentDate = LocalDate.now();
						if (holidayDate.isBefore(currentDate)) {
							taskAssignedMap.put("status", false);
						} else {
							taskAssignedMap.put("status", true);
						}
					}

					if (taskAssigned.containsKey("imageStatus")
							&& (Boolean) taskAssigned.get("imageStatus").equals(true)) {
						taskAssignedMap.put("attachment", imageUrl);
					}

					taskAssignedMap.putAll(taskAssigned);
					taskList.add(taskAssignedMap);
				}
				return ResponseEntity.ok(taskList);
			} else {
				String errorMessage = "Invalid value for 'TaskAssigned'. Expected 'findTaskAssignedDetails'.";
				return ResponseEntity.badRequest().body(Collections.singletonMap("error", errorMessage));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e));
		}
	}

	@GetMapping("/announcement/dashboard/{announcement_id}")
	public ResponseEntity<?> getTaskAssignedDetailsWithId(@RequestParam(required = true) String dashboard,
			@PathVariable("announcement_id") Long announcement_id) {
		try {
			if ("announcement".equals(dashboard)) {
				List<Map<String, Object>> tasks = repo.getAllByClientDetailsById(announcement_id);
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();

					int randomNumber = generateRandomNumber();


					String imageUrl = "announcement/" + randomNumber + "/" + taskAssigned.get("announcementId") + "/"
							+ taskAssigned.get("fileName");

					if (taskAssigned.containsKey("fromDate") && taskAssigned.get("fromDate") != null) {
						java.sql.Date sqlDate = (java.sql.Date) taskAssigned.get("fromDate");
						LocalDate holidayDate = sqlDate.toLocalDate();

						LocalDate currentDate = LocalDate.now();
						if (holidayDate.isBefore(currentDate)) {
							taskAssignedMap.put("status", false);
						} else {
							taskAssignedMap.put("status", true);
						}
					}

					if (taskAssigned.containsKey("imageStatus")
							&& (Boolean) taskAssigned.get("imageStatus").equals(true)) {
						taskAssignedMap.put("attachment", imageUrl);
					}

					taskAssignedMap.putAll(taskAssigned);
					taskList.add(taskAssignedMap);
				}
				return ResponseEntity.ok(taskList);
			} else {
				String errorMessage = "Invalid value for 'TaskAssigned'. Expected 'findTaskAssignedDetails'.";
				return ResponseEntity.badRequest().body(Collections.singletonMap("error", errorMessage));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", e));
		}
	}

	@GetMapping("/announcement")
	public ResponseEntity<?> getTaskAssignedDetailsp(@RequestParam(required = true) String announcementType) {
		try {
			if ("announcement".equals(announcementType)) {
				List<Map<String, Object>> tasks = repo.AllHolidaysinTable();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();

					int randomNumber = generateRandomNumber();

					String imageUrl = "announcement/" + randomNumber + "/" + taskAssigned.get("announcementId") + "/"
							+ taskAssigned.get("fileName");
					LocalDate currentDate = LocalDate.now();
					java.sql.Date sqlDate = (java.sql.Date) taskAssigned.get("fromDate");
					LocalDate holidayDate = sqlDate.toLocalDate();

					if (holidayDate.isBefore(currentDate)) {
						taskAssignedMap.put("status", false);
					} else {
						taskAssignedMap.put("status", true);
					}
					if (taskAssigned.containsKey("imageStatus")
							&& (Boolean) taskAssigned.get("imageStatus").equals(true)) {
						taskAssignedMap.put("attachment", imageUrl);
					}

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

	@PostMapping("/announcement/report")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndtrainee(
			@RequestBody Map<String, Object> requestBody) {

		if (!requestBody.containsKey("choose")) {
			return ResponseEntity.badRequest().build();
		}

		String choose = requestBody.get("choose").toString();

		switch (choose) {

		case "currentday":
			return handleGstScenario1(requestBody);

		case "year":
			return handleMonthScenario1(requestBody);

		default:
			return ResponseEntity.badRequest().build();
		}
	}

	private ResponseEntity<List<Map<String, Object>>> handleGstScenario1(Map<String, Object> requestBody) {
		if (requestBody.containsKey("data")) {
			String gstData = requestBody.get("data").toString();

			switch (gstData) {
			case "unpublished":
				return handleWithTaxScenario1();
			case "published":
				return handleWithoutTaxScenario1();
			default:
				return ResponseEntity.badRequest().build();
			}
		}
		return ResponseEntity.badRequest().build();
	}

	private ResponseEntity<List<Map<String, Object>>> handleWithTaxScenario1() {
		List<Map<String, Object>> leaveData = repo.getAllpromotionssemployee();
		return processResponse1(leaveData);
	}

	private ResponseEntity<List<Map<String, Object>>> handleWithoutTaxScenario1() {
		List<Map<String, Object>> leaveData = repo.Allfilter3absentlisttrainee();
		return processResponse1(leaveData);
	}

	private ResponseEntity<List<Map<String, Object>>> handleMonthScenario1(Map<String, Object> requestBody) {
		if (requestBody.containsKey("year")) {

			String year = requestBody.get("year").toString();
			List<Map<String, Object>> leaveData1 = repo.getAllpromotions(year);
			return processResponse1(leaveData1);
		}
		return ResponseEntity.badRequest().build();
	}

	private ResponseEntity<List<Map<String, Object>>> processResponse1(List<Map<String, Object>> leaveData) {
		List<Map<String, Object>> taskList = new ArrayList<>();

		for (Map<String, Object> taskAssigned : leaveData) {
			Map<String, Object> taskAssignedMap = new HashMap<>();

			int randomNumber = generateRandomNumber();
//			String fileExtension = getFileExtensionForImage(taskAssigned);

			String imageUrl = "announcement/" + randomNumber + "/" + taskAssigned.get("announcementId") + "/"
					+ taskAssigned.get("fileName");

			if (taskAssigned.containsKey("fromDate") && taskAssigned.get("fromDate") != null) {
				java.sql.Date sqlDate = (java.sql.Date) taskAssigned.get("fromDate");
				LocalDate holidayDate = sqlDate.toLocalDate();

				LocalDate currentDate = LocalDate.now();
				if (holidayDate.isBefore(currentDate)) {
					taskAssignedMap.put("status", false);
				} else {
					taskAssignedMap.put("status", true);
				}
			}

			if (taskAssigned.containsKey("imageStatus") && (Boolean) taskAssigned.get("imageStatus").equals(true)) {
				taskAssignedMap.put("attachment", imageUrl);
			}

			taskAssignedMap.putAll(taskAssigned);
			taskList.add(taskAssignedMap);
		}

		return ResponseEntity.ok(taskList);
	}
}
