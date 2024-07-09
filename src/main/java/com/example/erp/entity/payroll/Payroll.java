package com.example.erp.entity.payroll;

import java.sql.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "payroll")
public class Payroll {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long payrollId;
	private long companyId;
	private Date payrollDate;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "payrollId", referencedColumnName = "payrollId")
	private List<PayrollTypeList> payrollTypeList;

	public long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(long companyId) {
		this.companyId = companyId;
	}

	public long getPayrollId() {
		return payrollId;
	}

	public void setPayrollId(long payrollId) {
		this.payrollId = payrollId;
	}

	public Date getPayrollDate() {
		return payrollDate;
	}

	public void setPayrollDate(Date payrollDate) {
		this.payrollDate = payrollDate;
	}

	public List<PayrollTypeList> getPayrollTypeList() {
		return payrollTypeList;
	}

	public void setPayrollTypeList(List<PayrollTypeList> payrollTypeList) {
		this.payrollTypeList = payrollTypeList;
	}

	public Payroll() {
		super();
	}

}
