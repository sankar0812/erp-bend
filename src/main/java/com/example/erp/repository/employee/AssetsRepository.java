package com.example.erp.repository.employee;



import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.employee.Assets;
import com.example.erp.entity.employee.EmployeeAttendance;

public interface AssetsRepository extends JpaRepository<Assets, Long> {

	@Query(value = " select b.brand_id, b.brand_name,a.accessories_name,a.accessories_id,sum(ca.count) as count from brand as b"
			+ " join company_assets_list as ca on b.brand_id = ca.brand_id"
			+ " join accessories as a on a.accessories_id = ca.accessories_id"
			+ " group by b.brand_id,b.brand_name,a.accessories_name,a.accessories_id", nativeQuery = true)
	List<Map<String, Object>> getAllRoleByEmployees();
	
	
	@Query(value = "SELECT b.brand_id,b.brand_name,a.accessories_name,a.accessories_id,SUM(ca.count) AS count,ca.company_assets_id "
			+ " FROM brand AS b"
			+ " JOIN company_assets_list AS ca ON b.brand_id = ca.brand_id"
			+ " JOIN accessories AS a ON a.accessories_id = ca.accessories_id"
			+ " WHERE a.accessories_id = :accessories_id AND b.brand_id = :brand_id"
			+ " GROUP BY b.brand_id,b.brand_name,a.accessories_name,a.accessories_id,ca.company_assets_id", nativeQuery = true)
	List<Map<String, Object>> getAllRoleByEmployeesWithID(Long accessories_id, Long brand_id);
	
	@Query(value = "select a.assets_id,a.assets_date,a.employee_id,a.department_id,al.assets_list_id,al.accessories_id,"
			+ " al.count,al.brand_id,e.user_name,d.department_name,ac.accessories_name,b.brand_name,e.url,al.serial_number"
			+ " from assets as a"
			+ " join assets_list as al on a.assets_id=al.fk_assets_id"
			+ " join employee as e on e.employee_id=a.employee_id"
			+ " join department as d on d.department_id=a.department_id"
			+ " join accessories as ac on ac.accessories_id=al.accessories_id"
			+ " join brand as b on b.brand_id=al.brand_id;", nativeQuery = true)
	List<Map<String, Object>> getAllServerDetails();
	
	@Query(value = "select a.assets_id,a.assets_date,a.employee_id,a.department_id,al.assets_list_id,al.accessories_id,"
			+ " al.count,al.brand_id,e.user_name,d.department_name,ac.accessories_name,b.brand_name,e.url,al.serial_number"
			+ " from assets as a"
			+ " join assets_list as al on a.assets_id=al.fk_assets_id"
			+ " join employee as e on e.employee_id=a.employee_id"
			+ " join department as d on d.department_id=a.department_id"
			+ " join accessories as ac on ac.accessories_id=al.accessories_id"
			+ " join brand as b on b.brand_id=al.brand_id"
			+ " where e.employee_id =:employee_id", nativeQuery = true)
	List<Map<String, Object>> getAllServerDetailsemployeeId(@Param("employee_id") long employee_id);


	Optional<Assets> findByEmployeeId(Long employeeId);

	@Query(value = "SELECT a.assets_id as assetsId, a.assets_date as assetsDate, a.employee_id as employeeId, a.department_id as departmentId, al.assets_list_id as assetsListId, al.accessories_id as accessoriesId, "
	        + " al.count, al.brand_id as brandId, e.user_name as userName, d.department_name as departmentName, ac.accessories_name as  accessoriesName, b.brand_name as brandName, e.url, al.serial_number as serialNumber "
	        + " FROM assets AS a "
			+ " join assets_list as al on a.assets_id=al.fk_assets_id"
			+ " join employee as e on e.employee_id=a.employee_id"
			+ " join department as d on d.department_id=a.department_id"
			+ " join accessories as ac on ac.accessories_id=al.accessories_id"
			+ " join brand as b on b.brand_id=al.brand_id"
			+ " where year(a.assets_date) =:year ;", nativeQuery = true)
	List<Map<String, Object>> getAllAssetseYear(@Param("year") String year);
	
	@Query(value = "SELECT a.assets_id as assetsId, a.assets_date as assetsDate, a.employee_id as employeeId, a.department_id as departmentId, al.assets_list_id as assetsListId, al.accessories_id as accessoriesId, "
	        + " al.count, al.brand_id as brandId, e.user_name as userName, d.department_name as departmentName, ac.accessories_name as  accessoriesName, b.brand_name as brandName, e.url, al.serial_number as serialNumber "
	        + " FROM assets AS a "
	        + " JOIN assets_list AS al ON a.assets_id = al.fk_assets_id "
	        + " JOIN employee AS e ON e.employee_id = a.employee_id "
	        + " JOIN department AS d ON d.department_id = a.department_id "
	        + " JOIN accessories AS ac ON ac.accessories_id = al.accessories_id "
	        + " JOIN brand AS b ON b.brand_id = al.brand_id "
	        + " WHERE MONTHNAME(a.assets_date) = :monthName", nativeQuery = true)
	List<Map<String, Object>> getAllassetse(@Param("monthName") String monthName);


	@Query(value = " SELECT a.assets_id as assetsId, a.assets_date as assetsDate, a.employee_id as employeeId, a.department_id as departmentId, al.assets_list_id as assetsListId, al.accessories_id as accessoriesId, "
	        + " al.count, al.brand_id as brandId, e.user_name as userName, d.department_name as departmentName, ac.accessories_name as  accessoriesName, b.brand_name as brandName, e.url, al.serial_number as serialNumber "
	        + " FROM assets AS a "
	        + " JOIN assets_list AS al ON a.assets_id = al.fk_assets_id "
	        + " JOIN employee AS e ON e.employee_id = a.employee_id "
	        + " JOIN department AS d ON d.department_id = a.department_id "
	        + " JOIN accessories AS ac ON ac.accessories_id = al.accessories_id "
	        + " JOIN brand AS b ON b.brand_id = al.brand_id ", nativeQuery = true)
	List<Map<String, Object>> getAllassetsedetail();

	
}
