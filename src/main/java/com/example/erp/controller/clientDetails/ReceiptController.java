package com.example.erp.controller.clientDetails;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.erp.entity.clientDetails.ClientInvoice;
import com.example.erp.entity.clientDetails.Receipts;
import com.example.erp.repository.clientDetails.ReceiptRepository;
import com.example.erp.service.clientDetails.ClientInvoiceService;
import com.example.erp.service.clientDetails.ReceiptService;

@RestController
@CrossOrigin
public class ReceiptController {

	@Autowired
	private ReceiptService receiptService;
	
	@Autowired
	private ClientInvoiceService clientInvoiceService;
	
	@Autowired
	private ReceiptRepository receiptRepository;
	
	@PostMapping("/receipt/save")
	public String saveBook(@RequestBody Receipts receipt) {
		double receivedAmount = receipt.getReceivedAmount();
		receipt.setReceivedAmount(receivedAmount);

		ClientInvoice invoice = clientInvoiceService.findInvoiceById1(receipt.getInvoiceId());
		 if (invoice.getBalanceAmount() == 0) {
	            return "Invoice balance is already 0. No further payment required.";
	        }
		receiptService.SaveReceipt(receipt);
		double balance = invoice.getBalanceAmount() - receivedAmount;
		double receivedAmount2 = invoice.getReceived()+ receivedAmount;
		invoice.setBalanceAmount(balance);
		invoice.setBalance(balance);
		invoice.setReceived(receivedAmount2);
		double total = invoice.getTotalList() + receivedAmount;
		invoice.setTotalList(total);
		clientInvoiceService.saveInvoice(invoice);
		if (balance == 0) {
			return "Payment successfully saved. Balance is now 0.";
		} else {
			return "Payment successfully saved. Balance remaining: " + balance;
		}
	}
	

	@GetMapping("/receipt/details")
	public ResponseEntity<Object> getAllReceiptDetailsByInvoice(@RequestParam(required = true) String receipt){
		if("Receipts".equals(receipt)) {
			List<Map<String, Object>> receiptList = new ArrayList<>();
			List<Map<String, Object>> receiptDetails = receiptRepository.getAllReceiptDetailsByInvoice();
			Map<String, List<Map<String, Object>>> receiptGroupMap = receiptDetails.stream()
					.collect(Collectors.groupingBy(action -> action.get("client_id").toString()));

			for(Entry<String, List<Map<String, Object>>> receiptLoop : receiptGroupMap.entrySet()) {
				Map<String, Object> receiptMap = new HashMap<>();
				
				receiptMap.put("client_id", receiptLoop.getKey());
				receiptMap.put("client_name", receiptLoop.getValue().get(0).get("client_name"));
				receiptMap.put("clientDetails", receiptLoop.getValue());
				receiptList.add(receiptMap);
			}
			return ResponseEntity.ok(receiptList);
		}else {
			String errorMessage = "Invalid value for 'receipt'. Expected 'Receipts'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@GetMapping("/receipt/{id}")
	public List<Map<String, Object>> getAllReceiptDetailsByInvoiceId(@PathVariable("id") Long client_id){
		List<Map<String, Object>> receiptList = new ArrayList<>();
		List<Map<String, Object>> receiptDetails = receiptRepository.getAllReceiptDetailsByClientId(client_id);
		Map<String, List<Map<String, Object>>> receiptGroupMap = receiptDetails.stream()
				.collect(Collectors.groupingBy(action -> action.get("client_id").toString()));

		for(Entry<String, List<Map<String, Object>>> receiptLoop : receiptGroupMap.entrySet()) {
			Map<String, Object> receiptMap = new HashMap<>();
			
			receiptMap.put("client_id", receiptLoop.getKey());
			receiptMap.put("client_name", receiptLoop.getValue().get(0).get("client_name"));
			receiptMap.put("clientDetails", receiptLoop.getValue());
			receiptList.add(receiptMap);
		}
		return receiptList;
	}

	@GetMapping("/receipt/invoice/view")
	public ResponseEntity<Object> getAllReceipt(@RequestParam(required = true) String receipt) {
		if ("receiptDetails".equals(receipt)) {
			List<Map<String, Object>> receiptList = new ArrayList<>();
			List<Map<String, Object>> receiptRole = receiptRepository.getAllReceiptDetailsWithClient();
			Map<String, List<Map<String, Object>>> departmentGroupMap = receiptRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("client_id").toString()));

			for (Entry<String, List<Map<String, Object>>> receiptLoop : departmentGroupMap.entrySet()) {
				Map<String, Object> receiptMap = new HashMap<>();
				receiptMap.put("clientId", Long.parseLong(receiptLoop.getKey()));
				receiptMap.put("clientName", receiptLoop.getValue().get(0).get("client_name"));
				receiptMap.put("phoneNumber", receiptLoop.getValue().get(0).get("phone_number"));
				receiptMap.put("mobileNumber", receiptLoop.getValue().get(0).get("mobile_number"));
				receiptMap.put("address", receiptLoop.getValue().get(0).get("address"));
				List<Map<String, Object>> receiptSubList = new ArrayList<>();
				for (Map<String, Object> receiptSubLoop : receiptLoop.getValue()) {

					Map<String, Object> receiptSubMap = new HashMap<>();
//					int randomNumber = generateRandomNumber();
//					String fileExtension = getFileExtensionForImage(taskAssigned);
//					String imageUrl = "company/" + randomNumber + "/" + taskAssigned.get("company_id") + "." + fileExtension;
//					String imageUrl1 = "signature/" + randomNumber + "/" + taskAssigned.get("company_id") + "." + fileExtension;
//					taskAssignedMap.put("profile", imageUrl);
//					taskAssignedMap.put("signature", imageUrl1);
					receiptSubMap.put("invoiceId", receiptSubLoop.get("invoice_id"));
					receiptSubMap.put("receiptId", receiptSubLoop.get("receipt_id"));
					receiptSubMap.put("balance", receiptSubLoop.get("balance"));
					receiptSubMap.put("paymentDate", receiptSubLoop.get("payment_date"));
					receiptSubMap.put("paymentType", receiptSubLoop.get("payment_type"));
					receiptSubMap.put("receivedAmount", receiptSubLoop.get("received_amount"));
					receiptSubMap.put("clientId", receiptSubLoop.get("client_id"));
					receiptSubMap.put("companyId", receiptSubLoop.get("company_id"));
					receiptSubMap.put("address", receiptSubLoop.get("address"));
					receiptSubMap.put("city", receiptSubLoop.get("city"));
					receiptSubMap.put("clientName", receiptSubLoop.get("client_name"));
					receiptSubMap.put("country", receiptSubLoop.get("country"));
					receiptSubMap.put("email", receiptSubLoop.get("email"));
					receiptSubMap.put("gender", receiptSubLoop.get("gender"));
					receiptSubMap.put("phoneNumber", receiptSubLoop.get("phone_number"));
					receiptSubMap.put("mobileNumber", receiptSubLoop.get("mobile_number"));
					receiptSubMap.put("state", receiptSubLoop.get("state"));
					receiptSubMap.put("zipCode", receiptSubLoop.get("zip_code"));
					receiptSubMap.put("companyName", receiptSubLoop.get("company_name"));
					receiptSubMap.put("companyId", receiptSubLoop.get("company_id"));
					receiptSubMap.put("companyEmail", receiptSubLoop.get("companyEmail"));
					receiptSubMap.put("companyPhoneNumber1", receiptSubLoop.get("phone_number1"));
					receiptSubMap.put("companyPhoneNumber2", receiptSubLoop.get("phone_number2"));
					receiptSubMap.put("companyZipcode", receiptSubLoop.get("pincode"));
					receiptSubMap.put("companyCountry", receiptSubLoop.get("companyCountry"));
					receiptSubMap.put("companyAddress", receiptSubLoop.get("companyAddress"));
					receiptSubMap.put("amount", receiptSubLoop.get("amount"));


					receiptSubList.add(receiptSubMap);
				}
				receiptMap.put("clientDetails", receiptSubList);
				receiptList.add(receiptMap);
			}

			return ResponseEntity.ok(receiptList);
		} else {
			String errorMessage = "Invalid value for 'receipt'. Expected 'receiptDetails'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}
	@GetMapping("/receipt/invoice")
	public ResponseEntity<?> getTaskAssignedDetailsp22222(@RequestParam(required = true) String receipt) {
		try {
			if ("receiptDetails".equals(receipt)) {
				List<Map<String, Object>> tasks = receiptRepository.getAllReceiptDetails();
				List<Map<String, Object>> receiptList = new ArrayList<>();
				Map<String, Object> receiptMap = new HashMap<>();
				List<Map<String, Object>> receiptSubList = new ArrayList<>();
				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();
					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(taskAssigned);
					String imageUrl = "company/" + randomNumber + "/" + taskAssigned.get("company_id") + "." + fileExtension;
					String imageUrl1 = "signature/" + randomNumber + "/" + taskAssigned.get("company_id") + "." + fileExtension;
					taskAssignedMap.put("profile", imageUrl);
					taskAssignedMap.put("signature", imageUrl1);
					taskAssignedMap.put("invoiceId", taskAssigned.get("invoice_id"));
					taskAssignedMap.put("receiptId", taskAssigned.get("receipt_id"));
					taskAssignedMap.put("balance", taskAssigned.get("balance"));
					taskAssignedMap.put("paymentDate", taskAssigned.get("payment_date"));
					taskAssignedMap.put("paymentType", taskAssigned.get("payment_type"));
					taskAssignedMap.put("receivedAmount", taskAssigned.get("received_amount"));
					taskAssignedMap.put("clientId", taskAssigned.get("client_id"));
					taskAssignedMap.put("companyId", taskAssigned.get("company_id"));
					taskAssignedMap.put("address", taskAssigned.get("address"));
					taskAssignedMap.put("city", taskAssigned.get("city"));
					taskAssignedMap.put("clientName", taskAssigned.get("client_name"));
					taskAssignedMap.put("country", taskAssigned.get("country"));
					taskAssignedMap.put("email", taskAssigned.get("email"));
					taskAssignedMap.put("gender", taskAssigned.get("gender"));
					taskAssignedMap.put("phoneNumber", taskAssigned.get("phone_number"));
					taskAssignedMap.put("mobileNumber", taskAssigned.get("mobile_number"));
					taskAssignedMap.put("state", taskAssigned.get("state"));
					taskAssignedMap.put("zipCode", taskAssigned.get("zip_code"));
					taskAssignedMap.put("companyName", taskAssigned.get("company_name"));
					taskAssignedMap.put("companyId", taskAssigned.get("company_id"));
					taskAssignedMap.put("companyEmail", taskAssigned.get("companyEmail"));
					taskAssignedMap.put("companyPhoneNumber1", taskAssigned.get("phone_number1"));
					taskAssignedMap.put("companyPhoneNumber2", taskAssigned.get("phone_number2"));
					taskAssignedMap.put("companyZipcode", taskAssigned.get("pincode"));
					taskAssignedMap.put("companyCountry", taskAssigned.get("companyCountry"));
					taskAssignedMap.put("companyAddress", taskAssigned.get("companyAddress"));
					taskAssignedMap.put("amount", taskAssigned.get("amount"));
					receiptSubList.add(taskAssignedMap);
				}
				receiptMap.put("clientDetails", receiptSubList);
				
				receiptList.add(receiptMap);
				return ResponseEntity.ok(receiptList);
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
	
//	@GetMapping("/receipt/invoice")
//	public ResponseEntity<Object> getAllRoleByEmployee(@RequestParam(required = true) String receipt) {
//		if ("receiptDetails".equals(receipt)) {
//			List<Map<String, Object>> receiptList = new ArrayList<>();
//			List<Map<String, Object>> receiptRole = receiptRepository.getAllReceiptDetails();
//			Map<String, List<Map<String, Object>>> departmentGroupMap = receiptRole.stream()
//					.collect(Collectors.groupingBy(action -> action.get("client_id").toString()));
//
//			for (Entry<String, List<Map<String, Object>>> receiptLoop : departmentGroupMap.entrySet()) {
//				Map<String, Object> receiptMap = new HashMap<>();
//				receiptMap.put("clientId", Long.parseLong(receiptLoop.getKey()));
//				receiptMap.put("clientName", receiptLoop.getValue().get(0).get("client_name"));
//				receiptMap.put("phoneNumber", receiptLoop.getValue().get(0).get("phone_number"));
//				receiptMap.put("mobileNumber", receiptLoop.getValue().get(0).get("mobile_number"));
//				receiptMap.put("address", receiptLoop.getValue().get(0).get("address"));
//				List<Map<String, Object>> receiptSubList = new ArrayList<>();
//				for (Map<String, Object> receiptSubLoop : receiptLoop.getValue()) {
//					Map<String, Object> receiptSubMap = new HashMap<>();
//					receiptSubMap.put("invoiceId", receiptSubLoop.get("invoice_id"));
//					receiptSubMap.put("receiptId", receiptSubLoop.get("receipt_id"));
//					receiptSubMap.put("balance", receiptSubLoop.get("balance"));
//					receiptSubMap.put("paymentDate", receiptSubLoop.get("payment_date"));
//					receiptSubMap.put("paymentType", receiptSubLoop.get("payment_type"));
//					receiptSubMap.put("receivedAmount", receiptSubLoop.get("received_amount"));
//					receiptSubMap.put("clientId", receiptSubLoop.get("client_id"));
//					receiptSubMap.put("companyId", receiptSubLoop.get("company_id"));
//					receiptSubMap.put("address", receiptSubLoop.get("address"));
//					receiptSubMap.put("city", receiptSubLoop.get("city"));
//					receiptSubMap.put("clientName", receiptSubLoop.get("client_name"));
//					receiptSubMap.put("country", receiptSubLoop.get("country"));
//					receiptSubMap.put("email", receiptSubLoop.get("email"));
//					receiptSubMap.put("gender", receiptSubLoop.get("gender"));
//					receiptSubMap.put("phoneNumber", receiptSubLoop.get("phone_number"));
//					receiptSubMap.put("mobileNumber", receiptSubLoop.get("mobile_number"));
//					receiptSubMap.put("state", receiptSubLoop.get("state"));
//					receiptSubMap.put("zipCode", receiptSubLoop.get("zip_code"));
//					receiptSubMap.put("companyName", receiptSubLoop.get("company_name"));
//					receiptSubMap.put("companyId", receiptSubLoop.get("company_id"));
//					receiptSubMap.put("companyEmail", receiptSubLoop.get("companyEmail"));
//					receiptSubMap.put("companyPhoneNumber1", receiptSubLoop.get("phone_number1"));
//					receiptSubMap.put("companyPhoneNumber2", receiptSubLoop.get("phone_number2"));
//					receiptSubMap.put("companyZipcode", receiptSubLoop.get("pincode"));
//					receiptSubMap.put("companyCountry", receiptSubLoop.get("companyCountry"));
//					receiptSubMap.put("companyAddress", receiptSubLoop.get("companyAddress"));
//					receiptSubMap.put("amount", receiptSubLoop.get("amount"));
//
//
//					receiptSubList.add(receiptSubMap);
//				}
//				receiptMap.put("clientDetails", receiptSubList);
//				receiptList.add(receiptMap);
//			}
//
//			return ResponseEntity.ok(receiptList);
//		} else {
//			String errorMessage = "Invalid value for 'receipt'. Expected 'receiptDetails'.";
//			return ResponseEntity.badRequest().body(errorMessage);
//		}
//	}
	
	
	@GetMapping("/receipt/invoice/{id}")
	public ResponseEntity<Object> getAllReceiptDetailsWithId(@PathVariable("id") Long client_id) {
			List<Map<String, Object>> receiptList = new ArrayList<>();
			List<Map<String, Object>> receiptRole = receiptRepository.getAllReceiptDetailsWithClientId(client_id);
			Map<String, List<Map<String, Object>>> departmentGroupMap = receiptRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("client_id").toString()));

			for (Entry<String, List<Map<String, Object>>> receiptLoop : departmentGroupMap.entrySet()) {
				Map<String, Object> receiptMap = new HashMap<>();
				receiptMap.put("clientId", Long.parseLong(receiptLoop.getKey()));
				receiptMap.put("clientName", receiptLoop.getValue().get(0).get("client_name"));
				receiptMap.put("phoneNumber", receiptLoop.getValue().get(0).get("phone_number"));
				receiptMap.put("mobileNumber", receiptLoop.getValue().get(0).get("mobile_number"));
				receiptMap.put("address", receiptLoop.getValue().get(0).get("address"));

				List<Map<String, Object>> receiptSubList = new ArrayList<>();
				for (Map<String, Object> receiptSubLoop : receiptLoop.getValue()) {

					Map<String, Object> receiptSubMap = new HashMap<>();
					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(receiptSubLoop);
					String imageUrl = "company/" + randomNumber + "/" + receiptSubLoop.get("company_id") + "." + fileExtension;
					String imageUrl1 = "signature/" + randomNumber + "/" + receiptSubLoop.get("company_id") + "." + fileExtension;
					receiptSubMap.put("invoiceId", receiptSubLoop.get("invoice_id"));
					receiptSubMap.put("receiptId", receiptSubLoop.get("receipt_id"));
					receiptSubMap.put("balance", receiptSubLoop.get("balance"));
					receiptSubMap.put("paymentDate", receiptSubLoop.get("payment_date"));
					receiptSubMap.put("profile", imageUrl);
					receiptSubMap.put("signature", imageUrl1);
					receiptSubMap.put("paymentType", receiptSubLoop.get("payment_type"));
					receiptSubMap.put("receivedAmount", receiptSubLoop.get("received_amount"));
					receiptSubMap.put("clientId", receiptSubLoop.get("client_id"));
					receiptSubMap.put("companyId", receiptSubLoop.get("company_id"));
					receiptSubMap.put("address", receiptSubLoop.get("address"));
					receiptSubMap.put("city", receiptSubLoop.get("city"));
					receiptSubMap.put("clientName", receiptSubLoop.get("client_name"));
					receiptSubMap.put("country", receiptSubLoop.get("country"));
					receiptSubMap.put("email", receiptSubLoop.get("email"));
					receiptSubMap.put("gender", receiptSubLoop.get("gender"));
					receiptSubMap.put("phoneNumber", receiptSubLoop.get("phone_number"));
					receiptSubMap.put("mobileNumber", receiptSubLoop.get("mobile_number"));
					receiptSubMap.put("state", receiptSubLoop.get("state"));
					receiptSubMap.put("zipCode", receiptSubLoop.get("zip_code"));
					receiptSubMap.put("companyName", receiptSubLoop.get("company_name"));
					receiptSubMap.put("companyId", receiptSubLoop.get("company_id"));
					receiptSubMap.put("companyEmail", receiptSubLoop.get("companyEmail"));
					receiptSubMap.put("companyPhoneNumber1", receiptSubLoop.get("phone_number1"));
					receiptSubMap.put("companyPhoneNumber2", receiptSubLoop.get("phone_number2"));
					receiptSubMap.put("companyZipcode", receiptSubLoop.get("pincode"));
					receiptSubMap.put("companyCountry", receiptSubLoop.get("companyCountry"));
					receiptSubMap.put("companyAddress", receiptSubLoop.get("companyAddress"));
					receiptSubMap.put("amount", receiptSubLoop.get("amount"));
					receiptSubMap.put("companyState", receiptSubLoop.get("companyState"));
					receiptSubMap.put("companyLocation", receiptSubLoop.get("companyLocation"));

					receiptSubList.add(receiptSubMap);
				}
				receiptMap.put("clientDetails", receiptSubList);
			receiptList.add(receiptMap);
			
			}

			return ResponseEntity.ok(receiptList);
		
	}
	
	
	
	@GetMapping("/receipt/payment/{id}")
	public ResponseEntity<Object> getAllReceiptDetailsWithIdASD(@PathVariable("id") Long client_id) {
			List<Map<String, Object>> receiptList = new ArrayList<>();
			List<Map<String, Object>> receiptRole = receiptRepository.getAllReceiptDetailsWithClientId(client_id);
			Map<String, List<Map<String, Object>>> departmentGroupMap = receiptRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("client_id").toString()));

			for (Entry<String, List<Map<String, Object>>> receiptLoop : departmentGroupMap.entrySet()) {
				Map<String, Object> receiptMap = new HashMap<>();
				receiptMap.put("clientId", Long.parseLong(receiptLoop.getKey()));
				receiptMap.put("clientName", receiptLoop.getValue().get(0).get("client_name"));
				receiptMap.put("phoneNumber", receiptLoop.getValue().get(0).get("phone_number"));
				receiptMap.put("mobileNumber", receiptLoop.getValue().get(0).get("mobile_number"));
				receiptMap.put("address", receiptLoop.getValue().get(0).get("address"));

				List<Map<String, Object>> receiptSubList = new ArrayList<>();
				for (Map<String, Object> receiptSubLoop : receiptLoop.getValue()) {

					Map<String, Object> receiptSubMap = new HashMap<>();
					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(receiptSubLoop);
					String imageUrl = "company/" + randomNumber + "/" + receiptSubLoop.get("company_id") + "." + fileExtension;
					String imageUrl1 = "signature/" + randomNumber + "/" + receiptSubLoop.get("company_id") + "." + fileExtension;
					receiptSubMap.put("invoiceId", receiptSubLoop.get("invoice_id"));
					receiptSubMap.put("receiptId", receiptSubLoop.get("receipt_id"));
					receiptSubMap.put("balance", receiptSubLoop.get("balance"));
					receiptSubMap.put("paymentDate", receiptSubLoop.get("payment_date"));
					receiptSubMap.put("profile", imageUrl);
					receiptSubMap.put("signature", imageUrl1);
					receiptSubMap.put("paymentType", receiptSubLoop.get("payment_type"));
					receiptSubMap.put("receivedAmount", receiptSubLoop.get("received_amount"));
					receiptSubMap.put("clientId", receiptSubLoop.get("client_id"));
					receiptSubMap.put("companyId", receiptSubLoop.get("company_id"));
					receiptSubMap.put("address", receiptSubLoop.get("address"));
					receiptSubMap.put("city", receiptSubLoop.get("city"));
					receiptSubMap.put("clientName", receiptSubLoop.get("client_name"));
					receiptSubMap.put("country", receiptSubLoop.get("country"));
					receiptSubMap.put("email", receiptSubLoop.get("email"));
					receiptSubMap.put("gender", receiptSubLoop.get("gender"));
					receiptSubMap.put("phoneNumber", receiptSubLoop.get("phone_number"));
					receiptSubMap.put("mobileNumber", receiptSubLoop.get("mobile_number"));
					receiptSubMap.put("state", receiptSubLoop.get("state"));
					receiptSubMap.put("zipCode", receiptSubLoop.get("zip_code"));
					receiptSubMap.put("companyName", receiptSubLoop.get("company_name"));
					receiptSubMap.put("companyId", receiptSubLoop.get("company_id"));
					receiptSubMap.put("companyEmail", receiptSubLoop.get("companyEmail"));
					receiptSubMap.put("companyPhoneNumber1", receiptSubLoop.get("phone_number1"));
					receiptSubMap.put("companyPhoneNumber2", receiptSubLoop.get("phone_number2"));
					receiptSubMap.put("companyZipcode", receiptSubLoop.get("pincode"));
					receiptSubMap.put("companyCountry", receiptSubLoop.get("companyCountry"));
					receiptSubMap.put("companyAddress", receiptSubLoop.get("companyAddress"));
					receiptSubMap.put("amount", receiptSubLoop.get("amount"));
					receiptSubMap.put("companyState", receiptSubLoop.get("companyState"));
					receiptSubMap.put("companyLocation", receiptSubLoop.get("companyLocation"));

					receiptSubList.add(receiptSubMap);
				}
				receiptMap.put("clientDetails", receiptSubList);
//				receiptList.add(receiptMap);
				return ResponseEntity.ok(receiptMap);
			}

			return ResponseEntity.ok(receiptList);
		
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
		} else {
			return "jpg";
		}
	}
	@GetMapping("/receipt/invoice/view/{id}")
	public ResponseEntity<Object> getAllReceiptDetailsWithClientIdAndName(@PathVariable("id") Long client_id) {
			List<Map<String, Object>> receiptList = new ArrayList<>();
			List<Map<String, Object>> receiptRole = receiptRepository.getAllReceiptDetailsWithClientIdAndName(client_id);
			Map<String, List<Map<String, Object>>> departmentGroupMap = receiptRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("client_id").toString()));

			for (Entry<String, List<Map<String, Object>>> receiptLoop : departmentGroupMap.entrySet()) {
				Map<String, Object> receiptMap = new HashMap<>();
				receiptMap.put("clientId", Long.parseLong(receiptLoop.getKey()));
				receiptMap.put("clientName", receiptLoop.getValue().get(0).get("client_name"));
				receiptMap.put("phoneNumber", receiptLoop.getValue().get(0).get("phone_number"));
				receiptMap.put("mobileNumber", receiptLoop.getValue().get(0).get("mobile_number"));
				receiptMap.put("address", receiptLoop.getValue().get(0).get("address"));

				List<Map<String, Object>> receiptSubList = new ArrayList<>();
				for (Map<String, Object> receiptSubLoop : receiptLoop.getValue()) {

					Map<String, Object> receiptSubMap = new HashMap<>();
					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(receiptSubLoop);
					String imageUrl = "company/" + randomNumber + "/" + receiptSubLoop.get("company_id") + "." + fileExtension;
					receiptSubMap.put("invoiceId", receiptSubLoop.get("invoice_id"));
					receiptSubMap.put("receiptId", receiptSubLoop.get("receipt_id"));
					receiptSubMap.put("balance", receiptSubLoop.get("balance"));
					receiptSubMap.put("paymentDate", receiptSubLoop.get("payment_date"));
					receiptSubMap.put("profile", imageUrl);
					receiptSubMap.put("paymentType", receiptSubLoop.get("payment_type"));
					receiptSubMap.put("receivedAmount", receiptSubLoop.get("received_amount"));
					receiptSubMap.put("clientId", receiptSubLoop.get("client_id"));
					receiptSubMap.put("companyId", receiptSubLoop.get("company_id"));
					receiptSubMap.put("address", receiptSubLoop.get("address"));
					receiptSubMap.put("city", receiptSubLoop.get("city"));
					receiptSubMap.put("clientName", receiptSubLoop.get("client_name"));
					receiptSubMap.put("country", receiptSubLoop.get("country"));
					receiptSubMap.put("email", receiptSubLoop.get("email"));
					receiptSubMap.put("gender", receiptSubLoop.get("gender"));
					receiptSubMap.put("phoneNumber", receiptSubLoop.get("phone_number"));
					receiptSubMap.put("mobileNumber", receiptSubLoop.get("mobile_number"));
					receiptSubMap.put("state", receiptSubLoop.get("state"));
					receiptSubMap.put("zipCode", receiptSubLoop.get("zip_code"));
					receiptSubMap.put("companyName", receiptSubLoop.get("company_name"));
					receiptSubMap.put("companyId", receiptSubLoop.get("company_id"));
					receiptSubMap.put("companyEmail", receiptSubLoop.get("companyEmail"));
					receiptSubMap.put("companyPhoneNumber1", receiptSubLoop.get("phone_number1"));
					receiptSubMap.put("companyPhoneNumber2", receiptSubLoop.get("phone_number2"));
					receiptSubMap.put("companyZipcode", receiptSubLoop.get("pincode"));
					receiptSubMap.put("companyCountry", receiptSubLoop.get("companyCountry"));
					receiptSubMap.put("companyAddress", receiptSubLoop.get("companyAddress"));
					receiptSubMap.put("amount", receiptSubLoop.get("amount"));
					receiptSubMap.put("companyState", receiptSubLoop.get("companyState"));
					receiptSubMap.put("companyLocation", receiptSubLoop.get("companyLocation"));

					receiptSubList.add(receiptSubMap);
				}
				receiptMap.put("clientDetails", receiptSubList);
				receiptList.add(receiptMap);
			}

			return ResponseEntity.ok(receiptList);
		
	}
	
	
	
	@GetMapping("/receipt/report")
	public List<Map<String, Object>> allDeptDetails(@RequestParam(required = true) String report) {
	    try {
	        if ("receipt".equals(report)) {
	            return receiptRepository.getAllReceiptDetailsWithClimap();
	        } else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();
	    }
	}
	

	@GetMapping("/receipt/dashboard/view")
	public ResponseEntity<?> getTaskAssignedDetailsp(@RequestParam(required = true) String dashboard) {
		try {
			if ("receipt".equals(dashboard)) {
				List<Map<String, Object>> tasks = receiptRepository.getAllReceiptDetailsWithClientIdAndNamerrr();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();

					int randomNumber = generateRandomNumber();
				
					String imageUrl = "clientProfile/" + randomNumber + "/" + taskAssigned.get("client_id") ;
					taskAssignedMap.put("clientProfile", imageUrl);
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
	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000); // Change the upper limit if needed
	}
	
	@PostMapping("/receipt/date")
	public List<Map<String, Object>> allExpenseDetailsByDate(@RequestBody Map<String, Object> requestBody) {
	    LocalDate startDate = LocalDate.parse(requestBody.get("startDate").toString(), DateTimeFormatter.ISO_DATE);
	    LocalDate endDate = LocalDate.parse(requestBody.get("endDate").toString(), DateTimeFormatter.ISO_DATE);
	    return receiptRepository.getAllpromotionsBetweenDates(startDate, endDate);
	}
	
	
	

	@PostMapping("/receipt/client")
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
	                List<Map<String, Object>> leaveData = receiptRepository.getAllReceiptDetailsWithClientIdAndyear(year,clientId);
	                return ResponseEntity.ok(leaveData);
	            }
	            break;
	        case "month":
	            if (requestBody.containsKey("monthName")) {
	                String monthName = requestBody.get("monthName").toString();
	                List<Map<String, Object>> leaveData1 = receiptRepository.getAllReceiptDetailsWithClientIdAndMonthyear(monthName,clientId);
	                return ResponseEntity.ok(leaveData1);
	            }
	            break;
	        default:
	            return ResponseEntity.badRequest().build();
	    }
	    return ResponseEntity.badRequest().build();
	}

	 @PostMapping("/receipt/manager")
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
	                    List<Map<String, Object>> leaveData = receiptRepository.getAllReceiptDetailsWithClientIdAnd(year);
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
	                    List<Map<String, Object>> leaveData1 = receiptRepository.getAllReceiptDetailsWithClientIdAndMonth(monthName);
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
