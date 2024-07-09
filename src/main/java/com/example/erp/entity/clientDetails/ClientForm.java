package com.example.erp.entity.clientDetails;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "client_form")
public class ClientForm {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long clientFormId;
	private long clientId;
	private String projectName;
	private Date date;
    @Column(columnDefinition = "TEXT")
	private String skillsAndDescription;
	private String projectStatus;
    @Column(columnDefinition = "TEXT")
	private String features;
	private boolean status;
	private boolean rejected;

	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public long getClientFormId() {
		return clientFormId;
	}

	public void setClientFormId(long clientFormId) {
		this.clientFormId = clientFormId;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSkillsAndDescription() {
		return skillsAndDescription;
	}

	public void setSkillsAndDescription(String skillsAndDescription) {
		this.skillsAndDescription = skillsAndDescription;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public ClientForm() {
		super();
	}

}
