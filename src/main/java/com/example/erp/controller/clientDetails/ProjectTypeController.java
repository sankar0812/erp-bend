package com.example.erp.controller.clientDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.example.erp.entity.clientDetails.ProjectType;
import com.example.erp.repository.clientDetails.ProjectTypeRepository;
import com.example.erp.service.clientDetails.ProjectTypeService;

@RestController
@CrossOrigin
public class ProjectTypeController {
	@Autowired
	private ProjectTypeService projectTypeService;

	@GetMapping("/projectType/view")
	public ResponseEntity<Object> getProjectType(@RequestParam(required = true) String projectType) {
		if ("projectTypeDetails".equals(projectType)) {
			return ResponseEntity.ok(projectTypeService.listAll());
		} else {
			String errorMessage = "Invalid value for 'projectType'. Expected 'projectType'.";
			return ResponseEntity.badRequest().body(errorMessage);
		}
	}


	@PostMapping("/projectType/save")
	public ResponseEntity<String> saveAppointment(@RequestBody ProjectType projectType) {
		try {
			projectTypeService.SaveProjectType(projectType);
			long projectTypeId = projectType.getProjectTypeId();
			return ResponseEntity.ok("ProjectType saved successfully. ProjectType ID: " + projectTypeId);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error saving projectType: " + e.getMessage());
		}
	}

	@PutMapping("/updateProjectType/{id}")
	public ResponseEntity<ProjectType> updateAppointment(@PathVariable("id") long id,
			@RequestBody ProjectType projectType) {
		try {
			ProjectType existingprojectType = projectTypeService.findById(id);
			if (existingprojectType == null) {
				return ResponseEntity.notFound().build();
			}
			existingprojectType.setProjectType(projectType.getProjectType());
			existingprojectType.setColor(projectType.getColor());
			projectTypeService.SaveProjectType(existingprojectType);
			return ResponseEntity.ok(existingprojectType);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@DeleteMapping("/projectType/delete/{id}")
	public ResponseEntity<String> deleteProjectType(@PathVariable("id") Long id) {
		projectTypeService.deleteProjectTypeId(id);
		return ResponseEntity.ok("ProjectType details deleted successfully");
	}

	@Autowired
	private ProjectTypeRepository projectTypeRepository;

	@GetMapping("/findProjectTypeDetails")
	public ResponseEntity<?> getProjectTypeDetails() {
		try {
			List<Map<String, Object>> projectTypes = projectTypeRepository.findProjectTypeDetails();
			List<Map<String, Object>> projectTypeList = new ArrayList<>();

			for (Map<String, Object> projectType : projectTypes) {
				Map<String, Object> projectTypeMap = new HashMap<>();

				String fileUploadUrl = "/fileUpload/" + projectType.get("projectId");
				projectTypeMap.put("fileUploadUrl", fileUploadUrl);

				projectTypeMap.putAll(projectType);
				projectTypeList.add(projectTypeMap);
			}
			return ResponseEntity.ok(projectTypeList);
		} catch (Exception e) {
			String errorMessage = "Error occurred while retrieving project type details";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Collections.singletonMap("error", errorMessage));
		}
	}

	
	@GetMapping("/projectType/percentage/dashboard")
	public List<Map<String, Object>> getAllcount(@RequestParam(required = true) String dashboard) {
		try {
			if ("percentage".equalsIgnoreCase(dashboard)) {
				return projectTypeRepository.ProjectTypeDetails();
			} else {
				throw new IllegalArgumentException("Invalid parameter value. 'percentage' must be 'dashboard'.");
			}
		} catch (Exception e) {

			e.printStackTrace();
			return Collections.emptyList();

		}
	}
}
