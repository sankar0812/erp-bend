package com.example.erp.repository.erecruitment;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.erecruitment.InterviewProcess;

public interface InterviewProcessRepository extends JpaRepository<InterviewProcess, Long> {

	@Query(value=" select i.interview_process_id, i.started,i.in_progress,i.completed,i.start_date,i.end_date,"
			+ "  i.feedback,i.interview_type,i.job_position,c.candidate_id,c.first_name,c.last_name"
			+ "  from interview_process as i"
			+ "  join candidate_information as c on c.candidate_id=i.candidate_id", nativeQuery = true)
	List<Map<String, Object>>findInterviewProcessDetails();
}
