package com.example.erp.repository.clientDetails;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.clientDetails.ProjectType;
public interface ProjectTypeRepository extends JpaRepository<ProjectType, Long> {

	@Query(value=" select c.project_id as projectId, c.client_id as clientId,c.duration ,c.date,c.project_name as projectName,"
			+ "  c.services,p.project_type_id as projectTypeId,p.project_type as projectType from client_requirements as c"
			+ "  join project_type as p on p.project_id=c.project_id", nativeQuery = true)
	List<Map<String, Object>>findProjectTypeDetails();
	
	

	@Query(value=" SELECT pt.color,pt.project_type,COUNT(cr.project_type_id) AS count,ROUND(COUNT(cr.project_type_id) * 100.0 / SUM(COUNT(cr.project_type_id)) OVER (), 0) AS percentage"
			+ " FROM project_type AS pt"
			+ " JOIN client_requirements AS cr ON cr.project_type_id = pt.project_type_id"
			+ " GROUP BY pt.project_type,pt.color;", nativeQuery = true)
	List<Map<String, Object>>ProjectTypeDetails();
	
	
	
}

