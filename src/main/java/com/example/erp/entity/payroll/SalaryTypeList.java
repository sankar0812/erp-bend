package com.example.erp.entity.payroll;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "salary_type_list")
public class SalaryTypeList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long salaryTypeListId;
	private long employeeId;
	private long traineeId;
	private double salaryAmount;
	private LocalDate salaryDate = LocalDate.now();
	private Date updatingDate;

	

	
	public long getTraineeId() {
		return traineeId;
	}

	public void setTraineeId(long traineeId) {
		this.traineeId = traineeId;
	}

	public Date getUpdatingDate() {
		return updatingDate;
	}

	public void setUpdatingDate(Date updatingDate) {
		this.updatingDate = updatingDate;
	}

	public LocalDate getSalaryDate() {
		return salaryDate;
	}

	public void setSalaryDate(LocalDate salaryDate) {
		this.salaryDate = salaryDate;
	}

	public long getSalaryTypeListId() {
		return salaryTypeListId;
	}

	public void setSalaryTypeListId(long salaryTypeListId) {
		this.salaryTypeListId = salaryTypeListId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public double getSalaryAmount() {
		return salaryAmount;
	}

	public void setSalaryAmount(double salaryAmount) {
		this.salaryAmount = salaryAmount;
	}

	public SalaryTypeList() {
		super();
	}

}
