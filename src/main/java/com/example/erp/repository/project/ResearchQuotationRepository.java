package com.example.erp.repository.project;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.project.ResearchQuotation;

public interface ResearchQuotationRepository extends JpaRepository<ResearchQuotation, Long>{
	
	
	@Query(value = " "
			+ " select r.research_quotation_id as researchQuotationId ,r.accepted,r.date,r.user_id as userId,r.project_id as projectId,r.project_status as projectStatus,r.rejected,"
			+ " e.manager_id as managerId,e.username as userName,cr.project_name as projectName,r.url as fileUpload"
			+ "		 from  research_quotation as r"
			+ "			 join client_requirements as cr on cr.project_id=r.project_id"
			+ "			 left join user as e on e.user_id=r.user_id;",nativeQuery = true)
	List<Map<String, Object>> getAllProjectresearchQuotation();

}
