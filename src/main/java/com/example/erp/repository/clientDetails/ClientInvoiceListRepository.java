package com.example.erp.repository.clientDetails;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.erp.entity.clientDetails.ClientInvoice;
import com.example.erp.entity.clientDetails.ClientInvoiceList;

public interface ClientInvoiceListRepository  extends JpaRepository<ClientInvoiceList, Long>{
	
	
	Optional<ClientInvoiceList> findByProjectId(Long projectId);
	
	@Query(value=" select l.* from invoice_list as l"
			+ " join invoice as i on i.invoice_id=l.invoice_id"
			+ " where l.invoice_id=:id  ;"
			+ "",nativeQuery = true)
	Optional<ClientInvoiceList> findByProjectIdinconce(Long id);

}
