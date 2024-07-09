package com.example.erp.controller.accounting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.erp.entity.accounting.MaintenancePayment;
import com.example.erp.repository.accounting.MaintenancePaymentRepository;
import com.example.erp.service.accounting.MaintenancePaymentService;


@RestController
@CrossOrigin
public class MaintenancePaymentController {

	@Autowired
	private MaintenancePaymentService  maintenancePaymentService;
	
	
	@Autowired
	private MaintenancePaymentRepository maintenancePaymentRepository;
	

	@GetMapping("/maintenancePayment")
	public ResponseEntity<?> getDetails(@RequestParam(required = true) String maintenancePayment) {
	    try {
	        if ("maintenancePayment".equals(maintenancePayment)) {
			Iterable<MaintenancePayment> assestDetails = maintenancePaymentService.listAll();
			return new ResponseEntity<>(assestDetails, HttpStatus.OK);
		} 
	        else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided maintenancePayment is not supported.");
	        }
	    }catch (Exception e) {
	        
	        }
			String errorMessage = "An error occurred while retrieving l details.";
			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	

	@PostMapping("/maintenancePayment/save")
	public ResponseEntity<?> saveBank(@RequestBody MaintenancePayment maintenancePayment) {

		try {
			maintenancePaymentService.save(maintenancePayment);
			return ResponseEntity.status(HttpStatus.CREATED).body("maintenancePayment details saved successfully.");
		} catch (Exception e) {
			String errorMessage = "An error occurred while saving maintenancePayment details.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@RequestMapping("/maintenancePayment/{id}")
	private Optional<MaintenancePayment> getKeyboardBrand(@PathVariable(name = "id") long maintenancePaymentId) {
		return maintenancePaymentService.getKeyboardBrandById(maintenancePaymentId);
	}

	@PutMapping("/maintenancePayment/edit/{id}")
	public ResponseEntity<MaintenancePayment> updateKeyboardBrand(@PathVariable("id") Long maintenancePaymentId,
			@RequestBody MaintenancePayment brandDetails) {
		try {
			MaintenancePayment existingpayment = maintenancePaymentService.findById(maintenancePaymentId);
			if (existingpayment == null) {
				

				return ResponseEntity.notFound().build();
			}
//			existingpayment.setMaintenanceInvoiceId(brandDetails.getMaintenanceInvoiceId());
			existingpayment.setPaymentDate(brandDetails.getPaymentDate());
			existingpayment.setPaymentType(brandDetails.getPaymentType());
//			existingpayment.setProjectId(brandDetails.getProjectId());
			existingpayment.setReceivedAmount(brandDetails.getReceivedAmount());
			existingpayment.setBalance(brandDetails.getBalance());
			maintenancePaymentService.save(existingpayment);
			return ResponseEntity.ok(existingpayment);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/maintenancePayment/delete/{id}")
	public ResponseEntity<String> deleteKeyboardBrand(@PathVariable("id") Long maintenancePaymentId) {
		maintenancePaymentService.deleteKeyboardBrandById(maintenancePaymentId);
		return ResponseEntity.ok("maintenancePayment deleted successfully");

	}
	
	 @GetMapping("/maintenancePayment/client")
	    public ResponseEntity<Object> getAllRoleByEmployeeDetails(@RequestParam(required = true) String maintenancePayment) {
	        if ("client".equals(maintenancePayment)) {
	            try {
	                List<Map<String, Object>> departmentList = new ArrayList<>();
	                List<Map<String, Object>> departmentRole = maintenancePaymentRepository.getAllInvoiceDetailsList();

	                Map<String, List<Map<String, Object>>> departmentGroupMap = departmentRole.stream()
	                        .collect(Collectors.groupingBy(action -> action.get("client_id").toString()));

	                for (Entry<String, List<Map<String, Object>>> departmentLoop : departmentGroupMap.entrySet()) {
	                    Map<String, Object> departmentMap = new HashMap<>();
	                    List<Map<String, Object>> departmentSubList = new ArrayList<>();

	                    for (Map<String, Object> departmentSubLoop : departmentLoop.getValue()) {
	                        Map<String, Object> departmentSubMap = new HashMap<>();
	                        departmentSubMap.put("maintenancePaymentId", departmentSubLoop.get("maintenance_payment_id"));
	                        departmentSubMap.put("paymentDate", departmentSubLoop.get("payment_date"));
	                        departmentSubMap.put("invoiceNo", departmentSubLoop.get("invoice_no"));	
	                        departmentSubMap.put("paymentType", departmentSubLoop.get("payment_type"));
	                        departmentSubMap.put("received_amount", departmentSubLoop.get("received_amount"));
	                        departmentSubMap.put("amount", departmentSubLoop.get("amount"));
	                        departmentSubMap.put("balance", departmentSubLoop.get("balance"));
	                        departmentSubMap.put("payDate", departmentSubLoop.get("pay_date"));
	                        departmentSubList.add(departmentSubMap);
	                    }

	                    departmentMap.put("clientId", Long.parseLong(departmentLoop.getKey()));
	                    departmentMap.put("clientName", departmentLoop.getValue().get(0).get("client_name"));
	                    departmentMap.put("address", departmentLoop.getValue().get(0).get("address"));
	                    departmentMap.put("city", departmentLoop.getValue().get(0).get("city"));
	                    departmentMap.put("country", departmentLoop.getValue().get(0).get("country"));
	                    departmentMap.put("email", departmentLoop.getValue().get(0).get("email"));
	                    departmentMap.put("gender", departmentLoop.getValue().get(0).get("gender"));
	                    departmentMap.put("phoneNumber", departmentLoop.getValue().get(0).get("phone_number"));
	                    departmentMap.put("state", departmentLoop.getValue().get(0).get("state"));
	                    departmentMap.put("zipCode", departmentLoop.getValue().get(0).get("zip_code"));
	                    departmentMap.put("mobileNumber", departmentLoop.getValue().get(0).get("mobile_number"));
	                    departmentMap.put("maintenancePayment", departmentSubList);
	                    departmentList.add(departmentMap);
	                }

	                return ResponseEntity.ok(departmentList);
	            } catch (Exception e) {
	                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request.");
	            }
	        } else {
	            return ResponseEntity.badRequest().body("Invalid value for 'maintenancePayment'. Expected 'client'.");
	        }
	    }
	
	@GetMapping("/maintenancePayment/client/{id}")
	public ResponseEntity<Object> getAllRoleByEmployee(@PathVariable("id") Long clientId) {
	
			List<Map<String, Object>> departmentList = new ArrayList<>();
			List<Map<String, Object>> departmentRole = maintenancePaymentRepository.getAllInvoiceDetailsList23(clientId);
			Map<String, List<Map<String, Object>>> departmentGroupMap = departmentRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("client_id").toString()));

			for (Entry<String, List<Map<String, Object>>> departmentLoop : departmentGroupMap.entrySet()) {
				Map<String, Object> departmentMap = new HashMap<>();
				departmentMap.put("clientId", Long.parseLong(departmentLoop.getKey()));
				departmentMap.put("clientName", departmentLoop.getValue().get(0).get("client_name"));
				departmentMap.put("address", departmentLoop.getValue().get(0).get("address"));
				departmentMap.put("city", departmentLoop.getValue().get(0).get("city"));
				departmentMap.put("country", departmentLoop.getValue().get(0).get("country"));
				departmentMap.put("email", departmentLoop.getValue().get(0).get("email"));
				departmentMap.put("gender", departmentLoop.getValue().get(0).get("gender"));
				departmentMap.put("phoneNumber", departmentLoop.getValue().get(0).get("phone_number"));
				departmentMap.put("state", departmentLoop.getValue().get(0).get("state"));
				departmentMap.put("zipCode", departmentLoop.getValue().get(0).get("zip_code"));
				departmentMap.put("mobileNumber", departmentLoop.getValue().get(0).get("mobile_number"));
				List<Map<String, Object>> departmentSubList = new ArrayList<>();
				for (Map<String, Object> departmentsubLoop : departmentLoop.getValue()) {

					Map<String, Object> departmentSubMap = new HashMap<>();
					departmentSubMap.put("maintenancePaymentId", departmentsubLoop.get("maintenance_payment_id"));
					departmentSubMap.put("paymentDate", departmentsubLoop.get("payment_date"));
					departmentSubMap.put("invoiceNo", departmentsubLoop.get("invoice_no"));				
					departmentSubMap.put("amount", departmentsubLoop.get("received_amount"));
                    departmentSubMap.put("balance", departmentsubLoop.get("balance"));
					departmentSubMap.put("payDate", departmentsubLoop.get("pay_date"));
					 departmentSubMap.put("amount", departmentsubLoop.get("amount"));
					departmentSubList.add(departmentSubMap);
				}
				departmentMap.put("maintenancePayment", departmentSubList);
				departmentList.add(departmentMap);
			}

			return ResponseEntity.ok(departmentList);
		} 
	
	@PostMapping("/maintenancePayment/client")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndYearOrMonth(
	        @RequestBody Map<String, Object> requestBody) {
		 if (!requestBody.containsKey("choose") || !requestBody.containsKey("clientId")) {
	            return ResponseEntity.badRequest().build();
	        }

	        String choose = requestBody.get("choose").toString();
	        long clientId = Long.parseLong(requestBody.get("clientId").toString());

	    switch (choose) {
	       	        case "year":
	            if (requestBody.containsKey("year")) {
	                String year = requestBody.get("year").toString();
	                List<Map<String, Object>> leaveData = maintenancePaymentRepository.getAllReceiptDetailsWithClientIdAndyear(year,clientId);
	                return ResponseEntity.ok(leaveData);
	            }
	            break;
	        case "month":
	            if (requestBody.containsKey("monthName")) {
	                String monthName = requestBody.get("monthName").toString();
	                List<Map<String, Object>> leaveData1 = maintenancePaymentRepository.getAllReceiptDetailsWithClientIdAndMonthyear(monthName,clientId);
	                return ResponseEntity.ok(leaveData1);
	            }
	            break;
	        default:
	            return ResponseEntity.badRequest().build();
	    }
	    return ResponseEntity.badRequest().build();
	}
	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000); // Change the upper limit if needed
	}
	 @PostMapping("/maintenancePayment/manager")
	    public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAnd(
	            @RequestBody Map<String, Object> requestBody) {

			 if (!requestBody.containsKey("choose")) {
			        return ResponseEntity.badRequest().build();
			    }

			    String choose = requestBody.get("choose").toString();
	        switch (choose) {
	            case "year":
	                if (requestBody.containsKey("year")) {
	                    String year = requestBody.get("year").toString();
	                    List<Map<String, Object>> leaveData = maintenancePaymentRepository.getAllReceiptDetailsWithClientIdAnd(year);
	                    List<Map<String, Object>> imageResponses = new ArrayList<>();
	                    for (Map<String, Object> image : leaveData) {
	                        int randomNumber = generateRandomNumber();
	                        String imageUrl = "clientProfile/" + randomNumber + "/" + image.get("client_id");
	                        Map<String, Object> imageResponse = new HashMap<>();
	                        imageResponse.put("clientProfile", imageUrl);
	                        imageResponse.putAll(image);
	                        imageResponses.add(imageResponse);
	                    }
	                    
	                    return ResponseEntity.ok(imageResponses);
	                }
	                break;

	            case "month":
	                if (requestBody.containsKey("monthName")) {
	                    String monthName = requestBody.get("monthName").toString();
	                    List<Map<String, Object>> leaveData1 = maintenancePaymentRepository.getAllReceiptDetailsWithClientIdAndMonth(monthName);
	                    List<Map<String, Object>> imageResponses = new ArrayList<>();
	                    for (Map<String, Object> image : leaveData1) {
	                        int randomNumber = generateRandomNumber();
	                        String imageUrl = "clientProfile/" + randomNumber + "/" + image.get("client_id");
	                        Map<String, Object> imageResponse = new HashMap<>();
	                        imageResponse.put("clientProfile", imageUrl);
	                        imageResponse.putAll(image);
	                        imageResponses.add(imageResponse);
	                    }
	                    return ResponseEntity.ok(imageResponses);
	                }
	                break;

	            default:
	                return ResponseEntity.badRequest().build();
	        }

	        return ResponseEntity.badRequest().build();
	        
	    }


}
