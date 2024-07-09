package com.example.erp.controller.employee;

import java.util.Map;
import java.util.Optional;

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

import com.example.erp.entity.employee.FamilyInformations;
import com.example.erp.repository.employee.FamilyInformationsRepository;
import com.example.erp.service.employee.FamilyInformationsService;

@RestController
@CrossOrigin
public class FamilyInformationsController {

	@Autowired
	private FamilyInformationsService Service;
	
	@Autowired
	private FamilyInformationsRepository familyInformationsRepository;

	@GetMapping("/family")
	public ResponseEntity<?> getDetails(@RequestParam(required = true) String FamilyInformationsParam) {
	    try {
	        if ("FamilyInformations".equals(FamilyInformationsParam)) {
	            Iterable<FamilyInformations> departmentDetails = Service.listAll();
	            return new ResponseEntity<>(departmentDetails, HttpStatus.OK);
	        } else {
	            // Handle the case when the provided department is not supported
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided FamilyInformations is not supported.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error retrieving FamilyInformations details: " + e.getMessage());
	    }
	}

	@GetMapping("/family/{id}/{roleId}")
	public Map<String, Object> getAllAttendanceDetails(@PathVariable("id") Long employeeId,@PathVariable("roleId") Long roleId) {
	    return familyInformationsRepository.attendanceForTraineeIdAndRoleId(employeeId,roleId);
	}

	@PostMapping("/family/save")
	public ResponseEntity<?> saveDepartment(@RequestBody FamilyInformations FamilyInformations) {
		try {
			Service.SaveorUpdate(FamilyInformations);
			long id = FamilyInformations.getFamilyInformationsId();
			return ResponseEntity.status(HttpStatus.OK).body("FamilyInformations details saved successfully."  + id);
		} catch (Exception e) {
			String errorMessage = "An error occurred while saving Department details.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}


	
	@RequestMapping("/family/{id}")
	private FamilyInformations getDesignation(@PathVariable("id") long familyInformationId) {
		return Service.getById(familyInformationId);

	}
	
	@PutMapping("/family/edit/{Id}")
	public ResponseEntity<FamilyInformations> updateDepartmentId(@PathVariable("Id") Long employeeId, @RequestBody FamilyInformations DepartmentIdDetails) {
		try {
			FamilyInformations existingDepartment = Service.getById(employeeId);
			if (existingDepartment == null) {
				return ResponseEntity.notFound().build();
			}
			existingDepartment.setDob(DepartmentIdDetails.getDob());
			existingDepartment.setName(DepartmentIdDetails.getName());
			existingDepartment.setPhone(DepartmentIdDetails.getPhone());
			existingDepartment.setRelationShip(DepartmentIdDetails.getRelationShip());
	
			Service.SaveorUpdate(existingDepartment);
			return ResponseEntity.ok(existingDepartment);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/family/delete/{familyId}")
	public ResponseEntity<String> deletDepartmentName(@PathVariable("FamilyInformationsId") Long FamilyInformationsId) {
		Service.deleteById(FamilyInformationsId);
		return ResponseEntity.ok("FamilyInformations deleted successfully");
	}}
