package com.example.erp.repository.employee;


import java.util.Optional;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.employee.FamilyInformations;

public interface FamilyInformationsRepository extends JpaRepository<FamilyInformations, Long>{
	
	 @Query(value=
		     " select e.user_name,e.user_id,f.*"
		     + " from family as f "
		     + " join employee as e on e.employee_id= f.employee_id"
		     + " where e.employee_id=employeeId",nativeQuery = true)
		   List<Map<String, Object>> getAllFamilyInformations(@Param("employeeId") Long employeeId);


	Optional<FamilyInformations> findByEmployeeId(long id);


	@Query(value = " SELECT"
			+ "    b.family_informations_id as familyInformationsId, b.dob, b.name, e.status,u.status as userStatus,b.employee_id as employeeId, b.phone, b.relation_ship as relationShip, b.user_id as userId, b.role_id as roleId"
			+ " FROM"
			+ "    family AS b"
			+ " left JOIN employee AS e ON e.employee_id = b.employee_id"
			+ " left JOIN user AS u ON u.user_id = b.user_id  "
			+ " JOIN"
			+ "    role AS r ON r.role_id = b.role_id"
			+ " WHERE"
			+ "    (b.employee_id = :id OR b.user_id = :id)"
			+ "    AND b.role_id = :role_id", nativeQuery = true)
	Map<String, Object> attendanceForTraineeIdAndRoleId(@Param("id") Long id, @Param("role_id") long roleId);

}
