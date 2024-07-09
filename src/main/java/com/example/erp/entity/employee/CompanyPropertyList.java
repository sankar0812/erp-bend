package com.example.erp.entity.employee;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="companyPropertyList")
public class CompanyPropertyList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long companyPropertylistId;
	private long brandId;
	private long accessoriesId;
	private int count;
	private int returnCount;
	private int balanceCount;
	private String serialNumber;	 
	private int balanceCountt;
	
	
	
	
	

	public int getBalanceCountt() {
		return balanceCountt;
	}
	public void setBalanceCountt(int balanceCountt) {
		this.balanceCountt = balanceCountt;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public int getBalanceCount() {
		return balanceCount;
	}
	public void setBalanceCount(int balanceCount) {
		this.balanceCount = balanceCount;
	}
	public int getReturnCount() {
		return returnCount;
	}
	public void setReturnCount(int returnCount) {
		this.returnCount = returnCount;
	}
	public long getCompanyPropertylistId() {
		return companyPropertylistId;
	}
	public void setCompanyPropertylistId(long companyPropertylistId) {
		this.companyPropertylistId = companyPropertylistId;
	}
	public long getBrandId() {
		return brandId;
	}
	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}
	public long getAccessoriesId() {
		return accessoriesId;
	}
	public void setAccessoriesId(long accessoriesId) {
		this.accessoriesId = accessoriesId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	
	

}
