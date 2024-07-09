package com.example.erp.repository.attendance;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.attendance.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	@Query(value = "select a.*,al.*,e.user_name,e.user_id,designation_name" + "	from attendance as a"
			+ "	join attendancelist as al on al.fk_attendance_id = a.attendance_id"
			+ "	join employee as e on e.employee_id=al.employee_id"
			+ " join designation d on d.designation_id=e.designation_id", nativeQuery = true)
	List<Map<String, Object>> getAllMemberDetails();

	@Query(value = " SELECT "
			+ "    a.attendance_date AS absent_date,"
			+ "    e.employee_id,"
			+ "    e.user_name,"
			+ "    e.user_id,"
			+ "    d.department_name,"
			+ "     DAYNAME(a.attendance_date) AS day_name"
			+ " FROM "
			+ "    attendance AS a"
			+ " JOIN "
			+ "    attendancelist AS al ON al.fk_attendance_id = a.attendance_id"
			+ " JOIN "
			+ "    employee AS e ON e.employee_id = al.employee_id"
			+ " join "
			+ "     department as d on d.department_id =e.department_id"
			+ " WHERE "
			+ "    MONTH(a.attendance_date) = MONTH(CURDATE())"
			+ "    AND YEAR(a.attendance_date) = YEAR(CURRENT_DATE())"
			+ "    AND e.employee_id = :employeeid"
			+ "    AND al.absent = true;"
			+ "", nativeQuery = true)
	List<Map<String, Object>> getAllMemberDetailsByMemberId(@Param("employeeid") Long employeeid);

	@Query(value = " select sum(al.present) as present_count, sum(al.absent) as absent_count,current_date() as today_date from attendance as a"
			+ "  join attendancelist as al on al.fk_attendance_id= a.attendance_id"
			+ "	 where a.attendance_date = current_date()", nativeQuery = true)
	List<Map<String, Object>> getAllMemberDetailsByMemberByDate();

	Optional<Attendance> findByAttendanceDate(Date attendanceDate);

	
	@Query(value = "SELECT "
			+ "    ROUND(SUM(al.present), 0) AS present_count, "
			+ "    ROUND(SUM(al.absent), 0) AS absent_count,"
			+ "    MONTH(a.attendance_date) AS month,"
			+ "    YEAR(a.attendance_date) AS year,"
			+ "    ROUND((SUM(al.present) / (SUM(al.present) + SUM(al.absent))) * 100, 1) AS present_percentage,"
			+ "    ROUND((SUM(al.absent) / (SUM(al.present) + SUM(al.absent))) * 100, 1) AS absent_percentage"
			+ " FROM "
			+ "    attendance AS a"
			+ " JOIN "
			+ "    attendancelist AS al "
			+ " ON "
			+ "    al.fk_attendance_id = a.attendance_id"
			+ " WHERE "
			+ "    YEAR(a.attendance_date) = YEAR(CURDATE())"
			+ " GROUP BY "
			+ "    YEAR(a.attendance_date), MONTH(a.attendance_date);"
			+ "", nativeQuery = true)
	List<Map<String, Object>> getAllMemberDetailsByMemberByDate1();

	@Query(value = "select a.*,al.*,e.user_name,e.user_id,designation_name" + "	 from attendance as a"
			+ "		 join attendancelist as al on al.fk_attendance_id = a.attendance_id"
			+ "		  join employee as e on e.employee_id=al.employee_id"
			+ "  join designation d on d.designation_id=e.designation_id"
			+ "     where a.attendance_date =current_date();", nativeQuery = true)
	List<Map<String, Object>> getAllemployeeDetails();

	@Query(value = "select a.*, al.*, e.user_name,e.user_id, d.designation_name,de.department_name"
			+ " from attendance as a" + " join attendancelist as al on al.fk_attendance_id = a.attendance_id"
			+ " join employee as e on e.employee_id = al.employee_id"
			+ " join designation as d on d.designation_id = e.designation_id "
			+ " JOIN department AS de ON de.department_id = e.department_id"
			+ " where date(a.attendance_date) = curdate() and al.absent = true;", nativeQuery = true)
	List<Map<String, Object>> getAllemployeeDetails2();


	
	@Query(value = "select\r\n"
			+ "    e.employee_id,\r\n"
			+ "    e.user_name,\r\n"
			+ "    e.user_id,\r\n"
			+ "    de.department_name,\r\n"
			+ "    ifnull(a.absent_count, 0) as absent_count\r\n"
			+ " from\r\n"
			+ "    employee as e\r\n"
			+ "    join department as de on de.department_id = e.department_id\r\n"
			+ "    left join (\r\n"
			+ "        select\r\n"
			+ "            al.employee_id,\r\n"
			+ "            count(al.present) as present_count\r\n"
			+ "        from\r\n"
			+ "            attendance as a\r\n"
			+ "            join attendancelist as al on al.fk_attendance_id = a.attendance_id\r\n"
			+ "        where\r\n"
			+ "            year(a.attendance_date) = year(curdate())\r\n"
			+ "            and month(a.attendance_date) = month(curdate())\r\n"
			+ "            and al.present = true\r\n"
			+ "        group by\r\n"
			+ "            al.employee_id\r\n"
			+ "    ) as p on e.employee_id = p.employee_id\r\n"
			+ "    left join (\r\n"
			+ "        select\r\n"
			+ "            al.employee_id,\r\n"
			+ "            count(al.absent) as absent_count\r\n"
			+ "        from\r\n"
			+ "            attendance as a\r\n"
			+ "            join attendancelist as al on al.fk_attendance_id = a.attendance_id\r\n"
			+ "        where\r\n"
			+ "            year(a.attendance_date) = year(curdate())\r\n"
			+ "            and month(a.attendance_date) = month(curdate())\r\n"
			+ "            and al.absent = true\r\n"
			+ "        group by\r\n"
			+ "            al.employee_id\r\n"
			+ "    ) as a on e.employee_id = a.employee_id\r\n"
			+ " order by\r\n"
			+ "    absent_count desc;"
			, nativeQuery = true)
	List<Map<String, Object>> getAllemployeeDetails5();

	@Query(value = "   select coalesce(sum(al.present),0)  as present_count ,coalesce( sum(al.absent),0) as absent_count from attendance as a"
			+ "		  join attendancelist as al on al.fk_attendance_id= a.attendance_id"
			+ "			 where a.attendance_date = current_date()", nativeQuery = true)
	List<Map<String, Object>> getAllpresent();

	@Query(value = "   select a.*,al.*,e.user_name,e.user_id,designation_name" + "			 from attendance as a"
			+ "				 join attendancelist as al on al.fk_attendance_id = a.attendance_id"
			+ "			  join employee as e on e.employee_id=al.employee_id"
			+ "		 join designation d on d.designation_id=e.designation_id"
			+ "		    where a.attendance_date = :startDate ", nativeQuery = true)
	List<Map<String, Object>> getAllpromotionsBetweenDates(@Param("startDate") LocalDate startDate);
	
	
	@Query(value="select e.employee_id,e.user_name,e.gender,e.phone_number,e.role_type,st.shift_name,dt.department_name,"
			+ "	d.designation_name,e.shift_type_id,al.attstatus,al.intime,al.outtime,al.date from employee as e"
			+ "	join shift_type as st on st.shift_type_id = e.shift_type_id"
			+ "	join department as dt on dt.department_id = e.department_id"
			+ "	join designation as d on d.designation_id = e.designation_id"
			+ "	join attendancelist as al on al.employee_id = e.employee_id"
			+ "	where st.shift_type_id =1 and al.date=:date", nativeQuery = true)
	List<Map<String, Object>> getAllAttendanceTypeDetails(Date date);
	
	@Query(value="select e.employee_id,e.user_name,e.gender,e.phone_number,e.role_type,st.shift_name,"
			+ "	dt.department_name,d.designation_name,e.shift_type_id,s.shift_id,s.shift_type,s.in_time,s.out_time,al.attstatus,al.intime,al.outtime,al.date from employee as e"
			+ "	join shift_type as st on st.shift_type_id = e.shift_type_id"
			+ "	join department as dt on dt.department_id = e.department_id"
			+ "	join designation as d on d.designation_id = e.designation_id"
			+ "	join shift as s on s.shift_id = e.shift_id"
			+ " join attendancelist as al on al.employee_id = e.employee_id"
			+ "	where st.shift_type_id =2 and al.date=:date", nativeQuery = true)
	List<Map<String, Object>> getAllAttendanceAndShiftTypeDetails(Date date);
	

}
