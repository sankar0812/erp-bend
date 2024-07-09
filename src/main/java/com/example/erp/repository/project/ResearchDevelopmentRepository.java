package com.example.erp.repository.project;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.project.ResearchDevelopment;

public interface ResearchDevelopmentRepository extends JpaRepository<ResearchDevelopment, Long> {

	
	@Query(value = ""
			+ "  select rd.research_development_id as researchDevelopmentId ,rd.accepted,rd.date,rd.department_id as departmentId,rd.employee_id as employeeId,"
			+ " rd.project_id as projectId,rd.project_status as projectStatus,rd.rejected,d.department_name as departmentName,e.user_id as userId,"
			+ " e.user_name as userName,cr.project_name as projectName,rd.type_of_project as typeOfProject"
			+ " from  research_development as rd"
			+ " join client_requirements as cr on cr.project_id=rd.project_id"
			+ " left join department as d on d.department_id=rd.department_id"
			+ " left join employee as e on e.employee_id=rd.employee_id; "
		, nativeQuery = true)
	List<Map<String, Object>> getAllProjectresearchDevelopment();

}
