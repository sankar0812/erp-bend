package com.example.erp.entity.employee;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="contacts")
public class EmergencyContacts {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long emergencyContactsId;
	private Long employeeId;
	private String relatinoName;
	private String address;
	private Long phoneNumber;
	private boolean status;
	
	private long roleId;
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	private long userId;
	
	
	
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	
	
	
	
	public Long getEmergencyContactsId() {
		return emergencyContactsId;
	}
	public void setEmergencyContactsId(Long emergencyContactsId) {
		this.emergencyContactsId = emergencyContactsId;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public String getRelatinoName() {
		return relatinoName;
	}
	public void setRelatinoName(String relatinoName) {
		this.relatinoName = relatinoName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(Long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public EmergencyContacts() {
		super();
	}
	
	
	
	
}
