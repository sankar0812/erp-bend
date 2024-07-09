package com.example.erp.repository.accounting;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.accounting.Accessories;




public interface AccessoriesRepository extends JpaRepository<Accessories, Long>{
	
	
	@Query(value = "select a.accessories_id,a.accessories_name,a.color"
			+ " from accessories as a "
		, nativeQuery = true)
	List<Map<String, Object>> AllEmployeesaccessories();

}
