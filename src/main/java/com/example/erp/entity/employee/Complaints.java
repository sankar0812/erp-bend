package com.example.erp.entity.employee;

import java.sql.Blob;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "complaints")
public class Complaints {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long complaintsId;
	private Long employeeId;
	private Long traineeId;
	private String complaintsTitle;
	private Date complaintsDate;
	private String complaintsAgainstName;
	private Long complaintsAgainst;
	   @Column(columnDefinition = "TEXT")
	private String description;
	private String url;
	@JsonIgnore
	private Blob attachments;
	private String Reason;
	private boolean status;
	private boolean imageStatus;
	private String attachmentsUrl;
	private String  fileName;
	
	
	
	
	
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAttachmentsUrl() {
		return attachmentsUrl;
	}

	public void setAttachmentsUrl(String attachmentsUrl) {
		this.attachmentsUrl = attachmentsUrl;
	}

	public boolean isImageStatus() {
		return imageStatus;
	}

	public void setImageStatus(boolean imageStatus) {
		this.imageStatus = imageStatus;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public long getComplaintsId() {
		return complaintsId;
	}

	public void setComplaintsId(long complaintsId) {
		this.complaintsId = complaintsId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getTraineeId() {
		return traineeId;
	}

	public void setTraineeId(Long traineeId) {
		this.traineeId = traineeId;
	}

	public String getComplaintsTitle() {
		return complaintsTitle;
	}

	public void setComplaintsTitle(String complaintsTitle) {
		this.complaintsTitle = complaintsTitle;
	}

	public Date getComplaintsDate() {
		return complaintsDate;
	}

	public void setComplaintsDate(Date complaintsDate) {
		this.complaintsDate = complaintsDate;
	}

	public String getComplaintsAgainstName() {
		return complaintsAgainstName;
	}

	public void setComplaintsAgainstName(String complaintsAgainstName) {
		this.complaintsAgainstName = complaintsAgainstName;
	}

	public Long getComplaintsAgainst() {
		return complaintsAgainst;
	}

	public void setComplaintsAgainst(Long complaintsAgainst) {
		this.complaintsAgainst = complaintsAgainst;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Blob getAttachments() {
		return attachments;
	}

	public void setAttachments(Blob attachments) {
		this.attachments = attachments;
	}

	public String getReason() {
		return Reason;
	}

	public void setReason(String reason) {
		Reason = reason;
	}

}
