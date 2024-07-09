package com.example.erp.repository.erecruitment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.employee.Personal;
import com.example.erp.entity.erecruitment.TaskAssigned;

public interface TaskAssignedRepository extends JpaRepository<TaskAssigned, Long> {

	@Query(value = " select t.task_id, t.candidate_id ,t.date,t.start_time, t.end_time,"
			+ "  t.completed,  t.in_progress,t.process_id, t.task_assignee,"
			+ "  t.task_priority from task_assigned as t", nativeQuery = true)
	List<Map<String, Object>> getTaskAssignedDetails();

	
	@Query(value = " select t.task_id as taskId ,t.completed,t.date,t.start_time as startTime,t.canceled,t.cancellation_reason as cancellationReasont,t.file_name as fileName ,t.end_time as endTime,d.department_id as departmentId,t.approval_type as approvalType,"
			+ "					 d.department_name as departmentName,e.employee_id as employeeId,e.user_name as employeeName,e.user_id as userId,"
			+ "				 t.task_assignee as taskAssignee ,t.task_priority as taskPriority,"
			+ "					 c.candidate_id as candidateId,c.email_id as emailId,c.user_name as userName"
			+ "					 from task_assigned as t"
			+ "					  join candidate_information as c on c.candidate_id=t.candidate_id	"
			+ "			  join employee as e on e.employee_id=t.employee_id"
			+ "				join department as d on d.department_id= t.department_id", nativeQuery = true)
	List<Map<String, Object>> findTaskAssignedDetails();



	Optional<TaskAssigned> findBycandidateId(long candidateid);


	Optional<TaskAssigned> findByCandidateIdAndDepartmentId(long candidateId, long departmentId);

}
