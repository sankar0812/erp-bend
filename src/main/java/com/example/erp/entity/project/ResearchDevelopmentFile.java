package com.example.erp.entity.project;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "researchDevelopmentFile")
public class ResearchDevelopmentFile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long researchDevelopmentFileId;
	private long projectId;
	private long employeeId;
	private String url;
	private LocalDate date;
	private String projectStatus;
	private boolean accepted;
	private boolean rejected;
	private long clientId;

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public long getResearchDevelopmentFileId() {
		return researchDevelopmentFileId;
	}

	public void setResearchDevelopmentFileId(long researchDevelopmentFileId) {
		this.researchDevelopmentFileId = researchDevelopmentFileId;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public boolean isAccepted() {
		return accepted;
	}

	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}

	public boolean isRejected() {
		return rejected;
	}

	public void setRejected(boolean rejected) {
		this.rejected = rejected;
	}

	public ResearchDevelopmentFile() {
		super();
	}

}
