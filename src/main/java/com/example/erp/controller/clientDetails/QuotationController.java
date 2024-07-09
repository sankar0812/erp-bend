package com.example.erp.controller.clientDetails;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
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
import com.example.erp.entity.clientDetails.Quotation;
import com.example.erp.entity.clientDetails.QuotationList;
import com.example.erp.entity.project.ResearchDevelopment;
import com.example.erp.repository.clientDetails.QuotationRepository;
import com.example.erp.service.clientDetails.QuotationService;
import com.example.erp.service.project.ResearchDevelopmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class QuotationController {

	@Autowired
	private QuotationService quotationService;

	@Autowired
	private QuotationRepository quotationRepository;

	@Autowired
	private ResearchDevelopmentService researchDevelopmentService;

	@PostMapping("/quotation/save")
	public ResponseEntity<?> saveQuotation(@RequestBody Quotation quotation) {
		try {
			quotationService.SaveorUpdate(quotation);

			long quotationId = quotation.getQuotationId();
			return ResponseEntity.ok(quotationId);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving quotation: " + e.getMessage());
		}
	}

	@PutMapping("/quotation/edit/{id}")
	public ResponseEntity<Quotation> updateQuotation(@PathVariable("id") long id, @RequestBody Quotation quotation) {
		try {
			Quotation existingQuotation = quotationService.findById(id);
			if (existingQuotation == null) {
				return ResponseEntity.notFound().build();
			}
			existingQuotation.setCompanyId(quotation.getCompanyId());
			existingQuotation.setDate(quotation.getDate());
			existingQuotation.setProjectTypeId(quotation.getProjectTypeId());
			existingQuotation.setProjectStatus(quotation.getProjectStatus());
			existingQuotation.setReason(quotation.getReason());
			existingQuotation.setTotalAmount(quotation.getTotalAmount());
			existingQuotation.setUrl(quotation.getUrl());
			existingQuotation.setAccepted(quotation.isAccepted());
			existingQuotation.setRejected(quotation.isRejected());
			existingQuotation.setQuotationList(quotation.getQuotationList());

			quotationService.SaveorUpdate(existingQuotation);
			return ResponseEntity.ok(existingQuotation);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/quotation/view1")
	public ResponseEntity<?> getAllQuotationByClientDetails(@RequestParam(required = true) String quotation) {
		try {
			if ("details".equals(quotation)) {
				ObjectMapper object = new ObjectMapper();
				List<Quotation> quotationList = quotationService.listAll();
				List<Map<String, Object>> quotationDataList = object.convertValue(quotationList, List.class);
				List<Map<String, Object>> quotationDetails = quotationRepository.getAllQuotationByClientDetails();

				Map<String, Map<String, Object>> quotationMap = new HashMap<>();
				for (Map<String, Object> map : quotationDetails) {
					quotationMap.put(map.get("quotation_id").toString(), map);
				}

				for (Map<String, Object> map : quotationDataList) {
					Map<String, Object> innerMap = quotationMap.get(map.get("quotationId").toString());
					map.put("clientName", innerMap.get("client_name"));
					map.put("city", innerMap.get("city"));
					map.put("address", innerMap.get("address"));
					map.put("country", innerMap.get("country"));
					map.put("email", innerMap.get("email"));
					map.put("gender", innerMap.get("gender"));
					map.put("phoneNumber", innerMap.get("phone_number"));
					map.put("state", innerMap.get("state"));
					map.put("zipCode", innerMap.get("zip_code"));
					map.put("mobileNumber", innerMap.get("mobile_number"));
					map.put("companyEmail", innerMap.get("companyEmail"));
					map.put("companyName", innerMap.get("company_name"));
					map.put("companyPhoneNumber", innerMap.get("phone_number1"));
					map.put("companyMobileNumber", innerMap.get("phone_number2"));
					map.put("companyCountry", innerMap.get("companyCountry"));
					map.put("companyAddress", innerMap.get("companyAddress"));
					map.put("projectType", innerMap.get("project_type"));
					int randomNumber = generateRandomNumber();
					String imageUrl = "quotation/" + randomNumber + "/" + map.get("quotationId").toString();
					map.put("url", imageUrl);

				}

				return ResponseEntity.ok(quotationDataList);
			}
			String errorMessage = "Invalid value for 'quotation'. Expected 'details'.";
			return ResponseEntity.badRequest().body(errorMessage);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}

	@GetMapping("/quotation/view")
	public ResponseEntity<Object> getAllRoleByEmployee(@RequestParam(required = true) String view) {
		if ("quotation".equals(view)) {
			List<Map<String, Object>> assestList = new ArrayList<>();

			List<Map<String, Object>> assestRole = quotationRepository.getAllQuotationByClientDetails();

			Map<String, List<Map<String, Object>>> assestGroupMap = assestRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("quotation_id").toString()));

			for (Entry<String, List<Map<String, Object>>> assestLoop : assestGroupMap.entrySet()) {
				Map<String, Object> innerMap = assestLoop.getValue().get(0);

				Map<String, Object> assestMap = new HashMap<>();
				List<Map<String, Object>> demoList = new ArrayList<>();
				Set<Object> uniqueBriefIds = new HashSet<>(); // Set to track unique brief_ids

				assestMap.put("country", innerMap.get("country"));
				assestMap.put("address", innerMap.get("address"));
				assestMap.put("clientName", innerMap.get("client_name"));
				assestMap.put("clientId", innerMap.get("client_id"));
				assestMap.put("email", innerMap.get("email"));
				assestMap.put("date", innerMap.get("given_date"));
				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(innerMap);
				String imageUrl = "company/" + randomNumber + "/" + innerMap.get("company_id") + "." + fileExtension;
				assestMap.put("profile", imageUrl);
				assestMap.put("phoneNumber", innerMap.get("phone_number"));
				assestMap.put("mobileNumber", innerMap.get("mobile_number"));
				assestMap.put("gender", innerMap.get("gender"));
				assestMap.put("totalAmount", innerMap.get("total_amount"));
				assestMap.put("amount", innerMap.get("amount"));
				assestMap.put("projectName", innerMap.get("project_name"));
				assestMap.put("clientStatus", innerMap.get("client_status"));
				assestMap.put("quotationStatus", innerMap.get("quotation_status"));
				assestMap.put("projectStatus", innerMap.get("project_status"));
				assestMap.put("state", innerMap.get("state"));
				assestMap.put("zipCode", innerMap.get("zip_code"));
				assestMap.put("companyEmail", innerMap.get("companyEmail"));
				assestMap.put("quotationId", Long.parseLong(assestLoop.getKey()));
				assestMap.put("companyName", innerMap.get("company_name"));
				assestMap.put("phoneNumber1", innerMap.get("phone_number1"));
				assestMap.put("phoneNumber2", innerMap.get("phone_number2"));
				assestMap.put("pincode", innerMap.get("pincode"));
				assestMap.put("url", innerMap.get("url"));
				assestMap.put("companyCountry", innerMap.get("companyCountry"));
				assestMap.put("companyAddress", innerMap.get("companyAddress"));
				assestMap.put("projectType", innerMap.get("project_type"));
				for (Map<String, Object> assestSubLoop : assestLoop.getValue()) {
					Object briefId = assestSubLoop.get("quotation_list_id");
					if (uniqueBriefIds.contains(briefId)) {
						continue;
					}
					Map<String, Object> briefMap = new HashMap<>();
					briefMap.put("quotationListId", briefId);
					briefMap.put("termsAndCondition", assestSubLoop.get("terms_and_condition"));
					briefMap.put("additionalNotes", assestSubLoop.get("additional_notes"));
					briefMap.put("amount", assestSubLoop.get("amount"));
					briefMap.put("description", assestSubLoop.get("description"));
					briefMap.put("projectId", assestSubLoop.get("project_id"));
					briefMap.put("projectName", assestSubLoop.get("project_name"));
					briefMap.put("quantity", assestSubLoop.get("quantity"));
					briefMap.put("rate", assestSubLoop.get("rate"));

					demoList.add(briefMap);
					uniqueBriefIds.add(briefId);
				}
				assestMap.put("quotationList", demoList);

				assestList.add(assestMap);
			}

			return ResponseEntity.ok(assestList);
		} else {
			String errorMessage = "Invalid value for 'assest'. Expected 'Assest'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}
	
	
	
	@GetMapping("/quotation/admin/view")
	public ResponseEntity<Object> getAllQuotationByClientDetailsWithApproval(@RequestParam(required = true) String view) {
		if ("quotation".equals(view)) {
			List<Map<String, Object>> assestList = new ArrayList<>();

			List<Map<String, Object>> assestRole = quotationRepository.getAllQuotationByClientDetailsWithApproval();

			Map<String, List<Map<String, Object>>> assestGroupMap = assestRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("quotation_id").toString()));

			for (Entry<String, List<Map<String, Object>>> assestLoop : assestGroupMap.entrySet()) {
				Map<String, Object> innerMap = assestLoop.getValue().get(0);

				Map<String, Object> assestMap = new HashMap<>();
				List<Map<String, Object>> demoList = new ArrayList<>();
				Set<Object> uniqueBriefIds = new HashSet<>(); // Set to track unique brief_ids

				assestMap.put("country", innerMap.get("country"));
				assestMap.put("address", innerMap.get("address"));
				assestMap.put("clientName", innerMap.get("client_name"));
				assestMap.put("clientId", innerMap.get("client_id"));
				assestMap.put("email", innerMap.get("email"));
				assestMap.put("date", innerMap.get("given_date"));
				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(innerMap);
				String imageUrl = "company/" + randomNumber + "/" + innerMap.get("company_id") + "." + fileExtension;
				assestMap.put("profile", imageUrl);
				assestMap.put("phoneNumber", innerMap.get("phone_number"));
				assestMap.put("mobileNumber", innerMap.get("mobile_number"));
				assestMap.put("gender", innerMap.get("gender"));
				assestMap.put("totalAmount", innerMap.get("total_amount"));
				assestMap.put("clientStatus", innerMap.get("client_status"));
				assestMap.put("quotationStatus", innerMap.get("quotation_status"));
				assestMap.put("projectStatus", innerMap.get("project_status"));
				assestMap.put("state", innerMap.get("state"));	
				assestMap.put("companyEmail", innerMap.get("companyEmail"));
				assestMap.put("amount", innerMap.get("amount"));
				assestMap.put("zipCode", innerMap.get("zip_code"));
				assestMap.put("quotationId", Long.parseLong(assestLoop.getKey()));
				assestMap.put("companyName", innerMap.get("company_name"));
				assestMap.put("phoneNumber1", innerMap.get("phone_number1"));
				assestMap.put("phoneNumber2", innerMap.get("phone_number2"));
				assestMap.put("pincode", innerMap.get("pincode"));
				assestMap.put("url", innerMap.get("url"));
				assestMap.put("companyCountry", innerMap.get("companyCountry"));
				assestMap.put("companyAddress", innerMap.get("companyAddress"));
				assestMap.put("projectType", innerMap.get("project_type"));
				for (Map<String, Object> assestSubLoop : assestLoop.getValue()) {
					Object briefId = assestSubLoop.get("quotation_list_id");
					if (uniqueBriefIds.contains(briefId)) {
						continue;
					}
					Map<String, Object> briefMap = new HashMap<>();
					briefMap.put("quotationListId", briefId);
					briefMap.put("termsAndCondition", assestSubLoop.get("terms_and_condition"));
					briefMap.put("additionalNotes", assestSubLoop.get("additional_notes"));
					briefMap.put("amount", assestSubLoop.get("amount"));
					briefMap.put("description", assestSubLoop.get("description"));
					briefMap.put("projectId", assestSubLoop.get("project_id"));
					briefMap.put("projectName", assestSubLoop.get("project_name"));
					briefMap.put("quantity", assestSubLoop.get("quantity"));
					briefMap.put("rate", assestSubLoop.get("rate"));

					demoList.add(briefMap);
					uniqueBriefIds.add(briefId);
				}
				assestMap.put("quotationList", demoList);

				assestList.add(assestMap);
			}

			return ResponseEntity.ok(assestList);
		} else {
			String errorMessage = "Invalid value for 'assest'. Expected 'Assest'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}
	
	@GetMapping("/quotation/view/client")
	public ResponseEntity<Object> getAllByQuoteApproval(@RequestParam(required = true) String view) {
		if ("quotation".equals(view)) {
			List<Map<String, Object>> assestList = new ArrayList<>();

			List<Map<String, Object>> assestRole = quotationRepository.getAllByQuoteApproval();

			Map<String, List<Map<String, Object>>> assestGroupMap = assestRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("quotation_id").toString()));

			for (Entry<String, List<Map<String, Object>>> assestLoop : assestGroupMap.entrySet()) {
				Map<String, Object> innerMap = assestLoop.getValue().get(0);

				Map<String, Object> assestMap = new HashMap<>();
				List<Map<String, Object>> demoList = new ArrayList<>();
				Set<Object> uniqueBriefIds = new HashSet<>(); // Set to track unique brief_ids

				assestMap.put("country", innerMap.get("country"));
				assestMap.put("address", innerMap.get("address"));
				assestMap.put("clientName", innerMap.get("client_name"));
				assestMap.put("clientId", innerMap.get("client_id"));
				assestMap.put("email", innerMap.get("email"));
				assestMap.put("date", innerMap.get("given_date"));
				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(innerMap);
				String imageUrl = "company/" + randomNumber + "/" + innerMap.get("company_id") + "." + fileExtension;
				String gggg = "signature/" + randomNumber + "/" + innerMap.get("company_id") + "." + fileExtension;
				assestMap.put("signature", gggg);
				assestMap.put("profile", imageUrl);
				assestMap.put("phoneNumber", innerMap.get("phone_number"));
				assestMap.put("mobileNumber", innerMap.get("mobile_number"));
				assestMap.put("gender", innerMap.get("gender"));
				assestMap.put("totalAmount", innerMap.get("total_amount"));
				assestMap.put("clientStatus", innerMap.get("client_status"));
				assestMap.put("quotationStatus", innerMap.get("quotation_status"));
				assestMap.put("state", innerMap.get("state"));
				assestMap.put("zipCode", innerMap.get("zip_code"));
				assestMap.put("companyEmail", innerMap.get("companyEmail"));
				assestMap.put("quotation_id", Long.parseLong(assestLoop.getKey()));
				assestMap.put("projectStatus", innerMap.get("project_status"));
				assestMap.put("companyName", innerMap.get("company_name"));
				assestMap.put("phoneNumber1", innerMap.get("phone_number1"));
				assestMap.put("phoneNumber2", innerMap.get("phone_number2"));
				assestMap.put("pincode", innerMap.get("pincode"));
				assestMap.put("url", innerMap.get("url"));
				assestMap.put("companyCountry", innerMap.get("companyCountry"));
				assestMap.put("companyAddress", innerMap.get("companyAddress"));
				assestMap.put("projectType", innerMap.get("project_type"));
				for (Map<String, Object> assestSubLoop : assestLoop.getValue()) {
					Object briefId = assestSubLoop.get("quotation_list_id");
					if (uniqueBriefIds.contains(briefId)) {
						continue;
					}
					Map<String, Object> briefMap = new HashMap<>();
					briefMap.put("quotationListId", briefId);
					briefMap.put("termsAndCondition", assestSubLoop.get("terms_and_condition"));
					briefMap.put("additionalNotes", assestSubLoop.get("additional_notes"));
					briefMap.put("amount", assestSubLoop.get("amount"));
					briefMap.put("description", assestSubLoop.get("description"));
					briefMap.put("projectId", assestSubLoop.get("project_id"));
					briefMap.put("projectName", assestSubLoop.get("project_name"));
					briefMap.put("quantity", assestSubLoop.get("quantity"));
					briefMap.put("rate", assestSubLoop.get("rate"));

					demoList.add(briefMap);

					// Add the briefId to the set to mark it as processed
					uniqueBriefIds.add(briefId);
				}
				assestMap.put("quotationList", demoList);

				assestList.add(assestMap);
			}

			return ResponseEntity.ok(assestList);
		} else {
			String errorMessage = "Invalid value for 'assest'. Expected 'Assest'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@GetMapping("/quotation/view/{id}")
	public ResponseEntity<?> getAllQuotationByClientDetailsById(@PathVariable("id") Long clientId) {
		List<Map<String, Object>> assestList = new ArrayList<>();

		List<Map<String, Object>> assestRole = quotationRepository.getAllByClientDetails(clientId);

		// Grouping by hiring_id
		Map<String, List<Map<String, Object>>> assestGroupMap = assestRole.stream()
				.collect(Collectors.groupingBy(action -> action.get("quotation_id").toString()));

		for (Entry<String, List<Map<String, Object>>> assestLoop : assestGroupMap.entrySet()) {
			Map<String, Object> innerMap = assestLoop.getValue().get(0);

			Map<String, Object> assestMap = new HashMap<>();
			List<Map<String, Object>> demoList = new ArrayList<>();
			Set<Object> uniqueBriefIds = new HashSet<>(); // Set to track unique brief_ids

			assestMap.put("country", innerMap.get("country"));
			assestMap.put("address", innerMap.get("address"));
			assestMap.put("clientName", innerMap.get("client_name"));
			assestMap.put("clientId", innerMap.get("client_id"));
			assestMap.put("email", innerMap.get("email"));
			assestMap.put("date", innerMap.get("given_date"));
			int randomNumber = generateRandomNumber();
			String fileExtension = getFileExtensionForImage(innerMap);
			String imageUrl = "company/" + randomNumber + "/" + innerMap.get("company_id") + "." + fileExtension;
			assestMap.put("profile", imageUrl);
			String gggg = "signature/" + randomNumber + "/" + innerMap.get("company_id") + "." + fileExtension;
			assestMap.put("signature", gggg);
			assestMap.put("phoneNumber", innerMap.get("phone_number"));
			assestMap.put("mobileNumber", innerMap.get("mobile_number"));
			assestMap.put("gender", innerMap.get("gender"));
			assestMap.put("totalAmount", innerMap.get("total_amount"));
			assestMap.put("clientStatus", innerMap.get("client_status"));
			assestMap.put("quotationStatus", innerMap.get("quotation_status"));
			assestMap.put("state", innerMap.get("state"));
			assestMap.put("zipCode", innerMap.get("zip_code"));
			assestMap.put("companyEmail", innerMap.get("companyEmail"));
			assestMap.put("quotation_id", Long.parseLong(assestLoop.getKey()));
			assestMap.put("companyName", innerMap.get("company_name"));
			assestMap.put("phoneNumber1", innerMap.get("phone_number1"));
			assestMap.put("phoneNumber2", innerMap.get("phone_number2"));
			assestMap.put("pincode", innerMap.get("pincode"));
			assestMap.put("url", innerMap.get("url"));
			assestMap.put("projectStatus", innerMap.get("project_status"));
			assestMap.put("companyCountry", innerMap.get("companyCountry"));
			assestMap.put("companyAddress", innerMap.get("companyAddress"));
			assestMap.put("projectType", innerMap.get("project_type"));
			for (Map<String, Object> assestSubLoop : assestLoop.getValue()) {
				Object briefId = assestSubLoop.get("quotation_list_id");
				if (uniqueBriefIds.contains(briefId)) {
					continue;
				}
				Map<String, Object> briefMap = new HashMap<>();
				briefMap.put("quotationListId", briefId);
				briefMap.put("termsAndCondition", assestSubLoop.get("terms_and_condition"));
				briefMap.put("additionalNotes", assestSubLoop.get("additional_notes"));
				briefMap.put("amount", assestSubLoop.get("amount"));
				briefMap.put("description", assestSubLoop.get("description"));
				briefMap.put("projectId", assestSubLoop.get("project_id"));
				briefMap.put("projectName", assestSubLoop.get("project_name"));
				briefMap.put("quantity", assestSubLoop.get("quantity"));
				briefMap.put("rate", assestSubLoop.get("rate"));

				demoList.add(briefMap);

				// Add the briefId to the set to mark it as processed
				uniqueBriefIds.add(briefId);
			}
			assestMap.put("quotationList", demoList);

			assestList.add(assestMap);
		}

		return ResponseEntity.ok(assestList);
	}

	private String getFileExtensionForImage(Map<String, Object> object) {
		if (object == null || !object.containsKey("url") || object.get("url") == null) {
			return "jpg";
		}

		String url = object.get("url").toString();

		if (url.endsWith(".png")) {
			return "png";
		} else if (url.endsWith(".jpg")) {
			return "jpg";
		} else {
			return "jpg";
		}
	}

	@DeleteMapping("/quotation/delete/{id}")
	public ResponseEntity<String> deleteQuotationId(@PathVariable("id") Long quotationId) {
		quotationService.deleteById(quotationId);
		return ResponseEntity.ok("Quotation deleted successfully With Id :" + quotationId);

	}

	@PutMapping("/quotation/admin/status/edit/{id}")
	public ResponseEntity<?> updateClientApprovelStatus(@PathVariable("id") long id, @RequestBody Quotation quotation) {
		try {
			Quotation existingQuotation = quotationService.findById(id);
			if (existingQuotation == null) {
				return ResponseEntity.notFound().build();
			}

			existingQuotation.setQuotationStatus(quotation.getQuotationStatus());
			if (quotation.getQuotationStatus().equals("approved")) {
				existingQuotation.setApproved(true);
				existingQuotation.setCancelled(false);
			} else if (quotation.getQuotationStatus().equals("rejected")) {
				existingQuotation.setCancelled(true);
				existingQuotation.setApproved(false);
			} else {
				existingQuotation.setAccepted(false);
				existingQuotation.setRejected(false);
			}

			quotationService.save(existingQuotation);
			return ResponseEntity.ok(existingQuotation);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/quotation/Client/status/edit/{id}")
	public ResponseEntity<?> AdminlientApprovelStatus(@PathVariable("id") long id, @RequestBody Quotation quotation) {
		try {
			Quotation existingQuotation = quotationService.findById(id);
			if (existingQuotation == null) {
				return ResponseEntity.notFound().build();
			}

			existingQuotation.setClientStatus(quotation.getClientStatus());
			existingQuotation.setRoleName(quotation.getRoleName());
			existingQuotation.setRejectedReason(quotation.getRejectedReason());
			if (quotation.getClientStatus().equals("approved")) {
				existingQuotation.setApproval(true);
				existingQuotation.setCancell(false);
			} else if (quotation.getClientStatus().equals("rejected")) {
				existingQuotation.setCancell(true);
				existingQuotation.setApproval(false);
			} else {
				existingQuotation.setApproval(false);	
				existingQuotation.setCancell(false);
				existingQuotation.setAccepted(false);
				existingQuotation.setRejected(false);
			}

			quotationService.save(existingQuotation);
			return ResponseEntity.ok(existingQuotation);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/quotation/details/edit/{id}")
	public ResponseEntity<?> updateClientFormForProject(@PathVariable("id") long id, @RequestBody Quotation quotation) {
		try {
			Quotation existingQuotation = quotationService.findById(id);
			if (existingQuotation == null) {
				return ResponseEntity.notFound().build();
			}
			if (existingQuotation.isAccepted()) {
				String errorMessage = "A Quotation is Moved to Research Development";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}

			if (existingQuotation.isRejected()) {
				String errorMessage = "A Quotation is Rejected";
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}

			existingQuotation.setProjectStatus(quotation.getProjectStatus());
			if (quotation.getProjectStatus().equals("managerApproved")) {
				existingQuotation.setAccepted(true);
			} else if (quotation.getProjectStatus().equals("rejected")) {
				existingQuotation.setRejected(true);
			} else {
				existingQuotation.setAccepted(false);
				existingQuotation.setRejected(false);
			}

			quotationService.save(existingQuotation);

			if (existingQuotation.getProjectStatus().equals("managerApproved")) {
				List<QuotationList> quotationList = existingQuotation.getQuotationList();
				ResearchDevelopment researchDevelopment = new ResearchDevelopment();

				if (quotationList != null) {
				    for (QuotationList quotationLoop : quotationList) {
				        long projectId = quotationLoop.getProjectId();
				        researchDevelopment.setProjectId(projectId);
				    }} 

				Date date = existingQuotation.getDate();
				long clientId = existingQuotation.getClientId();

				researchDevelopment.setClientId(clientId);
				researchDevelopment.setTodayDate(LocalDate.now());
				researchDevelopment.setTypeOfProject("development");
				researchDevelopment.setProjectStatus("pending");
				researchDevelopment.setRoleId(7);

				researchDevelopmentService.SaveResearchDevelopmentDetails(researchDevelopment);

			}

			return ResponseEntity.ok(existingQuotation);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	 @PostMapping("/quotation/manager")
	 public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAnd(
	         @RequestBody Map<String, Object> requestBody) {

	     if (!requestBody.containsKey("choose")) {
	         return ResponseEntity.badRequest().build();
	     }

	     String choose = requestBody.get("choose").toString();

	     switch (choose) {
	         
	         case "client":
	             return handleGstScenario(requestBody);
	         case "manager":
	             return handleGstScenario1(requestBody);
	         case "project":
	             return handleGstScenario2(requestBody);

	         case "year":
	             return handleMonthScenario(requestBody);

	         default:
	             return ResponseEntity.badRequest().build();
	     }
	 }



	 private ResponseEntity<List<Map<String, Object>>> handleGstScenario(Map<String, Object> requestBody) {
	     if (requestBody.containsKey("data")) {
	         String gstData = requestBody.get("data").toString();

	         switch (gstData) {
	             case "approved":
	                 return handleWithTaxScenario();
	             case "pending":
	                 return handleWithoutTaxScenario1();
	             case "rejected":
	                 return handleWithoutTaxScenario();
	             default:
	                 return ResponseEntity.badRequest().build();
	         }
	     }
	     return ResponseEntity.badRequest().build();
	 }

	 private ResponseEntity<List<Map<String, Object>>> handleGstScenario1(Map<String, Object> requestBody) {
	     if (requestBody.containsKey("data")) {
	         String gstData = requestBody.get("data").toString();

	         switch (gstData) {
	             case "approved":
	                 return handleWithTaxScenario5();
	             case "pending":
	                 return handleWithoutTaxScenario6();
	             case "rejected":
	                 return handleWithoutTaxScenario7();
	             default:
	                 return ResponseEntity.badRequest().build();
	         }
	     }
	     return ResponseEntity.badRequest().build();
	 }
	 private ResponseEntity<List<Map<String, Object>>> handleGstScenario2(Map<String, Object> requestBody) {
	     if (requestBody.containsKey("data")) {
	         String gstData = requestBody.get("data").toString();

	         switch (gstData) {
	             case "approved":
	                 return handleWithTaxScenario8();
	             case "pending":
	                 return handleWithoutTaxScenario9();
	             case "rejected":
	                 return handleWithoutTaxScenario10();
	             default:
	                 return ResponseEntity.badRequest().build();
	         }
	     }
	     return ResponseEntity.badRequest().build();
	 }
	 private ResponseEntity<List<Map<String, Object>>> handleWithTaxScenario() {
	     List<Map<String, Object>> leaveData = quotationRepository.Allfilterpresentlist();
	     return processResponse(leaveData);
	 }

	 private ResponseEntity<List<Map<String, Object>>> handleWithoutTaxScenario1() {
	     List<Map<String, Object>> leaveData = quotationRepository.Allfilter3absentlist();
	     return processResponse(leaveData);
	 }
	 
	 private ResponseEntity<List<Map<String, Object>>> handleWithoutTaxScenario() {
	     List<Map<String, Object>> leaveData = quotationRepository.Allfilter3absentlist33();
	     return processResponse(leaveData);
	 }

	 private ResponseEntity<List<Map<String, Object>>> handleWithTaxScenario5() {
	     List<Map<String, Object>> leaveData = quotationRepository.Allfilterpresentlist5();
	     return processResponse(leaveData);
	 }

	 private ResponseEntity<List<Map<String, Object>>> handleWithoutTaxScenario6() {
	     List<Map<String, Object>> leaveData = quotationRepository.Allfilter3absentlist6();
	     return processResponse(leaveData);
	 }
	 
	 private ResponseEntity<List<Map<String, Object>>> handleWithoutTaxScenario7() {
	     List<Map<String, Object>> leaveData = quotationRepository.Allfilter3absentlist7();
	     return processResponse(leaveData);
	 }

	 private ResponseEntity<List<Map<String, Object>>> handleWithTaxScenario8() {
	     List<Map<String, Object>> leaveData = quotationRepository.Allfilterpresentlist8();
	     return processResponse(leaveData);
	 }

	 private ResponseEntity<List<Map<String, Object>>> handleWithoutTaxScenario9() {
	     List<Map<String, Object>> leaveData = quotationRepository.Allfilter3absentlist9();
	     return processResponse(leaveData);
	 }
	 
	 private ResponseEntity<List<Map<String, Object>>> handleWithoutTaxScenario10() {
	     List<Map<String, Object>> leaveData = quotationRepository.Allfilter3absentlist10();
	     return processResponse(leaveData);
	 }

	 private ResponseEntity<List<Map<String, Object>>> handleMonthScenario(Map<String, Object> requestBody) {
		 if (requestBody.containsKey("monthName") && requestBody.containsKey("year")) {
 
	         String monthName = requestBody.get("monthName").toString();
	         String year = requestBody.get("year").toString();
	         List<Map<String, Object>> leaveData1 = quotationRepository.getAllattendance(monthName,year);
	         return processResponse(leaveData1);
	     }
	     return ResponseEntity.badRequest().build();
	 }

	

	 private ResponseEntity<List<Map<String, Object>>> processResponse(List<Map<String, Object>> leaveData) {
	     List<Map<String, Object>> imageResponses = new ArrayList<>();
	     for (Map<String, Object> image : leaveData) {
	    	 int randomNumber = generateRandomNumber();
         
         	String imageUrl1 = "clientProfile/" + randomNumber + "/" + image.get("client_id");
	         Map<String, Object> imageResponse = new HashMap<>();
	         imageResponse.put("profile", imageUrl1);
	         imageResponse.putAll(image);
	         imageResponses.add(imageResponse);
	     }
	     return ResponseEntity.ok(imageResponses);
	 }

	
}
