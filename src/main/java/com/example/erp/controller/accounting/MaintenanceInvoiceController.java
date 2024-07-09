package com.example.erp.controller.accounting;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.erp.entity.accounting.MaintenanceInvoice;
import com.example.erp.entity.accounting.MaintenanceList;
import com.example.erp.entity.accounting.MaintenancePayment;
import com.example.erp.entity.clientDetails.ClientInvoiceList;
import com.example.erp.entity.clientDetails.ClientRequirement;
import com.example.erp.entity.clientDetails.Receipts;
import com.example.erp.entity.message.MemberList;
import com.example.erp.repository.accounting.MaintenanceInvoiceRepository;
import com.example.erp.repository.accounting.MaintenancePaymentRepository;
import com.example.erp.service.accounting.MaintenanceInvoiceService;
import com.example.erp.service.accounting.MaintenancePaymentService;

@RestController
@CrossOrigin
public class MaintenanceInvoiceController {

	@Autowired
	private MaintenancePaymentService maintenancePaymentService;

	@Autowired
	private MaintenancePaymentRepository maintenancePaymentRepository;
	@Autowired
	private MaintenanceInvoiceRepository repo;
	@Autowired
	private MaintenanceInvoiceService service;

	@GetMapping("/maintenance")
	public ResponseEntity<?> getHirings(@RequestParam(required = true) String maintenance) {
		try {
			if ("maintenance".equals(maintenance)) {
				List<MaintenanceInvoice> Hirings = service.invoiceList();
				return ResponseEntity.ok(Hirings);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided leave is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving Hirings: " + e.getMessage());
		}
	}

	@PostMapping("/maintenance/save")
	public ResponseEntity<?> saveHiring(@RequestBody MaintenanceInvoice maintenance) {
		try {
			maintenance.setCompanyId(1);
			maintenance.setStatus(true);
			double total = maintenance.getTotal();
			service.saveInvoice(maintenance);
			long InvoiceId = maintenance.getMaintenanceInvoiceId();
			Date date = maintenance.getInvoiceDate();
			List<MaintenanceList> maintenanceList = maintenance.getMaintenanceList();
			long projectId = 0;
			for (MaintenanceList maintenanceLoop : maintenanceList) {
				projectId = maintenanceLoop.getProjectId();
			}
			MaintenancePayment payment = new MaintenancePayment();
			payment.setPaymentDate(date);
			payment.setProjectId(projectId);
			payment.setBalance(total);
			payment.setAmount(total);
			payment.setMaintenanceInvoiceId(InvoiceId);
			maintenancePaymentService.save(payment);

			return ResponseEntity.ok(maintenance.getMaintenanceInvoiceId());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error MaintenanceInvoice: " + e.getMessage());
		}

	}

	@PutMapping("/maintenance/status/{id}")
	public ResponseEntity<?> getHiringById(@PathVariable(name = "id") long id) {
		try {
			MaintenanceInvoice hiring = service.findInvoiceById(id);
			if (hiring != null) {
				boolean currentStatus = hiring.isStatus();
				hiring.setStatus(!currentStatus);
				service.saveInvoice(hiring);
			} else {
				return ResponseEntity.ok(false);
			}

			return ResponseEntity.ok(hiring.isStatus());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}



	@PutMapping("/maintenance/edit/{id}")
	public ResponseEntity<?> updateHiring(@PathVariable("id") long id, @RequestBody MaintenanceInvoice maintenance) {
	    try {
	        MaintenanceInvoice existingHiring = service.findInvoiceById(id);
	        if (existingHiring == null) {
	            return ResponseEntity.notFound().build();
	        }

	        Optional<MaintenancePayment> existingPayment1 = maintenancePaymentRepository.findByMaintenanceInvoiceId(id);
	        if (!existingPayment1.isPresent()) {
	            return ResponseEntity.ok("MaintenancePayment not found for MaintenanceInvoiceId: " + id);
	        }

	        MaintenancePayment payment1 = existingPayment1.get();
	        double tttt = maintenance.getTotal();
	        double receivedAmountFromPayment = payment1.getReceivedAmount();

	        System.out.println("tttt: " + tttt);
	        System.out.println("receivedAmountFromPayment: " + receivedAmountFromPayment);

	        if (tttt < receivedAmountFromPayment) {
	            return ResponseEntity.badRequest().body("Received amount or Total exceeds the allowed threshold.");
	        }


	        existingHiring.setMaintenanceTermsId(maintenance.getMaintenanceTermsId());
	        existingHiring.setClientId(maintenance.getClientId());
	        existingHiring.setInvoiceDate(maintenance.getInvoiceDate());
	        existingHiring.setStatus(maintenance.isStatus());
	        existingHiring.setInvoiceNo(maintenance.getInvoiceNo());
	        existingHiring.setMaintenanceList(maintenance.getMaintenanceList());
	        existingHiring.setTotal(maintenance.getTotal());
	        service.saveInvoice(existingHiring);

	        Optional<MaintenancePayment> existingPayment = maintenancePaymentRepository.findByMaintenanceInvoiceId(id);
	        if (existingPayment.isPresent()) {
	            MaintenancePayment payment = existingPayment.get();
	            double iii = payment.getReceivedAmount();
	            double uuu = (tttt - iii);
	            payment.setPaymentDate(maintenance.getInvoiceDate());
	            payment.setBalance(uuu);
	            payment.setAmount(tttt);
	            maintenancePaymentService.save(payment);
	        } else {
	            return ResponseEntity.ok("MaintenancePayment not found for MaintenanceInvoiceId: " + id);
	        }

	        return ResponseEntity.ok(existingHiring);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}


	@GetMapping("/maintenance/view")
	public ResponseEntity<Object> getAllRoleByEmployee(@RequestParam(required = true) String maintenance) {
		if ("maintenance".equals(maintenance)) {
			List<Map<String, Object>> serverList = new ArrayList<>();
			List<Map<String, Object>> serverRole = repo.getAllServerDetails();
			Map<String, List<Map<String, Object>>> serverGroupMap = serverRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("maintenance_invoice_id").toString()));

			for (Entry<String, List<Map<String, Object>>> serverLoop : serverGroupMap.entrySet()) {
				Map<String, Object> serverMap = new HashMap<>();
				serverMap.put("maintenanceInvoiceId", Long.parseLong(serverLoop.getKey()));
				serverMap.put("clientId", serverLoop.getValue().get(0).get("client_id"));
				serverMap.put("companyId", serverLoop.getValue().get(0).get("company_id"));
				serverMap.put("invoiceDate", serverLoop.getValue().get(0).get("invoice_date"));
				serverMap.put("invoiceNo", serverLoop.getValue().get(0).get("invoice_no"));
				serverMap.put("terms", serverLoop.getValue().get(0).get("terms"));
				serverMap.put("title", serverLoop.getValue().get(0).get("title"));
				serverMap.put("maintenanceTermsId", serverLoop.getValue().get(0).get("maintenance_terms_id"));
				serverMap.put("clientName", serverLoop.getValue().get(0).get("client_name"));
				serverMap.put("address", serverLoop.getValue().get(0).get("address"));
				serverMap.put("bankName", serverLoop.getValue().get(0).get("bank_name"));
				serverMap.put("companyAddress", serverLoop.getValue().get(0).get("companyAddress"));
				serverMap.put("accountNo", serverLoop.getValue().get(0).get("account_no"));
				serverMap.put("zipCode", serverLoop.getValue().get(0).get("zip_code"));
				serverMap.put("state", serverLoop.getValue().get(0).get("state"));
				serverMap.put("phoneNumber", serverLoop.getValue().get(0).get("phone_number"));
				serverMap.put("mobileNumber", serverLoop.getValue().get(0).get("mobile_number"));
				serverMap.put("email", serverLoop.getValue().get(0).get("email"));
				serverMap.put("country", serverLoop.getValue().get(0).get("country"));
				serverMap.put("city", serverLoop.getValue().get(0).get("city"));
				serverMap.put("branchName", serverLoop.getValue().get(0).get("branch_name"));
				serverMap.put("companyCountry", serverLoop.getValue().get(0).get("companyCountry"));
				serverMap.put("companyEmail", serverLoop.getValue().get(0).get("companyEmail"));
				serverMap.put("gstNo", serverLoop.getValue().get(0).get("gst_no"));
				serverMap.put("status", serverLoop.getValue().get(0).get("status"));
				serverMap.put("holderName", serverLoop.getValue().get(0).get("holder_name"));
				serverMap.put("ifscCode", serverLoop.getValue().get(0).get("ifsc_code"));
				serverMap.put("location", serverLoop.getValue().get(0).get("location"));
				serverMap.put("phoneNumber1", serverLoop.getValue().get(0).get("phone_number1"));
				serverMap.put("phoneNumber2", serverLoop.getValue().get(0).get("phone_number2"));
				serverMap.put("pincode", serverLoop.getValue().get(0).get("pincode"));
				serverMap.put("taxNo", serverLoop.getValue().get(0).get("tax_no"));
				serverMap.put("total", serverLoop.getValue().get(0).get("total"));
				int randomNumber = generateRandomNumber();
				String imageUrl = "company/" + randomNumber + "/" + serverLoop.getValue().get(0).get("company_id");
				serverMap.put("company", imageUrl);
				List<Map<String, Object>> serverSubList = new ArrayList<>();
				for (Map<String, Object> serverSubLoop : serverLoop.getValue()) {

					Map<String, Object> serverSubMap = new HashMap<>();
					serverSubMap.put("maintenanceListId", serverSubLoop.get("maintenance_list_id"));
					serverSubMap.put("description", serverSubLoop.get("description"));
					serverSubMap.put("price", serverSubLoop.get("price"));
					serverSubMap.put("projectId", serverSubLoop.get("project_id"));
					serverSubMap.put("subTotal", serverSubLoop.get("sub_total"));
					serverSubMap.put("projectName", serverSubLoop.get("project_name"));
					serverSubMap.put("tax", serverSubLoop.get("tax"));
					serverSubMap.put("taxAmount", serverSubLoop.get("tax_amount"));

					serverSubList.add(serverSubMap);
				}
				serverMap.put("maintenanceList", serverSubList);
				serverList.add(serverMap);
			}

			return ResponseEntity.ok(serverList);
		} else {
			String errorMessage = "Invalid value for 'department'. Expected 'Department'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	private String getString(Map<String, Object> action, String key) {
	    Object value = action.get(key);
	    return value != null ? value.toString() : "null";
	}
	
	@PostMapping("/maintenance/date")
	public ResponseEntity<?> getAllQuotationByClientDetails1(@RequestBody Map<String, Object> requestBody) {
	    if (requestBody.containsKey("startDate") && requestBody.containsKey("endDate")) {
	        try {
	            LocalDate startDate = LocalDate.parse(requestBody.get("startDate").toString(), DateTimeFormatter.ISO_DATE);
	            LocalDate endDate = LocalDate.parse(requestBody.get("endDate").toString(), DateTimeFormatter.ISO_DATE);
		
			List<Map<String, Object>> invoiceRole = repo.getAllpromotionsBetweenDatesList(startDate, endDate);

			Map<String, Map<String, List<Map<String, Object>>>> clientGroupMap = invoiceRole.stream()
			        .collect(Collectors.groupingBy( action -> getString(action, "maintenance_invoice_id"),
			                Collectors.groupingBy(action -> getString(action, "maintenance_list_id"))));
			List<Map<String, Object>> invoiceList = new ArrayList<>();
			for (Entry<String, Map<String, List<Map<String, Object>>>> invoiceLoop : clientGroupMap.entrySet()) {
				Map<String, Object> serverMap = new HashMap<>();
				serverMap.put("maintenanceInvoiceId", invoiceLoop.getKey());

				List<Map<String, Object>> invoiceSubList = new ArrayList<>();
				for (Entry<String, List<Map<String, Object>>> serverLoop : invoiceLoop.getValue().entrySet()) {
					Map<String, Object> serverSubMap = new HashMap<>();
					serverSubMap.put("maintenance_list_id", serverLoop.getKey());
					serverMap.put("clientId", serverLoop.getValue().get(0).get("client_id"));
					serverMap.put("companyId", serverLoop.getValue().get(0).get("company_id"));
					serverMap.put("invoiceDate", serverLoop.getValue().get(0).get("invoice_date"));
					serverMap.put("invoiceNo", serverLoop.getValue().get(0).get("invoice_no"));
					serverMap.put("terms", serverLoop.getValue().get(0).get("terms"));
					serverMap.put("title", serverLoop.getValue().get(0).get("title"));
					serverMap.put("maintenanceTermsId", serverLoop.getValue().get(0).get("maintenance_terms_id"));
					serverMap.put("clientName", serverLoop.getValue().get(0).get("client_name"));
					serverMap.put("address", serverLoop.getValue().get(0).get("address"));
					serverMap.put("bankName", serverLoop.getValue().get(0).get("bank_name"));
					serverMap.put("companyAddress", serverLoop.getValue().get(0).get("companyAddress"));
					serverMap.put("accountNo", serverLoop.getValue().get(0).get("account_no"));
					serverMap.put("zipCode", serverLoop.getValue().get(0).get("zip_code"));
					serverMap.put("state", serverLoop.getValue().get(0).get("state"));
					serverMap.put("phoneNumber", serverLoop.getValue().get(0).get("phone_number"));
					serverMap.put("mobileNumber", serverLoop.getValue().get(0).get("mobile_number"));
					serverMap.put("email", serverLoop.getValue().get(0).get("email"));
					serverMap.put("country", serverLoop.getValue().get(0).get("country"));
					serverMap.put("city", serverLoop.getValue().get(0).get("city"));
					serverMap.put("branchName", serverLoop.getValue().get(0).get("branch_name"));
					serverMap.put("companyCountry", serverLoop.getValue().get(0).get("companyCountry"));
					serverMap.put("companyEmail", serverLoop.getValue().get(0).get("companyEmail"));
					serverMap.put("gstNo", serverLoop.getValue().get(0).get("gst_no"));
					serverMap.put("status", serverLoop.getValue().get(0).get("status"));
					serverMap.put("holderName", serverLoop.getValue().get(0).get("holder_name"));
					serverMap.put("ifscCode", serverLoop.getValue().get(0).get("ifsc_code"));
					serverMap.put("location", serverLoop.getValue().get(0).get("location"));
					serverMap.put("phoneNumber1", serverLoop.getValue().get(0).get("phone_number1"));
					serverMap.put("phoneNumber2", serverLoop.getValue().get(0).get("phone_number2"));
					serverMap.put("pincode", serverLoop.getValue().get(0).get("pincode"));
					serverMap.put("taxNo", serverLoop.getValue().get(0).get("tax_no"));
					serverMap.put("total", serverLoop.getValue().get(0).get("total"));
					int randomNumber = generateRandomNumber();
					String imageUrl = "company/" + randomNumber + "/" + serverLoop.getValue().get(0).get("company_id");
					serverMap.put("company", imageUrl);

					serverSubMap.put("maintenanceInvoiceId", invoiceLoop.getKey());
					serverSubMap.put("description", serverLoop.getValue().get(0).get("description"));
					serverSubMap.put("price", serverLoop.getValue().get(0).get("price"));
					serverSubMap.put("projectId", serverLoop.getValue().get(0).get("project_id"));
					serverSubMap.put("subTotal", serverLoop.getValue().get(0).get("sub_total"));
					serverSubMap.put("projectName", serverLoop.getValue().get(0).get("project_name"));
					serverSubMap.put("tax", serverLoop.getValue().get(0).get("tax"));
					serverSubMap.put("taxAmount", serverLoop.getValue().get(0).get("tax_amount"));


					invoiceSubList.add(serverSubMap);
				}
				serverMap.put("maintenanceList", invoiceSubList);
				invoiceList.add(serverMap);
			}

			    return ResponseEntity.ok(invoiceList);
	        } catch (DateTimeParseException e) {
	            String errorMessage = "Invalid date format. Please provide dates in ISO format.";
	            return ResponseEntity.badRequest().body(errorMessage);
	        }
	    }


	    return ResponseEntity.ok(Collections.emptyList());
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}

	@GetMapping("/maintenance/dashboard")
	public List<Map<String, Object>> allDeptDetails(@RequestParam(required = true) String dashboard) {
		try {
			if ("maintenance".equals(dashboard)) {
				return repo.getAllInvoiceDetailsListuuuu();
			} else {
				return Collections.emptyList();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@PostMapping("/maintenance/manager")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndYearOrMonth(
			@RequestBody Map<String, Object> requestBody) {

		if (!requestBody.containsKey("choose")) {
			return ResponseEntity.badRequest().build();
		}

		String choose = requestBody.get("choose").toString();

		switch (choose) {

		case "sixMonth":
			List<Map<String, Object>> leaveData3 = repo.getAllReceiptDetailsWithClientIdAndSixMonths();
			return ResponseEntity.ok(leaveData3);

		case "date":
			if (requestBody.containsKey("startDate") && requestBody.containsKey("endDate")) {
				LocalDate startDate = LocalDate.parse(requestBody.get("startDate").toString(),
						DateTimeFormatter.ISO_DATE);
				LocalDate endDate = LocalDate.parse(requestBody.get("endDate").toString(), DateTimeFormatter.ISO_DATE);
				return ResponseEntity.ok(repo.getAllpromotionsBetweenDates(startDate, endDate));
			}
			break;
		case "year":
			if (requestBody.containsKey("year")) {
				String year = requestBody.get("year").toString();
				List<Map<String, Object>> leaveData = repo.getAllReceiptDetailsWithClientIdAnd(year);
				return ResponseEntity.ok(leaveData);
			}
			break;

		case "month":
			if (requestBody.containsKey("monthName")) {
				String monthName = requestBody.get("monthName").toString();
				List<Map<String, Object>> leaveData1 = repo.getAllReceiptDetailsWithClientIdAndMonth(monthName);
				return ResponseEntity.ok(leaveData1);
			}
			break;

		default:
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.badRequest().build();
	}

	@PostMapping("/maintenance1/manager")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDafdtesAndYearOrMonth(
			@RequestBody Map<String, Object> requestBody) {

		if (!requestBody.containsKey("choose")) {
			return ResponseEntity.badRequest().build();
		}

		String choose = requestBody.get("choose").toString();

		switch (choose) {
		case "sixMonth":
			List<Map<String, Object>> leaveData3 = repo.getAllReceiptDetailsWithClientIdAndSixMonths();
			return ResponseEntity.ok(leaveData3);

		// Other cases for "date", "year", "month" can be added as needed

		default:
			return ResponseEntity.badRequest().build();
		}
	}

}
