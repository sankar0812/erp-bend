package com.example.erp.repository.employee;

import java.util.Optional;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.employee.Bank;
import com.example.erp.entity.employee.Personal;

public interface BankRepository extends JpaRepository<Bank, Long> {

	@Query(value = "SELECT e.*, d.designation_name, dd.department_name " + "FROM employee AS e "
			+ "JOIN designation AS d ON d.designation_id = e.designation_id "
			+ "JOIN department AS dd ON dd.department_id = e.department_id "
			+ "WHERE e.employee_id = :employeeId", nativeQuery = true)
	List<Map<String, Object>> getAllRoleByEmployees3(@Param("employeeId") Long employeeId);

	@Query(value = "SELECT e.user_name, e.user_id, b.* " + "FROM bank AS b "
			+ "JOIN employee AS e ON e.employee_id = b.employee_id "
			+ "WHERE e.employee_id = :employeeId", nativeQuery = true)
	List<Map<String, Object>> getAllBank(@Param("employeeId") Long employeeId);

	@Query(value = "SELECT e.user_name, e.user_id, p.* " + "FROM personal AS p "
			+ "JOIN employee AS e ON e.employee_id = p.employee_id "
			+ "WHERE e.employee_id = :employeeId", nativeQuery = true)
	List<Map<String, Object>> getAllPersonal(@Param("employeeId") Long employeeId);

	@Query(value = "SELECT e.user_name, e.user_id, c.* " + "FROM contacts AS c "
			+ "JOIN employee AS e ON e.employee_id = c.employee_id "
			+ "WHERE e.employee_id = :employeeId", nativeQuery = true)
	List<Map<String, Object>> getAllEmergencyContacts(@Param("employeeId") Long employeeId);

	@Query(value = "SELECT q.qualification_id, q.employee_id, q.highest_qualification, "
			+ "       q.resumeurl, q.tenurl, q.aadharurl, "
			+ "       q.degreeurl, q.pannourl, q.twelveurl, q.aadharno, q.status, " + "       e.user_name, e.user_id "
			+ "FROM qualification AS q " + "JOIN employee AS e ON e.employee_id = q.employee_id "
			+ "WHERE e.employee_id = :employeeId", nativeQuery = true)
	List<Map<String, Object>> getQualifications(@Param("employeeId") Long employeeId);

	@Query(value = " select e.user_name,e.user_id,f.*" + " from family as f "
			+ " join employee as e on e.employee_id= f.employee_id"
			+ " where e.employee_id=:employeeId", nativeQuery = true)
	List<Map<String, Object>> getAllFamilyInformations(@Param("employeeId") Long employeeId);

	Optional<Bank> findByEmployeeIdAndRoleId(long id, long roleid);

	@Query(value = " SELECT"
			+ "    b.bank_id as bankId, b.account_number as accountNumber, e.status,u.status as userStatus,b.bank_name as bankName, b.employee_id as employeeId,b.branch_name as branchName, b.holder_name holderName, b.ifse_code as ifseCode, b.pan_number as panNumber, b.user_id as userId, b.role_id as roleId"
			+ " FROM  bank AS b"
			+ " left JOIN employee AS e ON e.employee_id = b.employee_id"
			+ " left JOIN user AS u ON u.user_id = b.user_id  "
			+ " JOIN role AS r ON r.role_id = b.role_id"
			+ " WHERE"
			+ "    (b.employee_id = :id OR b.user_id = :id)"
			+ "    AND b.role_id = :role_id", nativeQuery = true)
	Map<String, Object> attendanceForTraineeIdAndRoleId(@Param("id") Long id, @Param("role_id") long roleId);

	Optional<Bank> findByEmployeeId(long id);
	
	Optional<Bank> findByUserId(long id);

	Optional<Bank> findByPanNumber(String pan);

	

}
