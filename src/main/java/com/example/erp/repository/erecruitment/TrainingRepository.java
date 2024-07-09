package com.example.erp.repository.erecruitment;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.erecruitment.Training;

public interface TrainingRepository extends JpaRepository<Training, Long> {
	
	@Query(value = " select t.trainee_id as traineeId ,t.address,t.city,t.country,t.user_id as userId,t.date_of_birth as dateOfBirth,t.department_id as departmentId,t.email,t.end_date as endDate,t.gender,t.marital_status as maritalStatus,"
			+ " t.mobile_number as mobileNumber,t.role_id as roleId,t.user_name as userName,t.role_name as roleName,t.trainee_status as traineeStatus,"
			+ " d.department_name as departmentName,t.state,t.password,t.confirm_password as confirmPassword,t.status"
			+ "  from training_details as t"
			+ " join department as d on d.department_id=t.department_id"
			+ "  order by  t.trainee_id DESC", nativeQuery = true)
	List<Map<String, Object>> findTrainingDetails();

	Training findByEmail(String email);

	@Query(value = " select t.trainee_id ,t.department_id ,t.role_id ,t.user_name,r.role_name ,d.department_name,m.member_list_id as memberListId  "
			+ " from training_details as t"
			+ " join department as d on d.department_id=t.department_id"
			+ " join role as r on r.role_id=t.role_id "
			+ "  join member_list as m on m.email = t.email"
			+ " where t.trainee_id = :trainee_id", nativeQuery = true)	
	List<Map<String, Object>> getAllClientDetails(Long trainee_id);
	
	@Query(value = " select t.trainee_id as traineeId ,t.address,t.city,t.country,t.date_of_birth as dateOfBirth,t.department_id as departmentId,t.email,t.end_date as endDate,t.user_id as userId,t.gender,t.marital_status as maritalStatus,"
			+ " t.mobile_number as mobileNumber,t.role_id as roleId,t.user_name as userName,t.role_name as roleName,t.trainee_status as traineeStatus,"
			+ " d.department_name as departmentName,t.state,t.password,t.confirm_password as confirmPassword,t.status"
			+ "  from training_details as t"
			+ " join department as d on d.department_id=t.department_id"
			+ " where t.trainee_id=:trainee_id", nativeQuery = true)
	List<Map<String, Object>> getAllEmployeesWithDetailsWithId(@Param("trainee_id") Long trainee_id);
	////////started/////
	@Query(value = " select t.trainee_id as traineeId ,t.address,t.city,t.country,t.date_of_birth as dateOfBirth,t.department_id as departmentId,t.email,t.user_id as userId,t.end_date as endDate,t.gender,t.marital_status as maritalStatus,"
			+ " t.mobile_number as mobileNumber,t.role_id as roleId,t.user_name as userName,t.role_name as roleName,t.trainee_status as traineeStatus,"
			+ " d.department_name as departmentName,t.state,t.password,t.confirm_password as confirmPassword,t.status"
			+ "  from training_details as t"
			+ " join department as d on d.department_id=t.department_id"
			+ " where  t.started =true;", nativeQuery = true)
	List<Map<String, Object>> findTrainingDetailsstarted();
/////////// cancel//////////
	@Query(value = " select t.trainee_id as traineeId ,t.address,t.city,t.country,t.date_of_birth as dateOfBirth,t.department_id as departmentId,t.user_id as userId,t.email,t.end_date as endDate,t.gender,t.marital_status as maritalStatus,"
			+ " t.mobile_number as mobileNumber,t.role_id as roleId,t.user_name as userName,t.role_name as roleName,t.trainee_status as traineeStatus,"
			+ " d.department_name as departmentName,t.state,t.password,t.confirm_password as confirmPassword,t.status"
			+ "  from training_details as t"
			+ " join department as d on d.department_id=t.department_id "
			+ "    where  t.cancel =true;", nativeQuery = true)
	List<Map<String, Object>> findTrainingDetailscancel();
////////////completed////////////
	@Query(value = " select t.trainee_id as traineeId ,t.address,t.city,t.country,t.date_of_birth as dateOfBirth,t.department_id as departmentId,t.user_id as userId,t.email,t.end_date as endDate,t.gender,t.marital_status as maritalStatus,"
			+ " t.mobile_number as mobileNumber,t.role_id as roleId,t.user_name as userName,t.role_name as roleName,t.trainee_status as traineeStatus,"
			+ " d.department_name as departmentName,t.state,t.password,t.confirm_password as confirmPassword,t.status"
			+ "  from training_details as t"
			+ " join department as d on d.department_id=t.department_id"
			+ " where  t.completed =true;", nativeQuery = true)
	List<Map<String, Object>> findTrainingDetailscompleted();

	@Query(value = "select e.trainee_id,e.user_name,d.department_id,d.department_name, e.user_id  from training_details as e"
			+ " join department as d on d.department_id = e.department_id", nativeQuery = true)
	List<Map<String, Object>> getAllRoleByEmployees();

	@Query(value=" select a.trainee_id,a.confirm_password,a.email,a.user_name,a.password,a.role_name,a.status,a.start_date,a.intime,a.role_id"
			+ " from training_details  as a "
			+ " where a.trainee_id =:id and a.role_id=:role_id", nativeQuery = true)
	List<Map<String, Object>> getAllEmployeesWithDetailsWithIdEmployeeTrainee(@Param("id") Long admin_id,@Param("role_id") Long role_id);

	Training findByMobileNumber(String mobileNumber);

	Optional<Training> findByMobileNumberAndEmail(String mobileNumber, String email);
	
	
}
