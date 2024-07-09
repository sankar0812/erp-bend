package com.example.erp.repository.employee;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.employee.Qualification;

public interface QualificationRepository extends JpaRepository<Qualification, Long> {

	@Query(value = "select q.qualification_id, q.employee_id, q.highest_qualification,e.status,u.status as userStatus, q.resumeurl, q.tenurl, q.aadharurl, q.degreeurl, "
			+ "q.pannourl, q.twelveurl,q.aadharno,q.role_id,q.user_id,q.image_status as imageStatus"
			+ " from qualification as q " 
			+ " left JOIN employee AS e ON e.employee_id = q.employee_id"
			+ " left JOIN user AS u ON u.user_id = q.user_id  "
			+ " jOIN role AS r ON r.role_id = q.role_id "
			+ " WHERE (q.employee_id = :id OR q.user_id = :id) AND q.role_id = :role_id", nativeQuery = true)
	List<Map<String, Object>> getAllQualificationsByImage(@Param("id") Long id, @Param("role_id") Long role_id);

	Optional<Qualification> findByEmployeeId(long id);

	@Query(value = "SELECT q.qualification_id, q.employee_id, q.highest_qualification, "
			+ "       q.resumeurl, q.tenurl, q.aadharurl, "
			+ "       q.degreeurl, q.pannourl, q.twelveurl, q.aadharno, q.status, " + "       e.user_name, e.user_id "
			+ " FROM qualification AS q " + " JOIN employee AS e ON e.employee_id = q.employee_id "
			+ " WHERE e.employee_id = :employeeId", nativeQuery = true)
	List<Map<String, Object>> getQualifications(@Param("employeeId") Long employeeId);

}
