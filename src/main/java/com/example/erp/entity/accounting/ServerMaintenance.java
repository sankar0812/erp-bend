package com.example.erp.entity.accounting;

import java.sql.Blob;
import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="serverMaintenance")
public class ServerMaintenance {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long serverMaintenanceId;
	private Long serverTypeId;
	private int amount;
	@JsonIgnore
	private Blob serverBilling;
	private boolean status;
	private Date date;
	private String url;
	
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public Long getServerMaintenanceId() {
		return serverMaintenanceId;
	}
	public void setServerMaintenanceId(Long serverMaintenanceId) {
		this.serverMaintenanceId = serverMaintenanceId;
	}
	public Long getServerTypeId() {
		return serverTypeId;
	}
	public void setServerTypeId(Long serverTypeId) {
		this.serverTypeId = serverTypeId;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Blob getServerBilling() {
		return serverBilling;
	}
	public void setServerBilling(Blob serverBilling) {
		this.serverBilling = serverBilling;
	}
	
	
	
}
