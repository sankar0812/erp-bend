package com.example.erp.repository.erecruitment;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.erecruitment.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {

	@Query(value=" SELECT"
			+ "    (SELECT COUNT(*) FROM candidate_information WHERE DATE(date) = CURDATE()) AS currentDayCandidatesCount,"
			+ "    (SELECT COUNT(*) FROM candidate_information WHERE DATE_FORMAT(date, '%Y-%m') = DATE_FORMAT(CURDATE(), '%Y-%m')) AS currentMonthCandidatesCount,"
			+ "    (SELECT COUNT(*) FROM candidate_information WHERE YEAR(date) = YEAR(CURDATE())) AS currentYearCandidatesCount", nativeQuery = true)
	List<Map<String, Object>>countOfCandidatesDetails();
			
			@Query(value=" select c.candidate_id as candidateId, c.address,c.branch,"
					+ "  c.cgpa, c.city, c.college, c.country,  c.date, c.dateof_birth as birthOfBirth,c.education, c.email_id as emailId,"
					+ "  c.first_name as firstName, c.gender, c.job_role as jobRole, c.last_name as lastName, c.marital_status as maritalStatus, c.mobile_number as mobileNumber, c.salary_expectations as salaryExpectations,c.skill_details as skillDetails,"
					+ "  c.work_experience as workExperience, c.year_of_passing as yearOfPassing"
					+ "  from candidate_information as c where date(c.date) = CURDATE()", nativeQuery = true)
			List<Map<String, Object>> findCurrentDateCandidatesDetails();
			
			
//			@Query(value = "SELECT "
//					+ "   h.application_id as applicationId, "
//					+ "     a.job_title as jobTitle"
//					+ " FROM application AS a"
//					+ " left JOIN hiring AS h ON h.application_id = a.application_id"
//					+ " WHERE "
//					+ " h.status = true and"
//					+ "    (date_add(h.currentdate, interval year(curdate()) - year(h.posted) year) BETWEEN curdate() AND curdate() + interval 5 day);",
//			        nativeQuery = true)
//			List<Map<String, Object>> getAllQuotationByClientDetails1();
			
			
			@Query(value = "SELECT "
					+ " h.application_id as applicationId, "
					+ " a.job_title as jobTitle"
					+ " FROM application AS a"
					+ " left JOIN hiring AS h ON h.application_id = a.application_id",nativeQuery = true)
			List<Map<String, Object>> getAllQuotationByClientDetails1();
			
			@Query(value = "SELECT c.candidate_id as candidateId, c.address, c.resume_url as resumeUrl, c.branch, c.cgpa, c.city, c.college, "
			        + " c.country, c.cover_letter_url as coverLetterUrl, c.date, c.dateof_birth as dateOfBirth, c.education, c.email_id as emailId, "
			        + " c.first_name as firstName, c.gender, c.job_role as jobRole, c.last_name as lastName, c.marital_status as maritalStatus, "
			        + " c.mobile_number as mobileNumber, c.salary_expectations as salaryExpectations, c.skill_details as skillDetails, "
			        + " c.work_experience as workExperience, c.year_of_passing as yearOfPassing "
			        + " FROM candidate_information AS c "
			        + " WHERE DATE(c.date) = CURDATE() "
			        + " AND c.work_experience = ("
			        + "    SELECT MAX(work_experience) FROM candidate_information AS ci "
			        + "    WHERE DATE(ci.date) = CURDATE())", nativeQuery = true)
			List<Map<String, Object>> findHighestExperienceDetails();

			List<Candidate> findByCancelledTrue();

			Optional<Date> findLatestApplicationDateByEmailId(String emId);

			
}
