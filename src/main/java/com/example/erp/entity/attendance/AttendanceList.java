package com.example.erp.entity.attendance;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "attendancelist")
public class AttendanceList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long attendanceListId;
	private boolean fullDay;
	private boolean halfDay;
	private boolean present;
	private boolean absent;
	private long employeeId;
	private boolean attstatus;
	private String section;
	private String intime;
	private String outtime;
	private Date date;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getAttendanceListId() {
		return attendanceListId;
	}

	public void setAttendanceListId(Long attendanceListId) {
		this.attendanceListId = attendanceListId;
	}

	public boolean isFullDay() {
		return fullDay;
	}

	public void setFullDay(boolean fullDay) {
		this.fullDay = fullDay;
	}

	public boolean isHalfDay() {
		return halfDay;
	}

	public void setHalfDay(boolean halfDay) {
		this.halfDay = halfDay;
	}

	public boolean isPresent() {
		return present;
	}

	public void setPresent(boolean present) {
		this.present = present;
	}

	public boolean isAbsent() {
		return absent;
	}

	public void setAbsent(boolean absent) {
		this.absent = absent;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
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

	public AttendanceList() {
		super();
	}

}
