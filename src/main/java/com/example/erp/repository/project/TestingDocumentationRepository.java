package com.example.erp.repository.project;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.project.TestingDocumentation;

public interface TestingDocumentationRepository extends JpaRepository<TestingDocumentation, Long> {

	
	@Query(value = "select r.testing_documentation_id as testingDocumentationId ,r.accepted,r.date,r.employee_id as employeeId,"
			+ " r.project_id as projectId,r.url,r.project_status as projectStatus,r.rejected,e.user_id as userId,"
			+ " e.user_name as userName,cr.project_name as projectName"
			+ " from  testing_documentation as r"
			+ " join client_requirements as cr on cr.project_id=r.project_id"
			+ " left join employee as e on e.employee_id=r.employee_id",nativeQuery = true)
	List<Map<String, Object>> getAllProjecttestingDocumentation();
	
		@Query(value = "select r.testing_documentation_id as testingDocumentationId ,r.accepted,r.date,r.employee_id as employeeId,"
				+ "			 r.project_id as projectId,r.url,r.project_status as projectStatus,r.rejected,e.user_id as userId,"
				+ "			 e.user_name as userName,cr.project_name as projectName"
				+ "			 from  testing_documentation as r"
				+ "			 join client_requirements as cr on cr.project_id=r.project_id"
				+ "			 left join employee as e on e.employee_id=r.employee_id"
				+ "             where r.accepted=true ",nativeQuery = true)
		List<Map<String, Object>> getAllProjecttestingDocumentationaccepted();

}
