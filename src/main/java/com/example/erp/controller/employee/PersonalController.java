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

import com.example.erp.entity.employee.Personal;
import com.example.erp.repository.employee.PersonalRepository;
import com.example.erp.service.employee.PersonalService;

@RestController
@CrossOrigin
public class PersonalController {

	@Autowired
	private PersonalService Service;
	
	@Autowired
	private PersonalRepository personalRepository;

	@GetMapping("/personal")
	public ResponseEntity<?> getDetails(@RequestParam(required = true) String PersonalParam) {
		try {
			if ("Personal".equals(PersonalParam)) {
				Iterable<Personal> departmentDetails = Service.listAll();
				return new ResponseEntity<>(departmentDetails, HttpStatus.OK);
			} else {
				// Handle the case when the provided department is not supported
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided Personal is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving Personal details: " + e.getMessage());
		}
	}
	
	
	@GetMapping("/personal/{id}/{roleId}")
	public Map<String, Object> getAllAttendanceDetails(@PathVariable("id") Long employeeId,@PathVariable("roleId") Long roleId) {
	    return personalRepository.attendanceForTraineeIdAndRoleId(employeeId,roleId);
	}

	@RequestMapping("/personal/{id}")
	private Optional<?> getDesignation(@PathVariable("id") long personalId) {
		return Service.getDesignationById(personalId);

	}

	@PostMapping("/personal/save")
	public ResponseEntity<?> saveDepartment(@RequestBody Personal Personal) {
		try {
			Service.SaveorUpdate(Personal);
			long id = Personal.getPersonalId();
			return ResponseEntity.status(HttpStatus.OK).body("personal details saved successfully." + id);
		} catch (Exception e) {
			String errorMessage = "An error occurred while saving Department details.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@PutMapping("/persona1l/edit/{PersonalId}")
	public ResponseEntity<Personal> updateDepartmentId(@PathVariable("PersonalId") Long employeeId,
			@RequestBody Personal DepartmentIdDetails) {
		try {
			Personal existingDepartment = Service.getByEmployeeId(employeeId);
			if (existingDepartment == null) {
				return ResponseEntity.notFound().build();
			}
			existingDepartment.setNationality(DepartmentIdDetails.getNationality());
			existingDepartment.setMarried(DepartmentIdDetails.getMarried());
			existingDepartment.setPassportExpDate(DepartmentIdDetails.getPassportExpDate());
			existingDepartment.setNoOfChildren(DepartmentIdDetails.getNoOfChildren());
			existingDepartment.setPassportNo(DepartmentIdDetails.getPassportNo());
			existingDepartment.setReligion(DepartmentIdDetails.getReligion());
			existingDepartment.setTel(DepartmentIdDetails.getTel());
			Service.SaveorUpdate(existingDepartment);
			return ResponseEntity.ok(existingDepartment);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/personal/edit/{PersonalId}")
	public ResponseEntity<Personal> updateDepartmentIdu8ser(@PathVariable("PersonalId") Long userId,
			@RequestBody Personal DepartmentIdDetails) {
		try {
			Personal existingDepartment = Service.findById(userId);
			if (existingDepartment == null) {
				return ResponseEntity.notFound().build();
			}
			existingDepartment.setNationality(DepartmentIdDetails.getNationality());
			existingDepartment.setMarried(DepartmentIdDetails.getMarried());
			existingDepartment.setPassportExpDate(DepartmentIdDetails.getPassportExpDate());
			existingDepartment.setNoOfChildren(DepartmentIdDetails.getNoOfChildren());
			existingDepartment.setPassportNo(DepartmentIdDetails.getPassportNo());
			existingDepartment.setReligion(DepartmentIdDetails.getReligion());
			existingDepartment.setTel(DepartmentIdDetails.getTel());
			Service.SaveorUpdate(existingDepartment);
			return ResponseEntity.ok(existingDepartment);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	@DeleteMapping("/personal/delete/{PersonalId}")
	public ResponseEntity<String> deletDepartmentName(@PathVariable("PersonalId") Long PersonalId) {
		Service.deleteById(PersonalId);
		return ResponseEntity.ok("personal deleted successfully");
	}
}