package com.example.erp.controller.employee;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

import com.example.erp.entity.clientDetails.ClientInvoiceList;
import com.example.erp.entity.employee.Bank;
import com.example.erp.repository.employee.BankRepository;
import com.example.erp.service.employee.BankService;

@RestController
@CrossOrigin
public class BankController {

	@Autowired
	private BankService Service;
	
	@Autowired
	private BankRepository repository;

	@GetMapping("/bank1")
	public ResponseEntity<?> getDetails(@RequestParam(required = true) String bankParam) {
	    try {
	        if ("bank".equals(bankParam)) {
	            Iterable<Bank> departmentDetails = Service.listAll();
	            return new ResponseEntity<>(departmentDetails, HttpStatus.OK);
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided bank is not supported.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error retrieving bank details: " + e.getMessage());
	    }
	}



	@PostMapping("/bank/save")
	public ResponseEntity<?> saveDepartment(@RequestBody Bank bank) {
		try {
			Service.SaveorUpdate(bank);
			long id = bank.getBankId();
			return ResponseEntity.status(HttpStatus.OK).body("Department details saved successfully."  + id);
		} catch (Exception e) {
			String errorMessage = "An error occurred while saving Department details.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}
	
	@GetMapping("/bank/{id}/{roleId}")
	public Map<String, Object> getAllAttendanceDetails(@PathVariable("id") Long employeeId,@PathVariable("roleId") Long roleId) {
	    return repository.attendanceForTraineeIdAndRoleId(employeeId,roleId);
	}

	@RequestMapping("/bank/{bankId}")
	private Optional<?> getDesignation(@PathVariable(name = "bankId") long bankId) {
		return Service.getDesignationById(bankId);

	}
	@PutMapping("/bank/edit/{bankId}")
	public ResponseEntity<?> updateDepartmentId(@PathVariable("bankId") Long BankId, @RequestBody Bank DepartmentIdDetails) {
		try {
			Bank existingDepartment = Service.findById(BankId);
			if (existingDepartment == null) {
				return ResponseEntity.notFound().build();
			}
			
			String pan =existingDepartment.getPanNumber();
//			 Optional<Bank> existingInvoice = repository.findByPanNumber(pan);
//
//             if (existingInvoice.isPresent()) {
//                 return ResponseEntity.badRequest().body("pan number" + pan + " already exists.");
//             }
         
			  String accountNumber = DepartmentIdDetails.getAccountNumber();
		        if (accountNumber.length() < 10 || accountNumber.length() > 18) {
		            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                .body("Account number must be between 10 and 18 digits long.");
		        }

			existingDepartment.setBankName(DepartmentIdDetails.getBankName());
			existingDepartment.setAccountNumber(DepartmentIdDetails.getAccountNumber());
			existingDepartment.setBranchName(DepartmentIdDetails.getBranchName());
			existingDepartment.setHolderName(DepartmentIdDetails.getHolderName());
			existingDepartment.setPanNumber(DepartmentIdDetails.getPanNumber());
			existingDepartment.setIfseCode(DepartmentIdDetails.getIfseCode());
			Service.save(existingDepartment);
			return ResponseEntity.ok(existingDepartment);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PutMapping("/bank/edit/employee/{id}/{roleId}")
	public ResponseEntity<Bank> updateBank(@PathVariable("id") Long employeeId,@PathVariable("roleId") Long roleId, @RequestBody Bank DepartmentIdDetails) {
		try {
			Bank existingDepartment = Service.getByEmployeeId(employeeId,roleId);
			if (existingDepartment == null) {
				return ResponseEntity.notFound().build();
			}
			existingDepartment.setBankName(DepartmentIdDetails.getBankName());
			existingDepartment.setAccountNumber(DepartmentIdDetails.getAccountNumber());
			existingDepartment.setBranchName(DepartmentIdDetails.getBranchName());
			existingDepartment.setHolderName(DepartmentIdDetails.getHolderName());
			existingDepartment.setPanNumber(DepartmentIdDetails.getPanNumber());
			existingDepartment.setIfseCode(DepartmentIdDetails.getIfseCode());
			Service.save(existingDepartment);
			return ResponseEntity.ok(existingDepartment);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/bank/delete/{bankId}")
	public ResponseEntity<String> deletDepartmentName(@PathVariable("bankId") Long bankId) {
		Service.deleteById(bankId);
		return ResponseEntity.ok("bank deleted successfully");
	}
}
