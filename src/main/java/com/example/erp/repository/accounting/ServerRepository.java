package com.example.erp.repository.accounting;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.accounting.Server;

public interface ServerRepository extends JpaRepository<Server, Long> {

	@Query(value = "select s.server_id,s.status,s.date,s.server_name,sl.server_list_id,sl.amount,sl.server_type_id,st.server_type_name,s.paying"
			+ " from server as s"
			+ " join server_list as sl on sl.fk_server_id = s.server_id"
			+ " join server_type as st on st.server_type_id=sl.server_type_id", nativeQuery = true)
	List<Map<String, Object>> getAllServerDetails();

	
	
	@Query(value = "SELECT s.server_id AS serverId, s.status, s.date, s.server_name AS serverName, sl.server_list_id AS serverListId, sl.amount, sl.server_type_id AS serverTypeId, st.server_type_name AS server_typeName, s.paying"
	        + " FROM server AS s"
	        + " JOIN server_list AS sl ON sl.fk_server_id = s.server_id"
	        + " JOIN server_type AS st ON st.server_type_id = sl.server_type_id"
	        + " WHERE s.date BETWEEN :startDate AND :endDate"
	        + " ORDER BY sl.amount DESC", nativeQuery = true)
	List<Map<String, Object>> getAllpromotionsBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

	@Query(value = "select s.server_id as serverId ,s.status,s.date,s.server_name as serverName,sl.server_list_id as serverListId,sl.amount,sl.server_type_id as serverTypeId,st.server_type_name as server_typeName,s.paying"
			+ " from server as s"
			+ " join server_list as sl on sl.fk_server_id = s.server_id"
			+ " join server_type as st on st.server_type_id=sl.server_type_id"
		, nativeQuery = true)
		List<Map<String, Object>> getAllpromotionsBetweenDatesserver();


	
	@Query(value = "select s.server_id as serverId ,s.status,s.date,s.server_name as serverName,sl.server_list_id as serverListId,sl.amount,sl.server_type_id as serverTypeId,st.server_type_name as server_typeName,s.paying"
			+ " from server as s"
			+ " join server_list as sl on sl.fk_server_id = s.server_id"
			+ " join server_type as st on st.server_type_id=sl.server_type_id"
			+ " WHERE  YEAR(s.date) = :year  "
			+ " ORDER BY"
			+ "    sl.amount DESC ;",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAnd(@Param("year") String year);
	
	
	@Query(value = "select s.server_id as serverId ,s.status,s.date,s.server_name as serverName,sl.server_list_id as serverListId,sl.amount,sl.server_type_id as serverTypeId,st.server_type_name as server_typeName,s.paying"
			+ " from server as s"
			+ " join server_list as sl on sl.fk_server_id = s.server_id"
			+ " join server_type as st on st.server_type_id=sl.server_type_id"
			+ " WHERE  monthname(s.date) = :monthName "
			+ " ORDER BY"
			+ "    sl.amount DESC;",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndMonth(@Param("monthName") String monthName);

}
