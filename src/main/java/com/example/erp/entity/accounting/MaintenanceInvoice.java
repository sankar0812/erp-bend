package com.example.erp.entity.accounting;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;



@Entity
@Table(name="maintenanceInvoice")
public class MaintenanceInvoice {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long maintenanceInvoiceId;
	private long companyId;
	private long clientId;
	private long invoiceNo;
	private Long maintenanceTermsId;
	private Date invoiceDate;
	private boolean status;
	private String serviceType;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "maintenanceInvoiceId", referencedColumnName = "maintenanceInvoiceId")
	private List<MaintenanceList> maintenanceList;
	private double total;

	
	
	
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}

	public Long getMaintenanceTermsId() {
		return maintenanceTermsId;
	}

	public void setMaintenanceTermsId(Long maintenanceTermsId) {
		this.maintenanceTermsId = maintenanceTermsId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public long getMaintenanceInvoiceId() {
		return maintenanceInvoiceId;
	}

	public void setMaintenanceInvoiceId(long maintenanceInvoiceId) {
		this.maintenanceInvoiceId = maintenanceInvoiceId;
	}

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public long getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(long invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public List<MaintenanceList> getMaintenanceList() {
		return maintenanceList;
	}

	public void setMaintenanceList(List<MaintenanceList> maintenanceList) {
		this.maintenanceList = maintenanceList;
	}
	
	
	

}
