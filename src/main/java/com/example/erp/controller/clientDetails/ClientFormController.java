package com.example.erp.controller.clientDetails;

import java.sql.Date;

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
import com.example.erp.entity.clientDetails.ClientForm;
import com.example.erp.entity.clientDetails.ClientRequirement;
import com.example.erp.service.clientDetails.ClientFormService;
import com.example.erp.service.clientDetails.ClientRequirementService;

@RestController
@CrossOrigin
public class ClientFormController {

	@Autowired
	private ClientFormService clientFormService;

	@Autowired
	private ClientRequirementService clientRequirementService;

	@GetMapping("/clientForm/view")
	public ResponseEntity<Object> getClientFormDetails(@RequestParam(required = true) String clientForm) {
		if ("clientFormDetails".equals(clientForm)) {
			return ResponseEntity.ok(clientFormService.listClientProjects());
		} else {
			String errorMessage = "Invalid value for 'clientForm'. Expected 'clientFormDetails'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}

	@PostMapping("/clientForm/save")
	public ResponseEntity<String> saveClientForm(@RequestBody ClientForm clientForm) {
		try {
			clientFormService.SaveClientProject(clientForm);
			long clientFormId = clientForm.getClientFormId();
			return ResponseEntity.ok("ClientForm saved successfully. ClientForm ID: " + clientFormId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving projectType: " + e.getMessage());
		}
	}

	@PutMapping("/clientForm/edit/{id}")
	public ResponseEntity<ClientForm> updateClientForm(@PathVariable("id") long id,
			@RequestBody ClientForm projectType) {
		try {
			ClientForm existingClientForm = clientFormService.findByClientProjectId(id);
			if (existingClientForm == null) {
				return ResponseEntity.notFound().build();
			}
			existingClientForm.setFeatures(projectType.getFeatures());
			existingClientForm.setProjectName(projectType.getProjectName());
			existingClientForm.setSkillsAndDescription(projectType.getSkillsAndDescription());
			existingClientForm.setProjectStatus(projectType.getProjectStatus());

			clientFormService.SaveClientProject(existingClientForm);
			return ResponseEntity.ok(existingClientForm);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PutMapping("/clientForm/details/edit/{id}")
	public ResponseEntity<?> updateClientFormForProject(@PathVariable("id") long id,
			@RequestBody ClientForm clientForm) {
		try {
			System.out.println("Received PATCH request. ClientForm: " + clientForm);

			ClientForm existingClientForm = clientFormService.findByClientProjectId(id);

			if (existingClientForm == null) {
				return ResponseEntity.notFound().build();
			}

			System.out.println("Existing ClientForm: " + existingClientForm);

			if (existingClientForm.isStatus()) {
				String errorMessage = "A Project is already approved";
				System.out.println(errorMessage);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}

			if (existingClientForm.isRejected()) {
				String errorMessage = "A Project is rejected";
				System.out.println(errorMessage);
				return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
			}

			existingClientForm.setProjectStatus(clientForm.getProjectStatus());

			if (clientForm.getProjectStatus().equals("approved")) {
				existingClientForm.setStatus(true);
			} else if (clientForm.getProjectStatus().equals("rejected")) {
				existingClientForm.setRejected(true);
			} else {
				existingClientForm.setStatus(false);
				existingClientForm.setRejected(false);
			}

			clientFormService.SaveClientProject(existingClientForm);

			if (existingClientForm.getProjectStatus().equals("approved")) {
				long clientId = existingClientForm.getClientId();
				String projectName = existingClientForm.getProjectName();
				Date date = existingClientForm.getDate();
				String skillsAndDescription = existingClientForm.getSkillsAndDescription();
				String features = existingClientForm.getFeatures();

				ClientRequirement requirement = new ClientRequirement();
				requirement.setClientId(clientId);
				requirement.setDate(date);
				requirement.setFeatures(features);
				requirement.setSkillsAndDescription(skillsAndDescription);
				requirement.setProjectName(projectName);

				System.out.println("Saving ClientRequirement: " + requirement);

				clientRequirementService.SaveClientRequirmentDetails(requirement);
			}

			return ResponseEntity.ok(existingClientForm);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/clientForm/delete/{id}")
	public ResponseEntity<String> deleteClientProject(@PathVariable("id") Long id) {
		clientFormService.deleteClientProjectById(id);
		return ResponseEntity.ok("ClientProject details deleted successfully");
	}

}
