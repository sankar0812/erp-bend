package com.example.erp.entity.clientDetails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "projectType")
public class ProjectType {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long projectTypeId;
	private String projectType;
	private String color;

	public long getProjectTypeId() {
		return projectTypeId;
	}

	public void setProjectTypeId(long projectTypeId) {
		this.projectTypeId = projectTypeId;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public ProjectType() {
		super();
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	
}
