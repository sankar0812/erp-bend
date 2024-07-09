package com.example.erp.entity.erecruitment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "preferredList")
public class Preferred {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long preferredId;
	   @Column(columnDefinition = "TEXT")
	private String  preferredSkills;
	public long getPreferredId() {
		return preferredId;
	}
	public void setPreferredId(long preferredId) {
		this.preferredId = preferredId;
	}
	public String getPreferredSkills() {
		return preferredSkills;
	}
	public void setPreferredSkills(String preferredSkills) {
		this.preferredSkills = preferredSkills;
	}
	
	
}
