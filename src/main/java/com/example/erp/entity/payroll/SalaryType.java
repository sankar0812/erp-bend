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
@Table(name = "salary_type")
public class SalaryType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long salaryTypeId;
	private String enteredBy;
	private Date salaryDate;
	

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "salaryTypeId", referencedColumnName = "salaryTypeId")
	private List<SalaryTypeList> salaryTypeList;
	
	

	public String getEnteredBy() {
		return enteredBy;
	}

	public void setEnteredBy(String enteredBy) {
		this.enteredBy = enteredBy;
	}
	
	public Date getSalaryDate() {
		return salaryDate;
	}
	
	public void setSalaryDate(Date salaryDate) {
		this.salaryDate = salaryDate;
	}

	public long getSalaryTypeId() {
		return salaryTypeId;
	}

	public void setSalaryTypeId(long salaryTypeId) {
		this.salaryTypeId = salaryTypeId;
	}

	public List<SalaryTypeList> getSalaryTypeList() {
		return salaryTypeList;
	}

	public void setSalaryTypeList(List<SalaryTypeList> salaryTypeList) {
		this.salaryTypeList = salaryTypeList;
	}

	public SalaryType() {
		super();
	}

}
