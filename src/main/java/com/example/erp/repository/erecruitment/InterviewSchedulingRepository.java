package com.example.erp.repository.erecruitment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.erecruitment.InterviewSchedule;
import com.example.erp.entity.erecruitment.TaskAssigned;

public interface InterviewSchedulingRepository extends JpaRepository<InterviewSchedule, Long> {

	@Query(value = " select i.interview_schedule_id as interviewScheduleId, i.canceled, i.completed, i.scheduled, i.cancellation_reason as cancellationReason, i.date,d.department_id as departmentId,"
			+ " d.department_name as departmentName,e.employee_id as employeeId,e.user_name as employeeName,e.user_id as userId ,i.end_time as endTime,"
			+ " i.interview_type as interviewType,  i.start_time as startTime, i.status, "
			+ "					       c.candidate_id as candidateId, c.user_name as userName, c.mobile_number as mobileNumber, c.email_id as emailId, c.job_role as jobRole"
			+ "					         from interview_schedule as i"
			+ "					         join candidate_information as c on c.candidate_id = i.candidate_id"
			+ "			                     join employee as e on e.employee_id=i.employee_id"
			+ "			                  join department as d on d.department_id= i.department_id", nativeQuery = true)
	List<Map<String, Object>> FindInterviewSchedulingDetails();

	@Query(value = " SELECT" + "    i.interview_schedule_id AS interviewScheduleId," + "  i.canceled, "
			+ "   i.completed," + "   i.scheduled," + "    COALESCE(i.cancellation_reason, '') AS cancellationReason,"
			+ "    COALESCE(i.date, '') AS date," + "    COALESCE(i.end_time, '') AS endTime,"
			+ "    COALESCE(i.interview_type, '') AS interviewType,"
			+ "    COALESCE(e.user_name, '') AS interviewerName,"
			+ "    COALESCE(i.start_time, '') AS startTime," + "     i.status,"
			+ "    COALESCE(c.candidate_id, '') AS candidateId," + "    COALESCE(c.user_name, '') AS userName,"
			+ "  COALESCE(d.department_id, '') AS departmentId,"
			+ "      COALESCE(d.department_name, '') AS departmentName,"
			+ "       COALESCE(e.employee_id, '') AS employeeId," 
			+ "    COALESCE(c.mobile_number, '') AS mobileNumber," + "    COALESCE(c.email_id, '') AS emailId,"
			+ "    COALESCE(c.job_role, '') AS jobRole" + "  FROM" + "    interview_schedule AS i" + "  JOIN"
			+ "    candidate_information AS c ON c.candidate_id = i.candidate_id"
			+ "  join employee as e on e.employee_id=i.employee_id"
			+ "  join department as d on d.department_id= i.department_id;" + "", nativeQuery = true)
	List<Map<String, Object>> FindInterviewSchedulingDetails1();

	@Query(value = "  select i.interview_schedule_id as interviewScheduleId, i.canceled, i.completed, i.scheduled, i.cancellation_reason as cancellationReason, i.date,"
			+ "					         i.end_time as endTime, i.interview_format as interviewFormat,"
			+ "    i.interview_type as interviewType, i.interviewer_contact as interviewerContact, i.interviewer_name as interviewerName, i.start_time as startTime, i.status, "
			+ "						       c.candidate_id as candidateId, c.user_name as userName, c.mobile_number as mobileNumber, c.email_id as emailId, c.job_role as jobRole"
			+ "				         from interview_schedule as i "
			+ "						         join candidate_information as c on c.candidate_id = i.candidate_id"
			+ "                                 where i.completed= true", nativeQuery = true)
	List<Map<String, Object>> FindInterviewSchedulingDetails2();

	@Query(value = "  select  i.date,i.start_time, c.first_name, c.last_name,"
			+ "   case when day(i.date) = day(curdate()) and" + "   month(i.date) = month(curdate()) and"
			+ "   year(i.date) = year(curdate()) then 'you will be interviewed today'"
			+ "   when day(i.date) = day(curdate() + interval 1 day) and"
			+ "   month(i.date) = month(curdate() + interval 1 day) and"
			+ "   year(i.date) = year(curdate() + interval 1 day) then 'you will have an interview tomorrow'"
			+ "   when day(i.date) = day(curdate() + interval 2 day) and"
			+ "   month(i.date) = month(curdate() + interval 2 day) and"
			+ "   year(i.date) = year(curdate() + interval 2 day) then 'you will have an interview the day after tomorrow'"
			+ "   else 'no special message'" + "   end as interview_notification" + "   from interview_schedule as i"
			+ "   join candidate_information as c on c.candidate_id = i.candidate_id"
			+ "   where date(i.date) between curdate() and curdate() + interval 3 day", nativeQuery = true)
	List<Map<String, Object>> findInterviewScheduleNotification();

	Optional<InterviewSchedule> findBycandidateId(long candidateid);

	Optional<InterviewSchedule> findByCandidateIdAndDepartmentId(long id, long depId);

}
