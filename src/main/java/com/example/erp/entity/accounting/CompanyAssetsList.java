package com.example.erp.entity.accounting;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "companyAssetsList")
public class CompanyAssetsList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long companyAssetsListId;
	private int count;
	private Long brandId;
	private Long accessoriesId;
	public Long getCompanyAssetsListId() {
		return companyAssetsListId;
	}
	public void setCompanyAssetsListId(Long companyAssetsListId) {
		this.companyAssetsListId = companyAssetsListId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
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
	public CompanyAssetsList() {
		super();
	}
	
	
	

}
