package com.example.erp.repository.accounting;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.accounting.MaintenanceInvoice;
import com.example.erp.entity.message.MemberList;

public interface MaintenanceInvoiceRepository extends JpaRepository<MaintenanceInvoice, Long> {
	
	@Query(value = "select mi.maintenance_invoice_id,ml.tax,ml.tax_amount,mi.client_id,mi.company_id,mi.invoice_date,mi.invoice_no,mi.maintenance_terms_id,mt.terms,mt.title,"
			+ "ml.maintenance_list_id,ml.description,ml.price,ml.project_id,ml.sub_total,mi.total,cr.project_name,cp.client_name,cp.address ,mi.status,"
			+ "cp.city ,cp.country ,cp. email,cp.mobile_number ,cp. phone_number,cp.state,cp.zip_code,c.account_no,c.address as companyAddress,c.bank_name,"
			+ "c.branch_name,c.company_name,c.country as companyCountry,c.email as companyEmail,c.gst_no,c.holder_name,c.ifsc_code,c.location,c.phone_number1,c.phone_number2,c.pincode,c.tax_no,c.url"
			+ " from maintenance_invoice as mi"
			+ " join maintenance_list as ml on ml.maintenance_invoice_id=mi.maintenance_invoice_id"
			+ " join maintenance_terms as mt on mt.maintenance_terms_id=mi.maintenance_terms_id"
			+ " join client_requirements as cr on cr.project_id=ml.project_id"
			+ " join client_profile as cp on cp.client_id=mi.client_id"
			+ " join company as c on c.company_id=mi.company_id", nativeQuery = true)
	List<Map<String, Object>> getAllServerDetails();

	@Query(value = "select mi.maintenance_invoice_id,mi.client_id,mi.company_id,mi.invoice_date,mi.invoice_no,mi.maintenance_terms_id,mt.terms,"
			+ " ml.maintenance_list_id,ml.description,ml.price,ml.project_id,ml.sub_total,mi.total,cr.project_name,cp.client_name,cp.address ,"
			+ " cp.city ,cp.country ,cp. email,cp.mobile_number ,cp. phone_number,cp.state,cp.zip_code,c.account_no,c.address as companyAddress,c.bank_name,"
			+ " c.branch_name,c.company_name,c.country as companyCountry,c.email as companyEmail,c.gst_no,c.holder_name,c.ifsc_code,c.location,c.phone_number1,c.phone_number2,c.pincode,c.tax_no,c.url"
			+ " from maintenance_invoice as mi"
			+ " join maintenance_list as ml on ml.maintenance_invoice_id=mi.maintenance_invoice_id"
			+ " join maintenance_terms as mt on mt.maintenance_terms_id=mi.maintenance_terms_id"
			+ " join client_requirements as cr on cr.project_id=ml.project_id"
			+ " join client_profile as cp on cp.client_id=mi.client_id"
			+ " join company as c on c.company_id=mi.company_id"
			+ " where DATE(mi.invoice_date) = CURRENT_DATE;", nativeQuery = true)
	List<Map<String, Object>> getAllInvoiceDetailsListuuuu();

	

	@Query(value = "select mi.maintenance_invoice_id,ml.tax,ml.tax_amount,mi.client_id,mi.company_id,mi.invoice_date,mi.invoice_no,mi.maintenance_terms_id,mt.terms,mt.title,"
			+ "ml.maintenance_list_id,ml.description,ml.price,ml.project_id,ml.sub_total,mi.total,cr.project_name,cp.client_name,cp.address ,mi.status,"
			+ "cp.city ,cp.country ,cp. email,cp.mobile_number ,cp. phone_number,cp.state,cp.zip_code,c.account_no,c.address as companyAddress,c.bank_name,"
			+ "c.branch_name,c.company_name,c.country as companyCountry,c.email as companyEmail,c.gst_no,c.holder_name,c.ifsc_code,c.location,c.phone_number1,c.phone_number2,c.pincode,c.tax_no,c.url"
			+ " from maintenance_invoice as mi"
			+ " join maintenance_list as ml on ml.maintenance_invoice_id=mi.maintenance_invoice_id"
			+ " join maintenance_terms as mt on mt.maintenance_terms_id=mi.maintenance_terms_id"
			+ " join client_requirements as cr on cr.project_id=ml.project_id"
			+ " join client_profile as cp on cp.client_id=mi.client_id"
			+ " join company as c on c.company_id=mi.company_id"
			+ "  where mi.invoice_date between :startDate and :endDate", nativeQuery = true)
		List<Map<String, Object>> getAllpromotionsBetweenDatesList(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

	
	
	@Query(value = ""
			+ " select mi.maintenance_invoice_id as maintenanceInvoiceId,mi.client_id as clientId,mi.company_id as companyid,mi.invoice_date as invoiceDate,mi.invoice_no as invoiceNo,mi.maintenance_terms_id as maintenanceTermsId,mt.terms,"
			+ "	ml.maintenance_list_id as maintenanceListId,ml.description,ml.price,ml.project_id as projectId,ml.sub_total as subTotal,mi.total,cr.project_name as projectName,cp.client_name as clientName,cp.address ,"
			+ "	cp.city ,cp.country ,cp. email,cp.mobile_number as mobileNumber ,cp. phone_number as phoneNumber,cp.state,cp.zip_code as zipCode,c.account_no as accountNo,c.address as companyAddress,c.bank_name as bankName,"
			+ "	c.branch_name as branchName,c.company_name as companyName,c.country as companyCountry,c.email as companyEmail,c.gst_no as gstNo,c.holder_name as holderName,c.ifsc_code as ifscCode,c.location,c.phone_number1 as phoneNumber1,c.phone_number2 as phoneNumber2,c.pincode,c.tax_no as taxNo,c.url"
			+ "	 from maintenance_invoice as mi"
			+ " join maintenance_list as ml on ml.maintenance_invoice_id=mi.maintenance_invoice_id"
			+ " join maintenance_terms as mt on mt.maintenance_terms_id=mi.maintenance_terms_id"
			+ " join client_requirements as cr on cr.project_id=ml.project_id"
			+ " join client_profile as cp on cp.client_id=mi.client_id"
			+ " join company as c on c.company_id=mi.company_id"
			   + " where mi.invoice_date between :startDate and :endDate"
			   , nativeQuery = true)
		List<Map<String, Object>> getAllpromotionsBetweenDates(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);


	
	@Query(value = ""
			+ " select mi.maintenance_invoice_id as maintenanceInvoiceId,mi.client_id as clientId,mi.company_id as companyid,mi.invoice_date as invoiceDate,mi.invoice_no as invoiceNo,mi.maintenance_terms_id as maintenanceTermsId,mt.terms,"
			+ "	ml.maintenance_list_id as maintenanceListId,ml.description,ml.price,ml.project_id as projectId,ml.sub_total as subTotal,mi.total,cr.project_name as projectName,cp.client_name as clientName,cp.address ,"
			+ "	cp.city ,cp.country ,cp. email,cp.mobile_number as mobileNumber ,cp. phone_number as phoneNumber,cp.state,cp.zip_code as zipCode,c.account_no as accountNo,c.address as companyAddress,c.bank_name as bankName,"
			+ "	c.branch_name as branchName,c.company_name as companyName,c.country as companyCountry,c.email as companyEmail,c.gst_no as gstNo,c.holder_name as holderName,c.ifsc_code as ifscCode,c.location,c.phone_number1 as phoneNumber1,c.phone_number2 as phoneNumber2,c.pincode,c.tax_no as taxNo,c.url"
			+ "	 from maintenance_invoice as mi"
			+ " join maintenance_list as ml on ml.maintenance_invoice_id=mi.maintenance_invoice_id"
			+ " join maintenance_terms as mt on mt.maintenance_terms_id=mi.maintenance_terms_id"
			+ " join client_requirements as cr on cr.project_id=ml.project_id"
			+ " join client_profile as cp on cp.client_id=mi.client_id"
			+ " join company as c on c.company_id=mi.company_id"
			+ " WHERE  YEAR(mi.invoice_date) = :year  "
			+ "  ;",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAnd(@Param("year") String year);
	
	
	@Query(value = ""
			+ " select mi.maintenance_invoice_id as maintenanceInvoiceId,mi.client_id as clientId,mi.company_id as companyid,mi.invoice_date as invoiceDate,mi.invoice_no as invoiceNo,mi.maintenance_terms_id as maintenanceTermsId,mt.terms,"
			+ "	ml.maintenance_list_id as maintenanceListId,ml.description,ml.price,ml.project_id as projectId,ml.sub_total as subTotal,mi.total,cr.project_name as projectName,cp.client_name as clientName,cp.address ,"
			+ "	cp.city ,cp.country ,cp. email,cp.mobile_number as mobileNumber ,cp. phone_number as phoneNumber,cp.state,cp.zip_code as zipCode,c.account_no as accountNo,c.address as companyAddress,c.bank_name as bankName,"
			+ "	c.branch_name as branchName,c.company_name as companyName,c.country as companyCountry,c.email as companyEmail,c.gst_no as gstNo,c.holder_name as holderName,c.ifsc_code as ifscCode,c.location,c.phone_number1 as phoneNumber1,c.phone_number2 as phoneNumber2,c.pincode,c.tax_no as taxNo,c.url"
			+ "	 from maintenance_invoice as mi"
			+ " join maintenance_list as ml on ml.maintenance_invoice_id=mi.maintenance_invoice_id"
			+ " join maintenance_terms as mt on mt.maintenance_terms_id=mi.maintenance_terms_id"
			+ " join client_requirements as cr on cr.project_id=ml.project_id"
			+ " join client_profile as cp on cp.client_id=mi.client_id"
			+ " join company as c on c.company_id=mi.company_id"
			+ " WHERE  monthname(mi.invoice_date) = :monthName "
			,nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndMonth(@Param("monthName") String monthName);
	
	
	@Query(value = ""
			+ " select mi.maintenance_invoice_id as maintenanceInvoiceId,mi.client_id as clientId,mi.company_id as companyid,mi.invoice_date as invoiceDate,mi.invoice_no as invoiceNo,mi.maintenance_terms_id as maintenanceTermsId,mt.terms,"
			+ "	ml.maintenance_list_id as maintenanceListId,ml.description,ml.price,ml.project_id as projectId,ml.sub_total as subTotal,mi.total,cr.project_name as projectName,cp.client_name as clientName,cp.address ,"
			+ "	cp.city ,cp.country ,cp. email,cp.mobile_number as mobileNumber ,cp. phone_number as phoneNumber,cp.state,cp.zip_code as zipCode,c.account_no as accountNo,c.address as companyAddress,c.bank_name as bankName,"
			+ "	c.branch_name as branchName,c.company_name as companyName,c.country as companyCountry,c.email as companyEmail,c.gst_no as gstNo,c.holder_name as holderName,c.ifsc_code as ifscCode,c.location,c.phone_number1 as phoneNumber1,c.phone_number2 as phoneNumber2,c.pincode,c.tax_no as taxNo,c.url"
			+ "	 from maintenance_invoice as mi"
	        + " JOIN maintenance_list AS ml ON ml.maintenance_invoice_id = mi.maintenance_invoice_id "
	        + " JOIN maintenance_terms AS mt ON mt.maintenance_terms_id = mi.maintenance_terms_id "
	        + " JOIN client_requirements AS cr ON cr.project_id = ml.project_id "
	        + " JOIN client_profile AS cp ON cp.client_id = mi.client_id "
	        + " JOIN company AS c ON c.company_id = mi.company_id "
	        + " WHERE mi.invoice_date >= DATE_SUB(NOW(), INTERVAL 6 MONTH)", nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndSixMonths();

	


}
