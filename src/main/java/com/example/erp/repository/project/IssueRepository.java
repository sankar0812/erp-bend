package com.example.erp.repository.project;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.project.Issue;

public interface IssueRepository extends JpaRepository<Issue, Long>{
	
	@Query(value = "select i.issue_id as issueId ,i.accepted,i.date,i.cancellation_reason as cancellationReason,i.employee_id as employeeId,i.project_id as projectId,i.project_status as projectStatus,i.rejected,"
			+ " e.user_id as userId,e.user_name as userName,cr.project_name as projectName"
			+ " from  issue as i"
			+ " join client_requirements as cr on cr.project_id=i.project_id"
			+ " join employee as e on e.employee_id=i.employee_id",nativeQuery = true)
	List<Map<String, Object>> getAllProjectissue();

}
