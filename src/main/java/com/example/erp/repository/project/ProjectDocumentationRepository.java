package com.example.erp.repository.project;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.project.ProjectDocumentation;

public interface ProjectDocumentationRepository extends JpaRepository<ProjectDocumentation, Long>{

	
	@Query(value = "select pd.project_documentation_id as projectDocumentationId ,pd.accepted,pd.date,pd.employee_id as employeeId,pd.project_id as projectId,"
			+ " pd.project_status as projectStatus,pd.rejected,e.user_id as userId,e.user_name as userName,cr.project_name as projectName, pd.url"
			+ " from  project_documentation as pd "
			+ " join client_requirements as cr on cr.project_id=pd.project_id"
			+ " left join employee as e on e.employee_id=pd.employee_id",nativeQuery = true)
	List<Map<String, Object>> getAllprojectDocumentation();

}
