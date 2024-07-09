package com.example.erp.entity.accounting;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="maintenanceList")
public class MaintenanceList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long maintenanceListId;
	private long projectId;
	private String Description;
	private double price;
	private double tax;
	private double taxAmount;
	private double subTotal;
	
	
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public long getMaintenanceListId() {
		return maintenanceListId;
	}
	public void setMaintenanceListId(long maintenanceListId) {
		this.maintenanceListId = maintenanceListId;
	}
	public long getProjectId() {
		return projectId;
	}
	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	public double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}
	public MaintenanceList() {
		super();
	}
	
	
	

}
