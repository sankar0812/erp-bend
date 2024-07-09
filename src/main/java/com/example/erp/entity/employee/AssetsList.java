package com.example.erp.entity.employee;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "assetsList")
public class AssetsList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long assetsListId;
	private Long brandId;
	private Long accessoriesId;
	private int count;
	private String serialNumber;	
	private int balanceCount;
	private int returncount;
	
	

	public int getBalanceCount() {
		return balanceCount;
	}
	public void setBalanceCount(int balanceCount) {
		this.balanceCount = balanceCount;
	}
	public int getReturncount() {
		return returncount;
	}
	public void setReturncount(int returncount) {
		this.returncount = returncount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Long getAssetsListId() {
		return assetsListId;
	}

	public void setAssetsListId(Long assetsListId) {
		this.assetsListId = assetsListId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Long getAccessoriesId() {
		return accessoriesId;
	}

	public void setAccessoriesId(Long accessoriesId) {
		this.accessoriesId = accessoriesId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public AssetsList() {
		super();
	}

}
