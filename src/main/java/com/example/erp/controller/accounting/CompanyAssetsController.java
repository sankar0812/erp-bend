package com.example.erp.controller.accounting;

import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
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
import com.example.erp.entity.accounting.CompanyAssets;
import com.example.erp.entity.accounting.CompanyAssetsList;
import com.example.erp.repository.accounting.CompanyAssetsRepository;
import com.example.erp.service.accounting.CompanyAssetsService;


@RestController
@CrossOrigin
public class CompanyAssetsController {
	@Autowired
	private CompanyAssetsService service;

	@Autowired
	private CompanyAssetsRepository companyAssetsRepository;

	@GetMapping("/companyassets")
	public ResponseEntity<?> displayAllImages(@RequestParam(required = true) String CompanyAssetsType) {
		try {
			if ("companyassets".equals(CompanyAssetsType)) {
				List<CompanyAssets> CompanyAssetss = service.listAll();
				List<CompanyAssets> CompanyAssetsResponses = new ArrayList<>();

				for (CompanyAssets CompanyAssets : CompanyAssetss) {
					int randomNumber = generateRandomNumber();
					String imageUrl = "companyassets/" + randomNumber + "/" + CompanyAssets.getCompanyAssetsId();

					CompanyAssets CompanyAssetsResponse = new CompanyAssets();
					CompanyAssetsResponse.setCompanyAssetsId(CompanyAssets.getCompanyAssetsId());
					CompanyAssetsResponse.setUrl(imageUrl);
					CompanyAssetsResponse.setDate(CompanyAssets.getDate());
					CompanyAssetsResponse.setAssetValues(CompanyAssets.getAssetValues());
					CompanyAssetsResponses.add(CompanyAssetsResponse);
				}

				return ResponseEntity.ok().body(CompanyAssetsResponses);
			} else	 {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("The provided CompanyAssetsType is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/CompanyAssets")
	public ResponseEntity<?> getDetails(@RequestParam(required = true) String serverParam) {
		try {
			if ("server".equals(serverParam)) {
				Iterable<CompanyAssets> designationDetails = service.listAll();
				return new ResponseEntity<>(designationDetails, HttpStatus.OK);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided serverParam is not supported.");
			}
		} catch (Exception e) {
			String errorMessage = "An error occurred while retrieving designation details.";
			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/company/assest/view")
	public ResponseEntity<Object> getAllRoleByEmployee(@RequestParam(required = true) String assest) {
		if ("Assest".equals(assest)) {
			List<Map<String, Object>> assestList = new ArrayList<>();
			List<Map<String, Object>> assestRole = companyAssetsRepository.getAllDetails();
			Map<String, List<Map<String, Object>>> assestGroupMap = assestRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("company_assets_id").toString()));

			for (Entry<String, List<Map<String, Object>>> assestLoop : assestGroupMap.entrySet()) {
				Map<String, Object> assestMap = new HashMap<>();
				assestMap.put("companyAssetsId", Long.parseLong(assestLoop.getKey()));
				assestMap.put("assetValues", assestLoop.getValue().get(0).get("asset_values"));
				assestMap.put("date", assestLoop.getValue().get(0).get("date"));
				assestMap.put("status", assestLoop.getValue().get(0).get("status"));
				assestMap.put("imageUrl", assestLoop.getValue().get(0).get("url"));
				int randomNumber = generateRandomNumber();
				String imageUrl1 = "accessories/" + randomNumber + "/" + Long.parseLong(assestLoop.getKey());
				String imageUrl = "companyassets/" + randomNumber + "/" + Long.parseLong(assestLoop.getKey());
				assestMap.put("url", imageUrl);
				assestMap.put("image", imageUrl1);
				List<Map<String, Object>> assestSubList = new ArrayList<>();
				for (Map<String, Object> assestSubLoop : assestLoop.getValue()) {

					Map<String, Object> departmentSubMap = new HashMap<>();
					departmentSubMap.put("companyAssetsListId", assestSubLoop.get("company_assets_list_id"));
					departmentSubMap.put("accessoriesId", assestSubLoop.get("accessories_id"));
					departmentSubMap.put("brandId", assestSubLoop.get("brand_id"));
					departmentSubMap.put("brandName", assestSubLoop.get("brand_name"));
					departmentSubMap.put("image", imageUrl1);
					departmentSubMap.put("accessoriesName", assestSubLoop.get("accessories_name"));
					departmentSubMap.put("count", assestSubLoop.get("count"));
					assestSubList.add(departmentSubMap);
				}
				assestMap.put("companyAssetsType", assestSubList);
				assestList.add(assestMap);
			}

			return ResponseEntity.ok(assestList);
		} else {
			String errorMessage = "Invalid value for 'department'. Expected 'Department'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@PostMapping("/companyassets/save")
	public ResponseEntity<?> saveProductWithDemo(@RequestBody CompanyAssets assest) {
		try {
			String base64Image = assest.getUrl();
			System.out.println(base64Image);
			byte[] imageBytes = Base64.getDecoder().decode(base64Image);
			Blob blob = null;
			try {
				blob = new javax.sql.rowset.serial.SerialBlob(imageBytes);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			assest.setBilling(blob);
			assest.setStatus(true);

			service.save(assest);
			return ResponseEntity.ok(assest);
		} catch (Exception e) {
			
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving product: " + e.getMessage());
		}
	}

	@RequestMapping("/companyassets/{companyassetsId}")
	private Optional<CompanyAssets> getCompanyAssets(@PathVariable(name = "companyassetsId") long CompanyAssetsId) {
		return service.getCompanyAssetsById(CompanyAssetsId);

	}

	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}

	@GetMapping("companyassets/{randomNumber}/{id}")
	public ResponseEntity<ByteArrayResource> serveFile(@PathVariable("randomNumber") int randomNumber,
			@PathVariable("id") Long id) {
		Optional<CompanyAssets> companyAssetsOptional = service.getCompanyAssetsById(id);

		if (companyAssetsOptional.isPresent()) {
			CompanyAssets companyAssets = companyAssetsOptional.get();
			String filename = "file_" + randomNumber + "_" + id;

			try {
				byte[] fileBytes = companyAssets.getBilling().getBytes(1, (int) companyAssets.getBilling().length());

				String extension = determineFileExtension(fileBytes);
				MediaType mediaType = determineMediaType(extension);

				ByteArrayResource resource = new ByteArrayResource(fileBytes);

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(mediaType);
				headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + filename + "." + extension);

				return ResponseEntity.ok().headers(headers).body(resource);
			} catch (SQLException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	private String determineFileExtension(byte[] fileBytes) {
		try {
			String fileSignature = bytesToHex(Arrays.copyOfRange(fileBytes, 0, 4));
			if (fileSignature.startsWith("89504E47")) {
				return "png";
			} else if (fileSignature.startsWith("FFD8FF")) {
				return "jpg";
			} else if (fileSignature.startsWith("52494646")) {
				return "webp";
			} else if (fileSignature.startsWith("47494638")) {
				return "gif";
			} else if (fileSignature.startsWith("66747970")) {
				return "mp4";
			} else if (fileSignature.startsWith("25504446")) {
				return "pdf";
			}
		} catch (Exception e) {
		}
		return "unknown";
	}

	private MediaType determineMediaType(String extension) {
		switch (extension) {
		case "png":
			return MediaType.IMAGE_PNG;
		case "jpg":
			return MediaType.IMAGE_JPEG;
		case "pdf":
			return MediaType.APPLICATION_PDF;
		case "webp":
			return MediaType.parseMediaType("image/webp");
		case "gif":
			return MediaType.parseMediaType("image/gif");
		case "mp4":
			return MediaType.parseMediaType("video/mp4");
		default:
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}

	private String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b));
		}
		return sb.toString();
	}

	@PutMapping("/companyassets/status/{companyassets_id}")
	public ResponseEntity<Boolean> toggleCustomerStatus(
			@PathVariable(name = "companyassets_id") long CompanyAssets_id) {
		try {
			CompanyAssets CompanyAssets = service.findById(CompanyAssets_id);
			if (CompanyAssets != null) {
				boolean currentStatus = CompanyAssets.isStatus();
				CompanyAssets.setStatus(!currentStatus);
				service.SaveorUpdate(CompanyAssets);
			} else {

				return ResponseEntity.ok(false);
			}

			return ResponseEntity.ok(CompanyAssets.isStatus()); // Return the new status (true or false)
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}



	@DeleteMapping("/companyassets/delete/{companyAssets_id}")
	public ResponseEntity<String> deleteTitle(@PathVariable("companyAssets_id") Long companyAssets_id) {
		service.deleteCompanyAssetsIdById(companyAssets_id);
		return ResponseEntity.ok("CompanyAssets deleted successfully");

	}

	@GetMapping("/companyassets/view")
	public ResponseEntity<List<Map<String, Object>>> displayAllImages2(
			@RequestParam(required = true) String trueParam) {
		try {
			if ("companyassets".equals(trueParam)) {
				List<Map<String, Object>> employeeDetails = companyAssetsRepository.AllEmployees();
				List<Map<String, Object>> employeeResponses = new ArrayList<>();
				for (Map<String, Object> employeeDetail : employeeDetails) {
					int randomNumber = generateRandomNumber();
					String imageUrl1 = "accessories/" + randomNumber + "/" + employeeDetail.get("accessories_id") ;
					String imageUrl = "companyassets/" + randomNumber + "/" + employeeDetail.get("company_assets_id");
					Map<String, Object> employeeResponse = new HashMap<>();
					employeeResponse.put("companyAssetsId", employeeDetail.get("company_assets_id"));
					employeeResponse.put("accessoriesId", employeeDetail.get("accessories_id"));
					employeeResponse.put("assetValues", employeeDetail.get("asset_values"));
					employeeResponse.put("brantId", employeeDetail.get("brant_id"));
					employeeResponse.put("count", employeeDetail.get("count"));
					employeeResponse.put("status", employeeDetail.get("status"));
					employeeResponse.put("brandName", employeeDetail.get("brand_name"));
					employeeResponse.put("accessoriesName", employeeDetail.get("accessories_name"));
					employeeResponse.put("date", employeeDetail.get("date"));
					employeeResponse.put("url", imageUrl);
					employeeResponse.put("image", imageUrl1);

					employeeResponses.add(employeeResponse);
				}

				return ResponseEntity.ok(employeeResponses);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/companyassets/edit/{id}")
	public ResponseEntity<?> updateCompanyAssets1(@PathVariable Long id,
			@RequestBody CompanyAssets updatedCompanyAssets) {
		try {
			Optional<CompanyAssets> existingCompanyAssetsOptional = service.getCompanyAssetsById(id);

			if (existingCompanyAssetsOptional.isPresent()) {
				CompanyAssets existingCompanyAssest = existingCompanyAssetsOptional.get();

				if (updatedCompanyAssets.getDate() != null) {
					existingCompanyAssest.setDate(updatedCompanyAssets.getDate());
				}
				if (updatedCompanyAssets.getAssetValues() != 0) {
					existingCompanyAssest.setAssetValues(updatedCompanyAssets.getAssetValues());
				}
				if (updatedCompanyAssets.getUrl() != null) {
					existingCompanyAssest.setUrl(updatedCompanyAssets.getUrl());

					String base64Image = updatedCompanyAssets.getUrl();
					byte[] imageBytes = Base64.getDecoder().decode(base64Image);
					Blob blob = new SerialBlob(imageBytes);
					existingCompanyAssest.setBilling(blob);
				}

				// Assuming that CompanyAssetsList has an equals method that compares based on some unique identifier
				if (updatedCompanyAssets.getCompanyAssetsType() != null
				        && !updatedCompanyAssets.getCompanyAssetsType().isEmpty()) {
				    List<CompanyAssetsList> existingCompanyAssetList = existingCompanyAssest.getCompanyAssetsType();
				    List<CompanyAssetsList> updatedCompanyAssetList = updatedCompanyAssets.getCompanyAssetsType();

				    // Remove entries that are present in existingCompanyAssetList but not in updatedCompanyAssetList
				    existingCompanyAssetList.removeIf(existingList ->
				            updatedCompanyAssetList.stream()
				                    .noneMatch(updatedList ->
				                            existingList.getCompanyAssetsListId().equals(updatedList.getCompanyAssetsListId())));

				    for (CompanyAssetsList updatedList : updatedCompanyAssetList) {
				        Optional<CompanyAssetsList> existingListOptional = existingCompanyAssetList.stream()
				                .filter(companyAssetsList -> {
				                    Long existingListId = companyAssetsList.getCompanyAssetsListId();
				                    Long updatedListId = updatedList.getCompanyAssetsListId();

				                    // Check for null before invoking equals
				                    return existingListId != null && existingListId.equals(updatedListId);
				                })
				                .findFirst();

				        if (existingListOptional.isPresent()) {
				            // Existing entry found, update it
				            CompanyAssetsList existingList = existingListOptional.get();
				            existingList.setCount(updatedList.getCount() != 0 ? updatedList.getCount() : existingList.getCount());
				            existingList.setBrandId(updatedList.getBrandId() != 0 ? updatedList.getBrandId() : existingList.getBrandId());
				            existingList.setAccessoriesId(updatedList.getAccessoriesId() != null ? updatedList.getAccessoriesId()
				                    : existingList.getAccessoriesId());
				        } else {
				            // New entry, add it to the existingCompanyAssest
				            existingCompanyAssest.getCompanyAssetsType().add(updatedList);
				        }
				    }
				}

				existingCompanyAssest.setStatus(updatedCompanyAssets.isStatus());

				service.save(existingCompanyAssest);

				return ResponseEntity.ok(existingCompanyAssest);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			String errorMessage = "An error occurred while updating server details.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}
	@GetMapping("/assets/balance")
	public List<Map<String, Object>> allDeptDetailspreviousYearincome(@RequestParam(required = true) String balance) {
	    try {
	        if ("balance".equals(balance)) {
	            return companyAssetsRepository.yearlyExpenseexpanceprevioustypeincome();
	        } else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();
	    }
	}
	
	
	@GetMapping("/companyassets/report")
	public List<Map<String, Object>> allDeptDetailspreviousYearincome1(@RequestParam(required = true) String companyassets) {
	    try {
	        if ("report".equals(companyassets)) {
	            return companyAssetsRepository.getAllReceiptDetailsWithClientIdAndMonthalter();
	        } else {	       
	            return Collections.emptyList();
	        }
	    } catch (Exception e) {	        
	        e.printStackTrace(); 
	        return Collections.emptyList();
	    }
	}
	
	@PostMapping("/companyassets/manager")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndYearOrMonth(
	        @RequestBody Map<String, Object> requestBody) {

	    if (!requestBody.containsKey("choose")) {
	        return ResponseEntity.badRequest().build();
	    }

	    String choose = requestBody.get("choose").toString();

	    switch (choose) {
	        case "date":
	            if (requestBody.containsKey("startDate") && requestBody.containsKey("endDate")) {
	                LocalDate startDate = LocalDate.parse(requestBody.get("startDate").toString(), DateTimeFormatter.ISO_DATE);
	                LocalDate endDate = LocalDate.parse(requestBody.get("endDate").toString(), DateTimeFormatter.ISO_DATE);
	                return ResponseEntity.ok(companyAssetsRepository.getAllpromotionsBetweenDates(startDate, endDate));
	            }
	            break;

	        case "year":
	            if (requestBody.containsKey("year")) {
	                String year = requestBody.get("year").toString();
	                List<Map<String, Object>> leaveData = companyAssetsRepository.getAllReceiptDetailsWithClientIdAnd(year);
	                return ResponseEntity.ok(leaveData);
	            }
	            break;

	        case "month":
	            if (requestBody.containsKey("monthName")) {
	                String monthName = requestBody.get("monthName").toString();
	                List<Map<String, Object>> leaveData1 = companyAssetsRepository.getAllReceiptDetailsWithClientIdAndMonth(monthName);
	                return ResponseEntity.ok(leaveData1);
	            }
	            break;

	        default:
	            return ResponseEntity.badRequest().build();
	    }

	    return ResponseEntity.badRequest().build();
	}

}
