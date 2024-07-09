package com.example.erp.repository.clientDetails;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.erp.entity.clientDetails.ClientInvoice;
import com.example.erp.entity.clientDetails.Receipts;

public interface ReceiptRepository extends JpaRepository<Receipts, Long> {

	@Query(value="select r.*,i.balance as invoiceBalance,c.company_name,c.address,c.state,c.country,c.pincode,cp.client_name,cp.client_id"
			+ " from receipt as r"
			+ " join invoice as i on i.invoice_id = r.invoice_id"
			+ " join client_profile as cp on cp.client_id = i.client_id"
			+ " join company as c on c.company_id = i.company_id",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsByInvoice();
	
	
	@Query(value="select r.*,i.balance,c.company_name,c.address,c.state,c.country,c.pincode,cp.client_name,cp.client_id from receipt as r"
			+ " join invoice as i on i.invoice_id = r.invoice_id"
			+ " join client_profile as cp on cp.client_id = i.client_id"
			+ " join company as c on c.company_id = i.company_id"
			+ " where cp.client_id =:client_id",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsByClientId(Long client_id);
	
	@Query(value="select r.receipt_id,r.balance,r.invoice_id,r.payment_date,i.amount,r.payment_type,r.received_amount,i.client_id,i.company_id,"
			+ " cp.address,cp.city,cp.client_name,cp.country,cp.email,cp.gender,cp.phone_number,cp.mobile_number,cp.state,cp.zip_code,"
			+ " c.company_name,c.email as companyEmail,c.phone_number1,c.phone_number2,c.pincode,c.country as companyCountry,"
			+ " c.address as companyAddress from receipt as r"
			+ " join invoice as i on i.invoice_id = r.invoice_id"
			+ " join  client_profile as cp on cp.client_id = i.client_id"
			+ " join company as c on c.company_id = i.company_id"
			+ " where not exists (select 1"
			+ " from receipt as r2"
			+ " where r2.invoice_id = r.invoice_id and r2.balance = 0)",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetails();
	
	@Query(value="select r.receipt_id,r.balance,r.invoice_id,i.amount,r.payment_date,r.payment_type,r.received_amount,i.client_id,i.company_id,"
			+ " cp.address,cp.city,cp.client_name,cp.country,cp.email,cp.gender,cp.phone_number,cp.mobile_number,cp.state,cp.zip_code,"
			+ " c.company_name,c.email as companyEmail,c.state as companyState,c.location as companyLocation, c.phone_number1,c.phone_number2,c.pincode,c.country as companyCountry,"
			+ " c.address as companyAddress from receipt as r"
			+ " join invoice as i on i.invoice_id = r.invoice_id"
			+ " join  client_profile as cp on cp.client_id = i.client_id"
			+ " join company as c on c.company_id = i.company_id"
			+ " where not exists (select 1"
			+ " from receipt as r2"
			+ " where r2.invoice_id = r.invoice_id and r2.balance = 0)"
			+ " and cp.client_id = :client_id",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientId(Long client_id);
	
	@Query(value="select r.receipt_id,r.balance,r.invoice_id,r.payment_date,i.amount,r.payment_type,r.received_amount,i.client_id,i.company_id,"
			+ " cp.address,cp.city,cp.client_name,cp.country,cp.email,cp.gender,cp.phone_number,cp.mobile_number,cp.state,cp.zip_code,"
			+ " c.company_name,c.email as companyEmail,c.phone_number1,c.phone_number2,c.pincode,c.country as companyCountry,"
			+ " c.address as companyAddress from receipt as r"
			+ " join invoice as i on i.invoice_id = r.invoice_id"
			+ " join  client_profile as cp on cp.client_id = i.client_id"
			+ " join company as c on c.company_id = i.company_id",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClient();
	
	@Query(value="select r.receipt_id,r.balance,r.invoice_id,i.amount,r.payment_date,r.payment_type,r.received_amount,i.client_id,i.company_id,"
			+ " cp.address,cp.city,cp.client_name,cp.country,cp.email,cp.gender,cp.phone_number,cp.mobile_number,cp.state,cp.zip_code,"
			+ " c.company_name,c.email as companyEmail,c.phone_number1,c.state as companyState,c.location as companyLocation,c.phone_number2,c.pincode,c.country as companyCountry,"
			+ " c.address as companyAddress from receipt as r"
			+ " join invoice as i on i.invoice_id = r.invoice_id"
			+ " join  client_profile as cp on cp.client_id = i.client_id"
			+ " join company as c on c.company_id = i.company_id"
			+ " where i.client_id = :client_id",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndName(Long client_id);
	
	@Query(value=" SELECT r.receipt_id,r.balance,r.invoice_id,i.amount,r.payment_date,r.payment_type,"
			+ " r.received_amount,i.client_id,cp.client_name"
			+ " FROM receipt AS r"
			+ " JOIN invoice AS i ON i.invoice_id = r.invoice_id"
			+ " JOIN client_profile AS cp ON cp.client_id = i.client_id"
			+ "  where DATE(r.payment_date) = CURRENT_DATE;"
			+ "",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndNamerrr();

	
	@Query(value=" SELECT "
			+ "    r.receipt_id as receiptId, r.balance as receiptBalance, r.invoice_id as invoiceId, i.amount, r.payment_date as paymentDate, r.payment_type as paymentType, r.received_amount as receivedAmount,"
			+ " i.client_id as clientId, i.balance as invoiceBalance,  i.company_id as companyId,cp.client_name as clientName"
			+ " FROM receipt AS r"
			+ "			 JOIN invoice AS i ON i.invoice_id = r.invoice_id"
			+ "			 JOIN client_profile AS cp ON cp.client_id = i.client_id"
			+ "",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClimap();
	
	@Query(value = "select r.receipt_id, r.balance, r.invoice_id, i.amount, r.payment_date, r.payment_type, r.received_amount, i.client_id, i.company_id,"
	        + "cp.address, cp.city, cp.client_name, cp.country, cp.email, cp.gender, cp.phone_number, cp.mobile_number, cp.state, cp.zip_code,"
	        + "c.company_name, c.email as companyemail, c.phone_number1, c.phone_number2, c.pincode, c.country as companycountry,"
	        + "c.address as companyaddress from receipt as r"
	        + " join invoice as i on i.invoice_id = r.invoice_id"
	        + " join client_profile as cp on cp.client_id = i.client_id"
	        + " join company as c on c.company_id = i.company_id"
	        + " where r.payment_date between ?1 and ?2", nativeQuery = true)
	List<Map<String, Object>> getAllpromotionsBetweenDates(LocalDate startDate, LocalDate endDate);

	
	@Query(value=" SELECT "
			+ "    r.receipt_id as receiptId, r.balance, r.invoice_id as invoiceId, i.amount, r.payment_date as paymentDate, r.payment_type as paymentType, r.received_amount as receivedAmount,"
			+ " i.client_id as clientId, i.balance as invoiceBalance,  i.company_id as companyId,cp.client_name as clientName"
			+ " FROM receipt AS r"
			+ " JOIN invoice AS i ON i.invoice_id = r.invoice_id"
			+ " JOIN client_profile AS cp ON cp.client_id = i.client_id"
			+ " WHERE  YEAR(r.payment_date) = :year  AND i.balance > 1;"
			+ "",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAnd(@Param("year") String year);
	
	
	@Query(value=" SELECT "
			+ "    r.receipt_id as receiptId, r.balance, r.invoice_id as invoiceId, i.amount, r.payment_date as paymentDate, r.payment_type as paymentType, r.received_amount as receivedAmount,"
			+ " i.client_id as clientId, i.balance as invoiceBalance,  i.company_id as companyId,cp.client_name as clientName"
			+ " FROM receipt AS r"
			+ " JOIN invoice AS i ON i.invoice_id = r.invoice_id"
			+ " JOIN client_profile AS cp ON cp.client_id = i.client_id"
			+ " WHERE  monthname(r.payment_date) = :monthName  AND i.balance > 1;"
			+ "",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndMonth(@Param("monthName") String monthName);

	@Query(value=" SELECT "
			+ "    r.receipt_id as receiptId, r.balance , r.invoice_id as invoiceId, i.amount, r.payment_date as paymentDate, r.payment_type as paymentType, r.received_amount as receivedAmount,"
			+ " i.client_id as clientId, i.balance as invoiceBalance,  i.company_id as companyId,cp.client_name as clientName"
			+ "			 FROM receipt AS r"
			+ "			 JOIN invoice AS i ON i.invoice_id = r.invoice_id"
			+ "			 JOIN client_profile AS cp ON cp.client_id = i.client_id"
			+ " WHERE  YEAR(r.payment_date) = :year and i.client_id=:client_id  ;"
			+ "",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndyear(@Param("year") String year,@Param("client_id") long client_id);
	
	
	@Query(value=" SELECT "
			+ "    r.receipt_id as receiptId, r.balance, r.invoice_id as invoiceId, i.amount, r.payment_date as paymentDate, r.payment_type as paymentType, r.received_amount as receivedAmount,"
			+ " i.client_id as clientId, i.balance as invoiceBalance,  i.company_id as companyId,cp.client_name as clientName"
			+ "			 FROM receipt AS r"
			+ "			 JOIN invoice AS i ON i.invoice_id = r.invoice_id"
			+ "			 JOIN client_profile AS cp ON cp.client_id = i.client_id"
			+ " WHERE  monthname(r.payment_date) = :monthName and i.client_id=:client_id "
			+ "",nativeQuery = true)
	List<Map<String, Object>> getAllReceiptDetailsWithClientIdAndMonthyear(@Param("monthName") String monthName,@Param("client_id") long client_id);


	Optional<Receipts> findByInvoiceId(Long invoiceId);

	@Query(value=" SELECT "
			+ "    r.receipt_id as receiptId, r.balance, r.invoice_id as invoiceId, i.amount, r.payment_date as paymentDate, r.payment_type as paymentType, r.received_amount as receivedAmount,"
			+ " i.client_id as clientId, i.balance as invoiceBalance,  i.company_id as companyId,cp.client_name as clientName"
			+ "			 FROM receipt AS r"
			+ "			 JOIN invoice AS i ON i.invoice_id = r.invoice_id"
			+ "			 JOIN client_profile AS cp ON cp.client_id = i.client_id"
			+ " WHERE  monthname(r.payment_date) = :monthName and i.client_id=:client_id "
			+ "",nativeQuery = true)
	List<Map<String, Object>> ALLCount();


	

	

}
