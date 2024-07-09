package com.example.erp.repository.employee;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.erp.entity.employee.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	@Query(value = " select  e.employee_id,e.address,e.attendance_type,e.city,e.company_id,e.confirm_password,e.country,e.email,e.date,e.department_id,e.description,e.designation_id,e.dob,e.gender,e.intime,e.password,e.phone_number,e.role_id,e.role_type,e.shift_id,e.shift_type_id,e.state,e.status,e.user_id,e.user_name,d.designation_name,dd.department_name,r.role_name,s.shift_type,s.in_time,s.out_time"
			+ " from employee as e" + " left join designation as d on d.designation_id=e.designation_id"
			+ " left join department as dd on dd.department_id= e.department_id" + " left join role as r on r.role_id = e.role_id"
			+ " left join shift as s on s.shift_id = e.shift_id"
			+ " where e.status= true "
			+ " order by  e.employee_id DESC", nativeQuery = true)
	List<Map<String, Object>> getAllEmployeesWithDetails();

	@Query(value = " select  e.employee_id,e.address,e.attendance_type,e.city,e.company_id,e.confirm_password,e.country,e.email,e.date,e.department_id,e.description,e.designation_id,e.dob,e.gender,e.intime,e.password,e.phone_number,e.role_id,e.role_type,e.shift_id,e.shift_type_id,e.state,e.status,e.user_id,e.user_name,d.designation_name,dd.department_name,r.role_name,s.shift_type,s.in_time,s.out_time,ee.exit_type "
			+ " from employee as e" + " left join designation as d on d.designation_id=e.designation_id"
			+ " left join department as dd on dd.department_id= e.department_id" + " left join role as r on r.role_id = e.role_id"
			+ " left join shift as s on s.shift_id = e.shift_id"
			+ " left join employeeexit as ee on ee.employee_id=e.employee_id"
			+ " where e.status= false"
			+ "  order by  e.employee_id DESC", nativeQuery = true)
	List<Map<String, Object>> getAllEmployeesWithDetailsinactive();
	
	@Query(value = " select e.employee_id,e.address,e.attendance_type,e.city,e.company_id,e.confirm_password,e.country,e.email,e.date,e.department_id,e.description,e.designation_id,e.dob,e.gender,e.intime,e.password,e.phone_number,e.role_id,e.role_type,e.shift_id,e.shift_type_id,e.state,e.status,e.user_id,e.user_name,r.role_name,d.designation_name,dd.department_name,s.shift_type,s.in_time,s.out_time,ee.exit_type "
			+ " from employee as e" + " join role as r on r.role_id=e.role_id"
			+ " left join designation as d on d.designation_id=e.designation_id"
			+ " left join department as dd on dd.department_id= e.department_id"
			+ " left join shift as s on s.shift_id = e.shift_id"
			+ " left join employeeexit as ee on ee.employee_id=e.employee_id"
			+ " where e.employee_id =:employee_id", nativeQuery = true)
	List<Map<String, Object>> getAllEmployeesWithDetailsWithId(Long employee_id);

	@Query(value = " select e.*,d.designation_name,dd.department_name" + "	from employee as e"
			+ " left join designation as d on d.designation_id=e.designation_id"
			+ "	left join department as dd on dd.department_id= e.department_id"
			+ " where e.status = true;", nativeQuery = true)
	List<Map<String, Object>> AllEmployees();

	@Query(value = "SELECT e.employee_id,e.address,e.attendance_type,e.city,e.company_id,e.confirm_password,e.country,e.email,e.date,e.department_id,e.description,e.designation_id,e.dob,e.gender,e.intime,e.password,e.phone_number,e.role_id,e.role_type,e.shift_id,e.shift_type_id,e.state,e.status,e.user_id,e.user_name, d.designation_name, dd.department_name " + "FROM employee AS e "
			+ "JOIN designation AS d ON d.designation_id = e.designation_id "
			+ "JOIN department AS dd ON dd.department_id = e.department_id "
			+ "WHERE e.employee_id = :employeeId", nativeQuery = true)
	List<Map<String, Object>> getAllRoleByEmployees3(@Param("employeeId") Long employeeId);

	@Query(value = "select e.employee_id,e.address,e.attendance_type,e.city,e.company_id,e.confirm_password,e.country,e.email,e.date,e.department_id,e.description,e.designation_id,e.dob,e.gender,e.intime,e.password,e.phone_number,e.role_id,e.role_type,e.shift_id,e.shift_type_id,e.state,e.status,e.user_id,e.user_name,,r.role_name from employee as e"
			+ " join role as r on r.role_id = e.role_id", nativeQuery = true)
	List<Map<String, Object>> getAllEmployeeWithRole();

	Employee findByEmail(String email);

	@Query(value = "select e.employee_id,e.user_name,d.department_id,d.department_name, e.user_id  from employee as e"
			+ " join department as d on d.department_id = e.department_id "
			+ " where e.status=true", nativeQuery = true)
	List<Map<String, Object>> getAllRoleByEmployees();

	@Query(value = "select e.trainee_id,e.user_name,d.department_id,d.department_name, e.user_id  "
			+ " from training_details as e"
			+ " join department as d on d.department_id = e.department_id"
			+ " where e.cancel=false and e.completed=false and e.status=true", nativeQuery = true)
	List<Map<String, Object>> getAllRoleByTrainee();
	
//	@Query(value = "select e.employee_id as employeeId,e.user_name as userName,d.department_id as departmentId,d.department_name as departmentName from employee as e"
//			+ " join department as d on d.department_id = e.department_id", nativeQuery = true)
//	List<Map<String, Object>> getAllRoleByEmployees();

	@Query(value = "select e.employee_id,e.address,e.attendance_type,e.city,e.company_id,e.confirm_password,e.country,e.email,e.date,e.department_id,e.description,e.designation_id,e.dob,e.gender,e.intime,e.password,e.phone_number,e.role_id,e.role_type,e.shift_id,e.shift_type_id,e.state,e.status,e.user_id,e.user_name, d.department_name " + "from employee as e "
			+ "join department as d on d.department_id = e.department_id "
			+ "where d.department_id = :departmentId", nativeQuery = true)
	List<Map<String, Object>> getAllRoleByEmployees1(@Param("departmentId") Long departmentId);

	@Query(value = "SELECT e.employee_id,e.address,e.attendance_type,e.city,e.company_id,e.confirm_password,e.country,e.email,e.date,e.department_id,e.description,e.designation_id,e.dob,e.gender,e.intime,e.password,e.phone_number,e.role_id,e.role_type,e.shift_id,e.shift_type_id,e.state,e.status,e.user_id,e.user_name, d.department_name " + "FROM employee e "
			+ "JOIN department d ON d.department_id = e.department_id "
			+ "WHERE d.department_id = :departmentId", nativeQuery = true)
	List<Map<String, Object>> getAllEmployeesByDepartment(@Param("departmentId") Long departmentId);

	@Query(value = "               SELECT"
			+ "    ROUND((SUM(CASE WHEN e.gender = 'Male' THEN 1 ELSE 0 END) / COUNT(*)) * 100, 1) AS male_percentage,"
			+ "    ROUND((SUM(CASE WHEN e.gender = 'Female' THEN 1 ELSE 0 END) / COUNT(*)) * 100, 1) AS female_percentage"
			+ " FROM" + "    employee AS e;", nativeQuery = true)
	List<Map<String, Object>> ALLOver();

	@Query(value = "select year(e.date) as year,"
			+ " sum(count(e.employee_id)) over (order by year(e.date)) as total_employee_count,"
			+ " count(e.employee_id) as new_employee_count" + " from employee e " + " where  e.status = true"
			+ " group by year(e.date)" + " order by year(e.date)", nativeQuery = true)
	List<Map<String, Object>> ALLCount();

	@Query(value = "  select d.department_name,d.color,coalesce(count(e.employee_id), 0) as employee_count,"
			+ "  coalesce((select count(t.trainee_id) from training_details as t"
			+ "	where t.department_id = d.department_id), 0) as training_count"
			+ " from department as d"
			+ " left join employee as e on e.department_id = d.department_id"
			+ " group by d.department_name, d.color,training_count;"
			+ "", nativeQuery = true)
	List<Map<String, Object>> ALLDepatment();

	@Query(value = "  SELECT " + "    e.user_name," + "    e.employee_id," + "    e.user_id,"
			+ "    DATE_ADD(e.date, INTERVAL YEAR(CURDATE()) - YEAR(e.date) YEAR) AS anniversary,"
			+ "    TIMESTAMPDIFF(YEAR, e.date, CURDATE()) AS years_since_joining,"
			+ "    CONCAT(e.user_name, ' - ', TIMESTAMPDIFF(YEAR, e.date, CURDATE()), ' year anniversary') AS anniversary_message"
			+ " FROM employee AS e", nativeQuery = true)
	List<Map<String, Object>> AllNotifications1();

	@Query(value = "  select " + "    day as event_date," + "    date as date," + "    message" + " from ("
			+ "    select " + "        h.day,"
			+ "        date_add(h.date, interval year(curdate()) - year(h.date) year) as date," + "        case"
			+ "            when date_add(h.date, interval year(curdate()) - year(h.date) year) = curdate()  then concat(h.title, ' - today is their holidays')"
			+ "            when date_add(h.date, interval year(curdate()) - year(h.date) year) = curdate() + interval 1 day then concat(h.title, ' - tomorrow is their holidays')"
			+ "            else 'no special message'" + "        end as message" + "    from holidays as h"
			+ "    where"
			+ "        date_add(h.date, interval year(curdate()) - year(h.date) year) between curdate() and curdate() + interval 1 day"
			+ "    union" + "    select "
			+ "    dayname(date_add(e.dob, interval year(curdate()) - year(e.dob) year)) as day,"
			+ "        date_add(e.dob, interval year(curdate()) - year(e.dob) year) as date," + "        case"
			+ "            when date_add(e.dob, interval year(curdate()) - year(e.dob) year) = curdate() then concat(e.user_name, ' - today is their birthday')"
			+ "            when date_add(e.dob, interval year(curdate()) - year(e.dob) year) = curdate() + interval 1 day then concat(e.user_name, ' - tomorrow is their birthday')"
			+ "            else 'no special message'" + "        end as message" + "    from employee as e"
			+ "    where"
			+ "        date_add(e.dob, interval year(curdate()) - year(e.dob) year) between curdate() and curdate() + interval 1 day"
			+ "    union" + " select "
			+ "        dayname(date_add(p.payroll_date, interval year(curdate()) - year(p.payroll_date) year)) as day,"
			+ "        date_add(p.payroll_date, interval year(curdate()) - year(p.payroll_date) year) as date,"
			+ "        case"
			+ "            when date_add(p.payroll_date, interval year(curdate()) - year(p.payroll_date) year) = curdate() then concat(p.payroll_date, ' - today is their salary date')"
			+ "            when date_add(p.payroll_date, interval year(curdate()) - year(p.payroll_date) year) = curdate() + interval 1 day then concat(p.payroll_date, ' - tomorrow is their salary date')"
			+ "            else 'no special message'" + "        end as message" + "    from payroll as p" + "    where"
			+ "       date_add(p.payroll_date, interval year(curdate()) - year(p.payroll_date) year) between curdate() and curdate() + interval 1 day"
			+ "        " + "  ) as all_events;" + "", nativeQuery = true)
	List<Map<String, Object>> AllNotifications();

	long countByStatusTrue();

	@Query(value = "select e.employee_id,e.user_name,e.gender,e.phone_number,e.role_type,st.shift_name,dt.department_name,"
			+ " d.designation_name,e.shift_type_id from employee as e"
			+ " join shift_type as st on st.shift_type_id = e.shift_type_id"
			+ " join department as dt on dt.department_id = e.department_id"
			+ " join designation as d on d.designation_id = e.designation_id"
			+ " where st.shift_type_id =1", nativeQuery = true)
	List<Map<String, Object>> getAllShiftTypeDetails();

	@Query(value = "select e.employee_id,e.user_name,e.gender,e.phone_number,e.role_type,st.shift_name,"
			+ " dt.department_name,d.designation_name,e.shift_type_id,s.shift_id,s.shift_type,s.in_time,s.out_time from employee as e"
			+ " join shift_type as st on st.shift_type_id = e.shift_type_id"
			+ " join department as dt on dt.department_id = e.department_id"
			+ " join designation as d on d.designation_id = e.designation_id"
			+ " join shift as s on s.shift_id = e.shift_id" + " where st.shift_type_id =2", nativeQuery = true)
	List<Map<String, Object>> getAllshiftAndShiftTypeDetails();

	@Query(value = "select e.employee_id,e.user_name,e.role_type,r.role_id,r.role_name,e.department_id,e.url,ml.member_list_id as memberListId "
			+ " from employee as e"
			+ " join role as r on r.role_id = e.role_id"
			+ " join department as d on d.department_id = e.department_id"
			+ "  join member_list as ml on ml.email = e.email"
			+ " where e.employee_id = :employee_id", nativeQuery = true)
	List<Map<String, Object>> getAllRoleDetails(Long employee_id);


	@Query(value = " select e.user_id,e.user_name,e.gender,d.designation_name,dd.department_name,e.employee_id"
			+ " 	from employee as e"
			+ "				join designation as d on d.designation_id=e.designation_id"
			+ "				join department as dd on dd.department_id= e.department_id"
			+ "			 where e.status = false;", nativeQuery = true)
	List<Map<String, Object>> getAllpromotionsemployees();

	@Query(value=" select a.employee_id,a.confirm_password,a.email,a.user_name,a.password,a.role_type,a.status,a.date,a.intime,a.role_id"
			+ " from employee  as a "
			+ " where a.employee_id =:id and a.role_id=:role_id", nativeQuery = true)
	List<Map<String, Object>> getAllEmployeesWithDetailsWithIdEmployee(@Param("id") Long admin_id,@Param("role_id") Long role_id);
	
	
	@Query(value=" select e.employee_id, null as trainee_id,e.user_name,d.department_id,d.department_name, e.user_id  from employee as e"
			+ "			 join department as d on d.department_id = e.department_id"
			+ "     union all         "
			+ "    select null as employee_id ,t.trainee_id,t.user_name,d.department_id,d.department_name, t.user_id  from training_details as t"
			+ "			 join department as d on d.department_id = t.department_id; ", nativeQuery = true)
	List<Map<String, Object>> AllHighPriorityCountINtask();

	Employee findByPhoneNumber(String phoneNumber);

	Optional<Employee> findByPhoneNumberAndEmail(String phoneNumber, String email);
	


}
