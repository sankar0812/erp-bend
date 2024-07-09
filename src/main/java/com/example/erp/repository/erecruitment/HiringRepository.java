package com.example.erp.repository.erecruitment;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.erecruitment.Hiring;

public interface HiringRepository  extends JpaRepository<Hiring, Long>{



	
	@Query(value = "SELECT h.hiring_id, h.company_id,h.posted, h.closing, h.position,h.currentdate,c.company_name,c.email AS companyemail,c.address,\r\n"
			+ "c.phone_number1,c.phone_number2,c.url,c.location,c.country, a.department_name,b.brief_description,p.preferred_skills,b.brief_id,p.preferred_id\r\n"
			+ "FROM hiring AS h\r\n"
			+ "JOIN company AS c ON c.company_id = h.company_id\r\n"
			+ "JOIN JOIN department AS a ON a.department_id = h.department_id\r\n"
			+ "JOIN brief_list AS b ON b.hiring_id = h.hiring_id\r\n"
			+ "JOIN preferred_list AS p ON p.hiring_id = h.hiring_id",
	        nativeQuery = true)
	List<Map<String, Object>> getAllQuotationByClientDetails();
	
	
	

	
	  @Query(value =" SELECT h.hiring_id, h.company_id,h.posted, h.closing,h.position, h.currentdate,c.company_name,c.email AS companyemail,c.address,h.email,h.status,h.vacancy,h.department_id,"
				+ " c.phone_number1,c.phone_number2,c.url,c.location,c.country, a.department_name,b.brief_description,p.preferred_skills,b.brief_id,p.preferred_id,h.requests"
				+ " FROM hiring AS h"
				+ " left JOIN company AS c ON c.company_id = h.company_id"
				+ " left JOIN department AS a ON a.department_id = h.department_id"
				+ " left  JOIN brief_list AS b ON b.hiring_id = h.hiring_id"
				+ " left JOIN preferred_list AS p ON p.hiring_id = h.hiring_id"
	  		+ " WHERE "
	  		+ "     h.closing > CURRENT_DATE;",
	            nativeQuery = true)
	    List<Map<String, Object>> getAllByClientDetails();
	  
	  
	  @Query(value =" SELECT h.hiring_id AS hiringId,h.posted,h.closing,h.currentdate,h.vacancy,h.department_id AS applicationId,h.position, a.department_name as departmentName ,a.department_name as jobRole"
	  		+ " FROM hiring AS h"
	  		+ " JOIN department AS a ON a.department_id = h.department_id"
	  		+ " WHERE h.status = true AND h.closing > CURRENT_DATE"
	  		+ " ORDER BY h.posted DESC;",
	            nativeQuery = true)
	    List<Map<String, Object>> getAllByClientDetailsposition();
	  
	  
	@Query(value = " SELECT h.hiring_id, h.company_id,h.posted, h.closing,h.position, h.currentdate,c.company_name,c.email AS companyemail,c.address,h.email,h.status,h.vacancy,h.department_id,"
			+ " c.phone_number1,c.phone_number2,c.url,c.location,c.country, a.department_name,b.brief_description,p.preferred_skills,b.brief_id,p.preferred_id,h.requests"
			+ " FROM hiring AS h"
			+ " left JOIN company AS c ON c.company_id = h.company_id"
			+ " left JOIN department AS a ON a.department_id = h.department_id"
			+ " left JOIN brief_list AS b ON b.hiring_id = h.hiring_id"
			+ " left JOIN preferred_list AS p ON p.hiring_id = h.hiring_id"
			+ " where h.hiring_id = :hiring_id  AND"
			+ " h.closing > CURRENT_DATE ;", nativeQuery = true)
	List<Map<String, Object>> getAllQuotationByClientDetailss(@Param("hiring_id") Long hiringId);


	@Query(value = "SELECT h.hiring_id as hiringId, COUNT(cc.hiring_id) AS candidate_count,h.posted,h.closing,h.currentdate,h.vacancy,h.department_id AS applicationId,a.department_name AS jobTitle \r\n"
			+ " FROM hiring AS h\r\n"
			+ " LEFT JOIN candidate_information AS cc ON cc.hiring_id = h.hiring_id\r\n"
			+ " JOIN department AS a ON a.department_id = h.department_id\r\n"
			+ " WHERE h.status = true AND h.closing > CURRENT_DATE\r\n"
			+ " GROUP BY h.hiring_id\r\n"
			+ " ORDER BY h.posted DESC;", nativeQuery = true)
	List<Map<String, Object>> getAllProjectWorkaa();








	Optional<Hiring> findByDepartmentIdAndPositionAndPostedAndClosing(long departmentId, String position, Date podate,
			Date codate);
}
