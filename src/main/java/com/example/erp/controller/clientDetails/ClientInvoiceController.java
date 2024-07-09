package com.example.erp.controller.clientDetails;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Optional;
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

import com.example.erp.entity.accounting.MaintenancePayment;
import com.example.erp.entity.clientDetails.ClientInvoice;
import com.example.erp.entity.clientDetails.ClientInvoiceList;
import com.example.erp.entity.clientDetails.ClientRequirement;
import com.example.erp.entity.clientDetails.Quotation;
import com.example.erp.entity.clientDetails.Receipts;
import com.example.erp.entity.organization.Company;
import com.example.erp.repository.clientDetails.ClientInvoiceListRepository;
import com.example.erp.repository.clientDetails.ClientInvoiceRepository;
import com.example.erp.repository.clientDetails.ReceiptRepository;
import com.example.erp.service.clientDetails.ClientInvoiceService;
import com.example.erp.service.clientDetails.ClientRequirementService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin
public class ClientInvoiceController {

	@Autowired
	private ClientInvoiceService clientInvoiceService;

	@Autowired
	private ClientInvoiceRepository clientInvoiceRepository;

	@Autowired
	private ClientInvoiceListRepository clientInvoiceListRepository;
	
	@Autowired
	private ClientRequirementService clientRequirementService;
	
	@Autowired
	private ReceiptRepository receiptRepository;


//	@PostMapping("/invoice/save")
//	private ResponseEntity<?> saveInvoice(@RequestBody ClientInvoice clientInvoice) {
//	    try {
//	        List<ClientInvoiceList> clientInvoiceList = clientInvoice.getInvoiceList();
//
//	        if (clientInvoiceList.isEmpty()) {
//	            throw new RuntimeException("Invoice list is empty. Unable to proceed.");
//	        }
//
//	        for (ClientInvoiceList clientInvoiceLoop : clientInvoiceList) {
//	            long projectId = clientInvoiceLoop.getProjectId();
//
//	            Optional<ClientInvoiceList> existingInvoice = clientInvoiceListRepository.findByProjectId(projectId);
//double amo=clientInvoiceLoop.getPrice();
//clientInvoice.setAmount(amo);
//	            if (existingInvoice.isPresent()) {
//	                return ResponseEntity.badRequest().body("Project with ID " + projectId + " already exists.");
//	            }
//	        }
//	      
//	        double balanceAmount = clientInvoice.getBalanceAmount();
//	        double received = clientInvoice.getReceived();
//	        double balance = clientInvoice.getBalanceAmount();
//	        clientInvoice.setBalanceAmount(balance);
//	        clientInvoice.setTotalList(received);
//	        clientInvoice.setBalance(balance);
//	        clientInvoice.setStatus(true);
//	        clientInvoiceService.saveInvoice(clientInvoice);
//	        long invoiceId = clientInvoice.getInvoiceId();
//	        Date date = clientInvoice.getInvoiceDate();
//	        String paymentType = clientInvoice.getPaymentType();
//	        Receipts receipts = new Receipts();
//	        receipts.setInvoiceId(invoiceId);
//	        receipts.setBalance(balanceAmount);
//	        receipts.setReceivedAmount(received);
//	        receipts.setTotalList(received);
//	        receipts.setPaymentDate(date);
//	        receipts.setPaymentType(paymentType);
//	        receiptRepository.save(receipts);
//	        
//	        
//	        
//	        return ResponseEntity.ok(clientInvoice);
//	    } catch (Exception e) {
//	        String errorMessage = "An error occurred while saving invoice details.";
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
//	    }
//	}

		
	
	 @PostMapping("/invoice/save")
	    private ResponseEntity<?> saveInvoice22222(@RequestBody ClientInvoice clientInvoice) {
	        try {
	            List<ClientInvoiceList> clientInvoiceList = clientInvoice.getInvoiceList();

	            if (clientInvoiceList.isEmpty()) {
	                throw new RuntimeException("Invoice list is empty. Unable to proceed.");
	            }

	            for (ClientInvoiceList clientInvoiceLoop : clientInvoiceList) {
	                long projectId = clientInvoiceLoop.getProjectId();

	                Optional<ClientInvoiceList> existingInvoice = clientInvoiceListRepository.findByProjectId(projectId);

	                if (existingInvoice.isPresent()) {
	                    return ResponseEntity.badRequest().body("Project with ID " + projectId + " already exists.");
	                }
	            }

	            double balanceAmount = clientInvoice.getBalanceAmount();
	            double received = clientInvoice.getReceived();
	            double balance = clientInvoice.getBalanceAmount();
	            clientInvoice.setBalanceAmount(balance);
	            clientInvoice.setTotalList(received);
	            clientInvoice.setBalance(balance);
	            clientInvoice.setStatus(true);
	            clientInvoiceService.saveInvoice(clientInvoice);
	            long invoiceId = clientInvoice.getInvoiceId();
	            Date date = clientInvoice.getInvoiceDate();
	            String paymentType = clientInvoice.getPaymentType();
	            Receipts receipts = new Receipts();
	            receipts.setInvoiceId(invoiceId);
	            receipts.setBalance(balanceAmount);
	            receipts.setReceivedAmount(received);
	            receipts.setTotalList(received);
	            receipts.setPaymentDate(date);
	            receipts.setPaymentType(paymentType);
	            receiptRepository.save(receipts);

	            List<Map<String, Object>> invoiceRole = clientInvoiceRepository.getAllInvoiceDetailsList();

	            Map<String, Map<String, List<Map<String, Object>>>> clientGroupMap = invoiceRole.stream()
	                    .collect(Collectors.groupingBy(action -> action.get("invoice_id").toString(),
	                            Collectors.groupingBy(action -> action.get("invoice_list_id").toString())));
	            Map<String, Object> invoiceMap = new HashMap<>();
	            List<Map<String, Object>> invoiceList = new ArrayList<>();
	            for (Entry<String, Map<String, List<Map<String, Object>>>> invoiceLoop : clientGroupMap.entrySet()) {
	              
	                invoiceMap.put("invoiceId", invoiceLoop.getKey());

	                List<Map<String, Object>> invoiceSubList = new ArrayList<>();
	                for (Entry<String, List<Map<String, Object>>> invoiceSubLoop : invoiceLoop.getValue().entrySet()) {
	                    Map<String, Object> invoiceSubMap = new HashMap<>();
	                    invoiceSubMap.put("invoiceListId", invoiceSubLoop.getKey());
	                    invoiceMap.put("amount", invoiceSubLoop.getValue().get(0).get("listAmount"));
	                    invoiceMap.put("balance", invoiceSubLoop.getValue().get(0).get("balance"));
	                    invoiceMap.put("balanceAmount", invoiceSubLoop.getValue().get(0).get("balance_amount"));
	                    invoiceMap.put("clientId", invoiceSubLoop.getValue().get(0).get("client_id"));
	                    invoiceMap.put("CompanyId", invoiceSubLoop.getValue().get(0).get("company_id"));
	                    invoiceMap.put("description", invoiceSubLoop.getValue().get(0).get("description"));
	                    invoiceMap.put("gstType", invoiceSubLoop.getValue().get(0).get("gst_type"));
	                    invoiceMap.put("invoiceStatus", invoiceSubLoop.getValue().get(0).get("invoiceStatus"));
	                    invoiceMap.put("invoiceDate", invoiceSubLoop.getValue().get(0).get("invoice_date"));
	                    invoiceMap.put("paymentType", invoiceSubLoop.getValue().get(0).get("payment_type"));
	                    invoiceMap.put("received", invoiceSubLoop.getValue().get(0).get("received"));
	                    invoiceMap.put("roundOffAmount", invoiceSubLoop.getValue().get(0).get("round_off_amount"));
	                    if ("withTax".equals(invoiceSubLoop.getValue().get(0).get("gst_type"))) {
	                        double taxAmount = (double) invoiceSubLoop.getValue().get(0).get("tax_amount");
	                        invoiceMap.put("cgst", taxAmount / 2);
	                        invoiceMap.put("sgst", taxAmount / 2);
	                    } else if ("withoutTax".equals(invoiceSubLoop.getValue().get(0).get("gst_type"))) {
	                        double taxAmount = (double) invoiceSubLoop.getValue().get(0).get("tax_amount");
	                        invoiceMap.put("igst", taxAmount);
	                    }

	                    invoiceMap.put("taxAmount", invoiceSubLoop.getValue().get(0).get("tax_amount"));
	                    invoiceMap.put("companyName", invoiceSubLoop.getValue().get(0).get("company_name"));
	                    invoiceMap.put("accountNo", invoiceSubLoop.getValue().get(0).get("account_no"));
	                    invoiceMap.put("companyAddress", invoiceSubLoop.getValue().get(0).get("companyAddress"));
	                    invoiceMap.put("bankName", invoiceSubLoop.getValue().get(0).get("bank_name"));
	                    invoiceMap.put("companyEmail", invoiceSubLoop.getValue().get(0).get("companyEmail"));
	                    invoiceMap.put("holderName", invoiceSubLoop.getValue().get(0).get("holder_name"));
	                    invoiceMap.put("ifscCode", invoiceSubLoop.getValue().get(0).get("ifsc_code"));
	                    invoiceMap.put("location", invoiceSubLoop.getValue().get(0).get("location"));
	                    invoiceMap.put("phoneNumber1", invoiceSubLoop.getValue().get(0).get("phone_number1"));
	                    invoiceMap.put("phoneNumber2", invoiceSubLoop.getValue().get(0).get("phone_number2"));
	                    invoiceMap.put("pincode", invoiceSubLoop.getValue().get(0).get("pincode"));
	                    invoiceMap.put("taxNo", invoiceSubLoop.getValue().get(0).get("tax_no"));
	                    invoiceMap.put("country", invoiceSubLoop.getValue().get(0).get("country"));
	                    invoiceMap.put("gstNo", invoiceSubLoop.getValue().get(0).get("gst_no"));
	                    invoiceMap.put("clientName", invoiceSubLoop.getValue().get(0).get("client_name"));
	                    invoiceMap.put("clientEmail", invoiceSubLoop.getValue().get(0).get("email"));
	                    invoiceMap.put("clientGender", invoiceSubLoop.getValue().get(0).get("gender"));
						invoiceMap.put("clientPhoneNumber", invoiceSubLoop.getValue().get(0).get("phone_number"));
						invoiceMap.put("clientState", invoiceSubLoop.getValue().get(0).get("state"));
						invoiceMap.put("clientAddress", invoiceSubLoop.getValue().get(0).get("address"));
						invoiceMap.put("projectId", invoiceSubLoop.getValue().get(0).get("project_id"));
						invoiceMap.put("projectName", invoiceSubLoop.getValue().get(0).get("project_name"));
						invoiceMap.put("clientZipcode", invoiceSubLoop.getValue().get(0).get("zip_code"));

						invoiceSubMap.put("invoiceId", invoiceLoop.getKey());
						invoiceSubMap.put("totalTaxAmount", invoiceSubLoop.getValue().get(0).get("total_tax_amount"));
						invoiceSubMap.put("taxQuantityAmount", invoiceSubLoop.getValue().get(0).get("tax_quantity_amount"));
						invoiceSubMap.put("taxIncludePrice", invoiceSubLoop.getValue().get(0).get("tax_include_price"));
						invoiceSubMap.put("quantity", invoiceSubLoop.getValue().get(0).get("quantity"));
						invoiceSubMap.put("projectId", invoiceSubLoop.getValue().get(0).get("project_id"));
						invoiceSubMap.put("price", invoiceSubLoop.getValue().get(0).get("price"));
						invoiceSubMap.put("gst", invoiceSubLoop.getValue().get(0).get("gst"));
						invoiceSubMap.put("discountPercentage",invoiceSubLoop.getValue().get(0).get("discount_percentage"));
						invoiceSubMap.put("discountAmount", invoiceSubLoop.getValue().get(0).get("discount_amount"));
						invoiceSubMap.put("amount", invoiceSubLoop.getValue().get(0).get("listAmount"));
						invoiceSubMap.put("projectName", invoiceSubLoop.getValue().get(0).get("project_name"));

						invoiceSubList.add(invoiceSubMap);
					}

                invoiceMap.put("invoiceList", invoiceSubList);   
             
	         
            }
	            return ResponseEntity.ok(invoiceMap);

        } catch (Exception e) {
            String errorMessage = "An error occurred while saving invoice details.";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

	

	@PutMapping("/invoice/edit/{id}")
	public ResponseEntity<?> updateInvoice(@PathVariable("id") Long invoiceId, @RequestBody ClientInvoice clientInvoice) {
	    try {
	        System.out.println("Received update request for invoiceId: " + invoiceId);

	        ClientInvoice existingInvoice = clientInvoiceService.findInvoiceById1(invoiceId);
	        if (existingInvoice == null) {
	            System.out.println("No existing invoice found for invoiceId: " + invoiceId);
	            return ResponseEntity.notFound().build();
	        }
	        
	        double receivedAmountFromReceipts = existingInvoice.getTotalList();
	        
	        List<ClientInvoiceList> clientInvoiceList = clientInvoice.getInvoiceList();

	        if (clientInvoiceList.isEmpty()) {
	            throw new RuntimeException("Invoice list is empty. Unable to proceed.");
	        }
	        for (ClientInvoiceList clientInvoiceLoop : clientInvoiceList) {
	        	
		        double newAmount = clientInvoiceLoop.getAmount();
		        double amo=clientInvoiceLoop.getPrice();
		        clientInvoice.setAmount(amo);
		        System.out.println("newAmount: " + newAmount);
		        System.out.println("receivedAmountFromReceipts: " + receivedAmountFromReceipts);

		        if (newAmount < receivedAmountFromReceipts) {
		            System.out.println("Received amount or Total exceeds the allowed threshold.");
		            return ResponseEntity.badRequest().body("Received amount or Total exceeds the allowed threshold.");
		        }
	        }

	        Receipts receipts = new Receipts();
	      
//	        existingInvoice.setAmount(clientInvoice.getAmount());
	        existingInvoice.setBalanceAmount(clientInvoice.getBalanceAmount());
	        existingInvoice.setClientId(clientInvoice.getClientId());
	        existingInvoice.setCompanyId(clientInvoice.getCompanyId());
	        existingInvoice.setDescription(clientInvoice.getDescription());
	        existingInvoice.setStatus(clientInvoice.isStatus());
	        existingInvoice.setGstType(clientInvoice.getGstType());
	        existingInvoice.setInvoiceDate(clientInvoice.getInvoiceDate());
	        existingInvoice.setInvoiceList(clientInvoice.getInvoiceList());
	        existingInvoice.setPaymentType(clientInvoice.getPaymentType());
	        existingInvoice.setReceived(clientInvoice.getReceived());
	        existingInvoice.setTaxAmount(clientInvoice.getTaxAmount());
	        existingInvoice.setRoundOffAmount(clientInvoice.getRoundOffAmount());
	        existingInvoice.setPaymentType(clientInvoice.getPaymentType());

	        clientInvoiceService.saveInvoice(existingInvoice);
	        
	        
	        receipts.setBalance(clientInvoice.getBalanceAmount());
	        receipts.setReceivedAmount(clientInvoice.getReceived());
	        receipts.setTotalList(clientInvoice.getReceived());
	        receipts.setPaymentDate(clientInvoice.getInvoiceDate());
	        receipts.setPaymentType(clientInvoice.getPaymentType());
	        receiptRepository.save(receipts);

	        return ResponseEntity.ok(existingInvoice);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	



	@DeleteMapping("/invoice/delete/{id}")
	public ResponseEntity<String> deleteInvoice(@PathVariable("id") Long invoiceId) {
		clientInvoiceService.deleteInvoiceById(invoiceId);
		return ResponseEntity.ok("Invoice deleted successfully");
	}

	@GetMapping("/invoice")
	public ResponseEntity<?> getAllQuotationByClientDetails(@RequestParam(required = true) String invoice) {
		if ("invoices".equals(invoice)) {
			List<Map<String, Object>> invoiceRole = clientInvoiceRepository.getAllInvoiceDetailsList();

			Map<String, Map<String, List<Map<String, Object>>>> clientGroupMap = invoiceRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("invoice_id").toString(),
							Collectors.groupingBy(action -> action.get("invoice_list_id").toString())));

			List<Map<String, Object>> invoiceList = new ArrayList<>();
			for (Entry<String, Map<String, List<Map<String, Object>>>> invoiceLoop : clientGroupMap.entrySet()) {
				Map<String, Object> invoiceMap = new HashMap<>();
				invoiceMap.put("invoiceId", invoiceLoop.getKey());

				List<Map<String, Object>> invoiceSubList = new ArrayList<>();
				for (Entry<String, List<Map<String, Object>>> invoiceSubLoop : invoiceLoop.getValue().entrySet()) {
					Map<String, Object> invoiceSubMap = new HashMap<>();
					invoiceSubMap.put("invoiceListId", invoiceSubLoop.getKey());
					invoiceMap.put("amount", invoiceSubLoop.getValue().get(0).get("listAmount"));
					invoiceMap.put("balance", invoiceSubLoop.getValue().get(0).get("balance"));
					invoiceMap.put("balanceAmount", invoiceSubLoop.getValue().get(0).get("balance_amount"));
					invoiceMap.put("clientId", invoiceSubLoop.getValue().get(0).get("client_id"));
					invoiceMap.put("CompanyId", invoiceSubLoop.getValue().get(0).get("company_id"));
					invoiceMap.put("description", invoiceSubLoop.getValue().get(0).get("description"));
					invoiceMap.put("gstType", invoiceSubLoop.getValue().get(0).get("gst_type"));
					invoiceMap.put("invoiceStatus", invoiceSubLoop.getValue().get(0).get("invoiceStatus"));
					invoiceMap.put("invoiceDate", invoiceSubLoop.getValue().get(0).get("invoice_date"));
					invoiceMap.put("paymentType", invoiceSubLoop.getValue().get(0).get("payment_type"));
					invoiceMap.put("received", invoiceSubLoop.getValue().get(0).get("received"));
					invoiceMap.put("roundOffAmount", invoiceSubLoop.getValue().get(0).get("round_off_amount"));
					if ("withTax".equals(invoiceSubLoop.getValue().get(0).get("gst_type"))) {
						double taxAmount = (double) invoiceSubLoop.getValue().get(0).get("tax_amount");
						invoiceMap.put("cgst", taxAmount / 2);
						invoiceMap.put("sgst", taxAmount / 2);
					} else if ("withoutTax".equals(invoiceSubLoop.getValue().get(0).get("gst_type"))) {
						double taxAmount = (double) invoiceSubLoop.getValue().get(0).get("tax_amount");
						invoiceMap.put("igst", taxAmount);
					}

					invoiceMap.put("taxAmount", invoiceSubLoop.getValue().get(0).get("tax_amount"));
					invoiceMap.put("companyName", invoiceSubLoop.getValue().get(0).get("company_name"));
					invoiceMap.put("accountNo", invoiceSubLoop.getValue().get(0).get("account_no"));
					invoiceMap.put("companyAddress", invoiceSubLoop.getValue().get(0).get("companyAddress"));
					invoiceMap.put("bankName", invoiceSubLoop.getValue().get(0).get("bank_name"));
					invoiceMap.put("companyEmail", invoiceSubLoop.getValue().get(0).get("companyEmail"));
					invoiceMap.put("holderName", invoiceSubLoop.getValue().get(0).get("holder_name"));
					invoiceMap.put("ifscCode", invoiceSubLoop.getValue().get(0).get("ifsc_code"));
					invoiceMap.put("location", invoiceSubLoop.getValue().get(0).get("location"));
					invoiceMap.put("phoneNumber1", invoiceSubLoop.getValue().get(0).get("phone_number1"));
					invoiceMap.put("phoneNumber2", invoiceSubLoop.getValue().get(0).get("phone_number2"));
					invoiceMap.put("pincode", invoiceSubLoop.getValue().get(0).get("pincode"));
					invoiceMap.put("taxNo", invoiceSubLoop.getValue().get(0).get("tax_no"));
					invoiceMap.put("country", invoiceSubLoop.getValue().get(0).get("country"));
					invoiceMap.put("gstNo", invoiceSubLoop.getValue().get(0).get("gst_no"));
					invoiceMap.put("clientName", invoiceSubLoop.getValue().get(0).get("client_name"));
					invoiceMap.put("clientEmail", invoiceSubLoop.getValue().get(0).get("email"));
					invoiceMap.put("clientGender", invoiceSubLoop.getValue().get(0).get("gender"));
					invoiceMap.put("clientPhoneNumber", invoiceSubLoop.getValue().get(0).get("phone_number"));
					invoiceMap.put("clientState", invoiceSubLoop.getValue().get(0).get("state"));
					invoiceMap.put("clientAddress", invoiceSubLoop.getValue().get(0).get("address"));
					invoiceMap.put("projectId", invoiceSubLoop.getValue().get(0).get("project_id"));
					invoiceMap.put("projectName", invoiceSubLoop.getValue().get(0).get("project_name"));
					invoiceMap.put("clientZipcode", invoiceSubLoop.getValue().get(0).get("zip_code"));

					invoiceSubMap.put("invoiceId", invoiceLoop.getKey());
					invoiceSubMap.put("totalTaxAmount", invoiceSubLoop.getValue().get(0).get("total_tax_amount"));
					invoiceSubMap.put("taxQuantityAmount", invoiceSubLoop.getValue().get(0).get("tax_quantity_amount"));
					invoiceSubMap.put("taxIncludePrice", invoiceSubLoop.getValue().get(0).get("tax_include_price"));
					invoiceSubMap.put("quantity", invoiceSubLoop.getValue().get(0).get("quantity"));
					invoiceSubMap.put("projectId", invoiceSubLoop.getValue().get(0).get("project_id"));
					invoiceSubMap.put("price", invoiceSubLoop.getValue().get(0).get("price"));
					invoiceSubMap.put("gst", invoiceSubLoop.getValue().get(0).get("gst"));
					invoiceSubMap.put("discountPercentage",invoiceSubLoop.getValue().get(0).get("discount_percentage"));
					invoiceSubMap.put("discountAmount", invoiceSubLoop.getValue().get(0).get("discount_amount"));
					invoiceSubMap.put("amount", invoiceSubLoop.getValue().get(0).get("listAmount"));
					invoiceSubMap.put("projectName", invoiceSubLoop.getValue().get(0).get("project_name"));

					invoiceSubList.add(invoiceSubMap);
				}

				invoiceMap.put("invoiceList", invoiceSubList);
				invoiceList.add(invoiceMap);
			}

			return ResponseEntity.ok(invoiceList);
		}
		String errorMessage = "Invalid value for 'invoice'. Expected 'invoices'.";
		return ResponseEntity.badRequest().body(errorMessage);
	}

	private String getString(Map<String, Object> action, String key) {
	    Object value = action.get(key);
	    return value != null ? value.toString() : "null";
	}
	
	@PostMapping("/invoice/date")
	public ResponseEntity<?> getAllQuotationByClientDetails1(@RequestBody Map<String, Object> requestBody) {
	    if (requestBody.containsKey("startDate") && requestBody.containsKey("endDate")) {
	        try {
	            LocalDate startDate = LocalDate.parse(requestBody.get("startDate").toString(), DateTimeFormatter.ISO_DATE);
	            LocalDate endDate = LocalDate.parse(requestBody.get("endDate").toString(), DateTimeFormatter.ISO_DATE);
		
			List<Map<String, Object>> invoiceRole = clientInvoiceRepository.getAllInvoiceDetailsListWithDate(startDate, endDate);

			Map<String, Map<String, List<Map<String, Object>>>> clientGroupMap = invoiceRole.stream()
			        .collect(Collectors.groupingBy( action -> getString(action, "invoice_id"),
			                Collectors.groupingBy(action -> getString(action, "invoice_list_id"))));
			List<Map<String, Object>> invoiceList = new ArrayList<>();
			for (Entry<String, Map<String, List<Map<String, Object>>>> invoiceLoop : clientGroupMap.entrySet()) {
				Map<String, Object> invoiceMap = new HashMap<>();
				invoiceMap.put("invoiceId", invoiceLoop.getKey());

				List<Map<String, Object>> invoiceSubList = new ArrayList<>();
				for (Entry<String, List<Map<String, Object>>> invoiceSubLoop : invoiceLoop.getValue().entrySet()) {
					Map<String, Object> invoiceSubMap = new HashMap<>();
					invoiceSubMap.put("invoiceListId", invoiceSubLoop.getKey());
					invoiceMap.put("amount", invoiceSubLoop.getValue().get(0).get("listAmount"));
					invoiceMap.put("balance", invoiceSubLoop.getValue().get(0).get("balance"));
					invoiceMap.put("balanceAmount", invoiceSubLoop.getValue().get(0).get("balance_amount"));
					invoiceMap.put("clientId", invoiceSubLoop.getValue().get(0).get("client_id"));
					invoiceMap.put("CompanyId", invoiceSubLoop.getValue().get(0).get("company_id"));
					invoiceMap.put("description", invoiceSubLoop.getValue().get(0).get("description"));
					invoiceMap.put("gstType", invoiceSubLoop.getValue().get(0).get("gst_type"));
					invoiceMap.put("invoiceStatus", invoiceSubLoop.getValue().get(0).get("invoiceStatus"));
					invoiceMap.put("invoiceDate", invoiceSubLoop.getValue().get(0).get("invoice_date"));
					invoiceMap.put("paymentType", invoiceSubLoop.getValue().get(0).get("payment_type"));
					invoiceMap.put("received", invoiceSubLoop.getValue().get(0).get("received"));
					invoiceMap.put("roundOffAmount", invoiceSubLoop.getValue().get(0).get("round_off_amount"));
					if ("withTax".equals(invoiceSubLoop.getValue().get(0).get("gst_type"))) {
						double taxAmount = (double) invoiceSubLoop.getValue().get(0).get("tax_amount");
						invoiceMap.put("cgst", taxAmount / 2);
						invoiceMap.put("sgst", taxAmount / 2);
					} else if ("withoutTax".equals(invoiceSubLoop.getValue().get(0).get("gst_type"))) {
						double taxAmount = (double) invoiceSubLoop.getValue().get(0).get("tax_amount");
						invoiceMap.put("igst", taxAmount);
					}

					invoiceMap.put("taxAmount", invoiceSubLoop.getValue().get(0).get("tax_amount"));
					invoiceMap.put("companyName", invoiceSubLoop.getValue().get(0).get("company_name"));
					invoiceMap.put("accountNo", invoiceSubLoop.getValue().get(0).get("account_no"));
					invoiceMap.put("companyAddress", invoiceSubLoop.getValue().get(0).get("companyAddress"));
					invoiceMap.put("bankName", invoiceSubLoop.getValue().get(0).get("bank_name"));
					invoiceMap.put("companyEmail", invoiceSubLoop.getValue().get(0).get("companyEmail"));
					invoiceMap.put("holderName", invoiceSubLoop.getValue().get(0).get("holder_name"));
					invoiceMap.put("ifscCode", invoiceSubLoop.getValue().get(0).get("ifsc_code"));
					invoiceMap.put("location", invoiceSubLoop.getValue().get(0).get("location"));
					invoiceMap.put("phoneNumber1", invoiceSubLoop.getValue().get(0).get("phone_number1"));
					invoiceMap.put("phoneNumber2", invoiceSubLoop.getValue().get(0).get("phone_number2"));
					invoiceMap.put("pincode", invoiceSubLoop.getValue().get(0).get("pincode"));
					invoiceMap.put("taxNo", invoiceSubLoop.getValue().get(0).get("tax_no"));
					invoiceMap.put("country", invoiceSubLoop.getValue().get(0).get("country"));
					invoiceMap.put("gstNo", invoiceSubLoop.getValue().get(0).get("gst_no"));
					invoiceMap.put("clientName", invoiceSubLoop.getValue().get(0).get("client_name"));
					invoiceMap.put("clientEmail", invoiceSubLoop.getValue().get(0).get("email"));
					invoiceMap.put("clientGender", invoiceSubLoop.getValue().get(0).get("gender"));
					invoiceMap.put("clientPhoneNumber", invoiceSubLoop.getValue().get(0).get("phone_number"));
					invoiceMap.put("clientState", invoiceSubLoop.getValue().get(0).get("state"));
					invoiceMap.put("clientAddress", invoiceSubLoop.getValue().get(0).get("address"));
					invoiceMap.put("projectId", invoiceSubLoop.getValue().get(0).get("project_id"));
					invoiceMap.put("projectName", invoiceSubLoop.getValue().get(0).get("project_name"));
					invoiceMap.put("clientZipcode", invoiceSubLoop.getValue().get(0).get("zip_code"));

					invoiceSubMap.put("invoiceId", invoiceLoop.getKey());
					invoiceSubMap.put("totalTaxAmount", invoiceSubLoop.getValue().get(0).get("total_tax_amount"));
					invoiceSubMap.put("taxQuantityAmount", invoiceSubLoop.getValue().get(0).get("tax_quantity_amount"));
					invoiceSubMap.put("taxIncludePrice", invoiceSubLoop.getValue().get(0).get("tax_include_price"));
					invoiceSubMap.put("quantity", invoiceSubLoop.getValue().get(0).get("quantity"));
					invoiceSubMap.put("projectId", invoiceSubLoop.getValue().get(0).get("project_id"));
					invoiceSubMap.put("price", invoiceSubLoop.getValue().get(0).get("price"));
					invoiceSubMap.put("gst", invoiceSubLoop.getValue().get(0).get("gst"));
					invoiceSubMap.put("discountPercentage",invoiceSubLoop.getValue().get(0).get("discount_percentage"));
					invoiceSubMap.put("discountAmount", invoiceSubLoop.getValue().get(0).get("discount_amount"));
					invoiceSubMap.put("amount", invoiceSubLoop.getValue().get(0).get("listAmount"));
					invoiceSubMap.put("projectName", invoiceSubLoop.getValue().get(0).get("project_name"));

					invoiceSubList.add(invoiceSubMap);
				}

				invoiceMap.put("invoiceList", invoiceSubList);
				invoiceList.add(invoiceMap);
			}

			    return ResponseEntity.ok(invoiceList);
	        } catch (DateTimeParseException e) {
	            // Handle date parsing error
	            String errorMessage = "Invalid date format. Please provide dates in ISO format.";
	            return ResponseEntity.badRequest().body(errorMessage);
	        }
	    }

	    // If 'startDate' or 'endDate' is missing in the request body, return an empty array
	    return ResponseEntity.ok(Collections.emptyList());
	}


	@GetMapping("/invoice/client/{id}")
	public ResponseEntity<Object> getAllRoleByEmployee(@PathVariable("id") Long clientId) {

		List<Map<String, Object>> invoiceRole = clientInvoiceRepository.getAllInvoiceDetailsList23(clientId);

		Map<String, Map<String, Map<String, List<Map<String, Object>>>>> clientGroupMap = invoiceRole.stream()
				.collect(Collectors.groupingBy(action -> action.get("client_id").toString(),
						Collectors.groupingBy(action -> action.get("invoice_id").toString(),
								Collectors.groupingBy(action -> action.get("invoice_list_id").toString()))));

		List<Map<String, Object>> clientList = new ArrayList<>();
		for (Entry<String, Map<String, Map<String, List<Map<String, Object>>>>> clientLoop : clientGroupMap
				.entrySet()) {
			Map<String, Object> clientMap = new HashMap<>();
			clientMap.put("clientId", clientLoop.getKey());
			List<Map<String, Object>> invoiceList = new ArrayList<>();
			for (Entry<String, Map<String, List<Map<String, Object>>>> invoiceLoop : clientLoop.getValue().entrySet()) {
				Map<String, Object> invoiceMap = new HashMap<>();
				invoiceMap.put("invoiceId", invoiceLoop.getKey());

				List<Map<String, Object>> invoiceSubList = new ArrayList<>();
				for (Entry<String, List<Map<String, Object>>> invoiceSubLoop : invoiceLoop.getValue().entrySet()) {
					Map<String, Object> invoiceSubMap = new HashMap<>();
					int randomNumber = generateRandomNumber();
					String fileExtension = getFileExtensionForImage(invoiceSubLoop);
					invoiceSubMap.put("invoiceListId", invoiceSubLoop.getKey());
					invoiceMap.put("amount", invoiceSubLoop.getValue().get(0).get("listAmount"));
					invoiceMap.put("balance", invoiceSubLoop.getValue().get(0).get("balance"));
					invoiceMap.put("balanceAmount", invoiceSubLoop.getValue().get(0).get("balance_amount"));
					invoiceMap.put("clientId", invoiceSubLoop.getValue().get(0).get("client_id"));
					invoiceMap.put("CompanyId", invoiceSubLoop.getValue().get(0).get("company_id"));
					invoiceMap.put("description", invoiceSubLoop.getValue().get(0).get("description"));
					invoiceMap.put("companyState", invoiceSubLoop.getValue().get(0).get("companyState"));
					invoiceMap.put("gstType", invoiceSubLoop.getValue().get(0).get("gst_type"));
					invoiceMap.put("invoiceDate", invoiceSubLoop.getValue().get(0).get("invoice_date"));
					invoiceMap.put("paymentType", invoiceSubLoop.getValue().get(0).get("payment_type"));
					invoiceMap.put("received", invoiceSubLoop.getValue().get(0).get("received"));
					invoiceMap.put("roundOffAmount", invoiceSubLoop.getValue().get(0).get("round_off_amount"));
					String imageUrl = "company/" + randomNumber + "/"
							+ invoiceSubLoop.getValue().get(0).get("company_id") + "." + fileExtension;
					String gggg = "signature/" + randomNumber + "/" + invoiceSubLoop.getValue().get(0).get("company_id")
							+ "." + fileExtension;
					invoiceMap.put("signature", gggg);
					invoiceMap.put("company", imageUrl);
					if ("withTax".equals(invoiceSubLoop.getValue().get(0).get("gst_type"))) {
						double taxAmount = (double) invoiceSubLoop.getValue().get(0).get("tax_amount");
						invoiceMap.put("cgst", taxAmount / 2);
						invoiceMap.put("sgst", taxAmount / 2);
					} else if ("withoutTax".equals(invoiceSubLoop.getValue().get(0).get("gst_type"))) {
						double taxAmount = (double) invoiceSubLoop.getValue().get(0).get("tax_amount");
						invoiceMap.put("igst", taxAmount);
					}

					invoiceMap.put("taxAmount", invoiceSubLoop.getValue().get(0).get("tax_amount"));
					invoiceMap.put("companyName", invoiceSubLoop.getValue().get(0).get("company_name"));
					invoiceMap.put("accountNo", invoiceSubLoop.getValue().get(0).get("account_no"));
					invoiceMap.put("companyAddress", invoiceSubLoop.getValue().get(0).get("companyAddress"));
					invoiceMap.put("bankName", invoiceSubLoop.getValue().get(0).get("bank_name"));
					invoiceMap.put("companyEmail", invoiceSubLoop.getValue().get(0).get("companyEmail"));
					invoiceMap.put("holderName", invoiceSubLoop.getValue().get(0).get("holder_name"));
					invoiceMap.put("ifscCode", invoiceSubLoop.getValue().get(0).get("ifsc_code"));
					invoiceMap.put("location", invoiceSubLoop.getValue().get(0).get("location"));
					invoiceMap.put("phoneNumber1", invoiceSubLoop.getValue().get(0).get("phone_number1"));
					invoiceMap.put("phoneNumber2", invoiceSubLoop.getValue().get(0).get("phone_number2"));
					invoiceMap.put("pincode", invoiceSubLoop.getValue().get(0).get("pincode"));
					invoiceMap.put("taxNo", invoiceSubLoop.getValue().get(0).get("tax_no"));
					invoiceMap.put("country", invoiceSubLoop.getValue().get(0).get("country"));
					invoiceMap.put("gstNo", invoiceSubLoop.getValue().get(0).get("gst_no"));
					invoiceMap.put("clientName", invoiceSubLoop.getValue().get(0).get("client_name"));
					invoiceMap.put("clientEmail", invoiceSubLoop.getValue().get(0).get("email"));
					invoiceMap.put("clientGender", invoiceSubLoop.getValue().get(0).get("gender"));
					invoiceMap.put("clientPhoneNumber", invoiceSubLoop.getValue().get(0).get("phone_number"));
					invoiceMap.put("clientState", invoiceSubLoop.getValue().get(0).get("state"));
					invoiceMap.put("clientAddress", invoiceSubLoop.getValue().get(0).get("address"));
					invoiceMap.put("clientZipcode", invoiceSubLoop.getValue().get(0).get("zip_code"));

					invoiceSubMap.put("invoiceId", invoiceLoop.getKey());
					invoiceSubMap.put("totalTaxAmount", invoiceSubLoop.getValue().get(0).get("total_tax_amount"));
					invoiceSubMap.put("taxQuantityAmount", invoiceSubLoop.getValue().get(0).get("tax_quantity_amount"));
					invoiceSubMap.put("taxIncludePrice", invoiceSubLoop.getValue().get(0).get("tax_include_price"));
					invoiceSubMap.put("quantity", invoiceSubLoop.getValue().get(0).get("quantity"));
					invoiceSubMap.put("projectId", invoiceSubLoop.getValue().get(0).get("project_id"));
					invoiceSubMap.put("price", invoiceSubLoop.getValue().get(0).get("price"));
					invoiceSubMap.put("gst", invoiceSubLoop.getValue().get(0).get("gst"));
					invoiceSubMap.put("discountPercentage",
							invoiceSubLoop.getValue().get(0).get("discount_percentage"));
					invoiceSubMap.put("discountAmount", invoiceSubLoop.getValue().get(0).get("discount_amount"));
					invoiceSubMap.put("amount", invoiceSubLoop.getValue().get(0).get("listAmount"));
					invoiceSubMap.put("projectName", invoiceSubLoop.getValue().get(0).get("project_name"));

					invoiceSubList.add(invoiceSubMap);
				}

				invoiceMap.put("invoiceList", invoiceSubList);
				invoiceList.add(invoiceMap);
			}

			clientMap.put("invoiceDetails", invoiceList);
//			clientList.add(clientMap);
			return ResponseEntity.ok(clientMap);
		}

		return ResponseEntity.ok(clientList);

	}

	@GetMapping("/invoice/view1{id}")
	public ResponseEntity<?> getAllQuotationByClientDetailsById(@PathVariable("id") Long clientId) {
		List<Map<String, Object>> assestList = new ArrayList<>();

		List<Map<String, Object>> assestRole = clientInvoiceRepository.getAllInvoiceDetailsList23(clientId);

		// Grouping by hiring_id
		Map<String, List<Map<String, Object>>> assestGroupMap = assestRole.stream()
				.collect(Collectors.groupingBy(action -> action.get("client_id").toString()));

		for (Entry<String, List<Map<String, Object>>> departmentLoop : assestGroupMap.entrySet()) {
			Map<String, Object> departmentMap = new HashMap<>();
			departmentMap.put("clientId", Long.parseLong(departmentLoop.getKey()));
			departmentMap.put("clientName", departmentLoop.getValue().get(0).get("client_name"));
			Set<Object> uniquePreferredIds1 = new HashSet<>(); // Keep track of processed entries

			for (Entry<String, List<Map<String, Object>>> assestLoop : assestGroupMap.entrySet()) {
				if (uniquePreferredIds1.contains(assestLoop.getKey())) {
					continue;
				}

				Map<String, Object> departmentsubLoop = assestLoop.getValue().get(0);
				uniquePreferredIds1.add(assestLoop.getKey());

				Map<String, Object> departmentSubMap = new HashMap<>();

				departmentSubMap.put("invoiceId", departmentsubLoop.get("invoice_id"));
				departmentSubMap.put("invoiceDate", departmentsubLoop.get("invoice_date"));
				departmentSubMap.put("gstType", departmentsubLoop.get("gstType"));
				departmentSubMap.put("taxAmount", departmentsubLoop.get("taxAmount"));
				departmentSubMap.put("address", departmentsubLoop.get("address"));
				departmentSubMap.put("city", departmentsubLoop.get("city"));
				departmentSubMap.put("clientName", departmentsubLoop.get("client_name"));
				departmentSubMap.put("country", departmentsubLoop.get("country"));
				departmentSubMap.put("email", departmentsubLoop.get("email"));
				departmentSubMap.put("gender", departmentsubLoop.get("gender"));
				departmentSubMap.put("phoneNumber", departmentsubLoop.get("phone_number"));
				departmentSubMap.put("mobileNumber", departmentsubLoop.get("mobile_number"));
				departmentSubMap.put("state", departmentsubLoop.get("state"));
				departmentSubMap.put("zipCode", departmentsubLoop.get("zip_code"));
				departmentSubMap.put("companyName", departmentsubLoop.get("company_name"));
				departmentSubMap.put("companyEmail", departmentsubLoop.get("companyEmail"));
				departmentSubMap.put("phoneNumber1", departmentsubLoop.get("phone_number1"));
				departmentSubMap.put("phoneNumber2", departmentsubLoop.get("phone_number2"));
				departmentSubMap.put("pinCode", departmentsubLoop.get("pincode"));
				departmentSubMap.put("companyCountry", departmentsubLoop.get("companyCountry"));
				departmentSubMap.put("departmentName", departmentsubLoop.get("project_name"));
				departmentSubMap.put("projectName", departmentsubLoop.get("companyAddress"));
				departmentSubMap.put("clientId", departmentsubLoop.get("client_id"));

				List<Map<String, Object>> demo1List = new ArrayList<>();
				Set<Object> uniquePreferredIds = new HashSet<>();

				for (Map<String, Object> assestSubLoop : assestLoop.getValue()) {
					Object preferredId = assestSubLoop.get("invoice_list_id");

					// Check if the preferredId is already processed
					if (uniquePreferredIds.contains(preferredId)) {
						continue;
					}

					Map<String, Object> preferredMap = new HashMap<>();
					preferredMap.put("invoice_list_id", preferredId);
					preferredMap.put("amount", assestSubLoop.get("amount"));
					preferredMap.put("discount_amount", assestSubLoop.get("discount_amount"));
					preferredMap.put("discount_percentage", assestSubLoop.get("discount_percentage"));
					preferredMap.put("gst", assestSubLoop.get("gst"));
					preferredMap.put("price", assestSubLoop.get("price"));
					preferredMap.put("project_id", assestSubLoop.get("project_id"));
					preferredMap.put("quantity", assestSubLoop.get("quantity"));
					preferredMap.put("tax_include_price", assestSubLoop.get("tax_include_price"));
					preferredMap.put("tax_quantity_amount", assestSubLoop.get("tax_quantity_amount"));
					preferredMap.put("total_tax_amount", assestSubLoop.get("total_tax_amount"));
					preferredMap.put("projectName", assestSubLoop.get("project_name"));

					// Add the preferredId to the set to mark it as processed
					uniquePreferredIds.add(preferredId);

					demo1List.add(preferredMap);
				}

				departmentSubMap.put("preferredList", demo1List);

				assestList.add(departmentSubMap);
			}
		}
		return ResponseEntity.ok().body(assestList);

	}

	@GetMapping("/invoice/view/{id}")
	public ResponseEntity<?> getAllQuotationByClientDetailsById1(@PathVariable("id") Long clientId) {
		try {
			List<Map<String, Object>> assestList = new ArrayList<>();
			List<Map<String, Object>> assestRole = clientInvoiceRepository.getAllInvoiceDetailsList23(clientId);
			Map<String, Map<String, Map<String, List<Map<String, Object>>>>> assestGroupMap = assestRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("client_id").toString(),
							Collectors.groupingBy(action -> action.get("invoice_id").toString(),
									Collectors.groupingBy(action -> action.get("invoice_list_id").toString()))));
			for (Entry<String, Map<String, Map<String, List<Map<String, Object>>>>> clientEntry : assestGroupMap
					.entrySet()) {
				Map<String, Object> clientMap = new HashMap<>();
				clientMap.put("clientId", Long.parseLong(clientEntry.getKey()));
				List<Map<String, Object>> invoiceDetailsList = new ArrayList<>();
				for (Entry<String, Map<String, List<Map<String, Object>>>> invoiceEntry : clientEntry.getValue()
						.entrySet()) {
					Map<String, Object> invoiceMap = new HashMap<>();
					invoiceMap.put("invoiceId", invoiceEntry.getKey());
					List<Map<String, Object>> preferredList = new ArrayList<>();
					for (Entry<String, List<Map<String, Object>>> preferredEntry : invoiceEntry.getValue().entrySet()) {
						Map<String, Object> preferredMap = new HashMap<>();
						int randomNumber = generateRandomNumber();
						String fileExtension = getFileExtensionForImage(preferredEntry);
						preferredMap.put("invoice_list_id", preferredEntry.getKey());
						preferredMap.put("amount", preferredEntry.getValue().get(0).get("amount"));
						preferredMap.put("discount_amount", preferredEntry.getValue().get(0).get("discount_amount"));
						preferredMap.put("discount_percentage",
								preferredEntry.getValue().get(0).get("discount_percentage"));
						preferredMap.put("gst", preferredEntry.getValue().get(0).get("gst"));
						preferredMap.put("price", preferredEntry.getValue().get(0).get("price"));
						preferredMap.put("project_id", preferredEntry.getValue().get(0).get("project_id"));
						preferredMap.put("quantity", preferredEntry.getValue().get(0).get("quantity"));
						preferredMap.put("tax_include_price",
								preferredEntry.getValue().get(0).get("tax_include_price"));
						preferredMap.put("tax_quantity_amount",
								preferredEntry.getValue().get(0).get("tax_quantity_amount"));
						preferredMap.put("total_tax_amount", preferredEntry.getValue().get(0).get("total_tax_amount"));
						preferredMap.put("projectName", preferredEntry.getValue().get(0).get("project_name"));

						invoiceMap.put("invoiceDate", preferredEntry.getValue().get(0).get("invoice_date"));
						invoiceMap.put("gstType", preferredEntry.getValue().get(0).get("gstType"));
						invoiceMap.put("taxAmount", preferredEntry.getValue().get(0).get("taxAmount"));
						invoiceMap.put("address", preferredEntry.getValue().get(0).get("address"));
						invoiceMap.put("city", preferredEntry.getValue().get(0).get("city"));
						invoiceMap.put("clientName", preferredEntry.getValue().get(0).get("client_name"));
						invoiceMap.put("country", preferredEntry.getValue().get(0).get("country"));
						invoiceMap.put("email", preferredEntry.getValue().get(0).get("email"));
						invoiceMap.put("gender", preferredEntry.getValue().get(0).get("gender"));
						invoiceMap.put("phoneNumber", preferredEntry.getValue().get(0).get("phone_number"));
						invoiceMap.put("mobileNumber", preferredEntry.getValue().get(0).get("mobile_number"));
						invoiceMap.put("state", preferredEntry.getValue().get(0).get("state"));
						invoiceMap.put("zipCode", preferredEntry.getValue().get(0).get("zip_code"));
						invoiceMap.put("companyName", preferredEntry.getValue().get(0).get("company_name"));
						invoiceMap.put("companyEmail", preferredEntry.getValue().get(0).get("companyEmail"));
						invoiceMap.put("phoneNumber1", preferredEntry.getValue().get(0).get("phone_number1"));
						invoiceMap.put("phoneNumber2", preferredEntry.getValue().get(0).get("phone_number2"));
						invoiceMap.put("pinCode", preferredEntry.getValue().get(0).get("pincode"));
						invoiceMap.put("companyCountry", preferredEntry.getValue().get(0).get("companyCountry"));
						invoiceMap.put("departmentName", preferredEntry.getValue().get(0).get("project_name"));
						invoiceMap.put("projectName", preferredEntry.getValue().get(0).get("companyAddress"));
						invoiceMap.put("clientId", preferredEntry.getValue().get(0).get("client_id"));
						String imageUrl = "company/" + randomNumber + "/"
								+ preferredEntry.getValue().get(0).get("company_id") + "." + fileExtension;
						String gggg = "signature/" + randomNumber + "/"
								+ preferredEntry.getValue().get(0).get("company_id") + "." + fileExtension;
						invoiceMap.put("signature", gggg);
						invoiceMap.put("company", imageUrl);
						clientMap.put("clientName", preferredEntry.getValue().get(0).get("client_name"));
						preferredList.add(preferredMap);
					}

					invoiceMap.put("invoiceList", preferredList);
					invoiceDetailsList.add(invoiceMap);
				}

				clientMap.put("invoiceDetails", invoiceDetailsList);
				assestList.add(clientMap);
			}

			return ResponseEntity.ok().body(assestList);
		} catch (Exception e) {
			String errorMessage = "An error occurred while fetching client invoices.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	private String getFileExtensionForImage(Entry<String, List<Map<String, Object>>> preferredEntry) {
		if (preferredEntry == null || preferredEntry.getValue().isEmpty()
				|| preferredEntry.getValue().get(0).get("company_id") == null) {
			return "jpg";
		}

		Object companyId = preferredEntry.getValue().get(0).get("company_id").toString();

		if (companyId.toString().endsWith(".png")) {
			return "png";
		} else if (companyId.toString().endsWith(".jpg")) {
			return "jpg";
		} else {
			return "jpg"; // Default to jpg if the extension is not recognized
		}
	}

	@GetMapping("/invoice/dashboard/previous")
	public List<Map<String, Object>> allDeptDetails(@RequestParam(required = true) String dashboard) {
		try {
			if ("previous".equals(dashboard)) {
				return clientInvoiceRepository.ALLCountcustomer();
			} else {
				return Collections.emptyList();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@GetMapping("/invoice/dashboard")
	public ResponseEntity<?> getTaskAssignedDetailsp(@RequestParam(required = true) String dashboard) {
		try {
			if ("invoice".equals(dashboard)) {
				List<Map<String, Object>> tasks = clientInvoiceRepository.getAllInvoiceDetailsListuuuu();
				List<Map<String, Object>> taskList = new ArrayList<>();

				for (Map<String, Object> taskAssigned : tasks) {
					Map<String, Object> taskAssignedMap = new HashMap<>();

					int randomNumber = generateRandomNumber();

					String imageUrl = "clientProfile/" + randomNumber + "/" + taskAssigned.get("client_id");
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

	@PostMapping("/invoice/cilent")
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
				List<Map<String, Object>> leaveData = clientInvoiceRepository.getAllinvoiceYear(year, clientId);

				return ResponseEntity.ok(leaveData);
			}
			break;

		case "month":
			if (requestBody.containsKey("monthName")) {
				String monthName = requestBody.get("monthName").toString();
				List<Map<String, Object>> leaveData1 = clientInvoiceRepository.getAllinvoicemonthname(monthName,
						clientId);

				return ResponseEntity.ok(leaveData1);
			}
			break;

		default:
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.badRequest().build();
	}

	@PostMapping("/invoice/manager")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAnd(
			@RequestBody Map<String, Object> requestBody) {

		if (!requestBody.containsKey("choose")) {
			return ResponseEntity.badRequest().build();
		}

		String choose = requestBody.get("choose").toString();

		switch (choose) {
		case "year":
			return handleYearScenario(requestBody);

		case "gst":
			return handleGstScenario(requestBody);

		case "month":
			return handleMonthScenario(requestBody);

		default:
			return ResponseEntity.badRequest().build();
		}
	}

	private ResponseEntity<List<Map<String, Object>>> handleYearScenario(Map<String, Object> requestBody) {
		if (requestBody.containsKey("year")) {
			String year = requestBody.get("year").toString();
			List<Map<String, Object>> leaveData = clientInvoiceRepository.getAllQuotationByInvoice(year);
			return processResponse(leaveData);
		}
		return ResponseEntity.badRequest().build();
	}

	private ResponseEntity<List<Map<String, Object>>> handleGstScenario(Map<String, Object> requestBody) {
		if (requestBody.containsKey("data")) {
			String gstData = requestBody.get("data").toString();

			switch (gstData) {
			case "withTax":
				return handleWithTaxScenario();
			case "withoutTax":
				return handleWithoutTaxScenario();
			default:
				return ResponseEntity.badRequest().build();
			}
		}
		return ResponseEntity.badRequest().build();
	}

	private ResponseEntity<List<Map<String, Object>>> handleWithTaxScenario() {
		List<Map<String, Object>> leaveData = clientInvoiceRepository.getAllQuotationByInvoiceGST();
		return processResponse(leaveData);
	}

	private ResponseEntity<List<Map<String, Object>>> handleWithoutTaxScenario() {
		List<Map<String, Object>> leaveData = clientInvoiceRepository.getAllQuotationByInvoiceGSTwithOut();
		return processResponse(leaveData);
	}

	private ResponseEntity<List<Map<String, Object>>> handleMonthScenario(Map<String, Object> requestBody) {
		if (requestBody.containsKey("monthName")) {
			String monthName = requestBody.get("monthName").toString();
			List<Map<String, Object>> leaveData1 = clientInvoiceRepository.getAllleaveInvoice(monthName);
			return processResponse(leaveData1);
		}
		return ResponseEntity.badRequest().build();
	}

	private ResponseEntity<List<Map<String, Object>>> processResponse(List<Map<String, Object>> leaveData) {
		List<Map<String, Object>> imageResponses = new ArrayList<>();
		for (Map<String, Object> image : leaveData) {
			int randomNumber = generateRandomNumber();
			String imageUrl = "clientProfile/" + randomNumber + "/" + image.get("clientId");
			Map<String, Object> imageResponse = new HashMap<>();
			imageResponse.put("clientProfile", imageUrl);
			imageResponse.putAll(image);
			imageResponses.add(imageResponse);
		}
		return ResponseEntity.ok(imageResponses);
	}

//	 @PostMapping("/invoice/manager")
//	 public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAnd(@RequestBody Map<String, Object> requestBody) {
//	     if (!requestBody.containsKey("choose")) {
//	         return ResponseEntity.badRequest().build();
//	     }
//
//	     String choose = requestBody.get("choose").toString();
//	     List<Map<String, Object>> imageResponses = new ArrayList<>();
//
//	     switch (choose) {
//	         case "year":
//	             if (requestBody.containsKey("year")) {
//	                 String year = requestBody.get("year").toString();
//	                 imageResponses = processInvoiceByYear(year);
//	                 return ResponseEntity.ok(imageResponses);
//	             }
//	             break;
//
//	         case "withTax":
//	             imageResponses = processInvoiceWithTax();
//	             return ResponseEntity.ok(imageResponses);
//
//	         case "withoutTax":
//	             imageResponses = processInvoiceWithoutTax();
//	             return ResponseEntity.ok(imageResponses);
//
//	         case "month":
//	             if (requestBody.containsKey("monthName")) {
//	                 String monthName = requestBody.get("monthName").toString();
//	                 imageResponses = processInvoiceByMonth(monthName);
//	                 return ResponseEntity.ok(imageResponses);
//	             }
//	             break;
//
//	         default:
//	             return ResponseEntity.badRequest().build();
//	     }
//
//	     return ResponseEntity.badRequest().build();
//	 }
//
//	 private List<Map<String, Object>> processInvoiceByYear(String year) {
//	     List<Map<String, Object>> leaveData = clientInvoiceRepository.getAllQuotationByInvoice(year);
//	     return processImageResponses(leaveData);
//	 }
//
//	 private List<Map<String, Object>> processInvoiceWithTax() {
//	     List<Map<String, Object>> leaveData = clientInvoiceRepository.getAllQuotationByInvoiceGST();
//	     return processImageResponses(leaveData);
//	 }
//
//	 private List<Map<String, Object>> processInvoiceWithoutTax() {
//	     List<Map<String, Object>> leaveData = clientInvoiceRepository.getAllQuotationByInvoiceGSTwithOut();
//	     return processImageResponses(leaveData);
//	 }
//
//	 private List<Map<String, Object>> processInvoiceByMonth(String monthName) {
//	     List<Map<String, Object>> leaveData = clientInvoiceRepository.getAllleaveInvoice(monthName);
//	     return processImageResponses(leaveData);
//	 }
//
//	 private List<Map<String, Object>> processImageResponses(List<Map<String, Object>> leaveData) {
//	     List<Map<String, Object>> imageResponses = new ArrayList<>();
//	     for (Map<String, Object> image : leaveData) {
//	         int randomNumber = generateRandomNumber();
//	         String imageUrl = "clientProfile/" + randomNumber + "/" + image.get("clientId");
//	         Map<String, Object> imageResponse = new HashMap<>();
//	         imageResponse.put("clientProfile", imageUrl);
//	         imageResponse.putAll(image);
//	         imageResponses.add(imageResponse);
//	     }
//	     return imageResponses;
//	 }
//
//

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000); // Change the upper limit if needed
	}

	@GetMapping("/client/dashboard/invoice/{client_id}")
	public List<Map<String, Object>> getAllAttendanceDetails(@PathVariable Long client_id) {
		return clientInvoiceRepository.getAllInvoiceDetailsListClient(client_id);
	}
}
