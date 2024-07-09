package com.example.erp.repository.employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.employee.Employee;
import com.example.erp.entity.employee.EmployeeLeave;
import com.example.erp.entity.employee.Promotions;

public interface PromotionsRepository extends JpaRepository<Promotions, Long> {

		@Query(value = "SELECT e.user_name, p.*, r.role_name\r\n"
				+ " FROM promotions AS p\r\n"
				+ " LEFT JOIN role AS r ON r.role_id = p.role_id\r\n"
				+ " JOIN employee AS e ON e.employee_id = p.employee_id\r\n"
				+ " ORDER BY p.promotions_id DESC;"
				+ ";",nativeQuery = true)
	List<Map<String,Object>>GoodAllWorks();


		@Query(value = "select e.first_name ,e.last_name ,p.*,r.role_name"
				+ " from promotions as p"
				+ " join employee as e on e.employee_id=p.employee_id"
				 + "	     join role as r on r.role_id=p.role_id"
				+ " where p.employee_id=:employee_id",nativeQuery = true)
		List<Map<String, Object>> Allpromotions(@Param ("employee_id")long  employee_id);
		
		@Query(value = "SELECT e.first_name, e.last_name, p.*,r.role_name "
				+ "FROM promotions AS p "
				+ " JOIN employee AS e ON e.employee_id = p.employee_id "
				 + "	     join role as r on r.role_id=p.role_id"
				+ " WHERE p.date BETWEEN :startDate AND :endDate", nativeQuery = true)
List<Map<String, Object>> getAllpromotionsBetweenDates(  @Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

		@Query(value = ""
				+ "select e.user_name ,r.role_name,p.promotions_id,p.date,p.description,p.employee_id,p.promotions_by,p.role_id,p.role_type"
				+ "				from promotions as p"
				+ "					     join role as r on r.role_id=p.role_id"
				+ "				 join employee as e on e.employee_id=p.employee_id"
				+ " where monthname(p.date) = :monthName",nativeQuery = true)
		List<Map<String, Object>> getAllpromotionssemployee(@Param("monthName")String monthName);

		@Query(value = "select e.user_name ,r.role_name,p.promotions_id,p.date,p.description,p.employee_id,p.promotions_by,p.role_id,p.role_type"
				+ "				from promotions as p"
				+ "					     join role as r on r.role_id=p.role_id"
				+ "				 join employee as e on e.employee_id=p.employee_id"
				+ " where year(p.date) = :year",nativeQuery = true)
		List<Map<String, Object>> getAllpromotions(@Param("year")String year);




		  Optional<Employee> findByEmployeeIdAndRoleId(Long employeeId, long roleId);
}
