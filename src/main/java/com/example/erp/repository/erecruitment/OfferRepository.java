package com.example.erp.repository.erecruitment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.erecruitment.HrInterview;
import com.example.erp.entity.erecruitment.Offer;

public interface OfferRepository extends JpaRepository<Offer, Long> {

	@Query(value="select ol.offer_id,ol.acceptance_status,ol.appointment_id,ol.expiry_date,"
			+ "	ol.joining_date,ol.salary_package,ol.candidate_id,ol.certificate_url from offer_letter as ol",nativeQuery = true)
	List<Map<String, Object>> getAllDetails();
	
	
	@Query(value=
			" select o.offer_id as offerId,o.joining_date as joiningDate, o.date,"
			+ "	o.expiry_date as expiryDate,o.salary_package as salaryPackage,"
			+ "	c.candidate_id as candidateId,c.user_name as userName, c.email_id as emailId ,o.approval_type as approvalType"
			+ " from offer_letter as o"
			+ "	join candidate_information as c on c.candidate_id= o.candidate_id", nativeQuery = true)
	List<Map<String, Object>>FindOfferDetails();


	Optional<Offer> findBycandidateId(long candidateid);


	Optional<Offer> findByCandidateIdAndAppointmentId(long id, long depId);
}
