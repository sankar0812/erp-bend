package com.example.erp.repository.employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.employee.EmployeeAttendance;

public interface EmployeeAttendanceRepository extends JpaRepository<EmployeeAttendance, Long> {

	Optional<EmployeeAttendance> findByEmployeeIdAndInDate(Long employeeId, LocalDate inDate);
	
	Optional<EmployeeAttendance> findByTraineeIdAndInDate(Long employeeId, LocalDate inDate);

	//Optional<EmployeeAttendance> findByEmployeeIdAndInDateAndPunchOutTrue(Long employeeId, LocalDate inDate);


	@Query(value = "  SELECT "
			+ "    e.user_name,e.user_id, "
			+ "    e.employee_id, "
			+ "    a.employee_att_id, "
			+ "    a.in_time, "
			+ "    a.out_time, "
			+ "    a.attendance, "
			+ "    a.in_date, "
			+ "    a.punch_in, "
			+ "    a.punch_out, "
			+ "    TIMEDIFF("
			+ "        STR_TO_DATE(a.out_time, '%h:%i %p'),"
			+ "        STR_TO_DATE(a.in_time, '%h:%i %p')"
			+ "    ) as working_hour,"
			+ "    CASE"
			+ "        WHEN a.employee_att_id IS NULL THEN 'Absent'"
			+ "        ELSE 'Present'"
			+ "    END AS current_attendance"
			+ " FROM employee e "
			+ " LEFT JOIN employee_att a ON e.employee_id = a.employee_id;",nativeQuery = true)
List<Map<String,Object>>GoodAllWorks();
	
	
	
	@Query(value = " SELECT"
			+ "    e.user_name as userName,"
			+ "    e.user_id as userId,"
			+ "    e.trainee_id as traineeId,"
			+ "    a.employee_att_id as employeeAttId,"
			+ "    a.in_time as inTime,"
			+ "    a.out_time as outTime,"
			+ "    a.attendance,"
			+ "    a.in_date as inDate,"
			+ "    a.punch_in as punchIn,"
			+ "    a.punch_out as punchOut,"
			+ "    a.working_hour as workingHour,"
			+ " a.ip_address as ipAddress"
			+ " FROM"
			+ "    training_details as  e"
			+ " LEFT JOIN"
			+ "    employee_att as a ON e.trainee_id = a.trainee_id"
			+ " WHERE"
			+ "    DATE(a.in_date) = CURDATE();"
			+ ";",nativeQuery = true)
List<Map<String,Object>>GoodAllWorkstrainee();
	
	@Query(value = " SELECT\r\n"
			+ "    e.employee_id,\r\n"
			+ "    e.user_name,\r\n"
			+ "    e.user_id,\r\n"
			+ "    e.url,\r\n"
			+ "    SUM(ea.working_hour) as workingHours,\r\n"
			+ "    d.department_name,\r\n"
			+ "    de.designation_name,\r\n"
			+ "    COUNT(DISTINCT DATE(ea.in_date)) AS present_days,\r\n"
			+ "    total_days.total_days AS total_days,\r\n"
			+ "    total_days.total_days - COUNT(DISTINCT DATE(ea.in_date)) AS absent_days\r\n"
			+ "\r\n"
			+ "FROM\r\n"
			+ "    employee e\r\n"
			+ "LEFT JOIN\r\n"
			+ "    employee_att ea ON e.employee_id = ea.employee_id AND MONTH(ea.in_date) = MONTH(CURRENT_DATE()) AND YEAR(ea.in_date) = YEAR(CURRENT_DATE())\r\n"
			+ "JOIN\r\n"
			+ "    department d ON d.department_id = e.department_id\r\n"
			+ "JOIN\r\n"
			+ "    designation de ON de.designation_id = e.designation_id\r\n"
			+ "JOIN\r\n"
			+ "    (SELECT COUNT(DISTINCT DATE(in_date)) AS total_days FROM employee_att WHERE MONTH(in_date) = MONTH(CURRENT_DATE()) AND YEAR(in_date) = YEAR(CURRENT_DATE())) AS total_days\r\n"
			+ "\r\n"
			+ "GROUP BY\r\n"
			+ "    e.employee_id, e.user_name, e.user_id, e.url, d.department_name, de.designation_name, total_days.total_days\r\n"
			+ "\r\n"
			+ "", nativeQuery = true)
	List<Map<String, Object>> GoodAllWorks22();

	@Query(value = " SELECT e.trainee_id,e.user_name,e.user_id, e.url,SUM(ea.working_hour) as workingHours,d.department_name,\r\n"
			+ " COUNT(DISTINCT DATE(ea.in_date)) AS present_days,total_days.total_days AS total_days,total_days.total_days - COUNT(DISTINCT DATE(ea.in_date)) AS absent_days\r\n"
			+ " FROM training_details as  e\r\n"
			+ " LEFT JOIN employee_att ea ON e.trainee_id = ea.trainee_id AND MONTH(ea.in_date) = MONTH(CURRENT_DATE()) AND YEAR(ea.in_date) = YEAR(CURRENT_DATE())\r\n"
			+ " JOIN department d ON d.department_id = e.department_id\r\n"
			+ " JOIN (SELECT COUNT(DISTINCT DATE(in_date)) AS total_days FROM employee_att WHERE MONTH(in_date) = MONTH(CURRENT_DATE()) AND YEAR(in_date) = YEAR(CURRENT_DATE())) AS total_days\r\n"
			+ " GROUP BY ne.trainee_id, e.user_name, e.user_id, e.url, d.department_name,total_days.total_days;", nativeQuery = true)
	List<Map<String, Object>> GoodTrainingDetailes();

	@Query(value = "SELECT e.user_name,e.user_id,e.employee_id,a.employee_att_id,a.in_time,a.out_time,a.attendance,a.in_date,r.role_id,r.role_name,a.punch_in,a.punch_out,\r\n"
			+ "    a. working_hour,\r\n"
			+ "    CASE\r\n"
			+ "        WHEN a.employee_att_id IS NULL THEN 'Absent'\r\n"
			+ "        ELSE 'Present'\r\n"
			+ " END AS current_attendance\r\n"
			+ " FROM employee e\r\n"
			+ " LEFT JOIN employee_att a ON e.employee_id = a.employee_id\r\n"
			+ " JOIN role AS r ON r.role_id = e.role_id\r\n"
			+ " WHERE\r\n"
			+ "    e.employee_id = :employeeId AND r.role_id = :role_id AND DATE(a.in_date) = CURRENT_DATE;",nativeQuery = true)
List<Map<String,Object>>AttendanceforEmployeeId1(@Param("employeeId") long employeeId,@Param("role_id") long role_id);
	
	
	@Query(value = "SELECT  e.trainee_id as traineeId,null as employeeId,e.user_name as userName,e.user_id as userId,e.url,SUM(ea.working_hour) as workingHours,d.department_name as departmentName,r.role_id as roleId,\r\n"
			+ " COUNT(DISTINCT DATE(ea.in_date)) AS presentDays,total_days.total_days AS totalDays,total_days.total_days - COUNT(DISTINCT DATE(ea.in_date)) AS absentDays\r\n"
			+ " FROM training_details as e\r\n"
			+ " JOIN role as r ON r.role_id = e.role_id\r\n"
			+ " LEFT JOIN employee_att ea ON e.trainee_id = ea.trainee_id AND MONTH(ea.in_date) = MONTH(CURRENT_DATE()) AND YEAR(ea.in_date) = YEAR(CURRENT_DATE())\r\n"
			+ " JOIN department d ON d.department_id = e.department_id\r\n"
			+ " JOIN(SELECT COUNT(DISTINCT DATE(in_date)) AS total_days FROM employee_att WHERE MONTH(in_date) = MONTH(CURRENT_DATE()) AND YEAR(in_date) = YEAR(CURRENT_DATE())) AS total_days\r\n"
			+ " WHERE e.trainee_id =:id AND r.role_id =:role_id \r\n"
			+ " GROUP BY e.trainee_id, e.user_name, e.user_id, e.url, d.department_name, total_days.total_days, r.role_id\r\n"
			+ " UNION ALL\r\n"
			+ " SELECT  null as traineeId,e.employee_id as employeeId,e.user_name as userUame,e.user_id userId,e.url,SUM(ea.working_hour) as workingHours,d.department_name as departmentName,e.role_id as roleId,"
			+ " COUNT(DISTINCT DATE(ea.in_date)) AS presentDays,total_days.total_days AS total_days,total_days.total_days - COUNT(DISTINCT DATE(ea.in_date)) AS absentDays\r\n"
			+ " FROM employee as e\r\n"
			+ " JOIN role as r ON r.role_id = e.role_id\r\n"
			+ " LEFT JOIN employee_att ea ON e.employee_id = ea.employee_id AND MONTH(ea.in_date) = MONTH(CURRENT_DATE()) AND YEAR(ea.in_date) = YEAR(CURRENT_DATE())\r\n"
			+ " JOIN department d ON d.department_id = e.department_id\r\n"
			+ " JOIN designation de ON de.designation_id = e.designation_id\r\n"
			+ " JOIN(SELECT COUNT(DISTINCT DATE(in_date)) AS total_days FROM employee_att WHERE MONTH(in_date) = MONTH(CURRENT_DATE()) AND YEAR(in_date) = YEAR(CURRENT_DATE())) AS total_days\r\n"
			+ " WHERE e.employee_id =:id AND e.role_id =:role_id"
			+ " GROUP BY e.employee_id, e.user_name, e.user_id, e.url, d.department_name, total_days.total_days, e.role_id;",nativeQuery = true)
List<Map<String,Object>>AttendanceforEmployeeId1Traineee(@Param("id") long employeeId,@Param("role_id") long role_id);
	
	
	@Query(value = " SELECT"
			+ "    e.user_name ,"
			+ "    e.user_id ,"
			+ "    e.trainee_id ,"
			+ "    a.employee_att_id ,"
			+ " r.role_id,"
			+ " r.role_name,"
			+ "    a.in_time ,"
			+ "    a.out_time ,"
			+ "    a.attendance,"
			+ "    a.in_date ,"
			+ "    a.punch_in ,"
			+ "    a.punch_out ,"
			+ "    a.working_hour "
			+ " FROM"
			+ "    training_details as  e"
			+ " JOIN role AS r ON r.role_id = e.role_id"
			+ " LEFT JOIN"
			+ "    employee_att as a ON e.trainee_id = a.trainee_id"
			+ " WHERE "
			+ " e.trainee_id= :trainee_id"			
			+ " and r.role_id =:role_id"
			+ " and "			
			+ "    DATE(a.in_date) = CURDATE()"
			+ ";",nativeQuery = true)
	List<Map<String,Object>>Attendancefortrainee(@Param("trainee_id") long trainee_id,@Param("role_id") long role_id);
	

	@Query(value = " SELECT"
			+ " e.employee_id,r.role_id,"
			+ " (SELECT SUM(working_hour) FROM employee_att  WHERE DATE(in_date) = CURDATE() AND employee_id = e.employee_id) AS total_working_hour,"
			+ " TIME_FORMAT(TIMEDIFF(CURRENT_TIME(), TIME(MAX(e.in_time))), '%H:%i:%s') AS to_day,"
			+ " (SELECT SUM(working_hour) FROM employee_att WHERE WEEK(in_date) = WEEK(MAX(e.in_date)) AND employee_id = e.employee_id) AS weekly_working_hour,"
			+ " (SELECT SUM(working_hour) FROM employee_att WHERE MONTH(in_date) = MONTH(MAX(e.in_date)) AND employee_id = e.employee_id) AS monthly_working_hour"
			+ " FROM employee_att AS e"
			+ " join employee as em on em.employee_id=e.employee_id"
			+ " join role as r on r.role_id=em.role_id"
			+ " WHERE e.employee_id = :employeeId and r.role_id = :role_id",nativeQuery = true)
	List<Map<String,Object>> AttendanceforEmployeeId(@Param("employeeId") long employeeId,@Param("role_id") long role_id);
	
	
	
	@Query(value = "SELECT "
			+ " e.trainee_id,r.role_id,"
			+ " (SELECT SUM(working_hour) FROM employee_att  WHERE DATE(in_date) = CURDATE() AND trainee_id = e.trainee_id) AS total_working_hour,"
			+ " TIME_FORMAT(TIMEDIFF(CURRENT_TIME(), TIME(MAX(e.in_time))), '%H:%i:%s') AS to_day,"
			+ " (SELECT SUM(working_hour) FROM employee_att WHERE WEEK(in_date) = WEEK(MAX(e.in_date)) AND trainee_id = e.trainee_id) AS weekly_working_hour,"
			+ " (SELECT SUM(working_hour) FROM employee_att WHERE MONTH(in_date) = MONTH(MAX(e.in_date)) AND trainee_id = e.trainee_id) AS monthly_working_hour"
			+ " FROM employee_att AS e"
			+ " join training_details as em on em.trainee_id=e.trainee_id"
			+ " join role as r on r.role_id=em.role_id"
			+ " WHERE e.trainee_id = :trainee_id and r.role_id = :role_id", nativeQuery = true)
	List<Map<String, Object>> AttendanceforEmployeeIdtrainee(@Param("trainee_id") long employeeId, @Param("role_id") long role_id);

	
	
//	@Query(value = " SELECT"
//			+ "    e.employee_id,"
//			+ "        monthname(total_days.date) AS month,"
//			+ "    month(total_days.date) AS monthname,"
//			+ "    COUNT(CASE WHEN ea.in_date IS NOT NULL THEN 1 END) AS presentPercentage,"
//			+ "    COUNT(CASE WHEN ea.in_date IS NULL THEN 1 END) AS absentPercentage,"
//			+ "     COUNT(month(total_days.date) = month(current_date())) AS totalWorkingDays"
//			+ " FROM"
//			+ "    (SELECT DISTINCT DATE(in_date) AS date FROM employee_att) total_days"
//			+ " CROSS JOIN"
//			+ "    employee e"
//			+ " LEFT JOIN"
//			+ "    employee_att ea"
//			+ " ON"
//			+ "    e.employee_id = ea.employee_id"
//			+ "    AND total_days.date = DATE(ea.in_date)"
//			+ " WHERE e.employee_id = :employeeId"
//			+ " GROUP BY"
//			+ "    e.employee_id,"
//			+ "    monthname(total_days.date),"
//			+ "     month(total_days.date)"
//			+ " ORDER BY"
//			+ "    e.employee_id,"
//			+ "      month(total_days.date);"
//			, nativeQuery = true)
//List<Map<String, Object>> AttendanceForEmployeeId123(@Param("employeeId") long employeeId);

	@Query(value = "SELECT "
	        + " e.employee_id,"
	        + " r.role_id, "
	        + " monthname(total_days.date) AS month, "
	        + " year(total_days.date) AS year, "
	        + " month(total_days.date) AS monthnum, "
	        + " COUNT(CASE WHEN ea.in_date IS NOT NULL THEN 1 END) AS presentPercentage, "
	        + " COUNT(CASE WHEN ea.in_date IS NULL THEN 1 END) AS absentPercentage, "
	        + " COUNT(month(total_days.date) = month(current_date())) AS totalWorkingDays "
	        + " FROM "
	        + " (SELECT DISTINCT DATE(in_date) AS date FROM employee_att) total_days "
	        + " CROSS JOIN employee e"
	        + " join role as r on r.role_id=e.role_id "
	        + " LEFT JOIN employee_att ea ON e.employee_id = ea.employee_id AND total_days.date = DATE(ea.in_date) "
	        + " WHERE e.employee_id = :employeeId"
	        + " and r.role_id=:role_id "
	        + "  AND year(total_days.date) = year(current_date()) "  
	        + " GROUP BY e.employee_id, monthname(total_days.date), year(total_days.date), month(total_days.date) "
	        + " ", nativeQuery = true)
	List<Map<String, Object>> AttendanceForEmployeeId123(@Param("employeeId") long employeeId,@Param("role_id") long role_id);



	
//	@Query(value = " SELECT e.employee_id, monthname(total_days.date) AS month, year(total_days.date) AS year, "
//	        + " COUNT(CASE WHEN ea.in_date IS NOT NULL THEN 1 END) AS presentPercentage, "
//	        + " COUNT(CASE WHEN ea.in_date IS NULL THEN 1 END) AS absentPercentage, "
//	        + " SUM(CASE WHEN month(total_days.date) = month(current_date()) THEN 1 ELSE 0 END) AS totalWorkingDays "
//	        + " FROM (SELECT DISTINCT DATE(in_date) AS date FROM employee_att) total_days "
//	        + " CROSS JOIN employee e "
//	        + " LEFT JOIN employee_att ea ON e.employee_id = ea.employee_id AND total_days.date = DATE(ea.in_date) "
//	        + " WHERE e.employee_id = :employeeId "
//	        + " GROUP BY e.employee_id, monthname(total_days.date), year(total_days.date);",
//	        nativeQuery = true)
//	List<Map<String, Object>> AttendanceForEmployeeId123(@Param("employeeId") long employeeId);
	@Query(value = "SELECT "
			+ "    e.employee_id, "
			+ "    total_days.date, "
			+ "    CASE WHEN COUNT(DISTINCT ea.in_date) > 0 THEN 'Present' ELSE 'Absent' END AS attendance_status"
			+ " FROM ("
			+ "    SELECT DISTINCT DATE(in_date) AS date "
			+ "    FROM employee_att "
			+ "    WHERE MONTH(in_date) = MONTH(CURRENT_DATE())"
			+ " ) total_days "
			+ " CROSS JOIN employee e "
	    	 + " join role as r on r.role_id=e.role_id "
			+ " LEFT JOIN employee_att ea "
			+ "    ON e.employee_id = ea.employee_id "
		 + " and r.role_id=:role_id "
			+ "    AND MONTH(ea.in_date) = MONTH(NOW()) "
			+ "    AND total_days.date = DATE(ea.in_date) "
			+ " WHERE e.employee_id = :employeeId "
			+ " GROUP BY e.employee_id, total_days.date;", nativeQuery = true)
	List<Map<String, Object>> attendanceForEmployeeIdDD(@Param("employeeId") long employeeId,@Param("role_id") long role_id);

	
	@Query(value = "SELECT"
	        + "    e.user_name,"
	        + "    e.user_id,"
	        + "    e.employee_id,"
	        + "    a.employee_att_id,"
	        + "    a.in_time,"
	        + "    a.out_time,"
	        + "    a.attendance,"
	        + "    a.in_date,"
	        + "    a.punch_in,"
	        + "    a.punch_out,"
	        + "   a.working_hour,"
	        + "    CASE"
	        + "        WHEN a.employee_att_id IS NULL THEN 'Absent'"
	        + "        ELSE 'Present'"
	        + "    END AS current_attendance"
	        + " FROM"
	        + "    employee e"
	        + " JOIN role AS r ON r.role_id = e.role_id "
	        + " LEFT JOIN employee_att a ON e.employee_id = a.employee_id"
	        + " WHERE"
	        + "    e.employee_id = :employee_id"
	        + "  and r.role_id=:role_id"
	        + "    AND MONTHNAME(a.in_date) = :monthName"
	        + "    AND YEAR(a.in_date) = :year",
	        nativeQuery = true)
	   List<Map<String, Object>> Allfilter(
	            @Param("employee_id") long employee_id,
	            @Param("role_id") long role_id,
	            @Param("monthName") String monthName,
	            @Param("year") int year);

	
	@Query(value = "SELECT"
	        + "    e.user_name,"
	        + "    e.user_id,"
	        + "    e.trainee_id,"
	        + "    a.employee_att_id,"
	        + "    r.role_id,"
	        + "    r.role_name,"
	        + "    a.in_time,"
	        + "    a.out_time,"
	        + "    a.attendance,"
	        + "    a.in_date,"
	        + "    a.punch_in,"
	        + "    a.punch_out,"
	        + "    a.working_hour,"
	        + "    CASE"
	        + "        WHEN a.employee_att_id IS NULL THEN 'Absent'"
	        + "        ELSE 'Present'"
	        + "    END AS current_attendance"
	        + " FROM"
	        + "    training_details as e "
	        + " JOIN role AS r ON r.role_id = e.role_id "
	        + " LEFT JOIN employee_att as a ON e.trainee_id = a.trainee_id"
	        + " WHERE"
	        + "    e.trainee_id = :trainee_id"
	        + "    AND r.role_id = :role_id"
	        + "    AND MONTHNAME(a.in_date) = :monthName"
	        + "    AND YEAR(a.in_date) = :year",
	        nativeQuery = true)
	List<Map<String, Object>> Allfiltert(
	        @Param("trainee_id") long traineeId,
	        @Param("role_id") long roleId,
	        @Param("monthName") String month,
	        @Param("year") int year);


	@Query(value = "SELECT e.user_name,e.user_id, e.employee_id, a.employee_att_id, a.in_time, a.out_time, a.attendance, a.in_date, a.punch_in, a.punch_out, a.working_hour,"
			+ "CASE" + "    WHEN a.employee_att_id IS NULL THEN 'Absent'" + "    ELSE 'Present'"
			+ " END AS current_attendance" + " FROM employee e"
			+ " LEFT JOIN employee_att a ON e.employee_id = a.employee_id" + "  WHERE e.employee_id = :employee_id"
			+ " AND a.in_date = CURDATE();", nativeQuery = true)
	List<Map<String, Object>> Allfilter2(@Param("employee_id") long employee_id);

		@Query(value = "  SELECT e.user_name,e.user_id e.employee_id, a.employee_att_id, a.in_time, a.out_time, a.attendance, a.in_date, a.punch_in, a.punch_out, a.working_hour,"
				+ " CASE" + "    WHEN a.employee_att_id IS NULL THEN 'Absent'" + "    ELSE 'Present'"
				+ " END AS current_attendance" + " FROM employee e"
				+ " LEFT JOIN employee_att a ON e.employee_id = a.employee_id"
				+ " AND a.in_date = CURDATE();", nativeQuery = true)
	List<Map<String, Object>> Allfilter3();
		
		
		@Query(value = " SELECT e.user_name as userName,e.user_id as userId,e.employee_id as employeeId,CASE WHEN a.employee_att_id IS NULL THEN 'Absent'END AS current_attendance"
				+ " FROM employee e"
				+ " LEFT JOIN employee_att a ON e.employee_id = a.employee_id AND a.in_date = CURDATE()"
				+ " WHERE a.employee_att_id IS NULL;", nativeQuery = true)
	List<Map<String, Object>> Allfilter3absentlist();

		@Query(value = "  SELECT e.user_name as userName,e.user_id as userId,e.employee_id as employeeId,a.employee_att_id as employeeAttId,a.in_time as inTime,a.out_time as outTime,a.attendance,a.in_date as inDate,a.punch_in as punchIn,a.punch_out as "
				+ "punchOut,a.working_hour as workingHour,a.ip_address as ipAddress,"
				+ "    CASE WHEN a.employee_att_id IS NULL THEN 'Absent' ELSE 'Present' END AS current_attendance FROM employee e"
				+ " LEFT JOIN employee_att a ON e.employee_id = a.employee_id AND a.in_date = CURDATE()"
				+ " WHERE a.employee_att_id IS NOT NULL;", nativeQuery = true)
	List<Map<String, Object>> Allfilterpresentlist();
		
	@Query(value = "SELECT " + "    COUNT(CASE WHEN a.employee_att_id IS NOT NULL THEN 1 END) AS present_count,"
			+ "    COUNT(CASE WHEN a.employee_att_id IS NULL THEN 1 END) AS absent_count" + "  FROM employee e"
			+ " LEFT JOIN employee_att a ON e.employee_id = a.employee_id"
			+ " AND a.in_date = CURDATE();", nativeQuery = true)
	List<Map<String, Object>> getAllpresent();
	
	
//	@Query(value = "  select"
//			+ "    monthname(min(total_days.date)) as month,"
//			+ "    month(min(total_days.date)) as monthnumber,"
//			+ "    sum(case when ea.in_date is not null then 1 else 0 end) as presentcount,"
//			+ "    sum(case when ea.in_date is null then 1 else 0 end) as absentcount,"
//			+ "    count(total_days.date) as totalworkingdays,"
//			+ "    round(sum(case when ea.in_date is not null then 1 else 0 end) / count(total_days.date) * 100, 0) as presentpercentage,"
//			+ "    round(sum(case when ea.in_date is null then 1 else 0 end) / count(total_days.date) * 100, 0) as absentpercentage"
//			+ " from"
//			+ "    (select distinct date(in_date) as date from employee_att) total_days"
//			+ " cross join"
//			+ "    employee e"
//			+ " left join"
//			+ "    ("
//			+ "        select employee_id, in_date"
//			+ "        from employee_att"
//			+ "    ) ea"
//			+ " on"
//			+ "    e.employee_id = ea.employee_id"
//			+ "    and total_days.date = date(ea.in_date)"
//			+ " group by"
//			+ "    year(total_days.date),"
//			+ "    month(total_days.date)"
//			+ " order by"
//			+ "    min(total_days.date);", nativeQuery = true)
//	List<Map<String, Object>> getAllAvarageData();

	
	@Query(value = "SELECT "
	        + "    monthname(min(total_days.date)) as month, "
	        + "    month(min(total_days.date)) as monthnumber, "
	        + "    sum(case when ea.in_date is not null then 1 else 0 end) as presentcount, "
	        + "    sum(case when ea.in_date is null then 1 else 0 end) as absentcount, "
	        + "    count(total_days.date) as totalworkingdays, "
	        + "    round(sum(case when ea.in_date is not null then 1 else 0 end) / count(total_days.date) * 100, 0) as presentpercentage, "
	        + "    round(sum(case when ea.in_date is null then 1 else 0 end) / count(total_days.date) * 100, 0) as absentpercentage "
	        + "FROM "
	        + "    (SELECT DISTINCT DATE(in_date) AS date FROM employee_att) total_days "
	        + "CROSS JOIN "
	        + "    employee e "
	        + "LEFT JOIN "
	        + "    (SELECT employee_id, in_date FROM employee_att) ea ON e.employee_id = ea.employee_id AND total_days.date = DATE(ea.in_date) "
	        + "WHERE "
	        + "    YEAR(total_days.date) = YEAR(CURRENT_DATE()) "
	        + "GROUP BY "
	        + "    YEAR(total_days.date), "
	        + "    MONTH(total_days.date) "
	        + "ORDER BY "
	        + "    min(total_days.date);", nativeQuery = true)
	List<Map<String, Object>> getAllAvarageData();


	@Query(value = "SELECT " + "    present_count," + "    absent_count,"
			+ "    present_count / (present_count + absent_count) * 100 AS present_percentage,"
			+ "    absent_count / (present_count + absent_count) * 100 AS absent_percentage" + " FROM (" + "    SELECT "
			+ "        COUNT(CASE WHEN a.employee_att_id IS NOT NULL THEN 1 END) AS present_count,"
			+ "        COUNT(CASE WHEN a.employee_att_id IS NULL THEN 1 END) AS absent_count" + "    FROM employee e"
			+ "    LEFT JOIN employee_att a ON e.employee_id = a.employee_id"
			+ "    WHERE MONTH(a.in_date) = MONTH(CURDATE()) " + "    AND YEAR(a.in_date) = YEAR(CURDATE())"
			+ ") AS sub;", nativeQuery = true)
	List<Map<String, Object>> getAllpresent1();

	@Query(value = "select e.* from employee_att as e"
			+ " where e.in_date=:in_date and e.employee_id=:employee_id and e.punch_out = true", nativeQuery = true)
	Map<String, Object> enamoOnuThaa(LocalDate in_date, Long employee_id);

	@Query(value = "select e.employee_id as employeeId , e.user_name as userName, e.user_id as userId, MONTHNAME(total_days.date) AS month,"
			+ "    MONTH(total_days.date) AS monthnumber,COUNT(CASE WHEN ea.in_date IS NOT NULL THEN 1 END) AS presentDays,"
			+ "    COUNT(CASE WHEN ea.in_date IS NULL THEN 1 END) AS absentDays,COUNT(MONTH(total_days.date) = MONTH(CURRENT_DATE())) AS totalDays"
			+ " FROM (SELECT DISTINCT DATE(in_date) AS date FROM employee_att) total_days  CROSS JOIN employee e"
			+ " LEFT JOIN employee_att ea ON e.employee_id = ea.employee_id AND total_days.date = DATE(ea.in_date)"
			+ " WHERE e.status = true  AND MONTHName(total_days.date) = :monthName AND YEAR(total_days.date) = :year"
			+ " GROUP BY e.employee_id, e.user_name,e.user_id,  MONTHNAME(total_days.date),  MONTH(total_days.date)"
			+ " ORDER BY  e.employee_id, e.user_name, e.user_id, MONTH(total_days.date);", nativeQuery = true)
	List<Map<String, Object>> getAllattendance(@Param("monthName")String monthName,@Param("year") String year);

//	@Query(value = "select e.employee_id as employeeId , e.user_name as userName, e.user_id as userId, MONTHNAME(total_days.date) AS month,e.department_id as departmentId,d.department_name as departmentName,"
//			+ "    MONTH(total_days.date) AS monthnumber,COUNT(CASE WHEN ea.in_date IS NOT NULL THEN 1 END) AS presentDays,"
//			+ "    COUNT(CASE WHEN ea.in_date IS NULL THEN 1 END) AS absentDays,COUNT(MONTH(total_days.date) = MONTH(CURRENT_DATE())) AS totalDays"
//			+ " FROM (SELECT DISTINCT DATE(in_date) AS date FROM employee_att) total_days  CROSS JOIN employee e"
//			 + " join department d on d.department_id = e.department_id"
//			    + " join role as r on r.role_id = e.role_id"
//			+ " LEFT JOIN employee_att ea ON e.employee_id = ea.employee_id AND total_days.date = DATE(ea.in_date)"
//			+ " WHERE e.status = true  AND MONTHName(total_days.date) = :monthName AND YEAR(total_days.date) = :year"
//			+ " and e.employee_id =:employee_id"
//			+ " and r.role_id=:role_id"
//			+ " GROUP BY e.employee_id, e.user_name,e.user_id,  MONTHNAME(total_days.date),  MONTH(total_days.date)" 
//			+ " ORDER BY  e.employee_id, e.user_name, e.user_id, MONTH(total_days.date);", nativeQuery = true)
//	List<Map<String, Object>> getAllattendanceenamoOnuThaa(@Param("monthName")String monthName,@Param("year") String year,@Param("employee_id") Long employee_id,@Param("role_id") long role_id) ;

	
	@Query(value = "SELECT"
			+ "    e.user_name as userName, e.user_id as userId,e.employee_id as employeeId,a.employee_att_id as employeeAttId,a.in_time as inTime,  a.out_time as outTime,a.attendance,a.in_date as indate,a.punch_in as punchIn,"
			+ "a.punch_out as punchOut,a.working_hour as workinghour,a.ip_address as ipAddress"
			+ " FROM employee e"
			+ "  LEFT JOIN employee_att a ON e.employee_id = a.employee_id"
			+ " JOIN role as r ON r.role_id = e.role_id"
			+ " WHERE"
			+ "    e.employee_id = :employeeId"
			+ "    AND r.role_id = :roleId"
			+ "    AND MONTHNAME(a.in_date) = :monthName"
			+ "    AND YEAR(a.in_date) = :year", nativeQuery = true)
	List<Map<String, Object>> getAllattendanceenamoOnuThaa1(@Param("monthName")String monthName,@Param("year") String year,@Param("employeeId") Long employee_id,@Param("roleId") long role_id) ;


	@Query(value = "select e.trainee_id as traineeId , e.user_name as userName, e.user_id as userId, MONTHNAME(total_days.date) AS month,"
			+ "    MONTH(total_days.date) AS monthnumber,COUNT(CASE WHEN ea.in_date IS NOT NULL THEN 1 END) AS presentDays,"
			+ "    COUNT(CASE WHEN ea.in_date IS NULL THEN 1 END) AS absentDays,COUNT(MONTH(total_days.date) = MONTH(CURRENT_DATE())) AS totalDays"
			+ " FROM (SELECT DISTINCT DATE(in_date) AS date FROM employee_att) total_days  CROSS JOIN training_details e"
			+ " LEFT JOIN employee_att ea ON e.trainee_id = ea.trainee_id AND total_days.date = DATE(ea.in_date)"
			+ " WHERE e.status = true  AND MONTHName(total_days.date) = :monthName AND YEAR(total_days.date) = :year and e.trainee_id =:traineeId and e.role_id=:roleId"
			+ " GROUP BY e.trainee_id, e.user_name,e.user_id,  MONTHNAME(total_days.date),  MONTH(total_days.date)"
			+ " ORDER BY  e.trainee_id, e.user_name, e.user_id, MONTH(total_days.date);", nativeQuery = true)
	List<Map<String, Object>> getAllattendanceenamoOnuThaatrainee(@Param("monthName") String monthName, @Param("year") String year, @Param("traineeId") Long trainee_id, @Param("roleId") long role_id);

	
	@Query(value = "select e.employee_id as employeeId , e.user_name as userName, e.user_id as userId, MONTHNAME(total_days.date) AS month,"
			+ "			   MONTH(total_days.date) AS monthnumber,COUNT(CASE WHEN ea.in_date IS NOT NULL THEN 1 END) AS presentDays,"
			+ "			   COUNT(CASE WHEN ea.in_date IS NULL THEN 1 END) AS absentDays,COUNT(MONTH(total_days.date) = MONTH(CURRENT_DATE())) AS totalDays"
			+ "			 FROM (SELECT DISTINCT DATE(in_date) AS date FROM employee_att) total_days  CROSS JOIN employee e"
			+ "			 LEFT JOIN employee_att ea ON e.employee_id = ea.employee_id AND total_days.date = DATE(ea.in_date)"
			+ "			 WHERE e.status = true  AND MONTHName(total_days.date) =:monthName AND YEAR(total_days.date) =:year  and e.employee_id =:employeeId  and e.role_id=:roleId "
			+ "			 GROUP BY e.employee_id, e.user_name,e.user_id,  MONTHNAME(total_days.date),  MONTH(total_days.date)"
			+ "			 ORDER BY  e.employee_id, e.user_name, e.user_id, MONTH(total_days.date);", nativeQuery = true)
	List<Map<String, Object>> getAllattendanceenamoOnuThaa(@Param("monthName")String monthName,@Param("year") String year,@Param("employeeId") Long employee_id,@Param("roleId") long role_id) ;
	
	@Query(value = "SELECT"
			+ "    e.user_name as userName, e.user_id as userId,e.employee_id as employeeId,a.employee_att_id as employeeAttId,a.in_time as inTime,  a.out_time as outTime,a.attendance,a.in_date as indate,a.punch_in as punchIn,"
			+ "a.punch_out as punchOut,a.working_hour as workinghour,a.ip_address as ipAddress"
			+ " FROM employee e"
			+ "  LEFT JOIN employee_att a ON e.employee_id = a.employee_id"
			+ " JOIN role as r ON r.role_id = e.role_id"
			+ " WHERE"
			+ "    e.employee_id = :employeeId"
			+ "    AND MONTHNAME(a.in_date) = :monthName"
			+ "    AND YEAR(a.in_date) = :year", nativeQuery = true)
	List<Map<String, Object>> getAllattendanceenamoOnuThaainMonth(@Param("monthName")String monthName,@Param("year") String year,@Param("employeeId") Long employee_id) ;

	@Query(value = "select e.employee_id as employeeId , e.user_name as userName, e.user_id as userId, monthname(total_days.date) as month,"
			+ "    month(total_days.date) as monthnumber,count(case when ea.in_date is not null then 1 end) as presentpercentage,"
			+ "    count(case when ea.in_date is null then 1 end) as absentpercentage,count(month(total_days.date) = month(current_date())) as totalworkingdays"
			+ " from (select distinct date(in_date) as date from employee_att) total_days  cross join employee e"
			+ " left join employee_att ea on e.employee_id = ea.employee_id and total_days.date = date(ea.in_date)"
			+ " where e.status = true  and month(total_days.date) = month(current_date())"
			+ " group by e.employee_id, e.user_name,e.user_id,  monthname(total_days.date),  month(total_days.date)"
			+ " order by  e.employee_id, e.user_name, e.user_id, month(total_days.date);", nativeQuery = true)
	List<Map<String, Object>> getAllassetsedetail();

	
	@Query(value = " SELECT e.user_name as userName,e.user_id as userId,e.employee_id as employeeId,a.employee_att_id as employeeAttId,a.in_time as inTime,a.out_time as outTime,a.attendance,a.in_date as inDate,a.punch_in as punchIn,"
			+ " a.punch_out as punchOut,a.working_hour as workingHour,a.ip_address as ipAddress"
			+ " FROM employee e"
			+ " LEFT JOIN employee_att a ON e.employee_id = a.employee_id"
			+ " WHERE DATE(a.in_date) = CURDATE();", nativeQuery = true)
	List<Map<String, Object>> ALLOverattendance();
	
	@Query(value = " SELECT \r\n"
			+ "    e.trainee_id, \r\n"
			+ "    r.role_id,\r\n"
			+ "    monthname(total_days.date) AS month, \r\n"
			+ "    year(total_days.date) AS year, \r\n"
			+ "    month(total_days.date) AS monthnum, \r\n"
			+ "    COUNT(CASE WHEN ea.in_date IS NOT NULL THEN 1 END) AS presentPercentage, \r\n"
			+ "    COUNT(CASE WHEN ea.in_date IS NULL THEN 1 END) AS absentPercentage, \r\n"
			+ "    COUNT(total_days.date) AS totalWorkingDays \r\n"
			+ " FROM (\r\n"
			+ "    SELECT DISTINCT DATE(in_date) AS date \r\n"
			+ "    FROM employee_att\r\n"
			+ " ) total_days \r\n"
			+ " CROSS JOIN training_details e \r\n"
			+ " join role as r on r.role_id=e.role_id\r\n"
			+ " LEFT JOIN employee_att ea \r\n"
			+ "    ON e.trainee_id = ea.trainee_id AND total_days.date = DATE(ea.in_date) \r\n"
			+ " WHERE \r\n"
			+ "    e.trainee_id = :trainee_id\r\n"
			+ "    AND r.role_id = :role_id\r\n"
			+ "    AND year(total_days.date) = year(current_date()) \r\n"
			+ " GROUP BY e.trainee_id, monthname(total_days.date), year(total_days.date), month(total_days.date) \r\n"
			+ " ", nativeQuery = true)
	List<Map<String, Object>> AttendanceforEmployeeIdtraineedashboard(@Param("trainee_id") long trainee_id,@Param("role_id") long role_id);

	
	@Query(value = " SELECT e.trainee_id,r.role_id, total_days.date, \r\n"
			+ "    CASE    WHEN COUNT(DISTINCT ea.in_date) > 0 THEN 'Present'  WHEN DAYOFWEEK(total_days.date) = 1 THEN 'Sunday'\r\n"
			+ "        WHEN 'Holiday' IN (total_days.attendance_status, total_days.absence_status) THEN 'Holiday' ELSE 'Absent'  END AS attendance_status\r\n"
			+ "FROM ( SELECT  DISTINCT DATE(in_date) AS date,   'Workday' AS attendance_status, CASE  WHEN ea.in_date IS NULL THEN 'Absent'  -- Marking the absence  ELSE NULL\r\n"
			+ "  END AS absence_status\r\n"
			+ "    FROM \r\n"
			+ "        employee_att ea\r\n"
			+ "    WHERE \r\n"
			+ "        MONTH(in_date) = MONTH(CURRENT_DATE())\r\n"
			+ "    UNION\r\n"
			+ "    SELECT \r\n"
			+ "        hl.date, \r\n"
			+ "        'Holiday' AS attendance_status,\r\n"
			+ "        NULL AS absence_status\r\n"
			+ "    FROM \r\n"
			+ "        holidays_list AS hl\r\n"
			+ "        JOIN holidays AS h ON hl.holidays_id = h.holidays_id\r\n"
			+ "    WHERE \r\n"
			+ "        MONTH(hl.date) = MONTH(CURRENT_DATE())\r\n"
			+ " ) total_days \r\n"
			+ " CROSS JOIN training_details e \r\n"
			+ " JOIN role AS r ON r.role_id = e.role_id \r\n"
			+ " LEFT JOIN employee_att ea \r\n"
			+ "    ON e.trainee_id = ea.trainee_id \r\n"
			+ "    AND MONTH(ea.in_date) = MONTH(NOW()) \r\n"
			+ "    AND total_days.date = DATE(ea.in_date) \r\n"
			+ " WHERE \r\n"
			+ "    e.trainee_id = :trainee_id"
			+ "    AND r.role_id = :role_id"
			+ " GROUP BY \r\n"
			+ "    e.trainee_id, \r\n"
			+ "    total_days.date, \r\n"
			+ "    total_days.attendance_status,\r\n"
			+ "    total_days.absence_status;", nativeQuery = true)
	List<Map<String, Object>> attendanceForTraineeIdAndRoleId(
	        @Param("trainee_id") long trainee_id,
	        @Param("role_id") long role_id);

	
	@Query(value = "SELECT e.employee_id,r.role_id, total_days.date, \r\n"
			+ "    CASE    WHEN COUNT(DISTINCT ea.in_date) > 0 THEN 'Present'  WHEN DAYOFWEEK(total_days.date) = 1 THEN 'Sunday'\r\n"
			+ "        WHEN 'Holiday' IN (total_days.attendance_status, total_days.absence_status) THEN 'Holiday' ELSE 'Absent'  END AS attendance_status\r\n"
			+ "FROM ( SELECT  DISTINCT DATE(in_date) AS date,   'Workday' AS attendance_status, CASE  WHEN ea.in_date IS NULL THEN 'Absent'  -- Marking the absence  ELSE NULL\r\n"
			+ "  END AS absence_status\r\n"
			+ "    FROM \r\n"
			+ "        employee_att ea\r\n"
			+ "    WHERE \r\n"
			+ "        MONTH(in_date) = MONTH(CURRENT_DATE())\r\n"
			+ "    UNION\r\n"
			+ "    SELECT \r\n"
			+ "        hl.date, \r\n"
			+ "        'Holiday' AS attendance_status,\r\n"
			+ "        NULL AS absence_status\r\n"
			+ "    FROM \r\n"
			+ "        holidays_list AS hl\r\n"
			+ "        JOIN holidays AS h ON hl.holidays_id = h.holidays_id\r\n"
			+ "    WHERE \r\n"
			+ "        MONTH(hl.date) = MONTH(CURRENT_DATE())\r\n"
			+ ") total_days \r\n"
			+ "CROSS JOIN employee e \r\n"
			+ "JOIN role AS r ON r.role_id = e.role_id \r\n"
			+ "LEFT JOIN employee_att ea \r\n"
			+ "    ON e.employee_id = ea.employee_id \r\n"
			+ "    AND MONTH(ea.in_date) = MONTH(NOW()) \r\n"
			+ "    AND total_days.date = DATE(ea.in_date) \r\n"
			+ "WHERE \r\n"
			+ "    e.employee_id = :employee_id"
			+ "    AND r.role_id = :role_id"
			+ " GROUP BY \r\n"
			+ "    e.employee_id, \r\n"
			+ "    total_days.date, \r\n"
			+ "    total_days.attendance_status,\r\n"
			+ "    total_days.absence_status;", nativeQuery = true)
	List<Map<String, Object>> attendanceForEmployeeIdAndRoleId(
	        @Param("employee_id") long employee_id,
	        @Param("role_id") long role_id);
//	Optional<EmployeeAttendance> findByEmployeeId(long id);
	
	
	@Query(value = ""
			+ "SELECT MONTHNAME(MIN(total_days.date)) AS month,"
			+ "    MONTH(MIN(total_days.date)) AS monthnumber,"
			+ "    SUM(CASE WHEN ea.in_date IS NOT NULL THEN 1 ELSE 0 END) AS presentcount,"
			+ "    SUM(CASE WHEN ea.in_date IS NULL THEN 1 ELSE 0 END) AS absentcount,"
			+ "    COUNT(total_days.date) AS totalworkingdays,"
			+ "    ROUND(SUM(CASE WHEN ea.in_date IS NOT NULL THEN 1 ELSE 0 END) / COUNT(total_days.date) * 100, 0) AS presentpercentage,"
			+ "    ROUND(SUM(CASE WHEN ea.in_date IS NULL THEN 1 ELSE 0 END) / COUNT(total_days.date) * 100, 0) AS absentpercentage"
			+ " FROM (SELECT DISTINCT DATE(in_date) AS date FROM employee_att) total_days"
			+ " CROSS JOIN training_details e"
			+ " LEFT JOIN (SELECT trainee_id, in_date FROM employee_att) ea ON e.trainee_id = ea.trainee_id AND total_days.date = DATE(ea.in_date)"
			+ " WHERE YEAR(total_days.date) = YEAR(CURRENT_DATE())"
			+ " GROUP BY YEAR(total_days.date),MONTH(total_days.date)"
			+ " ORDER BY MIN(total_days.date);", nativeQuery = true)
List<Map<String, Object>>GetTraineeAttendancepersentage();
	
	@Query(value = "  SELECT e.user_name as userName,e.user_id as userId,e.trainee_id as traineeId,a.employee_att_id as employeeAttId,a.in_time as inTime,a.out_time as outTime,a.attendance,a.in_date as inDate,a.punch_in as punchIn,a.punch_out as "
			+ "punchOut,a.working_hour as workingHour,a.ip_address as ipAddress,"
			+ "    CASE WHEN a.employee_att_id IS NULL THEN 'Absent' ELSE 'Present' END AS current_attendance FROM training_details e"
			+ " LEFT JOIN employee_att a ON e.trainee_id = a.trainee_id AND a.in_date = CURDATE()"
			+ " WHERE a.employee_att_id IS NOT NULL;", nativeQuery = true)
List<Map<String, Object>> Allfilterpresentlisttrainee();
	
	@Query(value = " SELECT e.user_name as userName,e.user_id as userId,e.trainee_id as traineeId,CASE WHEN a.employee_att_id IS NULL THEN 'Absent'END AS current_attendance"
			+ " FROM training_details e"
			+ " LEFT JOIN employee_att a ON e.trainee_id = a.trainee_id AND a.in_date = CURDATE()"
			+ " WHERE a.employee_att_id IS NULL;", nativeQuery = true)
List<Map<String, Object>> Allfilter3absentlisttrainee();
	
	@Query(value = "select e.trainee_id as traineeId , e.user_name as userName, e.user_id as userId, MONTHNAME(total_days.date) AS month,"
			+ "    MONTH(total_days.date) AS monthnumber,COUNT(CASE WHEN ea.in_date IS NOT NULL THEN 1 END) AS presentDays,"
			+ "    COUNT(CASE WHEN ea.in_date IS NULL THEN 1 END) AS absentDays,COUNT(MONTH(total_days.date) = MONTH(CURRENT_DATE())) AS totalDays"
			+ " FROM (SELECT DISTINCT DATE(in_date) AS date FROM employee_att) total_days  CROSS JOIN training_details e"
			+ " LEFT JOIN employee_att ea ON e.trainee_id = ea.trainee_id AND total_days.date = DATE(ea.in_date)"
			+ " WHERE e.status = true  AND MONTHName(total_days.date) = :monthName AND YEAR(total_days.date) = :year"
			+ " GROUP BY e.trainee_id, e.user_name,e.user_id,  MONTHNAME(total_days.date),  MONTH(total_days.date)"
			+ " ORDER BY  e.trainee_id, e.user_name, e.user_id, MONTH(total_days.date);", nativeQuery = true)
	List<Map<String, Object>> getAllattendancetrainee(@Param("monthName")String monthName,@Param("year") String year);
	
	@Query(value = "select e.trainee_id as traineeId , e.user_name as userName, e.user_id as userId, monthname(total_days.date) as month,"
			+ "    month(total_days.date) as monthnumber,count(case when ea.in_date is not null then 1 end) as presentpercentage,"
			+ "    count(case when ea.in_date is null then 1 end) as absentpercentage,count(month(total_days.date) = month(current_date())) as totalworkingdays"
			+ " from (select distinct date(in_date) as date from employee_att) total_days  cross join training_details e"
			+ " left join employee_att ea on e.trainee_id = ea.trainee_id and total_days.date = date(ea.in_date)"
			+ " where e.status = true  and month(total_days.date) = month(current_date())"
			+ " group by e.trainee_id, e.user_name,e.user_id,  monthname(total_days.date),  month(total_days.date)"
			+ " order by  e.trainee_id, e.user_name, e.user_id, month(total_days.date);", nativeQuery = true)
	List<Map<String, Object>> getAllassetsedetailtrainee_id();
	
	@Query(value = "SELECT"
	        + "    e.user_name as userName, e.user_id as userId, e.trainee_id as traineeId, a.employee_att_id as employeeAttId, a.in_time as inTime, a.out_time as outTime, a.attendance, a.in_date as indate, a.punch_in as punchIn, a.punch_out as punchOut, a.working_hour as workinghour "
	        + " FROM training_details e "
	        + " LEFT JOIN employee_att a ON e.trainee_id = a.trainee_id "
	        + " JOIN role as r ON r.role_id = e.role_id "
	        + " WHERE "
	        + "    e.trainee_id = :traineeId "
	        + "    AND r.role_id = :roleId "
	        + "    AND MONTHNAME(a.in_date) = :monthName "
	        + "    AND YEAR(a.in_date) = :year ", nativeQuery = true)
	List<Map<String, Object>> getAllattendanceenamoOnuThaatrainee1(@Param("monthName") String monthName, @Param("year") String year, @Param("traineeId") Long trainee_id, @Param("roleId") long role_id);

	
	@Query(value = "SELECT"
	        + "    e.user_name as userName, e.user_id as userId, e.trainee_id as traineeId, a.employee_att_id as employeeAttId, a.in_time as inTime, a.out_time as outTime, a.attendance, a.in_date as indate, a.punch_in as punchIn, a.punch_out as punchOut, a.working_hour as workinghour,a.ip_address as ipAddress "
	        + " FROM training_details e "
	        + " LEFT JOIN employee_att a ON e.trainee_id = a.trainee_id "
	        + " JOIN role as r ON r.role_id = e.role_id "
	        + " WHERE "
	        + "    e.trainee_id = :traineeId "
	        + "    AND MONTHNAME(a.in_date) = :monthName "
	        + "    AND YEAR(a.in_date) = :year ", nativeQuery = true)
	List<Map<String, Object>> getAllattendanceenamoOnuThaatraineeinmonth(@Param("monthName") String monthName, @Param("year") String year, @Param("traineeId") Long traineeId);

}
