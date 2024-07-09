package com.example.erp.repository.employee;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.employee.Awards;




public interface AwardsRepository extends JpaRepository<Awards, Long> {

	@Query(value = " select a.awards_id, a.description,  a.date, e.employee_id, e.user_name,e.user_id, ap.awards_photo_id,ap.url, a.status ,a.awards_type"
			+ "	 from awards as a  join awardsphoto as ap on a.awards_id = ap.fkawards_id "
			+ "	 join employee as e on e.employee_id = a.employee_id"
			+ "   order by a.awards_id desc", nativeQuery = true)
	List<Map<String, Object>> AllEmployee();

	
	
	@Query(value = " select a.awards_id, a.description,  a.date, e.employee_id, e.user_name,e.user_id, ap.awards_photo_id,ap.url, a.status ,a.awards_type"
			+ "	 from awards as a  join awardsphoto as ap on a.awards_id = ap.fkawards_id "
			+ "	 join employee as e on e.employee_id = a.employee_id"
			+ " WHERE e.employee_id = :employee_id", nativeQuery = true)
	List<Map<String, Object>> AllfilterID(@Param("employee_id") long employee_id);
	
	
	
	@Query(value = " SELECT a.awards_id, a.description, a.date, e.employee_id,e.user_name,e.user_id, ap.awards_photo_id, ap.awards_photo, a.status,a.awards_type "
			+ " FROM awards AS a " 
			+ " JOIN awardsphoto AS ap ON a.awards_id = ap.fkawards_id "
			+ " JOIN employee AS e ON e.employee_id = a.employee_id "
			+ " WHERE e.employee_id = :employee_id", nativeQuery = true)
	List<Map<String, Object>> Allfilter(@Param("employee_id") long employee_id);

	@Query(value = "SELECT a.awards_id, a.description, a.date,  e.employee_id, e.user_name,e.user_id,ap.awards_photo_id, ap.awards_photo, a.status ,a.awards_type"
			+ " FROM Awards a "
			+ " JOIN employee AS e ON e.employee_id = a.employee_id"
			+ " JOIN awardsphoto AS ap ON a.awards_id = ap.fkawards_id"
			+ " WHERE a.date BETWEEN :startDate AND :endDate ",nativeQuery = true)
    List<Map<String, Object>> findAwardsByEmployeeIdAndDate(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

	@Query(value = " SELECT a.awards_id, a.description, a.gift, a.date, a.cash, e.employee_id,e.user_name,e.user_id, ap.awards_photo_id, ap.awards_photo,a.awards_type,a.status, count_sub.awardsCount " +
	        " FROM awards AS a " +
	        " JOIN awardsphoto AS ap ON a.awards_id = ap.fkawards_id " +
	        " JOIN employee AS e ON e.employee_id = a.employee_id " +
	        " JOIN (" +
	        "   SELECT employee_id, COUNT(awards_id) as awardsCount " +
	        "   FROM awards " +
	        "   GROUP BY employee_id" +
	        " ) count_sub ON count_sub.employee_id = e.employee_id",nativeQuery = true)
	List<Object[]> getEmployeeAwardsCount();

	List<Awards> findByEmployeeId(Long employeeId);
	


	


	

	
	 
}

