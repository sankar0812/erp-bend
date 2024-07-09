package com.example.erp.controller.eRecruitment;


import java.util.List;

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

import com.example.erp.entity.erecruitment.Application;
import com.example.erp.repository.erecruitment.ApplicationRepository;
import com.example.erp.service.eRecruitment.ApplicationService;


@RestController
@CrossOrigin
public class ApplicationController {
	@Autowired
	private ApplicationService service;
	@Autowired
	private ApplicationRepository repo;
	
	@GetMapping("/application")
	public ResponseEntity<?> getApplications(@RequestParam(required = true) String Application) {
	    try {
	        if ("application".equals(Application)) {
	            List<Application> application = service.listAll();
	            return ResponseEntity.ok(application);
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("The provided leave is not supported.");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("Error retrieving Applications: " + e.getMessage());
	    }
	}



	
	@PostMapping("/application/save")
	public ResponseEntity<String> saveApplication(@RequestBody Application application) {
		try {
			application.setStatus(true);
			service.saveOrUpdate(application);
			
			return ResponseEntity.ok("Application saved with id: " + application.getApplicationId());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving Application: " + e.getMessage());
		}
	}
	
	


	@PutMapping("/application/status/{id}")
	public ResponseEntity<?> getApplicationById(@PathVariable(name = "id") long id) {
		try {
			Application Application = service.getById(id);
			if (Application != null) {
				 boolean currentStatus = Application.isStatus();
				 Application.setStatus(!currentStatus);
	                service.saveOrUpdate(Application); // Save the updated complaints
	            } else {
	                return ResponseEntity.ok(false); // Complaints with the given ID does not exist, return false
	            }

	            return ResponseEntity.ok(Application.isStatus()); // Return the new status (true or false)
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                    .body(false); // Set response to false in case of an error
	        }
	    }

	@PutMapping("/application/edit/{id}")
	public ResponseEntity<?> updateApplication(@PathVariable("id") long id, @RequestBody Application application) {
		try {
			
			
			Application existingApplication = service.getById(id);
			if (existingApplication == null) {
				return ResponseEntity.notFound().build();
			}
			existingApplication.setJobTitle(application.getJobTitle());
	

			
			
			service.saveOrUpdate(existingApplication);
			return ResponseEntity.ok(existingApplication);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/applicationf/delete/{id}")
	public ResponseEntity<String> deleteApplication(@PathVariable("id") long id) {
		try {
			service.deleteById(id);
			return ResponseEntity.ok("Application deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting Application: " + e.getMessage());
		}
	}

}
