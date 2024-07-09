package com.example.erp.repository.project;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.project.ProjectAssigning;

public interface ProjectAssigningRepository extends JpaRepository<ProjectAssigning, Long> {
	
	
	@Query(value = "select ps.project_assigning_id AS projectAssigningId,ps.accepted,"
			+ " ps.date,dl.department_id AS departmentId,"
			+ " dl.employee_id AS employeeId,ps.project_id AS projectId,"
			+ " ps.project_status AS projectStatus,ps.rejected,"
			+ " d.department_name AS departmentName,e.user_id AS userId,e.user_name AS userName,cr.project_name AS projectName,"
			+ " ps.type_of_project AS typeOfProject,dl.department_list_id"
			+ " from project_assigning as ps"
			+ " left join department_list as dl on dl.project_assigning_id = ps.project_assigning_id"
			+ " join client_requirements as cr on cr.project_id = ps.project_id"
			+ " left join department as d on d.department_id = dl.department_id"
			+ " left join employee as e on e.employee_id = dl.employee_id", nativeQuery = true)
	List<Map<String, Object>> getAllprojectAssigning();
	
	
}
