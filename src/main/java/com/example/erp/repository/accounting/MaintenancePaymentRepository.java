package com.example.erp.repository.accounting;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.accounting.MaintenanceInvoice;
import com.example.erp.entity.accounting.MaintenancePayment;

public interface MaintenancePaymentRepository extends JpaRepository<MaintenancePayment, Long> {

	
	
	@Query(value = " select mp.maintenance_payment_id, mp.maintenance_invoice_id,mp.amount,mp.balance, mp.payment_date, mp.payment_type, mp.received_amount,mp.pay_date,"
			+ " mi.client_id,mi.company_id,mi.invoice_date,mi.invoice_no,mi.total,"
			+ " cp.address,cp.city,cp.client_name,cp.country,cp.email,cp.gender,cp.phone_number,cp.mobile_number,cp.state,cp.zip_code"
			+ " from maintenance_payment as mp "
			+ " join  maintenance_invoice as mi on mi.maintenance_invoice_id=mp.maintenance_invoice_id"
			+ " join client_profile as cp on cp.client_id =mi.client_id"
			+ " where mi.client_id = :client_id", nativeQuery = true)
	List<Map<String, Object>> getAllInvoiceDetailsList23(@Param("client_id") Long clientId);
	
	
	@Query(value = " select mp.maintenance_payment_id,mp.amount, mp.maintenance_invoice_id, mp.balance,mp.payment_date, mp.payment_type, mp.received_amount,mp.pay_date,"
			+ " mi.client_id,mi.company_id,mi.invoice_date,mi.invoice_no,mi.total,"
			+ " cp.address,cp.city,cp.client_name,cp.country,cp.email,cp.gender,cp.phone_number,cp.mobile_number,cp.state,cp.zip_code"
			+ " from maintenance_payment as mp "
			+ " join  maintenance_invoice as mi on mi.maintenance_invoice_id=mp.maintenance_invoice_id"
			+ " join client_profile as cp on cp.client_id =mi.client_id", nativeQuery = true)
	List<Map<String, Object>> getAllInvoiceDetailsList();

	@Query(value = " select mp.maintenance_payment_id as maintenancePaymentId, mp.amount, mp.balance,mp.maintenance_invoice_id as maintenanceInvoiceId, mp.payment_date as paymentDate, mp.payment_type as paymentType, mp.received_amount as receivedAmount,mp.pay_date as payDate,"
			+ " mi.client_id as clientId,mi.company_id as companyId,mi.invoice_date as invoiceDate,mi.invoice_no as invoiceNo,mi.total,"
			+ " cp.address,cp.city,cp.client_name as clientName,cp.country,cp.email,cp.gender,cp.phone_number as phoneNumber,cp.mobile_number as mobileNumber,cp.state,cp.zip_code as zipCode"
			+ " from maintenance_payment as mp "
			+ " join  maintenance_invoice as mi on mi.maintenance_invoice_id=mp.maintenance_invoice_id"
			+ " join client_profile as cp on cp.client_id =mi.client_id"
			+ "  WHERE  YEAR(mp.payment_date) = :year and mi.client_id=:client_id", nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndyear(@Param("year") String year,@Param("client_id") long client_id);

	@Query(value = " select mp.maintenance_payment_id as maintenancePaymentId,mp.amount,mp.balance, mp.maintenance_invoice_id as maintenanceInvoiceId, mp.payment_date as paymentDate, mp.payment_type as paymentType, mp.received_amount as receivedAmount,mp.pay_date as payDate,"
			+ " mi.client_id as clientId,mi.company_id as companyId,mi.invoice_date as invoiceDate,mi.invoice_no as invoiceNo,mi.total,"
			+ " cp.address,cp.city,cp.client_name as clientName,cp.country,cp.email,cp.gender,cp.phone_number as phoneNumber,cp.mobile_number as mobileNumber,cp.state,cp.zip_code as zipCode"
			+ " from maintenance_payment as mp "
			+ " join  maintenance_invoice as mi on mi.maintenance_invoice_id=mp.maintenance_invoice_id"
			+ " join client_profile as cp on cp.client_id =mi.client_id"
			+ " WHERE  monthname(mp.payment_date) = :monthName and mi.client_id=:client_id", nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndMonthyear(@Param("monthName") String monthName,@Param("client_id") long client_id);

	@Query(value = " select mp.maintenance_payment_id, mp.maintenance_invoice_id, mp.payment_date, mp.payment_type, mp.received_amount,mp.pay_date,"
			+ " mi.client_id,mi.company_id,mi.invoice_date,mi.invoice_no,mi.total,"
			+ " cp.address,cp.city,cp.client_name,cp.country,cp.email,cp.gender,cp.phone_number,cp.mobile_number,cp.state,cp.zip_code"
			+ " from maintenance_payment as mp "
			+ " join  maintenance_invoice as mi on mi.maintenance_invoice_id=mp.maintenance_invoice_id"
			+ " join client_profile as cp on cp.client_id =mi.client_id"
			+ " WHERE  monthname(mp.payment_date) = :monthName", nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndMonth(@Param("monthName") String monthName);

	@Query(value = " select mp.maintenance_payment_id, mp.maintenance_invoice_id, mp.payment_date, mp.payment_type, mp.received_amount,mp.pay_date,"
			+ " mi.client_id,mi.company_id,mi.invoice_date,mi.invoice_no,mi.total,"
			+ " cp.address,cp.city,cp.client_name,cp.country,cp.email,cp.gender,cp.phone_number,cp.mobile_number,cp.state,cp.zip_code"
			+ " from maintenance_payment as mp "
			+ " join  maintenance_invoice as mi on mi.maintenance_invoice_id=mp.maintenance_invoice_id"
			+ " join client_profile as cp on cp.client_id =mi.client_id"
			+ " WHERE  YEAR(mp.payment_date) = :year", nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAnd(@Param("year") String year);
	
	
	Optional<MaintenancePayment> findByMaintenanceInvoiceId(long id);


	Optional<MaintenancePayment> findByReceivedAmount(long id);

}

