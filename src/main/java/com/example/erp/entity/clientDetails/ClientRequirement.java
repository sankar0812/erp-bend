package com.example.erp.entity.clientDetails;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "clientRequirements")
public class ClientRequirement {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long projectId;
	private String projectName;
	private int duration;
	private Date date;
	private long clientId;
	private boolean status;
	private String projectStatus;
	private long projectTypeId;
    @Column(columnDefinition = "TEXT")
	private String skillsAndDescription;
    @Column(columnDefinition = "TEXT")
	private String features;
	private boolean rejected;
	private Date durationdate;
	private String roleType;
	
	
	
	
	
	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public Date getDurationdate() {
		return durationdate;
	}

	public void setDurationdate(Date durationdate) {
		this.durationdate = durationdate;
	}

	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public long getProjectTypeId() {
		return projectTypeId;
	}

	public void setProjectTypeId(long projectTypeId) {
		this.projectTypeId = projectTypeId;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public ClientRequirement() {
		super();
	}



}
