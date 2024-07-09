package com.example.erp.repository.accounting;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.accounting.CompanyAssets;

public interface CompanyAssetsRepository extends JpaRepository<CompanyAssets, Long> {

	@Query(value = " select c.*,b.brand_name,a.accessories_name" + " from company_assets as c"
			+ " join brand as b on b.brand_id=c.brant_id"
			+ " join accessories as a on a.accessories_id=c.accessories_id;", nativeQuery = true)
	List<Map<String, Object>> AllEmployees();

//	CompanyAssets findByAccessoriesIdAndBrantId(Long accessoriesId, Long brantId);
	@Query(value = "select c.company_assets_id,c.asset_values,c.date,c.status,cl.company_assets_list_id,cl.accessories_id,"
			+ " cl.brand_id,cl.count ,b.brand_name,a.accessories_name from company_assets as c"
			+ " join company_assets_list as cl on c.company_assets_id = cl.company_assets_id"
			+ " join brand as b on b.brand_id=cl.brand_id"
			+ " join accessories as a on a.accessories_id=cl.accessories_id", nativeQuery = true)
	List<Map<String, Object>> getAllDetails();

	
	@Query(value = " select a.accessories_name,a.accessories_id,coalesce(sum(cs.count), 0) as count, a.color"
			+ " from accessories as a"
			+ " join company_assets_list as cs on cs.accessories_id = a.accessories_id"
			+ " group by a.accessories_name, a.accessories_id", nativeQuery = true)
	List<Map<String, Object>> yearlyExpenseexpanceprevioustypeincome();

	@Query(value = "SELECT c.company_assets_id AS companyAssetsId, c.asset_values AS assetValues, c.date, c.status, cl.company_assets_list_id AS companyAssetsListId, cl.accessories_id AS accessoriesId,"
	        + " cl.brand_id AS brandId, cl.count, b.brand_name AS brandName, a.accessories_name AS accessoriesName FROM company_assets AS c"
	        + " JOIN company_assets_list AS cl ON c.company_assets_id = cl.company_assets_id"
	        + " JOIN brand AS b ON b.brand_id = cl.brand_id"
	    	+ " join accessories as a on a.accessories_id=cl.accessories_id"
	        + " WHERE c.date BETWEEN :startDate AND :endDate"
	        + " ORDER BY c.asset_values DESC", nativeQuery = true)
	List<Map<String, Object>> getAllpromotionsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query(value = "select c.company_assets_id as companyAssetsId,c.asset_values as assetValues,c.date,c.status,cl.company_assets_list_id as companyAssetsListId,cl.accessories_id as accessoriesId,"
			+ " cl.brand_id as brandId,cl.count ,b.brand_name as brandName,a.accessories_name as accessoriesName from company_assets as c"
			+ " join company_assets_list as cl on c.company_assets_id = cl.company_assets_id"
			+ " join brand as b on b.brand_id=cl.brand_id"
			+ " join accessories as a on a.accessories_id=cl.accessories_id"
			+ " WHERE  YEAR(c.date) = :year  "
			+ " ORDER BY"
			+ "    c.asset_values DESC ;",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAnd(@Param("year") String year);
	
	@Query(value = "select c.company_assets_id as companyAssetsId,c.asset_values as assetValues,c.date,c.status,cl.company_assets_list_id as companyAssetsListId,cl.accessories_id as accessoriesId,"
			+ " cl.brand_id as brandId,cl.count ,b.brand_name as brandName,a.accessories_name as accessoriesName from company_assets as c"
			+ " join company_assets_list as cl on c.company_assets_id = cl.company_assets_id"
			+ " join brand as b on b.brand_id=cl.brand_id"
			+ " join accessories as a on a.accessories_id=cl.accessories_id"
			+ " WHERE  monthname(c.date) = :monthName "
			+ " ORDER BY"
			+ "    c.asset_values DESC;",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndMonth(@Param("monthName") String monthName);

		
	@Query(value = "select c.company_assets_id as companyAssetsId,c.asset_values as assetValues,c.date,c.status,cl.company_assets_list_id as companyAssetsListId,cl.accessories_id as accessoriesId,"
			+ " cl.brand_id as brandId,cl.count ,b.brand_name as brandName,a.accessories_name as accessoriesName from company_assets as c"
			+ " join company_assets_list as cl on c.company_assets_id = cl.company_assets_id"
			+ " join brand as b on b.brand_id=cl.brand_id"
			+ " join accessories as a on a.accessories_id=cl.accessories_id",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndMonthalter();

}
