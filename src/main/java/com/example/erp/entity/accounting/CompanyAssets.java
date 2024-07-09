package com.example.erp.entity.accounting;

import java.sql.Blob;
import java.sql.Date;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "company_assets")
public class CompanyAssets {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long companyAssetsId;
	private Date date;
	@JsonIgnore
	private Blob billing;
	private int assetValues;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "companyAssetsId", referencedColumnName = "companyAssetsId")
	private List<CompanyAssetsList> companyAssetsType;
	private boolean status;
	@Column(columnDefinition = "MEDIUMTEXT")
	private String url;

	public String getUrl() {
		return url;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Long getCompanyAssetsId() {
		return companyAssetsId;
	}

	public void setCompanyAssetsId(Long companyAssetsId) {
		this.companyAssetsId = companyAssetsId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Blob getBilling() {
		return billing;
	}

	public void setBilling(Blob billing) {
		this.billing = billing;
	}

	public int getAssetValues() {
		return assetValues;
	}

	public void setAssetValues(int assetValues) {
		this.assetValues = assetValues;
	}

	public List<CompanyAssetsList> getCompanyAssetsType() {
		return companyAssetsType;
	}

	public void setCompanyAssetsType(List<CompanyAssetsList> companyAssetsType) {
		this.companyAssetsType = companyAssetsType;
	}

	public CompanyAssets() {
		super();
	}

}
