package com.example.erp.entity.project;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "researchQuotation")
public class ResearchQuotation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long researchQuotationId;
	private long projectId;
	private long userId;
	private String url;
	private String projectStatus;
	private boolean accepted;
	private boolean rejected;
	private LocalDate date;
	private long clientId;
	private long roleId;

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

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

	public long getResearchQuotationId() {
		return researchQuotationId;
	}

	public void setResearchQuotationId(long researchQuotationId) {
		this.researchQuotationId = researchQuotationId;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

//	public Blob getFileUpload() {
//		return fileUpload;
//	}
//
//	public void setFileUpload(Blob fileUpload) {
//		this.fileUpload = fileUpload;
//	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public ResearchQuotation() {
		super();
	}

}
