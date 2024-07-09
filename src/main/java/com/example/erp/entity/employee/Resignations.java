package com.example.erp.entity.employee;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="resignations")
public class Resignations {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long resignationsId;
	private String type;
	
	private String title;
	   @Column(columnDefinition = "TEXT")
	private String reason ;
	private Date resignationsDate;
	private long employeeId;
	private boolean status;
	private Date fromDate;
	private Date toDate;
	private boolean pending;
	private boolean approved;
	private String resignationsList ;
	
	
	
	
	
	public String getResignationsList() {
		return resignationsList;
	}
	public void setResignationsList(String resignationsList) {
		this.resignationsList = resignationsList;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isPending() {
		return pending;
	}
	public void setPending(boolean pending) {
		this.pending = pending;
	}
	public boolean isApproved() {
		return approved;
	}
	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	
	public long getResignationsId() {
		return resignationsId;
	}
	public void setResignationsId(long resignationsId) {
		this.resignationsId = resignationsId;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public Date getResignationsDate() {
		return resignationsDate;
	}
	public void setResignationsDate(Date resignationsDate) {
		this.resignationsDate = resignationsDate;
	}
	public long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}

	public Resignations() {
		super();
	}

	
	

}
