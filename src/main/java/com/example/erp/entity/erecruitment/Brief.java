package com.example.erp.entity.erecruitment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "briefList")
public class Brief {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long briefId;
	   @Column(columnDefinition = "TEXT")
	private String  briefDescription ;
	public long getBriefId() {
		return briefId;
	}
	public void setBriefId(long briefId) {
		this.briefId = briefId;
	}
	public String getBriefDescription() {
		return briefDescription;
	}
	public void setBriefDescription(String briefDescription) {
		this.briefDescription = briefDescription;
	}
	
}
