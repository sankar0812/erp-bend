package com.example.erp.repository.clientDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.clientDetails.ClientRequirement;



public interface ClientRequirementRepository extends JpaRepository<ClientRequirement, Long> {

	@Query(value=" select c.project_id, c.date,c.duration,c.project_name,c.services, cp.client_id,"
			+ "  cp.client_name,cp.email,cp.phone_number  from client_requirements as c"
			+ "  join client_profile as cp on cp.client_id=c.client_id", nativeQuery = true)
			List<Map<String, Object>>findClientRequirementDetails();
	
	
	
	@Query(value="select cr.*,pt.project_type, cp.client_name,s.server_name ,sl.server_type,s.server_id"
			+ " from client_requirements as cr"
			+ " join client_profile as cp on cp.client_id=cr.client_id"
			+ " join server as s on s.server_id=cr.service_id"
			+ " join server_list as sl on sl.fk_server_id=s.server_id"
			+ " join project_type as pt on pt.project_type_id =cr.project_type_id", nativeQuery = true)
			List<Map<String, Object>>findClientRequirementDetails1();
	
	
	@Query(value=" select c.project_name as projectName,c.project_id as projectId "
			+ " from client_requirements as c", nativeQuery = true)
			List<Map<String, Object>>findClientRequirementDetailsAS();
	
//	Optional<ClientRequirement> findByClientId1(Long clientRequirmentId);
	List<ClientRequirement> findByProjectId(Long clientRequirmentId);
	@Query(value="select cr.*,pt.project_type\r\n"
			+ " from client_requirements as cr\r\n"
			+ " join project_type as pt on pt.project_type_id =cr.project_type_id",nativeQuery = true)
	List<Map<String, Object>> getAllRequirementDetails();
	
	List<ClientRequirement> findByStatusTrue();


	@Query(value="select cp.client_id,cp.client_name,cr.project_name,cr.project_id"
			+ "	from client_profile as cp"
			+ " left join client_requirements as cr on cr.client_id=cp.client_id",nativeQuery = true)
	List<Map<String, Object>> getAllRoleByEmployees();
	
	@Query(value="select cr.*,pt.project_type,c.client_name"
			+ " from client_requirements as cr"
			+ " left join project_type as pt on pt.project_type_id =cr.project_type_id"
			+ " left join client_profile as c on c.client_id = cr.client_id"
			+ " ORDER BY cr.project_id DESC;", nativeQuery = true)
	List<Map<String, Object>> getAllDetails();
	
	@Query(value="select cr.*,pt.project_type,c.client_name"
			+ " from client_requirements as cr"
			+ " left join project_type as pt on pt.project_type_id =cr.project_type_id"
			+ " left join client_profile as c on c.client_id = cr.client_id "
			+ " where cr.client_id =:client_id ", nativeQuery = true)
	List<Map<String, Object>> getAllDetailsClient(@Param("client_id") long client_id);


	@Query(value = "select cr.project_id,cr.client_id,cr.date,cr.project_name,cr.features,cr.project_status,cr.project_type_id,cr. rejected,cr.skills_and_description,cr.status,cp.client_name,pt.project_type from client_requirements as cr\r\n"
			+ "join client_profile as cp on cp.client_id=cr.client_id\r\n"
			+ "join project_type as pt on pt.project_type_id=cr.project_type_id\r\n"
			+ "where cr.project_status='approved'", nativeQuery = true)
	List<Map<String, Object>> Allfilterpresentlist();



	@Query(value = "select cr.project_id,cr.client_id,cr.date,cr.project_name,cr.features,cr.project_status,cr.project_type_id,cr. rejected,cr.skills_and_description,cr.status,cp.client_name,pt.project_type from client_requirements as cr\r\n"
			+ "join client_profile as cp on cp.client_id=cr.client_id\r\n"
			+ "join project_type as pt on pt.project_type_id=cr.project_type_id\r\n"
			+ "where MONTHName(cr.date) = :monthName AND YEAR(cr.date) = :year", nativeQuery = true)
	List<Map<String, Object>> getAllattendance(@Param("monthName")String monthName,@Param("year") String year);


	@Query(value = "select cr.project_id,cr.client_id,cr.date,cr.project_name,cr.features,cr.project_status,cr.project_type_id,cr. rejected,cr.skills_and_description,cr.status,cp.client_name,pt.project_type from client_requirements as cr\r\n"
			+ "join client_profile as cp on cp.client_id=cr.client_id\r\n"
			+ "join project_type as pt on pt.project_type_id=cr.project_type_id\r\n"
			+ "where cr.project_status='pending'", nativeQuery = true)
	List<Map<String, Object>> Allfilter3absentlist();


	@Query(value = "select cr.project_id,cr.client_id,cr.date,cr.project_name,cr.features,cr.project_status,cr.project_type_id,cr. rejected,cr.skills_and_description,cr.status,cp.client_name,pt.project_type from client_requirements as cr\r\n"
			+ "join client_profile as cp on cp.client_id=cr.client_id\r\n"
			+ "join project_type as pt on pt.project_type_id=cr.project_type_id\r\n"
			+ "where cr.project_status='rejected'", nativeQuery = true)
	List<Map<String, Object>> Allfilter3absentlist33();

}
