package com.example.erp.repository.clientDetails;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.clientDetails.ClientProfile;

public interface ClientRepository extends JpaRepository<ClientProfile, Long> {

	ClientProfile findByEmail(String email);
	
	
//	Optional<ClientProfile> findByEmail(String email);
	
	@Query(value="select c.client_id,c.client_name,r.role_id,r.role_name,m.member_list_id as memberListId"
			+ " from client_profile as c"
			+ " join role as r on r.role_id = c.role_id"
			+ "  join member_list as m on m.email = c.email"
			+ " where c.client_id = :client_id",nativeQuery = true)
	List<Map<String, Object>> getAllClientDetails(Long client_id);
	
	@Query(value="select c.client_id,c.address,c.city,c.client_name,c.confirm_password,c.password,c.country,c.gender,c.email,c.phone_number,"
			+ " c.referral,c.role_id,c.state,c.mobile_number,c.zip_code,c.status,r.role_name from client_profile as c"
			+ " join role as r on r.role_id = c.role_id"
			+ " where c.status = true",nativeQuery = true)
	List<Map<String, Object>> getAllClientDetailsTrue();
	
	@Query(value="select DISTINCT c.client_id as clientId,c.client_name as clientName,c.address,c.phone_number as phoneNumber,r.role_id as roleId,r.role_name as roleName from client_profile as c"
			+ " join role as r on r.role_id = c.role_id"
			+ " join invoice as i on i.client_id=c.client_id"
			+ " where c.status=true",nativeQuery = true)
	List<Map<String, Object>> getAllClientDetailsInvoice();
	
	
	@Query(value="select c.client_id,c.address,c.city,c.client_name,c.confirm_password,c.password,c.country,c.gender,c.email,c.phone_number,"
			+ " c.referral,c.role_id,c.state,c.mobile_number,c.zip_code,c.status,r.role_name from client_profile as c"
			+ " join role as r on r.role_id = c.role_id"
			+ " where c.status = false",nativeQuery = true)
	List<Map<String, Object>> getAllClientDetailsFalse();

	
	@Query(value=" select"
			+ "    year(curdate()) as currentyear, year(curdate()) - 1 as previousyear,monthname(cp.date) as month,"
			+ "    sum(case when year(curdate()) = year(cp.date) then 1 else 0 end) as currentcount,"
			+ "    sum(case when year(curdate()) - 1 = year(cp.date) then 1 else 0 end) as previouscount"
			+ " from client_profile as cp"
			+ " where year(cp.date) in (year(curdate()), year(curdate()) - 1)"
			+ " group by month "
			+ " order by min(cp.date);",nativeQuery = true)
	List<Map<String, Object>> ALLCountcustomer();
	
	@Query(value="select c.client_id,c.address,c.city,c.status,c.client_name,c.confirm_password,c.password,c.country,c.gender,c.email,c.phone_number,"
			+ " c.referral,c.role_id,c.state,c.mobile_number,c.zip_code,r.role_name from client_profile as c"
			+ " join role as r on r.role_id = c.role_id"
			+ " where c.client_id = :client_id",nativeQuery = true)
	List<Map<String, Object>> getAllEmployeesWithDetailsWithId(@Param("client_id") Long client_id);

	@Query(value=" select a.client_id,a.confirm_password,a.email,a.client_name,a.password,a.role_name,a.status,a.date,a.intime,a.role_id"
			+ " from client_profile  as a"
			+ " join role as r on r.role_id = a.role_id "
			+ " where a.client_id =:id and r.role_id=:role_id", nativeQuery = true)
	List<Map<String, Object>> getAllEmployeesWithDetailsWithIdEmployeeClient(@Param("id") Long admin_id,@Param("role_id") Long role_id);
	
	@Query(value=""
			+ "   SELECT\r\n"
			+ "    t.client_id,\r\n"
			+ "    t.completedProjectCount,\r\n"
			+ "    c.projectCount\r\n"
			+ " FROM\r\n"
			+ "    (SELECT\r\n"
			+ "        client_id,\r\n"
			+ "        SUM(CASE WHEN type_of_project = 'hosting' AND YEAR(hosting_date) = YEAR(CURDATE()) THEN 1 ELSE 0 END) AS completedProjectCount\r\n"
			+ "    FROM\r\n"
			+ "        task\r\n"
			+ "    WHERE\r\n"
			+ "        client_id =:client_id\r\n"
			+ "    GROUP BY\r\n"
			+ "        client_id) AS t\r\n"
			+ " JOIN\r\n"
			+ "    (SELECT\r\n"
			+ "        c.client_id,\r\n"
			+ "        COUNT(c.client_id) AS projectCount\r\n"
			+ "    FROM\r\n"
			+ "        client_requirements AS c\r\n"
			+ "    JOIN\r\n"
			+ "        client_profile AS cp ON cp.client_id = c.client_id\r\n"
			+ "    WHERE\r\n"
			+ "        c.client_id =:client_id\r\n"
			+ "    GROUP BY\r\n"
			+ "        c.client_id) AS c ON t.client_id = c.client_id;"
			+ "", nativeQuery = true)
	Map<String, Object> Allfilter2(@Param("client_id") long client_id);
	
	
	@Query(value=""
			+ " WITH ranked_tasks AS (\r\n"
			+ "  SELECT t.client_id,t.employee_report_id,t.type_of_project,cr.date,t.project_status,cr.durationdate,e.user_id,e.user_name,cr.project_name,cr.project_id,t.task_id,\r\n"
			+ "    ROW_NUMBER() OVER (PARTITION BY t.client_id, t.project_id ORDER BY t.task_id DESC) AS rn\r\n"
			+ "  FROM task AS t\r\n"
			+ "    JOIN client_requirements AS cr ON cr.project_id = t.project_id\r\n"
			+ "    JOIN client_profile AS cp ON cp.client_id = t.client_id\r\n"
			+ "    JOIN employee AS e ON e.employee_id = t.employee_report_id\r\n"
			+ "  WHERE t.type_of_project IN ('research', 'development', 'project', 'testing', 'hosting')\r\n"
			+ " )SELECT  client_id,employee_report_id,type_of_project,date,durationdate,user_id,  user_name,project_name, project_status, project_id,task_id\r\n"
			+ " FROM  ranked_tasks\r\n"
			+ " WHERE rn = 1 and client_id=:client_id ", nativeQuery = true)
		List<Map<String, Object>> GetClientProjectStatus( @Param("client_id")Long client_id);
	
	@Query(value=""
			+ " SELECT r.receipt_id as receiptId,r.balance,r.invoice_id as invoiceId,i.amount,r.payment_date as paymentDate,r.payment_type as paymentType,r.received_amount as receivedAmount,i.client_id\r\n"
			+ "			 FROM receipt AS r\r\n"
			+ "		 JOIN invoice AS i ON i.invoice_id = r.invoice_id\r\n"
			+ "		 WHERE i.client_id=:client_id and MONTH(r.payment_date) = MONTH(CURRENT_DATE) AND YEAR(r.payment_date) = YEAR(CURRENT_DATE)", nativeQuery = true)
		List<Map<String, Object>> GetClientCurrentMonthPayment(@Param("client_id")Long client_id);
	
	
	@Query(value=""
			+ " SELECT \r\n"
			+ "    cr.project_name,\r\n"
			+ "    t.client_id,\r\n"
			+ "    d.department_name,\r\n"
			+ "    td.trainee_id,\r\n"
			+ "    e.employee_id,\r\n"
			+ "    COALESCE(e.user_name, td.user_name) AS user_name,\r\n"
			+ "    COALESCE(e.user_id, td.user_id) AS user_id\r\n"
			+ " FROM \r\n"
			+ "    task AS t\r\n"
			+ " JOIN \r\n"
			+ "    task_list AS tl ON tl.task_id = t.task_id\r\n"
			+ " JOIN \r\n"
			+ "    client_requirements AS cr ON cr.project_id = t.project_id\r\n"
			+ " LEFT JOIN \r\n"
			+ "    employee AS e ON e.employee_id = tl.employee_id\r\n"
			+ " LEFT JOIN \r\n"
			+ "    training_details AS td ON td.trainee_id = tl.trainee_id\r\n"
			+ " JOIN \r\n"
			+ "    department AS d ON d.department_id = tl.department_id\r\n"
			+ " WHERE \r\n"
			+ "    t.client_id = :client_id"
			+ " GROUP BY \r\n"
			+ "    cr.project_name, t.client_id, d.department_name,  td.trainee_id,e.user_name, e.user_id, e.employee_id, tl.trainee_id, td.user_name, td.user_id;\r\n"
			+ "", nativeQuery = true)
	List<Map<String, Object>> GetClientProjectWorkingMember(@Param("client_id")Long client_id);
	
	@Query(value=""
			+ " SELECT mi.maintenance_invoice_id,mi.client_id,mi.company_id,mi.invoice_date,mi.invoice_no, mi.maintenance_terms_id,"
			+ " mt.terms,ml.maintenance_list_id,ml.description,ml.price,ml.project_id,ml.sub_total,mi.total,cr.project_name"
			+ " FROM maintenance_invoice AS mi"
			+ " JOIN maintenance_list AS ml ON ml.maintenance_invoice_id = mi.maintenance_invoice_id"
			+ " JOIN maintenance_terms AS mt ON mt.maintenance_terms_id = mi.maintenance_terms_id"
			+ " jOIN client_requirements AS cr ON cr.project_id = ml.project_id"
			+ " JOIN client_profile AS cp ON cp.client_id = mi.client_id"
			+ " JOIN company AS c ON c.company_id = mi.company_id"
			+ " WHERE MONTH( mi.invoice_date) = MONTH(CURRENT_DATE) AND YEAR( mi.invoice_date) = YEAR(CURRENT_DATE) and  mi.client_id=:client_id", nativeQuery = true)
	List<Map<String, Object>> GetClientMaintenance(@Param("client_id")Long client_id);

	@Query(value=""
			+ " select q.client_id,q.client_status,q.given_date,q.project_status,q.quotation_status,q.reason,q.rejected_reason,q.role_name,q.total_amount,\r\n"
			+ "cp.client_name,cr.project_name\r\n"
			+ "FROM quotation AS q\r\n"
			+ "join quotation_list as ql on ql.quotation_id=q.quotation_id\r\n"
			+ "join client_requirements as cr on cr.project_id =ql.project_id\r\n"
			+ "join client_profile as cp on cp.client_id=q.client_id\r\n"
			+ "WHERE q.quotation_status = 'approved' AND q.client_status = 'pending' AND q.client_id =:client_id", nativeQuery = true)
	List<Map<String, Object>> GetClientProjectWorkingMemberQuotaion(@Param("client_id")Long client_id);

	ClientProfile findByMobileNumber(String mobileNumber);

	Optional<ClientProfile> findByMobileNumberAndEmail(String mobileNumber, String email);
}
