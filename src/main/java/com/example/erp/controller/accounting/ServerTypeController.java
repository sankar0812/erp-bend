package com.example.erp.controller.accounting;

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

import com.example.erp.entity.accounting.ServerType;
import com.example.erp.entity.employee.Department;
import com.example.erp.service.accounting.ServerTypeService;
import com.example.erp.service.employee.DepartmentService;

@RestController
@CrossOrigin
public class ServerTypeController {

	@Autowired
	private ServerTypeService departmentService;
	
	

	@GetMapping("/serverType")
	public ResponseEntity<?> getDetails(@RequestParam(required = true) String serverTypeParam) {
	    try {
	        if ("serverType".equals(serverTypeParam)) {
	            Iterable<ServerType> departmentDetails = departmentService.listAll();
	            return new ResponseEntity<>(departmentDetails, HttpStatus.OK);
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided serverType is not supported.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error retrieving serverType details: " + e.getMessage());
	    }
	}


	@PostMapping("/serverType/save")
	public ResponseEntity<?> saveDepartment(@RequestBody ServerType serverType) {
		try {
			departmentService.SaveorUpdate(serverType);
			long id = serverType.getServerTypeId();
			return ResponseEntity.status(HttpStatus.OK).body("serverType details saved successfully."  + id);
		} catch (Exception e) {
			String errorMessage = "An error occurred while saving ServerType details.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}


	@PutMapping("/serverType/edit/{serverTypeId}")
	public ResponseEntity<ServerType> updateDepartmentId(@PathVariable("serverTypeId") Long ServerTypeId, @RequestBody ServerType serverType) {
		try {
			ServerType existingDepartment = departmentService.findById(ServerTypeId);
			if (existingDepartment == null) {
				return ResponseEntity.notFound().build();
			}
			existingDepartment.setServerTypeName(serverType.getServerTypeName());
			departmentService.save(existingDepartment);
			return ResponseEntity.ok(existingDepartment);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/serverType/delete/{serverTypeId}")
	public ResponseEntity<String> deletDepartmentName(@PathVariable("serverTypeId") Long DepartmentId) {
		departmentService.deleteById(DepartmentId);
		return ResponseEntity.ok("ServerType deleted successfully");
	}
}
