package com.example.erp.entity.accounting;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="maintenancePayment")
public class MaintenancePayment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long maintenancePaymentId;
	private long maintenanceInvoiceId;
	private Date paymentDate;
	private Date payDate;
	private double receivedAmount;
	private String paymentType;
	private long projectId;
	private double balance;
	private double amount;
	
	
	
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	public long getMaintenancePaymentId() {
		return maintenancePaymentId;
	}
	public void setMaintenancePaymentId(long maintenancePaymentId) {
		this.maintenancePaymentId = maintenancePaymentId;
	}
	public long getMaintenanceInvoiceId() {
		return maintenanceInvoiceId;
	}
	public void setMaintenanceInvoiceId(long maintenanceInvoiceId) {
		this.maintenanceInvoiceId = maintenanceInvoiceId;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public double getReceivedAmount() {
		return receivedAmount;
	}
	public void setReceivedAmount(double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public long getProjectId() {
		return projectId;
	}
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	
	
	
	
	
}
