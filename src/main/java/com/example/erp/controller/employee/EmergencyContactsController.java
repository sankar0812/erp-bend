package com.example.erp.controller.employee;

import java.util.List;
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
import com.example.erp.entity.employee.EmergencyContacts;
import com.example.erp.repository.employee.EmergencyContactsRepository;
import com.example.erp.service.employee.EmergencyContactsService;

@RestController
@CrossOrigin
public class EmergencyContactsController {

	@Autowired
	private EmergencyContactsService service;
	
	
	@Autowired
	private EmergencyContactsRepository contactsRepository;
	@GetMapping("/emergencycontacts")
	public ResponseEntity<?> getEmergencyContacts(@RequestParam(required = true) String contacts) {
		try {
			if ("emergency".equals(contacts)) {
				List<EmergencyContacts> emergencyContacts = service.listAll();
				return ResponseEntity.ok(emergencyContacts);
			} else {
				// Handle the case when the provided parameter is not supported
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The provided parameter is not supported.");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error retrieving emergency contacts: " + e.getMessage());
		}
	}
	
	@GetMapping("/emergencycontacts/{id}/{roleId}")
	public Map<String, Object> getAllAttendanceDetails(@PathVariable("id") Long employeeId,@PathVariable("roleId") Long roleId) {
	    return contactsRepository.attendanceForTraineeIdAndRoleId(employeeId,roleId);
	}
	
	
	
	@PostMapping("/emergencycontacts/save")
	public ResponseEntity<String> saveEmergencyContacts(@RequestBody EmergencyContacts EmergencyContacts) {
		try {
			EmergencyContacts.setStatus(true);
			service.saveOrUpdate(EmergencyContacts);
			return ResponseEntity.ok("EmergencyContacts saved with id: " + EmergencyContacts.getEmergencyContactsId());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving EmergencyContacts: " + e.getMessage());
		}
	}

	@RequestMapping("/emergencycontacts/{emergencycontactsId}")
	public Optional<?> getDesignation(@PathVariable(name = "emergencycontactsId") long emergencycontactsId) {
		return service.getDesignationById(emergencycontactsId);

	}

	@PutMapping("/emergencycontacts/or/{id}")
	public ResponseEntity<?> getEmergencyContactsById(@PathVariable(name = "id") long id) {
		try {
			EmergencyContacts EmergencyContacts = service.getByEmployeeId(id);
			if (EmergencyContacts != null) {
				boolean currentStatus = EmergencyContacts.isStatus();
				EmergencyContacts.setStatus(!currentStatus);
				service.saveOrUpdate(EmergencyContacts); // Save the updated complaints
			} else {
				return ResponseEntity.ok(false); // Complaints with the given ID does not exist, return false
			}

			return ResponseEntity.ok(EmergencyContacts.isStatus());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
		}
	}

	@PutMapping("/emergencycontacts/edit/{id}")
	public ResponseEntity<EmergencyContacts> updateEmergencyContacts(@PathVariable("id") long id,
			@RequestBody EmergencyContacts EmergencyContacts) {
		try {
			EmergencyContacts existingEmergencyContacts = service.getByEmployeeId1(id);
			if (existingEmergencyContacts == null) {
				return ResponseEntity.notFound().build();
			}
			existingEmergencyContacts.setAddress(EmergencyContacts.getAddress());
//			existingEmergencyContacts.setEmployeeId(EmergencyContacts.getEmployeeId());
			existingEmergencyContacts.setRelatinoName(EmergencyContacts.getRelatinoName());
			existingEmergencyContacts.setPhoneNumber(EmergencyContacts.getPhoneNumber());

			service.saveOrUpdate(existingEmergencyContacts);
			return ResponseEntity.ok(existingEmergencyContacts);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/emergencycontacts/delete/{id}")
	public ResponseEntity<String> deleteEmergencyContacts(@PathVariable("id") long id) {
		try {
			service.deleteById(id);
			return ResponseEntity.ok("EmergencyContacts deleted successfully");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error deleting EmergencyContacts: " + e.getMessage());
		}
	}

}
