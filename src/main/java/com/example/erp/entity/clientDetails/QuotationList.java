package com.example.erp.entity.clientDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "quotation_list")
public class QuotationList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long quotationListId;
	   @Column(columnDefinition = "TEXT")
	private String termsAndCondition;
	   @Column(columnDefinition = "TEXT")
	private String additionalNotes;
	private long projectId;
	private int quantity;
	private double rate;
	private double amount;
 @Column(columnDefinition = "TEXT")
	private String description;
	public long getQuotationListId() {
		return quotationListId;
	}
	public void setQuotationListId(long quotationListId) {
		this.quotationListId = quotationListId;
	}
	public String getTermsAndCondition() {
		return termsAndCondition;
	}
	public void setTermsAndCondition(String termsAndCondition) {
		this.termsAndCondition = termsAndCondition;
	}
	public String getAdditionalNotes() {
		return additionalNotes;
	}
	public void setAdditionalNotes(String additionalNotes) {
		this.additionalNotes = additionalNotes;
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
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public QuotationList() {
		super();
	}

	
}
