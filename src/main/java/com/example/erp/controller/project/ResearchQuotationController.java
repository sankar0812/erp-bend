package com.example.erp.controller.project;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.sql.rowset.serial.SerialBlob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.example.erp.entity.clientDetails.Quotation;
import com.example.erp.entity.clientDetails.QuotationList;
import com.example.erp.entity.project.ResearchQuotation;
import com.example.erp.repository.project.ResearchQuotationRepository;
import com.example.erp.service.clientDetails.QuotationService;
import com.example.erp.service.project.ResearchQuotationService;

@RestController
@CrossOrigin
public class ResearchQuotationController {

	@Autowired
	private ResearchQuotationService researchQuotationService;

	@Autowired
	private ResearchQuotationRepository quotationRepository;

	@Autowired
	private QuotationService quotationService;

	@PostMapping("/researchQuotation/save")
	public ResponseEntity<String> saveResearchQuotation(@RequestParam("projectId") long projectId,
			@RequestParam("userId") long employeeId, @RequestParam("projectStatus") String projectStatus,
			@RequestParam("date") LocalDate date, @RequestParam("accepted") boolean accepted,
			@RequestParam("rejected") boolean rejected, @RequestParam("fileUpload") MultipartFile fileUpload)
			throws SQLException {
		try {
			byte[] bytes = fileUpload.getBytes();
			ResearchQuotation researchQuotation = new ResearchQuotation();
			researchQuotation.setProjectId(projectId);
			researchQuotation.setAccepted(accepted);
			researchQuotation.setUserId(employeeId);
			researchQuotation.setRejected(rejected);
			researchQuotation.setProjectStatus(projectStatus);

			researchQuotationService.SaveResearchQuotation(researchQuotation);

			return ResponseEntity.ok("researchQuotation saved with id: " + researchQuotation.getResearchQuotationId());
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving Complaints: " + e.getMessage());
		}
	}

	@GetMapping("/researchQuotation")
	public ResponseEntity<?> displayAllValues(@RequestParam(required = true) String researchQuotation) {
		try {
			if ("researchQuotationDetail".equals(researchQuotation)) {
				List<ResearchQuotation> researchQuotationList = researchQuotationService.listAll();
				List<ResearchQuotation> researchQuotationObjects = new ArrayList<>();

				for (ResearchQuotation research : researchQuotationList) {
					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(research);
					String imageUrl = "researchQuotation/" + randomNumber + "/" + research.getResearchQuotationId()
							+ "." + fileExtension;

					ResearchQuotation researchQuotationObject = new ResearchQuotation();
					researchQuotationObject.setResearchQuotationId(research.getResearchQuotationId());
					researchQuotationObject.setUserId(research.getUserId());
					researchQuotationObject.setUrl(imageUrl);
					researchQuotationObject.setProjectStatus(research.getProjectStatus());
					researchQuotationObject.setAccepted(research.isAccepted());
					researchQuotationObject.setProjectId(research.getProjectId());
					researchQuotationObject.setDate(research.getDate());
					researchQuotationObject.setRejected(research.isRejected());
					researchQuotationObjects.add(researchQuotationObject);
				}

				return ResponseEntity.ok().body(researchQuotationObjects);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid researchQuotation value");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}

	private String getFileExtensionForImage(ResearchQuotation image) {
		if (image == null || image.getUrl() == null || image.getUrl().isEmpty()) {
			return "jpg";
		}

		String url = image.getUrl();
		if (url.endsWith(".png")) {
			return "png";
		} else if (url.endsWith(".jpg")) {
			return "jpg";
		} else {
			return "jpg";
		}
	}

	@PutMapping("/researchQuotation/edit/{researchQuotationId}")
	public ResponseEntity<String> updateResearchQuotation(@PathVariable long researchQuotationId,
			@RequestParam(value = "fileUpload", required = false) MultipartFile file,
			@RequestParam(value = "accepted", required = false) Boolean accepted,
			@RequestParam(value = "rejected", required = false) Boolean rejected,
			@RequestParam(value = "projectId", required = false) Long projectId,
			@RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "projectStatus", required = false) String projectStatus) throws IOException {
		try {
			ResearchQuotation researchQuotation = researchQuotationService
					.findResearchQuotationById(researchQuotationId);

			if (researchQuotation == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ResearchQuotation not found.");
			}

			if (accepted != null) {
				researchQuotation.setAccepted(accepted);
			}

			if (rejected != null) {
				researchQuotation.setRejected(rejected);
			}

			if (projectId != null) {
				researchQuotation.setProjectId(projectId);
			}

			if (userId != null) {
				researchQuotation.setUserId(userId);
			}

			if (projectStatus != null) {
				researchQuotation.setProjectStatus(projectStatus);
			}
			if (file != null && !file.isEmpty()) {
				byte[] bytes = file.getBytes();
				Blob blob = new SerialBlob(bytes);
			}
			researchQuotationService.SaveResearchQuotation(researchQuotation);

			return ResponseEntity.ok("Complaints updated successfully.Complaints ID: ");
		} catch (IOException | SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating employee.");
		}
	}

	@DeleteMapping("/researchQuotation/delete/{id}")
	public ResponseEntity<String> deleteResearchQuotationId(@PathVariable("id") Long researchQuotationId) {
		researchQuotationService.deleteResearchQuotationById(researchQuotationId);
		return ResponseEntity.ok("ResearchQuotation deleted successfully With Id :" + researchQuotationId);

	}

	@GetMapping("/researchQuotation/view")
	public ResponseEntity<?> getTaskAssignedDetailsp(@RequestParam(required = true) String view) {
		try {
			if ("researchQuotation".equals(view)) {
				List<Map<String, Object>> tasks = quotationRepository.getAllProjectresearchQuotation();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();

					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(taskAssigned);
					String imageUrl = "task/" + randomNumber + "/" + taskAssigned.get("") + "." + fileExtension;
					String imageUrl1 = "profile/" + randomNumber + "/" + taskAssigned.get("employeeId") + "."
							+ fileExtension;
//					taskAssignedMap.put("fileUpload", imageUrl);
//					taskAssignedMap.put("profile", imageUrl1);
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

	@PutMapping("/research/quotation/edit/{id}")
	public ResponseEntity<?> updateClientFormForProject(@PathVariable("id") long researchQuotationId,
			@RequestBody ResearchQuotation researchQuotation) {
		try {
			ResearchQuotation existingResearchQuotation = researchQuotationService
					.findResearchQuotationById(researchQuotationId);
			if (existingResearchQuotation == null) {
				return ResponseEntity.notFound().build();
			}
			if (existingResearchQuotation.isAccepted()) {
				String errormessage = "a project is moved to research first step task";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errormessage);
			}

			if (existingResearchQuotation.isRejected()) {
				String errormessage = "a project of research is rejected";
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errormessage);
			}

			existingResearchQuotation.setProjectStatus(researchQuotation.getProjectStatus());
			if (researchQuotation.getProjectStatus().equals("approved")) {
				existingResearchQuotation.setAccepted(true);
			} else if (researchQuotation.getProjectStatus().equals("rejected")) {
				existingResearchQuotation.setRejected(true);
			} else {
				existingResearchQuotation.setAccepted(false);
				existingResearchQuotation.setRejected(false);
			}

			researchQuotationService.SaveResearchQuotation(existingResearchQuotation);

			if (existingResearchQuotation.getProjectStatus().equals("approved")) {
				long projectId = existingResearchQuotation.getProjectId();
				long clientId = existingResearchQuotation.getClientId();

				Quotation quotation = new Quotation();

				List<QuotationList> quotationList = new ArrayList<>();

				QuotationList taskLoop = new QuotationList();
				quotationList.add(taskLoop);

				for (QuotationList quotationListItem : quotationList) {
					quotationListItem.setProjectId(projectId);
				}
				quotation.setQuotationList(quotationList);
				quotation.setClientId(clientId);
				quotation.setCompanyId(1);
				quotation.setProjectStatus("pending");
				quotation.setQuotationStatus("pending");
				quotation.setClientStatus("pending");
				quotation.setGivenDate(LocalDate.now());

				quotationService.save(quotation);
			}

			return ResponseEntity.ok(existingResearchQuotation);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
		} else if (url.endsWith(".pdf")) {
			return "pdf";
		}else {
			return "pdf";
		}
	}

}
