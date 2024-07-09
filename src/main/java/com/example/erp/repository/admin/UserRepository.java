package com.example.erp.repository.admin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PatchMapping;

import com.example.erp.entity.admin.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
	User findByMobileNumber (String mobilnumber);
	@Query(value = "select u.user_id as userId,u.address,u.city,u.country,u.email,u.location,u.mobile_number as mobileNumber,u.password,u.role_id as roleId,u.username,u.role_type as roleType,r.role_name as roleName from user as u"
			+ " join role as r on r.role_id = u.role_id", nativeQuery = true)
	List<Map<String, Object>> getManagerDetails();


	@Query(value = "SELECT e.employee_id, e.status,r.role_id,r.role_name " + " FROM employee AS e "
			+ " join role as r on r.role_id=e.role_id " + " WHERE e.employee_id = :employeeId"
			+ " and e.role_id = :roleID ", nativeQuery = true)
	Map<String, Object> getManagerDetailsById1(@Param("employeeId") long employeeId, @Param("roleID") long roleID);

	@Query(value = "SELECT u.user_id, u.status,r.role_id,r.role_name " + " FROM user AS u "
			+ " join role as r on r.role_id=u.role_id " + " WHERE u.user_id = :userId"
			+ " and u.role_id = :roleID", nativeQuery = true)
	Map<String, Object> getManagerDetailsById2(@Param("userId") long userId, @Param("roleID") long roleID);

	@Query(value = "SELECT c.client_id, c.status,r.role_id,r.role_name" + " FROM client_profile AS c "
			+ " join role as r on r.role_id=c.role_id " + " WHERE c.client_id = :clientId"
			+ " and c.role_id = :roleID", nativeQuery = true)
	Map<String, Object> getManagerDetailsById3(@Param("clientId") long clientId, @Param("roleID") long roleID);


	@Query(value = "select  u.user_id as userId,u.address,u.city,u.country,u.email,u.location,u.mobile_number as mobileNumber,u.password,u.role_id as roleId,u.username,u.role_type as roleType,r.role_name as roleName ,m.member_list_id as memberListId"
			+ " from user as u"
			+ " join role as r on r.role_id = u.role_id"
			+ " join member_list as m on m.email = u.email" 
			+ " where u.user_id =:user_id", nativeQuery = true)
	List<Map<String, Object>> getManagerDetailsById(Long user_id);

	@Query(value = "select a.* ,r.role_name" 
	+ " from admin_login as a" + " join role  as r on r.role_id = a.role_id"
			+ " where a.id=:id", nativeQuery = true)
	List<Map<String, Object>> getAllAdminDetailsById(Long id);

	List<User> findByUserId(Long userId);

	@Query(value = " select sum(employee_count) as employeecount,sum(presentcount) as presentcount,sum(absentcount) as absentcount,sum(traineecount) as traineecount"
			+ " from (select count(e.employee_id) as employee_count, 0 as presentcount, 0 as absentcount, 0 as traineecount from employee as e where e.status = true"
			+ "  union all select count(u.user_id) as employee_count, 0 as presentcount, 0 as absentcount, 0 as traineecount from user as u where u.status = true"
			+ "  union all select 0 as employee_count, 0 as presentcount, 0 as absentcount, count(t.trainee_id) as traineecount from training_details as t  where t.status = true"
			+ "  union all select 0 as employeecount,count(case when a.employee_att_id is not null then 1 end) as presentcount,count(case when a.employee_att_id is null then 1 end) as absentcount,0 as traineecount"
			+ " from employee e left join employee_att a on e.employee_id = a.employee_id and a.in_date = curdate()) as counts_from_each_table;"
			+ "", nativeQuery = true)
	Map<String, Object> getManagerDetailsmanager();
	
	
	@Query(value=" select a.user_id,a.confirm_password,a.email,a.username,a.password,a.role_type,a.status,a.date,a.intime,a.role_id"
			+ " from user  as a "
			+ " where a.user_id =:id and a.role_id=:role_id", nativeQuery = true)
	List<Map<String, Object>> getAllEmployeesWithDetailsWithIdUser(@Param("id") Long admin_id,@Param("role_id") Long role_id);

	List<User> findByStatusTrue();
	
	
	List<User> findByStatusFalse();
	
	Optional<User> findByMobileNumberAndEmail(String mobileNumber, String email);
	
	
	@Query(value = "SELECT c.trainee_id, c.status,r.role_id,r.role_name FROM training_details AS c "
			+ "		 join role as r on r.role_id=c.role_id  WHERE c.trainee_id =:traineeId"
			+ "			 and c.role_id =:roleID", nativeQuery = true)
	Map<String, Object> gettrainingById3(@Param("traineeId") long clientId, @Param("roleID") long roleID);



	
}
