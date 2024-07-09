package com.example.erp.controller.employee;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.erp.entity.employee.EmployeeRole;
import com.example.erp.service.employee.EmployeeRoleService;

@RestController
@CrossOrigin
public class EmployeeRoleController {

	@Autowired
	private EmployeeRoleService  EmployeeRoleService;
	
	@GetMapping("/employeerole")
	public ResponseEntity<?> getDetails() {
		try {
			Iterable<EmployeeRole> EmployeeRoleDetails = EmployeeRoleService.listAll();
			return new ResponseEntity<>(EmployeeRoleDetails, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving Employees: " + e.getMessage());
		}
	}

	
//	@GetMapping("/EmployeeRole")
//	public ResponseEntity<CustomResponse> getDetails() {
//	    try {
//	        Iterable<EmployeeRole> EmployeeRoleDetails = EmployeeRoleService.listAll();
//	        CustomResponse response = new CustomResponse(true, EmployeeRoleDetails);
//	        return new ResponseEntity<>(response, HttpStatus.OK);
//	    } catch (Exception e) {
//	        CustomResponse response = new CustomResponse(false, "Error retrieving EmployeeRoles: " + e.getMessage());
//	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//	    }
//	}




	@PostMapping("/employeerole/save")
	public ResponseEntity<?> saveEmployeeRole(@RequestBody EmployeeRole EmployeeRole) {
		try {
			EmployeeRoleService.SaveorUpdate(EmployeeRole);
			long id = EmployeeRole.getEmployeeRoleId();
			return ResponseEntity.status(HttpStatus.OK).body("EmployeeRole details saved successfully."  + id);
		} catch (Exception e) {
			String errorMessage = "An error occurred while saving EmployeeRole details.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}


	@PutMapping("/employeerole/edit/{employeerole}")
	public ResponseEntity<EmployeeRole> updateEmployeeRoleId(@PathVariable("employeerole") Long EmployeeRoleId, @RequestBody EmployeeRole EmployeeRoleIdDetails) {
		try {
			EmployeeRole existingEmployeeRole = EmployeeRoleService.findById(EmployeeRoleId);
			if (existingEmployeeRole == null) {
				return ResponseEntity.notFound().build();
			}
			existingEmployeeRole.setPayrolDate(EmployeeRoleIdDetails.getPayrolDate());
			existingEmployeeRole.setWorkingDays(EmployeeRoleIdDetails.getWorkingDays());
			existingEmployeeRole.setWorkingHour(EmployeeRoleIdDetails.getWorkingHour());
		
		
			EmployeeRoleService.save(existingEmployeeRole);
			return ResponseEntity.ok(existingEmployeeRole);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/employeerole/delete/{employeerole}")
	public ResponseEntity<String> deletEmployeeRoleName(@PathVariable("employeerole") Long EmployeeRoleId) {
		EmployeeRoleService.deleteById(EmployeeRoleId);
		return ResponseEntity.ok("EmployeeRole deleted successfully");
	}
}
