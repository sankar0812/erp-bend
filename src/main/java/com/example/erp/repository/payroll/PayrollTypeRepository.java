package com.example.erp.repository.payroll;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.payroll.PayrollTypeList;

public interface PayrollTypeRepository extends JpaRepository<PayrollTypeList, Long> {

	Optional<PayrollTypeList> findByEmployeeIdAndPaymentDate(Long id, LocalDate date);
	
	@Query(value="select p.*,e.user_name,d.department_name,ds.designation_name,month(p.payment_date) as month, year(p.payment_date) as year from payroll_type as p"
			+ " join employee as e on e.employee_id = p.employee_id"
			+ " join department as d on d.department_id = e.department_id"
			+ " join designation as ds on ds.designation_id = e.designation_id"
			+ " where month(p.payment_date) = month(current_date())  and year(p.payment_date) = year(current_date())", nativeQuery = true)
	List<Map<String, Object>> getAllPayrollListDetailsByMonthAndYear();

	
	@Query(value="select p.*,e.user_name,d.department_name,month(p.payment_date) as month, year(p.payment_date) as year "
			+ " from payroll_type as p"
			+ " join training_details as e on e.trainee_id = p.trainee_id"
			+ " join department as d on d.department_id = e.department_id"
			+ " where month(p.payment_date) = month(current_date())  and year(p.payment_date) = year(current_date())", nativeQuery = true)
	List<Map<String, Object>> getAllPayrollListDetailsByMonthAndYeartrainee();
	
	Optional<PayrollTypeList> findByTraineeIdAndPaymentDate(Long traineeId, LocalDate date);
	
//	 boolean existsByEmployeeIdAndMonth(String employeeId, YearMonth month);

}
