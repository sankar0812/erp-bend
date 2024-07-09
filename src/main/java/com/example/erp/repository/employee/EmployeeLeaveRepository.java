package com.example.erp.repository.employee;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.employee.EmployeeLeave;



public interface EmployeeLeaveRepository extends JpaRepository<EmployeeLeave, Long>{
	
	
	
	@Query(value = "WITH EmployeeLeaveCTE AS (SELECT \r\n"
			+ "        e.user_name AS userName,\r\n"
			+ "        em.user_name AS emp_name,\r\n"
			+ "        e.trainee_id AS traineeId,\r\n"
			+ "        em.employee_id AS employeeId,\r\n"
			+ "        l.date,\r\n"
			+ "        r.role_name AS roleName,\r\n"
			+ "        r.role_id AS roleId,\r\n"
			+ "        l.to_date AS toDate,\r\n"
			+ "        l.reason,\r\n"
			+ "        l.employee_leave_id AS employeeLeaveId,\r\n"
			+ "        DATEDIFF(l.to_date, l.date) + 1 AS totalDay,\r\n"
			+ "        l.leave_type AS leavetype,\r\n"
			+ "        l.cancellation_reason AS cancellationReason,\r\n"
			+ "        d.department_name AS departmentName,\r\n"
			+ "        COALESCE(e.department_id, em.department_id) AS departmentId\r\n"
			+ "    FROM employeeleave AS l\r\n"
			+ "    LEFT JOIN training_details AS e ON e.trainee_id = l.trainee_id\r\n"
			+ "    LEFT JOIN employee AS em ON em.employee_id = l.employee_id\r\n"
			+ "    JOIN role AS r ON r.role_id = COALESCE(e.role_id, em.role_id)\r\n"
			+ "    JOIN department AS d ON d.department_id = COALESCE(e.department_id, em.department_id)\r\n"
			+ "    WHERE l.date >= CURDATE()\r\n"
			+ " )SELECT * FROM EmployeeLeaveCTE"
			+ " ORDER BY employeeLeaveId DESC;"
		, nativeQuery = true)
	List<Map<String, Object>> getAllProjectWork();
	
	
	//sunday corrected query
	
//	@Query(value="WITH EmployeeLeaveCTE AS (\r\n"
//			+ "    SELECT\r\n"
//			+ "        e.user_name AS userName,\r\n"
//			+ "        em.user_name AS emp_name,\r\n"
//			+ "        e.trainee_id AS traineeId,\r\n"
//			+ "        em.employee_id AS employeeId,\r\n"
//			+ "        l.date,\r\n"
//			+ "        r.role_name AS roleName,\r\n"
//			+ "        r.role_id AS roleId,\r\n"
//			+ "        l.to_date AS toDate,\r\n"
//			+ "        l.reason,\r\n"
//			+ "        l.employee_leave_id AS employeeLeaveId,\r\n"
//			+ "        DATEDIFF(l.to_date, l.date) + 1 - \r\n"
//			+ "        round((DATEDIFF(DATE_ADD(l.to_date, INTERVAL 1-DAYOFWEEK(l.to_date) DAY), l.date) + 1) / 7) AS totalDay,\r\n"
//			+ "        l.leave_type AS leavetype,\r\n"
//			+ "        l.cancellation_reason AS cancellationReason,\r\n"
//			+ "        d.department_name AS departmentName,\r\n"
//			+ "        COALESCE(e.department_id, em.department_id) AS departmentId\r\n"
//			+ "    FROM\r\n"
//			+ "        employeeleave AS l\r\n"
//			+ "    LEFT JOIN\r\n"
//			+ "        training_details AS e ON e.trainee_id = l.trainee_id\r\n"
//			+ "    LEFT JOIN\r\n"
//			+ "        employee AS em ON em.employee_id = l.employee_id\r\n"
//			+ "    JOIN\r\n"
//			+ "        role AS r ON r.role_id = COALESCE(e.role_id, em.role_id)\r\n"
//			+ "    JOIN\r\n"
//			+ "        department AS d ON d.department_id = COALESCE(e.department_id, em.department_id)\r\n"
//			+ "    WHERE\r\n"
//			+ "        l.date >= CURDATE()\r\n"
//			+ " )\r\n"
//			+ " SELECT\r\n"
//			+ "    *\r\n"
//			+ " FROM\r\n"
//			+ "    EmployeeLeaveCTE\r\n"
//			+ " ORDER BY\r\n"
//			+ "    employeeLeaveId DESC;", nativeQuery = true)
//	List<Map<String, Object>> getAllProjectWork();
	
	@Query(value = "WITH EmployeeLeaveCTE AS (\r\n"
			+ "    SELECT \r\n"
			+ "        e.user_name as userName,\r\n"
			+ " em.user_name as emp_name,"
			+ "        e.trainee_id as traineeId,\r\n"
			+ "        em.employee_id as employeeId,\r\n"
			+ "        l.date,\r\n"
			+ "        r.role_name as roleName,\r\n"
			+ " r.role_id as roleId,"
			+ "        l.to_date as toDate,\r\n"
			+ "        l.reason,\r\n"
			+ "        l.employee_leave_id as employeeLeaveId,\r\n"
			+ "        DATEDIFF(l.to_date, l.date) + 1 as totalDay,\r\n"
			+ "        l.leave_type as leavetype,\r\n"
			+ "        l.cancellation_reason as cancellationReason,\r\n"
			+ "        d.department_name as departmentName,\r\n"
			+ "        COALESCE(e.department_id, em.department_id) as departmentId\r\n"
			+ "    FROM employeeleave as l\r\n"
			+ "    LEFT JOIN training_details as e ON e.trainee_id = l.trainee_id\r\n"
			+ "    LEFT JOIN employee as em ON em.employee_id = l.employee_id\r\n"
			+ "    JOIN role as r ON r.role_id = COALESCE(e.role_id, em.role_id)\r\n"
			+ "    JOIN department as d ON d.department_id = COALESCE(e.department_id, em.department_id)\r\n"
			+ "    WHERE l.pending = true AND l.date >= CURDATE()\r\n"
			+ " )\r\n"
			+ "SELECT * FROM EmployeeLeaveCTE; "
		, nativeQuery = true)
	List<Map<String, Object>> getAllProjectWorkaa();
	
	@Query(value = "select e.user_name as userName,e.user_id as userId,l.date,l.employee_id as employeeId,l.to_date as toDate,l.reason,l.employee_leave_id as employeeLeaveId,"
			+ "  DATEDIFF(l.to_date, l.date) + 1 as totalDay,l.leave_type as leavetype,l.cancellation_reason as cancellationReason, d.department_name as departmentName,e.department_id as departmentId"
			+ "			 from employeeleave as l "
			+ "			 join employee as e on e.employee_id=l.employee_id "
			+ "  join department as d on d.department_id =e.department_id"
			+ "  where l.completed=true and"
			+ " l.to_date >= curdate();"
		, nativeQuery = true)
	List<Map<String, Object>> getAllProjectWorkaaggg();
	
	
	@Query(value = "SELECT "
			+ "    e.user_name as userName,"
			+ "    e.trainee_id as traineeId,"
			+ "    l.date,"
			+ "    l.to_date as toDate,"
			+ "    l.reason,"
			+ "    l.employee_leave_id as employeeLeaveId,"
			+ "    e.url,"
			+ "    DATEDIFF(l.to_date, l.date) + 1 as totalDay,"
			+ "    l.leave_type as leavetype,"
			+ "    l.cancellation_reason as cancellationReason,"
			+ "    d.department_name as departmentName,"
			+ " l.reason_description as reasonDescription,"
			+ "    e.department_id as departmentId"
			+ " FROM "
			+ "    employeeleave as l"
			+ " JOIN "
			+ "    training_details as e ON e.trainee_id = l.trainee_id"
			+ " JOIN "
			+ "    department as d ON d.department_id = e.department_id"
			+ " WHERE "
			+ "    l.pending = true"
			+ "    AND l.date >= curdate();"
		, nativeQuery = true)
	List<Map<String, Object>> getAllProjectWorktraning();
	
	
	@Query(value = "SELECT "
			+ "    e.user_name as userName,"
			+ "    e.employee_id as employeeId,"
			+ "    l.date,r.role_id as roleId,r.role_name as roleName,"
			+ "    l.to_date as toDate,"
			+ "    l.reason,"
			+ "    l.employee_leave_id as employeeLeaveId,"
			+ "    e.url,"
			+ "    DATEDIFF(l.to_date, l.date) + 1 as totalDay,"
			+ "    l.leave_type as leavetype,"
			+ "    l.cancellation_reason as cancellationReason,"
			+ "    d.department_name as departmentName,"
			+ " l.reason_description as reasonDescription,"
			+ "    e.department_id as departmentId"
			+ " FROM "
			+ "    employeeleave as l"
			+ " JOIN "
			+ "    employee as e ON e.employee_id = l.employee_id"
			+ " join role as r on r.role_id = e.role_id"
			+ " JOIN "
			+ "    department as d ON d.department_id = e.department_id"
			+ " WHERE "
			+ " l.employee_id=:employee_id and "
			+ " r.role_id = :role_id "
			+ "     AND month(l.date) >= month(curdate());"
		, nativeQuery = true)
	List<Map<String, Object>> getAllProjectWorktraningWithEmployee(@Param("employee_id") long employee_id, @Param("role_id") long role_id);
	
	@Query(value = "SELECT "
			+ "    e.user_name as userName,"
			+ "    e.trainee_id as traineeId,"
			+ "    l.date,r.role_id as roleId,r.role_name as roleName,"
			+ "    l.to_date as toDate,"
			+ "    l.reason,"
			+ "    l.employee_leave_id as employeeLeaveId,"
			+ "    e.url,"
			+ "    DATEDIFF(l.to_date, l.date) + 1 as totalDay,"
			+ "    l.leave_type as leavetype,"
			+ "    l.cancellation_reason as cancellationReason,"
			+ " l.reason_description as reasonDescription,"
			+ "    d.department_name as departmentName,"
			+ "    e.department_id as departmentId"
			+ " FROM "
			+ "    employeeleave as l"
			+ " JOIN "
			+ "    training_details as e ON e.trainee_id = l.trainee_id"
			+ " join role as r on r.role_id = e.role_id"
			+ " JOIN "
			+ "    department as d ON d.department_id = e.department_id"
			+ " WHERE "
			+ " l.trainee_id=:trainee_id and"
			+ " r.role_id = :role_id "
			+ "    AND month(l.date) >= month(curdate());"
		, nativeQuery = true)
	List<Map<String, Object>> getAllProjectWorktraningWithTrainee(@Param("trainee_id") long trainee_id, @Param("role_id") long role_id);
	
	@Query(value = "select e.user_name as userName, e.user_id as userId, l.date, l.employee_id as employeeId, l.to_date as toDate, l.reason, l.employee_leave_id as employeeLeaveId,"
			+ " DATEDIFF(l.to_date, l.date) + 1 as totalDay, l.leave_type as leavetype, l.cancellation_reason as cancellationReason, d.department_name as departmentName ,e.department_id as departmentId "
			+ " from employeeleave as l "
			+ " join employee as e on e.employee_id = l.employee_id "
			+ " join department as d on d.department_id = e.department_id"
			+ " WHERE l.completed = true and d.department_id = :department_id", nativeQuery = true)
	List<Map<String, Object>> getAllProjectWorkout(@Param("department_id") long department_id);
	
	@Query(value = "select e.user_name as userName,e.user_id as userId,l.date,l.employee_id as employeeId,l.to_date as toDate,l.reason,l.employee_leave_id as employeeLeaveId,"
			+ " DATEDIFF(l.to_date, l.date) + 1 as totalDay,l.leave_type as leavetype,l.cancellation_reason as cancellationReason, d.department_name as departmentName"
			+ "	from employeeleave as l"
			+ "	join employee as e on e.employee_id=l.employee_id "
			+ " join department as d on d.department_id =e.department_id "
			+ " where l.employee_id=:employeeId", nativeQuery = true)
	List<Map<String, Object>> Allemployeeleave(@Param("employeeId") long employeeId);

	
	@Query(value = "SELECT e.user_name,e.user_id, el.employee_id, lt.leave_type, sum(el.total_day) AS leave_count"
			+ " FROM employeeleave AS el"
			+ " JOIN employee AS e ON e.employee_id = el.employee_id "
			+ " JOIN leavetype AS lt ON lt.leave_type_id = el.leave_type_id"
			+ " WHERE el.employee_id = :employee_id AND el.date >= DATE_SUB(CURRENT_DATE, INTERVAL 1 MONTH) AND el.date <= CURRENT_DATE"
			+ " GROUP BY e.first_name, el.employee_id, lt.leave_type;", nativeQuery = true)
	List<Map<String, Object>> getAllProject(@Param("employee_id") long employee_id);
	
	

	@Query(value = "select e.user_name as userName, e.user_id as userId, l.date, l.employee_id as employeeId, l.to_date as toDate, l.reason, l.employee_leave_id as employeeLeaveId,"
	        + " DATEDIFF(l.to_date, l.date) + 1 as totalDay, l.leave_type as leavetype, l.cancellation_reason as cancellationReason, d.department_name as departmentName, e.department_id as departmentId"
	        + " from employeeleave as l"
	        + " join employee as e on e.employee_id = l.employee_id"
	        + " join department as d on d.department_id = e.department_id"
	    	+ " join role as r on r.role_id = e.role_id"
	        + " where monthname(l.date) = :monthName", nativeQuery = true)
	List<Map<String, Object>> getAllleave(@Param("monthName") String monthName);

////////////////trainee///////////////////
	@Query(value = "select e.user_name as userName, e.user_id as userId, l.date, l.trainee_id as traineeId, l.to_date as toDate, l.reason, l.employee_leave_id as employeeLeaveId,"
	        + " DATEDIFF(l.to_date, l.date) + 1 as totalDay, l.leave_type as leavetype, l.cancellation_reason as cancellationReason, d.department_name as departmentName, e.department_id as departmentId"
	        + " from employeeleave as l"
	        + "  join training_details as e on e.trainee_id = l.trainee_id"
	        + " join department as d on d.department_id = e.department_id"
	        + " where monthname(l.date) = :monthName", nativeQuery = true)
	List<Map<String, Object>> getAllleavetrainee(@Param("monthName") String monthName);

	@Query(value = "select e.user_name as userName, e.user_id as userId, l.date, l.employee_id as employeeId, l.to_date as toDate, l.reason, l.employee_leave_id as employeeLeaveId,r.role_id as roleId,r.role_name as roleName,"
	        + " DATEDIFF(l.to_date, l.date) + 1 as totalDay, l.leave_type as leavetype, l.cancellation_reason as cancellationReason, d.department_name as departmentName, e.department_id as departmentId"
	        + " from employeeleave as l"
	        + " join employee as e on e.employee_id = l.employee_id"
	        + " join department as d on d.department_id = e.department_id"
	        + " where year(l.date) = :year", nativeQuery = true)
	List<Map<String, Object>> getAllLeaveYear(@Param("year") String year);

////////////////trainee///////////////////
	@Query(value = "select e.user_name as userName, e.user_id as userId, l.date, l.trainee_id as traineeId, l.to_date as toDate, l.reason, l.employee_leave_id as employeeLeaveId,r.role_id as roleId,r.role_name as roleName,"
	        + " DATEDIFF(l.to_date, l.date) + 1 as totalDay, l.leave_type as leavetype, l.cancellation_reason as cancellationReason, d.department_name as departmentName, e.department_id as departmentId"
	        + " from employeeleave as l"
	        + "  join training_details as e on e.trainee_id = l.trainee_id"
	    	+ " join role as r on r.role_id = e.role_id"
	        + " join department as d on d.department_id = e.department_id"
	        + " where year(l.date) = :year", nativeQuery = true)
	List<Map<String, Object>> getAllLeaveYearTrainee(@Param("year") String year);
	
	@Query(value = "select e.user_name as userName,e.user_id as userId,l.date,l.employee_id as employeeId,l.to_date as toDate,l.reason,l.employee_leave_id as employeeLeaveId,r.role_id as roleId,r.role_name as roleName,"
			+ "			DATEDIFF(l.to_date, l.date) + 1 as totalDay,l.leave_type as leavetype,l.cancellation_reason as cancellationReason, d.department_name as departmentName,e.department_id as departmentId"
			+ "			 from employeeleave as l "
			+ "			 join employee as e on e.employee_id=l.employee_id "
			+ " join role as r on r.role_id = e.role_id"
			+ "			join department as d on d.department_id =e.department_id"
        	+ "     WHERE l.date BETWEEN:startDate and :endDate", nativeQuery = true)
	 List<Map<String, Object>> getAllReceiptBetweenDate(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	
	
	////////////////trainee///////////////////
	@Query(value = "select e.user_name as userName,e.user_id as userId,l.date,l.trainee_id as traineeId,l.to_date as toDate,l.reason,l.employee_leave_id as employeeLeaveId,r.role_id as roleId,r.role_name as roleName,"
			+ "			DATEDIFF(l.to_date, l.date) + 1 as totalDay,l.leave_type as leavetype,l.cancellation_reason as cancellationReason, d.department_name as departmentName,e.department_id as departmentId"
			+ "			 from employeeleave as l "
		    + "  join training_details as e on e.trainee_id = l.trainee_id"
			+ " join role as r on r.role_id = e.role_id"
			+ "			join department as d on d.department_id =e.department_id"
        	+ "     WHERE l.date BETWEEN:startDate and :endDate", nativeQuery = true)
	 List<Map<String, Object>> getAllReceiptBetweenDateTrainee(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);
	
	

	@Query(value = "select e.user_name as userName, e.user_id as userId, l.date, l.employee_id as employeeId, l.to_date as toDate, l.reason, l.employee_leave_id as employeeLeaveId,r.role_id as roleId,r.role_name as roleName,"
	        + " DATEDIFF(l.to_date, l.date) + 1 as totalDay, l.leave_type as leavetype, l.cancellation_reason as cancellationReason, d.department_name as departmentName, e.department_id as departmentId"
	        + " from employeeleave as l"
	        + " join employee as e on e.employee_id = l.employee_id"
	    	+ " join role as r on r.role_id = e.role_id"
	        + " join department as d on d.department_id = e.department_id"
	        + " where monthname(l.date) = :monthName"
	        + " and e.employee_id = :employee_id"
	        + " and r.role_id=:role_id", nativeQuery = true)
	List<Map<String, Object>> getAllleave1(@Param("monthName") String monthName,@Param("employee_id") long employee_id,@Param("role_id") long role_id);

	

	@Query(value = "select e.user_name as userName, e.user_id as userId, l.date, l.employee_id as employeeId, l.to_date as toDate, l.reason, l.employee_leave_id as employeeLeaveId,r.role_id as roleId,r.role_name as roleName,"
	        + " DATEDIFF(l.to_date, l.date) + 1 as totalDay, l.leave_type as leavetype, l.cancellation_reason as cancellationReason, d.department_name as departmentName, e.department_id as departmentId"
	        + " from employeeleave as l"
	        + " join employee as e on e.employee_id = l.employee_id"
	    	+ " join role as r on r.role_id = e.role_id"
	        + " join department as d on d.department_id = e.department_id"
	        + " where year(l.date) = :year"
	        + " and e.employee_id = :employee_id"
	        + " and r.role_id=:role_id", nativeQuery = true)
	List<Map<String, Object>> getAllLeaveYear1(@Param("year") String year,@Param("employee_id") long employee_id,@Param("role_id") long role_id);

	@Query(value = "SELECT e.user_name AS userName, e.user_id AS userId, l.date, l.employee_id AS employeeId, l.to_date AS toDate, l.reason, l.employee_leave_id AS employeeLeaveId,r.role_id as roleId,r.role_name as roleName,"
	        + " DATEDIFF(l.to_date, l.date) + 1 AS totalDay, l.leave_type AS leavetype, l.cancellation_reason AS cancellationReason, d.department_name AS departmentName, e.department_id AS departmentId"
	        + " FROM employeeleave AS l"
	        + " JOIN employee AS e ON e.employee_id = l.employee_id"
	    	+ " join role as r on r.role_id = e.role_id"
	        + " JOIN department AS d ON d.department_id = e.department_id"
	        + " WHERE l.date BETWEEN :startDate AND :endDate AND e.employee_id = :employee_id"
	        + " and r.role_id=:role_id", nativeQuery = true)
	List<Map<String, Object>> getAllReceiptBetweenDate1(
	        @Param("startDate") LocalDate startDate,
	        @Param("endDate") LocalDate endDate,
	        @Param("role_id") long role_id,
	        @Param("employee_id") long employee_id);
	
	

	@Query(value = "select e.user_name as userName, e.user_id as userId, l.date, l.trainee_id as traineeId, l.to_date as toDate, l.reason, l.employee_leave_id as employeeLeaveId,r.role_id as roleId,r.role_name as roleName, "
	        + " DATEDIFF(l.to_date, l.date) + 1 as totalDay, l.leave_type as leavetype, l.cancellation_reason as cancellationReason, d.department_name as departmentName, e.department_id as departmentId"
	        + " from employeeleave as l"
	        + " join training_details as e on e.trainee_id = l.trainee_id"
	        + " join department as d on d.department_id = e.department_id "
			+ " join role as r on r.role_id = e.role_id"
	        + " where monthname(l.date) = :monthName"
	        + " and e.trainee_id = :trainee_id"
	        + " and r.role_id=:role_id", nativeQuery = true)
	List<Map<String, Object>> getAllleavetraineeId(@Param("monthName") String monthName,@Param("trainee_id") long employee_id,@Param("role_id") long role_id);

	

	@Query(value = "select e.user_name as userName, e.user_id as userId, l.date, l.trainee_id as traineeId, l.to_date as toDate, l.reason, l.employee_leave_id as employeeLeaveId,r.role_id as roleId,r.role_name as roleName,"
	        + " DATEDIFF(l.to_date, l.date) + 1 as totalDay, l.leave_type as leavetype, l.cancellation_reason as cancellationReason, d.department_name as departmentName, e.department_id as departmentId"
	        + " from employeeleave as l"
	        + " join training_details as e on e.trainee_id = l.trainee_id"
	        + " join department as d on d.department_id = e.department_id"
			+ " join role as r on r.role_id = e.role_id"
	        + " where year(l.date) = :year"
	        + " and e.trainee_id = :trainee_id"
	        + " and r.role_id=:role_id", nativeQuery = true)
	List<Map<String, Object>> getAllLeaveYeartraineeId(@Param("year") String year,@Param("trainee_id") long employee_id,@Param("role_id") long role_id);

	@Query(value = "SELECT e.user_name AS userName, e.user_id AS userId, l.date, l.trainee_id AS traineeId, l.to_date AS toDate, l.reason, l.employee_leave_id AS employeeLeaveId,r.role_id as roleId,r.role_name as roleName,"
	        + " DATEDIFF(l.to_date, l.date) + 1 AS totalDay, l.leave_type AS leavetype, l.cancellation_reason AS cancellationReason, d.department_name AS departmentName, e.department_id AS departmentId"
	        + " FROM employeeleave AS l"
	        + " JOIN training_details AS e ON e.trainee_id = l.trainee_id"
	        + " JOIN department AS d ON d.department_id = e.department_id"
			+ " join role as r on r.role_id = e.role_id"
	        + " WHERE l.date BETWEEN :startDate AND :endDate AND e.trainee_id = :trainee_id"
	        + " and r.role_id=:role_id", nativeQuery = true)
	List<Map<String, Object>> getAllReceiptBetweenDatetraineeId(
	        @Param("startDate") LocalDate startDate,
	        @Param("endDate") LocalDate endDate,
	        @Param("trainee_id") long trainee_id,
	 @Param("role_id") long role_id);
	
	

	@Query(value = "SELECT\r\n"
			+ "    e.user_name AS userName,\r\n"
			+ "    e.user_id AS userId,\r\n"
			+ "    l.date,\r\n"
			+ "    l.employee_id AS employeeId,\r\n"
			+ "    l.to_date AS toDate,\r\n"
			+ "    l.reason,\r\n"
			+ "    r.role_name AS roleName,\r\n"
			+ "    l.employee_leave_id AS employeeLeaveId,\r\n"
			+ "    DATEDIFF(l.to_date, l.date) + 1 AS totalDay,\r\n"
			+ "    l.leave_type AS leavetype,\r\n"
			+ "    l.cancellation_reason AS cancellationReason,\r\n"
			+ "    d.department_name AS departmentName,\r\n"
			+ "    e.department_id AS departmentId\r\n"
			+ "FROM\r\n"
			+ "    employeeleave AS l\r\n"
			+ "JOIN\r\n"
			+ "    employee AS e ON e.employee_id = l.employee_id\r\n"
			+ "JOIN\r\n"
			+ "    role AS r ON r.role_id = e.role_id\r\n"
			+ "JOIN\r\n"
			+ "    department AS d ON d.department_id = e.department_id\r\n"
			+ "WHERE\r\n"
			+ "    l.employee_id =:id"
			+ "    AND e.role_id =:roleId"
			+ "    AND l.date >= CURDATE();"
		, nativeQuery = true)
	List<Map<String, Object>> getAllProjectWorktraningWithTraineeleave(@Param("id") long trainee_id, @Param("roleId") long role_id);

	
	@Query(value = "SELECT \r\n"
			+ "    e.user_name as userName,\r\n"
			+ "    e.user_id as userId,\r\n"
			+ "    l.date,\r\n"
			+ "    l.trainee_id as traineeId,\r\n"
			+ "    l.to_date as toDate,\r\n"
			+ "    l.reason,\r\n"
			+ "    r.role_name as roleName,\r\n"
			+ "    l.employee_leave_id as employeeLeaveId,\r\n"
			+ "    DATEDIFF(l.to_date, l.date) + 1 as totalDay,\r\n"
			+ "    l.leave_type as leavetype,\r\n"
			+ "    l.cancellation_reason as cancellationReason,\r\n"
			+ "    d.department_name as departmentName,\r\n"
			+ "    e.department_id as departmentId"
			+ " FROM \r\n"
			+ "    employeeleave as l\r\n"
			+ " JOIN \r\n"
			+ "   training_details as e ON e.trainee_id = l.trainee_id\r\n"
			+ "    join role as r on r.role_id=e.role_id\r\n"
			+ " JOIN \r\n"
			+ "    department as d ON d.department_id = e.department_id\r\n"
			+ " WHERE \r\n"
			+ "    l.trainee_id=:id\r\n"
			+ " and e.role_id =:roleId"
			+ "  and l.date >= curdate()"
		, nativeQuery = true)
	List<Map<String, Object>> getAllProjectWorktraningWithTraineeEmployee(@Param("id") long trainee_id, @Param("roleId") long role_id);


	Optional<EmployeeLeave> findByEmployeeId(long id);

	Optional<EmployeeLeave> findByEmployeeIdAndEmployeeLeaveId(long id, long employeeLeaveId);
	
	@Query(value="select e.employee_id,ml.all_dates from employeeleave as e"
			+ " join multi_leaves as ml on ml.employee_leave_id = e.employee_leave_id"
			+ " where e.employee_id = :employeeId and ml.all_dates = :date and e.completed=true", nativeQuery = true)
	Map<String, Object> getAllEmpLeave(Long employeeId, Date date);
	
	
	@Query(value="select e.trainee_id,ml.all_dates from employeeleave as e"
			+ " join multi_leaves as ml on ml.employee_leave_id = e.employee_leave_id"
			+ " where e.trainee_id = :traineeId and ml.all_dates = :date and e.completed=true", nativeQuery = true)
	Map<String, Object> getAllTrainingLeave(Long traineeId, Date date);


	Optional<EmployeeLeave> findByEmployeeIdAndDateAndToDate(Long employeeId, Date date, Date toDate);


	Optional<EmployeeLeave> findByTraineeIdAndDateAndToDate(long traineeId, Date date, Date toDate);
	

	
}
