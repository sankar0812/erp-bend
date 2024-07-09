package com.example.erp.repository.payroll;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.payroll.SalaryType;

public interface SalaryTypeRepository extends JpaRepository<SalaryType, Long> {

	@Query(value = "SELECT \r\n"
			+ "    s.*, \r\n"
			+ "    sl.salary_type_list_id, \r\n"
			+ "    sl.employee_id, \r\n"
			+ "    sl.salary_amount, \r\n"
			+ "    sl.salary_date AS date1, \r\n"
			+ "    e.user_name, \r\n"
			+ "    e.department_id, \r\n"
			+ "    e.designation_id, \r\n"
			+ "    d.designation_name, \r\n"
			+ "    dt.department_name,\r\n"
			+ "    MONTHNAME(total_days.date) AS month,\r\n"
			+ "    MONTH(total_days.date) AS monthnumber,\r\n"
			+ "    COUNT(CASE WHEN ea.in_date IS NOT NULL THEN 1 END) AS presentDays,\r\n"
			+ "    COUNT(CASE WHEN ea.in_date IS NULL THEN 1 END) AS absentDays,\r\n"
			+ "    COUNT(MONTH(total_days.date) = MONTH(CURRENT_DATE())) AS totalDays\r\n"
			+ "FROM \r\n"
			+ "    salary_type AS s\r\n"
			+ "LEFT JOIN \r\n"
			+ "    salary_type_list AS sl ON sl.salary_type_id = s.salary_type_id\r\n"
			+ "LEFT JOIN \r\n"
			+ "    employee AS e ON e.employee_id = sl.employee_id\r\n"
			+ "JOIN \r\n"
			+ "    designation AS d ON d.designation_id = e.designation_id\r\n"
			+ "JOIN \r\n"
			+ "    department AS dt ON dt.department_id = e.department_id\r\n"
			+ "CROSS JOIN \r\n"
			+ "    (SELECT DISTINCT DATE(in_date) AS date FROM employee_att WHERE MONTH(in_date) = MONTH(CURRENT_DATE())) total_days\r\n"
			+ "LEFT JOIN \r\n"
			+ "    employee_att ea ON e.employee_id = ea.employee_id AND total_days.date = DATE(ea.in_date)"
			+ "     where e.status=true"
			+ " GROUP BY \r\n"
			+ "    s.salary_type_id, sl.salary_type_list_id, sl.employee_id, sl.salary_amount, sl.salary_date,\r\n"
			+ "    e.employee_id, e.user_name, e.department_id, e.designation_id, d.designation_name, dt.department_name,\r\n"
			+ "    MONTHNAME(total_days.date), MONTH(total_days.date)\r\n"
			+ " ORDER BY \r\n"
			+ "    e.employee_id, e.user_name, e.user_id, MONTH(total_days.date);", nativeQuery = true)
	List<Map<String, Object>> getAllDetailsForSalary();

	
	@Query(value = "SELECT\r\n"
			+ "    s.*,\r\n"
			+ "    sl.salary_type_list_id,\r\n"
			+ "    sl.trainee_id,\r\n"
			+ "    sl.salary_amount,\r\n"
			+ "    sl.salary_date AS date1,\r\n"
			+ "    e.user_name,\r\n"
			+ "    e.department_id,\r\n"
			+ "    dt.department_name,\r\n"
			+ "    MONTHNAME(total_days.date) AS month,\r\n"
			+ "    MONTH(total_days.date) AS monthnumber,\r\n"
			+ "    COUNT(CASE WHEN ea.in_date IS NOT NULL THEN 1 END) AS presentDays,\r\n"
			+ "    COUNT(CASE WHEN ea.in_date IS NULL THEN 1 END) AS absentDays,\r\n"
			+ "    COUNT(MONTH(total_days.date) = MONTH(CURRENT_DATE())) AS totalDays\r\n"
			+ "FROM\r\n"
			+ "    salary_type AS s\r\n"
			+ "LEFT JOIN\r\n"
			+ "    salary_type_list AS sl ON sl.salary_type_id = s.salary_type_id\r\n"
			+ "LEFT JOIN\r\n"
			+ "    training_details AS e ON e.trainee_id = sl.trainee_id\r\n"
			+ "JOIN\r\n"
			+ "    department AS dt ON dt.department_id = e.department_id\r\n"
			+ "CROSS JOIN\r\n"
			+ "    (SELECT DISTINCT DATE(in_date) AS date FROM employee_att WHERE MONTH(in_date) = MONTH(CURRENT_DATE())) total_days\r\n"
			+ "LEFT JOIN\r\n"
			+ "    employee_att ea ON e.trainee_id = ea.trainee_id AND total_days.date = DATE(ea.in_date)"
			+ "  where e.status =true "
			+ "GROUP BY\r\n"
			+ "    s.salary_type_id, sl.salary_type_list_id, sl.trainee_id, sl.salary_amount, sl.salary_date,\r\n"
			+ "    e.trainee_id, e.user_name, e.department_id, dt.department_name,\r\n"
			+ "    MONTHNAME(total_days.date), MONTH(total_days.date)\r\n"
			+ "ORDER BY\r\n"
			+ "    e.trainee_id, e.user_name, e.user_id, MONTH(total_days.date);\r\n"
			+ "", nativeQuery = true)
	List<Map<String, Object>> getAllDetailsForSalarytrainee();

	@Query(value = "select s.*,e.user_name,d.department_id,dt.designation_id,d.department_name,dt.designation_name from salary_type_list as s"
			+ " join employee as e on e.employee_id = s.employee_id"
			+ " join department as d on d.department_id = e.department_id"
			+ " join designation as dt on dt.designation_id = e.designation_id", nativeQuery = true)
	List<Map<String, Object>> getAllDetailsForSalaryByDetails();
	
	
	@Query(value = "select s.*,e.user_name,d.department_id,d.department_name from salary_type_list as s"
			+ " join training_details as e on e.trainee_id = s.trainee_id"
			+ " join department as d on d.department_id = e.department_id", nativeQuery = true)
	List<Map<String, Object>> getAllDetailsForSalaryByDetailstrainee();
	
	Long findEmployeeIdBySalaryTypeList(SalaryType salary);

}
