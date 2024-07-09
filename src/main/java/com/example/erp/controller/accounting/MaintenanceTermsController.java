package com.example.erp.controller.accounting;

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

import com.example.erp.entity.accounting.MaintenanceTerms;
import com.example.erp.service.accounting.AccessoriesService;
import com.example.erp.service.accounting.MaintenanceTermsService;

@RestController
@CrossOrigin
public class MaintenanceTermsController {

	@Autowired

	private MaintenanceTermsService keyboardBrandService;

	@GetMapping("/terms")
	public ResponseEntity<?> getDetails(@RequestParam(required = true) String terms) {
	    try {
	        if ("Maintenance".equals(terms)) {
			Iterable<MaintenanceTerms> assestDetails = keyboardBrandService.listAll();
			return new ResponseEntity<>(assestDetails, HttpStatus.OK);
		} 
	        else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided accessories is not supported.");
	        }
	    }catch (Exception e) {
	        
	        }
			String errorMessage = "An error occurred while retrieving l details.";
			return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	

	@PostMapping("/terms/save")
	public ResponseEntity<?> saveBank(@RequestBody MaintenanceTerms maintenance) {

		try {
			keyboardBrandService.save(maintenance);
			return ResponseEntity.status(HttpStatus.CREATED).body("keyboardBrand details saved successfully.");
		} catch (Exception e) {
			String errorMessage = "An error occurred while saving keyboardBrand details.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
		}
	}

	@RequestMapping("/terms/{id}")
	private Optional<MaintenanceTerms> getKeyboardBrand(@PathVariable(name = "id") long accessoriesId) {
		return keyboardBrandService.getKeyboardBrandById(accessoriesId);
	}

	@PutMapping("/terms/edit/{id}")
	public ResponseEntity<?> updateKeyboardBrand(@PathVariable("id") Long termsId,
			@RequestBody MaintenanceTerms terms) {
		try { 
			MaintenanceTerms existingBrand = keyboardBrandService.findById(termsId);
			if (existingBrand == null) {

				return ResponseEntity.notFound().build();
			}
			existingBrand.setTerms(terms.getTerms());
			keyboardBrandService.save(existingBrand);
			return ResponseEntity.ok(existingBrand);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/terms/delete/{id}")
	public ResponseEntity<?> deleteKeyboardBrand(@PathVariable("id") Long termsId) {
		keyboardBrandService.deleteKeyboardBrandById(termsId);
		return ResponseEntity.ok("Terms and Conditions deleted successfully");

	}
}