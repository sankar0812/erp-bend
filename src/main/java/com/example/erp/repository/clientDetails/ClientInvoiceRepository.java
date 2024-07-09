package com.example.erp.repository.clientDetails;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.clientDetails.ClientInvoice;
import com.example.erp.entity.employee.EmployeeAttendance;

public interface ClientInvoiceRepository extends JpaRepository<ClientInvoice, Long> {

	@Query(value = "select i.*,il.invoice_list_id,il.project_id,il.amount as listAmount,il.discount_percentage,il.gst,il.price,il.quantity,il.tax_include_price,il.tax_quantity_amount,"
			+ " il.total_tax_amount,c.email,c.client_name,c.phone_number" + " from invoice as i"
			+ "	join invoice_list as il on il.invoice_id = i.invoice_id"
			+ "	join client_profile as c on c.client_id=i.client_id", nativeQuery = true)
	List<Map<String, Object>> getAllInvoiceDetailsDemo();

	@Query(value = "select i.*,il.invoice_list_id,il.amount as listAmount,il.discount_amount,il.discount_percentage,il.gst,cp.client_name,cp.email,cp.gender,cp.phone_number,cp.state,"
			+ " cp.address,cp.zip_code,il.price,il.project_id,il.quantity,il.tax_include_price,il.tax_quantity_amount,il.total_tax_amount,c.company_name,"
			+ " c.account_no,c.address as companyAddress,c.bank_name,c.email as companyEmail,c.gst_no,c.holder_name,c.ifsc_code,c.location,c.phone_number1,"
			+ " c.phone_number2,c.pincode,c.tax_no,c.country,cr.project_name,i.status as invoiceStatus"
			+ " from invoice as i"
			+ " join invoice_list as il on il.invoice_id = i.invoice_id"
			+ " join client_requirements as cr on cr.project_id = il.project_id"
			+ " join company as c on c.company_id = i.company_id"
			+ " join client_profile as cp on cp.client_id = cr.client_id"
			+ " order by i.invoice_id DESC", nativeQuery = true)
	List<Map<String, Object>> getAllInvoiceDetailsList();

	@Query(value = "select i.*,il.invoice_list_id,il.amount as listAmount,il.discount_amount,il.discount_percentage,il.gst,cp.client_name,cp.email,cp.gender,cp.phone_number,cp.state,"
			+ " cp.address,cp.zip_code,il.price,il.project_id,il.quantity,il.tax_include_price,il.tax_quantity_amount,il.total_tax_amount,c.company_name,"
			+ " c.account_no,c.address as companyAddress,c.bank_name,c.email as companyEmail,c.gst_no,c.holder_name,c.ifsc_code,c.location,c.phone_number1,"
			+ " c.phone_number2,c.pincode,c.tax_no,c.country,cr.project_name,i.status as invoiceStatus"
			+ " from invoice as i"
			+ " join invoice_list as il on il.invoice_id = i.invoice_id"
			+ " join client_requirements as cr on cr.project_id = il.project_id"
			+ " join company as c on c.company_id = i.company_id"
			+ " join client_profile as cp on cp.client_id = cr.client_id"
			+ " where i.invoice_date between :startDate and :endDate", nativeQuery = true)
	List<Map<String, Object>> getAllInvoiceDetailsListWithDate(@Param("startDate") LocalDate startDate,
			@Param("endDate") LocalDate endDate);
	

	@Query(value = "select i.*,il.invoice_list_id,il.amount as listAmount,il.discount_amount,il.discount_percentage,il.gst,cp.client_name,cp.email,cp.gender,cp.phone_number,cp.state,"
			+ " cp.address,cp.zip_code,il.price,il.project_id,il.quantity,il.tax_include_price,il.tax_quantity_amount,il.total_tax_amount,c.company_name,"
			+ " c.account_no,c.address as companyAddress,c.bank_name,c.email as companyEmail,c.gst_no,c.holder_name,c.state as companyState,c.ifsc_code,c.location,c.phone_number1,"
			+ " c.phone_number2,c.pincode,c.tax_no,c.country,cr.project_name from invoice as i"
			+ " join invoice_list as il on il.invoice_id = i.invoice_id"
			+ " join client_requirements as cr on cr.project_id = il.project_id"
			+ " join company as c on c.company_id = i.company_id"
			+ " join client_profile as cp on cp.client_id = cr.client_id"
			+ " where i.client_id = :client_id"
			+ " order by i.invoice_id", nativeQuery = true)
	List<Map<String, Object>> getAllInvoiceDetailsList23(@Param("client_id") Long clientId);

	@Query(value = "select i.*,il.invoice_list_id,il.project_id,il.amount as listAmount,"
			+ "	c.email,c.client_name,c.phone_number "
			+ " from invoice as i"
			+ "	join invoice_list as il on il.invoice_id = i.invoice_id"
			+ "	join client_profile as c on c.client_id=i.client_id"
			+ " where DATE(i.invoice_date) = CURRENT_DATE", nativeQuery = true)
	List<Map<String, Object>> getAllInvoiceDetailsListuuuu();
	
	
	@Query(value = "select i.*,il.invoice_list_id,il.project_id,il.amount as listAmount,"
			+ "		c.email,c.client_name,c.phone_number "
			+ "         from invoice as i"
			+ "				join invoice_list as il on il.invoice_id = i.invoice_id"
			+ "			join client_profile as c on c.client_id=i.client_id"
			+ "             where DATE(i.invoice_date) = CURRENT_DATE"
			+ " and c.client_id=:client_id", nativeQuery = true)
	List<Map<String, Object>> getAllInvoiceDetailsListClient(@Param("client_id") Long clientId);
	
	@Query(value = "\r\n"
			+ "WITH all_months AS (\r\n"
			+ "    SELECT 'Jan' AS month, 1 AS month_order\r\n"
			+ "    UNION SELECT 'Feb' AS month, 2 AS month_order\r\n"
			+ "    UNION SELECT 'Mar' AS month, 3 AS month_order\r\n"
			+ "    UNION SELECT 'Apr' AS month, 4 AS month_order\r\n"
			+ "    UNION SELECT 'May' AS month, 5 AS month_order\r\n"
			+ "    UNION SELECT 'Jun' AS month, 6 AS month_order\r\n"
			+ "    UNION SELECT 'Jul' AS month, 7 AS month_order\r\n"
			+ "    UNION SELECT 'Aug' AS month, 8 AS month_order\r\n"
			+ "    UNION SELECT 'Sep' AS month, 9 AS month_order\r\n"
			+ "    UNION SELECT 'Oct' AS month, 10 AS month_order\r\n"
			+ "    UNION SELECT 'Nov' AS month, 11 AS month_order\r\n"
			+ "    UNION SELECT 'Dec' AS month, 12 AS month_order\r\n"
			+ " )"
			+ "SELECT\r\n"
			+ " YEAR(CURDATE()) AS currentyear,YEAR(CURDATE()) - 1 AS previousyear, am.month,\r\n"
			+ "    IFNULL(SUM(CASE WHEN YEAR(cp.invoice_date) = YEAR(CURDATE()) THEN 1 ELSE 0 END), 0) AS currentcount,\r\n"
			+ "    IFNULL(SUM(CASE WHEN YEAR(cp.invoice_date) = YEAR(CURDATE()) - 1 THEN 1 ELSE 0 END), 0) AS previouscount\r\n"
			+ "FROM all_months am\r\n"
			+ "LEFT JOIN invoice AS cp ON am.month = SUBSTRING(MONTHNAME(cp.invoice_date), 1, 3)\r\n"
			+ "WHERE YEAR(cp.invoice_date) IN (YEAR(CURDATE()), YEAR(CURDATE()) - 1) OR cp.invoice_date IS NULL\r\n"
			+ "GROUP BY am.month, am.month_order\r\n"
			+ "ORDER BY am.month_order;", nativeQuery = true)
	List<Map<String, Object>> ALLCountcustomer();
	
	
	@Query(value = "select i.invoice_id as invoiceId ,i.amount,i.balance,i.balance_amount as balanceAmount,i.client_id as clientId,i.gst_type as gstType,i.invoice_date as invoiceDate,i.payment_type as paymentType,i.received,i.tax_amount as taxAmount,"
			+ "			li.invoice_list_id as invoiceListId,li.amount as listAmount,li.discount_amount as discountAmount,li.gst,li.project_id as projectId,li.quantity,li.tax_include_price taxIncludePrice,li.total_tax_amount as totalTaxAmount,c.client_name as clientName,r.project_name as projectName"
			+ "			 from invoice as i  "
			+ " join invoice_list as li on li.invoice_id=i.invoice_id"
			+ " join client_profile as c on c.client_id=i.client_id"
			+ " join client_requirements as r on r.project_id=li.project_id"
			+ "	where year(i.invoice_date)= :year", nativeQuery = true)
	List<Map<String, Object>> getAllQuotationByInvoice(@Param("year") String year);
	
	
	
	@Query(value = "select i.invoice_id as invoiceId ,i.amount,i.balance,i.balance_amount as balanceAmount,i.client_id as clientId,i.gst_type as gstType,i.invoice_date as invoiceDate,i.payment_type as paymentType,i.received,i.tax_amount as taxAmount,"
			+ "			li.invoice_list_id as invoiceListId,li.amount as listAmount,li.discount_amount as discountAmount,li.gst,li.project_id as projectId,li.quantity,li.tax_include_price taxIncludePrice,li.total_tax_amount as totalTaxAmount,c.client_name as clientName,r.project_name as projectName"
			+ "			 from invoice as i  "
			+ " join invoice_list as li on li.invoice_id=i.invoice_id"
			+ " join client_profile as c on c.client_id=i.client_id"
			+ " join client_requirements as r on r.project_id=li.project_id"
			+ "	    where i.gst_type = 'withTax'", nativeQuery = true)
	List<Map<String, Object>> getAllQuotationByInvoiceGST();
	
	@Query(value = "select i.invoice_id as invoiceId ,i.amount,i.balance,i.balance_amount as balanceAmount,i.client_id as clientId,i.gst_type as gstType,i.invoice_date as invoiceDate,i.payment_type as paymentType,i.received,i.tax_amount as taxAmount,"
			+ "			li.invoice_list_id as invoiceListId,li.amount as listAmount,li.discount_amount as discountAmount,li.gst,li.project_id as projectId,li.quantity,li.tax_include_price taxIncludePrice,li.total_tax_amount as totalTaxAmount,c.client_name as clientName,r.project_name as projectName"
			+ "			 from invoice as i  "
			+ " join invoice_list as li on li.invoice_id=i.invoice_id"
			+ " join client_profile as c on c.client_id=i.client_id"
			+ " join client_requirements as r on r.project_id=li.project_id"
			+ "	    where i.gst_type = 'withoutTax'", nativeQuery = true)
	List<Map<String, Object>> getAllQuotationByInvoiceGSTwithOut();
	
	@Query(value = "select i.invoice_id as invoiceId ,i.amount,i.balance,i.balance_amount as balanceAmount,i.client_id as clientId,i.gst_type as gstType,i.invoice_date as invoiceDate,i.payment_type as paymentType,i.received,i.tax_amount as taxAmount,"
			+ "			li.invoice_list_id as invoiceListId,li.amount as listAmount,li.discount_amount as discountAmount,li.gst,li.project_id as projectId,li.quantity,li.tax_include_price taxIncludePrice,li.total_tax_amount as totalTaxAmount,c.client_name as clientName,r.project_name as projectName"
			+ "			 from invoice as i  "
			+ " join invoice_list as li on li.invoice_id=i.invoice_id"
			+ " join client_profile as c on c.client_id=i.client_id"
			+ " join client_requirements as r on r.project_id=li.project_id"
			+ " where monthname(i.invoice_date) = :monthName", nativeQuery = true)
	List<Map<String, Object>> getAllleaveInvoice(@Param("monthName")String monthName);
	
	
	@Query(value = "select i.invoice_id as invoiceId ,i.amount,i.balance,i.balance_amount as balanceAmount,i.client_id as clientId,i.gst_type as gstType,i.invoice_date as invoiceDate,i.payment_type as paymentType,i.received,i.tax_amount as taxAmount,"
			+ "			li.invoice_list_id as invoiceListId,li.amount as listAmount,li.discount_amount as discountAmount,li.gst,li.project_id as projectId,li.quantity,li.tax_include_price taxIncludePrice,li.total_tax_amount as totalTaxAmount,c.client_name as clientName,r.project_name as projectName"
			+ "			 from invoice as i  "
			+ " join invoice_list as li on li.invoice_id=i.invoice_id"
			+ " join client_profile as c on c.client_id=i.client_id"
			+ " join client_requirements as r on r.project_id=li.project_id"
			+ " where monthname(i.invoice_date) = :monthName and i.client_id=:client_id", nativeQuery = true)
	List<Map<String, Object>> getAllinvoicemonthname(@Param("monthName")String monthName,@Param("client_id")long client_id);

	
	@Query(value = "select i.invoice_id as invoiceId ,i.amount,i.balance,i.balance_amount as balanceAmount,i.client_id as clientId,i.gst_type as gstType,i.invoice_date as invoiceDate,i.payment_type as paymentType,i.received,i.tax_amount as taxAmount,"
			+ "			li.invoice_list_id as invoiceListId,li.amount as listAmount,li.discount_amount as discountAmount,li.gst,li.project_id as projectId,li.quantity,li.tax_include_price taxIncludePrice,li.total_tax_amount as totalTaxAmount,c.client_name as clientName,r.project_name as projectName"
			+ "			 from invoice as i  "
			+ " join invoice_list as li on li.invoice_id=i.invoice_id"
			+ " join client_profile as c on c.client_id=i.client_id"
			+ " join client_requirements as r on r.project_id=li.project_id"
			+ "	where year(i.invoice_date)= :year and i.client_id=:client_id", nativeQuery = true)
	List<Map<String, Object>> getAllinvoiceYear(@Param("year")String year,@Param("client_id")long client_id);

//	Optional<ClientInvoice> findInvoiceById(Long invoiceId);

	Optional<ClientInvoice> findByInvoiceId(Long invoiceId);

	



}
