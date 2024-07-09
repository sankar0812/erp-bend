package com.example.erp.entity.payroll;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "payroll_type")
public class PayrollTypeList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long payrollTypeId;
	private long employeeId;
	private long traineeId;
	private double deductions;
	private double payrollAmount;
	private double allowance;
	   @Column(columnDefinition = "TEXT")
	private String reason;
	private double netPay;
	private LocalDate paymentDate = LocalDate.now();

	
	
	
	public long getTraineeId() {
		return traineeId;
	}

	public void setTraineeId(long traineeId) {
		this.traineeId = traineeId;
	}

	public double getPayrollAmount() {
		return payrollAmount;
	}

	public void setPayrollAmount(double payrollAmount) {
		this.payrollAmount = payrollAmount;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public double getNetPay() {
		return netPay;
	}

	public void setNetPay(double netPay) {
		this.netPay = netPay;
	}

	public long getPayrollTypeId() {
		return payrollTypeId;
	}

	public void setPayrollTypeId(long payrollTypeId) {
		this.payrollTypeId = payrollTypeId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public double getDeductions() {
		return deductions;
	}

	public void setDeductions(double deductions) {
		this.deductions = deductions;
	}

	public double getAllowance() {
		return allowance;
	}

	public void setAllowance(double allowance) {
		this.allowance = allowance;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public PayrollTypeList() {
		super();
	}

}
