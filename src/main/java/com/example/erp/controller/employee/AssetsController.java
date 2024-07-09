package com.example.erp.controller.employee;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.NonUniqueResultException;

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
import com.example.erp.entity.accounting.CompanyAssetsList;
import com.example.erp.entity.employee.Assets;
import com.example.erp.entity.employee.AssetsList;
import com.example.erp.entity.employee.EmployeeAttendance;
import com.example.erp.entity.payroll.SalaryTypeList;
import com.example.erp.repository.employee.AssetsRepository;
import com.example.erp.service.accounting.CompanyAssestListService;
import com.example.erp.service.accounting.CompanyAssetsService;
import com.example.erp.service.employee.AssetsService;

@RestController
@CrossOrigin
public class AssetsController {

	@Autowired
	private AssetsService service;

	@Autowired
	private AssetsRepository repo;
	
	@Autowired
	private CompanyAssetsService companyAssetsService;

	@Autowired
	private CompanyAssestListService companyAssestListService;

	@GetMapping("/assets")
	public ResponseEntity<?> getDetails(@RequestParam(required = true) String assets) {
		try {
			if ("assetDetail".equals(assets)) {
				Iterable<Assets> assetDetails = service.listAll();
				List<Assets> sortedAssets = StreamSupport.stream(assetDetails.spliterator(), false)
						.sorted(Comparator.comparing(Assets::getAssetsId).reversed()).collect(Collectors.toList());

				return new ResponseEntity<>(sortedAssets, HttpStatus.OK);
			} else {

				String errorMessage = "The provided asset is not supported.";
				return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			String errorMessage = "An error occurred while retrieving asset details.";
			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/assets/save")
	public ResponseEntity<String> saveAssets(@RequestBody Assets assets) {
	    try {
	    	Optional<Assets> existingAttendance = repo
					.findByEmployeeId(assets.getEmployeeId());
	    	if (existingAttendance.isPresent()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("  employee ID already exists.");
			} 
	        List<AssetsList> assetsList = assets.getAssets();

	        if (assetsList != null) {
	            for (AssetsList asset : assetsList) {
	                Long brandId = asset.getBrandId();
	                Long accessoriesId = asset.getAccessoriesId();
	                Integer count = asset.getCount();
	                List<CompanyAssetsList> existingCompanyAssetsList = companyAssestListService
	                        .findAllByAccessoriesIdAndBrandId(accessoriesId, brandId);

	                if (existingCompanyAssetsList != null && !existingCompanyAssetsList.isEmpty()) {
	                    if (existingCompanyAssetsList.size() > 1) {
	                        System.out.println("Warning: Multiple existing assets found for brandId " + brandId + " and accessoriesId " + accessoriesId);
	                    }

	                    for (CompanyAssetsList existingCompanyAssets : existingCompanyAssetsList) {
	                        existingCompanyAssets.setCount(existingCompanyAssets.getCount() - count);
	                        companyAssestListService.SaveorUpdate(existingCompanyAssets);
	                    }
	                }
	            }

	            service.save(assets);
	            System.out.println("Saving assets: " + assets);

	            long assetsId = assets.getAssetsId();
	            return ResponseEntity.ok("Assets saved successfully. Assets ID: " + assetsId);
	        } else {
	            return ResponseEntity.badRequest().body("Error saving assets: assetsList is null");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error saving assets: " + e.getMessage());
	    }
	}




	@PutMapping("/assets/edit/{id}")
	public ResponseEntity<Assets> updatePayroll(@PathVariable("id") Long assetsId, @RequestBody Assets assets) {
		try {
			Assets existingSalaryType = service.findById(assetsId);
			if (existingSalaryType == null) {
				return ResponseEntity.notFound().build();
			}
			existingSalaryType.setAssetsDate(assets.getAssetsDate());
			existingSalaryType.setAssets(assets.getAssets());
			
			
			
			service.save(existingSalaryType);
			return ResponseEntity.ok(existingSalaryType);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/assets/delete/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable("id") long id) {
		try {
			service.deleteMemberById(id);
			return ResponseEntity.ok("Assets deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting Assets: " + e.getMessage());
		}
	}
	@GetMapping("/brand/accessories")
	public ResponseEntity<Object> getAllRoleByEmployee(@RequestParam(required = true) String accessories) {
		if ("brand".equals(accessories)) {
			List<Map<String, Object>> departmentList = new ArrayList<>();
			List<Map<String, Object>> departmentRole = repo.getAllRoleByEmployees();
			Map<String, List<Map<String, Object>>> departmentGroupMap = departmentRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("brand_id").toString()));

			for (Entry<String, List<Map<String, Object>>> departmentLoop : departmentGroupMap.entrySet()) {
				Map<String, Object> departmentMap = new HashMap<>();
				departmentMap.put("brandId", Long.parseLong(departmentLoop.getKey()));
				departmentMap.put("brandName", departmentLoop.getValue().get(0).get("brand_name"));
				List<Map<String, Object>> departmentSubList = new ArrayList<>();
				for (Map<String, Object> departmentsubLoop : departmentLoop.getValue()) {

					Map<String, Object> departmentSubMap = new HashMap<>();
					departmentSubMap.put("brandId", departmentsubLoop.get("brand_id"));
					departmentSubMap.put("brandName", departmentsubLoop.get("brand_name"));
					departmentSubMap.put("accessoriesId", departmentsubLoop.get("accessories_id"));
					departmentSubMap.put("accessoriesName", departmentsubLoop.get("accessories_name"));
					departmentSubMap.put("count", departmentsubLoop.get("count"));
					departmentSubList.add(departmentSubMap);
				}
				departmentMap.put("assetsDetails", departmentSubList);
				departmentList.add(departmentMap);
			}

			return ResponseEntity.ok(departmentList);
		} else {
			String errorMessage = "Invalid value for 'accessories'. Expected 'brand'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}
	
	
	@GetMapping("/assets/view")
	public ResponseEntity<Object> getAllRoleByEmplkjbchjbju(@RequestParam(required = true) String view) {
		if ("assets".equals(view)) {
			List<Map<String, Object>> serverList = new ArrayList<>();
			List<Map<String, Object>> serverRole = repo.getAllServerDetails();
			Map<String, List<Map<String, Object>>> serverGroupMap = serverRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("assets_id").toString()));

			for (Entry<String, List<Map<String, Object>>> serverLoop : serverGroupMap.entrySet()) {
				Map<String, Object> serverMap = new HashMap<>();
				serverMap.put("assetsId", Long.parseLong(serverLoop.getKey()));
				serverMap.put("assetsDate", serverLoop.getValue().get(0).get("assets_date"));
				serverMap.put("employeeId", serverLoop.getValue().get(0).get("employee_id"));
				serverMap.put("departmentId", serverLoop.getValue().get(0).get("department_id"));
				serverMap.put("userName", serverLoop.getValue().get(0).get("user_name"));
				serverMap.put("departmentName", serverLoop.getValue().get(0).get("department_name"));
				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(serverLoop);
				String imageUrl1 = "accessories/" + randomNumber + "/" + serverLoop.getValue().get(0).get("accessories_id");
				String imageUrl = "profile/" + randomNumber + "/" + serverLoop.getValue().get(0).get("employee_id") + "." + fileExtension;
				serverMap.put("profile", imageUrl);
				List<Map<String, Object>> serverSubList = new ArrayList<>();
				for (Map<String, Object> serverSubLoop : serverLoop.getValue()) {

					Map<String, Object> serverSubMap = new HashMap<>();
					serverSubMap.put("assetsListId", serverSubLoop.get("assets_list_id"));
					serverSubMap.put("accessoriesId", serverSubLoop.get("accessories_id"));
					serverSubMap.put("count", serverSubLoop.get("count"));
					serverSubMap.put("image", imageUrl1);
					serverSubMap.put("brandId", serverSubLoop.get("brand_id"));
					serverSubMap.put("accessoriesName", serverSubLoop.get("accessories_name"));
					serverSubMap.put("brandName", serverSubLoop.get("brand_name"));
					serverSubMap.put("serialNumber", serverSubLoop.get("serial_number"));
					
					BigInteger brandIdBigInteger = (BigInteger) serverSubLoop.get("brand_id");
					long brandId = brandIdBigInteger.longValue();

					BigInteger accessoriesIdBigInteger = (BigInteger) serverSubLoop.get("accessories_id");
					long accessoriesId = accessoriesIdBigInteger.longValue();

					List<Map<String, Object>> brandRole = repo.getAllRoleByEmployeesWithID(accessoriesId, brandId);
					Map<String, List<Map<String, Object>>> countGroupMap = brandRole.stream()
						    .collect(Collectors.groupingBy(action -> action.get("company_assets_id").toString()));

					List<Map<String, Object>> brandList = new ArrayList<>();
					for(Entry<String, List<Map<String, Object>>> countLoop : countGroupMap.entrySet()) {
						for (Map<String, Object> entry : countLoop.getValue()) {
//					        Map<String, Object> ob = new HashMap<>();
//					        ob.putAll(entry);
//					        brandList.add(ob);
					        
					        serverSubMap.put("balanceCount", entry.get("count"));
					    }
					}
					
					
					serverSubList.add(serverSubMap);
//					serverMap.put("countList", brandList);
				}
				serverMap.put("assets", serverSubList);
				serverList.add(serverMap);
			}

			return ResponseEntity.ok(serverList);
		} else {
			String errorMessage = "Invalid value for 'view'. Expected 'assets'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}
	@GetMapping("/assets/view/employee/{id}")
	public ResponseEntity<Object> getAllRoleByEmplkjbchjbjuview(@PathVariable ("id") long id) {
		
			List<Map<String, Object>> serverList = new ArrayList<>();
			List<Map<String, Object>> serverRole = repo.getAllServerDetailsemployeeId(id);
			Map<String, List<Map<String, Object>>> serverGroupMap = serverRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("assets_id").toString()));

			for (Entry<String, List<Map<String, Object>>> serverLoop : serverGroupMap.entrySet()) {
				Map<String, Object> serverMap = new HashMap<>();
				serverMap.put("assetsId", Long.parseLong(serverLoop.getKey()));
				serverMap.put("assetsDate", serverLoop.getValue().get(0).get("assets_date"));
				serverMap.put("employeeId", serverLoop.getValue().get(0).get("employee_id"));
				serverMap.put("departmentId", serverLoop.getValue().get(0).get("department_id"));
				serverMap.put("userName", serverLoop.getValue().get(0).get("user_name"));
				serverMap.put("departmentName", serverLoop.getValue().get(0).get("department_name"));
				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(serverLoop);
				
				String imageUrl = "profile/" + randomNumber + "/" + serverLoop.getValue().get(0).get("employee_id") + "." + fileExtension;
				serverMap.put("profile", imageUrl);
				List<Map<String, Object>> serverSubList = new ArrayList<>();
				for (Map<String, Object> serverSubLoop : serverLoop.getValue()) {
					
					Map<String, Object> serverSubMap = new HashMap<>();
					String imageUrl1 = "accessories/" + randomNumber + "/" + serverSubLoop.get("accessories_id");
					serverSubMap.put("assetsListId", serverSubLoop.get("assets_list_id"));
					serverSubMap.put("accessoriesId", serverSubLoop.get("accessories_id"));
					serverSubMap.put("count", serverSubLoop.get("count"));
					serverSubMap.put("brandId", serverSubLoop.get("brand_id"));
					serverSubMap.put("image", imageUrl1);
					serverSubMap.put("accessoriesName", serverSubLoop.get("accessories_name"));
					serverSubMap.put("brandName", serverSubLoop.get("brand_name"));
					serverSubMap.put("serialNumber", serverSubLoop.get("serial_number"));
					serverSubList.add(serverSubMap);
				}
				serverMap.put("assets", serverSubList);
				serverList.add(serverMap);
			}

			return ResponseEntity.ok(serverList);
		} 
	
	private String getFileExtensionForImage(Entry<String, List<Map<String, Object>>> serverLoop) {
	    if (serverLoop == null || serverLoop.getValue() == null || serverLoop.getValue().isEmpty()) {
	        return "jpg";
	    }

	    String url = (String) serverLoop.getValue().get(0).get("url");

	    if (url == null) {
	        return "jpg";
	    }

	    if (url.endsWith(".png")) {
	        return "png";
	    } else if (url.endsWith(".jpg")) {
	        return "jpg";
	    } else {
	        return "jpg";
	    }
	}


	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000);
	}
	
	@PostMapping("/employeeassets/manager")
	public ResponseEntity<List<Map<String, Object>>> getAllVoucherBetweenDatesAndYearOrMonth(
	        @RequestBody Map<String, Object> requestBody) {

	    if (!requestBody.containsKey("choose")) {
	        return ResponseEntity.badRequest().build();
	    }

	    String choose = requestBody.get("choose").toString();

	    switch (choose) {
        case "year":
            if (requestBody.containsKey("year")) {
                String year = requestBody.get("year").toString();
                List<Map<String, Object>> leaveData = repo.getAllAssetseYear(year);
                List<Map<String, Object>> imageResponses = new ArrayList<>();
	   	         
                for (Map<String, Object> image : leaveData) {
                    int randomNumber = generateRandomNumber();
                    
                    String fileExtension = getFileExtensionForImage(image);
                    String imageUrl2 = "accessories/" + randomNumber + "/" + image.get("accessoriesId");
                    String imageUrl1 = "profile/" + randomNumber + "/" + image.get("employeeId")+ "." + fileExtension;
                    Map<String, Object> imageResponse = new HashMap<>();
                    imageResponse.put("profile", imageUrl1);
                    imageResponse.put("image", imageUrl2);
                    imageResponse.putAll(image);
                    imageResponses.add(imageResponse);
                }
                return ResponseEntity.ok(imageResponses);
             
            }
            break;

        case "month":
            if (requestBody.containsKey("monthName")) {
                String monthName = requestBody.get("monthName").toString();
                List<Map<String, Object>> leaveData1 = repo.getAllassetse(monthName);
                List<Map<String, Object>> imageResponses = new ArrayList<>();
   	         
                for (Map<String, Object> image : leaveData1) {
                    int randomNumber = generateRandomNumber();
                    
                    String fileExtension = getFileExtensionForImage(image);
                    String imageUrl2 = "accessories/" + randomNumber + "/" + image.get("accessoriesId");
                    String imageUrl1 = "profile/" + randomNumber + "/" + image.get("employeeId")+ "." + fileExtension;
                    Map<String, Object> imageResponse = new HashMap<>();
                    imageResponse.put("profile", imageUrl1);
                    imageResponse.put("image", imageUrl2);
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
	@GetMapping("/employeeassets/report")
	public ResponseEntity<List<Map<String, Object>>> displayAllImages1(@RequestParam(required = true) String type) {
	    try {
	        if ("report".equals(type)) {
	            List<Map<String, Object>> images = repo.getAllassetsedetail();
	            List<Map<String, Object>> imageResponses = new ArrayList<>();

	            for (Map<String, Object> image : images) {
	                int randomNumber = generateRandomNumber();
	            	String fileExtension = getFileExtensionForImage(image);
	            	  String imageUrl2 = "accessories/" + randomNumber + "/" + image.get("accessoriesId");
	                    String imageUrl1 = "profile/" + randomNumber + "/" + image.get("employeeId")+ "." + fileExtension;
	                    Map<String, Object> imageResponse = new HashMap<>();
	                    imageResponse.put("profile", imageUrl1);
	                    imageResponse.put("image", imageUrl2);
	                imageResponse.putAll(image);
	                imageResponses.add(imageResponse);
	            }
	            return ResponseEntity.ok(imageResponses);
	        } else {	        
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}	 	 
	
	
}
