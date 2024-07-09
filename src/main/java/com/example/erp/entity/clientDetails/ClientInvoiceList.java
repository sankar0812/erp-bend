package com.example.erp.entity.clientDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "invoice_list")
public class ClientInvoiceList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long invoiceListId;
	private long projectId;
//	private String projectName;
	private int quantity;
	private double price;
	private double discountPercentage;
	private double taxIncludePrice;
	private int gst;
	private double taxQuantityAmount;
	private double totalTaxAmount;
	private double amount;
	private double discountAmount;

//	public String getProjectName() {
//		return projectName;
//	}
//
//	public void setProjectName(String projectName) {
//		this.projectName = projectName;
//	}

	public double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public long getInvoiceListId() {
		return invoiceListId;
	}

	public void setInvoiceListId(long invoiceListId) {
		this.invoiceListId = invoiceListId;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public double getTaxIncludePrice() {
		return taxIncludePrice;
	}

	public void setTaxIncludePrice(double taxIncludePrice) {
		this.taxIncludePrice = taxIncludePrice;
	}

	public int getGst() {
		return gst;
	}

	public void setGst(int gst) {
		this.gst = gst;
	}

	public double getTaxQuantityAmount() {
		return taxQuantityAmount;
	}

	public void setTaxQuantityAmount(double taxQuantityAmount) {
		this.taxQuantityAmount = taxQuantityAmount;
	}

	public double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public ClientInvoiceList() {
		super();
	}
}
