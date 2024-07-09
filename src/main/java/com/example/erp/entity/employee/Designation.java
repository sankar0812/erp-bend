package com.example.erp.entity.employee;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="designation")
public class Designation {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long designationId;
	private String designationName;
	
	public long getDesignationId() {
		return designationId;
	}

	public void setDesignationId(long designationId) {
		this.designationId = designationId;
	}

	public String getDesignationName() {
		return designationName;
	}

	public void setDesignationName(String designationName) {
		this.designationName = designationName;
	}

	public Designation() {
		super();
	}
	
	
	
	
	

}
