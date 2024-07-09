package com.example.erp.entity.project;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "departmentList")
public class DepartmentList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long departmentListId;
	private long departmentId;
	private long employeeId;

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public long getDepartmentListId() {
		return departmentListId;
	}

	public void setDepartmentListId(long departmentListId) {
		this.departmentListId = departmentListId;
	}

	public long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(long departmentId) {
		this.departmentId = departmentId;
	}

	public DepartmentList() {
		super();
	}

}
