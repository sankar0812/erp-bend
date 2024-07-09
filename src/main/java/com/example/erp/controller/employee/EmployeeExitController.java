package com.example.erp.controller.employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

import com.example.erp.entity.accounting.CompanyAssetsList;
import com.example.erp.entity.employee.AssetsList;
import com.example.erp.entity.employee.CompanyPropertyList;
import com.example.erp.entity.employee.Employee;
import com.example.erp.entity.employee.EmployeeExit;
import com.example.erp.repository.employee.AssetsListRepository;
import com.example.erp.repository.employee.CompanyPropertyListRepository;
import com.example.erp.repository.employee.EmployeeExitRepository;
import com.example.erp.service.accounting.CompanyAssestListService;
import com.example.erp.service.employee.EmployeeExitService;
import com.example.erp.service.employee.EmployeeService;

@RestController
@CrossOrigin
public class EmployeeExitController {

	@Autowired
	private EmployeeExitService service;
	@Autowired
	private EmployeeExitRepository repo;
	
	@Autowired
	private CompanyAssestListService companyAssestListService;
	
	@Autowired
	private AssetsListRepository assetsListRepository;

	
	@Autowired
	private AssetsListRepository companyPropertyListRepository;
	@GetMapping("/employeeexit")
	public ResponseEntity<?> getEmployeeExits(@RequestParam(required = true) String exitParam) {
		try {
			if ("employeeexit".equals(exitParam)) {
				List<EmployeeExit> employeeExits = service.listAll();
				return ResponseEntity.ok(employeeExits);
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("The provided departmentParam is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving EmployeeExits: " + e.getMessage());
		}
	}

	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/employeeexit1/save")
	public ResponseEntity<String> saveEmployeeExit(@RequestBody EmployeeExit employeeExit) {
		try {
			employeeExit.setStatus(false);
			employeeExit.setExitType(null);
			Long employeeId = employeeExit.getEmployeeId();
			Employee employee = employeeService.getEmployeeById(employeeId);
			if (employee != null) {
				employee.setStatus(false);
				employeeService.saveOrUpdate(employee);
			}
			service.saveOrUpdate(employeeExit);

			return ResponseEntity.ok("EmployeeExit saved with id: " + employeeExit.getEmployeeExitId());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving EmployeeExit: " + e.getMessage());
		}
	}
	
	@PostMapping("/employeeexit/save")
	public ResponseEntity<?> saveEmployeeExit1(@RequestBody EmployeeExit employeeExit) {
	    try {
	        employeeExit.setStatus(false);
	        List<CompanyPropertyList> propertyList = employeeExit.getCompanyProperty();
	        for(CompanyPropertyList propertyLoop : propertyList) {
	        	int count = propertyLoop.getCount();
	        	int returnCount = propertyLoop.getReturnCount();
	        	int balance2= propertyLoop.getBalanceCount();
	        	propertyLoop.setBalanceCount(count - returnCount);
	        	propertyLoop.setBalanceCountt(balance2);
	        }

	        Long employeeId = employeeExit.getEmployeeId();
	        Employee employee = employeeService.getEmployeeById(employeeId);
	        
	        if (employee != null) {
	        	employeeExit.setExitType("Exit");
	            employee.setStatus(false);
	            employeeService.saveOrUpdate(employee);

	            List<CompanyPropertyList> assetsList = employeeExit.getCompanyProperty();
	            
	            if (assetsList != null) {
	                for (CompanyPropertyList asset : assetsList) {
	                    Long brandId = asset.getBrandId();
	                    Long accessoriesId = asset.getAccessoriesId();
	                    Integer count = asset.getReturnCount();
	                    
	                    List<AssetsList> assets = assetsListRepository.findAllByAccessoriesIdAndBrandId(accessoriesId, brandId);
	                    
	                    
	                    if (assets != null && !assets.isEmpty()) {
	                        if (assets.size() > 1) {
	                            System.out.println("Warning: Multiple existing assets found for brandId " + brandId + " and accessoriesId " + accessoriesId);
	                        }

	                        for (AssetsList existingCompanyAssets : assets) {
	                            existingCompanyAssets.setCount(existingCompanyAssets.getCount() - count);
	                        }
	                    }
	                    List<CompanyAssetsList> existingCompanyAssetsList = companyAssestListService
	                            .findAllByAccessoriesIdAndBrandId(accessoriesId, brandId);

	                    if (existingCompanyAssetsList != null && !existingCompanyAssetsList.isEmpty()) {
	                        if (existingCompanyAssetsList.size() > 1) {
	                            System.out.println("Warning: Multiple existing assets found for brandId " + brandId + " and accessoriesId " + accessoriesId);
	                        }

	                        for (CompanyAssetsList existingCompanyAssets : existingCompanyAssetsList) {
	                            existingCompanyAssets.setCount(existingCompanyAssets.getCount() + count);
	                        }
	                    }
	                }

	                // Move the saveOrUpdate outside the loop
	                service.saveOrUpdate(employeeExit);
	            }
	            

	            return ResponseEntity.ok("EmployeeExit saved with id: " + employeeExit.getEmployeeExitId());
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error saving EmployeeExit: " + e.getMessage());
	    }
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error saving EmployeeExit111: ");
	}


	@PutMapping("/employeeexit/or/{id}")
	public ResponseEntity<?> getEmployeeExitById(@PathVariable(name = "id") long id) {
		try {
			EmployeeExit EmployeeExit = service.getById(id);
			if (EmployeeExit != null) {
				boolean currentStatus = EmployeeExit.isStatus();
				EmployeeExit.setStatus(!currentStatus);
				service.saveOrUpdate(EmployeeExit);
			} else {
				return ResponseEntity.ok(false);
			}
			return ResponseEntity.ok(EmployeeExit.isStatus());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}

	@PutMapping("/employeeexit/edit/{id}")
	public ResponseEntity<?> updateEmployeeExit(@PathVariable Long id, @RequestBody EmployeeExit employeeExit) {
	    try {
	        // Check if the employee exit with the given ID exists
	        Optional<EmployeeExit> existingEmployeeExitOptional = service.getfingid(id);

	        if (existingEmployeeExitOptional.isPresent()) {
	            EmployeeExit existingEmployeeExit = existingEmployeeExitOptional.get();

	            // Update properties of the existing employee exit
	            existingEmployeeExit.setStatus(false);
//	            List<CompanyPropertyList> propertyList = existingEmployeeExit.getCompanyProperty();
//
//	            for (CompanyPropertyList propertyLoop : propertyList) {
//	                int count = propertyLoop.getCount();
//	                int returnCount = propertyLoop.getReturnCount();
//	                propertyLoop.setBalanceCount(count - returnCount);
//	            }

	            Long employeeId = existingEmployeeExit.getEmployeeId();
	            Employee employee = employeeService.getEmployeeById(employeeId);

	            if (employee != null) {
	                employee.setStatus(false);
	                employeeService.saveOrUpdate(employee);

	                List<CompanyPropertyList> assetsList = existingEmployeeExit.getCompanyProperty();

	                if (assetsList != null) {
	                    for (CompanyPropertyList asset : assetsList) {
	                        Long brandId = asset.getBrandId();
	                        Long accessoriesId = asset.getAccessoriesId();
	                        Integer count = asset.getBalanceCount();

	                        // Assuming you have a repository named assetsListRepository for AssetsList
	                        List<AssetsList> assets = assetsListRepository.findAllByAccessoriesIdAndBrandId(accessoriesId, brandId);

	                        if (assets != null && !assets.isEmpty()) {
	                            if (assets.size() > 1) {
	                                System.out.println("Warning: Multiple existing assets found for brandId " + brandId + " and accessoriesId " + accessoriesId);
	                            }

	                            for (AssetsList existingCompanyAssets : assets) {
	                                existingCompanyAssets.setCount(existingCompanyAssets.getCount() - count);
	                            }
	                        }

	                        // Assuming you have a service named companyAssestListService for CompanyAssetsList
	                        List<CompanyAssetsList> existingCompanyAssetsList = companyAssestListService
	                                .findAllByAccessoriesIdAndBrandId(accessoriesId, brandId);

	                        if (existingCompanyAssetsList != null && !existingCompanyAssetsList.isEmpty()) {
	                            if (existingCompanyAssetsList.size() > 1) {
	                                System.out.println("Warning: Multiple existing assets found for brandId " + brandId + " and accessoriesId " + accessoriesId);
	                            }

	                            for (CompanyAssetsList existingCompanyAssets : existingCompanyAssetsList) {
	                                existingCompanyAssets.setCount(existingCompanyAssets.getCount() + count);
	                            }
	                        }
	                    }

	                    // Move the saveOrUpdate outside the loop
	                    service.saveOrUpdate(existingEmployeeExit);

	                    return ResponseEntity.ok("EmployeeExit updated with id: " + id);
	                }
	            }
	        } else {
	        	
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("EmployeeExit not found with id: " + id);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error updating EmployeeExit: " + e.getMessage());
	    }
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("Error updating EmployeeExit111");
	}

	@DeleteMapping("/employeeexit/delete/{id}")
	public ResponseEntity<String> deleteEmployeeExit(@PathVariable("id") long id) {
		try {
			service.deleteById(id);
			return ResponseEntity.ok("EmployeeExit deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting EmployeeExit: " + e.getMessage());
		}
	}



	
	@GetMapping("/employeeexit/view")
	public ResponseEntity<Object> getAllRoleByEmployee(@RequestParam(required = true) String viewParam) {
		if ("view".equals(viewParam)) {
			List<Map<String, Object>> serverList = new ArrayList<>();
			List<Map<String, Object>> serverRole = repo.getemployeeExit();
			Map<String, List<Map<String, Object>>> serverGroupMap = serverRole.stream()
					.collect(Collectors.groupingBy(action -> action.get("employee_exit_id").toString()));

			for (Entry<String, List<Map<String, Object>>> serverLoop : serverGroupMap.entrySet()) {
				Map<String, Object> serverMap = new HashMap<>();
				serverMap.put("employeeExitId", Long.parseLong(serverLoop.getKey()));
				serverMap.put("userName", serverLoop.getValue().get(0).get("user_name"));
				serverMap.put("employeeId", serverLoop.getValue().get(0).get("employee_id"));
				serverMap.put("date", serverLoop.getValue().get(0).get("date"));
				serverMap.put("departmentId", serverLoop.getValue().get(0).get("department_id"));
				serverMap.put("description", serverLoop.getValue().get(0).get("description"));
				serverMap.put("userId", serverLoop.getValue().get(0).get("user_id"));
				serverMap.put("departmentName", serverLoop.getValue().get(0).get("department_name"));
		
				int randomNumber = generateRandomNumber();
				String fileExtension = getFileExtensionForImage(serverLoop);
				String imageUrl = "profile/" + randomNumber + "/" +serverLoop.getValue().get(0).get("employee_id") + "." + fileExtension;
				serverMap.put("profile", imageUrl);
				List<Map<String, Object>> serverSubList = new ArrayList<>();
				for (Map<String, Object> serverSubLoop : serverLoop.getValue()) {

					Map<String, Object> serverSubMap = new HashMap<>();
					serverSubMap.put("companyPropertylistId", serverSubLoop.get("company_propertylist_id"));
					serverSubMap.put("accessoriesId", serverSubLoop.get("accessories_id"));
					serverSubMap.put("balanceCount", serverSubLoop.get("balance_count"));
					serverSubMap.put("balanceCountt", serverSubLoop.get("balance_countt"));
					serverSubMap.put("brandId", serverSubLoop.get("brand_id"));					
					serverSubMap.put("count", serverSubLoop.get("count"));
					serverSubMap.put("returnCount", serverSubLoop.get("return_count"));
					serverSubMap.put("serialNumber", serverSubLoop.get("serial_number"));	
					serverSubMap.put("brandName", serverSubLoop.get("brand_name"));		
					serverSubMap.put("accessoriesName", serverSubLoop.get("accessories_name"));
					serverSubList.add(serverSubMap);
				}
				serverMap.put("CompanyProperty", serverSubList);
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
	@GetMapping("/employeeexit/assets")
	public ResponseEntity<Object> getEmployeeExitAssets(@RequestParam(required = true) String employeeexit) {
		if ("assets".equals(employeeexit)) {
			List<Map<String, Object>> deptList = new ArrayList<>();
			List<Map<String, Object>> serverRole = repo.getAllServerDetails();
			Map<String, Map<String, Map<String, List<Map<String, Object>>>>> serverGroupMap = serverRole.stream()
				    .collect(Collectors.groupingBy(action -> getString(action, "department_id"),
				            Collectors.groupingBy(action -> getString(action, "employee_id"),
				                    Collectors.groupingBy(action -> getString(action, "assets_list_id")))));

			for (Entry<String, Map<String, Map<String, List<Map<String, Object>>>>> deptLoop : serverGroupMap
					.entrySet()) {
				Map<String, Object> deptMap = new HashMap<>();
				deptMap.put("departmentId", deptLoop.getKey());

				List<Map<String, Object>> employeeList = new ArrayList<>();
				for (Entry<String, Map<String, List<Map<String, Object>>>> empLoop : deptLoop.getValue().entrySet()) {
					Map<String, Object> empMap = new HashMap<>();
					empMap.put("employeeId", empLoop.getKey());
					String employeeId = empLoop.getKey();

					List<Map<String, Object>> assestList = new ArrayList<>();
					for (Entry<String, List<Map<String, Object>>> assestLoop : empLoop.getValue().entrySet()) {
						Map<String, Object> assestMap = new HashMap<>();
						assestMap.put("assetsListId", assestLoop.getKey());
						assestMap.put("brandName", assestLoop.getValue().get(0).get("brand_name"));
						assestMap.put("serialNumber", assestLoop.getValue().get(0).get("serial_number"));
						assestMap.put("brandId", assestLoop.getValue().get(0).get("brand_id"));
						assestMap.put("count", assestLoop.getValue().get(0).get("count"));
						assestMap.put("accessoriesName", assestLoop.getValue().get(0).get("accessories_name"));
						assestMap.put("accessoriesId", assestLoop.getValue().get(0).get("accessories_id"));
						assestMap.put("balanceCount", assestLoop.getValue().get(0).get("balance_count"));
						assestMap.put("returnCount", assestLoop.getValue().get(0).get("returncount"));

						empMap.put("userName", assestLoop.getValue().get(0).get("user_name"));
						String fileExtension = getFileExtensionForImage(assestLoop);
						int randomNumber = generateRandomNumber();
						String imageUrl = "profile/" + randomNumber + "/" + employeeId + "." + fileExtension;
						empMap.put("profile", imageUrl);

						deptMap.put("departmentName", assestLoop.getValue().get(0).get("department_name"));
						assestList.add(assestMap);
					}
					empMap.put("assets", assestList);
					employeeList.add(empMap);
				}
				deptMap.put("employees", employeeList);
				deptList.add(deptMap);

			}

			return ResponseEntity.ok(deptList);
		} else {
			String errorMessage = "Invalid value for 'employeeexit'. Expected 'assets'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
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
	
	
	
	@GetMapping("/employee/exit/{id}")
	public ResponseEntity<List<Map<String, Object>>> displayAllImages1(@PathVariable("id") Long employeeId) {
	    try {	       
	            List<Map<String, Object>> images = repo.AllDetailsandexit(employeeId);
	            List<Map<String, Object>> imageResponses = new ArrayList<>();

	            for (Map<String, Object> image : images) {
	                int randomNumber = generateRandomNumber();
	                String fileExtension = getFileExtensionForImage(image);
	            	String imageUrl = "company/" + randomNumber + "/" + image.get("company_id") + "." + fileExtension;
	                Map<String, Object> imageResponse = new HashMap<>();
	                imageResponse.put("profile", imageUrl);
	                imageResponse.put("designationName", image.get("designation_name"));
	                imageResponse.put("employeeId", image.get("employee_id"));
	                imageResponse.put("designationId", image.get("designation_id"));
	                imageResponse.put("companyId", image.get("company_id"));
	                imageResponse.put("companyName", image.get("company_name"));
	                imageResponse.put("date", image.get("date"));	              
	                imageResponse.put("userName", image.get("user_name")); 
	                imageResponse.put("noticeDate", image.get("noticeDate")); 
	                imageResponse.put("country", image.get("country")); 
	                imageResponse.put("email", image.get("email")); 
	                imageResponse.put("gstNo", image.get("gst_no"));
	                imageResponse.put("address", image.get("address"));	              
	                imageResponse.put("phoneNumber1", image.get("phone_number1")); 
	                imageResponse.put("phoneNumber2", image.get("phone_number2")); 
	                imageResponse.put("state", image.get("state")); 
	                imageResponse.put("pincode", image.get("pincode")); 
	                imageResponses.add(imageResponse);
	            }
	            return ResponseEntity.ok(imageResponses);
	        }catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}	 	 

	@PostMapping("/employeeexit/manager")
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
                List<Map<String, Object>> leaveData = repo.getAllemployeeexit(year);
                List<Map<String, Object>> imageResponses = new ArrayList<>();
	   	         
                for (Map<String, Object> image : leaveData) {
                    int randomNumber = generateRandomNumber();
                    
                    String fileExtension = getFileExtensionForImage(image);
                    String imageUrl1 = "profile/" + randomNumber + "/" + image.get("employee_id")+ "." + fileExtension;
                    Map<String, Object> imageResponse = new HashMap<>();
                    imageResponse.put("profile", imageUrl1);
                
                    imageResponse.putAll(image);
                    imageResponses.add(imageResponse);
                }
                return ResponseEntity.ok(imageResponses);
             
            }
            break;

        case "month":
            if (requestBody.containsKey("monthName")) {
                String monthName = requestBody.get("monthName").toString();
                List<Map<String, Object>> leaveData1 = repo.getAllemployeeexitemployee(monthName);
                List<Map<String, Object>> imageResponses = new ArrayList<>();
   	         
                for (Map<String, Object> image : leaveData1) {
                    int randomNumber = generateRandomNumber();
                    
                    String fileExtension = getFileExtensionForImage(image);
                    String imageUrl1 = "profile/" + randomNumber + "/" + image.get("employee_id")+ "." + fileExtension;
                    Map<String, Object> imageResponse = new HashMap<>();
                    imageResponse.put("profile", imageUrl1);
                
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
