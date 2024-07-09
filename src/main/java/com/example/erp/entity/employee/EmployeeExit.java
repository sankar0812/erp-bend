package com.example.erp.entity.employee;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="employeeexit")
public class EmployeeExit {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long employeeExitId;
	private Long employeeId;
	private Long departmentId;
	private  Date date;
	   @Column(columnDefinition = "TEXT")
	private String description;
	private boolean status;
//	private String exitReason;
//	private String companyProperty;
//	private Date endDate ;
	private String exitType;
//	private LocalDate inDate = LocalDate.now();
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_employeeExitId", referencedColumnName = "employeeExitId")
	private List<CompanyPropertyList> CompanyProperty;
	
	
	
	public String getExitType() {
		return exitType;
	}
	public void setExitType(String exitType) {
		this.exitType = exitType;
	}
	public Long getEmployeeExitId() {
		return employeeExitId;
	}
	public void setEmployeeExitId(Long employeeExitId) {
		this.employeeExitId = employeeExitId;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
//	public String getExitReason() {
//		return exitReason;
//	}
//	public void setExitReason(String exitReason) {
//		this.exitReason = exitReason;
//	}
//	public Date getEndDate() {
//		return endDate;
//	}
//	public void setEndDate(Date endDate) {
//		this.endDate = endDate;
//	}
//	public String getExitType() {
//		return exitType;
//	}
//	public void setExitType(String exitType) {
//		this.exitType = exitType;
//	}
//	public LocalDate getInDate() {
//		return inDate;
//	}
//	public void setInDate(LocalDate inDate) {
//		this.inDate = inDate;
//	}
	public List<CompanyPropertyList> getCompanyProperty() {
		return CompanyProperty;
	}
	public void setCompanyProperty(List<CompanyPropertyList> companyProperty) {
		CompanyProperty = companyProperty;
	}

	
	
	
	  
	
	
}
