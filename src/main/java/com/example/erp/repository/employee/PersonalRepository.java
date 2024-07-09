package com.example.erp.repository.employee;


import java.util.Optional;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.employee.Personal;

public interface PersonalRepository extends JpaRepository<Personal , Long>{
	
	@Query(value =
		    "SELECT e.user_name, e.user_id, p.* " +
		    "FROM personal AS p " +
		    "JOIN employee AS e ON e.employee_id = p.employee_id " +
		    "WHERE e.employee_id = :employeeId",
		    nativeQuery = true)
		List<Map<String, Object>> getAllPersonal(@Param("employeeId") Long employeeId);


	Optional<Personal> findByEmployeeId(long id);
	
	Optional<Personal> findByUserId(long id);


	
	@Query(value = "   SELECT  b.personal_id as personalId, b.married,e.status,u.status as userStatus, b.nationality, b.employee_id as employeeId, b.no_of_children as noOfChildren, b.passport_exp_date as passportExpDate, b.user_id as userId, b.role_id as roleId,"
			+ "  b. passport_no as passportNo,b.religion ,b.tel  "
			+ " FROM  personal AS b"
			+ " left JOIN employee AS e ON e.employee_id = b.employee_id"
			+ " left JOIN user AS u ON u.user_id = b.user_id  "
			+ " jOIN role AS r ON r.role_id = b.role_id"
			+ " WHERE (b.employee_id = :id OR b.user_id = :id) AND b.role_id = :role_id", nativeQuery = true)
	Map<String, Object> attendanceForTraineeIdAndRoleId(@Param("id") Long id, @Param("role_id") long roleId);

	

}
