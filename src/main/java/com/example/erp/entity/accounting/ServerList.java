package com.example.erp.entity.accounting;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "serverList")
public class ServerList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long serverListId;
	private Long serverTypeId;
	private int amount;

	public Long getServerListId() {
		return serverListId;
	}
	public void setServerListId(Long serverListId) {
		this.serverListId = serverListId;
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

}
