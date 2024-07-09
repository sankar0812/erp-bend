package com.example.erp.controller.organization;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.erp.entity.message.MemberList;
import com.example.erp.entity.organization.Company;
import com.example.erp.repository.message.MemberListRepository;
import com.example.erp.service.organization.CompanyService;

@RestController
@CrossOrigin
public class CompanyController {

	@Autowired
	private CompanyService companyservice;

	@Autowired
	private MemberListRepository listRepository;

	@GetMapping("/company")
	public ResponseEntity<?> displayAllImages(@RequestParam(required = true) String company) {
		if ("company".equals(company)) {
			List<Company> images = companyservice.listAll();
			Map<String, Object> imageObjects = new HashMap<>();
			for (Company image : images) {
				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(image);
				String imageUrl = "company/" + randomNumber + "/" + image.getCompanyId() + "." + fileExtension;
				String gggg = "signature/" + randomNumber + "/" + image.getCompanyId() + "." + fileExtension;
				image.setUrl(imageUrl);
				image.setSignatureUrl(gggg);
				imageObjects.put("companyId", image.getCompanyId());
				imageObjects.put("companyName", image.getCompanyName());
				imageObjects.put("url", image.getUrl());
				imageObjects.put("signature", image.getSignatureUrl());
				imageObjects.put("address", image.getAddress());
				imageObjects.put("bankName", image.getBankName());
				imageObjects.put("branchName", image.getBranchName());
				imageObjects.put("country", image.getCountry());
				imageObjects.put("email", image.getEmail());
				imageObjects.put("state", image.getState());
				imageObjects.put("pincode", image.getPincode());
				imageObjects.put("location", image.getLocation());
				imageObjects.put("phoneNumber1", image.getPhoneNumber1());
				imageObjects.put("phoneNumber2", image.getPhoneNumber2());
				imageObjects.put("gstNo", image.getGstNo());
				imageObjects.put("taxNo", image.getTaxNo());
				imageObjects.put("accountNo", image.getAccountNo());
				imageObjects.put("ifscCode", image.getIfscCode());
				imageObjects.put("holderName", image.getHolderName());
				imageObjects.put("status", image.isStatus());
			}
			return ResponseEntity.ok(imageObjects);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
		}
	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}

	@GetMapping("company/{randomNumber}/{id:.+}")
	public ResponseEntity<Resource> serveImage(@PathVariable("randomNumber") int randomNumber,
			@PathVariable("id") String id) {
		// Extract the file extension from the 'id' string
		String[] parts = id.split("\\.");
		if (parts.length != 2) {
			return ResponseEntity.badRequest().build();
		}
		String fileExtension = parts[1];

		Long imageId;
		try {
			imageId = Long.parseLong(parts[0]);
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().build(); // Invalid image ID format
		}

		Company image = companyservice.findById(imageId);
		if (image == null) {
			return ResponseEntity.notFound().build();
		}

		byte[] imageBytes;
		try {
			imageBytes = image.getProfile().getBytes(1, (int) image.getProfile().length());
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		ByteArrayResource resource = new ByteArrayResource(imageBytes);
		HttpHeaders headers = new HttpHeaders();

		if ("jpg".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.IMAGE_JPEG);
		} else if ("png".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.IMAGE_PNG);
		} else {

			headers.setContentType(MediaType.IMAGE_JPEG);
		}

		return ResponseEntity.ok().headers(headers).body(resource);
	}

	private String getFileExtensionForImage(Company image) {
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

	@GetMapping("signature/{randomNumber}/{id:.+}")
	public ResponseEntity<Resource> serveImageSigbjiecbn(@PathVariable("randomNumber") int randomNumber,
			@PathVariable("id") String id) {
		// Extract the file extension from the 'id' string
		String[] parts = id.split("\\.");
		if (parts.length != 2) {
			return ResponseEntity.badRequest().build();
		}
		String fileExtension = parts[1];

		Long imageId;
		try {
			imageId = Long.parseLong(parts[0]);
		} catch (NumberFormatException e) {
			return ResponseEntity.badRequest().build(); // Invalid image ID format
		}

		Company image = companyservice.findById(imageId);
		if (image == null) {
			return ResponseEntity.notFound().build();
		}

		byte[] imageBytes;
		try {
			imageBytes = image.getSignature().getBytes(1, (int) image.getSignature().length());
		} catch (SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		ByteArrayResource resource = new ByteArrayResource(imageBytes);
		HttpHeaders headers = new HttpHeaders();

		if ("jpg".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.IMAGE_JPEG);
		} else if ("png".equalsIgnoreCase(fileExtension)) {
			headers.setContentType(MediaType.IMAGE_PNG);
		} else {

			headers.setContentType(MediaType.IMAGE_JPEG);
		}

		return ResponseEntity.ok().headers(headers).body(resource);
	}

	@PostMapping("/company/save")
	public ResponseEntity<?> addEmployeeWithImage(@RequestParam("profile") MultipartFile file,
			@RequestParam("signature") MultipartFile signature, @RequestParam("companyName") String companyName,
			@RequestParam("gstNo") String gstNo, @RequestParam("taxNo") String taxNo,
			@RequestParam("country") String country, @RequestParam("email") String email,
			@RequestParam("state") String state, @RequestParam("address") String address,
			@RequestParam("bankName") String bankName, @RequestParam("branchName") String branchName,
			@RequestParam("holderName") String holderName, @RequestParam("ifscCode") String ifscCode,
			@RequestParam("location") String location, @RequestParam("pincode") int pincode,
			@RequestParam("accountNo") String accountNo,
			@RequestParam(value = "phoneNumber2", required = false) String phoneNumber2,
			@RequestParam("phoneNumber1") String phoneNumber1) {
		try {

			if (phoneNumber1 != null && phoneNumber1.equals(phoneNumber2)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phone numbers must be different.");
			}
			Optional<MemberList> existingemail = listRepository.findByEmail(email);
			if (existingemail.isPresent()) {
				return ResponseEntity.badRequest().body("email with ID " + email + " already exists.");
			}
			Optional<MemberList> existingMobileOptional = listRepository.findByPhoneNumber(phoneNumber1);

			if (existingMobileOptional.isPresent()) {
				MemberList existingClient = existingMobileOptional.get();
				if (existingClient.getPhoneNumber() != null && existingClient.getPhoneNumber().equals(phoneNumber1)) {
					return ResponseEntity.badRequest()
							.body("This number " + phoneNumber1 + " already exists for another client.");
				} else if (existingClient.getPhoneNumber() != null
						&& existingClient.getPhoneNumber().equals(phoneNumber2)) {
					return ResponseEntity.badRequest()
							.body("Alternate number " + phoneNumber2 + " already exists for another client.");
				}
			}
			byte[] bytes = file.getBytes();
			Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
			byte[] bytes1 = signature.getBytes();
			Blob blob1 = new javax.sql.rowset.serial.SerialBlob(bytes1);
			Company employee = new Company();
			employee.setProfile(blob);
			employee.setSignature(blob1);
			employee.setStatus(true);
			employee.setCompanyName(companyName);
			employee.setCountry(country);
			employee.setEmail(email);
			employee.setState(state);
			employee.setAddress(address);
			employee.setBankName(bankName);
			employee.setBranchName(branchName);
			employee.setAccountNo(accountNo);
			employee.setHolderName(holderName);
			employee.setGstNo(gstNo);
			employee.setTaxNo(taxNo);
			employee.setLocation(location);
			employee.setPincode(pincode);
			employee.setPhoneNumber1(phoneNumber1);
			employee.setPhoneNumber2(phoneNumber2);
			employee.setIfscCode(ifscCode);

		
			if (accountNo.length() < 10 || accountNo.length() > 18) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Account number must be between 10 and 18 digits long.");
			}
			companyservice.SaveorUpdate(employee);

			long id = employee.getCompanyId();

			return ResponseEntity.ok("Employee added successfully. Employee ID: " + id);
		} catch (IOException | SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding employee.");

		}
	}

	@PutMapping("/company/or/{id}")
	public ResponseEntity<Boolean> toggleCustomerStatus(@PathVariable(name = "id") long id) {
		try {
			Company company = companyservice.findById(id);
			if (company != null) {
				boolean currentStatus = company.isStatus();
				company.setStatus(!currentStatus);
				companyservice.SaveorUpdate(company);
			} else {

				return ResponseEntity.ok(false);
			}

			return ResponseEntity.ok(company.isStatus());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);

		}
	}

	@PutMapping("/company/edit/{companyId}")
	public ResponseEntity<String> updateCompany(@PathVariable long companyId,
			@RequestParam(value = "profile", required = false) MultipartFile file,
			@RequestParam(value = "signature", required = false) MultipartFile signature,
			@RequestParam(value = "companyName", required = false) String companyName,
			@RequestParam(value = "gstNo", required = false) String gstNo,
			@RequestParam(value = "taxNo", required = false) String taxNo,
			@RequestParam(value = "country", required = false) String country,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "state", required = false) String state,
			@RequestParam(value = "address", required = false) String address,
			@RequestParam(value = "bankName", required = false) String bankName,
			@RequestParam(value = "branchName", required = false) String branchName,
			@RequestParam(value = "holderName", required = false) String holderName,
			@RequestParam(value = "ifscCode", required = false) String ifscCode,
			@RequestParam(value = "location", required = false) String location,
			@RequestParam(value = "pincode", required = false) Integer pincode,
			@RequestParam(value = "accountNo", required = false) String accountNo,
			@RequestParam(value = "phoneNumber2", required = false) String phoneNumber2,
			@RequestParam(value = "phoneNumber1", required = false) String phoneNumber1) {
		try {
			Company company = companyservice.getCompanyById(companyId);

			if (company == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Company not found.");
			}

			if (phoneNumber1 != null && phoneNumber1.equals(phoneNumber2)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phone numbers must be different.");
			}
			Optional<MemberList> existingClientOptional = listRepository.findByEmail(email);

			if (existingClientOptional.isPresent()) {
				MemberList existingClient = existingClientOptional.get();
				if (existingClient.getEmail() != null && existingClient.getEmail().equals(email)) {
					return ResponseEntity.badRequest().body("Email ID " + email + " already exists for another.");
				}

			}
			Optional<MemberList> existingMobileOptional = listRepository.findByPhoneNumber(phoneNumber1);
			if (existingMobileOptional.isPresent()) {
				MemberList existingClient = existingMobileOptional.get();
				if (existingClient.getPhoneNumber() != null && existingClient.getPhoneNumber().equals(phoneNumber1)) {
					return ResponseEntity.badRequest()
							.body("This number " + phoneNumber1 + " already exists for another client.");
				} else if (existingClient.getPhoneNumber() != null
						&& existingClient.getPhoneNumber().equals(phoneNumber2)) {
					return ResponseEntity.badRequest()
							.body("Alternate number " + phoneNumber2 + " already exists for another client.");
				}
			}
			
			
			
			if (file != null && !file.isEmpty()) {
				byte[] bytes = file.getBytes();
				Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
				company.setProfile(blob);
			}
			if (signature != null && !signature.isEmpty()) {
				byte[] bytes1 = signature.getBytes();
				Blob blob1 = new javax.sql.rowset.serial.SerialBlob(bytes1);
				company.setSignature(blob1);
			}
			if (companyName != null) {
				company.setCompanyName(companyName);
			}

			if (gstNo != null) {
				company.setGstNo(gstNo);
			}

			if (taxNo != null) {
				company.setTaxNo(taxNo);
			}

			if (country != null) {
				company.setCountry(country);
			}

			if (email != null) {
				company.setEmail(email);
			}

			if (location != null) {
				company.setLocation(location);
			}

			if (state != null) {
				company.setState(state);
			}

			if (address != null) {
				company.setAddress(address);
			}

			if (bankName != null) {
				company.setBankName(bankName);
			}

			if (branchName != null) {
				company.setBranchName(branchName);
			}

			if (ifscCode != null) {
				company.setIfscCode(ifscCode);
			}

			if (holderName != null) {
				company.setHolderName(holderName);
			}
			if (phoneNumber1 != null) {
				company.setPhoneNumber1(phoneNumber1);
			}

			if (pincode != null) {
				company.setPincode(pincode);
			}

			if (accountNo != null) {
				if (accountNo.length() < 10 || accountNo.length() > 18) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Account number must be between 10 and 18 digits long.");
				}
				company.setAccountNo(accountNo);
			}
			if (phoneNumber2 != null) {
				company.setPhoneNumber2(phoneNumber2);
			}

			companyservice.SaveorUpdate(company);

			return ResponseEntity.ok("Company updated successfully. Company ID: " + companyId);
		} catch (IOException | SQLException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating company.");
		}
	}

	@DeleteMapping("/company/companydelete/{companyId}")
	public ResponseEntity<String> deleteTitle(@PathVariable("companyId") Long companyId) {
		companyservice.deleteCompanyById(companyId);
		return ResponseEntity.ok("company deleted successfully");

	}
}
