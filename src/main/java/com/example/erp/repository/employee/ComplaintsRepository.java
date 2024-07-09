package com.example.erp.repository.employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.employee.Complaints;
import com.example.erp.entity.employee.Employee;



public interface ComplaintsRepository extends JpaRepository<Complaints, Long>{
	
	@Query(value = "SELECT\r\n"
			+ "    NULLIF(e.employee_id, 'null') AS employeeId,\r\n"
			+ "   c.complaints_id as complaintsId,c.complaints_against as complaintsAgainst,t.trainee_id as traineeId,r.role_id as roleId,r.role_name as roleName,COALESCE(e.user_name, t.user_name) as userName,\r\n"
			+ " COALESCE(e.user_id, t.user_id) as userId,\r\n"
			+ "			 c.complaints_date as complaintsDate,c.complaints_title as complaintsTitle,c.description,c.status,c.url,c.image_status as imageStatus,"
			+ "			 c.complaints_against_name as complaintsAgainstName\r\n"
			+ "  FROM\r\n"
			+ "    complaints AS c\r\n"
			+ " LEFT JOIN\r\n"
			+ "    employee AS e ON e.employee_id = c.employee_id\r\n"
			+ " LEFT JOIN\r\n"
			+ "    training_details AS t ON t.trainee_id = c.trainee_id\r\n"
			+ " JOIN\r\n"
			+ "    role AS r ON r.role_id = COALESCE(e.role_id, t.role_id)\r\n"
			+ " ORDER BY\r\n"
			+ "    c.complaints_id DESC; ",nativeQuery = true)
	 List<Map<String,Object>> AllComplaints();
	
	
	
	@Query(value = " select e.user_name,e.user_id,c.complaints_id,c.complaints_against,c.complaints_date,c.complaints_title,c.description,c.employee_id,c.status,c.url,"
			+ "c.complaints_against_name"
			+ "	from complaints as c"
			+ "	join employee as e on e.employee_id=c.employee_id"
			+ "  where DATE(c.complaints_date) = CURRENT_DATE;",nativeQuery = true)
	 List<Map<String,Object>> AllComplaints111();
	
	@Query(value = " select e.user_name,e.user_id,c.complaints_id,c.complaints_against,c.complaints_date,c.complaints_title,"
			+ "c.description,c.employee_id,c.status,c.url,c.complaints_against_name,r.role_name,r.role_id,c.image_status as imageStatus"
			+ "	from complaints as c"
			+ "	join employee as e on e.employee_id=c.employee_id"
			+ " join role as r on r.role_id=e.role_id"
			+ "	where e.employee_id=:employee_id  and r.role_id=:role_id",nativeQuery = true)	
	List<Map<String, Object>> Allcomplaints( @Param("employee_id") long employee_id, @Param("role_id") long role_id);
	
	
	@Query(value = " select e.user_name,c.complaints_id,c.complaints_against,c.complaints_date,c.complaints_title,"
			+ " c.description,c.trainee_id,c.status,c.url,c.complaints_against_name,r.role_name,r.role_id,e.user_id,c.image_status as imageStatus"
			+ "	from complaints as c"
			+ "	join training_details as e on e.trainee_id=c.trainee_id"
			+ " join role as r on r.role_id=e.role_id"
			+ "	where e.trainee_id=:trainee_id  and r.role_id=:role_id"
			+ " ORDER BY "
			+ "	   c.complaints_id DESC;",nativeQuery = true)	
	List<Map<String, Object>> Allcomplaintstraining( @Param("trainee_id") long trainee_id, @Param("role_id") long role_id);
	
	
	
	@Query(value = "select e.first_name ,e.last_name ,c.*"
			+ "from complaints as c"
			+ " join employee as e on e.employee_id=c.employee_id"
			+ " where c.complaints_date BETWEEN :startDate AND :endDate",nativeQuery = true)	
	List<Map<String, Object>> getAllpromotionsBetweenDates(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

	
	
	@Query(value = " select year(complaints_date) as year_number, month(complaints_date) as month_number, count(complaints_id) as total_complaints"
			+ " from complaints"
			+ " group by year(complaints_date), month(complaints_date);",nativeQuery = true)	
	List<Map<String, Object>> getAllcomplaints();


	@Query(value =" select e.user_name as userName,e.user_id as userId,c.complaints_id as complaintsId,c.complaints_against as complaintsAgainst,c.complaints_date as complaintsDate,c.complaints_title as complaintsTitle,c.description,c.employee_id as employeeId,c.status,c.url,"
			+ " c.complaints_against_name as complaintsAgainstName"
			+ "	from complaints as c"
			+ "	join employee as e on e.employee_id=c.employee_id"
	        + " where monthname(c.complaints_date) = :monthName", nativeQuery = true)
	List<Map<String, Object>> getAllcomplaints(@Param("monthName") String monthName);
	
	///////////trainee///////////
	@Query(value =" select e.user_name as userName,e.user_id as userId,c.complaints_id as complaintsId,c.complaints_against as complaintsAgainst,c.complaints_date as complaintsDate,c.complaints_title as complaintsTitle,c.description,c.trainee_id as traineeId,c.status,c.url,"
			+ " c.complaints_against_name as complaintsAgainstName"
			+ "	from complaints as c"
			+ "	join training_details as e on e.trainee_id=c.trainee_id"
	        + " where monthname(c.complaints_date) = :monthName", nativeQuery = true)
	List<Map<String, Object>> getAllcomplaintstrainee(@Param("monthName") String monthName);

	@Query(value =" select e.user_name as userName,e.user_id as userId,c.complaints_id as complaintsId,c.complaints_against as complaintsAgainst,c.complaints_date as complaintsDate,c.complaints_title as complaintsTitle,c.description,c.employee_id as employeeId,c.status,c.url,"
			+ " c.complaints_against_name as complaintsAgainstName"
			+ "	from complaints as c"
			+ "	join employee as e on e.employee_id=c.employee_id"
	        + " where year(c.complaints_date) = :year", nativeQuery = true)
	List<Map<String, Object>> getAllcomplaintsYear(@Param("year") String year);

	   ///////////trainee///////////
	@Query(value =" select e.user_name as userName,e.user_id as userId,c.complaints_id as complaintsId,c.complaints_against as complaintsAgainst,c.complaints_date as complaintsDate,c.complaints_title as complaintsTitle,c.description,c.trainee_id as traineeId,c.status,c.url,"
			+ " c.complaints_against_name as complaintsAgainstName"
			+ "	from complaints as c"
			+ "	join training_details as e on e.trainee_id=c.trainee_id"
	        + " where year(c.complaints_date) = :year", nativeQuery = true)
	List<Map<String, Object>> getAllcomplaintsYeartrainee(@Param("year") String year);

	
	@Query(value =" select e.user_name as userName,e.user_id as userId,c.complaints_id as complaintsId,c.complaints_against as complaintsAgainst,c.complaints_date as complaintsDate,c.complaints_title as complaintsTitle,c.description,c.employee_id as employeeId,c.status,c.url,"
			+ " c.complaints_against_name as complaintsAgainstName"
			+ "	from complaints as c"
			+ "	join employee as e on e.employee_id=c.employee_id"
        	+ "     WHERE c.complaints_date BETWEEN:startDate and :endDate", nativeQuery = true)
	 List<Map<String, Object>> getAllcomplaintsDate(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	
	   ///////////trainee///////////
	@Query(value =" select e.user_name as userName,e.user_id as userId,c.complaints_id as complaintsId,c.complaints_against as complaintsAgainst,c.complaints_date as complaintsDate,c.complaints_title as complaintsTitle,c.description,c.trainee_id as traineeId,c.status,c.url,"
			+ " c.complaints_against_name as complaintsAgainstName"
			+ "	from complaints as c"
			+ "	join training_details as e on e.trainee_id=c.trainee_id"
        	+ "     WHERE c.complaints_date BETWEEN:startDate and :endDate", nativeQuery = true)
	 List<Map<String, Object>> getAllcomplaintsDatetrainee(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	
	
	

	@Query(value =" select e.user_name as userName,e.user_id as userId,c.complaints_id as complaintsId,c.complaints_against as complaintsAgainst,c.complaints_date as complaintsDate,c.complaints_title as complaintsTitle,c.description,c.employee_id as employeeId,c.status,c.url,r.role_id as roleId,r.role_name as roleName,"
			+ " c.complaints_against_name as complaintsAgainstName"
	        + " FROM complaints AS c"	    	
	        + " JOIN employee AS e ON e.employee_id = c.employee_id"
	        + " join role as r on r.role_id = e.role_id"
	        + " WHERE MONTHNAME(c.complaints_date) = :monthName AND e.employee_id = :employee_id"
	        + " and r.role_id=:role_id", nativeQuery = true)
	List<Map<String, Object>> getAllcomplaints(@Param("monthName") String monthName, @Param("employee_id") long employee_id, @Param("role_id") long role_id);

	
	@Query(value =" select e.user_name as userName,e.user_id as userId,c.complaints_id as complaintsId,c.complaints_against as complaintsAgainst,c.complaints_date as complaintsDate,c.complaints_title as complaintsTitle,c.description,c.employee_id as employeeId,c.status,c.url,r.role_id as roleId,r.role_name as roleName,"
			+ " c.complaints_against_name as complaintsAgainstName"
	        + " FROM complaints AS c"
	        + " JOIN employee AS e ON e.employee_id = c.employee_id"
	    	+ " join role as r on r.role_id = e.role_id"
	        + " WHERE YEAR(c.complaints_date) = :year AND e.employee_id = :employee_id"
	        + " and r.role_id=:role_id", nativeQuery = true)
	List<Map<String, Object>> getAllLeavecomplaints(@Param("year") String year, @Param("employee_id") long employee_id, @Param("role_id") long role_id);


	@Query(value =" select e.user_name as userName,e.user_id as userId,c.complaints_id as complaintsId,c.complaints_against as complaintsAgainst,c.complaints_date as complaintsDate,c.complaints_title as complaintsTitle,c.description,c.employee_id as employeeId,c.status,c.url,r.role_id as roleId,r.role_name as roleName,"
			+ " c.complaints_against_name as complaintsAgainstName"
			+ " FROM complaints AS c "
			+ " JOIN employee AS e ON e.employee_id = c.employee_id "
	    	+ " join role as r on r.role_id = e.role_id"
			+ " WHERE c.complaints_date BETWEEN :startDate AND :endDate AND e.employee_id = :employee_id"
			   + " and r.role_id=:role_id", nativeQuery = true)
List<Map<String, Object>> getAllReceiptcomplaintstrainee(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("role_id") long role_id,
        @Param("employee_id") long employee_id);
	
	@Query(value =" select e.user_name as userName,e.user_id as userId,c.complaints_id as complaintsId,c.complaints_against as complaintsAgainst,c.complaints_date as complaintsDate,c.complaints_title as complaintsTitle,c.description,c.trainee_id as traineeId,c.status,c.url,r.role_id as roleId,r.role_name as roleName,"
			+ " c.complaints_against_name as complaintsAgainstName"
	        + " FROM complaints AS c"	
	        + " JOIN training_details AS e ON e.trainee_id = c.trainee_id"
	    	+ " join role as r on r.role_id = e.role_id"
	        + " WHERE MONTHNAME(c.complaints_date) = :monthName AND e.trainee_id = :trainee_id"
	        + " and r.role_id=:role_id", nativeQuery = true)
	List<Map<String, Object>> getAllcomplaintstrainee(@Param("monthName") String monthName, @Param("trainee_id") long employee_id,@Param("role_id") long role_id);

	
	@Query(value =" select e.user_name as userName,e.user_id as userId,c.complaints_id as complaintsId,c.complaints_against as complaintsAgainst,c.complaints_date as complaintsDate,c.complaints_title as complaintsTitle,c.description,c.trainee_id as traineeId,c.status,c.url,r.role_id as roleId,r.role_name as roleName,"
			+ " c.complaints_against_name as complaintsAgainstName"
	        + " FROM complaints AS c"
	        + " JOIN training_details AS e ON e.trainee_id = c.trainee_id"
	    	+ " join role as r on r.role_id = e.role_id"
	        + " WHERE YEAR(c.complaints_date) = :year AND e.trainee_id = :trainee_id"
	        + " and r.role_id=:role_id", nativeQuery = true)
	List<Map<String, Object>> getAllLeavecomplaintstrainee(@Param("year") String year, @Param("trainee_id") long trainee_id,  @Param("role_id") long role_id);


	@Query(value =" select e.user_name as userName,e.user_id as userId,c.complaints_id as complaintsId,c.complaints_against as complaintsAgainst,c.complaints_date as complaintsDate,c.complaints_title as complaintsTitle,c.description,c.trainee_id as traineeId,c.status,c.url,r.role_id as roleId,r.role_name as roleName,"
			+ " c.complaints_against_name as complaintsAgainstName"
			+ " FROM complaints AS c "		
			+ " JOIN training_details AS e ON e.trainee_id = c.trainee_id "
			+ " join role as r on r.role_id = e.role_id"
			+ " WHERE c.complaints_date BETWEEN :startDate AND :endDate AND e.trainee_id = :trainee_id"
			 + " and r.role_id=:role_id", nativeQuery = true)
List<Map<String, Object>> getAllReceiptcomplaints(
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        @Param("role_id") long role_id,
        @Param("trainee_id") long trainee_id);



	Optional<Complaints> findByComplaintsTitleAndComplaintsAgainst(String complaintsTitle, Long complaintsAgainst);


}
