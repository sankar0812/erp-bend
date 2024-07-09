package com.example.erp.entity.employee;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "multi_leaves")
public class EmployeeLeaveList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long employeeLeaveListId;
	private Date allDates;


	public long getEmployeeLeaveListId() {
		return employeeLeaveListId;
	}

	public void setEmployeeLeaveListId(long employeeLeaveListId) {
		this.employeeLeaveListId = employeeLeaveListId;
	}


	public Date getAllDates() {
		return allDates;
	}

	public void setAllDates(Date allDates) {
		this.allDates = allDates;
	}

	public EmployeeLeaveList() {
		super();
	}

}
