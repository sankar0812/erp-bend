package com.example.erp.repository.employee;


import java.util.Optional;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.erp.entity.employee.EmergencyContacts;
import com.example.erp.entity.employee.Personal;

public interface EmergencyContactsRepository extends JpaRepository<EmergencyContacts, Long>{

	Optional<EmergencyContacts> findByEmployeeId(long id);

	

	
	
	@Query(value =
		    "SELECT e.user_name, e.user_id, c.* " +
		    "FROM contacts AS c " +
		    "JOIN employee AS e ON e.employee_id = c.employee_id " +
		    "WHERE e.employee_id = :employeeId",
		    nativeQuery = true)
		List<Map<String, Object>> getAllEmergencyContactsByEmployeeId(@Param("employeeId") Long employeeId);





	@Query(value = "  SELECT  b.emergency_contacts_id as emergencyContactsId, e.status,u.status as userStatus,b.address,b.employee_id as employeeId, b.phone_number as phoneNumber, b.relatino_name as relatinoName, b.user_id as userId, b.role_id as roleId"
			+ " FROM  contacts AS b"
			+ " left JOIN employee AS e ON e.employee_id = b.employee_id"
			+ " left JOIN user AS u ON u.user_id = b.user_id  "
			+ " jOIN role AS r ON r.role_id = b.role_id"
			+ " WHERE (b.employee_id = :id OR b.user_id = :id) AND b.role_id = :role_id", nativeQuery = true)
	Map<String, Object> attendanceForTraineeIdAndRoleId(@Param("id") Long id, @Param("role_id") long roleId);






	
}
