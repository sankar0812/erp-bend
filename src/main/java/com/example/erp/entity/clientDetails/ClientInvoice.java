package com.example.erp.entity.clientDetails;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "invoice")
public class ClientInvoice {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long invoiceId;
	private long companyId;
	private long clientId;
	private String gstType;
	private Date invoiceDate;
	@Column(length = 5000)
	private String description;
	private String paymentType;
	private double amount;
	private double received;
	private double balance;
	private double totalList;
	private double taxAmount;
	private double roundOffAmount;
	private double balanceAmount;
	private boolean status;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "invoiceId", referencedColumnName = "invoiceId")
	private List<ClientInvoiceList> invoiceList;

	
	
	
	public double getTotalList() {
		return totalList;
	}

	public void setTotalList(double totalList) {
		this.totalList = totalList;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getGstType() {
		return gstType;
	}

	public void setGstType(String gstType) {
		this.gstType = gstType;
	}

	public long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
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

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getReceived() {
		return received;
	}

	public void setReceived(double received) {
		this.received = received;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public double getRoundOffAmount() {
		return roundOffAmount;
	}

	public void setRoundOffAmount(double roundOffAmount) {
		this.roundOffAmount = roundOffAmount;
	}

	public double getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(double balanceAmount) {
		this.balanceAmount = balanceAmount;
	}

	public List<ClientInvoiceList> getInvoiceList() {
		return invoiceList;
	}

	public void setInvoiceList(List<ClientInvoiceList> invoiceList) {
		this.invoiceList = invoiceList;
	}

	public ClientInvoice() {
		super();
	}


}
