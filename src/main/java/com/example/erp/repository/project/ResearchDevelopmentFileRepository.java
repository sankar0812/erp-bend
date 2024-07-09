package com.example.erp.repository.project;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.project.ResearchDevelopmentFile;

public interface ResearchDevelopmentFileRepository extends JpaRepository<ResearchDevelopmentFile, Long>{

	
	
	@Query(value = "select r.research_development_file_id as researchDevelopmentFileId ,r.accepted,r.date,r.employee_id as employeeId,"
			+ " r.project_id as projectId,r.project_status as projectStatus,r.rejected,e.user_id as userId,r.url,"
			+ " e.user_name as userName,cr.project_name as projectName"
			+ " from  research_development_file as r"
			+ " join client_requirements as cr on cr.project_id=r.project_id"
			+ " left join employee as e on e.employee_id=r.employee_id",nativeQuery = true)	
	List<Map<String, Object>> getAllProjectresearchDevelopmentFile();



}
