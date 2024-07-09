package com.example.erp.repository.admin;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.admin.AdminLogin;

public interface AdminLoginRepository extends JpaRepository<AdminLogin, Long>{

	AdminLogin findByEmail(String email);
	
	
	@Query(value=" SELECT SUM(employee_count) as employeecount,SUM(invoicecount) as projectcount,SUM(client_count) as clientcount,SUM(present_count) as presentcount"
			+ " FROM (SELECT COUNT(e.employee_id) as employee_count,0 as invoicecount,0 as client_count,0 as present_count"
			+ "  FROM employee as e WHERE e.status = true"
			+ "  UNION ALL SELECT COUNT(u.user_id) as employee_count,0 as invoicecount,0 as client_count,0 as present_count"
			+ "  FROM user as u  WHERE u.status = true UNION ALL"
			+ "  SELECT COUNT(t.trainee_id) as employee_count,0 as invoicecount,0 as client_count,0 as present_count"
			+ "  FROM training_details as t WHERE t.status = true"
			+ "  UNION ALL"
			+ "  SELECT 0 as employee_count,COUNT(i.project_id) as invoicecount,0 as client_count,0 as present_count FROM client_requirements as i"
			+ "    UNION ALL SELECT 0 as employee_count,0 as invoicecount,COUNT(c.client_id) as client_count,0 as present_count"
			+ "  FROM client_profile as c WHERE c.status = true"
			+ "  UNION ALL SELECT  0 as employee_count,  0 as invoicecount, 0 as client_count, COUNT(CASE WHEN a.employee_att_id IS NOT NULL THEN 1 END) as present_count"
			+ "  FROM employee e"
			+ " LEFT JOIN employee_att a ON e.employee_id = a.employee_id AND a.in_date = CURDATE()) as counts_from_each_table;;", nativeQuery = true)
	Map<String, Object>ProjectTypeDetails();

	
	@Query(value=" select a.id,a.confirm_password,a.email,a.name,a.password,a.role_type,a.status,a.date,a.intime,a.role_id"
			+ " from admin_login as a"
			+ " where a.id=:id and a.role_id=:role_id", nativeQuery = true)
	List<Map<String, Object>> getAllEmployeesWithDetailsWithId(@Param("id") Long admin_id,@Param("role_id") Long role_id);
	
	
	
	
	
	
}
