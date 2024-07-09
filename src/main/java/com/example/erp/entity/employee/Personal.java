package com.example.erp.entity.employee;


import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "personal")
public class Personal {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long personalId;
	private String passportNo;
	private Date passportExpDate;
	private String nationality;
	private String married;
	private String religion;
	private String tel;
	private long noOfChildren;
	private long employeeId;
	private boolean status;	
	private long roleId;
	private long userId;
	
	
	
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}
	public Long getPersonalId() {
		return personalId;
	}
	public void setPersonalId(Long personalId) {
		this.personalId = personalId;
	}
	public String getPassportNo() {
		return passportNo;
	}
	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}
	public Date getPassportExpDate() {
		return passportExpDate;
	}
	public void setPassportExpDate(Date passportExpDate) {
		this.passportExpDate = passportExpDate;
	}

	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getMarried() {
		return married;
	}
	public void setMarried(String married) {
		this.married = married;
	}
	public String getReligion() {
		return religion;
	}
	public void setReligion(String religion) {
		this.religion = religion;
	}

	public long getNoOfChildren() {
		return noOfChildren;
	}
	public void setNoOfChildren(long noOfChildren) {
		this.noOfChildren = noOfChildren;
	}


	
	
	
	

}
