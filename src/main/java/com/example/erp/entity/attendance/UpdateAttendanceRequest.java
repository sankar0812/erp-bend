package com.example.erp.entity.attendance;

import java.sql.Date;

public class UpdateAttendanceRequest {
	private Long employeeId;
	private Date date;
	private boolean attstatus;
	private String section;
	private String intime;
	private String outtime;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isAttstatus() {
		return attstatus;
	}

	public void setAttstatus(boolean attstatus) {
		this.attstatus = attstatus;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getIntime() {
		return intime;
	}

	public void setIntime(String intime) {
		this.intime = intime;
	}

	public String getOuttime() {
		return outtime;
	}

	public void setOuttime(String outtime) {
		this.outtime = outtime;
	}

	public UpdateAttendanceRequest() {
		super();
	}

}
