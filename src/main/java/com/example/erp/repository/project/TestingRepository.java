package com.example.erp.repository.project;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.project.Testing;

public interface TestingRepository extends JpaRepository<Testing, Long>{

	
@Query(value = "select t.testing_id as testingId ,t.accepted,t.date,t.department_id as departmentId,t.employee_id as employeeId,"
		+ " t.project_id as projectId,t.project_status as projectStatus,t.rejected,d.department_name as departmentName,e.user_id as userId,"
		+ " e.user_name as userName,cr.project_name as projectName,t.type_of_project as typeOfProject"
		+ " from  testing as t"
		+ " join client_requirements as cr on cr.project_id=t.project_id"
		+ " left join department as d on d.department_id=t.department_id"
		+ " left join employee as e on e.employee_id=t.employee_id;",nativeQuery = true)
	List<Map<String, Object>> getAllProjecttesting();

}
