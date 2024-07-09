package com.example.erp.repository.erecruitment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.erecruitment.GroupDiscussion;
import com.example.erp.entity.erecruitment.TaskAssigned;

public interface GroupDiscussionRepository extends JpaRepository<GroupDiscussion, Long> {

	@Query(value = " SELECT g.group_discussion_id AS groupDiscussionId, g.completed, g.date, g.feedback,  "
			+ "	         g.scheduled,g.canceled , g.status , g.time , g.topic ,"
			+ "             c.candidate_id AS candidateId, c.user_name AS userName,g.approval_type as approvalType"
			+ "	       FROM group_discussion AS g "
			+ "	       JOIN candidate_information AS c ON c.candidate_id = g.candidate_id ", nativeQuery = true)
	List<Map<String, Object>> findGroupDiscussionDetails();

	Optional<GroupDiscussion> findBycandidateId(long candidateid);

	Optional<GroupDiscussion> findByCandidateIdAndTopic(long candidateId, String top);


}
