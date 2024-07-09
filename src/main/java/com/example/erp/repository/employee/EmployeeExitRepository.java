package com.example.erp.repository.employee;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.erp.entity.employee.EmployeeExit;

public interface EmployeeExitRepository extends JpaRepository<EmployeeExit, Long>{
	
	
	@Query(value = "select e.user_name ,c.*"
			+ " from employeeexit as c"
			+ " join employee as e on e.employee_id=c.employee_id", nativeQuery = true)
	List<Map<String, Object>> getAllProjectWork();
	
	
	@Query(value = "select e.user_name,e.employee_id,e.department_id,d.department_name,al.assets_list_id,"
			+ " al.accessories_id,al.brand_id,al.count,al.serial_number,al.balance_count,al.returncount ,ac.accessories_name,b.brand_name "
			+ " from employee as e "
			+ " left join department as d on d.department_id=e.department_id"
			+ " left join assets as a on a.employee_id= e.employee_id"
			+ " left join assets_list as al on al.fk_assets_id=a.assets_id"
			+ " left join accessories as ac on ac.accessories_id=al.accessories_id"
			+ " left join brand as b on b.brand_id=al.brand_id"
			+ " where e.status=true", nativeQuery = true)
	List<Map<String, Object>> getAllServerDetails();



	@Query(value = " select e.employee_exit_id,e.date,e.department_id,e.description,e.employee_id,e.status,"
			+ " cp.company_propertylist_id ,cp. accessories_id,cp.balance_count ,cp.brand_id ,cp.count ,cp.return_count ,"
			+ " d.department_name ,emp.user_name,emp.user_id,ac.accessories_name,b.brand_name,cp.serial_number,cp.balance_countt "
			+ " from employeeexit as e "
			+ " left join company_property_list as cp on e.employee_exit_id=cp.fk_employee_exit_id "
			+ " left join employee as emp on e.employee_id= emp.employee_id"
			+ " left join department as d on d.department_id = e.department_id"
			+ " left join accessories as ac on ac.accessories_id=cp.accessories_id"
			+ " left join brand as b on b.brand_id=cp.brand_id", nativeQuery = true)
	List<Map<String, Object>> getemployeeExit();
	
//	@Query(value = " select"
//			+ "    e.employee_id ,"
//			+ "    e.user_name,"
//			+ "    de.designation_name,"
//			+ "    de.designation_id,"
//			+ "    c.company_id,"
//			+ "    c.company_name,"
//			+ "    date_format(e.date, '%m %y') as date,"
//			+ "    date_format(ee.date, '%m %y') as noticedate,"
//			+ "    c.country,"
//			+ "    c.email,"
//			+ "    c.gst_no,"
//			+ "    c.address,"
//			+ "    c.phone_number1,"
//			+ "    c.phone_number2,"
//			+ "    c.state,"
//			+ "    c.pincode"
//			+ " from employee as e"
//			+ " join company as c on c.company_id = e.company_id"
//			+ " join designation as de on de.designation_id = e.designation_id"
//			+ " join employeeexit as ee on ee.employee_id = e.employee_id"
//			+ " where ee.employee_id= :employee_id;", nativeQuery = true)
//	List<Map<String, Object>> AllNotifications1(@Param("employee_id") long employee_id);


	@Query(value = " select e.employee_id, e.user_name,de.designation_name,de.designation_id,c.company_id,c.company_name,DATE_FORMAT(e.date, '%M %Y') AS date,"
			+ "    DATE_FORMAT(ee.date, '%M %Y') AS noticeDate,"
			+ " c.country,c.email,c.gst_no,c.address,c.phone_number1,c.phone_number2,c.state,c.pincode"
			+ "  from employee as e "
			+ " join company as c on c.company_id = e.company_id "
			+ " join designation as de on de.designation_id = e.designation_id"
			+ " join employeeexit as ee on ee.employee_id = e.employee_id"
			+ "  where ee.employee_id= :employee_id ;", nativeQuery = true)
	List<Map<String, Object>> AllDetailsandexit(@RequestParam("employee_id") long employee_id);

	@Query(value = "select ee.employee_exit_id as employeeExitId,ee.date,ee.department_id as departmentId,ee.description,ee.employee_id as employeeId,e.user_name as userName,e.user_id as userId,d.department_name as departmentName"
			+ " from employeeexit as ee"
			+ " join employee as e on e.employee_id=ee.employee_id"
			+ " join department as d on d.department_id = ee.department_id"
			+ " where monthname(ee.date) = :monthName", nativeQuery = true)
	List<Map<String, Object>> getAllemployeeexitemployee(@Param("monthName")String monthName);

	@Query(value = "select ee.employee_exit_id as employeeExitId,ee.date,ee.department_id as departmentId,ee.description,ee.employee_id as employeeId,e.user_name as userName,e.user_id as userId,d.department_name as departmentName"
			+ " from employeeexit as ee"
			+ " join employee as e on e.employee_id=ee.employee_id"
			+ " join department as d on d.department_id = ee.department_id"
			+ " where year(ee.date) = :year", nativeQuery = true)
	List<Map<String, Object>> getAllemployeeexit(@Param("year")String year);

}
