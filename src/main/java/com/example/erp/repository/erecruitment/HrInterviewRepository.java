package com.example.erp.repository.erecruitment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.erecruitment.HrInterview;
import com.example.erp.entity.erecruitment.InterviewSchedule;
import com.example.erp.entity.erecruitment.TaskAssigned;

public interface HrInterviewRepository extends JpaRepository<HrInterview, Long>{

	@Query(value=" select h.hr_interview_id as hr_interviewId,  h.completed,h.date,h.feedback,"
			+ "		 h.status,h.time ,c.candidate_id as candidateId ,c.user_name as userName,d.department_id as departmentId,h.approval_type as approvalType,"
			+ " d.department_name as departmentName,e.employee_id as employeeId,e.user_name as employeeName,e.user_id as userId,h.cancellation_reason as cancellationreason"
			+ "         from hr_interview as h"
			+ "			  join candidate_information as c on c.candidate_id=h.candidate_id"
			+ "               join employee as e on e.employee_id=h.employee_id"
			+ "					join department as d on d.department_id= h.department_id", nativeQuery = true)
	List<Map<String, Object>>findHrInterviewDetails();
	
	Optional<HrInterview> findBycandidateId(long candidateid);

	Optional<HrInterview> findByCandidateIdAndDepartmentId(long id, long depId);
}
