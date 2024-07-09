package com.example.erp.repository.accounting;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.accounting.ServerMaintenance;

public interface ServerMaintenanceRepository extends JpaRepository<ServerMaintenance, Long>{
	
	@Query(value = " select sm.amount,sm.server_maintenance_id,sm.date ,sm.server_type_id,sm.status,st.server_type_name "
			+ " from server_maintenance as sm"
			+ " join server_type as st on st.server_type_id=sm.server_type_id", nativeQuery = true)
	List<Map<String, Object>> AllserverMaintenance1();

	
	@Query(value = " select sm.amount,sm.server_maintenance_id as serverMaintenanceId,sm.date ,sm.server_type_id as serverTypeId,sm.status,st.server_type_name as serverTypeName "
			+ " from server_maintenance as sm"
			+ " join server_type as st on st.server_type_id=sm.server_type_id"
			+ " WHERE  YEAR(sm.date) = :year  "
			+ "  ;",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAnd(@Param("year") String year);
	
	
	@Query(value = " select sm.amount,sm.server_maintenance_id as serverMaintenanceId,sm.date ,sm.server_type_id as serverTypeId,sm.status,st.server_type_name as serverTypeName "
			+ " from server_maintenance as sm"
			+ " join server_type as st on st.server_type_id=sm.server_type_id"
			+ " WHERE  monthname(sm.date) = :monthName "
			,nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndMonth(@Param("monthName") String monthName);
	
	
	@Query(value = " select sm.amount,sm.server_maintenance_id as serverMaintenanceId,sm.date ,sm.server_type_id as serverTypeId,sm.status,st.server_type_name as serverTypeName "
			+ " from server_maintenance as sm"
			+ " join server_type as st on st.server_type_id=sm.server_type_id"
	        + " WHERE sm.date >= DATE_SUB(NOW(), INTERVAL 6 MONTH)", nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndSixMonths();




}
