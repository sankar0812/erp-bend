package com.example.erp.controller.employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.erp.entity.employee.Department;
import com.example.erp.repository.employee.DepartmentRepository;
import com.example.erp.service.employee.DepartmentService;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private DepartmentRepository departmentRepository;

	@GetMapping("/department")
	public ResponseEntity<?> getDetails(@RequestParam(required = true) String departmentParam) {
	    try {
	        if ("department".equals(departmentParam)) {
	            Iterable<Department> departmentDetails = departmentService.listAll();
	            return new ResponseEntity<>(departmentDetails, HttpStatus.OK);
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided department is not supported.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error retrieving department details: " + e.getMessage());
	    }
	}


	@PostMapping("/department/save")
	public ResponseEntity<?> saveDepartment(@RequestBody Department Department) {
		try {
			departmentService.SaveorUpdate(Department);
			long id = Department.getDepartmentId();
			return ResponseEntity.status(HttpStatus.OK).body("Department details saved successfully."  + id);
		} catch (Exception e) {
			String errorMessage = "An error occurred while saving Department details.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}


	@PutMapping("/department/edit/{DepartmentId}")
	public ResponseEntity<Department> updateDepartmentId(@PathVariable("DepartmentId") Long DepartmentId, @RequestBody Department DepartmentIdDetails) {
		try {
			Department existingDepartment = departmentService.findById(DepartmentId);
			if (existingDepartment == null) {
				return ResponseEntity.notFound().build();
			}
			existingDepartment.setDepartmentName(DepartmentIdDetails.getDepartmentName());
			existingDepartment.setColor(DepartmentIdDetails.getColor());
			departmentService.save(existingDepartment);
			return ResponseEntity.ok(existingDepartment);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/department/delete/{DepartmentId}")
	public ResponseEntity<String> deletDepartmentName(@PathVariable("DepartmentId") Long DepartmentId) {
		departmentService.deleteById(DepartmentId);
		return ResponseEntity.ok("Department deleted successfully");
}
	private int generateRandomNumber() {
		Random random = new Random();
		return random.nextInt(1000000); 
	}
	
	@GetMapping("/task/team/menber/{id}")
	public ResponseEntity<?> getTaskAssignedDetailsp(@PathVariable("id") Long department_id) {
	    try {
	        List<Map<String, Object>> tasks = departmentRepository.GetClientProjectWorkingMember(department_id);
	        List<Map<String, Object>> taskList = new ArrayList<>();

	        for (Map<String, Object> taskAssigned : tasks) {
	            Map<String, Object> taskAssignedMap = new HashMap<>();

	            int randomNumber = generateRandomNumber();
	            String fileExtension = getFileExtensionForImage(taskAssigned);

	            if (taskAssigned.containsKey("employee_id")) {
	                String imageUrl = "profile/" + randomNumber + "/" + taskAssigned.get("employee_id") + "."
	                        + fileExtension;
	                taskAssignedMap.put("profile", imageUrl);
	            } else if (taskAssigned.containsKey("trainee_id")) {
	                String imageUrl1 = "training/" + randomNumber + "/" + taskAssigned.get("trainee_id") ;
	                       
	                taskAssignedMap.put("profile", imageUrl1);
	            }

	            taskAssignedMap.putAll(taskAssigned);
	            taskList.add(taskAssignedMap);
	        }
	        return ResponseEntity.ok(taskList);
	    } catch (Exception e) {
	        String errorMessage = "Error occurred while retrieving task assigned details";
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Collections.singletonMap("error", errorMessage));
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
		} else {
			return "jpg";
		}
	}
	
}
