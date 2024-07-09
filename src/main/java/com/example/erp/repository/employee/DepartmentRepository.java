package com.example.erp.repository.employee;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.employee.Department;



public interface DepartmentRepository extends JpaRepository<Department, Long> {

	
	@Query(value=" SELECT cr.project_name,t.client_id,d.department_name,e.user_name,e.user_id,e.employee_id,"
			+ " tl.trainee_id, td.user_id AS trainee_user_id,td.user_name AS trainee_user_name"
			+ " FROM task AS t"
			+ " join task_list as tl on tl.task_id=t.task_id"
			+ " JOIN client_requirements as cr ON cr.project_id = t.project_id"
			+ " left join employee as e on e.employee_id=tl.employee_id"
			+ " left join training_details as td on td.trainee_id=tl.trainee_id"
			+ " join department as d on d.department_id=tl.department_id"
			+ " where  d.department_id=:department_id", nativeQuery = true)
	List<Map<String, Object>> GetClientProjectWorkingMember(@Param("department_id")Long department_id);

}
