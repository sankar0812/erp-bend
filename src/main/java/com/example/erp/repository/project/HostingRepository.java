package com.example.erp.repository.project;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.project.Hosting;

public interface HostingRepository extends JpaRepository<Hosting, Long>{
	
	
	@Query(value = "select h.hosting_id as hostingId ,h.accepted,h.date,h.department_id as departmentId,h.employee_id as employeeId,h.project_id as projectId,h.project_status as projectStatus,h.rejected,d.department_name as departmentName,"
			+ " e.user_id as userId,e.user_name as userName,cr.project_name as projectName, h.url"
			+ " from  hosting as h "
			+ " left join client_requirements as cr on cr.project_id=h.project_id"
			+ " left join department as d on d.department_id=h.department_id"
			+ " left join employee as e on e.employee_id=h.employee_id; "
		, nativeQuery = true)
	List<Map<String, Object>> getAllProjectWorkaaggg();

}
