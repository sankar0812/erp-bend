package com.example.erp.repository.erecruitment;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.erecruitment.Appointment;
import com.example.erp.entity.erecruitment.Offer;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{

	@Query(value=" select a.appointment_id as appointmentId,a.confirmation_status as confirmationStatus,"
			+ " a.date,a.position,a.status,a.time,a.approval_type as approvalType,c.candidate_id as candidateId , c.user_name as userName"
			+ " from appointment as a"
			+ " join candidate_information as c on c.candidate_id= a.candidate_id", nativeQuery = true)
	List<Map<String, Object>>findAppointmentDetails();

	Optional<Appointment> findBycandidateId(long candidateid);
	
	
	@Query(value=" select a.appointment_id as appointmentId,a.confirmation_status as confirmationStatus,"
			+ " a.date,a.position,a.status,a.time,a.approval_type as approvalType,c.candidate_id as candidateId , c.user_name as userName"
			+ " from appointment as a"
			+ " join candidate_information as c on c.candidate_id= a.candidate_id"
			+ " where year(a.date) = :year ", nativeQuery = true)
	List<Map<String, Object>>findAppointmentDetail(@Param("year") String year);

	Optional<Offer> findByCandidateIdAndDateAndPosition(long id1, Date date, String po);
	
	
}
