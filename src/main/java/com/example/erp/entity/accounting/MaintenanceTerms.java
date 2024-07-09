package com.example.erp.entity.accounting;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "maintenanceTerms")
public class MaintenanceTerms {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long maintenanceTermsId;
	private String title;
    @Column(columnDefinition = "TEXT")
	private String terms;
	public Long getMaintenanceTermsId() {
		return maintenanceTermsId;
	}
	public void setMaintenanceTermsId(Long maintenanceTermsId) {
		this.maintenanceTermsId = maintenanceTermsId;
	}
	public String getTerms() {
		return terms;
	}
	public void setTerms(String terms) {
		this.terms = terms;
	}
	
	public MaintenanceTerms() {
		super();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	

}
