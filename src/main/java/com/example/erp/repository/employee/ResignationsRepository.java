package com.example.erp.repository.employee;



import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.employee.Resignations;

public interface ResignationsRepository extends JpaRepository<Resignations, Long>{

	
	
	@Query(value=
			  " SELECT"
			  + " COALESCE(e.user_name, '') AS userName,"
			  + " COALESCE(e.user_id, '') AS userId,"
			  + " COALESCE(d.resignations_id, '') AS resignationsId,"
			  + " COALESCE(d.employee_id, '') AS employeeId,"
			  + " COALESCE(d.reason, '') AS reason,"
			  + " COALESCE(d.resignations_date, '') AS resignationsDate,"
			  + " COALESCE(d.type, '') AS type,"
			  + " COALESCE(d.from_date, '') AS fromDate,"
			  + " COALESCE(d.to_date, '') AS toDate,"
			  + " COALESCE(d.title, '') AS title,"
			  + " CASE"
			  + " WHEN date(d.to_date) <= date(current_date() - interval 1 day) THEN 'timeEnd'"
			  + " ELSE 'approved'"
			  + " END as btype"
			  + " FROM"
			  + " resignations AS d"
			  + " JOIN"
			  + " employee AS e ON e.employee_id = d.employee_id"
			  + " ORDER BY"
			  + " d.resignations_id DESC;",nativeQuery = true)
	List <Map<String,Object>>AllGoat();
	
	
	@Query(value=
			  " SELECT "
			  + "    COALESCE(e.user_name, '') AS userName,"
			  + "    COALESCE(e.user_id, '') AS userId,"
			  + "    COALESCE(d.resignations_id, '') AS resignationsId,"
			  + "    COALESCE(d.employee_id, '') AS employeeId,"
			  + "    COALESCE(d.reason, '') AS reason,"
			  + "    COALESCE(d.resignations_date, '') AS resignationsDate,"
			  + "    COALESCE(d.type, '') AS type,"
			  + "    COALESCE(d.from_date, '') AS fromDate,"
			  + "    COALESCE(d.to_date, '') AS toDate,"
			  + "    COALESCE(d.title, '') AS title"
			  + " FROM resignations AS d"
			  + " JOIN employee AS e ON e.employee_id = d.employee_id"
			  + " WHERE"
			  + "    d.approved = true AND "
			  + "    d.to_date >= curdate();",nativeQuery = true)
	List <Map<String,Object>>AllResignationsrepository();
	
	
	@Query(value="select c.employee_id,c.status from resignations as c"
			+ " where c.employee_id = :employee_id", nativeQuery = true)
	Optional<Resignations> getAllEmployeeIdByQualification(Long employee_id);
	
	@Query(value="select c.employee_id,c.status from resignations as c"
			+ " where c.employee_id = :employee_id", nativeQuery = true)
	List <Map<String,Object>> getAllEmployeeIdByQualification1(Long employee_id);
	
	@Query(value=
			  " select e.user_name ,e.user-id ,d.*"
			  + " from resignations as d"
			  + " join employee as e on e.employee_id=d.employee_id"
			  + " where d.employee_id=:employee_id",nativeQuery = true)
	List <Map<String,Object>>AllTimeGoat(@Param("employee_id")long employee_id);

	@Query(value = "select e.user_name ,e.user-id ,d.*"
			+ "			 from resignations as d"
			+ "			  join employee as e on e.employee_id=d.employee_id"
			+ "              where d.resignations_date between :startDate and :endDate", nativeQuery = true)
	 List<Map<String, Object>> getAllReceiptBetweenDate(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	
	@Query(value = "select e.user_name ,e.user-id, d.resignations_id, d.notice_date, d.reason, d.resignations_date, d.status,"
			+ "       datediff( d.resignations_date,d.notice_date) as duration_date"
			+ " from resignations as d"
			+ "  join employee as e on e.employee_id = d.employee_id;", nativeQuery = true)
	 List<Map<String, Object>> getAllDurationDate();

	@Query(value = " select e.employee_id, e.user_name,de.designation_name,de.designation_id,c.company_id,c.company_name,DATE_FORMAT(e.date, '%M %Y') AS date,"
			+ "    DATE_FORMAT(r.to_date, '%M %Y') AS noticeDate,"
			+ " c.country,c.email,c.gst_no,c.address,c.phone_number1,c.phone_number2,c.state,c.pincode"
			+ "  from employee as e "
			+ " join company as c on c.company_id = e.company_id "
			+ " join designation as de on de.designation_id = e.designation_id"
			+ " join resignations as r on r.employee_id=e.employee_id"
			+ " where r.employee_id = :employee_id", nativeQuery = true)
	List<Map<String, Object>> AllNotifications1(@Param("employee_id") long employee_id);

	@Query(value=
			  " SELECT "
			  + "    COALESCE(e.user_name, '') AS userName,"
			  + "    COALESCE(e.user_id, '') AS userId,"
			  + "    COALESCE(d.resignations_id, '') AS resignationsId,"
			  + "    COALESCE(d.employee_id, '') AS employeeId,"
			  + "    COALESCE(d.reason, '') AS reason,"
			  + "    COALESCE(d.resignations_date, '') AS resignationsDate,"
			  + "    COALESCE(d.type, '') AS type,"
			  + "    COALESCE(d.from_date, '') AS fromDate,"
			  + "    COALESCE(d.to_date, '') AS toDate,"
			  + "    COALESCE(d.title, '') AS title"
			  + " FROM resignations AS d"
			  + " JOIN employee AS e ON e.employee_id = d.employee_id"
			  + " where monthname(d.from_date) = :monthName",nativeQuery = true)
	List<Map<String, Object>> getAllresignationsemployee(@Param("monthName")String monthName);
	@Query(value=
			  " SELECT "
			  + "    COALESCE(e.user_name, '') AS userName,"
			  + "    COALESCE(e.user_id, '') AS userId,"
			  + "    COALESCE(d.resignations_id, '') AS resignationsId,"
			  + "    COALESCE(d.employee_id, '') AS employeeId,"
			  + "    COALESCE(d.reason, '') AS reason,"
			  + "    COALESCE(d.resignations_date, '') AS resignationsDate,"
			  + "    COALESCE(d.type, '') AS type,"
			  + "    COALESCE(d.from_date, '') AS fromDate,"
			  + "    COALESCE(d.to_date, '') AS toDate,"
			  + "    COALESCE(d.title, '') AS title"
			  + " FROM resignations AS d"
			  + " JOIN employee AS e ON e.employee_id = d.employee_id"
			  + "  where year(d.from_date) = :year",nativeQuery = true)
	List<Map<String, Object>> getAllresignations(@Param("year")String year);

	@Modifying
	@Query(value="UPDATE employee AS e\r\n"
	     + "JOIN resignations AS r ON r.employee_id = e.employee_id\r\n"
	     + "SET e.status = false\r\n"
	     + "WHERE e.employee_id =:employeeId AND r.to_date = CURDATE()",nativeQuery = true)
	void updateEmployeeStatus(@Param("employeeId") Long employeeId);



}
