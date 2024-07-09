package com.example.erp.repository.clientDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.clientDetails.Quotation;

public interface QuotationRepository extends JpaRepository<Quotation, Long> {

	@Query(value = "  select q.*,c.client_name,c.phone_number,c.email,p.project_type" + " from quotation as q"
			+ " join client_profile as c on c.client_id=q.client_id"
			+ " join project_type as p on p.project_type_id = q.project_type_id", nativeQuery = true)
	List<Map<String, Object>> getAllQuotation();

	Optional<Quotation> findByClientId(long id);

	@Query(value = "select q.*,ql.additional_notes,ql.terms_and_condition,ql.quotation_list_id,t.amount as quotationAmount,t.description,t.term_list_id,t.item_name,t.quantity,t.rate,"
			+ " cp.address,cp.city,cp.client_name,cp.country,cp.email,cp.gender,cp.phone_number,cp.mobile_number,cp.state,cp.zip_code,c.company_name,c.email as companyEmail,c.phone_number1,c.phone_number2,c.pincode,c.country as companyCountry,"
			+ " c.address as companyAddress,p.project_type from quotation as q"
			+ " join quotation_list as ql on ql.quotation_id = q.quotation_id"
			+ " join terms as t on t.quotation_id = q.quotation_id"
			+ " join  client_profile as cp on cp.client_id = q.client_id"
			+ " join company as c on c.company_id = q.company_id"
			+ " join project_type as p on p.project_type_id = q.project_type_id", nativeQuery = true)
	List<Map<String, Object>> getAllQuotationByClirentDetails();

	@Query(value =
	        "SELECT " +
	            "q.quotation_id, cp.address, cp.city, cp.client_name, cp.country, cp.email, cp.gender,cp.client_id, " +
	            "q.project_status, q.reason, q.total_amount, q.given_date, cp.phone_number, ql.project_id, " +
	            "cr.project_name, rq.url, q.client_status, q.quotation_status, cp.mobile_number, cp.state, " +
	            "cp.zip_code, c.company_name, c.email AS companyEmail, c.phone_number1, c.phone_number2, " +
	            "c.pincode, c.country AS companyCountry, c.address AS companyAddress, p.project_type, " +
	            "ql.additional_notes, ql.terms_and_condition, ql.quotation_list_id, c.company_id, ql.amount, " +
	            "ql.description, ql.quantity, ql.rate " +
	        "FROM " +
	            "quotation AS q " +
	            " LEFT JOIN client_profile AS cp ON cp.client_id = q.client_id " +
	            " LEFT JOIN quotation_list AS ql ON ql.quotation_id = q.quotation_id " +
	            " LEFT JOIN client_requirements AS cr ON cr.project_id = ql.project_id " +
	            " LEFT JOIN research_quotation AS rq ON rq.client_id = q.client_id " +
	            " LEFT JOIN company AS c ON c.company_id = q.company_id " +
	            " LEFT JOIN project_type AS p ON p.project_type_id = cr.project_type_id",
	        nativeQuery = true)
	List<Map<String, Object>> getAllQuotationByClientDetails();

	
	@Query(value =
			"SELECT q.quotation_id, cp.address,cp.city,cp.client_name,cp.country,cp.email,cp.gender,q.project_status,q.reason,q.total_amount,q.given_date,cp.phone_number,ql.project_id,cr.project_name,rq.url,q.client_status,q.quotation_status,"
					+ "			 cp.mobile_number,cp.state,cp.zip_code,c.company_name,c.email AS companyEmail,c.phone_number1,c.phone_number2,c.pincode,c.country AS companyCountry,"
					+ "		   c.address AS companyAddress,p.project_type,ql.additional_notes,ql.terms_and_condition,ql.quotation_list_id,c.company_id,ql.amount,ql.description,ql.quantity,ql.rate "
					+ "			 FROM quotation AS q"
					+ "			LEFT JOIN client_profile AS cp ON cp.client_id = q.client_id"
					+ "			LEFT JOIN quotation_list AS ql ON ql.quotation_id = q.quotation_id"
					+ "			 LEFT JOIN client_requirements AS cr ON cr.project_id = ql.project_id"
					+ "             left join research_quotation as rq on rq.client_id=q.client_id"
					+ "			 LEFT JOIN company AS c ON c.company_id = q.company_id"
					+ "			 LEFT JOIN project_type AS p ON p.project_type_id = cr.project_type_id"
					+ "        where q.approved =true",
	        nativeQuery = true)
	List<Map<String, Object>> getAllQuotationByClientDetailsWithApproval();
	
	
	@Query(value = "SELECT q.quotation_id, cp.address,cp.city,cp.client_name,cp.country,cp.email,cp.gender,q.project_status,q.reason,q.total_amount,q.given_date,cp.phone_number,ql.project_id,cr.project_name,rq.url,q.client_status,q.quotation_status,"
			+ "			 cp.mobile_number,cp.state,cp.zip_code,c.company_name,c.email AS companyEmail,c.phone_number1,c.phone_number2,c.pincode,c.country AS companyCountry,"
			+ "		   c.address AS companyAddress,p.project_type,ql.additional_notes,ql.terms_and_condition,ql.quotation_list_id,c.company_id,ql.amount,ql.description,ql.quantity,ql.rate "
			+ "			 FROM quotation AS q"
			+ "			LEFT JOIN client_profile AS cp ON cp.client_id = q.client_id"
			+ "			LEFT JOIN quotation_list AS ql ON ql.quotation_id = q.quotation_id"
			+ "			 LEFT JOIN client_requirements AS cr ON cr.project_id = ql.project_id"
			+ "             left join research_quotation as rq on rq.client_id=q.client_id"
			+ "			 LEFT JOIN company AS c ON c.company_id = q.company_id"
			+ "			 LEFT JOIN project_type AS p ON p.project_type_id = cr.project_type_id"
			+ "        where q.approved =true"
			+ "         and q.client_id=:client_id ", nativeQuery = true)
	List<Map<String, Object>> getAllByClientDetails(@Param("client_id")Long client_id);

	@Query(value = "select q.client_id,q.client_status,q.given_date,q.project_status,q.quotation_status,q.reason,q.rejected_reason,q.role_name,q.total_amount,\r\n"
			+ " cp.client_name,pt.project_type,cr.project_name\r\n"
			+ "  from quotation as q\r\n"
			+ " join client_profile as cp on cp.client_id=q.client_id\r\n"
			+ " join client_requirements as cr on cr.client_id=cp.client_id"
			+ " join project_type as pt on pt.project_type_id=cr.project_type_id\r\n"
			+ " where MONTHName(q.given_date) = :monthName AND YEAR(q.given_date) = :year", nativeQuery = true)
	List<Map<String, Object>> getAllattendance(@Param("monthName")String monthName,@Param("year") String year);
	
	@Query(value = "select q.client_id,q.client_status,q.given_date,q.project_status,q.quotation_status,q.reason,q.rejected_reason,q.role_name,q.total_amount,\r\n"
			+ "cp.client_name,pt.project_type,cr.project_name\r\n"
			+ "from quotation as q\r\n"
			+ "join client_profile as cp on cp.client_id=q.client_id\r\n"
			+ "join client_requirements as cr on cr.client_id=cp.client_id\r\n"
			+ "join project_type as pt on pt.project_type_id=cr.project_type_id\r\n"
			+ "where q.client_status='approved'", nativeQuery = true)
	List<Map<String, Object>> Allfilterpresentlist();
	@Query(value = "select q.client_id,q.client_status,q.given_date,q.project_status,q.quotation_status,q.reason,q.rejected_reason,q.role_name,q.total_amount,\r\n"
			+ "cp.client_name,pt.project_type,cr.project_name\r\n"
			+ "from quotation as q\r\n"
			+ "join client_profile as cp on cp.client_id=q.client_id\r\n"
			+ "join client_requirements as cr on cr.client_id=cp.client_id\r\n"
			+ "join project_type as pt on pt.project_type_id=cr.project_type_id\r\n"
			+ "where q.client_status='pending'", nativeQuery = true)
	List<Map<String, Object>> Allfilter3absentlist();
	@Query(value = "select q.client_id,q.client_status,q.given_date,q.project_status,q.quotation_status,q.reason,q.rejected_reason,q.role_name,q.total_amount,\r\n"
			+ "cp.client_name,pt.project_type,cr.project_name\r\n"
			+ "from quotation as q\r\n"
			+ "join client_profile as cp on cp.client_id=q.client_id\r\n"
			+ "join client_requirements as cr on cr.client_id=cp.client_id\r\n"
			+ "join project_type as pt on pt.project_type_id=cr.project_type_id\r\n"
			+ "where q.client_status='rejected'", nativeQuery = true)
	List<Map<String, Object>> Allfilter3absentlist33();
	@Query(value = "select q.client_id,q.client_status,q.given_date,q.project_status,q.quotation_status,q.reason,q.rejected_reason,q.role_name,q.total_amount,\r\n"
			+ "cp.client_name,pt.project_type,cr.project_name\r\n"
			+ "from quotation as q\r\n"
			+ "join client_profile as cp on cp.client_id=q.client_id\r\n"
			+ "join client_requirements as cr on cr.client_id=cp.client_id\r\n"
			+ "join project_type as pt on pt.project_type_id=cr.project_type_id\r\n"
			+ "where q.project_status='approved'", nativeQuery = true)
	List<Map<String, Object>> Allfilterpresentlist5();
	@Query(value = "select q.client_id,q.client_status,q.given_date,q.project_status,q.quotation_status,q.reason,q.rejected_reason,q.role_name,q.total_amount,\r\n"
			+ "cp.client_name,pt.project_type,cr.project_name\r\n"
			+ "from quotation as q\r\n"
			+ "join client_profile as cp on cp.client_id=q.client_id\r\n"
			+ "join client_requirements as cr on cr.client_id=cp.client_id\r\n"
			+ "join project_type as pt on pt.project_type_id=cr.project_type_id\r\n"
			+ "where q.project_status='pending'", nativeQuery = true)
	List<Map<String, Object>> Allfilter3absentlist6();
	@Query(value = "select q.client_id,q.client_status,q.given_date,q.project_status,q.quotation_status,q.reason,q.rejected_reason,q.role_name,q.total_amount,\r\n"
			+ "cp.client_name,pt.project_type,cr.project_name\r\n"
			+ "from quotation as q\r\n"
			+ "join client_profile as cp on cp.client_id=q.client_id\r\n"
			+ "join client_requirements as cr on cr.client_id=cp.client_id\r\n"
			+ "join project_type as pt on pt.project_type_id=cr.project_type_id\r\n"
			+ "where q.project_status='rejected'", nativeQuery = true)
	List<Map<String, Object>> Allfilter3absentlist7();
	@Query(value = "select q.client_id,q.client_status,q.given_date,q.project_status,q.quotation_status,q.reason,q.rejected_reason,q.role_name,q.total_amount,\r\n"
			+ "cp.client_name,pt.project_type,cr.project_name\r\n"
			+ "from quotation as q\r\n"
			+ "join client_profile as cp on cp.client_id=q.client_id\r\n"
			+ "join client_requirements as cr on cr.client_id=cp.client_id\r\n"
			+ "join project_type as pt on pt.project_type_id=cr.project_type_id\r\n"
			+ "where q.quotation_status='approved'", nativeQuery = true)
	List<Map<String, Object>> Allfilterpresentlist8();
	@Query(value = "select q.client_id,q.client_status,q.given_date,q.project_status,q.quotation_status,q.reason,q.rejected_reason,q.role_name,q.total_amount,\r\n"
			+ "cp.client_name,pt.project_type,cr.project_name\r\n"
			+ "from quotation as q\r\n"
			+ "join client_profile as cp on cp.client_id=q.client_id\r\n"
			+ "join client_requirements as cr on cr.client_id=cp.client_id\r\n"
			+ "join project_type as pt on pt.project_type_id=cr.project_type_id\r\n"
			+ "where q.quotation_status='pending'", nativeQuery = true)
	List<Map<String, Object>> Allfilter3absentlist9();
	@Query(value = "select q.client_id,q.client_status,q.given_date,q.project_status,q.quotation_status,q.reason,q.rejected_reason,q.role_name,q.total_amount,\r\n"
			+ "cp.client_name,pt.project_type,cr.project_name\r\n"
			+ "from quotation as q\r\n"
			+ "join client_profile as cp on cp.client_id=q.client_id\r\n"
			+ "join client_requirements as cr on cr.client_id=cp.client_id\r\n"
			+ "join project_type as pt on pt.project_type_id=cr.project_type_id\r\n"
			+ "where q.quotation_status='rejected'", nativeQuery = true)
	List<Map<String, Object>> Allfilter3absentlist10();


	
	
	@Query(value = "SELECT q.quotation_id, cp.address,cp.city,cp.client_name,cp.country,cp.email,cp.gender,q.project_status,q.reason,q.total_amount,q.given_date,q.approval,cp.phone_number,ql.project_id,cr.project_name,rq.url,q.client_status,q.quotation_status,"
			+ "			 cp.mobile_number,cp.state,cp.zip_code,c.company_name,c.email AS companyEmail,c.phone_number1,c.phone_number2,c.pincode,c.country AS companyCountry,"
			+ "		   c.address AS companyAddress,p.project_type,ql.additional_notes,ql.terms_and_condition,ql.quotation_list_id,c.company_id,ql.amount,ql.description,ql.quantity,ql.rate "
			+ "			 FROM quotation AS q"
			+ "			LEFT JOIN client_profile AS cp ON cp.client_id = q.client_id"
			+ "			LEFT JOIN quotation_list AS ql ON ql.quotation_id = q.quotation_id"
			+ "			 LEFT JOIN client_requirements AS cr ON cr.project_id = ql.project_id"
			+ "             left join research_quotation as rq on rq.client_id=q.client_id"
			+ "			 LEFT JOIN company AS c ON c.company_id = q.company_id"
			+ "			 LEFT JOIN project_type AS p ON p.project_type_id = cr.project_type_id"
			+ "        where q.approval = true", nativeQuery = true)
	List<Map<String, Object>> getAllByQuoteApproval();

}



